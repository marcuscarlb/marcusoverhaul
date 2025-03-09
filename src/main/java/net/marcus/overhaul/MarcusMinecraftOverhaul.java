package net.marcus.overhaul;

import net.fabricmc.api.ModInitializer;

import net.marcus.overhaul.block.ModBlocks;
import net.marcus.overhaul.block.entity.ModBlockEntities;
import net.marcus.overhaul.item.ModItems;
import net.marcus.overhaul.util.ModScreenHandlers;
import net.marcus.overhaul.util.networking.KnappingPacket;
import net.marcus.overhaul.util.screen.KnappingScreen;
import net.marcus.overhaul.world.worldgen.CustomChunkGen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarcusMinecraftOverhaul implements ModInitializer {

	public static final String MOD_ID = "marcusminecraftoverhaul";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModScreenHandlers.register();
		KnappingPacket.register();
		HandledScreens.register(ModScreenHandlers.KNAPPING_SCREEN_HANDLER, KnappingScreen::new);
		ModBlockEntities.registerBlockEntities();
		CustomChunkGen.init();
	}
}