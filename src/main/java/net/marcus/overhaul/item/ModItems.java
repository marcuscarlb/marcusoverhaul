package net.marcus.overhaul.item;

import net.marcus.overhaul.MarcusMinecraftOverhaul;
import net.marcus.overhaul.block.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MarcusMinecraftOverhaul.MOD_ID, "item"));

    public static Item register(Item item, String id) {
        Identifier itemID = Identifier.of(MarcusMinecraftOverhaul.MOD_ID, id);
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
        return registeredItem;
    }

    public static final Item EXAMPLE_ITEM = register(new StoneKnappingItem(ModBlocks.EXAMPLE_BLOCK, new Item.Settings()), "example_item");
    public static final Item LOOSE_ROCK = register(new StoneKnappingItem(ModBlocks.LOOSE_ROCK_BLOCK, new Item.Settings()), "loose_rock");
    public static final Item FIRE_STARTER = register(new FireStarterItem(new Item.Settings()), "fire_starter");

    public static void registerModItems() {}
}
