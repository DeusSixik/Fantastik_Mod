package net.lisalaf.fantastikmod.worldgen.structures;

import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("removal")
public class ModStructures {
    // Старая структура
    public static final ResourceKey<Structure> TEA_HOUSE = ResourceKey.create(Registries.STRUCTURE,
            new ResourceLocation(fantastikmod.MOD_ID, "tea_house"));

    public static final ResourceKey<StructureSet> TEA_HOUSE_SET = ResourceKey.create(Registries.STRUCTURE_SET,
            new ResourceLocation(fantastikmod.MOD_ID, "tea_house"));

    public static final ResourceKey<Structure> MOON_RUIN_HOUSE = ResourceKey.create(Registries.STRUCTURE,
            new ResourceLocation(fantastikmod.MOD_ID, "moon_ruin_house"));

    public static final ResourceKey<StructureSet> MOON_RUIN_HOUSE_SET = ResourceKey.create(Registries.STRUCTURE_SET,
            new ResourceLocation(fantastikmod.MOD_ID, "moon_ruin_house"));

    public static final ResourceKey<Structure> TOWN_CENTER = ResourceKey.create(Registries.STRUCTURE,
            new ResourceLocation(fantastikmod.MOD_ID, "town_center"));

    public static final ResourceKey<StructureSet> TOWN_CENTER_SET = ResourceKey.create(Registries.STRUCTURE_SET,
            new ResourceLocation(fantastikmod.MOD_ID, "town_center"));

    public static final ResourceKey<Structure> MOON_RUIN_CHURCH = ResourceKey.create(Registries.STRUCTURE,
            new ResourceLocation(fantastikmod.MOD_ID, "moon_ruin_church"));

    public static final ResourceKey<StructureSet> MOON_RUIN_CHURCH_SET = ResourceKey.create(Registries.STRUCTURE_SET,
            new ResourceLocation(fantastikmod.MOD_ID, "moon_ruin_church"));

    public static final ResourceKey<Structure> INARI_SMALL_SHRINE = ResourceKey.create(Registries.STRUCTURE,
            new ResourceLocation(fantastikmod.MOD_ID, "inari_small_shrine"));

    public static final ResourceKey<StructureSet> INARI_SMALL_SHRINE_SET = ResourceKey.create(Registries.STRUCTURE_SET,
            new ResourceLocation(fantastikmod.MOD_ID, "inari_small_shrine"));


    public static void bootstrap(BootstapContext<Structure> context) {
        context.register(TEA_HOUSE, new JigsawStructure(
                new Structure.StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(ModTags.Biomes.HAS_TEA_HOUSE),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                context.lookup(Registries.TEMPLATE_POOL).getOrThrow(ModTemplatePools.TEA_HOUSE_START),
                Optional.empty(),
                7,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                116
        ));

        context.register(MOON_RUIN_HOUSE, new JigsawStructure(
                new Structure.StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(ModTags.Biomes.HAS_MOON_RUIN_HOUSE),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                context.lookup(Registries.TEMPLATE_POOL).getOrThrow(ModTemplatePools.MOON_RUIN_HOUSE_START),
                Optional.empty(),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                116
        ));

        context.register(TOWN_CENTER, new JigsawStructure(
                new Structure.StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(ModTags.Biomes.HAS_TOWN_CENTER),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                context.lookup(Registries.TEMPLATE_POOL).getOrThrow(ModTemplatePools.TOWN_CENTER_START),
                Optional.empty(),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(-1)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                116
        ));

        context.register(MOON_RUIN_CHURCH, new JigsawStructure(
                new Structure.StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(ModTags.Biomes.HAS_MOON_RUIN_CHURCH),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                context.lookup(Registries.TEMPLATE_POOL).getOrThrow(ModTemplatePools.MOON_RUIN_CHURCH_START),
                Optional.empty(),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                116
        ));

        context.register(INARI_SMALL_SHRINE, new JigsawStructure(
                new Structure.StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(ModTags.Biomes.HAS_INARI_SMALL_SHRINE),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                context.lookup(Registries.TEMPLATE_POOL).getOrThrow(ModTemplatePools.INARI_SMALL_SHRINE_START),
                Optional.empty(),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                116
        ));
    }


    public static void bootstrapSets(BootstapContext<StructureSet> context) {
        context.register(TEA_HOUSE_SET, new StructureSet(
                context.lookup(Registries.STRUCTURE).getOrThrow(TEA_HOUSE),
                new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 123456789)
        ));

        context.register(MOON_RUIN_HOUSE_SET, new StructureSet(
                context.lookup(Registries.STRUCTURE).getOrThrow(MOON_RUIN_HOUSE),
                new RandomSpreadStructurePlacement(
                        40,
                        1,
                        RandomSpreadType.LINEAR,
                        987654321
                )
        ));
        context.register(TOWN_CENTER_SET, new StructureSet(
                context.lookup(Registries.STRUCTURE).getOrThrow(TOWN_CENTER),
                new RandomSpreadStructurePlacement(
                        50,
                        15,
                        RandomSpreadType.LINEAR,
                        555666777
                )
        ));
        context.register(MOON_RUIN_CHURCH_SET, new StructureSet(
                context.lookup(Registries.STRUCTURE).getOrThrow(MOON_RUIN_CHURCH),
                new RandomSpreadStructurePlacement(60, 20, RandomSpreadType.LINEAR, 123450987)
        ));

        context.register(INARI_SMALL_SHRINE_SET, new StructureSet(
                context.lookup(Registries.STRUCTURE).getOrThrow(INARI_SMALL_SHRINE),
                new RandomSpreadStructurePlacement(45, 12, RandomSpreadType.LINEAR, 1122334455)
        ));
    }
}