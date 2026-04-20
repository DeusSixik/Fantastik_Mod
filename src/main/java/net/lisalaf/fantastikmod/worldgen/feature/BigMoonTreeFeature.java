package net.lisalaf.fantastikmod.worldgen.feature;

import net.lisalaf.fantastikmod.worldgen.tree.BigMoonTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BigMoonTreeFeature extends Feature<NoneFeatureConfiguration> {

    public BigMoonTreeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        return BigMoonTreeGrower.generateBigMoonTree(level, pos, random);
    }
}