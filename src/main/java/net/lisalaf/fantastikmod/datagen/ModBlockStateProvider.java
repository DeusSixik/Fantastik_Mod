package net.lisalaf.fantastikmod.datagen;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.block.custom.*;
import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.ChunkPalettedStorageFix;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("removal")
public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, fantastikmod.MOD_ID, exFileHelper);
        System.out.println("ModBlockStateProvider constructor called");
    }

    @Override
    protected void registerStatesAndModels() {
        System.out.println("Registering block states and models...");

        // ПРОСТЫЕ БЛОКИ (которые дропают себя)
        simpleBlockWithItem(ModBlocks.ASH_BLOCK.get(), cubeAll(ModBlocks.ASH_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.MOON_CRYSTAL_BLOCK.get(), cubeAll(ModBlocks.MOON_CRYSTAL_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.AURIPIGMENT_BLOCK.get(), cubeAll(ModBlocks.AURIPIGMENT_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.DRYING_BASKET.get(), cubeAll(ModBlocks.DRYING_BASKET.get()));
        simpleBlockWithItem(ModBlocks.GEMKITSUNE_BLOCK.get(), cubeAll(ModBlocks.GEMKITSUNE_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.MOON_PLANKS.get(), cubeAll(ModBlocks.MOON_PLANKS.get()));

        simpleBlockWithItem(ModBlocks.AURIPIGMENT_ORE_BLOCK.get(), cubeAll(ModBlocks.AURIPIGMENT_ORE_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.MOON_ORE.get(), cubeAll(ModBlocks.MOON_ORE.get()));
        simpleBlockWithItem(ModBlocks.MOONSTONE.get(), cubeAll(ModBlocks.MOONSTONE.get()));
        simpleBlockWithItem(ModBlocks.SILVER_ORE.get(), cubeAll(ModBlocks.SILVER_ORE.get()));
        simpleBlockWithItem(ModBlocks.SILVER_BLOCK.get(), cubeAll(ModBlocks.SILVER_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.MOON_GEM_BLOCK.get(), cubeAll(ModBlocks.MOON_GEM_BLOCK.get()));

        leavesBlock(ModBlocks.TREE_MOON_FOLIAGE_BLOCK);
        
        logBlock((RotatedPillarBlock) ModBlocks.TREE_MOON_LOG_BLOCK.get(),
                modLoc("block/tree_moon_log_block"),      // боковая текстура
                modLoc("block/tree_moon_log_block_top")   // текстура торцов
        );

        // ФУНКЦИОНАЛЬНЫЕ БЛОКИ (специальные модели)
        // Лестница
        stairsBlock(((StairBlock) ModBlocks.MOON_PLANKS_STAIRS.get()),
                blockTexture(ModBlocks.MOON_PLANKS.get()));

        // Плита
        slabBlock(((SlabBlock) ModBlocks.MOON_PLANKS_SLAB.get()),
                blockTexture(ModBlocks.MOON_PLANKS.get()),
                blockTexture(ModBlocks.MOON_PLANKS.get()));

        // Забор
        fenceBlock(((FenceBlock) ModBlocks.MOON_PLANKS_FENCE.get()),
                blockTexture(ModBlocks.MOON_PLANKS.get()));

        // Калитка
        fenceGateBlock(((FenceGateBlock) ModBlocks.MOON_PLANKS_FENCE_GATE.get()),
                blockTexture(ModBlocks.MOON_PLANKS.get()));

        // Стена
        wallBlock(((WallBlock) ModBlocks.MOON_WALL.get()),
                blockTexture(ModBlocks.MOON_PLANKS.get()));

        // Кнопка
        buttonBlock(((ButtonBlock) ModBlocks.MOON_PLANKS_BUTTON.get()),
                blockTexture(ModBlocks.MOON_PLANKS.get()));

        // Нажимная плита
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.MOON_PLANKS_PLATE.get()),
                blockTexture(ModBlocks.MOON_PLANKS.get()));

        // Люк
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.MOON_TREE_TRAPDOOR.get()),
                modLoc("block/moon_tree_trapdoor"), true, "cutout");

        // Дверь
        doorBlockWithRenderType(((DoorBlock) ModBlocks.MOON_TREE_DOOR.get()),
                modLoc("block/moon_tree_door_bottom"),
                modLoc("block/moon_tree_door_top"), "cutout");

        makeStrawberryCrop((CropBlock) ModBlocks.STRAWBERRY_CROP.get(), "strawberry_stage", "strawberry_stage");
        makeTeaCrop((CropBlock) ModBlocks.TEA_CROP.get(), "tea_stage", "tea_stage");
        makeCrowberryShrub((CropBlock) ModBlocks.BUSH_CROWBERRY.get(), "shrub_crowberry_", "shrub_crowberry_");
        makeRiceCrop(((CropBlock) ModBlocks.RICE_CROP.get()), "rice_stage_", "rice_stage_");

        simpleBlockWithItem(ModBlocks.SPIDER_LILY.get(), models().cross(blockTexture(ModBlocks.SPIDER_LILY.get()).getPath(),
                blockTexture(ModBlocks.SPIDER_LILY.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_SPIDER_LILY.get(), models().singleTexture("potted_spider_lily", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.SPIDER_LILY.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.MOON_LILY.get(), models().cross(blockTexture(ModBlocks.MOON_LILY.get()).getPath(),
                blockTexture(ModBlocks.MOON_LILY.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_MOON_LILY.get(), models().singleTexture("potted_moon_lily", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.MOON_LILY.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.MOON_HEATHER.get(), models().cross(blockTexture(ModBlocks.MOON_HEATHER.get()).getPath(),
                blockTexture(ModBlocks.MOON_HEATHER.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_MOON_HEATHER.get(), models().singleTexture("potted_moon_heather", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.MOON_HEATHER.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.MOON_NORTHERN_BLUEBELL.get(), models().cross(blockTexture(ModBlocks.MOON_NORTHERN_BLUEBELL.get()).getPath(),
                blockTexture(ModBlocks.MOON_NORTHERN_BLUEBELL.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_MOON_NORTHERN_BLUEBELL.get(), models().singleTexture("potted_moon_northern_bluebell", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.MOON_NORTHERN_BLUEBELL.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.MOON_GRASS.get(),
                models().cross(blockTexture(ModBlocks.MOON_GRASS.get()).getPath(),
                                blockTexture(ModBlocks.MOON_GRASS.get()))
                        .renderType("cutout"));
        simpleBlockWithItem(ModBlocks.MOON_GRASS_1.get(),
                models().cross(blockTexture(ModBlocks.MOON_GRASS_1.get()).getPath(),
                                blockTexture(ModBlocks.MOON_GRASS_1.get()))
                        .renderType("cutout"));

        saplingBlock(ModBlocks.MOON_SAPLING);


        simpleBlockWithItem(ModBlocks.MOON_VINE.get(),
                models().cross("moon_vine", modLoc("block/moon_vine"))
                        .renderType("cutout"));

        simpleBlockWithItem(ModBlocks.MOON_CRYSTAL_GLASS.get(),
                models().cubeAll("moon_crystal_glass",
                                modLoc("block/moon_crystal_glass"))
                        .renderType("translucent"));

        ResourceLocation glassTexture = blockTexture(ModBlocks.MOON_CRYSTAL_GLASS.get());
        ModelFile post = models().withExistingParent("moon_crystal_glass_pane_post",
                        "block/template_glass_pane_post")
                .texture("pane", glassTexture)
                .texture("edge", glassTexture)
                .renderType("translucent");
        ModelFile side = models().withExistingParent("moon_crystal_glass_pane_side",
                        "block/template_glass_pane_side")
                .texture("pane", glassTexture)
                .texture("edge", glassTexture)
                .renderType("translucent");
        ModelFile sideAlt = models().withExistingParent("moon_crystal_glass_pane_side_alt",
                        "block/template_glass_pane_side_alt")
                .texture("pane", glassTexture)
                .texture("edge", glassTexture)
                .renderType("translucent");
        ModelFile noSide = models().withExistingParent("moon_crystal_glass_pane_noside",
                        "block/template_glass_pane_noside")
                .texture("pane", glassTexture)
                .texture("edge", glassTexture)
                .renderType("translucent");
        ModelFile noSideAlt = models().withExistingParent("moon_crystal_glass_pane_noside_alt",
                        "block/template_glass_pane_noside_alt")
                .texture("pane", glassTexture)
                .texture("edge", glassTexture)
                .renderType("translucent");
        paneBlock((IronBarsBlock) ModBlocks.MOON_CRYSTAL_GLASS_PANE.get(),
                post, side, sideAlt, noSide, noSideAlt);

        getVariantBuilder(ModBlocks.MOON_CRYSTAL.get()).forAllStates(state -> {
            Direction dir = state.getValue(AmethystClusterBlock.FACING);

            ModelFile modelFile = models().withExistingParent("moon_crystal_" + dir.getName(), "block/cross")
                    .texture("cross", modLoc("block/moon_crystal"))
                    .renderType("cutout");

            int rotationX = 0;
            int rotationY = 0;

            // Правильные повороты для всех направлений
            switch(dir) {
                case DOWN:
                    rotationX = 180;
                    rotationY = 0;
                    break;
                case UP:
                    rotationX = 0;
                    rotationY = 0;
                    break;
                case NORTH:
                    rotationX = 90;
                    rotationY = 0;
                    break;
                case SOUTH:
                    rotationX = 90;
                    rotationY = 180;
                    break;
                case WEST:
                    rotationX = 90;
                    rotationY = 270;
                    break;
                case EAST:
                    rotationX = 90;
                    rotationY = 90;
                    break;
            }

            return ConfiguredModel.builder()
                    .modelFile(modelFile)
                    .rotationX(rotationX)
                    .rotationY(rotationY)
                    .build();
        });
        simpleBlockItem(ModBlocks.MOON_CRYSTAL.get(),
                models().withExistingParent("moon_crystal", "item/generated")
                        .texture("layer0", modLoc("block/moon_crystal"))
                        .renderType("cutout"));

        simpleBlockWithItem(ModBlocks.FROST.get(),
                models().withExistingParent("frost", "block/snow_height2")
                        .texture("particle", modLoc("block/frost"))
                        .texture("texture", modLoc("block/frost"))
                        .renderType("translucent"));

        simpleBlockWithItem(ModBlocks.CATNIP_WINE.get(),
                models().getExistingFile(modLoc("block/catnip_wine")));

        getVariantBuilder(ModBlocks.KITSUNE_STATUE.get()).forAllStates(state -> {
            Direction dir = state.getValue(KitsuneStatueBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/kitsune_statue")))
                    .rotationY((int) dir.toYRot())
                    .build();
        });
        simpleBlockItem(ModBlocks.KITSUNE_STATUE.get(),
                models().getExistingFile(modLoc("block/kitsune_statue")));

        getVariantBuilder(ModBlocks.SAKE.get()).forAllStates(state -> {
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/sake")))
                    .build();
        });
        simpleBlockItem(ModBlocks.SAKE.get(),
                models().getExistingFile(modLoc("block/sake")));

        simpleBlockWithItem(ModBlocks.WILD_CATNIP.get(),
                models().cross("wild_catnip", modLoc("block/wild_catnip"))
                        .renderType("cutout"));

        makeCatnipCrop((CropBlock) ModBlocks.CATNIP_CROP.get(), "catnip_stage_", "catnip_stage_");

    }

    private void frostBlock(RegistryObject<Block> block) {
        models().withExistingParent(block.getId().getPath(), "block/snow_height2")
                .texture("particle", modLoc("block/frost"))
                .texture("texture", modLoc("block/frost"));
        getVariantBuilder(block.get()).forAllStates(state -> {
            int layers = state.getValue(SnowLayerBlock.LAYERS);
            return new ConfiguredModel[]{
                    new ConfiguredModel(models().getExistingFile(
                            modLoc("block/frost_height" + layers * 2)
                    ))
            };
        });}


    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }


    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(),
                                new ResourceLocation("minecraft:block/leaves"),
                                "all", blockTexture(blockRegistryObject.get()))
                        .renderType("cutout_mipped")); // Используйте "cutout_mipped" для листвы
    }
    private void logBlock(RotatedPillarBlock block, ResourceLocation sideTexture, ResourceLocation endTexture) {
        axisBlock(block, sideTexture, endTexture);
    }

    public void makeCrowberryShrub(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> crowberryStates(state, block, modelName, textureName);
        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] crowberryStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CrowberryShrubBlock) block).getAgeProperty()),
                new ResourceLocation(fantastikmod.MOD_ID, "block/" + textureName + state.getValue(((CrowberryShrubBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void makeStrawberryCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> strawberryStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }


    private ConfiguredModel[] strawberryStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((StrawberryCropBlock) block).getAgeProperty()),
                new ResourceLocation(fantastikmod.MOD_ID, "block/" + textureName + state.getValue(((StrawberryCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void makeTeaCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> teaStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] teaStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((TeaCropBlock) block).getAgeProperty()),
                new ResourceLocation(fantastikmod.MOD_ID, "block/" + textureName + state.getValue(((TeaCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void makeRiceCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> riceStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] riceStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RiceCropBlock) block).getAgeProperty()),
                new ResourceLocation(fantastikmod.MOD_ID, "block/" + textureName + state.getValue(((RiceCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private int getXRotation(Direction dir) {
        return switch (dir) {
            case DOWN -> 180;
            case UP -> 0;
            default -> 90; // Для всех горизонтальных
        };
    }

    private int getYRotation(Direction dir) {
        if (!dir.getAxis().isHorizontal()) return 0;
        return switch (dir) {
            case NORTH -> 0;
            case SOUTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
    }

    public void makeCatnipCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> catnipStates(state, block, modelName, textureName);
        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] catnipStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        int age = state.getValue(((CatnipCropBlock) block).getAgeProperty());
        models[0] = new ConfiguredModel(models().crop(modelName + age,
                new ResourceLocation(fantastikmod.MOD_ID, "block/" + textureName + age)).renderType("cutout"));
        return models;
    }

}