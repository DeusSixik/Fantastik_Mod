package net.lisalaf.fantastikmod.entity.ai;

import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class BakenekoGoal extends Goal {
    private final BakenekoEntity bakeneko;
    private final double speedModifier;
    private final float minDistance;
    private final float maxDistance;

    private Player followingPlayer;
    private Player targetPlayer;
    private int eatCooldown;
    private BlockPos shelterPos;
    private int shelterCooldown;
    private int panicTime;
    private BlockPos sittingPos;
    private BlockPos sleepingPos;
    private int sittingDuration;
    private int sleepingDuration;
    private int lastActionTime = 0;
    private int actionCooldown = 0;

    public BakenekoGoal(BakenekoEntity bakeneko, double speed, float minDist, float maxDist) {
        this.bakeneko = bakeneko;
        this.speedModifier = speed;
        this.minDistance = minDist;
        this.maxDistance = maxDist;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (bakeneko.isSitting() || bakeneko.isSleeping()) {
            return false;
        }

        if (bakeneko.isInWater() || bakeneko.isInLava()) {
            return false;
        }

        Level level = bakeneko.level();
        if (level == null) return false;

        if (actionCooldown > 0) {
            actionCooldown--;
            return false;
        }

        if (bakeneko.isSitting() || bakeneko.isSleeping()) {
            return false;
        }

        int currentTime = bakeneko.tickCount;
        int timeSinceLastAction = currentTime - lastActionTime;

        if (timeSinceLastAction > 2400) {
            return true;
        }

        return level.random.nextFloat() < 0.01f;
    }

    @Override
    public boolean canContinueToUse() {
        return bakeneko.isSitting() || bakeneko.isSleeping() || targetPlayer != null;
    }

    @Override
    public void start() {
        Level level = bakeneko.level();
        if (level == null) return;

        int timeSinceLastAction = bakeneko.tickCount - lastActionTime;

        if (timeSinceLastAction > 2400) {
            if (level.random.nextBoolean()) {
                tryFindSleepingSpot();
            } else {
                tryFindSittingSpot();
            }
        } else {
            float chance = level.random.nextFloat();

            if (chance < 0.4f) {
                tryFindSittingSpot();
            } else if (chance < 0.8f) {
                tryFindSleepingSpot();
            } else {
                tryFindFoodPlayer();
            }
        }

        lastActionTime = bakeneko.tickCount;
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
        this.followingPlayer = null;
        this.shelterPos = null;
        this.panicTime = 0;
        this.actionCooldown = 100 + bakeneko.level().random.nextInt(100);
    }

    @Override
    public void tick() {
        if (bakeneko == null || bakeneko.level() == null) return;
        if (bakeneko.isAngry() && bakeneko.getLastThief() != null) {
            targetPlayer = bakeneko.getLastThief();
        }

        if (bakeneko.isSitting()) {
            handleSitting();
            return;
        }

        if (bakeneko.isSleeping()) {
            handleSleeping();
            return;
        }

        if (targetPlayer != null) {
            handleFollowTarget();
            return;
        }
    }

    private void tryFindSittingSpot() {
        Level level = bakeneko.level();
        BlockPos currentPos = bakeneko.blockPosition();

        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos checkPos = currentPos.offset(x, y, z);

                    if (isGoodSittingSpot(checkPos)) {
                        sittingPos = checkPos;
                        sittingDuration = 400 + level.random.nextInt(800);
                        bakeneko.setSitting(true, checkPos);
                        bakeneko.startSittingAnimation();
                        bakeneko.getNavigation().moveTo(
                                checkPos.getX() + 0.5,
                                checkPos.getY(),
                                checkPos.getZ() + 0.5,
                                speedModifier
                        );
                        actionCooldown = 600;
                        return;
                    }
                }
            }
        }

        actionCooldown = 200;
    }

    private boolean isGoodSittingSpot(BlockPos pos) {
        Level level = bakeneko.level();
        BlockState state = level.getBlockState(pos);

        if (!state.isAir()) return false;

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (!belowState.isSolidRender(level, belowPos)) return false;

        Block block = belowState.getBlock();

        return block instanceof FurnaceBlock ||
                block instanceof ChestBlock ||
                block instanceof BedBlock ||
                block instanceof CraftingTableBlock ||
                block instanceof LecternBlock ||
                block instanceof HayBlock ||
                block instanceof WoolCarpetBlock ||
                isWarmBlock(belowPos) ||
                block == Blocks.GRASS_BLOCK ||
                block == Blocks.DIRT ||
                block == Blocks.STONE ;
    }

    private void handleSitting() {
        if (!bakeneko.isSitting()) {
            sittingPos = null;
            return;
        }

        sittingDuration--;

        if (sittingDuration <= 0) {
            endSitting();
            return;
        }

        if (sittingPos != null && bakeneko.distanceToSqr(
                sittingPos.getX() + 0.5, sittingPos.getY(), sittingPos.getZ() + 0.5) < 2.0) {
            bakeneko.getNavigation().stop();
        }

        if (shouldInterruptSitting()) {
            endSitting();
        }
    }

    private void endSitting() {
        bakeneko.setSitting(false, null);
        bakeneko.stopSittingSleepingAnimation();
        sittingPos = null;
        sittingDuration = 0;
        actionCooldown = 400;
    }

    private boolean shouldInterruptSitting() {
        Level level = bakeneko.level();
        return level.isRaining() || bakeneko.isAngry() ||
                (targetPlayer != null && bakeneko.distanceToSqr(targetPlayer) < 16.0);
    }

    private void tryFindSleepingSpot() {
        Level level = bakeneko.level();
        BlockPos currentPos = bakeneko.blockPosition();

        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos checkPos = currentPos.offset(x, y, z);

                    if (isGoodSleepingSpot(checkPos)) {
                        sleepingPos = checkPos;
                        sleepingDuration = 600 + level.random.nextInt(1200);
                        bakeneko.setSleeping(true, checkPos);
                        bakeneko.startSleepingAnimation();
                        bakeneko.getNavigation().moveTo(
                                checkPos.getX() + 0.5,
                                checkPos.getY(),
                                checkPos.getZ() + 0.5,
                                speedModifier
                        );
                        actionCooldown = 800;
                        return;
                    }
                }
            }
        }

        actionCooldown = 200;
    }

    private boolean isGoodSleepingSpot(BlockPos pos) {
        Level level = bakeneko.level();
        BlockState state = level.getBlockState(pos);

        if (!state.isAir()) return false;

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (!belowState.isSolidRender(level, belowPos)) return false;

        Block block = belowState.getBlock();

        if (block instanceof BedBlock) return true;
        if (block instanceof HayBlock) return true;
        if (block instanceof WoolCarpetBlock) return true;

        if (isWarmBlock(belowPos)) return true;

        if (isSunnySpot(pos)) return true;

        return false;
    }

    private boolean isWarmBlock(BlockPos pos) {
        Level level = bakeneko.level();

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    FluidState fluidState = level.getFluidState(checkPos);

                    if (state.getBlock() instanceof FurnaceBlock) {
                        if (state.getValue(FurnaceBlock.LIT)) {
                            return true;
                        }
                    }

                    if (state.getBlock() instanceof CampfireBlock) {
                        if (state.getValue(CampfireBlock.LIT)) {
                            return true;
                        }
                    }

                    if (state.getBlock() instanceof TorchBlock) return true;
                    if (state.getBlock() instanceof LanternBlock) return true;

                    if (fluidState.getType() == Fluids.LAVA) {
                        if (canFeelHeatThroughBlock(checkPos)) {
                            return true;
                        }
                    }

                    if (state.getBlock() instanceof FireBlock) return true;
                }
            }
        }
        return false;
    }

    private boolean canFeelHeatThroughBlock(BlockPos lavaPos) {
        Level level = bakeneko.level();
        BlockPos abovePos = lavaPos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        if (aboveState.isAir()) return true;

        Block block = aboveState.getBlock();
        if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) {
            return false;
        }

        return true;
    }

    private boolean isSunnySpot(BlockPos pos) {
        Level level = bakeneko.level();
        return level.isDay() && level.canSeeSky(pos) && !level.isRainingAt(pos);
    }

    private void handleSleeping() {
        if (!bakeneko.isSleeping()) {
            sleepingPos = null;
            return;
        }

        sleepingDuration--;

        if (sleepingDuration <= 0) {
            endSleeping();
            return;
        }

        if (sleepingPos != null && bakeneko.distanceToSqr(
                sleepingPos.getX() + 0.5, sleepingPos.getY(), sleepingPos.getZ() + 0.5) < 2.0) {
            bakeneko.getNavigation().stop();
        }

        if (shouldInterruptSleeping()) {
            endSleeping();
        }
    }

    private void endSleeping() {
        bakeneko.setSleeping(false, null);
        bakeneko.stopSittingSleepingAnimation();
        sleepingPos = null;
        sleepingDuration = 0;
        actionCooldown = 600;
    }

    private boolean shouldInterruptSleeping() {
        Level level = bakeneko.level();
        return level.isRaining() || bakeneko.isAngry() ||
                (targetPlayer != null && bakeneko.distanceToSqr(targetPlayer) < 25.0);
    }

    private void tryFindFoodPlayer() {
        Player player = bakeneko.level().getNearestPlayer(bakeneko, maxDistance);
        if (player != null && hasFoodInInventory(player)) {
            targetPlayer = player;
            eatCooldown = 10;
        } else {
            actionCooldown = 100;
        }
    }

    private void handleFollowTarget() {
        if (targetPlayer == null || !targetPlayer.isAlive()) {
            targetPlayer = null;
            actionCooldown = 200;
            return;
        }

        if (bakeneko.isSitting() || bakeneko.isSleeping()) {
            bakeneko.forceWakeUp();
        }

        boolean shouldFollow = false;

        if (bakeneko.isAngry() && targetPlayer == bakeneko.getLastThief()) {
            shouldFollow = true;
        }
        else if (!bakeneko.isAngry() && hasFoodInInventory(targetPlayer)) {
            shouldFollow = true;
        }
        else {
            targetPlayer = null;
            actionCooldown = 200;
            return;
        }

        if (bakeneko.distanceToSqr(targetPlayer) > maxDistance * maxDistance) {
            targetPlayer = null;
            actionCooldown = 200;
            return;
        }

        if (--eatCooldown <= 0) {
            eatCooldown = 10;

            if (bakeneko.distanceToSqr(targetPlayer) > minDistance * minDistance) {
                bakeneko.getNavigation().moveTo(targetPlayer, speedModifier);
            } else {
                bakeneko.getNavigation().stop();
                bakeneko.getLookControl().setLookAt(targetPlayer, 10.0F, bakeneko.getMaxHeadXRot());

                if (bakeneko.isAngry() && bakeneko.tickCount % 20 == 0) {
                    bakeneko.playSound(net.minecraft.sounds.SoundEvents.CAT_HISS, 0.5F, 1.0F);
                } else if (bakeneko.tickCount % 40 == 0) {
                    bakeneko.playSound(net.minecraft.sounds.SoundEvents.CAT_BEG_FOR_FOOD, 0.5F, 1.0F);
                }
            }
        }
    }

    private boolean hasFoodInInventory(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (bakeneko.isFood(stack)) {
                return true;
            }
        }
        return false;
    }
}