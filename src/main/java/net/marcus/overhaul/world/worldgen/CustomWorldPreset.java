package net.marcus.overhaul.world.worldgen;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;

import java.util.Map;

public class CustomWorldPreset extends WorldPreset {
    public CustomWorldPreset(Map<RegistryKey<DimensionOptions>, DimensionOptions> dimensions) {
        super(dimensions);
    }
    
}
