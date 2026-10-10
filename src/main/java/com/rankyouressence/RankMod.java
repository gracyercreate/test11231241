package com.rankyouressence;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class RankMod implements ModInitializer {
    public static final String MOD_ID = "rankyouressence";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(RankPayload.TYPE, RankPayload.CODEC);

        ServerLifecycleEvents.SERVER_STARTED.register(RankStore::load);
        ServerLifecycleEvents.SERVER_STOPPING.register(RankStore::save);

        // Send all known ranks to the joining player.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            for (Map.Entry<UUID, Rank> e : RankStore.SERVER.entrySet()) {
                sender.sendPacket(new RankPayload(e.getKey(), e.getValue().ordinal()));
            }
            // Everyone has BRONZE by default; make sure the new player's icon is announced too.
            UUID id = handler.getPlayer().getUUID();
            Rank r = RankStore.get(RankStore.SERVER, id);
            RankPayload payload = new RankPayload(id, r.ordinal());
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                ServerPlayNetworking.send(p, payload);
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) ->
                dispatcher.register(Commands.literal("setrunk")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("rank", StringArgumentType.word())
                                        .suggests((ctx, b) -> {
                                            for (Rank r : Rank.values()) {
                                                if (r.id().startsWith(b.getRemainingLowerCase())) b.suggest(r.id());
                                            }
                                            return b.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                                            String name = StringArgumentType.getString(ctx, "rank");
                                            Rank rank = Rank.byId(name);
                                            if (rank == null) {
                                                ctx.getSource().sendFailure(Component.literal(
                                                        "Unknown rank. Available: bronze, silver, gold, platinum, amethyst, diamond, emerald, ruby"));
                                                return 0;
                                            }
                                            RankStore.SERVER.put(target.getUUID(), rank);
                                            RankStore.save(ctx.getSource().getServer());
                                            RankPayload payload = new RankPayload(target.getUUID(), rank.ordinal());
                                            for (ServerPlayer p : ctx.getSource().getServer().getPlayerList().getPlayers()) {
                                                ServerPlayNetworking.send(p, payload);
                                            }
                                            ctx.getSource().sendSuccess(() -> Component.literal(
                                                    "Rank of " + target.getName().getString() + " set to " + rank.id()), true);
                                            return 1;
                                        })))));
    }
}
