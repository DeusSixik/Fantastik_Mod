package net.lisalaf.fantastikmod.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

@SuppressWarnings("removal")
public class ModOverworldRegion extends Region {

    public ModOverworldRegion() {
        super(new ResourceLocation(fantastikmod.MOD_ID, "overworld"), RegionType.OVERWORLD, 10);
    }

    public ModOverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addBiome(
                mapper,
                Climate.Parameter.span(0.3F, 0.6F),
                Climate.Parameter.span(0.4F, 0.6F),
                Climate.Parameter.span(0.3F, 0.7F),
                Climate.Parameter.span(-1.0F, -0.3F),
                Climate.Parameter.point(0.0F),
                Climate.Parameter.span(0.5F, 1.5F),
                0,
                ModBiomes.BLUE_MOON_FOREST_BIOME
        );
    }
}