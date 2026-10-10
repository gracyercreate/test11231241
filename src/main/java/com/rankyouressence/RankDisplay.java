package com.rankyouressence;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class RankDisplay {
    private RankDisplay() {}

    public static Component decorate(Entity self, Component original) {
        if (!(self instanceof Player player)) return original;
        String s = original.getString();
        if (!s.isEmpty()) {
            char c = s.charAt(0);
            if (c >= 0xE000 && c < 0xE000 + Rank.values().length) return original; // already has icon
        }
        Rank rank = RankStore.get(self.level().isClientSide() ? RankStore.CLIENT : RankStore.SERVER, player.getUUID());
        return Component.literal(rank.glyph() + " ").append(original);
    }
}
