package net.marcus.overhaul.block.entity;

import net.marcus.overhaul.MarcusMinecraftOverhaul;
import net.marcus.overhaul.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MarcusMinecraftOverhaul.MOD_ID, path), blockEntityType);
    }
    public static final BlockEntityType<FirePitBlockEntity> FIRE_PIT = register("fire_pit",
            BlockEntityType.Builder.create(FirePitBlockEntity::new, ModBlocks.FIRE_PIT_BLOCK).build());

    public static void registerBlockEntities(){
    }
}
