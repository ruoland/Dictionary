package org.ruoland.dictionary.dictionary.dictionary.manager;


import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.biome.BiomeContent;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseContent;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseSubData;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IDictionaryAdapter;
import org.ruoland.dictionary.dictionary.dictionary.entity.EntityContent;
import org.ruoland.dictionary.dictionary.dictionary.item.DefaultDictionary;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static final HashMap<String, ItemStack> ITEM_STACK_MAP = new HashMap<>();
    private static final TreeMap<String, EntityType> ENTITIES = new TreeMap<>();
    private static final TreeMap<String, String> BIOMES = new TreeMap<>();

    private static final HashMap<String, BaseContent> CONTENT_MAP = new HashMap<>();

    private static final ArrayList<ItemStack> ITEM_LIST = new ArrayList<>();

    public static ArrayList<ItemStack> getItemList() {
        return ITEM_LIST;
    }

    public static TreeMap<String, EntityType> getEntities() {
        return ENTITIES;
    }



    public static void updateData(){
        File updateFile = new File("./update.txt");

    }
    public static String getContent(IDictionaryAdapter adapter) {
        Dictionary.LOGGER.info("[데이터]getContent called for item: {}", adapter.getID());

        TagManager tagManager = TagManager.getTagManager();
        BaseSubData sub = tagManager.getDictionaryTag(adapter).getSubData();
        BaseGroupContent baseGroup = sub.findGroupByID(adapter.getID());
        BaseContent content = baseGroup.getContent(adapter);

        StringBuilder stringBuffer = new StringBuilder();
        if(sub.getSubDictionary() == null && content.getDictionary() == null) {
            stringBuffer.append(sub.findGroupByID(adapter.getID()).getGoupDesc());
        } else {
            stringBuffer.append("아이템 설명:\n");
            stringBuffer.append(baseGroup.getGoupDesc()).append("\n\n");
            String dictionary = content.getDictionary();
            if(dictionary != null && !dictionary.equals(DefaultDictionary.ITEM_DESC)) {
                return dictionary;
            }
        }

        return stringBuffer.toString();
    }


    /**
     * 마인크래프트 기본 아이템 전부 불러오기
     * 리플렉션 제외하면 모든 아이템을 가져올 수 없는 것 같음
     * TODO 모드 아이템 대응 불가능
     */
    public static void loadMinecraftItems() throws RuntimeException, IllegalAccessException {
        Field[] itemFields = Items.class.getFields();
        for (Field itemField : itemFields) {
            if (itemField.getType() == Item.class) {
                ItemStack itemStack = new ItemStack((Item) itemField.get(null));
                ITEM_STACK_MAP.put(itemStack.getDescriptionId(), itemStack);
                CONTENT_MAP.put(itemStack.getDescriptionId(), new ItemContent(itemStack));
                ITEM_LIST.add(itemStack);
            }
        }

        List<ItemContent> entityContentList = CONTENT_MAP.values().stream()
                .filter(content -> content instanceof ItemContent)
                .map(content -> (ItemContent) content)
                .collect(Collectors.toList());

        Dictionary.LOGGER.info("[데이터]불러온 아이템의 개수: {}", entityContentList.size());
        if(entityContentList.isEmpty())
            Dictionary.LOGGER.error("[위험][데이터] 불러온 아이템이 없습니다.");
    }

    public static HashMap<String, ItemStack> getItemStackMap() {
        return ITEM_STACK_MAP;
    }

    public static void loadEntities(){
        //EntityType에 있는 가입된 EntityType 필드를 전부 긁어 오기
        //TODO 이렇게 하면 모드 엔티티 대응 불가능
        Class<EntityType> typeClass = EntityType.class;
        Field[] entityFields = typeClass.getFields();
        for(Field field : entityFields){
            if(field.getType() == typeClass){
                try {
                    EntityType type = (EntityType) field.get(null);
                    Dictionary.LOGGER.debug("엔티티 검색 중: {}, {},", type.getBaseClass(), type.getDescriptionId());

                    //발견시 해당 엔티티의 아이디와 객체를 맵에 넣음
                    ENTITIES.put(type.getDescriptionId(), type);
                    CONTENT_MAP.put(type.getDescriptionId(), new EntityContent(type));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //엔티티 객체가 얼마나 되는지 가져오기
        List<EntityContent> entityContentList = CONTENT_MAP.values().stream()
                .filter(content -> content instanceof EntityContent)
                .map(content -> (EntityContent) content)
                .collect(Collectors.toList());

        Dictionary.LOGGER.info("[데이터]불러온 엔티티 개수: {}", entityContentList.size());

        if(entityContentList.isEmpty())
            Dictionary.LOGGER.error("[위험][데이터] 불러온 엔티티가 없습니다.");
    }

    public static String getEntityNameById(String id){
        Dictionary.LOGGER.info("[데이터]{}의 이름을 가져옵니다. {}", id, ENTITIES);
        return ENTITIES.get(id).getDescription().getString();
    }

    public static void loadBiome(){
        Class typeClass = Biomes.class;
        Field[] biomeFields = typeClass.getFields();
        for(Field field : biomeFields){
            try {
                ResourceKey<Biome> biomeKey = (ResourceKey<Biome>) field.get(null);
                String location = "biome."+biomeKey.location().getNamespace()+"."+biomeKey.location().getPath();
                String biomeName = LangManager.getEnglishName(location);
                BIOMES.put(location ,biomeName);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        for (Map.Entry<String, String> entry : BIOMES.entrySet()) {
            String id = entry.getKey();
            CONTENT_MAP.put(id, new BiomeContent(id));
        }
    }
    public static String getBiomeNameById(String id){
        Dictionary.LOGGER.info("[데이터]{}의 이름을 가져옵니다. {}", id, BIOMES);
        return BIOMES.get(id);
    }

}
