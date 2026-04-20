package net.lisalaf.fantastikmod.worldgen;

import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.lisalaf.fantastikmod.entity.ModEntities;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import java.util.List;

import static net.lisalaf.fantastikmod.block.ModBlocks.register;

@SuppressWarnings("removal")
public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_SILVER_ORE = registerKey("add_silver_ore");
    public static final ResourceKey<BiomeModifier> ADD_AURIPIGMENT_ORE = registerKey("add_auripigment_ore");

    public static final ResourceKey<BiomeModifier> ADD_SPIDER_LILY_TO_CHERRY = registerKey("add_spider_lily_to_cherry");

    //public static final ResourceKey<BiomeModifier> ADD_TEA_HOUSE = registerKey("add_tea_house");

    public static final ResourceKey<BiomeModifier> ADD_DRAGON_CAVE_TO_SNOWY_BIOMES = registerKey("add_dragon_cave_to_snowy_biomes");

    public static final ResourceKey<BiomeModifier> ADD_KITSUNE_TO_PLAINS = registerKey("add_kitsune_to_plains");

    public static final ResourceKey<BiomeModifier> ADD_BAKENEKO_TO_VILLAGE_BIOMES = registerKey("add_bakeneko_to_village_biomes");
    public static final ResourceKey<BiomeModifier> ADD_KITSUNE_STATUE_TO_CHERRY = registerKey("add_kitsune_statue_to_cherry");
    public static final ResourceKey<BiomeModifier> ADD_WILD_CATNIP = registerKey("add_wild_catnip");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        var structures = context.lookup(Registries.STRUCTURE);

        context.register(ADD_SILVER_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SILVER_ORE_PLACED_KEI)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_AURIPIGMENT_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.AURIPIGMENT_ORE_PLACED_KEI)),
                GenerationStep.Decoration.UNDERGROUND_ORES));


       // HolderSet<Biome> snowyBiomes = HolderSet.direct(biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.SNOWY_PLAINS), biomes.getOrThrow(Biomes.ICE_SPIKES), biomes.getOrThrow(Biomes.SNOWY_SLOPES), biomes.getOrThrow(Biomes.FROZEN_PEAKS));

       //context.register(ADD_DRAGON_CAVE_TO_SNOWY_BIOMES, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(snowyBiomes, HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DRAGON_CAVE_PLACED_KEY)), placedFeaturesGenerationStep.Decoration.UNDERGROUND_STRUCTURES));

        context.register(ADD_KITSUNE_TO_PLAINS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.HAS_VILLAGE_PLAINS),
                List.of(
                        new MobSpawnSettings.SpawnerData(
                                ModEntities.KITSUNE_LIGHT.get(),
                                40,
                                1,
                                4
                        )
                )

        ));

        context.register(ADD_BAKENEKO_TO_VILLAGE_BIOMES, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(
                        biomes.getOrThrow(Biomes.PLAINS),
                        biomes.getOrThrow(Biomes.SAVANNA),
                        biomes.getOrThrow(Biomes.DESERT),
                        biomes.getOrThrow(Biomes.TAIGA),
                        biomes.getOrThrow(Biomes.SNOWY_PLAINS),
                        biomes.getOrThrow(Biomes.MEADOW)
                ),
                List.of(
                        new MobSpawnSettings.SpawnerData(
                                ModEntities.BAKENEKO.get(),
                                10,
                                1,
                                1
                        )
                )
        ));

        context.register(ADD_SPIDER_LILY_TO_CHERRY, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.CHERRY_GROVE)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SPIDER_LILY_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_KITSUNE_STATUE_TO_CHERRY, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.CHERRY_GROVE)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.KITSUNE_STATUE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_WILD_CATNIP, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                        biomes.getOrThrow(Biomes.SAVANNA),
                        biomes.getOrThrow(Biomes.SAVANNA_PLATEAU),
                        biomes.getOrThrow(Biomes.WINDSWEPT_SAVANNA),
                        biomes.getOrThrow(Biomes.PLAINS),
                        biomes.getOrThrow(Biomes.BIRCH_FOREST),
                        biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST),
                        biomes.getOrThrow(Biomes.TAIGA),
                        biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA),
                        biomes.getOrThrow(Biomes.OLD_GROWTH_SPRUCE_TAIGA),
                        biomes.getOrThrow(Biomes.WINDSWEPT_HILLS),
                        biomes.getOrThrow(Biomes.WINDSWEPT_GRAVELLY_HILLS),
                        biomes.getOrThrow(Biomes.MEADOW)
                ),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WILD_CATNIP_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));


    }

    private static void register(BootstapContext<BiomeModifier> context, ResourceKey<BiomeModifier> key, BiomeModifier modifier) {
        context.register(key, modifier);
    }


    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(fantastikmod.MOD_ID, name));
    }
}
