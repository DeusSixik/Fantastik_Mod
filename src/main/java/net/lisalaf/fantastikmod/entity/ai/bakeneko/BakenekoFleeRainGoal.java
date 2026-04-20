package net.lisalaf.fantastikmod.entity.ai.bakeneko;

import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BakenekoFleeRainGoal extends Goal {
    private final BakenekoEntity bakeneko;
    private double targetX, targetY, targetZ;

    public BakenekoFleeRainGoal(BakenekoEntity entity) {
        this.bakeneko = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        /*
            Запускаем только если идет дождь и на кота капает вода
         */
        if (!this.bakeneko.level().isRaining() || !this.bakeneko.level().canSeeSky(this.bakeneko.blockPosition())) {
            return false;
        }

        Vec3 hidePos = this.getHidePos();
        if (hidePos == null) return false;

        this.targetX = hidePos.x;
        this.targetY = hidePos.y;
        this.targetZ = hidePos.z;
        return true;
    }

    @Override
    public void start() {

        /*
            Будим, если спал под дождем
         */
        this.bakeneko.wakeUp();
        this.bakeneko.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, 1.3D);
    }

    @Nullable
    private Vec3 getHidePos() {
        RandomSource random = this.bakeneko.getRandom();
        BlockPos pos = this.bakeneko.blockPosition();

        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();

        /*
             Поиск укрытия
             Проверяем 10 случайных блоков вокруг
         */
        for (int i = 0; i < 10; i++) {
            mutableBlockPos.set(
                    mutableBlockPos.getX() + (random.nextInt(20) - 10),
                    mutableBlockPos.getY() + (random.nextInt(6) - 3),
                    mutableBlockPos.getZ() + random.nextInt(20) - 10
            );
            if (!this.bakeneko.level().canSeeSky(mutableBlockPos) && this.bakeneko.level().getBlockState(mutableBlockPos).isAir()) {
                return Vec3.atBottomCenterOf(mutableBlockPos);
            }
        }
        return null;
    }
}