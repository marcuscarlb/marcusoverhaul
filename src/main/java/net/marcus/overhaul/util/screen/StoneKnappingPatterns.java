package net.marcus.overhaul.util.screen;

import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.List;

public class StoneKnappingPatterns {
    public static final List<KnappingPattern> PATTERNS = Arrays.asList(
            new KnappingPattern(
                    new boolean[][]{
                            {true, false, false, false, true},
                            {true, false, false, false, true},
                            {true, false, false, false, true},
                            {true, false, false, false, true},
                            {true, true, false, true, true}
                    },
                    Items.STONE
            ) ,
            new KnappingPattern(
                    new boolean[][]{
                            {true, false, false, false, false},
                            {true, false, false, false, false},
                            {true, false, false, false, false},
                            {true, false, false, false, false},
                            {true, false, false, false, false}
                    },
                    Items.STONE
            ),
            new KnappingPattern(
                    new boolean[][]{
                            {false, false, false, false, false},
                            {false, false, false, false, false},
                            {false, false, false, false, false},
                            {false, false, false, false, false},
                            {false, false, false, false, false}
                    },
                    Items.DIRT
            ));
}
