package net.lisalaf.fantastikmod.block.custom;

import net.lisalaf.fantastikmod.effect.ModEffects;
import net.lisalaf.fantastikmod.entity.custom.KitsuneLightEntity;
import net.lisalaf.fantastikmod.entity.custom.MoonDeerEntity;
import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.item.ModItems;
import net.lisalaf.fantastikmod.block.ModBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("removal")
public class KitsuneStatueBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 2.0, 13.0, 20.0, 14.0);

    private static final Map<UUID, DialogState> activeDialogs = new HashMap<>();

    private static class DialogState {
        int currentLine = 0;
        boolean isActive = false;

        DialogState() {
            this.currentLine = 0;
            this.isActive = true;
        }
    }

    private static final String[] DIALOG_LINES_RU = {
            "Кажется ты нашёл статую лисы",
            "Люди делали такие и ставили в храмах как посланников богини Инари",
            "Они верили что если статуям делать подношения, то так смогут предрасположить кицунэ",
            "И они встанут перед богиней на сторону преподносящего",
            "Статуям часто преподносили рис и сакэ",
            "Рис нам без надобности. Но можешь попробовать дать его статуе"
    };

    private static final String[] DIALOG_LINES_EN = {
            "It seems you've found a fox statue",
            "People made these and placed them in temples as messengers of the goddess Inari",
            "They believed that making offerings to statues could win the favor of kitsune",
            "And they would stand before the goddess on the side of the one making offerings",
            "Rice and sake were often offered to the statues",
            "We have no need for rice. But you can try giving it to the statue"
    };

    public KitsuneStatueBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            boolean hasKitsune = false;
            for (KitsuneLightEntity kitsune : level.getEntitiesOfClass(KitsuneLightEntity.class,
                    player.getBoundingBox().inflate(16))) {
                if (kitsune.isTamed()) {
                    hasKitsune = true;
                    break;
                }
            }

            if (hasKitsune) {
                startDialogWithNearbyKitsune(level, player);
            } else {
                startDialogWithNearbyMoonDeer(level, player);
            }
        }
        super.attack(state, level, pos, player);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        boolean isSakeItem = itemInHand.getItem() == ModItems.SAKE_DRINK.get();
        boolean isRice = itemInHand.getItem() == ModItems.RICE.get();

        if (isRice || isSakeItem) {
            if (!level.isClientSide) {
                int amplifier = 0;
                int duration = 15 * 20;

                if (isSakeItem) {
                    amplifier = 1;
                    duration = 30 * 20;
                }

                MobEffectInstance currentEffect = player.getEffect(ModEffects.INARI_BLESSING.get());
                int newDuration = duration;
                int newAmplifier = amplifier;

                if (currentEffect != null) {
                    newDuration += currentEffect.getDuration();
                    if (newDuration > 180 * 20) newDuration = 180 * 20;
                    if (currentEffect.getAmplifier() >= amplifier) {
                        newAmplifier = currentEffect.getAmplifier();
                    }
                }

                player.addEffect(new MobEffectInstance(ModEffects.INARI_BLESSING.get(), newDuration, newAmplifier));

                if (!player.isCreative()) {
                    itemInHand.shrink(1);
                }

                level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);

            }
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide) {
            boolean hasKitsune = false;
            for (KitsuneLightEntity kitsune : level.getEntitiesOfClass(KitsuneLightEntity.class,
                    player.getBoundingBox().inflate(16))) {
                if (kitsune.isTamed()) {
                    hasKitsune = true;
                    break;
                }
            }

            if (hasKitsune) {
                startDialogWithNearbyKitsune(level, player);
            } else {
                startDialogWithNearbyMoonDeer(level, player);
            }

            if (player instanceof ServerPlayer serverPlayer) {
                Advancement advancement = serverPlayer.server.getAdvancements()
                        .getAdvancement(new ResourceLocation(fantastikmod.MOD_ID, "inari_blessing"));
                if (advancement != null) {
                    serverPlayer.getAdvancements().award(advancement, "make_offering");
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void startDialogWithNearbyKitsune(Level level, Player player) {
        KitsuneLightEntity nearbyKitsune = null;
        double closestDistance = 16.0;

        for (KitsuneLightEntity kitsune : level.getEntitiesOfClass(KitsuneLightEntity.class,
                player.getBoundingBox().inflate(16))) {
            if (kitsune.isTamed()) {
                double distance = player.distanceTo(kitsune);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    nearbyKitsune = kitsune;
                }
            }
        }

        if (nearbyKitsune != null) {
            if (activeDialogs.containsKey(player.getUUID()) && activeDialogs.get(player.getUUID()).isActive) {
                return;
            }

            DialogState dialogState = new DialogState();
            activeDialogs.put(player.getUUID(), dialogState);

            sendNextDialogLine(level, player, nearbyKitsune, dialogState);

            if (level instanceof ServerLevel serverLevel) {
                scheduleNextDialog(serverLevel, player, nearbyKitsune, dialogState, 1);
            }
        }
    }

    private void scheduleNextDialog(ServerLevel level, Player player, KitsuneLightEntity kitsune, DialogState state, int nextLineIndex) {
        new Thread(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                return;
            }

            level.getServer().execute(() -> {
                if (!state.isActive || player.isRemoved() || !player.isAlive()) {
                    activeDialogs.remove(player.getUUID());
                    return;
                }

                state.currentLine = nextLineIndex;

                if (state.currentLine < DIALOG_LINES_RU.length) {
                    sendNextDialogLine(level, player, kitsune, state);
                    scheduleNextDialog(level, player, kitsune, state, state.currentLine + 1);
                } else {
                    state.isActive = false;
                    activeDialogs.remove(player.getUUID());
                }
            });
        }).start();
    }

    private void sendNextDialogLine(Level level, Player player, KitsuneLightEntity kitsune, DialogState state) {
        boolean isRussian = isRussianPlayer(player);
        String[] lines = isRussian ? DIALOG_LINES_RU : DIALOG_LINES_EN;

        if (state.currentLine < lines.length) {
            String kitsuneName = kitsune.getCustomName() != null ?
                    kitsune.getCustomName().getString() :
                    (isRussian ? "Кицунэ" : "Kitsune");

            String message = kitsuneName + ": " + lines[state.currentLine];
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(message));
        }
    }

    private boolean isRussianPlayer(Player player) {
        try {
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                String language = serverPlayer.getLanguage();
                return language != null && language.startsWith("ru");
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void startDialogWithNearbyMoonDeer(Level level, Player player) {
        MoonDeerEntity nearbyMoonDeer = null;
        double closestDistance = 16.0;

        for (MoonDeerEntity moonDeer : level.getEntitiesOfClass(MoonDeerEntity.class,
                player.getBoundingBox().inflate(16))) {
            if (moonDeer.isTamed()) {
                double distance = player.distanceTo(moonDeer);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    nearbyMoonDeer = moonDeer;
                }
            }
        }

        if (nearbyMoonDeer != null) {
            if (activeDialogs.containsKey(player.getUUID()) && activeDialogs.get(player.getUUID()).isActive) {
                return;
            }

            DialogState dialogState = new DialogState();
            activeDialogs.put(player.getUUID(), dialogState);

            sendNextMoonDeerDialogLine(level, player, nearbyMoonDeer, dialogState);

            if (level instanceof ServerLevel serverLevel) {
                scheduleNextMoonDeerDialog(serverLevel, player, nearbyMoonDeer, dialogState, 1);
            }
        }
    }

    private static final String[] MOON_DEER_DIALOG_LINES_RU = {
            "Это чья-то статуя? Божества или зверя?",
            "Много лет назад в Лунном лесу жили люди, они верили в своих богов",
            "У них были подобные статуи для своих божеств, но люди ушли вместе с упавшим осколком луны...",
            "А от статуй почти ничего не осталось"
    };

    private static final String[] MOON_DEER_DIALOG_LINES_EN = {
            "Is this someone's statue? A deity or a beast?",
            "Many years ago, people lived in the Moon Forest, they believed in their gods",
            "They had similar statues for their deities, but the people left with a fallen shard of the moon...",
            "Almost nothing remains of the statues"
    };

    private void scheduleNextMoonDeerDialog(ServerLevel level, Player player, MoonDeerEntity moonDeer, DialogState state, int nextLineIndex) {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return;
            }

            level.getServer().execute(() -> {
                if (!state.isActive || player.isRemoved() || !player.isAlive()) {
                    activeDialogs.remove(player.getUUID());
                    return;
                }

                state.currentLine = nextLineIndex;

                if (state.currentLine < MOON_DEER_DIALOG_LINES_RU.length) {
                    sendNextMoonDeerDialogLine(level, player, moonDeer, state);
                    scheduleNextMoonDeerDialog(level, player, moonDeer, state, state.currentLine + 1);
                } else {
                    state.isActive = false;
                    activeDialogs.remove(player.getUUID());
                }
            });
        }).start();
    }

    private void sendNextMoonDeerDialogLine(Level level, Player player, MoonDeerEntity moonDeer, DialogState state) {
        boolean isRussian = isRussianPlayer(player);
        String[] lines = isRussian ? MOON_DEER_DIALOG_LINES_RU : MOON_DEER_DIALOG_LINES_EN;

        if (state.currentLine < lines.length) {
            String deerName = moonDeer.getCustomName() != null ?
                    moonDeer.getCustomName().getString() :
                    (isRussian ? "Лунный олень" : "Moon Deer");

            String message = deerName + ": " + lines[state.currentLine];
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(message));
        }
    }
}