package com.rankyouressence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Server-side rank storage (saved in the world folder). Missing entry = BRONZE. */
public final class RankStore {
    private static final Gson GSON = new Gson();
    public static final Map<UUID, Rank> SERVER = new HashMap<>();
    /** Client-side copy received from the server. */
    public static final Map<UUID, Rank> CLIENT = new HashMap<>();

    private RankStore() {}

    private static Path file(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve("rankyouressence.json");
    }

    public static void load(MinecraftServer server) {
        SERVER.clear();
        try {
            Path p = file(server);
            if (Files.exists(p)) {
                Map<String, String> raw = GSON.fromJson(Files.readString(p), new TypeToken<Map<String, String>>() {}.getType());
                if (raw != null) {
                    raw.forEach((k, v) -> {
                        Rank r = Rank.byId(v);
                        if (r != null) SERVER.put(UUID.fromString(k), r);
                    });
                }
            }
        } catch (Exception e) {
            RankMod.LOGGER.error("Failed to load ranks", e);
        }
    }

    public static void save(MinecraftServer server) {
        try {
            Map<String, String> raw = new HashMap<>();
            SERVER.forEach((k, v) -> raw.put(k.toString(), v.id()));
            Files.writeString(file(server), GSON.toJson(raw));
        } catch (Exception e) {
            RankMod.LOGGER.error("Failed to save ranks", e);
        }
    }

    public static Rank get(Map<UUID, Rank> map, UUID id) {
        return map.getOrDefault(id, Rank.BRONZE);
    }
}
