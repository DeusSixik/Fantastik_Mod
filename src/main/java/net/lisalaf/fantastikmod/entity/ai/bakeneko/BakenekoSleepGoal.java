package net.lisalaf.fantastikmod.entity.ai.bakeneko;

import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BakenekoSleepGoal extends MoveToBlockGoal {
    private final BakenekoEntity bakeneko;

    public BakenekoSleepGoal(BakenekoEntity entity, double speedModifier) {
        /*
            Ищем цель в радиусе 8 блоков по горизонтали и 2 по вертикали.
            Ванильный алгоритм в полне не плохо справляеться с этой задачей
         */
        super(entity, speedModifier, 8, 2);
        this.bakeneko = entity;
    }

    @Override
    public boolean canUse() {
        /*
            Спим только если моб спокоен, и вероятность прокнула
         */
        return !this.bakeneko.isAngry()
                && !this.bakeneko.isSitting()
                && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.bakeneko.getNavigation().stop();
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {

        /*
            Быстренько проверяем есть ли воздух над блоком чтобы моб поместился.
         */
        if (!level.isEmptyBlock(pos.above())) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        return state.is(BlockTags.BEDS)
                || (state.is(Blocks.FURNACE) && state.getValue(FurnaceBlock.LIT))
                || (state.is(Blocks.CAMPFIRE) && state.getValue(CampfireBlock.LIT));
    }

    @Override
    public void tick() {
        super.tick();

        /*
            Если мы дошли до теплого блока
         */
        if (this.isReachedTarget()) {
            if (!this.bakeneko.isSleeping()) {
                this.bakeneko.setSleeping(true, this.blockPos);
                this.bakeneko.startSleepingAnimation();
            }
        } else if (this.bakeneko.isSleeping()) {
            /*
                Если в пути цель сместилась или нас толкнули - сбрасываем сон
             */
            this.bakeneko.setSleeping(false, null);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.bakeneko.setSleeping(false, null);
        this.bakeneko.stopSittingSleepingAnimation();
    }
}
