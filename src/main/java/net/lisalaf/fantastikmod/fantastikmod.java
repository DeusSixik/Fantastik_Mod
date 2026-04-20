package net.lisalaf.fantastikmod;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.datagen.loot.AddItemModifier;
import net.lisalaf.fantastikmod.datagen.loot.ModLootModfiers;
import net.lisalaf.fantastikmod.dialog.DialogSystem;
import net.lisalaf.fantastikmod.effect.ModEffects;
import net.lisalaf.fantastikmod.entity.ModEntities;
import net.lisalaf.fantastikmod.entity.client.*;
import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.lisalaf.fantastikmod.event.ModEvent;
import net.lisalaf.fantastikmod.item.ModCreativeModTabs;
import net.lisalaf.fantastikmod.item.ModItems;
import net.lisalaf.fantastikmod.sound.ModSounds;
import net.lisalaf.fantastikmod.villager.ModVillagers;
import net.lisalaf.fantastikmod.worldgen.ModFeatures;
import net.lisalaf.fantastikmod.worldgen.biome.ModTerrablender;
import net.lisalaf.fantastikmod.worldgen.biome.sunface.ModSurfaceRules;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

import java.util.concurrent.CompletableFuture;

@Mod(fantastikmod.MOD_ID)
public class fantastikmod {
    public static final String MOD_ID = "fantastikmod";
    public static final Logger LOGGER = LogUtils.getLogger();


    public fantastikmod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        DialogSystem.registerDialogs();

        ModCreativeModTabs.register(modEventBus);

        ModEntities.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModVillagers.register(modEventBus);

        ModSounds.register(modEventBus);

        ModLootModfiers.register(modEventBus);

        ModEffects.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(this::gatherData);

        ModFeatures.FEATURES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(ModEvent.class);

        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::addFuel);


    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.MOON_LILY.getId(), ModBlocks.POTTED_MOON_LILY);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.MOON_NORTHERN_BLUEBELL.getId(), ModBlocks.POTTED_MOON_NORTHERN_BLUEBELL);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.MOON_HEATHER.getId(), ModBlocks.POTTED_MOON_HEATHER);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.SPIDER_LILY.getId(), ModBlocks.POTTED_SPIDER_LILY);

            ModTerrablender.registerBiomes();

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());
        });
    }



    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.SPAWN_EGGS)) {
            event.accept(ModItems.EGG_KITSUNE_LIGHT);
            event.accept(ModItems.EGG_BLUE_BUTTERFLY);
            event.accept(ModItems.EGG_ICE_DRAGON);
            event.accept(ModItems.EGG_MOON_DEER);
        } else if (event.getTabKey().equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            event.accept(ModItems.TOFU);
            event.accept(ModItems.STRAWBERRY);
            event.accept(ModItems.RICE);
            event.accept(ModItems.STRAWBERRY_MOCHI);
            event.accept(ModItems.MATCHA_MOCHI);
            event.accept(ModItems.MOCHI);
        } else if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            event.accept(ModItems.GEMKITSUNE);
            event.accept(ModItems.MOONMASCOT);
        }
    }

    private void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
        }
    }
    public void addFuel(FurnaceFuelBurnTimeEvent event) {
        if(event.getItemStack().getItem() == ModBlocks.MOON_PLANKS.get().asItem()) {
            event.setBurnTime(300);
        }
        if(event.getItemStack().getItem() == ModBlocks.TREE_MOON_LOG_BLOCK.get().asItem()) {
            event.setBurnTime(300);
        }
        if(event.getItemStack().getItem() == ModBlocks.TREE_MOON_WOOD.get().asItem()) {
            event.setBurnTime(300);
        }
        if(event.getItemStack().getItem() == ModBlocks.MOON_PLANKS_SLAB.get().asItem()) {
            event.setBurnTime(150); // 7.5 секунд
        }
        if(event.getItemStack().getItem() == ModBlocks.MOON_PLANKS_STAIRS.get().asItem()) {
            event.setBurnTime(300); // 15 секунд
        }
        if(event.getItemStack().getItem() == ModBlocks.MOON_PLANKS_FENCE.get().asItem()) {
            event.setBurnTime(300);
        }
        if(event.getItemStack().getItem() == ModBlocks.MOON_PLANKS_FENCE_GATE.get().asItem()) {
            event.setBurnTime(300);
        }

    }

}
