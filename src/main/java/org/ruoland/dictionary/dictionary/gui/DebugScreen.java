package org.ruoland.dictionary.dictionary.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public class DebugScreen extends Screen {
    protected int xSize, ySize, guiLeft, guiTop;
    int mouseDefaultX = -65, mouseDefaultY = 5;
    int mouseX, mouseY;
    int plusX, plusY;
    int prevMouseX, prevMouseY;
    private int selectID;
    protected DebugScreen(Component pTitle) {
        super(pTitle);
    }
    Screen lastScreen;
    @Override
    protected void init() {
        super.init();

        xSize=width-10;
        ySize=height-10;
        guiLeft=(width-xSize)/2;
        guiTop=(height-ySize)/2;

    }


    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T guiEventListener) {
        return super.addRenderableWidget(guiEventListener);
    }
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if(selectID > 0) {
            this.mouseX = pMouseX + mouseDefaultX;
            this.mouseY = pMouseY + mouseDefaultY;
            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }
        else {
            this.mouseX = 0;
            this.mouseY = 0;
        }
        drawText(-1, pGuiGraphics, "X:"+mouseX +",   Y:"+mouseY, 0,0,100, 0xFFFFFF);


    }

    protected void drawText(int id, GuiGraphics pGuiGraphics, String text, int x, int y, int width, int color){
       drawText(id, pGuiGraphics, FormattedText.of(text), x,y,width,color);
    }

    protected void drawText(int id, GuiGraphics pGuiGraphics, FormattedText text, int x, int y, int width, int color){
        int posX = x + (selectID == id ? mouseX : 0) ;
        int posY = y + (selectID == id ? mouseY : 0);

        if(selectID == 0 && (prevMouseX != 0 && prevMouseY != 0)){
            posX += prevMouseX;
            posY += prevMouseY;
        }

        pGuiGraphics.drawWordWrap(this.font, (text), posX, posY, width, color);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        switch (pKeyCode){
            case InputConstants.KEY_0 -> selectID = 0;
            case InputConstants.KEY_1 -> selectID = 1;
            case InputConstants.KEY_2 -> selectID = 2;
            case InputConstants.KEY_3 -> selectID = 3;
            case InputConstants.KEY_4 -> selectID = 4;
            case InputConstants.KEY_5 -> selectID = 5;
            case InputConstants.KEY_6 -> selectID = 6;
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    public int getMouseX(){
        return prevMouseX;
    }
    public int getMouseY(){
        return prevMouseX;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    @Override
    public void onClose() {
        this.minecraft.setScreen(lastScreen);

    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
