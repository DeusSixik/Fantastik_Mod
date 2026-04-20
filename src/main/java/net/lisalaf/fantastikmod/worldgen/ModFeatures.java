package net.lisalaf.fantastikmod.worldgen;

import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.worldgen.feature.BigMoonTreeFeature;
import net.lisalaf.fantastikmod.worldgen.feature.DragonCaveFeature;
import net.lisalaf.fantastikmod.worldgen.feature.MoonCrystalClusterFeature;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.lisalaf.fantastikmod.worldgen.ModConfiguredFeatures.registerKey;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, fantastikmod.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> BIG_MOON_TREE = FEATURES.register("big_moon_tree",
            () -> new BigMoonTreeFeature());

   // public static final RegistryObject<Feature<NoneFeatureConfiguration>> DRAGON_CAVE = FEATURES.register("dragon_cave", () -> new DragonCaveFeature(NoneFeatureConfiguration.CODEC));


    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOON_CRYSTAL_CLUSTER =
            FEATURES.register("moon_crystal_cluster", () -> new MoonCrystalClusterFeature());
}