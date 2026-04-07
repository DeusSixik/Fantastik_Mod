package net.lisalaf.fantastikmod.datagen;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SAKE_DRINK.get(), 1)
                .requires(ModBlocks.SAKE.get())
                .unlockedBy("has_sake", has(ModBlocks.SAKE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STRAWBERRY_SEEDS.get(), 1)
                .requires(ModItems.STRAWBERRY.get())
                .unlockedBy("has_strawberry", has(ModItems.STRAWBERRY.get()))
                .save(pWriter, "strawberry_seeds_from_strawberry");

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SAKE.get(), 1)
                .pattern("   ")
                .pattern(" S ")
                .pattern("   ")
                .define('S', ModItems.SAKE_DRINK.get())
                .unlockedBy("has_sake_drink", has(ModItems.SAKE_DRINK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SAKE_DRINK.get(), 1)
                .requires(ModItems.RICE.get())
                .requires(Items.SUGAR)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.WATER_BUCKET)
                .requires(Items.BROWN_MUSHROOM)
                .unlockedBy("has_rice", has(ModItems.RICE.get()))
                .save(pWriter, "sake_drink_from_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModBlocks.CATNIP_WINE.get(), 1)
                .requires(ModItems.CATNIP.get(), 2)
                .requires(Items.SUGAR)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.WATER_BUCKET)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .requires(Items.SWEET_BERRIES)
                .unlockedBy("has_catnip", has(ModItems.CATNIP.get()))
                .save(pWriter);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.CATNIP.get()),
                        RecipeCategory.FOOD,
                        ModItems.DRIED_CATNIP.get(),
                        0.35f,
                        200)
                .unlockedBy("has_catnip", has(ModItems.CATNIP.get()))
                .save(pWriter, "dried_catnip_from_smelting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CATNIP_SEEDS.get(), 1)
                .requires(ModItems.CATNIP.get())
                .unlockedBy("has_catnip", has(ModItems.CATNIP.get()))
                .save(pWriter, "catnip_seeds_from_catnip");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GEMKITSUNE.get(), 9)
                .requires(ModBlocks.GEMKITSUNE_BLOCK.get())
                .unlockedBy("has_gemkitsune_block", has(ModBlocks.GEMKITSUNE_BLOCK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 9)
                .requires(ModBlocks.SILVER_BLOCK.get())
                .unlockedBy("has_silver_block", has(ModBlocks.SILVER_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.TOTEM_OF_UNDYING, 1)
                .pattern("btb")
                .pattern("gag")
                .pattern("ggg")
                .define('g', Items.GOLD_BLOCK)
                .define('b', Items.EMERALD)
                .define('a', ModBlocks.AURIPIGMENT_BLOCK.get())
                .define('t', ModItems.TOTEM_BLUEPRINT.get())
                .unlockedBy("has_totem_blueprint", has(ModItems.TOTEM_BLUEPRINT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOTEM_BLUEPRINT.get(), 2)
                .pattern("prp")
                .pattern("pbp")
                .pattern("prp")
                .define('p', Items.PAPER)
                .define('r', ModItems.AURIPIGMENT.get())
                .define('b', ModItems.TOTEM_BLUEPRINT.get())
                .unlockedBy("has_totem_blueprint", has(ModItems.TOTEM_BLUEPRINT.get()))
                .save(pWriter, "totem_blueprint_copy");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GEM_MOON.get(), 9)
                .requires(ModBlocks.MOON_GEM_BLOCK.get())
                .unlockedBy("has_moon_gem_block", has(ModBlocks.MOON_GEM_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GEMKITSUNE_BLOCK.get())
                .pattern("ggg")
                .pattern("ggg")
                .pattern("ggg")
                .define('g', ModItems.GEMKITSUNE.get())
                .unlockedBy("has_gemkitsune", has(ModItems.GEMKITSUNE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AURIPIGMENT.get(), 9)
                .requires(ModBlocks.AURIPIGMENT_BLOCK.get())
                .unlockedBy("has_auripigment_block", has(ModBlocks.AURIPIGMENT_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AURIPIGMENT_BLOCK.get())
                .pattern("ggg")
                .pattern("ggg")
                .pattern("ggg")
                .define('g', ModItems.AURIPIGMENT.get())
                .unlockedBy("has_auripigment", has(ModItems.AURIPIGMENT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SILVER_BLOCK.get())
                .pattern("ggg")
                .pattern("ggg")
                .pattern("ggg")
                .define('g', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MOON_GEM_BLOCK.get())
                .pattern("ggg")
                .pattern("ggg")
                .pattern("ggg")
                .define('g', ModItems.GEM_MOON.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MOONMASCOT.get())
                .pattern("f f")
                .pattern(" s ")
                .pattern("mmm")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('m', ModItems.MOON_DUST.get())
                .define('f', Items.STRING)
                .unlockedBy("has_soul_tamed_kitsune", has(ModItems.MOONMASCOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LIVELY_SOUL_TAMED_KITSUNE.get())
                .pattern("   ")
                .pattern("fsf")
                .pattern(" t ")
                .define('s', ModItems.SOUL_TAMED_KITSUNE.get())
                .define('t', Items.SOUL_TORCH)
                .define('f', Items.BLAZE_POWDER)
                .unlockedBy("has_soul_tamed_kitsune", has(ModItems.SOUL_TAMED_KITSUNE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_PLANKS.get(), 4)
                .requires(ModBlocks.TREE_MOON_LOG_BLOCK.get())
                .unlockedBy("has_tree_moon_log_block", has(ModBlocks.TREE_MOON_LOG_BLOCK.get()))
                .save(pWriter);

        // Серебряный меч
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILVER_SWORD.get(), 1)
                .pattern(" s ")
                .pattern(" s ")
                .pattern(" k ")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('k', Items.STICK)
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

// Серебряная кирка
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILVER_PICKAXE.get(), 1)
                .pattern("sss")
                .pattern(" k ")
                .pattern(" k ")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('k', Items.STICK)
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

// Серебряный топор
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILVER_AXE.get(), 1)
                .pattern("ss ")
                .pattern("sk ")
                .pattern(" k ")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('k', Items.STICK)
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

// Серебряная лопата
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILVER_SHOVEL.get(), 1)
                .pattern(" s ")
                .pattern(" k ")
                .pattern(" k ")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('k', Items.STICK)
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

// Серебряная мотыга
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILVER_HOE.get(), 1)
                .pattern("ss ")
                .pattern(" k ")
                .pattern(" k ")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('k', Items.STICK)
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SWORD_AURIPIGMENT.get(), 1)
                .pattern(" a ")
                .pattern(" a ")
                .pattern(" s ") // третья строка
                .define('a', ModItems.AURIPIGMENT.get())
                .define('s', Items.STICK)
                .unlockedBy("has_auripigment", has(ModItems.AURIPIGMENT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AURIPIGMENT_BOOTS.get(), 1)
                .pattern("a a")
                .pattern("a a")
                .pattern("   ") // третья строка
                .define('a', ModItems.AURIPIGMENT.get())
                .unlockedBy("has_auripigment", has(ModItems.AURIPIGMENT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AURIPIGMENT_HELMET.get(), 1)
                .pattern("aaa")
                .pattern("a a")
                .pattern("   ") // третья строка
                .define('a', ModItems.AURIPIGMENT.get())
                .unlockedBy("has_auripigment", has(ModItems.AURIPIGMENT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AURIPIGMENT_LEGGINGS.get(), 1)
                .pattern("aaa")
                .pattern("a a")
                .pattern("a a") // третья строка
                .define('a', ModItems.AURIPIGMENT.get())
                .unlockedBy("has_auripigment", has(ModItems.AURIPIGMENT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AURIPIGMENT_CHESTPLATE.get(), 1)
                .pattern("a a")
                .pattern("aaa")
                .pattern("aaa") // третья строка
                .define('a', ModItems.AURIPIGMENT.get())
                .unlockedBy("has_auripigment", has(ModItems.AURIPIGMENT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MOON_BOOTS.get(), 1)
                .pattern("g g")
                .pattern("g g")
                .pattern("   ")
                .define('g', ModItems.GEM_MOON.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MOON_HELMET.get(), 1)
                .pattern("ggg")
                .pattern("g g")
                .pattern("   ")
                .define('g', ModItems.GEM_MOON.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MOON_LEGGINGS.get(), 1)
                .pattern("ggg")
                .pattern("g g")
                .pattern("g g")
                .define('g', ModItems.GEM_MOON.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MOON_CHESTPLATE.get(), 1)
                .pattern("g g")
                .pattern("ggg")
                .pattern("ggg")
                .define('g', ModItems.GEM_MOON.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        // Шлем
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILVER_HELMET.get(), 1)
                .pattern("sss")
                .pattern("s s")
                .pattern("   ")
                .define('s', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

// Нагрудник
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILVER_CHESTPLATE.get(), 1)
                .pattern("s s")
                .pattern("sss")
                .pattern("sss")
                .define('s', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

// Поножи
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILVER_LEGGINGS.get(), 1)
                .pattern("sss")
                .pattern("s s")
                .pattern("s s")
                .define('s', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

// Ботинки
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILVER_BOOTS.get(), 1)
                .pattern("s s")
                .pattern("s s")
                .pattern("   ")
                .define('s', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BOOTS_FUR_ICE_DRAGON.get(), 1)
                .pattern("i i")
                .pattern("a a")
                .pattern("a a")
                 // третья строка
                .define('a', ModItems.FUR_ICE_DRAGON.get())
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_fur_ice_dragon", has(ModItems.FUR_ICE_DRAGON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HELMET_FUR_ICE_DRAGON.get(), 1)
                .pattern("aaa")
                .pattern("a a")
                .pattern("i i") // третья строка
                .define('a', ModItems.FUR_ICE_DRAGON.get())
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_fur_ice_dragon", has(ModItems.FUR_ICE_DRAGON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LEGGINGS_FUR_ICE_DRAGON.get(), 1)
                .pattern("iai")
                .pattern("a a")
                .pattern("a a") // третья строка
                .define('a', ModItems.FUR_ICE_DRAGON.get())
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_fur_ice_dragon", has(ModItems.FUR_ICE_DRAGON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CHESTPLATE_FUR_ICE_DRAGON.get(), 1)
                .pattern("i i")
                .pattern("aaa")
                .pattern("aaa") // третья строка
                .define('a', ModItems.FUR_ICE_DRAGON.get())
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_fur_ice_dragon", has(ModItems.FUR_ICE_DRAGON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.MOON_TREE_DOOR.get(), 3)
                .pattern("dd")
                .pattern("dd")
                .pattern("dd")
                .define('d', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.MOON_PLANKS_BUTTON.get(), 1)
                .requires(ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

        // КАЛИТКА (FENCE GATE) - исправлено
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_PLANKS_FENCE_GATE.get(), 1) // обычно 1 калитка
                .pattern("sds")
                .pattern("sds")
                .pattern("   ") // третья строка (можно использовать пробелы или пустую строку)
                .define('s', Items.STICK)
                .define('d', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

// СТЕНА (WALL) - исправлено
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_WALL.get(), 6)
                .pattern("ddd")
                .pattern("ddd")
                .pattern("   ") // третья строка
                .define('d', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

// ЗАБОР (FENCE) - исправлено
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_PLANKS_FENCE.get(), 3) // обычно 3 секции забора
                .pattern("dsd")
                .pattern("dsd")
                .pattern("   ") // третья строка
                .define('d', ModBlocks.MOON_PLANKS.get())
                .define('s', Items.STICK)
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

// ПЛИТА (SLAB) - исправлено
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_PLANKS_SLAB.get(), 6)
                .pattern("ddd")
                .pattern("   ")
                .pattern("   ") // три строки всего
                .define('d', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

// НАЖИМНАЯ ПЛАСТИНА (PRESSURE PLATE) - исправлено
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.MOON_PLANKS_PLATE.get(), 1)
                .pattern("dd")
                .pattern("  ")
                .pattern("  ") // три строки
                .define('d', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

// ЛЮК (TRAPDOOR) - исправлено (у вас уже правильно)
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.MOON_TREE_TRAPDOOR.get(), 2)
                .pattern("ddd")
                .pattern("ddd")
                .pattern("   ") // третья строка
                .define('d', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

// СТУПЕНЬКИ (STAIRS) - исправлено
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_PLANKS_STAIRS.get(), 4)
                .pattern("d  ")
                .pattern("dd ")
                .pattern("ddd") // три строки
                .define('d', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.DRYING_BASKET.get())
                .pattern("sts")
                .pattern("tst")
                .pattern("sts")
                .define('s', Items.STRING)
                .define('t', Items.STICK)
                .unlockedBy("has_milk", has(Items.STRING))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.TOFU.get())
                .pattern("mmm")
                .pattern("ses")
                .pattern(" b ")
                .define('m', Items.MILK_BUCKET)
                .define('s', Items.SUGAR)
                .define('e', Items.EGG)
                .define('b', Items.BOWL)
                .unlockedBy("has_milk", has(Items.MILK_BUCKET))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.MATCHA_TEA.get())
                .pattern("mt ")
                .pattern("s  ")
                .pattern("   ")
                .define('m', Items.WATER_BUCKET)
                .define('s', Items.GLASS_BOTTLE)
                .define('t', ModItems.MATCHA_POWDER.get())
                .unlockedBy("has_matcha_powder", has(ModItems.MATCHA_POWDER.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.BLACK_TEA.get())
                .pattern("mt ")
                .pattern("s  ")
                .pattern("   ")
                .define('m', Items.WATER_BUCKET)
                .define('s', Items.GLASS_BOTTLE)
                .define('t', ModItems.TEA_LEAFS_BLACK.get())
                .unlockedBy("has_tea_leafs_black", has(ModItems.TEA_LEAFS_BLACK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.GREEN_TEA.get())
                .pattern("mt ")
                .pattern("s  ")
                .pattern("   ")
                .define('m', Items.WATER_BUCKET)
                .define('s', Items.GLASS_BOTTLE)
                .define('t', ModItems.TEA_LEAFS_GREEN.get())
                .unlockedBy("has_tea_leafs_green", has(ModItems.TEA_LEAFS_GREEN.get()))
                .save(pWriter);


        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.TEA_LEAFS_GREEN.get()),
                        RecipeCategory.FOOD,
                        ModItems.TEA_LEAFS_BLACK.get(),
                        0.35f,  // Опыт
                        200)    // Время в тиках (10 секунд)
                .unlockedBy("has_tea_leafs_green", has(ModItems.TEA_LEAFS_GREEN.get()))
                .save(pWriter, "black_tea_from_smelting");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.DOUGH_MOCHI.get(), 3)
                .pattern("rrr")
                .pattern("   ")
                .pattern("   ") // три строки всего
                .define('r', ModItems.RICE.get())
                .unlockedBy("has_rice", has(ModItems.RICE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MOCHI.get(), 1)
                .requires(ModItems.DOUGH_MOCHI.get())
                .unlockedBy("has_dough_mochi", has(ModItems.DOUGH_MOCHI.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.STRAWBERRY_MOCHI.get(), 1)
                .requires(ModItems.DOUGH_MOCHI.get())
                .requires(ModItems.STRAWBERRY.get())
                .unlockedBy("has_dough_mochi", has(ModItems.DOUGH_MOCHI.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MATCHA_MOCHI.get(), 1)
                .requires(ModItems.DOUGH_MOCHI.get())
                .requires(ModItems.MATCHA_POWDER.get())
                .unlockedBy("has_dough_mochi", has(ModItems.DOUGH_MOCHI.get()))
                .save(pWriter);

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ModItems.TEA_LEAF.get()),
                        RecipeCategory.FOOD,
                        ModItems.MATCHA_POWDER.get(),
                        0.35f,
                        100)   // 5 секунд
                .unlockedBy("has_tea_leaf", has(ModItems.TEA_LEAF.get()))
                .save(pWriter, "matcha_from_smoking");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.SILVER_RAW.get()),
                        RecipeCategory.MISC,
                        ModItems.SILVER_INGOT.get(),
                        0.35f,  // Опыт
                        200)    // Время в тиках (10 секунд)
                .unlockedBy("has_silver_raw", has(ModItems.SILVER_RAW.get()))
                .save(pWriter, "silver_ingot_from_smelting");

        // Крафт серебряной палки
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILVER_STICK.get(), 2)
                .pattern("s  ")
                .pattern("s  ")
                .define('s', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_silver_ingot", has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

// Крафт лунных инструментов
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MOON_PICKAXE.get(), 1)
                .pattern("ggg")
                .pattern(" s ")
                .pattern(" s ")
                .define('g', ModItems.GEM_MOON.get())
                .define('s', ModItems.SILVER_STICK.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MOON_AXE.get(), 1)
                .pattern("gg ")
                .pattern("gs ")
                .pattern(" s ")
                .define('g', ModItems.GEM_MOON.get())
                .define('s', ModItems.SILVER_STICK.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MOON_SHOVEL.get(), 1)
                .pattern(" g ")
                .pattern(" s ")
                .pattern(" s ")
                .define('g', ModItems.GEM_MOON.get())
                .define('s', ModItems.SILVER_STICK.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MOON_SWORD.get(), 1)
                .pattern(" g ")
                .pattern(" g ")
                .pattern(" s ")
                .define('g', ModItems.GEM_MOON.get())
                .define('s', ModItems.SILVER_STICK.get())
                .unlockedBy("has_gem_moon", has(ModItems.GEM_MOON.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.STICK, 4)
                .pattern("P")
                .pattern("P")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "sticks_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.CRAFTING_TABLE, 1)
                .pattern("PP")
                .pattern("PP")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "crafting_table_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.CHEST, 1)
                .pattern("PPP")
                .pattern("P P")
                .pattern("PPP")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "chest_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_PICKAXE, 1)
                .pattern("PPP")
                .pattern(" S ")
                .pattern(" S ")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .define('S', Items.STICK)
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "wooden_pickaxe_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_AXE, 1)
                .pattern("PP ")
                .pattern("PS ")
                .pattern(" S ")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .define('S', Items.STICK)
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "wooden_axe_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_SHOVEL, 1)
                .pattern(" P ")
                .pattern(" S ")
                .pattern(" S ")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .define('S', Items.STICK)
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "wooden_shovel_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_HOE, 1)
                .pattern("PP ")
                .pattern(" S ")
                .pattern(" S ")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .define('S', Items.STICK)
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "wooden_hoe_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.WOODEN_SWORD, 1)
                .pattern(" P ")
                .pattern(" P ")
                .pattern(" S ")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .define('S', Items.STICK)
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "wooden_sword_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BOWL, 4)
                .pattern("P P")
                .pattern(" P ")
                .define('P', ModBlocks.MOON_PLANKS.get())
                .unlockedBy("has_moon_planks", has(ModBlocks.MOON_PLANKS.get()))
                .save(pWriter, "bowl_from_moon_planks");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_CRYSTAL_GLASS_PANE.get(), 16)
                .pattern("ggg")
                .pattern("ggg")
                .define('g', ModBlocks.MOON_CRYSTAL_GLASS.get())
                .unlockedBy("has_moon_crystal_glass", has(ModBlocks.MOON_CRYSTAL_GLASS.get()))
                .save(pWriter, "moon_crystal_glass_pane_from_glass");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.MOON_DUST.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.MOON_CRYSTAL_GLASS.get().asItem(),
                        0.2f,
                        200)
                .unlockedBy("has_moon_dust", has(ModItems.MOON_DUST.get()))
                .save(pWriter, "moon_crystal_glass_from_smelting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MOON_DUST.get(), 2)
                .requires(ModBlocks.MOON_CRYSTAL.get())
                .unlockedBy("has_moon_crystal", has(ModBlocks.MOON_CRYSTAL.get()))
                .save(pWriter, "moon_dust_from_crystal");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MOON_DUST.get(), 9)
                .requires(ModBlocks.MOON_CRYSTAL_BLOCK.get())
                .unlockedBy("has_moon_crystal_block", has(ModBlocks.MOON_CRYSTAL_BLOCK.get()))
                .save(pWriter, "moon_dust_from_block");
    }
}