package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.ruoland.dictionary.Dictionary;

import java.util.List;

public class DictionaryPlainTextButton extends Button {
    private final Font font;



    public DictionaryPlainTextButton(int x, int y, int width, int height, Component message, OnPress onPress, Font font) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.font = font;

    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int textColor = isHovered() ? 0xFFFFFF : 0xAAAAAA;

        List<FormattedCharSequence> lines = font.split(getMessage(), getWidth());
        int yOffset = 0;
        for (FormattedCharSequence line : lines) {
            // Render shadow
            //guiGraphics.drawString(font, line, getX() + 1, getY() + yOffset + 1, 0x55000000);

            // Render main text
            guiGraphics.drawString(font, line, getX(), getY() + yOffset, textColor);

            yOffset += font.lineHeight;
        }

        if (isHovered()) {
            // Draw a thin line under the text when hovered
            guiGraphics.fill(getX(), getY() + yOffset, getX() + getWidth(), getY() + yOffset + 1, 0xFFFFFFFF);
        }
    }

    private void renderText(GuiGraphics guiGraphics, Component text, int xOffset, int yOffset, int color) {
        guiGraphics.drawString(font, text, getX() + xOffset, getY() + yOffset, color);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        Dictionary.LOGGER.info("PlainTextButton clicked: {}", getMessage().getString());
        super.onClick(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.clicked(mouseX, mouseY)) {
            Dictionary.LOGGER.info("PlainTextButton mouseClicked: {}", getMessage().getString());
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }
}