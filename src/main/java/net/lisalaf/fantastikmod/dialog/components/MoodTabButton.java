package net.lisalaf.fantastikmod.dialog.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class MoodTabButton extends Button {
    private boolean active;
    private final int tabIndex;
    private final String emoji;

    public MoodTabButton(int x, int y, int width, int height,
                         String emoji, int tabIndex, boolean active,
                         OnPress onPress) {
        super(x, y, width, height, Component.literal(""), onPress, DEFAULT_NARRATION);
        this.active = active;
        this.tabIndex = tabIndex;
        this.emoji = emoji;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int bgColor = active ? 0xFF3A2618 : 0xFF1A0D0A;
        int borderColor = active ? 0xFFD4AF37 : 0xFF780D13;
        int textColor = active ? 0xFFFFF98A : 0xAAAAAA;

        guiGraphics.fill(this.getX(), this.getY(),
                this.getX() + this.width, this.getY() + this.height, bgColor);

        for (int i = 0; i < 2; i++) {
            int yPos = active ? this.getY() + i : this.getY();
            int yEnd = active ? this.getY() + i + 1 : this.getY() + 1;

            guiGraphics.fill(this.getX(), yPos, this.getX() + this.width, yEnd, borderColor);

            guiGraphics.fill(this.getX(), this.getY() + this.height - i - 1,
                    this.getX() + this.width, this.getY() + this.height - i, borderColor);
        }


        for (int i = 0; i < 2; i++) {
            int xPos = active ? this.getX() + i : this.getX();
            int xEnd = active ? this.getX() + i + 1 : this.getX() + 1;

            guiGraphics.fill(xPos, this.getY(), xEnd, this.getY() + this.height, borderColor);
            guiGraphics.fill(this.getX() + this.width - i - 1, this.getY(),
                    this.getX() + this.width - i, this.getY() + this.height, borderColor);
        }

        int cornerLength = 3;
        int goldColor = 0xFFD4AF37;
        int accentColor = 0xFFFFF98A;

        for (int i = 0; i < cornerLength; i++) {
            guiGraphics.fill(this.getX() + i, this.getY(),
                    this.getX() + i + 1, this.getY() + 1, goldColor);
            guiGraphics.fill(this.getX(), this.getY() + i,
                    this.getX() + 1, this.getY() + i + 1, goldColor);
        }

        for (int i = 0; i < cornerLength; i++) {
            guiGraphics.fill(this.getX() + this.width - 1 - i, this.getY(),
                    this.getX() + this.width - i, this.getY() + 1, goldColor);
            guiGraphics.fill(this.getX() + this.width - 1, this.getY() + i,
                    this.getX() + this.width, this.getY() + i + 1, goldColor);
        }

        for (int i = 0; i < cornerLength; i++) {
            guiGraphics.fill(this.getX() + i, this.getY() + this.height - 1,
                    this.getX() + i + 1, this.getY() + this.height, goldColor);
            guiGraphics.fill(this.getX(), this.getY() + this.height - 1 - i,
                    this.getX() + 1, this.getY() + this.height - i, goldColor);
        }

        for (int i = 0; i < cornerLength; i++) {
            guiGraphics.fill(this.getX() + this.width - 1 - i, this.getY() + this.height - 1,
                    this.getX() + this.width - i, this.getY() + this.height, goldColor);
            guiGraphics.fill(this.getX() + this.width - 1, this.getY() + this.height - 1 - i,
                    this.getX() + this.width, this.getY() + this.height - i, goldColor);
        }

        if (active) {
            guiGraphics.fill(this.getX() + 1, this.getY() + 1,
                    this.getX() + 2, this.getY() + 2, accentColor);
            guiGraphics.fill(this.getX() + this.width - 2, this.getY() + 1,
                    this.getX() + this.width - 1, this.getY() + 2, accentColor);
            guiGraphics.fill(this.getX() + 1, this.getY() + this.height - 2,
                    this.getX() + 2, this.getY() + this.height - 1, accentColor);
            guiGraphics.fill(this.getX() + this.width - 2, this.getY() + this.height - 2,
                    this.getX() + this.width - 1, this.getY() + this.height - 1, accentColor);
        }

        if (this.isHovered()) {
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
        }

        var minecraft = net.minecraft.client.Minecraft.getInstance();

        guiGraphics.drawCenteredString(minecraft.font, emoji,
                this.getX() + this.width / 2,
                this.getY() + (this.height - minecraft.font.lineHeight) / 2,
                textColor);
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
}