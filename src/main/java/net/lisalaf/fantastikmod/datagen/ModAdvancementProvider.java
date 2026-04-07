package net.lisalaf.fantastikmod.datagen;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.effect.ModEffects;
import net.lisalaf.fantastikmod.entity.ModEntities;
import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends ForgeAdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new ModAdvancementGenerator()));
    }

    @SuppressWarnings("removal")
    public static class ModAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper helper) {
            ResourceLocation minecraftRoot = new ResourceLocation("minecraft", "story/root");

            Advancement root = Advancement.Builder.advancement()
                    .display(ModItems.MOONMASCOT.get(),
                            Component.translatable("advancement.fantastikmod.root.title"),
                            Component.translatable("advancement.fantastikmod.root.description"),
                            new ResourceLocation(fantastikmod.MOD_ID, "textures/block/moon_planks.png"),
                            FrameType.TASK, true, true, false)
                    .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "root"), helper);

            Advancement enterBiome = Advancement.Builder.advancement()
                    .parent(root)
                    .display(ModItems.GEM_MOON.get(),
                            Component.translatable("advancement.fantastikmod.enter_biome.title"),
                            Component.translatable("advancement.fantastikmod.enter_biome.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("enter_biome", PlayerTrigger.TriggerInstance.located(
                            LocationPredicate.inBiome(ResourceKey.create(net.minecraft.core.registries.Registries.BIOME,
                                    new ResourceLocation(fantastikmod.MOD_ID, "blue_moon_forest")))))
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "enter_biome"), helper);

            Advancement getAuripigment = Advancement.Builder.advancement()
                    .parent(root)
                    .display(ModItems.AURIPIGMENT.get(),
                            Component.translatable("advancement.fantastikmod.get_auripigment.title"),
                            Component.translatable("advancement.fantastikmod.get_auripigment.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_auripigment", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.AURIPIGMENT.get()))
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "get_auripigment"), helper);

            Advancement fullAuripigmentArmor = Advancement.Builder.advancement()
                    .parent(getAuripigment)
                    .display(ModItems.AURIPIGMENT_CHESTPLATE.get(),
                            Component.translatable("advancement.fantastikmod.full_auripigment_armor.title"),
                            Component.translatable("advancement.fantastikmod.full_auripigment_armor.description"),
                            null, FrameType.GOAL, true, true, false)
                    .addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.AURIPIGMENT_HELMET.get()))
                    .addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.AURIPIGMENT_CHESTPLATE.get()))
                    .addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.AURIPIGMENT_LEGGINGS.get()))
                    .addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.AURIPIGMENT_BOOTS.get()))
                    .requirements(RequirementsStrategy.AND)
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "full_auripigment_armor"), helper);

            Advancement totemBlueprint = Advancement.Builder.advancement()
                    .parent(getAuripigment)
                    .display(ModItems.TOTEM_BLUEPRINT.get(),
                            Component.translatable("advancement.fantastikmod.totem_blueprint.title"),
                            Component.translatable("advancement.fantastikmod.totem_blueprint.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_totem_blueprint", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOTEM_BLUEPRINT.get()))
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "totem_blueprint"), helper);

            Advancement getMoonGem = Advancement.Builder.advancement()
                    .parent(enterBiome)
                    .display(ModItems.GEM_MOON.get(),
                            Component.translatable("advancement.fantastikmod.get_moon_gem.title"),
                            Component.translatable("advancement.fantastikmod.get_moon_gem.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_gem_moon", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GEM_MOON.get()))
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "get_moon_gem"), helper);

            Advancement fullMoonArmor = Advancement.Builder.advancement()
                    .parent(getMoonGem)
                    .display(ModItems.MOON_CHESTPLATE.get(),
                            Component.translatable("advancement.fantastikmod.full_moon_armor.title"),
                            Component.translatable("advancement.fantastikmod.full_moon_armor.description"),
                            null, FrameType.GOAL, true, true, false)
                    .addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MOON_HELMET.get()))
                    .addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MOON_CHESTPLATE.get()))
                    .addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MOON_LEGGINGS.get()))
                    .addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MOON_BOOTS.get()))
                    .requirements(RequirementsStrategy.AND)
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "full_moon_armor"), helper);

            Advancement fullMoonTools = Advancement.Builder.advancement()
                    .parent(getMoonGem)
                    .display(ModItems.MOON_PICKAXE.get(),
                            Component.translatable("advancement.fantastikmod.full_moon_tools.title"),
                            Component.translatable("advancement.fantastikmod.full_moon_tools.description"),
                            null, FrameType.GOAL, true, true, false)
                    .addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MOON_SWORD.get()))
                    .addCriterion("has_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MOON_PICKAXE.get()))
                    .addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MOON_AXE.get()))
                    .addCriterion("has_shovel", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MOON_SHOVEL.get()))
                    .requirements(RequirementsStrategy.OR)
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "full_moon_tools"), helper);

            Advancement tameKitsune = Advancement.Builder.advancement()
                    .parent(root)
                    .display(ModItems.SOUL_TAMED_KITSUNE.get(),
                            Component.translatable("advancement.fantastikmod.tame_kitsune.title"),
                            Component.translatable("advancement.fantastikmod.tame_kitsune.description"),
                            null, FrameType.GOAL, true, true, false)
                    .addCriterion("tame_kitsune", TameAnimalTrigger.TriggerInstance.tamedAnimal(
                            EntityPredicate.Builder.entity().of(ModEntities.KITSUNE_LIGHT.get()).build()))
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "tame_kitsune"), helper);

            Advancement tameMoonDeer = Advancement.Builder.advancement()
                    .parent(root)
                    .display(ModItems.EGG_MOON_DEER.get(),
                            Component.translatable("advancement.fantastikmod.tame_moon_deer.title"),
                            Component.translatable("advancement.fantastikmod.tame_moon_deer.description"),
                            null, FrameType.GOAL, true, true, false)
                    .addCriterion("tame_moon_deer", TameAnimalTrigger.TriggerInstance.tamedAnimal(
                            EntityPredicate.Builder.entity().of(ModEntities.MOON_DEER.get()).build()))
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "tame_moon_deer"), helper);

            Advancement catnipMaster = Advancement.Builder.advancement()
                    .parent(root)
                    .display(ModItems.CATNIP.get(),
                            Component.translatable("advancement.fantastikmod.catnip_master.title"),
                            Component.translatable("advancement.fantastikmod.catnip_master.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_catnip", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CATNIP.get()))
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "catnip_master"), helper);

            Advancement inariBlessing = Advancement.Builder.advancement()
                    .parent(root)
                    .display(ModBlocks.KITSUNE_STATUE.get().asItem(),
                            Component.translatable("advancement.fantastikmod.inari_blessing.title"),
                            Component.translatable("advancement.fantastikmod.inari_blessing.description"),
                            null, FrameType.GOAL, true, true, false)
                    .addCriterion("make_offering", new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(fantastikmod.MOD_ID, "inari_blessing"), helper);
        }
    }
}