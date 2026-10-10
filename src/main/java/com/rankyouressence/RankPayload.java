package com.rankyouressence;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record RankPayload(UUID player, int rank) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RankPayload> TYPE = CustomPacketPayload.createType("rankyouressence_sync");

    public static final StreamCodec<FriendlyByteBuf, RankPayload> CODEC = StreamCodec.of(
            (buf, p) -> { buf.writeUUID(p.player); buf.writeVarInt(p.rank); },
            buf -> new RankPayload(buf.readUUID(), buf.readVarInt()));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
