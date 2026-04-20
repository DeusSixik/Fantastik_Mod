package net.lisalaf.fantastikmod.worldgen.tree;

import net.lisalaf.fantastikmod.worldgen.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class MoonTreeGrower extends AbstractTreeGrower {

    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        return ModConfiguredFeatures.MOON_TREE_KEY;
    }

    public boolean growBigTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (BigMoonTreeGrower.growBigMoonTree(level, pos, random)) {
            return true;
        }
        return false;
    }
}