package net.lisalaf.fantastikmod.entity.ai.bakeneko;

import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BakenekoSitGoal extends MoveToBlockGoal {
    private final BakenekoEntity bakeneko;

    public BakenekoSitGoal(BakenekoEntity entity, double speedModifier) {
        super(entity, speedModifier, 8, 2);
        this.bakeneko = entity;
    }

    @Override
    public boolean canUse() {
        return !this.bakeneko.isAngry()
                && !this.bakeneko.isSleeping()
                && this.bakeneko.getRandom().nextFloat() < 0.01f // Шанс захотеть посидеть
                && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.bakeneko.getNavigation().stop();
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos.above())) return false;

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        return block instanceof ChestBlock ||
                block instanceof CraftingTableBlock ||
                block == Blocks.GRASS_BLOCK ||
                block == Blocks.STONE;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isReachedTarget() && !this.bakeneko.isSitting()) {
            this.bakeneko.setSitting(true, this.blockPos);
            this.bakeneko.startSittingAnimation();
        } else if (!this.isReachedTarget() && this.bakeneko.isSitting()) {
            this.bakeneko.setSitting(false, null);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.bakeneko.setSitting(false, null);
        this.bakeneko.stopSittingSleepingAnimation();
    }
}
