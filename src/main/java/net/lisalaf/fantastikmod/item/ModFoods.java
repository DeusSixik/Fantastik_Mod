package net.lisalaf.fantastikmod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties TOFU = new FoodProperties.Builder().nutrition(5).fast()
            .saturationMod(0.7f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100), 0.4f).build();
    public static final FoodProperties RICE = new FoodProperties.Builder().nutrition(1).fast()
            .saturationMod(0.1f).build();
    public static final FoodProperties STRAWBERRY = new FoodProperties.Builder().nutrition(3).fast()
            .saturationMod(0.3f).build();
    public static final FoodProperties MATCHA_POWDER = new FoodProperties.Builder().nutrition(1).fast()
            .saturationMod(0.2f).build();
    public static final FoodProperties DOUGH_MOCHI = new FoodProperties.Builder().nutrition(1).fast()
            .saturationMod(0.2f).build();
    public static final FoodProperties MOCHI = new FoodProperties.Builder().nutrition(5).fast()
            .saturationMod(0.6f).build();
    public static final FoodProperties STRAWBERRY_MOCHI = new FoodProperties.Builder().nutrition(5).fast()
            .saturationMod(0.6f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100), 1.0f).build();
    public static final FoodProperties MATCHA_MOCHI = new FoodProperties.Builder().nutrition(5).fast()
            .saturationMod(0.6f).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100), 1.0f).build();
    public static final FoodProperties BLACK_TEA = new FoodProperties.Builder().nutrition(5).fast()
            .saturationMod(0.3f).effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 80), 0.5f).build();
    public static final FoodProperties GREEN_TEA = new FoodProperties.Builder().nutrition(5).fast()
            .saturationMod(0.3f).effect(() -> new MobEffectInstance(MobEffects.HEAL, 100), 0.4f).build();
    public static final FoodProperties MATCHA_TEA = new FoodProperties.Builder().nutrition(8).fast()
            .saturationMod(0.3f).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 150), 1.0f).build();
    public static final FoodProperties MOON_CROWBERRY = new FoodProperties.Builder().nutrition(1).fast()
            .saturationMod(0.1f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 40), 0.9f).build();

    public static final FoodProperties SAKE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.5f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.3f)
            .build();


    public static final FoodProperties HEART_ICE_DRAGON = new FoodProperties.Builder().nutrition(7).fast()
            .saturationMod(0.9f).effect(() -> new MobEffectInstance(MobEffects.POISON, 120), 0.4f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 170, 2), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 170, 1), 0.5f)
            .build();
}
