package net.lisalaf.fantastikmod.datagen;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {

    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, fantastikmod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Инструменты для добычи
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.MOON_PLANKS.get(),
                        ModBlocks.MOON_PLANKS_STAIRS.get(),
                        ModBlocks.MOON_PLANKS_SLAB.get(),
                        ModBlocks.MOON_PLANKS_FENCE.get(),
                        ModBlocks.MOON_PLANKS_FENCE_GATE.get(),
                        ModBlocks.MOON_WALL.get(),
                        ModBlocks.MOON_PLANKS_BUTTON.get(),
                        ModBlocks.MOON_PLANKS_PLATE.get(),
                        ModBlocks.MOON_TREE_DOOR.get(),
                        ModBlocks.MOON_TREE_TRAPDOOR.get(),
                        ModBlocks.TREE_MOON_LOG_BLOCK.get(),
                        ModBlocks.TREE_MOON_WOOD.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.ASH_BLOCK.get());


        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.AURIPIGMENT_ORE_BLOCK.get(),
                        (ModBlocks.SILVER_BLOCK.get()),
                        (ModBlocks.MOONSTONE.get()),
                        (ModBlocks.SILVER_ORE.get()),
                        (ModBlocks.MOON_GEM_BLOCK.get()),
                        (ModBlocks.MOON_ORE.get()),
                        ModBlocks.MOON_CRYSTAL_BLOCK.get(),
                        ModBlocks.GEMKITSUNE_BLOCK.get(),
                        ModBlocks.KITSUNE_STATUE.get());

        // Уровень инструментов
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.GEMKITSUNE_BLOCK.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.MOON_ORE.get())
                .add(ModBlocks.MOON_GEM_BLOCK.get());

        tag(ModTags.Blocks.NEEDS_AURIPIGMENT_TOOL)
                .add(ModBlocks.GEMKITSUNE_BLOCK.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.AURIPIGMENT_ORE_BLOCK.get())
                .add(ModBlocks.AURIPIGMENT_BLOCK.get())
                .add(ModBlocks.SILVER_ORE.get())
                .add(ModBlocks.SILVER_BLOCK.get())
                .add(ModBlocks.MOON_CRYSTAL_BLOCK.get())
                .add(ModBlocks.MOONSTONE.get());


        // Функциональные теги
        tag(BlockTags.FENCES)
                .add(ModBlocks.MOON_PLANKS_FENCE.get());
        tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.MOON_PLANKS_FENCE.get());
        tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.MOON_PLANKS_FENCE_GATE.get());
        tag(BlockTags.WALLS)
                .add(ModBlocks.MOON_WALL.get());

        tag(BlockTags.STAIRS)
                .add(ModBlocks.MOON_PLANKS_STAIRS.get());
        tag(BlockTags.SLABS)
                .add(ModBlocks.MOON_PLANKS_SLAB.get());
        tag(BlockTags.BUTTONS)
                .add(ModBlocks.MOON_PLANKS_BUTTON.get());
        tag(BlockTags.PRESSURE_PLATES)
                .add(ModBlocks.MOON_PLANKS_PLATE.get());
        tag(BlockTags.DOORS)
                .add(ModBlocks.MOON_TREE_DOOR.get());
        tag(BlockTags.TRAPDOORS)
                .add(ModBlocks.MOON_TREE_TRAPDOOR.get());

        tag(BlockTags.PLANKS)
                .add(ModBlocks.MOON_PLANKS.get());

        tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.TREE_MOON_LOG_BLOCK.get())
                .add(ModBlocks.TREE_MOON_WOOD.get());


    }
}