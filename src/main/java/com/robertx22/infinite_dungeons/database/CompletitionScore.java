package com.robertx22.infinite_dungeons.database;

import net.minecraft.util.text.TextFormatting;

public enum CompletitionScore {
    GOLD(TextFormatting.GOLD, 2),
    SILVER(TextFormatting.GREEN, 1),
    BRONZE(TextFormatting.WHITE, 0),
    FAIL(TextFormatting.RED, -1);

    public TextFormatting format;
    public int tier;

    CompletitionScore(TextFormatting format, int tier) {
        this.format = format;
        this.tier = tier;
    }
}
