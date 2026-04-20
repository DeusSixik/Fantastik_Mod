package net.lisalaf.fantastikmod.dialog.quest;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class QuestTabButton extends Button {
    private boolean active;
    private final int tabIndex;

    public QuestTabButton(int x, int y, int width, int height,
                          int tabIndex, boolean active,
                          OnPress onPress) {
        super(x, y, width, height, Component.literal(""), onPress, DEFAULT_NARRATION);
        this.active = active;
        this.tabIndex = tabIndex;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int bgColor = active ? 0xFF3A2618 : 0xFF1A0D0A;
        int borderColor = active ? 0xFFD4AF37 : 0xFF780D13;
        int textColor = active ? 0xFFFFF98A : 0xAAAAAA;

        guiGraphics.fill(this.getX(), this.getY(),
                this.getX() + this.width, this.getY() + this.height, bgColor);

        guiGraphics.fill(this.getX(), this.getY(),
                this.getX() + this.width, this.getY() + 1, borderColor);
        guiGraphics.fill(this.getX(), this.getY() + this.height - 1,
                this.getX() + this.width, this.getY() + this.height, borderColor);
        guiGraphics.fill(this.getX(), this.getY(),
                this.getX() + 1, this.getY() + this.height, borderColor);
        guiGraphics.fill(this.getX() + this.width - 1, this.getY(),
                this.getX() + this.width, this.getY() + this.height, borderColor);

        var minecraft = net.minecraft.client.Minecraft.getInstance();

        guiGraphics.drawCenteredString(minecraft.font, "!",
                this.getX() + this.width / 2,
                this.getY() + (this.height - minecraft.font.lineHeight) / 2,
                textColor);
    }
}