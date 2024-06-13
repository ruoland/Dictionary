package org.ruoland.dictionary.dictionary.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.ItemManager;
import org.ruoland.dictionary.dictionary.dictionary.LangManager;
import org.ruoland.dictionary.dictionary.dictionary.TagManager;
import org.ruoland.dictionary.dictionary.dictionary.VariableManager;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.SubData;

import java.util.ArrayList;

public class ContentScreen extends DebugScreen {
    private static final String NEW_LINE = "-NewNewNewLine-";
    private final FormattedText itemName, itemEngName;
    private final ItemStack itemStack;

    private Component[] dictionarySplit = new Component[10];
    private boolean onlyGroup;

    int itemInfoName = 90;
    int itemInfoX = itemInfoName + 30;
    int width = 300;

    public ContentScreen(Screen lastScreen, ItemStack itemStack, boolean onlyGroup) {
        super(Component.literal("도감"));
        String groupName = TagManager.getTagManager().getItemGroup(itemStack).getGroupName();

        if(onlyGroup)
            itemName = FormattedText.of(groupName);
        else
            itemName = FormattedText.of(groupName+":"+itemStack.getDisplayName().getString());
        itemEngName = FormattedText.of(LangManager.getEnglishName(itemStack));

        this.itemStack = itemStack;
        this.onlyGroup = onlyGroup;
        this.lastScreen = lastScreen;


    }

    @Override
    protected void init() {
        super.init();
        try {
            String content = (ItemManager.getContent(itemStack).replace("\\n", NEW_LINE));
            content = VariableManager.replaceVariable(itemStack, content);
            addComments(content);
            content = VariableManager.replace(content);
            contentSplitLines(content);

        }catch (NullPointerException e){
            e.printStackTrace();
            dictionarySplit[0] = Component.literal("이 도감에는 잘못된 변수가 존재합니다. 변수를 제대로 입력했는지 확인해주세요.:" +e.getMessage());
        }
    }

    public void contentSplitLines(String content){
        try {
            String[] contentSplit =content.split(NEW_LINE);
            dictionarySplit = new Component[contentSplit.length];
            for(int i = 0; i < contentSplit.length;i++){
                dictionarySplit[i] = Component.translatable(contentSplit[i]);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            dictionarySplit[0] = Component.literal("데이터를 불러오는 중에 어떤 오류가 발생했습니다.  도감 모드에서 이 아이템을 인식을 못 한 것처럼 같습니다.");
        }
        catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            dictionarySplit[0] = Component.literal("이 도감 내용에는 줄바꿈이 너무 많아 제대로 표현할 수 없습니다. '\\n' 의 개수를 줄여주세요.");
        }
    }

    public void addComments(String dictionary){
        int groupX = 0;
        for(ItemGroupContent groupContent : VariableManager.getGroupFromId(dictionary)){
            String groupName = groupContent.getGroupName();
            addRenderableWidget(new PlainTextButton(itemInfoX+ groupX, guiTop + 200, font.width(groupName), 10, Component.literal(groupName).append(", ").withColor(0), button -> {
                minecraft.setScreen(new ContentScreen(minecraft.screen, groupContent.getZeroItem(), true));
            }, font));
            groupX += font.width(groupContent.getGroupName()) + 5;
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.3F,1.3F,1);
        drawText(3, pGuiGraphics, itemName, guiLeft+itemInfoName , guiTop+5, width, 0);
        pGuiGraphics.pose().popPose();

        
        int engLineY = 0;
        if(font.width(itemName) > 100) //한국어 이름이 두줄인 경우
            engLineY += 10;//영어 이름 Y를 더 아래로 내린다

        if(!onlyGroup) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(1.0F, 1.0F, 1);
            drawText(2, pGuiGraphics, itemEngName, guiLeft + itemInfoX, guiTop + 25 + engLineY, width, 0);
            pGuiGraphics.pose().popPose();

            if (font.width(itemEngName) > 100)
                engLineY += 10;
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1F,1F,1);
        int newLine = 0;
        for(int i = 0; i < dictionarySplit.length;i++) {
            Component dictionary = dictionarySplit[i];

            for (FormattedText formattedcharsequence : font.getSplitter().splitLines(dictionary, 240, Style.EMPTY)) {
                drawText(1, pGuiGraphics, formattedcharsequence, guiLeft+itemInfoX, guiTop + 35 +(engLineY + newLine), width, 0);
                newLine+=10;
            }
            newLine += 25;
        }
        pGuiGraphics.pose().popPose();
        int itemRenderPostionX = 75;
        renderItem(pGuiGraphics, guiLeft + itemInfoName - itemRenderPostionX, guiTop-2, 3.5F, (itemStack), pPartialTick);

    }


    @Override
    public void tick() {
        super.tick();

    }

    public void renderItem(GuiGraphics pGuiGraphics, int resultX, int resultY, float scale, ItemStack item, float pPartialTick){
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale,scale,scale);
        pGuiGraphics.renderItem(item, resultX, resultY);
        pGuiGraphics.pose().popPose();
    }
    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderTransparentBackground(pGuiGraphics);
        float scaleX = 2.3F;

        int left=-5, top = -5;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scaleX,1.3F,1);
        pGuiGraphics.blit(BookViewScreen.BOOK_LOCATION, guiLeft + left, guiTop + top, 0, 0, 192, 192);
        pGuiGraphics.pose().popPose();
    }


}
