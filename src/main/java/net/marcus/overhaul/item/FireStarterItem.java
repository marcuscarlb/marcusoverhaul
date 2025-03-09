package net.marcus.overhaul.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class FireStarterItem extends Item {
    public FireStarterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        if (playerEntity != null && this.getHitResult(playerEntity).getType() == HitResult.Type.BLOCK) {
            playerEntity.setCurrentHand(context.getHand());
        }

        return ActionResult.CONSUME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks == 1 && user instanceof PlayerEntity playerEntity) {
            Hand hand = playerEntity.getActiveHand();
            HitResult hitResult = this.getHitResult(playerEntity);
            if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
                ItemUsageContext context = new ItemUsageContext(playerEntity, hand, blockHitResult);
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos blockPos2 = blockPos.offset(context.getSide());
                if (AbstractFireBlock.canPlaceAt(world, blockPos2, playerEntity.getHorizontalFacing())) {
                    BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
                    world.setBlockState(blockPos2, blockState2, Block.NOTIFY_ALL_AND_REDRAW);
                    world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
                    ItemStack itemStack = playerEntity.getStackInHand(hand);
                    if (playerEntity instanceof ServerPlayerEntity) {
                        Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos2, itemStack);
                        itemStack.damage(1, playerEntity, LivingEntity.getSlotForHand(playerEntity.getActiveHand()));
                    }
                }
            }
        }
    }
    private HitResult getHitResult(PlayerEntity user) {
        return ProjectileUtil.getCollision(user, entity -> !entity.isSpectator() && entity.canHit(), user.getBlockInteractionRange());
    }

}
