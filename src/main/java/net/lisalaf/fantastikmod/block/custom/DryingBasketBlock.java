package net.lisalaf.fantastikmod.block.custom;

import net.lisalaf.fantastikmod.entity.custom.KitsuneLightEntity;
import net.lisalaf.fantastikmod.entity.custom.MoonDeerEntity;
import net.lisalaf.fantastikmod.item.ModItems;
import net.lisalaf.fantastikmod.villager.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DryingBasketBlock extends Block {
    public static final BooleanProperty DRYING = BooleanProperty.create("drying");
    public static final IntegerProperty LEAVES_COUNT = IntegerProperty.create("leaves_count", 0, 64);
    private static final int DRYING_TIME = 600;
    private static final int DIALOG_RADIUS = 6;

    private static final Map<UUID, DialogState> activeDialogs = new HashMap<>();

    private static class DialogState {
        int currentLine = 0;
        boolean isActive = false;
        DialogState() {
            this.currentLine = 0;
            this.isActive = true;
        }
    }

    private static final String[] TEA_MASTER_DIALOG_RU = {
            "Что бы сделать чай недостаточно собрать чайные листы и залить их кипятком",
            "Нужно для начала их высушить и только потом насладиться вкусом зелёного чая"
    };

    private static final String[] TEA_MASTER_DIALOG_EN = {
            "To make tea, it's not enough to collect tea leaves and pour boiling water over them",
            "You first need to dry them and only then enjoy the taste of green tea"
    };

    private static final String[] MOON_DEER_DIALOG_RU = {
            "Это называют корзинами?",
            "Люди в таких что-то хранят?"
    };

    private static final String[] MOON_DEER_DIALOG_EN = {
            "Are these called baskets?",
            "Do people store something in them?"
    };

    private static final String[] KITSUNE_DIALOG_RU = {
            "Ты любишь чай?",
            "Вот на востоке очень. Даже чайные церемонии проводят среди знати",
            "Слышала от одной кицунэ постарше что жила среди людей",
            "А я только видела как в таких корзинках сушили чай"
    };

    private static final String[] KITSUNE_DIALOG_EN = {
            "Do you like tea?",
            "In the east, they do. They even hold tea ceremonies among the nobility",
            "I heard from an older kitsune who lived among people",
            "I've only seen how tea was dried in such baskets"
    };

    public DryingBasketBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DRYING, false)
                .setValue(LEAVES_COUNT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DRYING, LEAVES_COUNT);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        boolean isDrying = state.getValue(DRYING);

        if (itemInHand.isEmpty()) {
            if (!level.isClientSide) {
                startDialog(level, player, pos);
            }
            return InteractionResult.SUCCESS;
        }

        if (itemInHand.getItem() == ModItems.TEA_LEAF.get() && !isDrying) {
            if (!level.isClientSide) {
                int count = itemInHand.getCount();

                if (!player.isCreative()) {
                    itemInHand.shrink(count);
                }

                level.setBlock(pos, state
                        .setValue(DRYING, true)
                        .setValue(LEAVES_COUNT, count), 3);

                level.playSound(null, pos, SoundEvents.BAMBOO_PLACE, SoundSource.BLOCKS, 0.8f, 1.0f);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.scheduleTick(pos, this, DRYING_TIME);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private void startDialog(Level level, Player player, BlockPos pos) {
        AABB bounds = new AABB(pos).inflate(DIALOG_RADIUS);
        List<Villager> villagers = level.getEntitiesOfClass(Villager.class, bounds);
        List<KitsuneLightEntity> kitsunes = level.getEntitiesOfClass(KitsuneLightEntity.class, bounds);
        List<MoonDeerEntity> moonDeers = level.getEntitiesOfClass(MoonDeerEntity.class, bounds);

        for (Villager villager : villagers) {
            if (villager.getVillagerData().getProfession() == ModVillagers.TEA_MASTER.get()) {
                startTeaMasterDialog(level, player);
                return;
            }
        }

        for (KitsuneLightEntity kitsune : kitsunes) {
            if (kitsune.isTamed()) {
                startKitsuneDialog(level, player, kitsune);
                return;
            }
        }

        for (MoonDeerEntity moonDeer : moonDeers) {
            if (moonDeer.isTamed()) {
                startMoonDeerDialog(level, player, moonDeer);
                return;
            }
        }
    }

    private void startTeaMasterDialog(Level level, Player player) {
        if (activeDialogs.containsKey(player.getUUID()) && activeDialogs.get(player.getUUID()).isActive) {
            return;
        }
        DialogState state = new DialogState();
        activeDialogs.put(player.getUUID(), state);
        sendNextTeaMasterLine(level, player, state, 0);
    }

    private void sendNextTeaMasterLine(Level level, Player player, DialogState state, int lineIndex) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        serverLevel.getServer().execute(() -> {
            if (!state.isActive || player.isRemoved() || !player.isAlive()) {
                activeDialogs.remove(player.getUUID());
                return;
            }

            boolean isRussian = isRussianPlayer(player);
            String[] lines = isRussian ? TEA_MASTER_DIALOG_RU : TEA_MASTER_DIALOG_EN;

            if (lineIndex < lines.length) {
                player.sendSystemMessage(Component.literal(lines[lineIndex]));

                int nextLine = lineIndex + 1;
                serverLevel.getServer().execute(() -> {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    sendNextTeaMasterLine(level, player, state, nextLine);
                });
            } else {
                state.isActive = false;
                activeDialogs.remove(player.getUUID());
            }
        });
    }

    private void startKitsuneDialog(Level level, Player player, KitsuneLightEntity kitsune) {
        if (activeDialogs.containsKey(player.getUUID()) && activeDialogs.get(player.getUUID()).isActive) {
            return;
        }
        DialogState state = new DialogState();
        activeDialogs.put(player.getUUID(), state);
        sendNextKitsuneLine(level, player, kitsune, state, 0);
    }

    private void sendNextKitsuneLine(Level level, Player player, KitsuneLightEntity kitsune, DialogState state, int lineIndex) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        serverLevel.getServer().execute(() -> {
            if (!state.isActive || player.isRemoved() || !player.isAlive()) {
                activeDialogs.remove(player.getUUID());
                return;
            }

            boolean isRussian = isRussianPlayer(player);
            String[] lines = isRussian ? KITSUNE_DIALOG_RU : KITSUNE_DIALOG_EN;
            String name = kitsune.getCustomName() != null ? kitsune.getCustomName().getString() : (isRussian ? "Кицунэ" : "Kitsune");

            if (lineIndex < lines.length) {
                player.sendSystemMessage(Component.literal(name + ": " + lines[lineIndex]));

                int nextLine = lineIndex + 1;
                serverLevel.getServer().execute(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    sendNextKitsuneLine(level, player, kitsune, state, nextLine);
                });
            } else {
                state.isActive = false;
                activeDialogs.remove(player.getUUID());
            }
        });
    }

    private void startMoonDeerDialog(Level level, Player player, MoonDeerEntity moonDeer) {
        if (activeDialogs.containsKey(player.getUUID()) && activeDialogs.get(player.getUUID()).isActive) {
            return;
        }
        DialogState state = new DialogState();
        activeDialogs.put(player.getUUID(), state);
        sendNextMoonDeerLine(level, player, moonDeer, state, 0);
    }

    private void sendNextMoonDeerLine(Level level, Player player, MoonDeerEntity moonDeer, DialogState state, int lineIndex) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        serverLevel.getServer().execute(() -> {
            if (!state.isActive || player.isRemoved() || !player.isAlive()) {
                activeDialogs.remove(player.getUUID());
                return;
            }

            boolean isRussian = isRussianPlayer(player);
            String[] lines = isRussian ? MOON_DEER_DIALOG_RU : MOON_DEER_DIALOG_EN;
            String name = moonDeer.getCustomName() != null ? moonDeer.getCustomName().getString() : (isRussian ? "Лунный олень" : "Moon Deer");

            if (lineIndex < lines.length) {
                player.sendSystemMessage(Component.literal(name + ": " + lines[lineIndex]));

                int nextLine = lineIndex + 1;
                serverLevel.getServer().execute(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    sendNextMoonDeerLine(level, player, moonDeer, state, nextLine);
                });
            } else {
                state.isActive = false;
                activeDialogs.remove(player.getUUID());
            }
        });
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

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource random) {
        if (state.getValue(DRYING)) {
            int count = state.getValue(LEAVES_COUNT);

            level.setBlock(pos, state
                    .setValue(DRYING, false)
                    .setValue(LEAVES_COUNT, 0), 3);

            if (count > 0) {
                popResource(level, pos, new ItemStack(ModItems.TEA_LEAFS_GREEN.get(), count));
            }

            level.playSound(null, pos, SoundEvents.BAMBOO_BREAK, SoundSource.BLOCKS, 0.8f, 1.2f);
        }
    }
}