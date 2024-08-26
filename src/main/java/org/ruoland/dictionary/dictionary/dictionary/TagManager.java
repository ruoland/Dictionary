package org.ruoland.dictionary.dictionary.dictionary;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.Data;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.LinkedHashMap;

/**
 * 태그를 부여하거나, 태그에 있는 아이템을 모아두거나, 태그를 저장, 불러오기 하는 클래스
 */
public class TagManager {
    //태그 -> 아이템 태그를 가져옴
    private static final EnumMap<EnumTag, ItemsTag> tagEnumMap = new EnumMap<>(EnumTag.class);
    //아이템 아이디 -> EnumTag를 가져오는 것
    private static final LinkedHashMap<String, EnumTag> idToTag = new LinkedHashMap<>();

    //아이템 아이디 -> 해당 아이템이 속한 그룹을 가져오기
    private static final LinkedHashMap<String, ItemGroupContent> idToGroup = new LinkedHashMap<>();
    private static final EnumMap<EnumTag, Long> lastEditedMap = new EnumMap<>(EnumTag.class);


    private static final TagManager TAG_MANAGER;
    static {
        TAG_MANAGER = new TagManager();

    }

    public static TagManager getTagManager() {
        return TAG_MANAGER;
    }

    public static LinkedHashMap<String, EnumTag> getIdToTag() {
        return idToTag;
    }
    public void loadTag() {
        Dictionary.LOGGER.info("Starting to load tags...");
        for (EnumTag tag : EnumTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            if (Files.exists(tagFile)) {
                try {
                    ItemsTag itemsTag = (ItemsTag) Data.readJson(tagFile, ItemsTag.class);
                    tagEnumMap.put(tag, itemsTag);
                    lastEditedMap.put(tag, tagFile.toFile().lastModified());

                    // Log loaded data
                    logLoadedData(itemsTag);
                } catch (Exception e) {
                    Dictionary.LOGGER.error("Error loading tag {}: {}", tag, e.getMessage());
                    e.printStackTrace();
                }
            } else {
                Dictionary.LOGGER.warn("Tag file not found for {}, creating a new one", tag);
                tagEnumMap.put(tag, new ItemsTag(tag));
            }
        }
        Dictionary.LOGGER.info("Finished loading tags. Total tags loaded: {}", tagEnumMap.size());
        tagging();
    }

    private void logLoadedData(ItemsTag itemsTag) {
        SubData subData = itemsTag.getSubData();
        for (ItemGroupContent group : subData.getGroupMap().values()) {
            for (ItemContent item : group.getContentMap().values()) {
                Dictionary.LOGGER.info("Loaded item: {}, description: {}", item.getItemID(), item.getDictionary(true));
            }
        }
    }

    public void saveTag() throws IOException {
        Dictionary.LOGGER.info("Starting to save tags...");
        for (EnumTag tag : EnumTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            boolean newFile = !tagFile.toFile().exists();
            if (newFile) {
                Files.createFile(tagFile);
            }

            ItemsTag itemsTag = tagEnumMap.get(tag);
            if (newFile || tagFile.toFile().lastModified() <= lastEditedMap.getOrDefault(tag, 0L)) {
                // Log data being saved
                logSavedData(itemsTag);

                Data.saveJson(tagFile, itemsTag);
                lastEditedMap.put(tag, System.currentTimeMillis());
                Dictionary.LOGGER.info("Saved tag: {}", tag);
            } else {
                Dictionary.LOGGER.info("Skipped saving tag {} as it hasn't been modified", tag);
            }
        }
        Dictionary.LOGGER.info("Finished saving tags.");
    }

    private void logSavedData(ItemsTag itemsTag) {
        SubData subData = itemsTag.getSubData();
        for (ItemGroupContent group : subData.getGroupMap().values()) {
            for (ItemContent item : group.getContentMap().values()) {
                Dictionary.LOGGER.info("Saving item: {}, description: {}", item.getItemID(), item.getDictionary(true));
            }
        }
    }
    public void tagging() {
        Dictionary.LOGGER.info("Starting tagging process...");
        try {
            for (ItemStack itemStack : ItemManager.getItemList()) {
                ItemsTag itemsTag = getItemTag(getTag(itemStack));
                SubData sub = itemsTag.getSubData();

                if (sub == null) {
                    Dictionary.LOGGER.warn("SubData is null for item: {}", itemStack.getDescriptionId());
                    continue;
                }

                ItemGroupContent group = sub.getItemGroup(itemStack);
                if (group != null) {
                    Dictionary.LOGGER.info("Adding item to existing group: {} for item: {}", group.getGroupName(), itemStack.getDescriptionId());
                    group.add(itemStack);
                } else {
                    Dictionary.LOGGER.info("Adding new item content for item: {}", itemStack.getDescriptionId());
                    sub.addItemContent(itemStack);
                }

                String itemId = getItemCutID(itemStack);
                ItemContent itemContent = group.getItemContent(itemStack);
                if (itemContent != null) {
                    Dictionary.LOGGER.info("Tagging process - itemId: {}, description: {}", itemId, itemContent.getDictionary(true));
                } else {
                    Dictionary.LOGGER.warn("ItemContent is null for item: {}", itemId);
                }
            }
        } catch (Exception e) {
            Dictionary.LOGGER.error("Error during tagging process", e);
            e.printStackTrace();
        }
        Dictionary.LOGGER.info("Tagging process completed.");
    }

    public ItemGroupContent findGroupByItemID(String id){
        if(idToTag.containsKey(id))
            return idToGroup.get(id);
        else
            throw new NullPointerException(id+"가 없습니다. " +idToGroup.keySet());
    }

    public void sortTag(){
        for(EnumTag enumTag : EnumTag.values()){
            Dictionary.LOGGER.info(enumTag +" 태그 정리 시작...");
            getItemTag(enumTag).getSubData().sortGroup();
            Dictionary.LOGGER.info(enumTag +" 태그 정리 완료...");
        }
    }
    //1단계
    public ItemsTag getItemTag(EnumTag enumTag){
        return tagEnumMap.get(enumTag);
    }

    public ItemsTag getItemTag(ItemStack itemStack){
        return getItemTag(getTag(itemStack));
    }
    
    //2단계
    public SubData getItemSub(ItemStack itemStack){
        return getItemSub(getTag(itemStack));
    }
    public SubData getItemSub(EnumTag enumTag){
        return getItemTag(enumTag).getSubData();
    }

    //3단계
    public ItemGroupContent getItemGroup(ItemStack itemStack){
        return getItemTag(getTag(itemStack)).getSubData().getItemGroup(itemStack);
    }
    
    //4단계

    public EnumTag getTag(ItemStack itemStack){
        String namespace = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getNamespace();
        boolean isMinecraft = namespace.equals("minecraft");
        if(isMinecraft) {
            String itemID = itemStack.getDescriptionId();
            int modID = itemID.indexOf("minecraft.");
            itemID = itemID.substring(modID != -1 ? modID + 10 : itemID.indexOf(namespace) + namespace.length());
            //TODO 모드 아이템인 경우 대비 안했음
            Item item = itemStack.getItem();
            String[] split = itemID.split("_");

            if (split.length > 0) {
                for (EnumTag enumCombine : EnumTag.values()) {
                    if (item instanceof RecordItem || item instanceof DiscFragmentItem)
                        return EnumTag.MUSIC;
                    else if (item instanceof SpawnEggItem)
                        continue;
                    else if (item instanceof BannerItem)
                        return EnumTag.BANNER;
                    else if (item instanceof BoatItem || item instanceof MinecartItem || item instanceof SaddleItem)
                        return EnumTag.RIDING;
                    else if (item instanceof ProjectileItem)
                        return EnumTag.PROJECTILE;
                    else if (item instanceof BucketItem)
                        return EnumTag.TOOLS;
                    else if (item instanceof DyeItem)
                        return EnumTag.DYE;
                    else if (item instanceof EnderpearlItem || item instanceof EnderEyeItem)
                        return EnumTag.ENDER;
                    else if (item instanceof ExperienceBottleItem || item instanceof FireChargeItem || item instanceof EndCrystalItem)
                        return EnumTag.SPECIAL;
                    else if (item instanceof ArmorItem)
                        return EnumTag.ARMOR;
                    else if (item instanceof AxeItem || item instanceof PickaxeItem || item instanceof ShovelItem || item instanceof HoeItem)
                        return EnumTag.TOOLS;
                    else if (itemID.contains("armor_stand"))
                        return EnumTag.ETC;
                    else if (itemID.contains("horse_armor"))
                        return EnumTag.ETC;
                    else if (itemID.contains("crimson") || itemID.contains("quartz") || itemID.contains("nether_"))
                        return EnumTag.NETHER;
                    else if (itemID.contains("coral") || itemID.contains("kelp"))
                        return EnumTag.CORAL;
                    if (enumCombine.containsKey(getItemCutID(itemStack))) {
                        return enumCombine;
                    }
                }
            }
        }

        return EnumTag.ETC;
    }


    /**
     * 앞 글자나 뒷 글자로 아이디 가져오기.
     * */
    public String getItemCutID(ItemStack itemStack) {
        String itemID = itemStack.getDescriptionId();
        itemID = itemID.substring(itemID.indexOf("minecraft.") + 10);
        String[] split = itemID.split("_");
        if (split.length > 0) {
            String postfix = split[split.length-1];
            String prefix = split[0];
            for(EnumTag enumTag : EnumTag.values()){
                if(enumTag.containsKey(postfix))
                    return postfix;
                else if(enumTag.containsKey(prefix))
                    return prefix;
                else
                    return postfix;

            }
        }
        return itemID;
    }

}