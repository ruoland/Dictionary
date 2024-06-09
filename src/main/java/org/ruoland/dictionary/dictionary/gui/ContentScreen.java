package org.ruoland.dictionary.dictionary.gui;


import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
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
    private final FormattedText itemName;
    private final FormattedText itemEngName;
    private final ArrayList<ItemStack> itemList = new ArrayList<>();
    private final Screen lastScreen;
    int itemInfoName = 90;
    int itemInfoX = itemInfoName + 30;
    int itemRender = 75;
    int width = 300;
    int tick  = 0;
    private Component[] dictionarySplit = new Component[10];
    private int itemIndex;

    public ContentScreen(Screen lastScreen, ItemStack itemStack) {
        super(Component.literal("도감"));
        itemName = FormattedText.of(itemStack.getDisplayName().getString());
        itemEngName = FormattedText.of(LangManager.getEnglishName(itemStack));
        SubData subData = TagManager.getTagManager().getItemTag(itemStack).getSubData();

        for(ItemGroupContent groupContent : subData.getGroupMap().values()){
            for(ItemContent content :groupContent.getContentMap().values())
                itemList.add(ItemManager.getItemStackMap().get(content.getItemID()));
        }
        this.lastScreen = lastScreen;

        try {
            String content = (ItemManager.getContent(itemStack).replace("\\n", NEW_LINE));
            content = VariableManager.getVariable(itemStack, content);

            try {
                String[] contentSplit =content.split(NEW_LINE);
                dictionarySplit = new Component[contentSplit.length];
                for(int i = 0; i < contentSplit.length;i++){
                    dictionarySplit[i] = Component.literal(contentSplit[i]);

                }
            }catch (NullPointerException e){
                e.printStackTrace();
                dictionarySplit[0] = Component.literal("데이터를 불러오는 중에 어떤 오류가 발생했습니다.  도감 모드에서 이 아이템을 인식을 못 한 것처럼 같습니다.");
            }
            catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                dictionarySplit[0] = Component.literal("이 도감 내용에는 줄바꿈이 너무 많아 제대로 표현할 수 없습니다. '\\n' 의 개수를 줄여주세요.");
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            dictionarySplit[0] = Component.literal("이 도감에는 잘못된 변수가 존재합니다. 변수를 제대로 입력했는지 확인해주세요.:" +e.getMessage());
        }

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int engLineY = 0;
        if(font.width(itemName) > 100)
            engLineY += 10;
        //아이템 한국어 이름

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.3F,1.3F,1);
        drawText(3, pGuiGraphics, itemName, guiLeft+itemInfoName , guiTop+5, width, 0);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.0F,1.0F,1);
        drawText(2, pGuiGraphics, itemEngName, guiLeft+itemInfoX , guiTop+25+engLineY, width, 0);
        pGuiGraphics.pose().popPose();

        if(font.width(itemEngName) > 100)
            engLineY += 10;

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

        if(itemList.size() <= itemIndex)
        {
            itemIndex = 0;
        }


        renderItem(pGuiGraphics, guiLeft + itemInfoName - itemRender, guiTop-2, 3.5F, itemList.get(itemIndex), pPartialTick);
    }

    @Override
    public void tick() {
        super.tick();
        tick++;
        if(itemList.size() <= itemIndex)
        {
            itemIndex = 0;
        }
        else if(tick % 30 == 0)
        {
            itemIndex++;
        }

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
        int posX = 200;

        int left=-5, top = -5;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scaleX,1.3F,1);
        pGuiGraphics.blit(BookViewScreen.BOOK_LOCATION, guiLeft + left, guiTop + top, 0, 0, 192, 192);
        pGuiGraphics.pose().popPose();


    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(lastScreen);

    }


}
