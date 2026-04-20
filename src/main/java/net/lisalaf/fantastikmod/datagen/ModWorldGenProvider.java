package net.lisalaf.fantastikmod.datagen;

import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.worldgen.ModBiomeModifiers;
import net.lisalaf.fantastikmod.worldgen.ModConfiguredFeatures;
import net.lisalaf.fantastikmod.worldgen.ModPlacedFeatures;
import net.lisalaf.fantastikmod.worldgen.biome.ModBiomes;
import net.lisalaf.fantastikmod.worldgen.structures.ModStructures;
import net.lisalaf.fantastikmod.worldgen.structures.ModTemplatePools;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.BIOME, ModBiomes::boostrap)
            .add(Registries.STRUCTURE, ModStructures::bootstrap)
            .add(Registries.TEMPLATE_POOL, ModTemplatePools::bootstrap)
            .add(Registries.STRUCTURE_SET, ModStructures::bootstrapSets);
    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(fantastikmod.MOD_ID));
    }
}
