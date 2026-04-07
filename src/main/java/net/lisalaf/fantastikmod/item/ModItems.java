package net.lisalaf.fantastikmod.item;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.entity.ModEntities;
import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.item.custom.ModArmorItem;
import net.lisalaf.fantastikmod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, fantastikmod.MOD_ID);

    public static final RegistryObject<Item> GEMKITSUNE = ITEMS.register("gemkitsune",
            ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> SOUL_TAMED_KITSUNE = ITEMS.register("soul_tamed_kitsune",
            ()-> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LIVELY_SOUL_TAMED_KITSUNE = ITEMS.register("lively_soul_tamed_kitsune",
            ()-> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MOONMASCOT = ITEMS.register("moonmascot",
            ()-> new Item( new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SILVER_STICK = ITEMS.register("silver_stick",
            ()-> new Item( new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> FUR_ICE_DRAGON = ITEMS.register("fur_ice_dragon",
            ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> HEART_ICE_DRAGON = ITEMS.register("heart_ice_dragon",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.HEART_ICE_DRAGON)));

    public static final RegistryObject<Item> TEA_CEREMONY_MUSIC_DISC = ITEMS.register("tea_ceremony_music_disc",
            () -> new RecordItem(6, ModSounds.TEA_CEREMONY,  // ← обратите внимание
                    new Item.Properties().stacksTo(1), 1560));


    //public static final RegistryObject<Item> TEA_CEREMORY_MUSIC_DISC = ITEMS.register("tea_ceremony_music_disc", ()-> new RecordItem(6, ModSounds.TEA_CEREMORY, new Item.Properties().stacksTo(1), 1560));

    public static final RegistryObject<Item> TOFU = ITEMS.register("tofu",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.TOFU)));
    public static final RegistryObject<Item> RICE = ITEMS.register("rice",
            ()-> new ItemNameBlockItem(ModBlocks.RICE_CROP.get(), new Item.Properties().stacksTo(64).food(ModFoods.RICE)));
    public static final RegistryObject<Item> STRAWBERRY = ITEMS.register("strawberry",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.STRAWBERRY)));
    public static final RegistryObject<Item> TEA_LEAF = ITEMS.register("tea_leaf",
            ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> TEA_LEAFS_GREEN = ITEMS.register("tea_leafs_green",
            ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> TEA_LEAFS_BLACK = ITEMS.register("tea_leafs_black",
            ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> MATCHA_POWDER = ITEMS.register("matcha_powder",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.MATCHA_POWDER)));
    public static final RegistryObject<Item> MATCHA_MOCHI = ITEMS.register("matcha_mochi",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.MATCHA_MOCHI)));
    public static final RegistryObject<Item> STRAWBERRY_MOCHI = ITEMS.register("strawberry_mochi",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.STRAWBERRY_MOCHI)));
    public static final RegistryObject<Item> MOCHI = ITEMS.register("mochi",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.MOCHI)));
    public static final RegistryObject<Item> DOUGH_MOCHI = ITEMS.register("dough_mochi",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.DOUGH_MOCHI)));
    public static final RegistryObject<Item> BLACK_TEA = ITEMS.register("black_tea",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.BLACK_TEA)));
    public static final RegistryObject<Item> GREEN_TEA = ITEMS.register("green_tea",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.GREEN_TEA)));
    public static final RegistryObject<Item> MATCHA_TEA = ITEMS.register("matcha_tea",
            ()-> new Item( new Item.Properties().stacksTo(64).food(ModFoods.MATCHA_TEA)));
    public static final RegistryObject<Item> MOON_CROWBERRY = ITEMS.register("moon_crowberry",
            ()-> new  ItemNameBlockItem(ModBlocks.BUSH_CROWBERRY.get(), new Item.Properties().stacksTo(64).food(ModFoods.MOON_CROWBERRY)));
    public static final RegistryObject<Item> SAKE_DRINK = ITEMS.register("sake_drink",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.SAKE)
                    .stacksTo(16)) {
                @Override
                public UseAnim getUseAnimation(ItemStack stack) {
                    return UseAnim.DRINK;
                }
            });

    public static final RegistryObject<Item> CATNIP = ITEMS.register("catnip",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> DRIED_CATNIP = ITEMS.register("dried_catnip",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> CATNIP_SEEDS = ITEMS.register("catnip_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CATNIP_CROP.get(), new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> STRAWBERRY_SEEDS = ITEMS.register("strawberry_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.STRAWBERRY_CROP.get(), new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> TEA_SEEDS = ITEMS.register("tea_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.TEA_CROP.get(), new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> EGG_KITSUNE_LIGHT = ITEMS.register("egg_kitsune_light",
            () -> new ForgeSpawnEggItem(ModEntities.KITSUNE_LIGHT,
                    0xFFFFFF, // Основной цвет (белый)
                    0xFFA500, // Цвет точек (оранжевый)
                    new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> EGG_ICE_DRAGON = ITEMS.register("egg_ice_dragon",
            () -> new ForgeSpawnEggItem(ModEntities.ICE_DRAGON,
                    0xFFFFFF, // Основной цвет (белый)
                    0xFFA500, // Цвет точек (оранжевый)
                    new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> EGG_MOON_DEER = ITEMS.register("egg_moon_deer",
            () -> new ForgeSpawnEggItem(ModEntities.MOON_DEER,
                    0xFFFFFF, // Основной цвет (белый)
                    0xFFA500, // Цвет точек (оранжевый)
                    new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> EGG_BLUE_BUTTERFLY = ITEMS.register("egg_blue_butterfly",
            () -> new ForgeSpawnEggItem(ModEntities.BLUE_BUTTERFLY,
                    0xFFFFFF, // Основной цвет (белый)
                    0xFFA500, // Цвет точек (оранжевый)
                    new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> EGG_BAKENEKO = ITEMS.register("egg_bakeneko",
            () -> new ForgeSpawnEggItem(ModEntities.BAKENEKO,
                    0xFFFFFF, // Основной цвет (белый)
                    0xFFA500, // Цвет точек (оранжевый)
                    new Item.Properties().stacksTo(64)));




    public static final RegistryObject<Item> AURIPIGMENT = ITEMS.register("auripigment",
            ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> GEM_MOON = ITEMS.register("gem_moon",
            ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> SILVER_RAW = ITEMS.register("silver_raw",
            ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> MOON_DUST = ITEMS.register("moon_dust",
           ()-> new Item( new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            ()-> new Item( new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> MOON_LAMP = ITEMS.register("moon_lamp",
            ()-> new Item( new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> AURIPIGMENT_HELMET = ITEMS.register("auripigment_helmet",
            ()-> new ArmorItem(ModArmorMaterials.AURIPIGMENT, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> AURIPIGMENT_CHESTPLATE = ITEMS.register("auripigment_chestplate",
            ()-> new ArmorItem(ModArmorMaterials.AURIPIGMENT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> AURIPIGMENT_LEGGINGS = ITEMS.register("auripigment_leggings",
            ()-> new ModArmorItem(ModArmorMaterials.AURIPIGMENT, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> AURIPIGMENT_BOOTS = ITEMS.register("auripigment_boots",
            ()-> new ArmorItem(ModArmorMaterials.AURIPIGMENT, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> HELMET_FUR_ICE_DRAGON = ITEMS.register("helmet_fur_ice_dragon",
            ()-> new ArmorItem(ModArmorMaterials.FUR_ICE_DRAGON, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> CHESTPLATE_FUR_ICE_DRAGON = ITEMS.register("chestplate_fur_ice_dragon",
            ()-> new ArmorItem(ModArmorMaterials.FUR_ICE_DRAGON, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> LEGGINGS_FUR_ICE_DRAGON = ITEMS.register("leggings_fur_ice_dragon",
            ()-> new ModArmorItem(ModArmorMaterials.FUR_ICE_DRAGON, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> BOOTS_FUR_ICE_DRAGON = ITEMS.register("boots_fur_ice_dragon",
            ()-> new ModArmorItem(ModArmorMaterials.FUR_ICE_DRAGON, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> TOTEM_BLUEPRINT = ITEMS.register("totem_blueprint",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SILVER_HELMET = ITEMS.register("silver_helmet",
            () -> new ModArmorItem(ModArmorMaterials.SILVER, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SILVER_CHESTPLATE = ITEMS.register("silver_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.SILVER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SILVER_LEGGINGS = ITEMS.register("silver_leggings",
            () -> new ModArmorItem(ModArmorMaterials.SILVER, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SILVER_BOOTS = ITEMS.register("silver_boots",
            () -> new ModArmorItem(ModArmorMaterials.SILVER, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> MOON_HELMET = ITEMS.register("moon_helmet",
            () -> new ModArmorItem(ModArmorMaterials.GEM_MOON, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> MOON_CHESTPLATE = ITEMS.register("moon_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.GEM_MOON, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MOON_LEGGINGS = ITEMS.register("moon_leggings",
            () -> new ModArmorItem(ModArmorMaterials.GEM_MOON, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MOON_BOOTS = ITEMS.register("moon_boots",
            () -> new ModArmorItem(ModArmorMaterials.GEM_MOON, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> SWORD_AURIPIGMENT = ITEMS.register("sword_auripigment",
            () -> new SwordItem(ModToolTiers.AURIPIGMENT, 3, -2.4f, new Item.Properties()) {
                @Override
                public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                    target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));

                    if (attacker.getCommandSenderWorld().getRandom().nextFloat() < 0.5f) {
                        target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
                    }

                    return super.hurtEnemy(stack, target, attacker);
                }
            });

    public static final RegistryObject<Item> SILVER_SWORD = ITEMS.register("silver_sword",
            () -> new SwordItem(ModToolTiers.SILVER, 3, -2.0f, new Item.Properties()) {
                @Override
                public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                    // Проверяем является ли цель нежитью
                    if (target instanceof Zombie || target instanceof Skeleton ||
                            target instanceof Phantom || target instanceof WitherSkeleton ||
                            target instanceof ZombifiedPiglin || target instanceof Drowned) {
                        target.hurt(attacker.damageSources().magic(), 5.0F); // +5 магического урона
                    }
                    return super.hurtEnemy(stack, target, attacker);
                }
            });

    public static final RegistryObject<Item> SILVER_PICKAXE = ITEMS.register("silver_pickaxe",
            () -> new PickaxeItem(ModToolTiers.SILVER, 1, -2.8f, new Item.Properties()));

    public static final RegistryObject<Item> SILVER_AXE = ITEMS.register("silver_axe",
            () -> new AxeItem(ModToolTiers.SILVER, 6.0f, -3.0f, new Item.Properties()));

    public static final RegistryObject<Item> SILVER_SHOVEL = ITEMS.register("silver_shovel",
            () -> new ShovelItem(ModToolTiers.SILVER, 1.5f, -3.0f, new Item.Properties()));

    public static final RegistryObject<Item> SILVER_HOE = ITEMS.register("silver_hoe",
            () -> new HoeItem(ModToolTiers.SILVER, -2, -1.0f, new Item.Properties()));

    public static final RegistryObject<Item> MOON_PICKAXE = ITEMS.register("moon_pickaxe",
            () -> new PickaxeItem(ModToolTiers.GEM_MOON, 1, -2.8f, new Item.Properties()) {
                @Override
                public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
                    super.inventoryTick(stack, level, entity, slot, selected);
                    if (entity instanceof Player player && selected && isNightTime(level)) {
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 40, 0));
                    }
                }

                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    ItemStack stack = player.getItemInHand(hand);
                    if (isNightTime(level) && !player.getCooldowns().isOnCooldown(this)) {
                        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 200, 0));
                        player.getCooldowns().addCooldown(this, 400);
                        return InteractionResultHolder.success(stack);
                    }
                    return InteractionResultHolder.pass(stack);
                }
            });

    public static final RegistryObject<Item> MOON_AXE = ITEMS.register("moon_axe",
            () -> new AxeItem(ModToolTiers.GEM_MOON, 6.0f, -3.0f, new Item.Properties()) {
                @Override
                public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
                    super.inventoryTick(stack, level, entity, slot, selected);
                    if (entity instanceof Player player && selected && isNightTime(level)) {
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 40, 0));
                    }
                }
            });

    public static final RegistryObject<Item> MOON_SHOVEL = ITEMS.register("moon_shovel",
            () -> new ShovelItem(ModToolTiers.GEM_MOON, 1.5f, -3.0f, new Item.Properties()) {
                @Override
                public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
                    super.inventoryTick(stack, level, entity, slot, selected);
                    if (entity instanceof Player player && selected && isNightTime(level)) {
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 40, 0));
                    }
                }
            });

    public static final RegistryObject<Item> MOON_SWORD = ITEMS.register("moon_sword",
            () -> new SwordItem(ModToolTiers.GEM_MOON, 3, -2.4f, new Item.Properties()) {
                @Override
                public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                    if (isNightTime(attacker.level())) {
                        target.hurt(attacker.damageSources().mobAttack(attacker), 3.0f);
                    }


                    return super.hurtEnemy(stack, target, attacker);
                }
            });

    public static final RegistryObject<Item> NOTE_1 = ITEMS.register("note_1",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NOTE_2 = ITEMS.register("note_2",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NOTE_3 = ITEMS.register("note_3",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NOTE_4 = ITEMS.register("note_4",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NOTE_5 = ITEMS.register("note_5",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NOTE_6 = ITEMS.register("note_6",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static boolean isNightTime(Level level) {
        if (level == null) return false;
        long time = level.getDayTime() % 24000;
        return time >= 13000 && time < 23000;
    }


}
