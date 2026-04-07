package net.lisalaf.fantastikmod.datagen;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("removal")
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, fantastikmod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        registerSimpleItem("egg_kitsune_light");
        registerSimpleItem("egg_ice_dragon");
        registerSimpleItem("egg_blue_butterfly");
        registerSimpleItem("egg_bakeneko");
        registerSimpleItem("fur_ice_dragon");
        registerSimpleItem("heart_ice_dragon");
        registerSimpleItem("egg_moon_deer");
        registerSimpleItem("tofu");
        registerSimpleItem("gemkitsune");
        registerSimpleItem("soul_tamed_kitsune");
        registerSimpleItem("lively_soul_tamed_kitsune");
        registerSimpleItem("moonmascot");
        registerSimpleItem("auripigment");
        registerSimpleItem("silver_raw");
        registerSimpleItem("gem_moon");
        registerSimpleItem("silver_ingot");
        registerSimpleItem("moon_dust");
        registerSimpleItem("moon_lamp");
        registerSimpleItem("strawberry");
        registerSimpleItem("rice");
        registerSimpleItem("tea_leaf");
        registerSimpleItem("matcha_powder");
        registerSimpleItem("tea_leafs_green");
        registerSimpleItem("tea_leafs_black");
        registerSimpleItem("matcha_mochi");
        registerSimpleItem("strawberry_mochi");
        registerSimpleItem("mochi");
        registerSimpleItem("black_tea");
        registerSimpleItem("green_tea");
        registerSimpleItem("matcha_tea");
        registerSimpleItem("dough_mochi");
        registerSimpleItem("tea_ceremony_music_disc");
        registerSimpleItem("moon_axe");
        registerSimpleItem("moon_pickaxe");
        registerSimpleItem("moon_shovel");
        registerSimpleItem("moon_sword");
        registerSimpleItem("moon_crowberry");

        registerBlockItem("moon_crystal_glass");
        registerSimpleItem("moon_crystal_glass_pane", "block/moon_crystal_glass");
        registerBlockItem("moon_crystal");

        registerSimpleItem("strawberry_seeds");
        registerSimpleItem("tea_seeds");

        registerBlockItem("drying_basket");

        registerBlockItem("ash_block");
        registerBlockItem("moon_crystal_block");
        registerBlockItem("auripigment_block");
        registerBlockItem("gemkitsune_block");
        registerBlockItem("tree_moon_log_block");
        registerBlockItem("moon_planks");
        registerBlockItem("tree_moon_foliage_block");
        registerBlockItem("auripigment_ore_block");
        registerBlockItem("silver_ore");
        registerBlockItem("moonstone");
        registerBlockItem("moon_ore");
        registerSimpleItem("silver_stick");
        registerBlockItem("moon_gem_block");
        registerBlockItem("silver_block");

        registerSimpleItem("totem_blueprint");


        registerBlockItem("moon_planks_stairs");
        registerBlockItem("moon_planks_slab");
        registerBlockItem("moon_planks_button");
        registerBlockItem("moon_planks_plate");

        registerDoorItem("moon_tree_door");
        registerFenceItem("moon_planks_fence", "moon_planks");
        registerWallItem("moon_planks_wall", "moon_planks");
        trapdoorItem(ModBlocks.MOON_TREE_TRAPDOOR);
        fenceGateItem(ModBlocks.MOON_PLANKS_FENCE_GATE, ModBlocks.MOON_PLANKS);
        
        registerHandheldItem("sword_auripigment");
        registerHandheldItem("silver_sword");
        registerHandheldItem("silver_pickaxe");
        registerHandheldItem("silver_axe");
        registerHandheldItem("silver_shovel");
        registerHandheldItem("silver_hoe");

        withExistingParent("frost", "block/snow_height2")
                .texture("all", modLoc("block/frost"));

        registerBlockItemBlockTexture("spider_lily");
        registerBlockItemBlockTexture("moon_lily");
        registerBlockItemBlockTexture("moon_northern_bluebell");
        registerBlockItemBlockTexture("moon_heather");

        registerBlockItemBlockTexture("moon_grass");
        registerBlockItemBlockTexture("moon_grass_1");
        registerBlockItem("catnip_wine");
        registerBlockItem("kitsune_statue");
        registerBlockItem("sake");
        withExistingParent("sake_drink", modLoc("block/sake"));
        registerSimpleItem("catnip");
        registerSimpleItem("dried_catnip");
        registerSimpleItem("catnip_seeds");

        registerBlockItem("wild_catnip");

        saplingItem(ModBlocks.MOON_SAPLING);

        registerArmorItems();

        for (int i = 1; i <= 6; i++) {
            withExistingParent("note_" + i, mcLoc("item/written_book"));
        }
    }


    private void registerArmorItems() {
        registerArmorItem("auripigment_helmet");
        registerArmorItem("auripigment_chestplate");
        registerArmorItem("auripigment_leggings");
        registerArmorItem("auripigment_boots");

        registerArmorItem("moon_helmet");
        registerArmorItem("moon_chestplate");
        registerArmorItem("moon_leggings");
        registerArmorItem("moon_boots");

        registerArmorItem("helmet_fur_ice_dragon");
        registerArmorItem("chestplate_fur_ice_dragon");
        registerArmorItem("leggings_fur_ice_dragon");
        registerArmorItem("boots_fur_ice_dragon");


        registerArmorItem("silver_helmet");
        registerArmorItem("silver_chestplate");
        registerArmorItem("silver_leggings");
        registerArmorItem("silver_boots");

    }

    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(fantastikmod.MOD_ID,"block/" + item.getId().getPath()));
    }

    private void registerBlockItem(String blockName) {
        withExistingParent(blockName, new ResourceLocation(fantastikmod.MOD_ID, "block/" + blockName));
    }

    private void registerBlockItemBlockTexture(String blockName) {
        withExistingParent(blockName, new ResourceLocation(fantastikmod.MOD_ID, "block/" + blockName));
    }

    private void registerSimpleItem(String itemName) {
        withExistingParent(itemName, "item/generated")
                .texture("layer0", new ResourceLocation(fantastikmod.MOD_ID, "item/" + itemName));
    }

    private void registerDoorItem(String doorName) {
        withExistingParent(doorName, "item/generated")
                .texture("layer0", new ResourceLocation(fantastikmod.MOD_ID, "item/" + doorName));
    }

    private void registerFenceItem(String fenceName, String baseTexture) {
        withExistingParent(fenceName, "block/fence_inventory")
                .texture("texture", new ResourceLocation(fantastikmod.MOD_ID, "block/" + baseTexture));
    }

    private void registerFenceGateItem(String fenceGateName, String baseTexture) {
        withExistingParent(fenceGateName, "block/block");
    }

    public void fenceGateItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(fantastikmod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private void registerWallItem(String wallName, String baseTexture) {
        withExistingParent(wallName, "block/wall_inventory")
                .texture("wall", new ResourceLocation(fantastikmod.MOD_ID, "block/" + baseTexture));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    private void registerHandheldItem(String itemName) {
        withExistingParent(itemName, "item/handheld")
                .texture("layer0", new ResourceLocation(fantastikmod.MOD_ID, "item/" + itemName));
    }

    private void registerArmorItem(String armorName) {
        withExistingParent(armorName, "item/generated")
                .texture("layer0", new ResourceLocation(fantastikmod.MOD_ID, "item/" + armorName));
    }

    private void registerSimpleItem(String itemName, String texturePath) {
        withExistingParent(itemName, "item/generated")
                .texture("layer0", new ResourceLocation(fantastikmod.MOD_ID, texturePath));
    }

}
