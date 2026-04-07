package net.lisalaf.fantastikmod.dialog.mobs;

import net.lisalaf.fantastikmod.dialog.Dialog;
import net.lisalaf.fantastikmod.dialog.DialogNode;
import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BakenekoDialog extends Dialog {

    private final BakenekoEntity bakeneko;
    private final Player player;

    public BakenekoDialog(BakenekoEntity bakeneko, Player player) {
        this.bakeneko = bakeneko;
        this.player = player;
        setupDialog();
    }

    @Override
    protected void setupDialog() {
        // Начальный узел
        addNode("start", new DialogNode(
                Component.translatable("dialog.bakeneko.greeting"), "start"
        ).addOption(Component.translatable("dialog.player.ask_bakeneko_who"), "response_who")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_business"), "response_business")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_follow"), "response_follow")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_steal"), "response_steal")
                .addOption(Component.translatable("dialog.player.goodbye"), "end"));

        // 1.0 Кто ты?
        addNode("response_who", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_who"), "response_who"
        ).addOption(Component.translatable("dialog.player.ask_bakeneko_who_1"), "response_who_1")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_who_2"), "response_who_2")
                .addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("response_who_1", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_who_1"), "response_who_1"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("response_who_2", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_who_2"), "response_who_2"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        // 2.0 Каких дел?
        addNode("response_business", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_business"), "response_business"
        ).addOption(Component.translatable("dialog.player.ask_bakeneko_business_1"), "response_business_1")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_business_2"), "response_business_2")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_business_3"), "end"));

        addNode("response_business_1", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_business_1"), "response_business_1"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("response_business_2", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_business_2"), "response_business_2"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        // 3.0 Зачем преследуешь?
        addNode("response_follow", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_follow"), "response_follow"
        ).addOption(Component.translatable("dialog.player.ask_bakeneko_follow_1"), "response_follow_1")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_follow_2"), "response_follow_2"));

        addNode("response_follow_1", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_follow_1"), "response_follow_1"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("response_follow_2", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_follow_2"), "response_follow_2"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        // 4.0 Кража
        addNode("response_steal", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_steal"), "response_steal"
        ).addOption(Component.translatable("dialog.player.ask_bakeneko_steal_1"), "response_steal_1")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_steal_2"), "response_steal_2")
                .addOption(Component.translatable("dialog.player.ask_bakeneko_steal_3"), "response_steal_3"));

        addNode("response_steal_1", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_steal_1"), "response_steal_1"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("response_steal_2", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_steal_2"), "response_steal_2"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("response_steal_3", new DialogNode(
                Component.translatable("dialog.bakeneko.answer_steal_3"), "response_steal_3"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("end", new DialogNode(
                Component.translatable("dialog.bakeneko.farewell"), "end"
        ));
    }

    @Override
    public void selectOption(int optionIndex) {
        DialogNode node = nodes.get(currentDialogId);
        if (node == null || optionIndex >= node.getOptions().size()) {
            super.selectOption(optionIndex);
            return;
        }

        String toNode = node.getOptions().get(optionIndex).getNextNodeId();

        // Согласие дать еду
        if ("response_follow_1".equals(toNode)) {
            // Выполняем на сервере
            if (player.getServer() != null) {
                player.getServer().execute(() -> {
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (bakeneko.isFood(stack) && !stack.isEmpty()) {
                            stack.shrink(1);
                            bakeneko.heal(4.0F);
                            player.sendSystemMessage(Component.translatable("chat.bakeneko.feed"));
                            bakeneko.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EAT, 0.5F, 1.0F);
                            break;
                        }
                    }
                });
            }
        }

        // Отказ дать еду - атака
        if ("response_follow_2".equals(toNode)) {
            if (player.getServer() != null) {
                player.getServer().execute(() -> {
                    player.hurt(player.damageSources().mobAttack(bakeneko), 3.0F);
                    player.sendSystemMessage(Component.translatable("chat.bakeneko.hurt"));
                    bakeneko.playSound(net.minecraft.sounds.SoundEvents.CAT_HISS, 1.0F, 0.5F);
                });
            }
            // Закрываем диалог на клиенте
            Minecraft.getInstance().setScreen(null);
            return;
        }

        // Возврат украденного
        if ("response_steal_1".equals(toNode)) {
            if (player.getServer() != null) {
                player.getServer().execute(() -> {
                    if (bakeneko.getLastThief() == player) {
                        ItemStack stolenItem = bakeneko.getHeldItem();
                        if (!stolenItem.isEmpty()) {
                            if (!player.getInventory().add(stolenItem.copy())) {
                                player.drop(stolenItem.copy(), false);
                            }
                            bakeneko.setHeldItem(ItemStack.EMPTY);
                            bakeneko.setHasItem(false);
                            bakeneko.setLastThief(null);
                            player.sendSystemMessage(Component.translatable("chat.bakeneko.stolen_from_bakeneko"));
                        }
                    }
                });
            }
        }

        // Злость
        if ("response_steal_2".equals(toNode)) {
            if (player.getServer() != null) {
                player.getServer().execute(() -> {
                    bakeneko.setAngry(true);
                    bakeneko.setLastThief(player);
                    player.sendSystemMessage(Component.translatable("chat.bakeneko.angry"));
                });
            }
        }

        super.selectOption(optionIndex);
    }
}