package org.ruoland.dictionary.dictionary.dictionary.manager;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.*;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.*;
import org.ruoland.dictionary.dictionary.dictionary.entity.*;
import org.ruoland.dictionary.dictionary.dictionary.item.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * 태그를 부여하거나, 태그에 있는 아이템을 모아두거나, 태그를 저장, 불러오기 하는 클래스
 */
public class TagManager {
    //태그 -> 아이템 태그를 가져옴
    private static final EnumMap<EnumItemTag, ItemsTag> itemTagMap = new EnumMap<>(EnumItemTag.class);
    private static final EnumMap<EnumEntityTag, EntitiesTag> entityTagMap = new EnumMap<>(EnumEntityTag.class);
    //아이템 아이디 -> 해당 아이템이 속한 그룹을 가져오기
    private static final LinkedHashMap<String, ItemGroupContent> idToGroup = new LinkedHashMap<>();
    private static final TreeMap<IEnumTag, Long> lastEditedMap = new TreeMap<>();


    private static final TagManager TAG_MANAGER;

    static {
        TAG_MANAGER = new TagManager();

    }

    public static TagManager getTagManager() {
        return TAG_MANAGER;
    }

    @Nullable
    public ItemContent findItemByID(String itemID) {
        Dictionary.LOGGER.info("아이템을 찾기 시작합니다: {} ", itemID);

        for (EnumItemTag tag : EnumItemTag.values()) {
            ItemsTag itemsTag = getItemTag(tag);
            ItemSubData itemSubData = itemsTag.getSubData();
            for (ItemGroupContent group : itemSubData.getGroupMap().values()) {
                ItemContent item = group.getContentMap().get(itemID);
                if (item != null) {
                    Dictionary.LOGGER.info("{} 아이템을 찾았습니다.: {}", itemID, item);
                    return item;
                }
            }

        }
        if (itemID.startsWith("item.")) {
            Dictionary.LOGGER.info("아이템을 찾지 못했습니다. 블럭으로 검색합니다. ");

            return findItemByID(itemID.replace("item.", "block."));
        }
        Dictionary.LOGGER.info("아이템을 찾지 못했습니다.: {}", itemID);

        return null;
    }
    public void loadTag(){
        loadItemTag();
        loadEntityTag();
        tagging();
    }
    public void loadEntityTag() {
        Dictionary.LOGGER.info("Starting to load tags...");
        for (EnumEntityTag tag : EnumEntityTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            if (Files.exists(tagFile)) {
                try {
                    EntitiesTag entityTag = (EntitiesTag) Data.readJson(tagFile, EntitiesTag.class);
                    Dictionary.LOGGER.info("{} 파일을 불러옵니다. 아이템 태그{}:", tagFile.getFileName(), entityTag.getTagSubMap());
                    for (EntitySubData subData : entityTag.getTagSubMap().values()) {
                        for (EntityGroupContent group : subData.getGroupMap().values()) {
                            for (EntityContent item : group.getContentMap().values()) {
                                if (item.getDictionary() != null) {
                                    item.setDictionary(item.getDictionary());
                                }
                            }
                        }
                    }

                    entityTagMap.put(tag, entityTag);
                    lastEditedMap.put(tag, tagFile.toFile().lastModified());
                    Dictionary.LOGGER.info("{} 태그 파일을 성공적으로 불러왔습니다. ", entityTag);

                } catch (Exception e) {
                    Dictionary.LOGGER.error("Error loading tag {}: {}", tag, e.getMessage());
                    e.printStackTrace();
                }
            } else {
                Dictionary.LOGGER.warn("Tag file not found for {}, creating a new one", tag);
                entityTagMap.put(tag, new EntitiesTag(tag));
            }
        }
        if(itemTagMap.isEmpty()){
            Dictionary.LOGGER.error("엔티티에서 불러온 태그가 하나도 없습니다.");
            throw new NullPointerException();
        }
        Dictionary.LOGGER.info("Finished loading tags. Total tags loaded: {}", itemTagMap.size());

    }

    public void loadItemTag() {
        Dictionary.LOGGER.info("Starting to load tags...");
        for (EnumItemTag tag : EnumItemTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            if (Files.exists(tagFile)) {
                try {
                    ItemsTag itemsTag = (ItemsTag) Data.readJson(tagFile, ItemsTag.class);
                    Dictionary.LOGGER.info("{} 파일을 불러옵니다. 아이템 태그{}:", tagFile.getFileName(), itemsTag.getTagSubMap());
                    for (ItemSubData subData : itemsTag.getTagSubMap().values()) {
                        for (ItemGroupContent group : subData.getGroupMap().values()) {
                            for (ItemContent item : group.getContentMap().values()) {
                                if (item.getDictionary() != null) {
                                    item.setDictionary(item.getDictionary());
                                }
                            }
                        }
                    }

                    itemTagMap.put(tag, itemsTag);
                    lastEditedMap.put(tag, tagFile.toFile().lastModified());
                    Dictionary.LOGGER.info("{} 태그 파일을 성공적으로 불러왔습니다. ", itemsTag);

                } catch (Exception e) {
                    Dictionary.LOGGER.error("Error loading tag {}: {}", tag, e.getMessage());
                    e.printStackTrace();
                }
            } else {
                Dictionary.LOGGER.warn("Tag file not found for {}, creating a new one", tag);
                itemTagMap.put(tag, new ItemsTag(tag));
            }
        }
        if(itemTagMap.isEmpty()){
            Dictionary.LOGGER.error("아이템에서 불러온 태그가 하나도 없습니다.");
            throw new NullPointerException();
        }
        Dictionary.LOGGER.info("Finished loading tags. Total tags loaded: {}", itemTagMap.size());

    }


    public void loadTagTest() {
        Dictionary.LOGGER.info("Starting to load tags...");
        EnumEntityTag[] entityTags = EnumEntityTag.values();
        EnumItemTag[] itemTags = EnumItemTag.values();

        // Stream을 사용하여 두 배열을 합칩니다.
        IEnumTag[] combinedTags = Stream.concat(
                Arrays.stream(entityTags),
                Arrays.stream(itemTags)
        ).toArray(IEnumTag[]::new);

        for (IEnumTag tag: combinedTags) {

            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            if (Files.exists(tagFile)) {
                try {
                    BaseTags<?,?> baseTags = (BaseTags) Data.readJson(tagFile, BaseTags.class);
                    Dictionary.LOGGER.info("{} 파일을 불러옵니다. 아이템 태그{}:", tagFile.getFileName(), baseTags.getTagSubMap());
                    for (BaseSubData<?,?> subData : baseTags.getTagSubMap().values()) {
                        for (BaseGroupContent group : subData.getGroupMap().values()) {
                            for (Object obj : group.getContentMap().values()) {
                                BaseContent item = (BaseContent) obj;
                                if (item.getDictionary() != null) {
                                    item.setDictionary(item.getDictionary());
                                }
                            }
                        }
                    }

                    if(tag instanceof EnumItemTag enumItemTag)
                        itemTagMap.put(enumItemTag, (ItemsTag) baseTags);
                    else if(tag instanceof EnumEntityTag entityTag)
                        entityTagMap.put(entityTag, (EntitiesTag) baseTags);
                    //TODO 바이옴 조건도 추가하기..

                    lastEditedMap.put(tag, tagFile.toFile().lastModified());
                    Dictionary.LOGGER.info("{} 태그 파일을 성공적으로 불러왔습니다. ", baseTags);

                } catch (Exception e) {
                    Dictionary.LOGGER.error("Error loading tag {}: {}", tag, e.getMessage());
                    e.printStackTrace();
                }
            } else {
                Dictionary.LOGGER.warn("Tag file not found for {}, creating a new one", tag);

                if(tag instanceof EnumItemTag enumItemTag)
                    itemTagMap.put(enumItemTag, new ItemsTag(enumItemTag));
                else if(tag instanceof EnumEntityTag entityTag)
                    entityTagMap.put(entityTag, new EntitiesTag(entityTag));
            }
        }
        if(itemTagMap.isEmpty()){
            Dictionary.LOGGER.error("아이템에서 불러온 태그가 하나도 없습니다.");
            throw new NullPointerException();
        }
        Dictionary.LOGGER.info("Finished loading tags. Total tags loaded: {}", itemTagMap.size());

    }
    public void saveTagTest() throws IOException {
        Dictionary.LOGGER.info("[태그 저장]시작...");
        IEnumTag[] enums = ArrayUtils.addAll(EnumItemTag.values(), EnumEntityTag.values());

        for (IEnumTag tag : enums) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            boolean newFile = !tagFile.toFile().exists();
            if (newFile) {
                Files.createFile(tagFile);
            }

            BaseTags baseTags = itemTagMap.containsKey(tag) ? itemTagMap.get(tag) : entityTagMap.containsKey(tag) ? entityTagMap.get(tag) : null ;

            if (newFile || tagFile.toFile().lastModified() <= lastEditedMap.getOrDefault(tag, 0L)) {
                // Log data being saved
                //logSavedData(itemsTag);

                Data.saveJson(tagFile, baseTags);
                lastEditedMap.put(tag, System.currentTimeMillis());

            } else {
                Dictionary.LOGGER.info("[태그 저장] 이 파일은 저장하지 않습니다, 최근에 수정한 적이 있습니다. {}", tag);
            }
        }
        Dictionary.LOGGER.info("[태그 저장] 저장완료.");
    }

    public void saveItemTag() throws IOException {
        Dictionary.LOGGER.info("[태그 저장]시작...");

        for (EnumItemTag tag : EnumItemTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            boolean newFile = !tagFile.toFile().exists();
            if (newFile) {
                Files.createFile(tagFile);
            }

            ItemsTag itemsTag = itemTagMap.get(tag);
            if (newFile || tagFile.toFile().lastModified() <= lastEditedMap.getOrDefault(tag, 0L)) {
                // Log data being saved
                //logSavedData(itemsTag);

                Data.saveJson(tagFile, itemsTag);
                lastEditedMap.put(tag, System.currentTimeMillis());

            } else {
                Dictionary.LOGGER.info("[태그 저장] 이 파일은 저장하지 않습니다, 최근에 수정한 적이 있습니다. {}", tag);
            }
        }
        Dictionary.LOGGER.info("[태그 저장] 저장완료.");
    }


    public void tagging() {
        Dictionary.LOGGER.info("[태깅] 게임 속 개체를 모두 검사하여 사전을 부여하는 작업을 시작합니다.");
        try {
            for (ItemStack itemStack : ContentManager.getItemList()) {
                ItemsTag itemsTag = getItemTag(getItemEnumTag(itemStack));
                ItemSubData sub = itemsTag.getSubData();

                if (sub == null) {
                    Dictionary.LOGGER.warn("BaseSubData is null for item: {}", itemStack.getDescriptionId());
                    continue;
                }

                ItemGroupContent group = sub.getItemGroup(itemStack);
                //Dictionary.LOGGER.info("[태깅] 중 설명 확인 1단계 {} ",group.getGroupContentMap().values());
                if (group != null) {
                    Dictionary.LOGGER.info("아이템의 그룹이 존재합니다. {} 에 {} 아이템을 추가하였습니다.", group.getGroupName(), itemStack.getDescriptionId());
                    group.addToNewContent(new IDictionaryAdapter.ItemStackAdapter(itemStack));
                } else {
                    Dictionary.LOGGER.info("아이템의 그룹을 찾지 못했습니다. 그룹을 생성한 후 아이템을 추가합니다: {}", itemStack.getDescriptionId());
                    sub.addItemContentInGroup(itemStack);
                }
                //Dictionary.LOGGER.info("[태깅] 중 설명 확인 2단계 {}",group.getGroupContentMap().get(itemStack.getDescriptionId()).getDictionary());
                String itemId = getCutID(itemStack.getDescriptionId());
                if(group == null) {
                    Dictionary.LOGGER.warn("그룹이 없습니다. {}, {}", itemId, sub.getGroupMap());
                    group = sub.getItemGroup(itemStack);
                }

                ItemContent itemContent = group.getContent(new IDictionaryAdapter.ItemStackAdapter(itemStack));
                idToGroup.put(itemId, group);
                //Dictionary.LOGGER.info("[태깅] 중 설명 확인 3단계 {}",group.getGroupContentMap().get(itemStack.getDescriptionId()).getDictionary());
                if (itemContent != null) {
                    //Dictionary.LOGGER.info("Tagging process - itemId: {}, description: {}", itemId, itemContent.getDictionary(true));
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

    @Nullable
    public ItemGroupContent findGroupByItemID(String id){
        if(idToGroup.containsKey(id))
            return idToGroup.get(id);
        else {
            Dictionary.LOGGER.error("findGroupByItemID에서 "+id + "를 찾으려 했지만 없었습니다. " + idToGroup.keySet());
            return null;
        }
    }

    public void sortTag(){

        for(EnumItemTag enumItemTag : EnumItemTag.values()){
            try{
                Dictionary.LOGGER.info(enumItemTag +" 태그 정리 시작...\n", itemTagMap);
                if(getItemTag(enumItemTag).getSubData() == null)
                    throw new NullPointerException("태그 정리 오류: 서브 데이터가 없습니다:"+ enumItemTag);
                getItemTag(enumItemTag).getSubData().sortGroup();
                Dictionary.LOGGER.info(enumItemTag +" 태그 정리 완료...");
            }
            catch (NullPointerException e){
                e.printStackTrace();

                Dictionary.LOGGER.error("태그를 정리하는 중 오류가 발생하였습니다. {}",e.getMessage());
            }
        }
    }
    public BaseTags getDictionaryTag(IDictionaryAdapter adapter){
        if (adapter instanceof IDictionaryAdapter.BiomeAdapter) {

        }
        else if(adapter instanceof IDictionaryAdapter.ItemStackAdapter itemStackAdapter){
            ItemStack itemStack =itemStackAdapter.get();
            return itemTagMap.get(EnumItemTag.valueOf(itemStack.getDescriptionId()));
        }
        else if(adapter instanceof IDictionaryAdapter.LivingEntityAdapter livingEntityAdapter){
            LivingEntity type = livingEntityAdapter.get();
            return entityTagMap.get(getEntityEnumTag(type));
        }
        Dictionary.LOGGER.error("타입을 찾지 못 했습니다. 찾으려 한 객체: {}", adapter);
        return null;
    }

    public EnumEntityTag getEntityEnumTag(LivingEntity entity){

        if(entity instanceof Mob){
            return EnumEntityTag.MOB;
        }else if(entity instanceof Animal){
            return EnumEntityTag.ANIMAL;
        }
        return EnumEntityTag.CREATURE;
    }

    //1단계
    public ItemsTag getItemTag(EnumItemTag enumItemTag){
        return itemTagMap.get(enumItemTag);
    }

    public ItemsTag getItemTag(ItemStack itemStack){
        return getItemTag(getItemEnumTag(itemStack));
    }
    
    //2단계
    public ItemSubData getItemSub(EnumItemTag enumItemTag){
        return getItemTag(enumItemTag).getSubData();
    }

    //3단계
    public ItemGroupContent getItemGroup(ItemStack itemStack){
        return getItemTag(getItemEnumTag(itemStack)).getSubData().getItemGroup(itemStack);
    }
    
    //4단계

    public EnumItemTag getItemEnumTag(ItemStack itemStack){
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
                for (EnumItemTag enumCombine : EnumItemTag.values()) {
                    if (item instanceof RecordItem || item instanceof DiscFragmentItem)
                        return EnumItemTag.MUSIC;
                    else if (item instanceof SpawnEggItem)
                        continue;
                    else if (item instanceof BannerItem)
                        return EnumItemTag.BANNER;
                    else if (item instanceof BoatItem || item instanceof MinecartItem || item instanceof SaddleItem)
                        return EnumItemTag.RIDING;
                    else if (item instanceof ProjectileItem)
                        return EnumItemTag.PROJECTILE;
                    else if (item instanceof BucketItem)
                        return EnumItemTag.TOOLS;
                    else if (item instanceof DyeItem)
                        return EnumItemTag.DYE;
                    else if (item instanceof EnderpearlItem || item instanceof EnderEyeItem)
                        return EnumItemTag.ENDER;
                    else if (item instanceof ExperienceBottleItem || item instanceof FireChargeItem || item instanceof EndCrystalItem)
                        return EnumItemTag.SPECIAL;
                    else if (item instanceof ArmorItem)
                        return EnumItemTag.ARMOR;
                    else if (item instanceof AxeItem || item instanceof PickaxeItem || item instanceof ShovelItem || item instanceof HoeItem)
                        return EnumItemTag.TOOLS;
                    else if (itemID.contains("armor_stand"))
                        return EnumItemTag.ETC;
                    else if (itemID.contains("horse_armor"))
                        return EnumItemTag.ETC;
                    else if (itemID.contains("crimson") || itemID.contains("quartz") || itemID.contains("nether_"))
                        return EnumItemTag.NETHER;
                    else if (itemID.contains("coral") || itemID.contains("kelp"))
                        return EnumItemTag.CORAL;
                    if (enumCombine.containsKey(getCutID(itemStack.getDescriptionId()))) {
                        return enumCombine;
                    }
                }
            }
        }

        return EnumItemTag.ETC;
    }


    /**
     * 앞 글자나 뒷 글자로 아이디 가져오기.
     * */
    public String getCutID(String itemID) {
        itemID = itemID.substring(itemID.indexOf("minecraft.") + 10);
        String[] split = itemID.split("_");
        if (split.length > 0) {
            String postfix = split[split.length-1];
            String prefix = split[0];
            for(EnumItemTag enumItemTag : EnumItemTag.values()){
                if(enumItemTag.containsKey(postfix))
                    return postfix;
                else if(enumItemTag.containsKey(prefix))
                    return prefix;
                else
                    return postfix;

            }
        }
        return itemID;
    }

}