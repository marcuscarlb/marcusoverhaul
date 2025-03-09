package net.marcus.overhaul.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.marcus.overhaul.MarcusMinecraftOverhaul;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block EXAMPLE_BLOCK = registerBlock("example_block",
            new StoneKnappingBlock(FabricBlockSettings.create().hardness(0.5f).noCollision()));

    public static final Block LOOSE_ROCK_BLOCK = registerBlock("loose_rock",
            new StoneKnappingBlock(FabricBlockSettings.create().hardness(0.5f).noCollision()));

    public static final Block FIRE_PIT_BLOCK = registerBlock("fire_pit_block",
            new FirePitBlock(FabricBlockSettings.create()));

    public static final Block CUSTOM_BLOCK = registerBlock("custom_block",
            new Block(FabricBlockSettings.create()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(MarcusMinecraftOverhaul.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(MarcusMinecraftOverhaul.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
    }
}
