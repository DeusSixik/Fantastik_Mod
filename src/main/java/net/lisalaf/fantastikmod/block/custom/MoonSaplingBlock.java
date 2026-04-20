package net.lisalaf.fantastikmod.block.custom;

import net.lisalaf.fantastikmod.worldgen.tree.BigMoonTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class MoonSaplingBlock extends SaplingBlock {

    public MoonSaplingBlock(AbstractTreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        BlockPos belowPos = pos.below();
        if (level.getFluidState(belowPos).is(FluidTags.WATER)) {
            return;
        }

        BlockPos cornerPos = find2x2Corner(level, pos);

        if (cornerPos != null) {
            if (BigMoonTreeGrower.growBigMoonTree(level, cornerPos, random)) {
                return;
            }
        }

        super.advanceTree(level, pos, state, random);
    }

    private BlockPos find2x2Corner(ServerLevel level, BlockPos pos) {
        for (int x = -1; x <= 0; x++) {
            for (int z = -1; z <= 0; z++) {
                BlockPos corner = pos.offset(x, 0, z);
                if (areSaplingsIn2x2Square(level, corner)) {
                    return corner;
                }
            }
        }
        return null;
    }

    private boolean areSaplingsIn2x2Square(ServerLevel level, BlockPos corner) {
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                BlockPos checkPos = corner.offset(x, 0, z);
                if (!(level.getBlockState(checkPos).getBlock() instanceof MoonSaplingBlock)) {
                    return false;
                }
            }
        }
        return true;
    }
}