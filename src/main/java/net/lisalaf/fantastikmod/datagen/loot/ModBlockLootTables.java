package net.lisalaf.fantastikmod.datagen.loot;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.block.custom.*;
import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

@SuppressWarnings("removal")
public class ModBlockLootTables extends LootTableProvider {

    public ModBlockLootTables(PackOutput output) {
        super(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootGenerator::new, LootContextParamSets.BLOCK)
        ));
    }

    public static class ModBlockLootGenerator extends BlockLootSubProvider {

        public ModBlockLootGenerator() {
            super(Set.of(), net.minecraft.world.flag.FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            // Блоки, которые дропают сами себя
            this.dropSelf(ModBlocks.GEMKITSUNE_BLOCK.get());
            this.dropSelf(ModBlocks.TREE_MOON_LOG_BLOCK.get());

            this.dropSelf(ModBlocks.ASH_BLOCK.get());
            this.dropSelf(ModBlocks.MOON_CRYSTAL_BLOCK.get());
            this.dropSelf(ModBlocks.MOON_CRYSTAL.get());
            this.dropSelf(ModBlocks.AURIPIGMENT_BLOCK.get());
            this.dropSelf(ModBlocks.MOON_PLANKS.get());
            this.dropSelf(ModBlocks.MOONSTONE.get());
            this.dropSelf(ModBlocks.MOON_PLANKS_STAIRS.get());
            this.dropSelf(ModBlocks.MOON_PLANKS_FENCE.get());
            this.dropSelf(ModBlocks.MOON_PLANKS_FENCE_GATE.get());
            this.dropSelf(ModBlocks.MOON_WALL.get());
            this.dropSelf(ModBlocks.MOON_PLANKS_BUTTON.get());
            this.dropSelf(ModBlocks.MOON_PLANKS_PLATE.get());
            this.dropSelf(ModBlocks.MOON_TREE_TRAPDOOR.get());
            this.dropSelf(ModBlocks.MOON_GEM_BLOCK.get());
            this.dropSelf(ModBlocks.SILVER_BLOCK.get());
            this.dropSelf(ModBlocks.MOON_SAPLING.get());
            this.dropSelf(ModBlocks.TREE_MOON_WOOD.get());

            this.dropSelf(ModBlocks.DRYING_BASKET.get());
            this.dropSelf(ModBlocks.CATNIP_WINE.get());
            this.dropSelf(ModBlocks.KITSUNE_STATUE.get());
            this.dropSelf(ModBlocks.SAKE.get());

            // Специальные блоки
            this.add(ModBlocks.MOON_PLANKS_SLAB.get(),
                    block -> createSlabItemTable(ModBlocks.MOON_PLANKS_SLAB.get()));
            this.add(ModBlocks.MOON_TREE_DOOR.get(),
                    block -> createDoorTable(ModBlocks.MOON_TREE_DOOR.get()));

            this.add(ModBlocks.AURIPIGMENT_ORE_BLOCK.get(),
                    block -> createCopperLikeOreDrops(ModBlocks.AURIPIGMENT_ORE_BLOCK.get(), ModItems.AURIPIGMENT.get()));
            this.add(ModBlocks.MOON_ORE.get(),
                    block -> createCopperLikeOreDrops(ModBlocks.MOON_ORE.get(), ModItems.GEM_MOON.get()));
            this.add(ModBlocks.SILVER_ORE.get(),
                    block -> createSilkTouchDispatchTable(block,
                            this.applyExplosionDecay(block,
                                    LootItem.lootTableItem(ModItems.SILVER_RAW.get())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                            )));

            LootItemCondition.Builder lootitemcondition$builder = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(ModBlocks.STRAWBERRY_CROP.get())
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StrawberryCropBlock.AGE, 5));

            this.add(ModBlocks.STRAWBERRY_CROP.get(), createCropDrops(ModBlocks.STRAWBERRY_CROP.get(), ModItems.STRAWBERRY.get(),
                    ModItems.STRAWBERRY_SEEDS.get(), lootitemcondition$builder));
            lootitemcondition$builder = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(ModBlocks.TEA_CROP.get())
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TeaCropBlock.AGE, 5));
            this.add(ModBlocks.TEA_CROP.get(), createCropDrops(ModBlocks.TEA_CROP.get(), ModItems.TEA_LEAF.get(),
                    ModItems.TEA_SEEDS.get(), lootitemcondition$builder));

            LootItemCondition.Builder riceConditionBuilder = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(ModBlocks.RICE_CROP.get())
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RiceCropBlock.AGE, 8));

            this.add(ModBlocks.RICE_CROP.get(), createCropDrops(ModBlocks.RICE_CROP.get(), ModItems.RICE.get(),
                    ModItems.RICE.get(), riceConditionBuilder));

            LootItemCondition.Builder crowberryCondition = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(ModBlocks.BUSH_CROWBERRY.get())
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CrowberryShrubBlock.AGE, 4));

            this.add(ModBlocks.BUSH_CROWBERRY.get(), createCropDrops(ModBlocks.BUSH_CROWBERRY.get(), ModItems.MOON_CROWBERRY.get(),
                    ModItems.MOON_CROWBERRY.get(), crowberryCondition));

            this.add(ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get(), block ->
                    createSilkTouchDispatchTable(block,
                            LootItem.lootTableItem(ModBlocks.MOON_SAPLING.get())
                                    .when(LootItemRandomChanceCondition.randomChance(0.05f)) // 5% шанс саженца
                                    .otherwise(
                                            LootItem.lootTableItem(ModItems.MOON_DUST.get())
                                                    .when(LootItemRandomChanceCondition.randomChance(0.02f)) // 0.2% шанс лунной пыли
                                    )
                    )
            );

            this.add(ModBlocks.WILD_CATNIP.get(), (block) ->
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .add(LootItem.lootTableItem(ModItems.CATNIP.get())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                                    .add(LootItem.lootTableItem(ModItems.CATNIP_SEEDS.get())
                                            .when(LootItemRandomChanceCondition.randomChance(0.3f)))));

            LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(ModBlocks.CATNIP_CROP.get())
                    .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(CatnipCropBlock.AGE, 4));

            this.add(ModBlocks.CATNIP_CROP.get(), (block) ->
                    applyExplosionDecay(block,
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool()
                                            .when(condition)
                                            .add(LootItem.lootTableItem(ModItems.CATNIP.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))
                                            .add(LootItem.lootTableItem(ModItems.CATNIP_SEEDS.get())
                                                    .when(LootItemRandomChanceCondition.randomChance(0.5f))
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))))));
            this.dropSelf(ModBlocks.MOON_LILY.get());
            this.add(ModBlocks.POTTED_MOON_LILY.get(), createPotFlowerItemTable(ModBlocks.MOON_LILY.get()));

            this.dropSelf(ModBlocks.SPIDER_LILY.get());
            this.add(ModBlocks.POTTED_SPIDER_LILY.get(), createPotFlowerItemTable(ModBlocks.SPIDER_LILY.get()));

            this.dropSelf(ModBlocks.MOON_NORTHERN_BLUEBELL.get());
            this.add(ModBlocks.POTTED_MOON_NORTHERN_BLUEBELL.get(), createPotFlowerItemTable(ModBlocks.MOON_NORTHERN_BLUEBELL.get()));

            this.dropSelf(ModBlocks.MOON_HEATHER.get());
            this.add(ModBlocks.POTTED_MOON_HEATHER.get(), createPotFlowerItemTable(ModBlocks.MOON_HEATHER.get()));

            //Без дропа
            this.add(ModBlocks.MOON_GRASS.get(), noDrop());
            this.add(ModBlocks.MOON_CRYSTAL_GLASS.get(), noDrop());
            this.add(ModBlocks.MOON_CRYSTAL_GLASS_PANE.get(), noDrop());


            this.add(ModBlocks.MOON_VINE.get(), noDrop());

            this.add(ModBlocks.FROST.get(), noDrop());


            this.add(ModBlocks.MOON_GRASS_1.get(),
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .add(LootItem.lootTableItem(ModItems.MOON_DUST.get())
                                            .when(LootItemRandomChanceCondition.randomChance(0.07f))
                                    )
                            )
            );
        }


        private LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
            return createSilkTouchDispatchTable(pBlock,
                    this.applyExplosionDecay(pBlock,
                            LootItem.lootTableItem(item)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(net.minecraftforge.registries.RegistryObject::get)::iterator;
        }
    }
}