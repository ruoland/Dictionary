package org.ruoland.dictionary.dictionary.dictionary;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.Data;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.EnumTag;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemsTag;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.SubData;

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

    public void saveTag() throws IOException {

        for (EnumTag tag : EnumTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            boolean newFile = !tagFile.toFile().exists();
            if(newFile)
                tagFile.toFile().createNewFile();

            if(newFile || tagFile.toFile().lastModified() <= lastEditedMap.get(tag)){
                Data.saveJson(tagFile, getTagManager().getItemTag(tag));
            }

        }
    }
    public void loadTag() {
        for (EnumTag tag : EnumTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            if (Files.exists(tagFile))
                tagEnumMap.put(tag, (ItemsTag) Data.readJson(tagFile, ItemsTag.class));
            lastEditedMap.put(tag, tagFile.toFile().lastModified());
        }
        if(tagEnumMap.isEmpty()){
            for(EnumTag tag : EnumTag.values()){
                tagEnumMap.put(tag, new ItemsTag(tag));

            }

        }
        tagging();
    }

    public void tagging(){
        try {
            for (ItemStack itemStack : ItemManager.getItemList()) {

                ItemsTag itemsTag = getItemTag(getTag(itemStack));
                SubData sub = itemsTag.getSubData();

                if (sub == null) {
                    continue;
                }
                if (sub.hasGroup(itemStack))
                    sub.getItemGroup(itemStack).add(itemStack);
                else
                    sub.addItemContent(itemStack);

                idToTag.put(getItemCutID(itemStack), getTag(itemStack));
                idToGroup.put(getItemCutID(itemStack), sub.getItemGroup(itemStack));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ResourceLocation getItemLocation(ItemStack itemStack){
        return  BuiltInRegistries.ITEM.getKey(itemStack.getItem());
    }
    public ItemGroupContent findGroupByItemID(String id){
        if(idToTag.containsKey(id))
            return idToGroup.get(id);
        else
            throw new NullPointerException(id+"가 없습니다. " +idToGroup.keySet());
    }
    public ItemsTag getItemTag(EnumTag enumTag){
        return tagEnumMap.get(enumTag);
    }

    public void sortTag(){
        for(EnumTag enumTag : EnumTag.values()){
            getItemSub(enumTag).sortGroup();
        }
    }
    public ItemsTag getItemTag(ItemStack itemStack){
        return getItemTag(getTag(itemStack));
    }
    public SubData getItemSub(ItemStack itemStack){
        return getItemTag(getTag(itemStack)).getSubData();
    }
    public ItemGroupContent getItemGroup(ItemStack itemStack){
        return getItemTag(getTag(itemStack)).getSubData().getItemGroup(itemStack);
    }
    public SubData getItemSub(EnumTag enumTag){
        return getItemTag(enumTag).getSubData();
    }

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