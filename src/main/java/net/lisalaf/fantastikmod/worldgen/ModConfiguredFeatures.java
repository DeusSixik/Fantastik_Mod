package net.lisalaf.fantastikmod.worldgen;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.block.custom.CrowberryShrubBlock;
import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.worldgen.feature.MoonCrystalClusterFeature;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SILVER_ORE_KEY = registerKey("silver_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_AURIPIGMENT_ORE_KEY = registerKey("auripigment_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_ORE_KEY = registerKey("moon_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_KEY = registerKey("moon");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_LILY_KEY = registerKey("moon_lily");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_NORTHERN_BLUEBELL_KEY = registerKey("moon_northern_bluebell");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_HEATHER_KEY = registerKey("moon_heather");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_GRASS_KEY = registerKey("moon_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_GRASS_1_KEY = registerKey("moon_grass_1");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_VINE_KEY = registerKey("moon_vine");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BUSH_CROWBERRY_KEY = registerKey("bush_crowberry");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIDER_LILY_KEY = registerKey("spider_lily");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MOONSTONE_KEY = registerKey("moonstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> KITSUNE_STATUE_KEY = registerKey("kitsune_statue");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_CATNIP_KEY = registerKey("wild_catnip");

   // public static final ResourceKey<ConfiguredFeature<?, ?>> TEA_HOUSE_KEY = registerKey("tea_house");

    public static final ResourceKey<ConfiguredFeature<?, ?>> DRAGON_CAVE_KEY = registerKey("dragon_cave");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_CRYSTAL_CLUSTER_KEY =
            registerKey("moon_crystal_cluster");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldSilverOres = List.of(OreConfiguration.target(stoneReplaceable,
                ModBlocks.SILVER_ORE.get().defaultBlockState()));
        register(context, OVERWORLD_SILVER_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSilverOres, 7));

        List<OreConfiguration.TargetBlockState> overworldAuripigmentOres = List.of(OreConfiguration.target(stoneReplaceable,
                ModBlocks.AURIPIGMENT_ORE_BLOCK.get().defaultBlockState()));
        register(context, OVERWORLD_AURIPIGMENT_ORE_KEY, Feature.ORE, new OreConfiguration(overworldAuripigmentOres, 3));

        List<OreConfiguration.TargetBlockState> moonOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.MOON_ORE.get().defaultBlockState()));
        register(context, MOON_ORE_KEY, Feature.ORE, new OreConfiguration(moonOres, 5));

        register(context, MOON_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.TREE_MOON_LOG_BLOCK.get()),
                new FancyTrunkPlacer(10, 12, 5), // Более естественные ветвления
                BlockStateProvider.simple(ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(3), 5), // Соответствующая листва
                new TwoLayersFeatureSize(1, 2, 4, OptionalInt.of(3)))
                .ignoreVines()
                //.decorators(List.of(new MoonVineTreeDecorator()))
                .build());

        register(context, MOON_HEATHER_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(50, 4, 2,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MOON_HEATHER.get())))));

        register(context, MOON_NORTHERN_BLUEBELL_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(40, 2, 4,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MOON_NORTHERN_BLUEBELL.get())))));

        register(context, MOON_LILY_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(29, 6, 2,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MOON_LILY.get())))));

        register(context, MOON_GRASS_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(60, 10, 25,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MOON_GRASS.get())))));

        register(context, MOON_GRASS_1_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(47, 3, 10,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MOON_GRASS_1.get())))));
        register(context, MOON_VINE_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(20, 2, 6,
                        PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MOON_VINE.get())),
                                BlockPredicate.allOf(
                                        BlockPredicate.replaceable(),
                                        BlockPredicate.matchesBlocks(Direction.UP.getNormal(), ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get())
                                ))));

        register(context, BUSH_CROWBERRY_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(25, 3, 5,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.BUSH_CROWBERRY.get().defaultBlockState()
                                                        .setValue(CrowberryShrubBlock.AGE, 4)
                                        )
                                )
                        )
                )
        );

        register(context, MOONSTONE_KEY, Feature.ORE, new OreConfiguration(
                stoneReplaceable,
                ModBlocks.MOONSTONE.get().defaultBlockState(),
                64
        ));

        register(context, SPIDER_LILY_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(32, 7, 3,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.SPIDER_LILY.get())))));

        context.register(MOON_CRYSTAL_CLUSTER_KEY,
                new ConfiguredFeature<>(ModFeatures.MOON_CRYSTAL_CLUSTER.get(),
                        new NoneFeatureConfiguration()));

        register(context, KITSUNE_STATUE_KEY, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.KITSUNE_STATUE.get())));

        register(context, WILD_CATNIP_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(16, 4, 1,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_CATNIP.get())))));



       // register(context, DRAGON_CAVE_KEY, ModFeatures.DRAGON_CAVE.get(), new NoneFeatureConfiguration());




    }

    @SuppressWarnings("removal")
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(fantastikmod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
