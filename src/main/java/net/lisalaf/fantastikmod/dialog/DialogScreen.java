package net.lisalaf.fantastikmod.dialog;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lisalaf.fantastikmod.dialog.components.MoodTabButton;
import net.lisalaf.fantastikmod.dialog.mood.MobMood;
import net.lisalaf.fantastikmod.dialog.mood.MoodSystem;
import net.lisalaf.fantastikmod.dialog.quest.KitsuneQuest;
import net.lisalaf.fantastikmod.dialog.quest.Quest;
import net.lisalaf.fantastikmod.dialog.quest.QuestSystem;
import net.lisalaf.fantastikmod.dialog.quest.QuestTabButton;
import net.lisalaf.fantastikmod.entity.custom.KitsuneLightEntity;
import net.lisalaf.fantastikmod.entity.custom.MoonDeerEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("removal")
public class DialogScreen extends Screen {
    private long lastRenderTime = 0;
    private static final ResourceLocation STONE_BRICKS = new ResourceLocation("textures/block/stone_bricks.png");

    private final Entity entity;
    private final Dialog dialog;
    private Button getQuestButton;
    private boolean questCreationChecked = false;

    private DialogButton acceptQuestButton;
    private DialogButton declineQuestButton;
    private DialogButton completeQuestButton;



    private int dialogWidth = 350;
    private int dialogHeight = 200;
    private int dialogX, dialogY;

    private MoodTabButton[] tabButtons;
    private int currentTab = 0;
    private MoodSystem moodSystem;

    private DialogButton[] optionButtons;
    private int selectedButtonIndex = -1;

    private QuestTabButton questTabButton;
    private DialogButton cancelQuestButton;

    public DialogScreen(Entity entity, Dialog dialog) {
        super(Component.empty());
        this.entity = entity;
        this.dialog = dialog;

        if (entity instanceof MobMood mobMood) {
            this.moodSystem = mobMood.getMoodSystem();
        }
    }

    @Override
    protected void init() {
        super.init();

        this.dialogX = (this.width - dialogWidth) / 2;
        this.dialogY = (this.height - dialogHeight) / 2;

        int tabWidth = 28;
        int tabHeight = 35;
        int tabsX = dialogX + dialogWidth + 1;
        int tabsY = dialogY - 3;

        tabButtons = new MoodTabButton[2];

        tabButtons[0] = new MoodTabButton(tabsX, tabsY, tabWidth, tabHeight,
                "💬", 0, currentTab == 0, button -> {
            currentTab = 0;
            updateTabs();
            resetQuestCreationCheck();
            resetQuestButtons();
        });

       // tabButtons[1] = new MoodTabButton(tabsX, tabsY + tabHeight + 5, tabWidth, tabHeight, "⭐", 1, currentTab == 1, button -> {
          //  if (moodSystem != null) {currentTab = 1;updateTabs();resetQuestCreationCheck();resetQuestButtons();}});

        questTabButton = new QuestTabButton(tabsX, tabsY + tabHeight + 5,
                tabWidth, tabHeight, 2, currentTab == 2, button -> {
            currentTab = 2;
            updateTabs();
            resetQuestButtons();
        });

        addRenderableWidget(questTabButton);

        for (int i = 0; i < 2; i++) {
            if (tabButtons[i] != null) {
                addRenderableWidget(tabButtons[i]);
            }
        }


        updateOptionButtons();
        updateTabs();
    }

    private void resetQuestButton() {
        if (getQuestButton != null) {
            removeWidget(getQuestButton);
            getQuestButton = null;
        }
    }

    private void updateTabs() {
        for (int i = 0; i < 2; i++) {
            if (tabButtons[i] != null) {
                tabButtons[i].setActive(currentTab == i);
            }
        }

        if (questTabButton != null) {
            questTabButton.setActive(currentTab == 2);
        }
    }


    private void updateOptionButtons() {
        if (optionButtons != null) {
            for (DialogButton button : optionButtons) {
                if (button != null) {
                    removeWidget(button);
                }
            }
        }

        DialogNode currentNode = dialog.getCurrentNode();
        if (currentNode != null && !currentNode.getOptions().isEmpty()) {
            int optionCount = currentNode.getOptions().size();
            optionButtons = new DialogButton[optionCount];

            int buttonMargin = 6;
            int minButtonWidth = 120;
            int buttonHeight = 28;
            int buttonPadding = 12;
            int maxButtonWidth = dialogWidth / 2 - 40;

            if (optionCount > 3) {
                buttonHeight = 24;
                buttonPadding = 10;
                maxButtonWidth = dialogWidth / 2 - 30;
            }

            int startY = dialogY + 50;
            int maxButtonsHeight = dialogHeight - 80;
            int availableHeightPerButton = maxButtonsHeight / optionCount;

            if (availableHeightPerButton < buttonHeight + buttonMargin) {
                buttonHeight = Math.max(20, availableHeightPerButton - buttonMargin);
            }

            for (int i = 0; i < optionCount; i++) {
                DialogOption option = currentNode.getOptions().get(i);

                int buttonX = dialogX + 20;
                int buttonY = startY + i * (buttonHeight + buttonMargin);

                int finalI = i;
                optionButtons[i] = new DialogButton(
                        buttonX, buttonY, maxButtonWidth, buttonHeight,
                        option.getText(),
                        i,
                        button -> {
                            selectedButtonIndex = finalI;
                            dialog.selectOption(finalI);
                            if (dialog.isFinished()) {
                                this.minecraft.setScreen(null);
                            } else {
                                selectedButtonIndex = -1;
                                updateOptionButtons();
                            }
                        }
                );

                addRenderableWidget(optionButtons[i]);
            }
        } else {
            optionButtons = new DialogButton[0];
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        renderDialogWindow(guiGraphics);

        if (currentTab == 0) {
            renderNpcText(guiGraphics);
            renderEntityName(guiGraphics);
            renderEntityModel(guiGraphics, partialTick);
            renderDialogButtons(guiGraphics, mouseX, mouseY, partialTick);
            renderTabButtons(guiGraphics, mouseX, mouseY, partialTick);

        } else if (currentTab == 1 && moodSystem != null) {
            renderMoodScreenContent(guiGraphics);
            renderTabButtons(guiGraphics, mouseX, mouseY, partialTick);

        } else if (currentTab == 2) {
            renderQuestScreen(guiGraphics);
        }

        if (optionButtons != null) {
            for (DialogButton button : optionButtons) {
                if (button != null) {
                    button.visible = (currentTab == 0);
                }
            }
        }

        if (acceptQuestButton != null) acceptQuestButton.visible = (currentTab == 2);
        if (declineQuestButton != null) declineQuestButton.visible = (currentTab == 2);
        if (completeQuestButton != null) completeQuestButton.visible = (currentTab == 2);
        if (cancelQuestButton != null) cancelQuestButton.visible = (currentTab == 2);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }


    private void renderQuestScreen(GuiGraphics guiGraphics) {
        int mouseX = (int)(minecraft.mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth());
        int mouseY = (int)(minecraft.mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight());
        float partialTick = minecraft.getFrameTime();

        Quest quest = null;
        if (entity instanceof KitsuneLightEntity kitsune) {
            quest = QuestSystem.getQuestForKitsune(kitsune);

            if (quest == null && !questCreationChecked) {
                questCreationChecked = true;
                if (QuestSystem.canGiveNewQuest(kitsune)) {
                    quest = QuestSystem.createNewQuest(kitsune);
                }
            }
        }
        else if (entity instanceof MoonDeerEntity moonDeer) {
            quest = QuestSystem.getQuestForMoonDeer(moonDeer);

            if (quest == null && !questCreationChecked) {
                questCreationChecked = true;
                if (QuestSystem.canGiveNewQuest(moonDeer)) {
                    quest = QuestSystem.createNewQuest(moonDeer);
                }
            }
        }

        guiGraphics.drawCenteredString(this.font,
                Component.translatable("gui.quest.title"),
                dialogX + dialogWidth / 2, dialogY + 15, 0xFFFFFF);

        if (quest == null) {
            guiGraphics.drawCenteredString(this.font,
                    Component.translatable("gui.quest.none"),
                    dialogX + dialogWidth / 2, dialogY + 60, 0xAAAAAA);
        } else {
            renderActiveQuest(guiGraphics, quest);

            if (!quest.isActive()) {
                renderQuestButtons(guiGraphics, mouseX, mouseY, partialTick, quest);
            } else {
                if (quest.canComplete()) {
                    renderCompleteButton(guiGraphics, mouseX, mouseY, partialTick, quest);
                } else {
                    renderActiveQuestInfo(guiGraphics, mouseX, mouseY, partialTick, quest);
                }
            }
        }
    }

    private void resetQuestCreationCheck() {
        questCreationChecked = false;
    }



    private void renderQuestButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, Quest quest) {
        int centerX = dialogX + dialogWidth / 2;
        int buttonY = dialogY + 160;

        if (quest != null && !quest.isActive()) {
            if (acceptQuestButton == null) {
                acceptQuestButton = new DialogButton(
                        centerX - 100, buttonY, 90, 24,
                        Component.translatable("gui.quest.accept"),
                        0, button -> {
                    quest.start();
                    resetQuestButtons();
                });
                addRenderableWidget(acceptQuestButton);
            }

            if (declineQuestButton == null) {
                declineQuestButton = new DialogButton(
                        centerX + 10, buttonY, 90, 24,
                        Component.translatable("gui.quest.decline"),
                        1, button -> {
                    // ОТМЕНА ДЛЯ ЛЮБОЙ СУЩНОСТИ
                    if (entity instanceof KitsuneLightEntity kitsune) {
                        QuestSystem.cancelQuest(kitsune);
                    } else if (entity instanceof MoonDeerEntity moonDeer) {
                        QuestSystem.cancelQuest(moonDeer);
                    }
                    resetQuestButtons();
                    resetQuestCreationCheck();
                });
                addRenderableWidget(declineQuestButton);
            }
        }
    }



    private void renderCompleteButton(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, Quest quest) {
        int centerX = dialogX + dialogWidth / 2;
        int buttonY = dialogY + 180;

        if (quest != null && quest.isActive() && quest.canComplete()) {
            boolean hasItems = true;
            if (minecraft.player != null) {
                int itemCount = minecraft.player.getInventory().countItem(quest.getRequiredItem());
                hasItems = itemCount >= quest.getRequiredAmount();
            }

            if (hasItems) {
                if (completeQuestButton == null) {
                    completeQuestButton = new DialogButton(
                            centerX - 45, buttonY, 90, 24,
                            Component.translatable("gui.quest.complete"),
                            2, button -> {
                        if (minecraft.player != null) {
                            boolean success = false;
                            // ЗАВЕРШЕНИЕ ДЛЯ ЛЮБОЙ СУЩНОСТИ
                            if (entity instanceof KitsuneLightEntity kitsune) {
                                success = QuestSystem.completeQuest(kitsune, minecraft.player);
                            } else if (entity instanceof MoonDeerEntity moonDeer) {
                                success = QuestSystem.completeQuest(moonDeer, minecraft.player);
                            }
                            if (success) {
                                resetQuestButtons();
                            }
                        }
                    });
                    addRenderableWidget(completeQuestButton);
                }
            }
        }
    }

    private void renderActiveQuestInfo(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, Quest quest) {
        int centerX = dialogX + dialogWidth / 2;
        int buttonY = dialogY + 180;

        if (quest != null && quest.isActive()) {
            long timeLeft = quest.getTimeLeft();

            if (cancelQuestButton == null) {
                cancelQuestButton = new DialogButton(
                        centerX - 45, buttonY, 90, 24,
                        Component.translatable("gui.quest.cancel"),
                        3, button -> {
                    // ИСПРАВЬ ЭТУ СТРОКУ!
                    QuestSystem.cancelQuestForEntity(entity);
                    resetQuestButtons();
                    resetQuestCreationCheck();
                });
                addRenderableWidget(cancelQuestButton);
            }

            // Обновляем текст кнопки с актуальным временем
            if (timeLeft > 0) {
                long minutes = timeLeft / (60 * 1000);
                long seconds = (timeLeft % (60 * 1000)) / 1000;
                String timeStr = String.format("%d:%02d", minutes, seconds);
                cancelQuestButton.setMessage(Component.translatable("gui.quest.cancel")
                        .copy()
                        .append(" (")
                        .append(Component.literal(timeStr))
                        .append(")"));
            } else {
                cancelQuestButton.setMessage(Component.translatable("gui.quest.cancel"));
            }

            if (cancelQuestButton != null) {
                cancelQuestButton.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }
    }


    private void renderActiveQuest(GuiGraphics guiGraphics, Quest quest) {
        int centerX = dialogX + dialogWidth / 2;

        guiGraphics.drawCenteredString(this.font, quest.getName(),
                centerX, dialogY + 30, 0xFFFFFF);

        var descLines = this.font.split(quest.getDescription(), dialogWidth - 40);
        for (int i = 0; i < Math.min(descLines.size(), 2); i++) {
            guiGraphics.drawCenteredString(this.font, descLines.get(i),
                    centerX, dialogY + 50 + i * (this.font.lineHeight + 2), 0xAAAAAA);
        }

        guiGraphics.fill(dialogX + 30, dialogY + 90, dialogX + dialogWidth - 30, dialogY + 91, 0x55FFFFFF);

        int leftX = dialogX + 50;
        int rightX = dialogX + dialogWidth - 80;
        int itemY = dialogY + 110;

        guiGraphics.drawString(this.font, Component.translatable("gui.quest.requires"),
                leftX - 40, itemY - 15, 0xFFFFFF);

        guiGraphics.drawString(this.font, Component.translatable("gui.quest.reward"),
                rightX - 30, itemY - 15, 0xFFFFFF);

        renderItemInFrame(guiGraphics, quest.getRequiredItem(),
                quest.getRequiredAmount(), leftX, itemY, false);

        renderItemInFrame(guiGraphics, quest.getRewardItem(),
                quest.getRewardAmount(), rightX, itemY, true);

        if (quest.isActive()) {
            int currentCount = quest.getCurrentCount();
            int required = quest.getRequiredAmount();

            String progressText = currentCount + "/" + required;
            int textColor = currentCount >= required ? 0xFF00FF00 : 0xFFFFFF;

            Component progressLabel = Component.translatable("gui.quest.collected", progressText);

            guiGraphics.drawCenteredString(this.font, progressLabel,
                    centerX, dialogY + 150, textColor);
        }
    }


    private void resetQuestButtons() {
        if (acceptQuestButton != null) {
            removeWidget(acceptQuestButton);
            acceptQuestButton = null;
        }
        if (declineQuestButton != null) {
            removeWidget(declineQuestButton);
            declineQuestButton = null;
        }
        if (completeQuestButton != null) {
            removeWidget(completeQuestButton);
            completeQuestButton = null;
        }
        if (cancelQuestButton != null) {
            removeWidget(cancelQuestButton);
            cancelQuestButton = null;
        }
    }

    private void renderItemInFrame(GuiGraphics guiGraphics, Item item, int count, int x, int y, boolean isReward) {
        int frameSize = 40; // Размер рамки
        int frameX = x - frameSize / 2;
        int frameY = y - 5;

        // Цвета рамки
        int frameColor = isReward ? 0xFFD4A017 : 0xFF780D13; // Золото для награды, бордо для требуемого
        int innerColor = 0xFF2A1B0E;
        int accentColor = isReward ? 0xFFFFD700 : 0xFFA0151C;

        // Внешняя рамка
        guiGraphics.fill(frameX, frameY, frameX + frameSize, frameY + frameSize, frameColor);

        // Внутренняя область
        guiGraphics.fill(frameX + 2, frameY + 2, frameX + frameSize - 2, frameY + frameSize - 2, innerColor);

        // Угловые акценты
        int cornerSize = 4;
        for (int i = 0; i < cornerSize; i++) {
            // Левый верхний
            guiGraphics.fill(frameX + i, frameY, frameX + i + 1, frameY + 1, accentColor);
            guiGraphics.fill(frameX, frameY + i, frameX + 1, frameY + i + 1, accentColor);

            // Правый верхний
            guiGraphics.fill(frameX + frameSize - 1 - i, frameY,
                    frameX + frameSize - i, frameY + 1, accentColor);
            guiGraphics.fill(frameX + frameSize - 1, frameY + i,
                    frameX + frameSize, frameY + i + 1, accentColor);

            // Левый нижний
            guiGraphics.fill(frameX + i, frameY + frameSize - 1,
                    frameX + i + 1, frameY + frameSize, accentColor);
            guiGraphics.fill(frameX, frameY + frameSize - 1 - i,
                    frameX + 1, frameY + frameSize - i, accentColor);

            // Правый нижний
            guiGraphics.fill(frameX + frameSize - 1 - i, frameY + frameSize - 1,
                    frameX + frameSize - i, frameY + frameSize, accentColor);
            guiGraphics.fill(frameX + frameSize - 1, frameY + frameSize - 1 - i,
                    frameX + frameSize, frameY + frameSize - i, accentColor);
        }

        try {
            // Рендерим иконку предмета
            ItemStack itemStack = new ItemStack(item, count);

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(frameX + frameSize / 2, frameY + frameSize / 2, 0);
            poseStack.scale(1.5f, 1.5f, 1.5f); // Увеличиваем иконку

            guiGraphics.renderItem(itemStack, -8, -8); // Центрируем

            poseStack.popPose();

            // Количество предметов
            if (count > 1) {
                String countText = "x" + count;
                int textWidth = this.font.width(countText);
                int textX = frameX + frameSize - textWidth - 2;
                int textY = frameY + frameSize - 9;

                // Фон для текста
                guiGraphics.fill(textX - 1, textY - 1, textX + textWidth + 1, textY + 9, 0x80000000);

                // Текст количества
                guiGraphics.drawString(this.font, countText, textX, textY, 0xFFFFFF, false);
            }

        } catch (Exception e) {
            // Fallback: если не удалось отрендерить предмет
            guiGraphics.drawCenteredString(this.font,
                    Component.literal("?"),
                    frameX + frameSize / 2,
                    frameY + frameSize / 2 - 4,
                    0xFFFFFF);
        }

        // Название предмета под рамкой
        String itemName = item.getDescription().getString();
        if (itemName.length() > 12) {
            itemName = itemName.substring(0, 10) + "...";
        }

        guiGraphics.drawCenteredString(this.font,
                Component.literal(itemName),
                frameX + frameSize / 2,
                frameY + frameSize + 5,
                0xAAAAAA);
    }


    private void renderDialogButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        if (optionButtons != null) {
            for (DialogButton button : optionButtons) {
                if (button != null && button.visible) {
                    button.render(guiGraphics, mouseX, mouseY, partialTick);
                }
            }
        }
    }

    private void renderTabButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (int i = 0; i < 2; i++) {
            if (tabButtons[i] != null) {
                tabButtons[i].render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        if (questTabButton != null) {
            questTabButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void renderMoodScreenContent(GuiGraphics guiGraphics) {
        String mobTypeName = entity instanceof MobMood ?
                ((MobMood)entity).getMobTypeName() : "kitsune";
        String customName = entity.hasCustomName() ?
                entity.getCustomName().getString() : null;
        String ownerName = entity instanceof MobMood ?
                ((MobMood)entity).getOwnerName() : null;

        // ЗАГОЛОВОК
        Component moodTitle = Component.translatable("gui.mood.title");
        guiGraphics.drawCenteredString(this.font, moodTitle,
                dialogX + dialogWidth / 2, dialogY + 15, 0xFFFFFF);

        renderMoodBar(guiGraphics);

        renderCurrentMood(guiGraphics);

        renderOwnerInfo(guiGraphics, ownerName);
    }

    private void renderTitle(GuiGraphics guiGraphics, String mobTypeName, String customName) {
        Component mobName = Component.translatable("entity.fantastikmod." + mobTypeName);
        guiGraphics.drawCenteredString(this.font, mobName,
                dialogX + dialogWidth / 2, dialogY + 15, 0xFFFFFF);

        if (customName != null && !customName.isEmpty()) {
            guiGraphics.drawCenteredString(this.font,
                    Component.literal(": " + customName),
                    dialogX + dialogWidth / 2 + this.font.width(mobName) / 2 + 2,
                    dialogY + 15, 0xFFFF00);
        }
    }

    private void renderMoodBar(GuiGraphics guiGraphics) {
        if (moodSystem == null) return;

        int barY = dialogY + 55;
        int barHeight = 25; // Выше для золотой полоски
        int barWidth = dialogWidth - 60; // Увеличиваем

        // 1. Градиентная полоска настроения (красный-серый-зеленый)
        for (int x = 0; x < barWidth; x++) {
            float progress = (float)x / barWidth;
            int color = calculateMoodGradient(progress);
            guiGraphics.fill(dialogX + 30 + x, barY + 2,
                    dialogX + 31 + x, barY + barHeight - 2, color);
        }

        // 2. ЗОЛОТАЯ полоска-указатель (ПОВЕРХ градиента)
        float moodPercent = moodSystem.getMoodPercentage();
        int goldX = dialogX + 30 + (int)(barWidth * moodPercent);

        // Толстая золотая линия
        for (int i = -2; i <= 2; i++) {
            guiGraphics.fill(goldX + i, barY,
                    goldX + i + 1, barY + barHeight, 0xFFFFD700);
        }

        // Декоративные концы золотой полоски
        int[] triangleX = {goldX - 3, goldX, goldX + 3};
        int[] triangleY = {barY - 3, barY, barY - 3};
        for (int i = 0; i < 3; i++) {
            guiGraphics.fill(triangleX[i], triangleY[i],
                    triangleX[i] + 1, triangleY[i] + 1, 0xFFFFD700);
        }

        // Шкала значений
        guiGraphics.drawString(this.font, "-100", dialogX + 25, barY + barHeight + 5, 0xAAAAAA);
        guiGraphics.drawCenteredString(this.font, "0",
                dialogX + dialogWidth / 2, barY + barHeight + 5, 0xAAAAAA);
        guiGraphics.drawString(this.font, "100",
                dialogX + dialogWidth - 40, barY + barHeight + 5, 0xAAAAAA);

        // Подпись
        guiGraphics.drawCenteredString(this.font,
                Component.translatable("gui.mood.title"),
                dialogX + dialogWidth / 2, barY - 15, 0xFFFFFF);
    }

    private void renderMobInFrame(GuiGraphics guiGraphics, String mobId, int count, int x, int y) {
        int frameSize = 40;
        int frameX = x - frameSize / 2;
        int frameY = y - 5;

        // Цвета рамки для мобов
        int frameColor = 0xFF780D13; // Бордо
        int innerColor = 0xFF2A1B0E;
        int accentColor = 0xFFA0151C;

        // Рисуем рамку (как в renderItemInFrame)
        // ... код рамки такой же как в renderItemInFrame ...

        // Вместо иконки предмета рисуем символ моба
        String mobSymbol = getMobSymbol(mobId);
        guiGraphics.drawCenteredString(this.font, mobSymbol,
                frameX + frameSize / 2, frameY + frameSize / 2 - 4, 0xFFFFFF);

        // Название моба под рамкой
        String mobName = getMobName(mobId);
        guiGraphics.drawCenteredString(this.font,
                Component.literal(mobName),
                frameX + frameSize / 2,
                frameY + frameSize + 5,
                0xAAAAAA);
    }

    private String getMobSymbol(String mobId) {
        switch (mobId) {
            case "zombie": return "🧟";
            case "skeleton": return "💀";
            case "creeper": return "🌀";
            default: return "👹";
        }
    }

    private String getMobName(String mobId) {
        switch (mobId) {
            case "zombie": return "Зомби";
            case "skeleton": return "Скелет";
            case "creeper": return "Крипер";
            default: return mobId;
        }
    }

    // Градиент красный-серый-зеленый
    private int calculateMoodGradient(float progress) {
        if (progress < 0.33f) {
            float local = progress / 0.33f;
            int r = 255 - (int)(105 * local);
            int g = 0 + (int)(150 * local);
            int b = 0 + (int)(150 * local);
            return 0xFF000000 | (r << 16) | (g << 8) | b;
        } else if (progress < 0.66f) {
            float local = (progress - 0.33f) / 0.33f;
            int r = 150 - (int)(150 * local);
            int g = 150 + (int)(105 * local);
            int b = 150 - (int)(150 * local);
            return 0xFF000000 | (r << 16) | (g << 8) | b;
        } else {
            return 0xFF00FF00;
        }
    }

    private void renderCurrentMood(GuiGraphics guiGraphics) {
        if (moodSystem == null) return;

        int textY = dialogY + 100;

        // Текущее значение
        String moodText = String.valueOf(moodSystem.getMood());
        int moodColor = moodSystem.getMoodColor();

        guiGraphics.drawString(this.font,
                Component.translatable("gui.mood.current"),
                dialogX + 30, textY, 0xFFFFFF);

        guiGraphics.drawString(this.font, moodText,
                dialogX + dialogWidth - 40 - this.font.width(moodText),
                textY, moodColor);

        // Текстовое описание
        Component moodDescription = Component.translatable(moodSystem.getMoodTextKey());
        guiGraphics.drawCenteredString(this.font, moodDescription,
                dialogX + dialogWidth / 2, textY + 25, moodColor);
    }

    private void renderOwnerInfo(GuiGraphics guiGraphics, String ownerName) {
        if (ownerName != null && !ownerName.isEmpty()) {
            int infoY = dialogY + 140;
            guiGraphics.drawString(this.font,
                    Component.translatable("gui.mood.companion", ownerName),
                    dialogX + 30, infoY, 0xAAAAAA);
        }
    }


    private void renderDialogWindow(GuiGraphics guiGraphics) {
        renderMinecraftStoneBrickBackground(guiGraphics);

        renderThickBordeauxFrame(guiGraphics);

        drawFramePatterns(guiGraphics);

        drawOrnateGemCorners(guiGraphics);
    }

    private void renderMinecraftStoneBrickBackground(GuiGraphics guiGraphics) {
        guiGraphics.setColor(0.25f, 0.25f, 0.25f, 1.0f);

        int textureSize = 16;
        int repeatX = (dialogWidth + textureSize - 1) / textureSize;
        int repeatY = (dialogHeight + textureSize - 1) / textureSize;

        for (int y = 0; y < repeatY; y++) {
            float brightness = 0.7f + 0.3f * ((float)y / repeatY);
            guiGraphics.setColor(brightness * 0.25f, brightness * 0.25f, brightness * 0.25f, 1.0f);

            for (int x = 0; x < repeatX; x++) {
                int texX = dialogX + x * textureSize;
                int texY = dialogY + y * textureSize;
                int texWidth = Math.min(textureSize, dialogX + dialogWidth - texX);
                int texHeight = Math.min(textureSize, dialogY + dialogHeight - texY);

                float u1 = 0.0f;
                float v1 = 0.0f;
                float u2 = (float)texWidth / textureSize;
                float v2 = (float)texHeight / textureSize;

                guiGraphics.blit(STONE_BRICKS, texX, texY, texWidth, texHeight, u1, v1, (int)(u2 * textureSize), (int)(v2 * textureSize), textureSize, textureSize);
            }
        }

        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        for (int y = 0; y < dialogHeight; y++) {
            float progress = (float)y / dialogHeight;
            int alpha = (int)((0.5f - progress * 0.3f) * 255);
            guiGraphics.fill(dialogX, dialogY + y, dialogX + dialogWidth, dialogY + y + 1, (alpha << 24));
        }

        for (int i = 0; i < 6; i++) {
            float alpha = 0.15f - i * 0.025f;
            int darkColor = (int)(alpha * 255) << 24;

            guiGraphics.fill(dialogX, dialogY + i, dialogX + dialogWidth, dialogY + i + 1, darkColor);
            guiGraphics.fill(dialogX, dialogY + dialogHeight - i - 1, dialogX + dialogWidth, dialogY + dialogHeight - i, darkColor);
            guiGraphics.fill(dialogX + i, dialogY, dialogX + i + 1, dialogY + dialogHeight, darkColor);
            guiGraphics.fill(dialogX + dialogWidth - i - 1, dialogY, dialogX + dialogWidth - i, dialogY + dialogHeight, darkColor);
        }
    }

    private void renderThickBordeauxFrame(GuiGraphics guiGraphics) {
        int bordeauxDark = 0xFF50050A;
        int bordeauxMedium = 0xFF780D13;
        int bordeauxLight = 0xFFA0151C;

        int frameThickness = 8;

        for (int i = 0; i < frameThickness; i++) {
            float progress = (float)i / frameThickness;
            int r = (int)(0x50 * (1 - progress) + 0xA0 * progress);
            int g = (int)(0x05 * (1 - progress) + 0x15 * progress);
            int b = (int)(0x0A * (1 - progress) + 0x1C * progress);
            int color = (0xFF << 24) | (r << 16) | (g << 8) | b;

            guiGraphics.fill(dialogX - i - 1, dialogY - i - 1,
                    dialogX + dialogWidth + i + 1, dialogY - i, color);
            guiGraphics.fill(dialogX - i - 1, dialogY + dialogHeight + i,
                    dialogX + dialogWidth + i + 1, dialogY + dialogHeight + i + 1, color);
            guiGraphics.fill(dialogX - i - 1, dialogY - i - 1,
                    dialogX - i, dialogY + dialogHeight + i + 1, color);
            guiGraphics.fill(dialogX + dialogWidth + i, dialogY - i - 1,
                    dialogX + dialogWidth + i + 1, dialogY + dialogHeight + i + 1, color);
        }

        int goldStripThickness = 2;
        int goldStripOffset = frameThickness;

        for (int i = 0; i < goldStripThickness; i++) {
            for (int x = dialogX - goldStripOffset; x < dialogX + dialogWidth + goldStripOffset; x++) {
                float progress = (float)(x - (dialogX - goldStripOffset)) / (dialogWidth + 2 * goldStripOffset);
                int color = calculateGoldGradient(progress);
                guiGraphics.fill(x, dialogY - goldStripOffset + i, x + 1, dialogY - goldStripOffset + i + 1, color);
            }

            for (int x = dialogX - goldStripOffset; x < dialogX + dialogWidth + goldStripOffset; x++) {
                float progress = (float)(x - (dialogX - goldStripOffset)) / (dialogWidth + 2 * goldStripOffset);
                int color = calculateGoldGradient(progress);
                guiGraphics.fill(x, dialogY + dialogHeight + goldStripOffset - i - 1,
                        x + 1, dialogY + dialogHeight + goldStripOffset - i, color);
            }

            for (int y = dialogY - goldStripOffset; y < dialogY + dialogHeight + goldStripOffset; y++) {
                float progress = (float)(y - (dialogY - goldStripOffset)) / (dialogHeight + 2 * goldStripOffset);
                int color = calculateGoldGradient(progress);
                guiGraphics.fill(dialogX - goldStripOffset + i, y,
                        dialogX - goldStripOffset + i + 1, y + 1, color);
            }

            for (int y = dialogY - goldStripOffset; y < dialogY + dialogHeight + goldStripOffset; y++) {
                float progress = (float)(y - (dialogY - goldStripOffset)) / (dialogHeight + 2 * goldStripOffset);
                int color = calculateGoldGradient(progress);
                guiGraphics.fill(dialogX + dialogWidth + goldStripOffset - i - 1, y,
                        dialogX + dialogWidth + goldStripOffset - i, y + 1, color);
            }
        }

        int innerOffset = frameThickness + goldStripThickness;
        guiGraphics.fill(dialogX - innerOffset, dialogY - innerOffset,
                dialogX + dialogWidth + innerOffset, dialogY - innerOffset + 1, bordeauxLight);
        guiGraphics.fill(dialogX - innerOffset, dialogY + dialogHeight + innerOffset - 1,
                dialogX + dialogWidth + innerOffset, dialogY + dialogHeight + innerOffset, bordeauxLight);
        guiGraphics.fill(dialogX - innerOffset, dialogY - innerOffset,
                dialogX - innerOffset + 1, dialogY + dialogHeight + innerOffset, bordeauxLight);
        guiGraphics.fill(dialogX + dialogWidth + innerOffset - 1, dialogY - innerOffset,
                dialogX + dialogWidth + innerOffset, dialogY + dialogHeight + innerOffset, bordeauxLight);
    }

    private void drawFramePatterns(GuiGraphics guiGraphics) {
        int patternColor = 0xFFD4A017;
        int patternAccent = 0xFFFFD700;

        int patternSpacing = 12;

        for (int x = dialogX + 20; x < dialogX + dialogWidth - 20; x += patternSpacing) {
            drawTopPattern(guiGraphics, x, dialogY - 10, patternColor, patternAccent);
        }

        for (int x = dialogX + 20; x < dialogX + dialogWidth - 20; x += patternSpacing) {
            drawBottomPattern(guiGraphics, x, dialogY + dialogHeight + 6, patternColor, patternAccent);
        }

        for (int y = dialogY + 20; y < dialogY + dialogHeight - 20; y += patternSpacing) {
            drawLeftPattern(guiGraphics, dialogX - 10, y, patternColor, patternAccent);
        }

        for (int y = dialogY + 20; y < dialogY + dialogHeight - 20; y += patternSpacing) {
            drawRightPattern(guiGraphics, dialogX + dialogWidth + 6, y, patternColor, patternAccent);
        }

        drawCornerPatterns(guiGraphics, patternColor, patternAccent);
    }

    private void drawTopPattern(GuiGraphics guiGraphics, int x, int y, int mainColor, int accentColor) {
        for (int i = 0; i < 3; i++) {
            guiGraphics.fill(x - 1 + i, y, x + i, y + 1, mainColor);
        }

        guiGraphics.fill(x, y + 1, x + 1, y + 2, accentColor);
        guiGraphics.fill(x + 1, y + 1, x + 2, y + 2, mainColor);

        for (int i = 0; i < 2; i++) {
            guiGraphics.fill(x - 1, y + 2 + i, x, y + 3 + i, mainColor);
            guiGraphics.fill(x + 2, y + 2 + i, x + 3, y + 3 + i, mainColor);
        }
    }

    private void drawBottomPattern(GuiGraphics guiGraphics, int x, int y, int mainColor, int accentColor) {
        for (int i = 0; i < 3; i++) {
            guiGraphics.fill(x - 1 + i, y, x + i, y + 1, mainColor);
        }

        guiGraphics.fill(x, y - 1, x + 1, y, accentColor);
        guiGraphics.fill(x + 1, y - 1, x + 2, y, mainColor);

        for (int i = 0; i < 2; i++) {
            guiGraphics.fill(x - 1, y - 3 - i, x, y - 2 - i, mainColor);
            guiGraphics.fill(x + 2, y - 3 - i, x + 3, y - 2 - i, mainColor);
        }
    }

    private void drawLeftPattern(GuiGraphics guiGraphics, int x, int y, int mainColor, int accentColor) {
        for (int i = 0; i < 3; i++) {
            guiGraphics.fill(x, y - 1 + i, x + 1, y + i, mainColor);
        }

        guiGraphics.fill(x + 1, y, x + 2, y + 1, accentColor);
        guiGraphics.fill(x + 1, y + 1, x + 2, y + 2, mainColor);

        for (int i = 0; i < 2; i++) {
            guiGraphics.fill(x + 2 + i, y - 1, x + 3 + i, y, mainColor);
            guiGraphics.fill(x + 2 + i, y + 2, x + 3 + i, y + 3, mainColor);
        }
    }

    private void drawRightPattern(GuiGraphics guiGraphics, int x, int y, int mainColor, int accentColor) {
        for (int i = 0; i < 3; i++) {
            guiGraphics.fill(x, y - 1 + i, x + 1, y + i, mainColor);
        }

        guiGraphics.fill(x - 1, y, x, y + 1, accentColor);
        guiGraphics.fill(x - 1, y + 1, x, y + 2, mainColor);

        for (int i = 0; i < 2; i++) {
            guiGraphics.fill(x - 3 - i, y - 1, x - 2 - i, y, mainColor);
            guiGraphics.fill(x - 3 - i, y + 2, x - 2 - i, y + 3, mainColor);
        }
    }

    private void drawCornerPatterns(GuiGraphics guiGraphics, int mainColor, int accentColor) {
        int cornerSize = 6;

        drawCornerDetail(guiGraphics, dialogX - cornerSize, dialogY - cornerSize, mainColor, accentColor, false, false);
        drawCornerDetail(guiGraphics, dialogX + dialogWidth, dialogY - cornerSize, mainColor, accentColor, true, false);
        drawCornerDetail(guiGraphics, dialogX - cornerSize, dialogY + dialogHeight, mainColor, accentColor, false, true);
        drawCornerDetail(guiGraphics, dialogX + dialogWidth, dialogY + dialogHeight, mainColor, accentColor, true, true);
    }

    private void drawCornerDetail(GuiGraphics guiGraphics, int x, int y, int mainColor, int accentColor, boolean flipX, boolean flipY) {
        int dirX = flipX ? -1 : 1;
        int dirY = flipY ? -1 : 1;

        for (int i = 0; i < 3; i++) {
            guiGraphics.fill(x + i * dirX, y, x + (i + 1) * dirX, y + 1, mainColor);
            guiGraphics.fill(x, y + i * dirY, x + 1, y + (i + 1) * dirY, mainColor);
        }

        for (int i = 1; i < 4; i++) {
            guiGraphics.fill(x + i * dirX, y + 1 * dirY, x + (i + 1) * dirX, y + 2 * dirY, i == 2 ? accentColor : mainColor);
            guiGraphics.fill(x + 1 * dirX, y + i * dirY, x + 2 * dirX, y + (i + 1) * dirY, i == 2 ? accentColor : mainColor);
        }
    }

    private int calculateGoldGradient(float progress) {
        if (progress < 0.25f) {
            float local = progress / 0.25f;
            int r = (int)(0xE0 * (1 - local) + 0xF5 * local);
            int g = (int)(0x4C * (1 - local) + 0x7A * local);
            int b = (int)(0x02 * (1 - local) + 0x16 * local);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        } else if (progress < 0.5f) {
            float local = (progress - 0.25f) / 0.25f;
            int r = (int)(0xF5 * (1 - local) + 0xFF * local);
            int g = (int)(0x7A * (1 - local) + 0xC9 * local);
            int b = (int)(0x16 * (1 - local) + 0x40 * local);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        } else if (progress < 0.75f) {
            float local = (progress - 0.5f) / 0.25f;
            int r = (int)(0xFF * (1 - local) + 0xFF * local);
            int g = (int)(0xC9 * (1 - local) + 0xF9 * local);
            int b = (int)(0x40 * (1 - local) + 0x8A * local);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        } else {
            float local = (progress - 0.75f) / 0.25f;
            int r = (int)(0xFF * (1 - local) + 0xE0 * local);
            int g = (int)(0xF9 * (1 - local) + 0x4C * local);
            int b = (int)(0x8A * (1 - local) + 0x02 * local);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        }
    }

    private void drawOrnateGemCorners(GuiGraphics guiGraphics) {
        int cornerSize = 20;

        drawOrnateCorner(guiGraphics, dialogX, dialogY, cornerSize, 0xFFC41E3A, false, false);
        drawOrnateCorner(guiGraphics, dialogX + dialogWidth, dialogY, cornerSize, 0xFF50C878, true, false);
        drawOrnateCorner(guiGraphics, dialogX, dialogY + dialogHeight, cornerSize, 0xFF0F52BA, false, true);
        drawOrnateCorner(guiGraphics, dialogX + dialogWidth, dialogY + dialogHeight, cornerSize, 0xFF9966CC, true, true);
    }

    private void drawOrnateCorner(GuiGraphics guiGraphics, int x, int y, int size, int gemColor, boolean flipX, boolean flipY) {
        int dirX = flipX ? -1 : 1;
        int dirY = flipY ? -1 : 1;

        int frameSize = size;
        int innerSize = size - 4;

        for (int i = 0; i < frameSize; i++) {
            float progress = (float)i / frameSize;
            int frameColor = calculateOrnateFrameGradient(progress);

            guiGraphics.fill(x - i * dirX, y, x - (i - 1) * dirX, y + 1, frameColor);
            guiGraphics.fill(x, y - i * dirY, x + 1, y - (i - 1) * dirY, frameColor);
        }

        for (int i = 2; i < innerSize; i++) {
            for (int j = 2; j < innerSize; j++) {
                if ((i + j) % 3 == 0) {
                    float xProgress = (float)i / innerSize;
                    float yProgress = (float)j / innerSize;
                    int patternColor = calculatePatternColor(xProgress, yProgress);
                    guiGraphics.fill(x - i * dirX, y - j * dirY,
                            x - (i - 1) * dirX, y - (j - 1) * dirY, patternColor);
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 || j == 0 || i == 3 || j == 3) {
                    int accentColor = 0xFFFFF98A;
                    guiGraphics.fill(x - (i + 1) * dirX, y - (j + 1) * dirY,
                            x - i * dirX, y - j * dirY, accentColor);
                }
            }
        }

        for (int i = 1; i < innerSize - 1; i += 2) {
            int detailColor = 0xFF8B6914;
            guiGraphics.fill(x - (i + 1) * dirX, y - 1 * dirY, x - i * dirX, y, detailColor);
            guiGraphics.fill(x - 1 * dirX, y - (i + 1) * dirY, x, y - i * dirY, detailColor);
        }

        int gemX = x - (size/2) * dirX;
        int gemY = y - (size/2) * dirY;
        drawOrnateGem(guiGraphics, gemX, gemY, gemColor, dirX, dirY);
    }

    private int calculateOrnateFrameGradient(float progress) {
        if (progress < 0.33f) {
            float local = progress / 0.33f;
            int r = (int)(0x8B * (1 - local) + 0xD4 * local);
            int g = (int)(0x69 * (1 - local) + 0xAF * local);
            int b = (int)(0x14 * (1 - local) + 0x17 * local);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        } else if (progress < 0.66f) {
            float local = (progress - 0.33f) / 0.33f;
            int r = (int)(0xD4 * (1 - local) + 0xFF * local);
            int g = (int)(0xAF * (1 - local) + 0xD7 * local);
            int b = (int)(0x17 * (1 - local) + 0x00 * local);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        } else {
            float local = (progress - 0.66f) / 0.34f;
            int r = (int)(0xFF * (1 - local) + 0xF5 * local);
            int g = (int)(0xD7 * (1 - local) + 0x7A * local);
            int b = (int)(0x00 * (1 - local) + 0x16 * local);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        }
    }

    private int calculatePatternColor(float xProgress, float yProgress) {
        float pattern = (float)Math.sin(xProgress * Math.PI * 2) * (float)Math.cos(yProgress * Math.PI * 2);
        float brightness = 0.5f + 0.3f * pattern;

        int r = (int)(0xD4 * brightness);
        int g = (int)(0xAF * brightness);
        int b = (int)(0x17 * brightness);

        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    private void drawOrnateGem(GuiGraphics guiGraphics, int x, int y, int gemColor, int dirX, int dirY) {
        int gemSize = 10;
        int lightColor = lighten(gemColor, 0.5f);
        int mediumColor = gemColor;
        int darkColor = darken(gemColor, 0.5f);

        for (int i = 0; i < gemSize; i++) {
            int width = gemSize - Math.abs(i - gemSize/2);
            int startX = x - (width/2) * dirX;
            int currentY = y + (i - gemSize/2) * dirY;

            for (int j = 0; j < width; j++) {
                float xProgress = (float)j / width;
                float yProgress = (float)i / gemSize;
                float diamondPattern = Math.abs(xProgress - 0.5f) + Math.abs(yProgress - 0.5f);

                int color;
                if (diamondPattern < 0.25f) {
                    float local = diamondPattern / 0.25f;
                    color = mixColor(lightColor, mediumColor, local);
                } else if (diamondPattern < 0.5f) {
                    float local = (diamondPattern - 0.25f) / 0.25f;
                    color = mixColor(mediumColor, darkColor, local);
                } else {
                    color = darkColor;
                }

                if ((i + j) % 3 == 0) {
                    color = lighten(color, 0.2f);
                }

                guiGraphics.fill(startX + j * dirX, currentY,
                        startX + (j + 1) * dirX, currentY + dirY, color);
            }
        }

        long time = System.currentTimeMillis();
        float pulse = (float)(Math.sin(time / 800.0) * 0.3 + 0.7);

        for (int i = 0; i < 5; i++) {
            int px = x + ((int)(Math.random() * 8) - 4) * dirX;
            int py = y + ((int)(Math.random() * 8) - 4) * dirY;
            int alpha = (int)(0x50 * pulse);
            guiGraphics.fill(px, py, px + dirX, py + dirY, (alpha << 24) | 0xFFFFFF);
        }

        int frameX = x - (gemSize/2 + 3) * dirX;
        int frameY = y - (gemSize/2 + 3) * dirY;
        int frameSize = gemSize + 6;

        for (int layer = 0; layer < 3; layer++) {
            int frameColor = layer == 0 ? 0xFF8B6914 : (layer == 1 ? 0xFFD4A017 : 0xFFFFD700);

            for (int i = 0; i < frameSize; i++) {
                guiGraphics.fill(frameX + layer * dirX, frameY + i * dirY,
                        frameX + (layer + 1) * dirX, frameY + (i + 1) * dirY, frameColor);
                guiGraphics.fill(frameX + i * dirX, frameY + layer * dirY,
                        frameX + (i + 1) * dirX, frameY + (layer + 1) * dirY, frameColor);
                guiGraphics.fill(frameX + (frameSize - 1 - layer) * dirX, frameY + i * dirY,
                        frameX + (frameSize - layer) * dirX, frameY + (i + 1) * dirY, frameColor);
                guiGraphics.fill(frameX + i * dirX, frameY + (frameSize - 1 - layer) * dirY,
                        frameX + (i + 1) * dirX, frameY + (frameSize - layer) * dirY, frameColor);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int cornerX = frameX + (frameSize - 1) * i * dirX;
                int cornerY = frameY + (frameSize - 1) * j * dirY;
                guiGraphics.fill(cornerX, cornerY, cornerX + dirX, cornerY + dirY, 0xFFFFF98A);
            }
        }
    }

    private int lighten(int color, float amount) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = Math.min(255, (int)(r + (255 - r) * amount));
        g = Math.min(255, (int)(g + (255 - g) * amount));
        b = Math.min(255, (int)(b + (255 - b) * amount));

        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    private int darken(int color, float amount) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = (int)(r * (1 - amount));
        g = (int)(g * (1 - amount));
        b = (int)(b * (1 - amount));

        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    private int mixColor(int color1, int color2, float progress) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = (int)(r1 * (1 - progress) + r2 * progress);
        int g = (int)(g1 * (1 - progress) + g2 * progress);
        int b = (int)(b1 * (1 - progress) + b2 * progress);

        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    private void renderNpcText(GuiGraphics guiGraphics) {
        DialogNode currentNode = dialog.getCurrentNode();
        if (currentNode != null) {
            int textX = dialogX + dialogWidth / 2 + 10;
            int textY = dialogY + 40;
            int textWidth = dialogWidth / 2 - 30;

            Component displayText = currentNode.getNpcText();

            var textRenderer = this.font;
            var lines = textRenderer.split(displayText, textWidth);

            for (int i = 0; i < lines.size(); i++) {
                guiGraphics.drawString(textRenderer, lines.get(i), textX, textY + i * (textRenderer.lineHeight + 2),
                        0xFFFFFF, false);
            }
        }
    }

    private void renderEntityName(GuiGraphics guiGraphics) {
        Component name = entity.getDisplayName();
        int nameWidth = this.font.width(name);
        int nameX = dialogX + (dialogWidth - nameWidth) / 2;
        int nameY = dialogY + 10;

        guiGraphics.drawString(this.font, name, nameX, nameY, 0xFFFFFF, true);
    }

    private void renderEntityModel(GuiGraphics guiGraphics, float partialTick) {
        int modelX = dialogX + dialogWidth - 140;
        int modelY = dialogY + dialogHeight - 100;

        try {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            poseStack.translate(modelX + 70, modelY + 100, 150);
            poseStack.scale(40.0F, 40.0F, 40.0F);

            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180));
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(210f));
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-10f));

            Lighting.setupForEntityInInventory();

            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

            dispatcher.render(entity, 0, 0, 0, 0, partialTick, poseStack, bufferSource,
                    LightTexture.FULL_BRIGHT);

            bufferSource.endBatch();
            Lighting.setupFor3DItems();
            poseStack.popPose();

        } catch (Exception e) {
            int fallbackWidth = 120;
            int fallbackHeight = 100;

            guiGraphics.fill(modelX, modelY, modelX + fallbackWidth, modelY + fallbackHeight, 0x80000000);
            guiGraphics.fill(modelX - 1, modelY - 1, modelX + fallbackWidth + 1, modelY + fallbackHeight + 1, 0xFFFFFFFF);

            guiGraphics.drawString(this.font, "🦌", modelX + fallbackWidth/2 - 4, modelY + 30, 0xFFFFFF, false);
            guiGraphics.drawCenteredString(this.font, entity.getDisplayName(),
                    modelX + fallbackWidth/2, modelY + 60, 0xFFFFFF);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 69) {
            this.minecraft.setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private class DialogButton extends net.minecraft.client.gui.components.Button {
        private final int buttonIndex;

        public DialogButton(int x, int y, int width, int height, Component message, int index, OnPress onPress) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
            this.buttonIndex = index;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            boolean isSelected = this.buttonIndex == selectedButtonIndex;
            boolean isHovered = this.isHovered();

            int backgroundColor = 0xFF220000;
            int bordeauxBorder = isSelected ? 0xFFF57A16 : 0xFF780D13;
            int goldAccent = isSelected ? 0xFFFFC940 : 0xFFE04C02;
            int textColor = isSelected ? 0xFFFFF98A : 0xFFFFFF;

            guiGraphics.fill(this.getX(), this.getY(),
                    this.getX() + this.width, this.getY() + this.height, backgroundColor);

            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, bordeauxBorder);
            guiGraphics.fill(this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, bordeauxBorder);
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, bordeauxBorder);
            guiGraphics.fill(this.getX() + this.width - 1, this.getY(), this.getX() + this.width, this.getY() + this.height, bordeauxBorder);

            if (isSelected || isHovered) {
                int innerOffset = 2;

                for (int x = this.getX() + innerOffset; x < this.getX() + this.width - innerOffset; x++) {
                    float progress = (float)(x - (this.getX() + innerOffset)) / (this.width - 2 * innerOffset);
                    int color = calculateGoldGradient(progress);
                    guiGraphics.fill(x, this.getY() + innerOffset, x + 1, this.getY() + innerOffset + 1, color);
                }

                for (int x = this.getX() + innerOffset; x < this.getX() + this.width - innerOffset; x++) {
                    float progress = (float)(x - (this.getX() + innerOffset)) / (this.width - 2 * innerOffset);
                    int color = calculateGoldGradient(progress);
                    guiGraphics.fill(x, this.getY() + this.height - innerOffset - 1,
                            x + 1, this.getY() + this.height - innerOffset, color);
                }

                for (int y = this.getY() + innerOffset; y < this.getY() + this.height - innerOffset; y++) {
                    float progress = (float)(y - (this.getY() + innerOffset)) / (this.height - 2 * innerOffset);
                    int color = calculateGoldGradient(progress);
                    guiGraphics.fill(this.getX() + innerOffset, y,
                            this.getX() + innerOffset + 1, y + 1, color);
                }

                for (int y = this.getY() + innerOffset; y < this.getY() + this.height - innerOffset; y++) {
                    float progress = (float)(y - (this.getY() + innerOffset)) / (this.height - 2 * innerOffset);
                    int color = calculateGoldGradient(progress);
                    guiGraphics.fill(this.getX() + this.width - innerOffset - 1, y,
                            this.getX() + this.width - innerOffset, y + 1, color);
                }
            }

            int cornerLength = 3;

            for (int i = 0; i < cornerLength; i++) {
                guiGraphics.fill(this.getX() + i, this.getY(), this.getX() + i + 1, this.getY() + 1, goldAccent);
                guiGraphics.fill(this.getX(), this.getY() + i, this.getX() + 1, this.getY() + i + 1, goldAccent);
            }

            for (int i = 0; i < cornerLength; i++) {
                guiGraphics.fill(this.getX() + this.width - 1 - i, this.getY(),
                        this.getX() + this.width - i, this.getY() + 1, goldAccent);
                guiGraphics.fill(this.getX() + this.width - 1, this.getY() + i,
                        this.getX() + this.width, this.getY() + i + 1, goldAccent);
            }

            for (int i = 0; i < cornerLength; i++) {
                guiGraphics.fill(this.getX() + i, this.getY() + this.height - 1,
                        this.getX() + i + 1, this.getY() + this.height, goldAccent);
                guiGraphics.fill(this.getX(), this.getY() + this.height - 1 - i,
                        this.getX() + 1, this.getY() + this.height - i, goldAccent);
            }

            for (int i = 0; i < cornerLength; i++) {
                guiGraphics.fill(this.getX() + this.width - 1 - i, this.getY() + this.height - 1,
                        this.getX() + this.width - i, this.getY() + this.height, goldAccent);
                guiGraphics.fill(this.getX() + this.width - 1, this.getY() + this.height - 1 - i,
                        this.getX() + this.width, this.getY() + this.height - i, goldAccent);
            }

            if (isSelected) {
                guiGraphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + 2, this.getY() + 2, 0xFFFFF98A);
                guiGraphics.fill(this.getX() + this.width - 2, this.getY() + 1,
                        this.getX() + this.width - 1, this.getY() + 2, 0xFFFFF98A);
                guiGraphics.fill(this.getX() + 1, this.getY() + this.height - 2,
                        this.getX() + 2, this.getY() + this.height - 1, 0xFFFFF98A);
                guiGraphics.fill(this.getX() + this.width - 2, this.getY() + this.height - 2,
                        this.getX() + this.width - 1, this.getY() + this.height - 1, 0xFFFFF98A);
            }

            int textWidth = DialogScreen.this.font.width(this.getMessage());
            int availableWidth = this.width - 10;

            if (textWidth <= availableWidth) {
                guiGraphics.drawCenteredString(DialogScreen.this.font, this.getMessage(),
                        this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2,
                        textColor);
            } else {
                var lines = DialogScreen.this.font.split(this.getMessage(), availableWidth);
                if (lines.size() == 1) {
                    guiGraphics.drawCenteredString(DialogScreen.this.font, this.getMessage(),
                            this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2,
                            textColor);
                } else {
                    int lineHeight = DialogScreen.this.font.lineHeight;
                    int totalHeight = lines.size() * (lineHeight + 1);
                    int startY = this.getY() + (this.height - totalHeight) / 2;

                    for (int i = 0; i < lines.size(); i++) {
                        guiGraphics.drawCenteredString(DialogScreen.this.font, lines.get(i),
                                this.getX() + this.width / 2, startY + i * (lineHeight + 1),
                                textColor);
                    }
                }
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if (dialog != null) {
            dialog.reset();
        }
        selectedButtonIndex = -1;
        resetQuestButtons();
        resetQuestCreationCheck();
        questCreationChecked = false;
    }

    @Override
    public void removed() {
        super.removed();
        if (dialog != null) {
            dialog.reset();
        }
        selectedButtonIndex = -1;
        resetQuestButtons();
        resetQuestCreationCheck();
        questCreationChecked = false;
    }
}