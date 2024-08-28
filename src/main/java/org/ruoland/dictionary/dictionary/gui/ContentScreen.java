package org.ruoland.dictionary.dictionary.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.DictionaryLogger;
import org.ruoland.dictionary.dictionary.dictionary.ItemManager;
import org.ruoland.dictionary.dictionary.dictionary.LangManager;
import org.ruoland.dictionary.dictionary.dictionary.TagManager;
import org.ruoland.dictionary.dictionary.dictionary.VariableManager;
import org.ruoland.dictionary.dictionary.dictionary.entity.EntityTag;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemGroupContent;

import java.util.ArrayList;
import java.util.List;

public class ContentScreen extends DebugScreen {
    DictionaryLogger LOGGER = Dictionary.LOGGER;
    private final FormattedText itemName, itemEngName;
    private final ItemStack itemStack;
    private List<Object> contentComponents;

    private final boolean onlyGroup;
    private static final float CONTENT_WIDTH_RATIO = 0.8f; // 화면 너비의 60%
    private static final int LINE_SPACING = 2; //줄 간 간격
    private int contentWidth; //실제 콘텐츠 너비
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

        updateContentWidth();
        Dictionary.LOGGER.info("ContentScreen initialized. Content width: {}", contentWidth);
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

    private void updateContentWidth() {
        this.contentWidth = (int)(this.width * CONTENT_WIDTH_RATIO);
        LOGGER.debug("Content width updated to: {}", contentWidth);
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
            if(groupContent == null ){
                if(itemId.equals("pressure_plate")) {
                    groupContent = TagManager.getTagManager().findGroupByItemID("plate");

                }
            }
            buttonText = Component.literal(groupContent.getGroupName());

        } else if(id.startsWith("%id:item.") || id.startsWith("%id:block.")){

            ItemContent itemContent = TagManager.getTagManager().findItemByID(itemId);
            Dictionary.LOGGER.info("아이템을 가져옵니다. {}, {}, {}", id, itemId, itemContent);
            buttonText = itemContent.getItemStack().getHoverName();
        } else if(id.startsWith("%id:entity.")){
            String entityName = EntityTag.getEntityNameById(itemId);
            buttonText = Component.literal(entityName);
        } else if(id.startsWith("%id:biome.")){
            String biomeName = LangManager.getBiomeNameKor(itemId);
            buttonText = Component.literal(biomeName);
        } else {
            buttonText = Component.literal("인식할 수 없는 문자열:"+itemId +", "+ id);
            Dictionary.LOGGER.warn("인식할 수 없는 변수가 감지되었습니다. :{}, {}", id, itemId);
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



    private void renderContent(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, int engLineY) {
        LOGGER.debug("Rendering content. Mouse position: ({}, {})", pMouseX, pMouseY);
        int x = guiLeft + itemInfoX;
        int y = guiTop + 35 + engLineY;
        int lineHeight = font.lineHeight + LINE_SPACING;

        List<TextPart> textParts = createTextParts();
        LOGGER.debug("Created {} text parts", textParts.size());
        renderTextParts(pGuiGraphics, textParts, x, y, lineHeight, pMouseX, pMouseY, pPartialTick);
    }


    private List<TextPart> createTextParts() {
        LOGGER.debug("Creating text parts from {} content components", contentComponents.size());
        List<TextPart> textParts = new ArrayList<>();
        for (Object component : contentComponents) {
            if (component instanceof Component textComponent) {
                List<TextPart> splitParts = splitTextComponent(textComponent);
                LOGGER.trace("Split text component into {} parts", splitParts.size());
                textParts.addAll(splitParts);
            } else if (component instanceof DictionaryPlainTextButton button) {
                LOGGER.trace("Added button part: {}", button.getMessage().getString());
                textParts.add(new TextPart(button.getMessage().getString(), true, button));
            } else if (component.equals("\n")) {
                LOGGER.trace("Added newline part");
                textParts.add(new TextPart("\n", false, null));
            }
        }
        LOGGER.debug("Created a total of {} text parts", textParts.size());
        return textParts;
    }

    private List<TextPart> splitTextComponent(Component textComponent) {
        LOGGER.debug("Splitting text component: {}", textComponent.getString());
        List<TextPart> parts = new ArrayList<>();
        String[] words = textComponent.getString().split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (font.width(currentLine + " " + word) > contentWidth) {
                if (!currentLine.isEmpty()) {
                    LOGGER.trace("Created new text part: {}", currentLine.toString().trim());
                    parts.add(new TextPart(currentLine.toString().trim(), false, null));
                    currentLine = new StringBuilder();
                }
            }
            if (!currentLine.isEmpty()) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }

        if (!currentLine.isEmpty()) {
            LOGGER.trace("Created final text part: {}", currentLine.toString().trim());
            parts.add(new TextPart(currentLine.toString().trim(), false, null));
        }

        LOGGER.debug("Split text component into {} parts", parts.size());
        return parts;
    }

    private void renderTextParts(GuiGraphics pGuiGraphics, List<TextPart> textParts, int x, int y, int lineHeight, int pMouseX, int pMouseY, float pPartialTick) {
        LOGGER.debug("Rendering {} text parts", textParts.size());
        int currentX = x;
        int currentY = y;
        int lineWidth = 0;
        List<TextPart> currentLineParts = new ArrayList<>();

        for (TextPart part : textParts) {
            if (part.text.equals("\n")) {
                LOGGER.trace("Rendering line at y={}", currentY);
                renderLine(pGuiGraphics, currentLineParts, x, currentY, pMouseX, pMouseY, pPartialTick);
                currentX = x;
                currentY += lineHeight;
                lineWidth = 0;
                currentLineParts.clear();
                continue;
            }

            int partWidth = part.isButton ? font.width(part.button.getMessage()) : font.width(part.text);

            if (lineWidth + partWidth > contentWidth && !currentLineParts.isEmpty()) {
                LOGGER.trace("Line width exceeded. Rendering line at y={}", currentY);
                renderLine(pGuiGraphics, currentLineParts, x, currentY, pMouseX, pMouseY, pPartialTick);
                currentX = x;
                currentY += lineHeight;
                lineWidth = 0;
                currentLineParts.clear();
            }

            currentLineParts.add(part);
            lineWidth += partWidth;
            LOGGER.trace("Added part to current line. Current width: {}", lineWidth);
        }

        if (!currentLineParts.isEmpty()) {
            LOGGER.trace("Rendering final line at y={}", currentY);
            renderLine(pGuiGraphics, currentLineParts, x, currentY, pMouseX, pMouseY, pPartialTick);
        }
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

    private void renderLine(GuiGraphics pGuiGraphics, List<TextPart> lineParts, int x, int y, int pMouseX, int pMouseY, float pPartialTick) {
        LOGGER.debug("Rendering line with {} parts at y={}", lineParts.size(), y);
        int currentX = x;
        for (TextPart part : lineParts) {
            if (part.isButton) {
                DictionaryPlainTextButton button = part.button;
                button.setX(currentX);
                button.setY(y);
                button.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                currentX += font.width(button.getMessage());
                LOGGER.trace("Rendered button at ({}, {})", currentX, y);
            } else {
                pGuiGraphics.drawString(font, part.text, currentX, y, 0xFFFFFF);
                currentX += font.width(part.text);
                LOGGER.trace("Rendered text '{}' at ({}, {})", part.text, currentX, y);
            }
        }
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        LOGGER.info("Resizing screen to {}x{}", pWidth, pHeight);
        super.resize(pMinecraft, pWidth, pHeight);
        updateContentWidth();
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
