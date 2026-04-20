package net.lisalaf.fantastikmod.entity.ai.bakeneko;

import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BakenekoEscapeWaterGoal extends Goal {
    private final BakenekoEntity bakeneko;
    private double targetX, targetY, targetZ;

    public BakenekoEscapeWaterGoal(BakenekoEntity entity) {
        this.bakeneko = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.bakeneko.isInWater() && !this.bakeneko.isInLava()) {
            return false;
        }

        Vec3 landPos = this.getLandPos();
        if (landPos == null) return false;

        this.targetX = landPos.x;
        this.targetY = landPos.y;
        this.targetZ = landPos.z;
        return true;
    }

    @Override
    public void start() {
        this.bakeneko.wakeUp();
        this.bakeneko.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, 1.8D);
    }

    @Override
    public void tick() {
        /*
            Кот шипит каждые 20 тиков, пока плывет
         */
        if (this.bakeneko.tickCount % 20 == 0) {
            this.bakeneko.playSound(SoundEvents.CAT_HISS, 1.0F, 1.5F);
        }
    }

    @Nullable
    private Vec3 getLandPos() {
        RandomSource random = this.bakeneko.getRandom();
        BlockPos pos = this.bakeneko.blockPosition();

        for (int i = 0; i < 20; i++) {
            int x = pos.getX() + (random.nextInt(21) - 10);
            int z = pos.getZ() + (random.nextInt(21) - 10);
            int y = this.bakeneko.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos checkPos = new BlockPos(x, y, z);

            if (!this.bakeneko.level().getBlockState(checkPos).getFluidState().is(net.minecraft.tags.FluidTags.WATER)) {
                return Vec3.atBottomCenterOf(checkPos);
            }
        }
        return null;
    }
}
