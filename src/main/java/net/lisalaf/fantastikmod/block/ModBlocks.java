package net.lisalaf.fantastikmod.block;

import com.eliotlash.mclib.math.Constant;
import net.lisalaf.fantastikmod.block.custom.*;
import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.item.ModFoods;
import net.lisalaf.fantastikmod.item.ModItems;
import net.lisalaf.fantastikmod.sound.ModSounds;
import net.lisalaf.fantastikmod.worldgen.tree.MoonTreeGrower;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, fantastikmod.MOD_ID);

    public static final RegistryObject<Block> ASH_BLOCK = registerBlock("ash_block",
            () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL).sound(SoundType.SAND)) {
        
                @Override
                public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                    if (random.nextInt(16) == 0) {
                        BlockPos blockpos = pos.below();
                        if (level.isEmptyBlock(blockpos) || (level.getBlockState(blockpos).canBeReplaced() && !level.getBlockState(blockpos).isSolid())) {
                            double d0 = (double)pos.getX() + random.nextDouble();
                            double d1 = (double)pos.getY() - 0.05D;
                            double d2 = (double)pos.getZ() + random.nextDouble();
                            level.addParticle((ParticleOptions) ParticleTypes.FALLING_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            });
    public static final RegistryObject<Block> DRYING_BASKET = registerBlock("drying_basket",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> STRAWBERRY_CROP = BLOCKS.register("strawberry_crop",
            () -> new StrawberryCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noOcclusion()));
    public static final RegistryObject<Block> RICE_CROP = BLOCKS.register("rice_crop",
            () -> new RiceCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noOcclusion()));
    public static final RegistryObject<Block> TEA_CROP = BLOCKS.register("tea_crop",
            () -> new TeaCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noOcclusion()));

    public static final RegistryObject<Block> BUSH_CROWBERRY = BLOCKS.register("bush_crowberry",
            () -> new CrowberryShrubBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noOcclusion().noCollission().lightLevel(state ->6)));

    public static final RegistryObject<Block> GEMKITSUNE_BLOCK = registerBlock("gemkitsune_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST).lightLevel(state -> 8)));

    public static final RegistryObject<Block> AURIPIGMENT_BLOCK = registerBlock("auripigment_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).lightLevel(state -> 4)));

    public static final RegistryObject<Block> TREE_MOON_LOG_BLOCK = registerBlock("tree_moon_log_block",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).lightLevel(state -> 4)));
    public static final RegistryObject<Block> MOON_PLANKS = registerBlock("moon_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).lightLevel(state -> 7)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final RegistryObject<Block> MOON_PLANKS_PLATE = registerBlock("moon_planks_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .lightLevel(state -> 4),
                    BlockSetType.CHERRY));
    public static final RegistryObject<Block> MOON_PLANKS_SLAB = registerBlock("moon_planks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .lightLevel(state -> 4)));
    public static final RegistryObject<Block> MOON_PLANKS_FENCE = registerBlock("moon_planks_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).lightLevel(state -> 4)));
    public static final RegistryObject<Block> MOON_PLANKS_STAIRS = registerBlock("moon_planks_stairs",
            () -> new StairBlock(() -> ModBlocks.MOON_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.CHERRY_STAIRS).lightLevel(state -> 4)));
    public static final RegistryObject<Block> MOON_PLANKS_FENCE_GATE = registerBlock("moon_planks_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).lightLevel(state -> 4), SoundEvents.CHERRY_WOOD_FENCE_GATE_OPEN, SoundEvents.CHERRY_WOOD_FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> MOON_TREE_TRAPDOOR = registerBlock("moon_tree_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).lightLevel(state -> 4).noOcclusion(), BlockSetType.CHERRY));
    public static final RegistryObject<Block> MOON_WALL = registerBlock("moon_planks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).lightLevel(state -> 4)));
    public static final RegistryObject<Block> MOON_PLANKS_BUTTON = registerBlock("moon_planks_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_BUTTON).lightLevel(state -> 4).noOcclusion(),
                    BlockSetType.CHERRY, 35, true));
    public static final RegistryObject<Block> MOON_TREE_DOOR = registerBlock("moon_tree_door",
            () -> new DoorBlock(
                    BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                            .lightLevel(state -> 4)
                            .noOcclusion(),
                    BlockSetType.CHERRY
            ));

    public static final RegistryObject<Block> SPIDER_LILY = registerBlock("spider_lily",
            () -> new FlowerBlock(
                    MobEffects.POISON,
                    8,
                    Block.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
            ) {
                @Override
                public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
                    if (!level.isClientSide() && entity instanceof LivingEntity livingEntity) {
                        if (entity.getBoundingBox().minY <= pos.getY() + 0.8) {
                            livingEntity.addEffect(new MobEffectInstance(
                                    MobEffects.POISON,
                                    60,
                                    1
                            ));
                            if (entity instanceof Player player) {
                                player.hurt(player.damageSources().cactus(), 1.0F);
                            }
                        }
                    }
                    super.entityInside(state, level, pos, entity);
                }
            });

    public static final RegistryObject<Block> POTTED_SPIDER_LILY = BLOCKS.register("potted_spider_lily",
            () -> new FlowerPotBlock(
                    () -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.SPIDER_LILY,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_ALLIUM).noOcclusion()));

    public static final RegistryObject<Block> MOON_LILY = registerBlock("moon_lily",
            () -> new FlowerBlock(() -> MobEffects.NIGHT_VISION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).sound(SoundType.AMETHYST).noOcclusion().noCollission().lightLevel( state -> 5)));
    public static final RegistryObject<Block> POTTED_MOON_LILY = BLOCKS.register("potted_moon_lily",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.MOON_LILY,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_ALLIUM).sound(SoundType.AMETHYST).noOcclusion().lightLevel( state -> 5)));

    public static final RegistryObject<Block> MOON_NORTHERN_BLUEBELL = registerBlock("moon_northern_bluebell",
            () -> new FlowerBlock(() -> MobEffects.NIGHT_VISION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).sound(SoundType.AMETHYST).noOcclusion().noCollission().lightLevel( state -> 5)));
    public static final RegistryObject<Block> POTTED_MOON_NORTHERN_BLUEBELL = BLOCKS.register("potted_moon_northern_bluebell",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.MOON_LILY,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_ALLIUM).sound(SoundType.AMETHYST).noOcclusion().lightLevel( state -> 5)));

    public static final RegistryObject<Block> MOON_HEATHER = registerBlock("moon_heather",
            () -> new FlowerBlock(() -> MobEffects.NIGHT_VISION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).sound(SoundType.AMETHYST).noOcclusion().noCollission().lightLevel( state -> 5)));
    public static final RegistryObject<Block> POTTED_MOON_HEATHER = BLOCKS.register("potted_moon_heather",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.MOON_LILY,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_ALLIUM).sound(SoundType.AMETHYST).noOcclusion().lightLevel( state -> 5)));

    public static final RegistryObject<Block> MOON_GRASS = registerBlock("moon_grass",
            () -> new FlowerBlock(() -> MobEffects.NIGHT_VISION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM)
                            .sound(SoundType.GRASS) // Более подходящий звук
                            .noOcclusion()
                            .noCollission()
                            .lightLevel(state -> 5)
                            .offsetType(BlockBehaviour.OffsetType.XZ) // Для правильного позиционирования
            ));

    public static final RegistryObject<Block> MOON_GRASS_1 = registerBlock("moon_grass_1",
            () -> new FlowerBlock(() -> MobEffects.NIGHT_VISION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM)
                            .sound(SoundType.GRASS) // Более подходящий звук
                            .noOcclusion()
                            .noCollission()
                            .lightLevel(state -> 5)
                            .offsetType(BlockBehaviour.OffsetType.XZ) // Для правильного позиционирования
            ));

    public static final RegistryObject<Block> MOON_VINE = registerBlock("moon_vine",
            () -> new MoonVineBlock(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES)
                    .lightLevel(state -> 5)
                    .sound(SoundType.GRASS)));



    public static final RegistryObject<Block> MOON_SAPLING = registerBlock("moon_sapling",
            () -> new SaplingBlock(new MoonTreeGrower(), BlockBehaviour.Properties.copy(Blocks.CHERRY_SAPLING)));

    public static final RegistryObject<Block> TREE_MOON_FOLIAGE_BLOCK = registerBlock("tree_moon_foliage_block",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_LEAVES)
                    .lightLevel(state -> 9)) {
                @Override
                public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random)
                {
                    if (random.nextFloat() <0.2f){
                        double x = (double) pos.getX() + random.nextDouble();
                        double y = (double) pos.getY() + -0.1D;
                        double z = (double) pos.getZ() + random.nextDouble();

                        level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0.0D, 0.0D, 0.0D);
                    }
                }
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
                @Override
                protected boolean decaying(BlockState state) {
                    return false; // Полностью отключает исчезновение листвы
                }
            }
    );

    public static final RegistryObject<Block> MOON_ORE = registerBlock("moon_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE).lightLevel(state -> 7)){

                // Токсичные частицы
                @Override
                public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                    if (random.nextInt(7) == 0) {
                        level.addParticle(ParticleTypes.SNOWFLAKE,
                                pos.getX() + random.nextDouble(),
                                pos.getY() + random.nextDouble(),
                                pos.getZ() + random.nextDouble(),
                                0.0D, 0.0D, 0.0D);
                    }
                }});

    public static final RegistryObject<Block> MOONSTONE = registerBlock("moonstone",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE).lightLevel(state -> 4)));

    public static final RegistryObject<Block> SILVER_ORE = registerBlock("silver_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));

    public static final RegistryObject<Block> SILVER_BLOCK = registerBlock("silver_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));

    public static final RegistryObject<Block> MOON_GEM_BLOCK = registerBlock("moon_gem_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE).sound(SoundType.AMETHYST).lightLevel(state -> 8)));

    public static final RegistryObject<Block> AURIPIGMENT_ORE_BLOCK = registerBlock("auripigment_ore_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                                .sound(SoundType.DEEPSLATE)
                                .lightLevel(state -> 1)
                                .strength(2.5f, 3.0f)
                                .randomTicks()
                                //.emissiveRendering((state, level, pos) -> true)
                                .hasPostProcess((state, level, pos) -> true)
                                .requiresCorrectToolForDrops(), UniformInt.of(2, 5)) {

                // Токсичные частицы
                @Override
                public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                    if (random.nextInt(5) == 0) {
                        level.addParticle(ParticleTypes.ITEM_SLIME,
                                pos.getX() + random.nextDouble(),
                                pos.getY() + random.nextDouble(),
                                pos.getZ() + random.nextDouble(),
                                0.0D, 0.0D, 0.0D);
                    }
                }

                // Эффект при ходьбе
                @Override
                public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
                    if (entity instanceof LivingEntity livingEntity && !level.isClientSide()) {
                        livingEntity.addEffect(new MobEffectInstance(
                                MobEffects.POISON,
                                100,  // 5 секунд
                                0     // Уровень I
                        ));
                    }
                    super.stepOn(level, pos, state, entity);
                }

                // Эффект при добыче
                @Override
                public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                                          @Nullable BlockEntity blockEntity, ItemStack tool) {
                    if (!player.isCreative() && !player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                        player.addEffect(new MobEffectInstance(
                                MobEffects.POISON,
                                150,
                                1
                        ));
                        player.addEffect(new MobEffectInstance(
                                MobEffects.CONFUSION,
                                200,
                                0
                        ));
                    }
                    super.playerDestroy(level, player, pos, state, blockEntity, tool);
                }
            });

    public static final RegistryObject<Block> MOON_CRYSTAL_BLOCK = registerBlock("moon_crystal_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)
                    .lightLevel(state -> 7)) {

                @Override
                @OnlyIn(Dist.CLIENT)
                public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                    super.animateTick(state, level, pos, random);
                    if (random.nextInt(3) == 0) {
                        int side = random.nextInt(6);

                        double x = pos.getX();
                        double y = pos.getY();
                        double z = pos.getZ();
                        double speedX = 0, speedY = 0, speedZ = 0;

                        switch(side) {
                            case 0:
                                x += 0.5 + (random.nextDouble() - 0.5);
                                y += 1.05;
                                z += 0.5 + (random.nextDouble() - 0.5);
                                speedY = 0.02;
                                break;
                            case 1:
                                x += 0.5 + (random.nextDouble() - 0.5);
                                y += -0.05;
                                z += 0.5 + (random.nextDouble() - 0.5);
                                speedY = -0.02;
                                break;
                            case 2:
                                x += 0.5 + (random.nextDouble() - 0.5);
                                y += 0.5 + (random.nextDouble() - 0.5);
                                z += -0.05;
                                speedZ = -0.02;
                                break;
                            case 3:
                                x += 0.5 + (random.nextDouble() - 0.5);
                                y += 0.5 + (random.nextDouble() - 0.5);
                                z += 1.05;
                                speedZ = 0.02;
                                break;
                            case 4:
                                x += -0.05;
                                y += 0.5 + (random.nextDouble() - 0.5);
                                z += 0.5 + (random.nextDouble() - 0.5);
                                speedX = -0.02;
                                break;
                            case 5:
                                x += 1.05;
                                y += 0.5 + (random.nextDouble() - 0.5);
                                z += 0.5 + (random.nextDouble() - 0.5);
                                speedX = 0.02;
                                break;
                        }

                        level.addParticle(ParticleTypes.SNOWFLAKE,
                                x, y, z,
                                speedX + (random.nextDouble() - 0.5) * 0.01,
                                speedY + (random.nextDouble() - 0.5) * 0.01,
                                speedZ + (random.nextDouble() - 0.5) * 0.01);

                        for (int i = 0; i < 2; i++) {
                            level.addParticle(ParticleTypes.WHITE_ASH,
                                    x, y, z,
                                    (random.nextDouble() - 0.5) * 0.01,
                                    (random.nextDouble() - 0.5) * 0.01,
                                    (random.nextDouble() - 0.5) * 0.01);
                        }
                    }

                    if (random.nextInt(8) == 0) {
                        for (int i = 0; i < 2 + random.nextInt(3); i++) {
                            double centerX = pos.getX() + 0.5;
                            double centerY = pos.getY() + 0.5;
                            double centerZ = pos.getZ() + 0.5;

                            double angle = random.nextDouble() * Math.PI * 2;
                            double radius = random.nextDouble() * 0.3;
                            double particleX = centerX + Math.cos(angle) * radius;
                            double particleZ = centerZ + Math.sin(angle) * radius;
                            double particleY = centerY + (random.nextDouble() - 0.5) * 0.3;

                            double speedFactor = 0.02 + random.nextDouble() * 0.02;
                            double speedX = Math.cos(angle) * speedFactor;
                            double speedZ = Math.sin(angle) * speedFactor;
                            double speedY = (random.nextDouble() - 0.5) * 0.01;

                            level.addParticle(ParticleTypes.END_ROD,
                                    particleX, particleY, particleZ,
                                    speedX, speedY, speedZ);
                        }
                    }
                }
            });

    public static final RegistryObject<Block> MOON_CRYSTAL_GLASS = registerBlock("moon_crystal_glass",
            () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)
                    .lightLevel(state -> 5)
                    .noOcclusion()
                    .isValidSpawn((state, getter, pos, entity) -> false)
                    .isRedstoneConductor((state, getter, pos) -> false)
                    .isSuffocating((state, getter, pos) -> false)
                    .isViewBlocking((state, getter, pos) -> false)));

    public static final RegistryObject<Block> MOON_CRYSTAL_GLASS_PANE = registerBlock("moon_crystal_glass_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)
                    .lightLevel(state -> 5)
                    .noOcclusion()
                    .isValidSpawn((state, getter, pos, entity) -> false)
                    .isRedstoneConductor((state, getter, pos) -> false)
                    .isSuffocating((state, getter, pos) -> false)
                    .isViewBlocking((state, getter, pos) -> false)));

    public static final RegistryObject<Block> MOON_CRYSTAL = registerBlock("moon_crystal",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)
                    .lightLevel(state -> 7)
                    .noOcclusion()
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .strength(1.5f)
                    .pushReaction(PushReaction.DESTROY)) {
                @Override
                public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
                    Direction direction = state.getValue(FACING);
                    BlockPos blockpos = pos.relative(direction.getOpposite());
                    BlockState blockstate = level.getBlockState(blockpos);
                    return blockstate.isFaceSturdy(level, blockpos, direction) ||
                            blockstate.isSolid() ||
                            blockstate.canOcclude();
                }
                @Override
                @OnlyIn(Dist.CLIENT)
                public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                    super.animateTick(state, level, pos, random);
                    if (random.nextInt(8) == 0) {
                        Direction direction = state.getValue(FACING);

                        double x = pos.getX() + 0.5;
                        double y = pos.getY() + 0.5;
                        double z = pos.getZ() + 0.5;
                        switch(direction) {
                            case UP -> y += 0.4;
                            case DOWN -> y -= 0.4;
                            case NORTH -> z -= 0.4;
                            case SOUTH -> z += 0.4;
                            case WEST -> x -= 0.4;
                            case EAST -> x += 0.4;
                        }
                        x += (random.nextDouble() - 0.5) * 0.3;
                        y += (random.nextDouble() - 0.5) * 0.3;
                        z += (random.nextDouble() - 0.5) * 0.3;
                        double speedX = (random.nextDouble() - 0.5) * 0.02;
                        double speedY = (random.nextDouble() - 0.5) * 0.02;
                        double speedZ = (random.nextDouble() - 0.5) * 0.02;
                        level.addParticle(ParticleTypes.SNOWFLAKE,
                                x, y, z,
                                speedX, speedY, speedZ);
                    }
                }
            });
    public static final RegistryObject<Block> CATNIP_WINE = registerBlock("catnip_wine",
            () -> new LanternBlock(BlockBehaviour.Properties.of()
                    .instabreak()
                    .noOcclusion()
                    .strength(0.1f)
                    .sound(SoundType.GLASS)
            ));

    public static final RegistryObject<Block> KITSUNE_STATUE = registerBlock("kitsune_statue",
            () -> new KitsuneStatueBlock(BlockBehaviour.Properties.of()
                    .instabreak()
                    .strength(2.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
                    .hasPostProcess((state, level, pos) -> true) // Добавьте
            ));

    public static final RegistryObject<Block> SAKE = registerBlock("sake",
            () -> new LanternBlock(BlockBehaviour.Properties.of()
                    .instabreak()
                    .noOcclusion()
                    .strength(0.1f)
                    .sound(SoundType.GLASS)
            ));

    public static final RegistryObject<Block> WILD_CATNIP = registerBlock("wild_catnip",
            () -> new FlowerBlock(MobEffects.CONFUSION, 5,
                    BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)
                            .noOcclusion()
                            .sound(SoundType.GRASS)
                            .randomTicks()));

    public static final RegistryObject<Block> CATNIP_CROP = registerBlock("catnip_crop",
            () -> new CatnipCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noOcclusion()
                    .randomTicks()
                    .instabreak()));

    public static final RegistryObject<Block> FROST = registerBlock("frost",
            () -> new FrostLayerBlock(BlockBehaviour.Properties.copy(Blocks.SNOW).lightLevel(state -> 2)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}