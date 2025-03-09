package net.marcus.overhaul.util.screen;

import net.minecraft.item.Item;

public class KnappingPattern {
    private final boolean[][] pattern;
    private final Item result;

    public KnappingPattern(boolean[][] pattern, Item result) {
        this.pattern = pattern;
        this.result = result;
    }
    public boolean[][] getPattern() {
        return pattern;
    }
    public Item getResult() {
        return result;
    }
}
