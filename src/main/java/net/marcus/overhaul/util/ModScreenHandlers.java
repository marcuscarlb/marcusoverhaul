package net.marcus.overhaul.util;

import net.marcus.overhaul.MarcusMinecraftOverhaul;
import net.marcus.overhaul.item.ModItems;
import net.marcus.overhaul.util.screen.KnappingScreenHandler;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final Item KNAPPING_ITEM = ModItems.LOOSE_ROCK;

    public static final ScreenHandlerType<KnappingScreenHandler> KNAPPING_SCREEN_HANDLER = new ScreenHandlerType<>((syncId, playerInventory) -> new KnappingScreenHandler(syncId, playerInventory, KNAPPING_ITEM), FeatureSet.empty());


    public static void register() {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(MarcusMinecraftOverhaul.MOD_ID, "knapping_screen"), KNAPPING_SCREEN_HANDLER);
    }
}
