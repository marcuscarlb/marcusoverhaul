package net.marcus.overhaul.block;

import net.marcus.overhaul.item.StoneKnappingItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StoneKnappingBlock extends Block {
    public static final IntProperty PLACEMENT_COUNT = IntProperty.of("placement_count", 1, 3);  // Tracks placements
    private static final VoxelShape[] SHAPES = {
            Block.createCuboidShape(6, 0, 6, 10, 4, 10),  // Shape for 1 placement
            Block.createCuboidShape(4, 0, 4, 12, 6, 12),  // Shape for 2 placements
            Block.createCuboidShape(2, 0, 2, 14, 8, 14)   // Shape for 3 placements
    };

    public StoneKnappingBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(PLACEMENT_COUNT, 1));  // Default to 1 placement
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // Get the item the player is holding
        ItemStack heldItem = player.getMainHandStack();

        // Only allow placement if the block is not full (i.e., placement count < 3)
        if (!player.isCreative()) {
            int currentCount = state.get(PLACEMENT_COUNT);  // Get current placement count

            // If player is holding a StoneKnappingItem and placement count is less than 3
            if (heldItem.getItem() instanceof StoneKnappingItem && currentCount < 3) {
                heldItem.decrement(1);  // Remove 1 StoneKnappingItem from the player's inventory
                world.setBlockState(pos, state.with(PLACEMENT_COUNT, currentCount + 1));  // Increase placement count
                world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return ActionResult.SUCCESS;  // Item placed successfully
            }
            if (!(heldItem.getItem() instanceof StoneKnappingItem)) {
                // If player is interacting to break the block (remove items)
                if (currentCount > 0) {
                    // Drop the correct number of StoneKnappingItems based on the placement count
                    for (int i = 0; i < currentCount; i++) {
                        Block.dropStack(world, pos, new ItemStack(this.asItem()));  // Drop the item
                    }

                    // Remove the block entirely after breaking it
                    world.removeBlock(pos, false);  // Removes the block
                    world.playSound(null, pos, BlockSoundGroup.DEEPSLATE.getBreakSound(), SoundCategory.BLOCKS, 1.0f, 1.0f);
                    return ActionResult.SUCCESS;  // Block removed and items dropped
                }
            }
        }

        return ActionResult.PASS;  // No action if conditions aren't met
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PLACEMENT_COUNT);  // Add the placement count property to the block state
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(PLACEMENT_COUNT) - 1];  // Return the correct shape based on the placement count
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.isCreative()) {
            int currentCount = state.get(PLACEMENT_COUNT);

            // Drop the correct number of StoneKnappingItems based on the placement count
            for (int i = 0; i < currentCount; i++) {
                Block.dropStack(world, pos, new ItemStack(this.asItem()));  // Drop the item
            }
        }

        // Call the super method to handle the block breaking
        return super.onBreak(world, pos, state, player);
    }
}
