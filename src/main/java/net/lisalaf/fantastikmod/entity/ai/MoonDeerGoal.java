package net.lisalaf.fantastikmod.entity.ai;

import net.lisalaf.fantastikmod.entity.custom.MoonDeerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;

import java.util.EnumSet;

public class MoonDeerGoal extends Goal {
    private final MoonDeerEntity deer;
    private LivingEntity owner;
    private final double speedModifier;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private boolean wasInWater;

    public MoonDeerGoal(MoonDeerEntity deer, double speed, float minDist, float maxDist) {
        this.deer = deer;
        this.speedModifier = speed;
        this.startDistance = minDist;
        this.stopDistance = maxDist;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (deer.getAIMode() != MoonDeerEntity.AI_FOLLOW) return false;
        if (deer.isDrinking()) return false;

        LivingEntity livingentity = deer.getOwner();
        if (livingentity == null) {
            return false;
        } else if (deer.distanceToSqr(livingentity) < (double)(this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (deer.getAIMode() != MoonDeerEntity.AI_FOLLOW) return false;
        if (deer.getNavigation().isDone() && !deer.isInWater()) {
            return false;
        } else if (deer.isDrinking()) {
            return false;
        } else {
            return !(deer.distanceToSqr(this.owner) <= (double)(this.stopDistance * this.stopDistance));
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.wasInWater = deer.isInWater();

        if (deer.isInWater()) {
            enableSwimming(true);
        }
    }

    @Override
    public void stop() {
        this.owner = null;
        deer.getNavigation().stop();

        if (wasInWater || deer.isInWater()) {
            enableSwimming(false);
        }
    }

    @Override
    public void tick() {
        deer.getLookControl().setLookAt(this.owner, 10.0F, (float)deer.getMaxHeadXRot());

        boolean inWater = deer.isInWater();

        if (inWater != wasInWater) {
            enableSwimming(inWater);
            wasInWater = inWater;
        }

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;

            if (deer.isInWater()) {
                double yOffset = owner.isInWater() ? 0 : 1.0;
                deer.getNavigation().moveTo(owner.getX(), owner.getY() + yOffset, owner.getZ(), this.speedModifier);
            } else {
                deer.getNavigation().moveTo(this.owner, this.speedModifier);
            }
        }

        if (deer.isInWater() && deer.getY() < owner.getY() - 2) {
            deer.setDeltaMovement(deer.getDeltaMovement().x, 0.2, deer.getDeltaMovement().z);
        }
    }

    private void enableSwimming(boolean enable) {
        if (enable) {
            deer.setNoGravity(true);
            deer.getNavigation().stop();
        } else {
            deer.setNoGravity(false);
        }
    }
}