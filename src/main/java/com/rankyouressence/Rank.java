package com.rankyouressence;

import java.util.Locale;

public enum Rank {
    BRONZE, SILVER, GOLD, PLATINUM, AMETHYST, DIAMOND, EMERALD, RUBY;

    public String glyph() {
        return String.valueOf((char) (0xE000 + ordinal()));
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static Rank byId(String s) {
        for (Rank r : values()) if (r.id().equalsIgnoreCase(s)) return r;
        return null;
    }
}
