package org.ruoland.dictionary.dictionary.gui;



import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.ItemManager;
import org.ruoland.dictionary.dictionary.dictionary.LangManager;
import org.ruoland.dictionary.dictionary.dictionary.TagManager;
import org.ruoland.dictionary.dictionary.dictionary.VariableManager;
import org.ruoland.dictionary.dictionary.dictionary.entitycontent.EntityTag;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemGroupContent;


import java.util.ArrayList;
import java.util.List;

public class ContentScreen extends DebugScreen {

    private final FormattedText itemName, itemEngName;
    private final ItemStack itemStack;
    private List<Object> contentComponents;

    private boolean onlyGroup;
    private int contentWidth = 200; // 콘텐츠의 최대 너비
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
        this.contentComponents = new ArrayList<>();

    }

    protected void init() {
        super.init();
        try {
            String content = ItemManager.getContent(itemStack).replace("\\n", "\n");
            content = VariableManager.replaceVariable(itemStack, content);
            parseContent(content);
        } catch (NullPointerException e) {
            e.printStackTrace();
            contentComponents.add(Component.literal("이 도감에는 잘못된 변수가 존재합니다. 변수를 제대로 입력했는지 확인해주세요.: " + e.getMessage()));
        }
        for (Object component : contentComponents) {
            if (component instanceof DictionaryPlainTextButton button) {
                this.addRenderableWidget(button);
            }
        }
    }

    private void parseContent(String content) {
        contentComponents = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        int index = 0;
        while (index < content.length()) {
            if (content.startsWith("%id:", index) || content.startsWith("%group_id:", index)) {
                if (!currentText.isEmpty()) {
                    addTextComponent(currentText.toString());
                    currentText = new StringBuilder();
                }
                int endIndex = content.indexOf('%', index + 1);
                if (endIndex != -1) {
                    String id = content.substring(index, endIndex + 1);
                    contentComponents.add(createPlainTextButton(id));
                    index = endIndex + 1;
                } else {
                    currentText.append(content.substring(index));
                    break;
                }
            } else if (content.charAt(index) == '\n') {
                if (!currentText.isEmpty()) {
                    addTextComponent(currentText.toString());
                    currentText = new StringBuilder();
                }
                contentComponents.add("\n");
                index++;
            } else {
                currentText.append(content.charAt(index));
                index++;
            }
        }
        if (!currentText.isEmpty()) {
            addTextComponent(currentText.toString());
        }
    }

    private void addTextComponent(String text) {
        String[] lines = text.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].isEmpty()) {
                contentComponents.add(Component.literal(lines[i]));
            }
            if (i < lines.length - 1) {
                contentComponents.add("\n");
            }
        }
    }

    private DictionaryPlainTextButton createPlainTextButton(String id) {
        String itemId = VariableManager.cutVarId(id, false, id.startsWith("%group_id:"));
        Component buttonText;

        if (id.startsWith("%group_id:")) {
            ItemGroupContent groupContent = TagManager.getTagManager().findGroupByItemID(itemId);
            buttonText = Component.literal(groupContent.getGroupName());
        } else if(id.startsWith("%id:item.") || id.startsWith("%id:block.")){
            ItemContent itemContent = TagManager.getTagManager().findItemByID(itemId);
            buttonText = itemContent.getItemStack().getHoverName();
        } else if(id.startsWith("%id:entity.")){
            String entityName = EntityTag.getEntityNameById(itemId);
            buttonText = Component.literal(entityName);
        } else if(id.startsWith("%id:biome.")){
            String biomeName = LangManager.getBiomeNameKor(itemId);
            buttonText = Component.literal(biomeName);
        } else {
            buttonText = Component.literal("인식할 수 없는 문자열:"+itemId);
        }

        return new DictionaryPlainTextButton(0, 0, font.width(buttonText), font.lineHeight, buttonText,
                (button) -> {
                    Dictionary.LOGGER.info("Button clicked: {}", id);
                    Screen nextScreen = null;
                    if (id.startsWith("%group_id:")) {
                        ItemGroupContent groupContent = TagManager.getTagManager().findGroupByItemID(itemId);
                        nextScreen = new GroupContentScreen(this, groupContent);
                    } else if (id.startsWith("%id:item.") || id.startsWith("%id:block.")) {
                        ItemContent itemContent = TagManager.getTagManager().findItemByID(itemId);
                        nextScreen = new ContentScreen(this, itemContent.getItemStack(), false);
                    } else if (id.startsWith("%id:entity.")) {
                        Dictionary.LOGGER.info("Entity dictionary not implemented yet");
                        minecraft.gui.setOverlayMessage(Component.literal("엔티티 도감은 아직 구현되지 않았습니다."), false);
                        return;
                    } else if (id.startsWith("%id:biome.")) {
                        Dictionary.LOGGER.info("Biome dictionary not implemented yet");
                        minecraft.gui.setOverlayMessage(Component.literal("바이옴 도감은 아직 구현되지 않았습니다."), false);
                        return;
                    }

                    if (nextScreen != null) {
                        Dictionary.LOGGER.info("Setting next screen: {}", nextScreen.getClass().getSimpleName());
                        minecraft.setScreen(nextScreen);
                    } else {
                        Dictionary.LOGGER.warn("Next screen is null for id: {}", id);
                    }
                }, font);
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        renderTitle(pGuiGraphics);
        int y = renderSubtitle(pGuiGraphics);
        renderContent(pGuiGraphics, pMouseX, pMouseY, pPartialTick, y);
        renderItem(pGuiGraphics, pPartialTick);
    }

    private void renderTitle(GuiGraphics pGuiGraphics) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.3F, 1.3F, 1);
        drawText(3, pGuiGraphics, itemName, guiLeft + itemInfoName, guiTop + 5, width, 0);
        pGuiGraphics.pose().popPose();
    }

    private int renderSubtitle(GuiGraphics pGuiGraphics) {
        int engLineY = 0;
        if (font.width(itemName) > 100) {
            engLineY += 10;
        }

        if (!onlyGroup) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(1.0F, 1.0F, 1);
            drawText(2, pGuiGraphics, itemEngName, guiLeft + itemInfoX, guiTop + 25 + engLineY, width, 0);
            pGuiGraphics.pose().popPose();

            if (font.width(itemEngName) > 100) {
                engLineY += 10;
            }
        }
        return engLineY;
    }

    private void renderContent(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, int engLineY) {
        int x = guiLeft + itemInfoX;
        int y = guiTop + 35 + engLineY;
        int lineHeight = font.lineHeight + 2;

        List<TextPart> textParts = createTextParts();
        renderTextParts(pGuiGraphics, textParts, x, y, lineHeight, pMouseX, pMouseY, pPartialTick);
    }

    private List<TextPart> createTextParts() {
        List<TextPart> textParts = new ArrayList<>();
        for (Object component : contentComponents) {
            if (component instanceof Component textComponent) {
                textParts.add(new TextPart(textComponent.getString(), false, null));
            } else if (component instanceof DictionaryPlainTextButton button) {
                textParts.add(new TextPart(button.getMessage().getString(), true, button));
            } else if (component.equals("\n")) {
                textParts.add(new TextPart("\n", false, null));
            }
        }
        return textParts;
    }

    private void renderTextParts(GuiGraphics pGuiGraphics, List<TextPart> textParts, int x, int y, int lineHeight, int pMouseX, int pMouseY, float pPartialTick) {
        StringBuilder currentLine = new StringBuilder();
        List<TextPart> currentLineParts = new ArrayList<>();

        for (TextPart part : textParts) {
            if (part.text.equals("\n") || font.width(currentLine.toString() + part.text) > contentWidth) {
                renderLine(pGuiGraphics, currentLine.toString(), currentLineParts, x, y, pMouseX, pMouseY, pPartialTick);
                y += lineHeight;
                currentLine = new StringBuilder();
                currentLineParts = new ArrayList<>();
                if (!part.text.equals("\n")) {
                    currentLine.append(part.text);
                    currentLineParts.add(part);
                }
            } else {
                currentLine.append(part.text);
                currentLineParts.add(part);
            }
        }

        if (currentLine.length() > 0) {
            renderLine(pGuiGraphics, currentLine.toString(), currentLineParts, x, y, pMouseX, pMouseY, pPartialTick);
        }
    }

    private void renderLine(GuiGraphics pGuiGraphics, String lineText, List<TextPart> lineParts, int x, int y, int pMouseX, int pMouseY, float pPartialTick) {
        int currentX = x;
        for (TextPart part : lineParts) {
            if (part.isButton) {
                DictionaryPlainTextButton button = part.button;
                button.setX(currentX);
                button.setY(y);
                button.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                currentX += font.width(button.getMessage());
            } else {
                pGuiGraphics.drawString(font, part.text, currentX, y, 0xFFFFFF);
                currentX += font.width(part.text);
            }
        }
    }

    private void renderItem(GuiGraphics pGuiGraphics, float pPartialTick) {
        int itemRenderPositionX = 75;
        renderItem(pGuiGraphics, guiLeft + itemInfoName - itemRenderPositionX, guiTop - 2, 3.5F, itemStack, pPartialTick);
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

    private static class TextPart {
        String text;
        boolean isButton;
        DictionaryPlainTextButton button;

        TextPart(String text, boolean isButton, DictionaryPlainTextButton button) {
            this.text = text;
            this.isButton = isButton;
            this.button = button;
        }
    }

}
