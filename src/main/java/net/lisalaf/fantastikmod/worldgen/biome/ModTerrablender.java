package net.lisalaf.fantastikmod.worldgen.biome;

import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

@SuppressWarnings("removal")
public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(fantastikmod.MOD_ID, "overworld"), 2));
    }
}
