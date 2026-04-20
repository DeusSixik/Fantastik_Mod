package net.lisalaf.fantastikmod.worldgen.structures;

import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModTags {
    @SuppressWarnings("removal")
    public static class Biomes {
        public static final TagKey<Biome> HAS_MOON_RUIN_HOUSE = TagKey.create(
                Registries.BIOME,
                new ResourceLocation(fantastikmod.MOD_ID, "has_structure/moon_ruin_house")
        );

        public static final TagKey<Biome> HAS_TEA_HOUSE = TagKey.create(
                Registries.BIOME,
                new ResourceLocation(fantastikmod.MOD_ID, "has_structure/tea_house")
        );

        public static final TagKey<Biome> HAS_TOWN_CENTER = TagKey.create(
                Registries.BIOME,
                new ResourceLocation(fantastikmod.MOD_ID, "has_structure/town_center")
        );

        public static final TagKey<Biome> HAS_MOON_RUIN_CHURCH = TagKey.create(
                Registries.BIOME,
                new ResourceLocation(fantastikmod.MOD_ID, "has_structure/moon_ruin_church")
        );

        public static final TagKey<Biome> HAS_INARI_SMALL_SHRINE = TagKey.create(
                Registries.BIOME,
                new ResourceLocation(fantastikmod.MOD_ID, "has_structure/inari_small_shrine")
        );
    }
}