package net.marcus.overhaul.util.blockentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface GuiHandler {

    void open(PlayerEntity player, BlockPos pos, World world);
}
