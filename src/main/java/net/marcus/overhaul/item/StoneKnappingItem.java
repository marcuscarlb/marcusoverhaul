package net.marcus.overhaul.item;

import net.marcus.overhaul.block.StoneKnappingBlock;
import net.marcus.overhaul.util.screen.KnappingScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StoneKnappingItem extends BlockItem {
    public StoneKnappingItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    protected boolean place(ItemPlacementContext context, BlockState state) {

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState existingState = world.getBlockState(pos);

        // Check if the block beneath is solid (important for placement)
        if (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
            return false;
        }

        // Handle logic if there's already a `GroundPickups` block at the position
        if (existingState.getBlock() instanceof StoneKnappingBlock) {
            int placementCount = existingState.get(StoneKnappingBlock.PLACEMENT_COUNT);
            // Only allow placing the knapping item if the count is less than 3
            if (placementCount < 3) {
                // Update the block state with an increased placement count
                world.setBlockState(pos, existingState.with(StoneKnappingBlock.PLACEMENT_COUNT, placementCount + 1));
                return true;
            } else {
                return false; // Block placement restricted to 3
            }
        }

        // If it's not a `GroundPickups` block, place the block normally
        return world.setBlockState(pos, state);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.getCount() >= 2) {
            world.playSoundFromEntity(user, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            HitResult hitResult = this.getHitResult(user);
            if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.MISS) {
                if (!world.isClient) {
                    if (blockHitResult.getType() == HitResult.Type.MISS) {
                        if (user instanceof ServerPlayerEntity serverPlayer) {
                            serverPlayer.openHandledScreen(new SimpleNamedScreenHandlerFactory(((syncId, playerInventory, player) -> new KnappingScreenHandler(syncId, playerInventory, this)), Text.literal("Knapping")));
                        }
                    }
                    return TypedActionResult.success(stack);
                }
            }
        }
        return TypedActionResult.pass(stack);
    }
    private HitResult getHitResult(PlayerEntity user) {
        return ProjectileUtil.getCollision(user, entity -> !entity.isSpectator() && entity.canHit(), user.getBlockInteractionRange());
    }
}
