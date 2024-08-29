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
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IDictionaryAdapter;
import org.ruoland.dictionary.dictionary.dictionary.entity.EntityContent;
import org.ruoland.dictionary.dictionary.dictionary.item.DefaultDictionary;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemSubData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ContentManager {
    private static final TreeMap<String, String> biomes = new TreeMap<>();
    private static final ArrayList<ItemStack> ITEM_LIST = new ArrayList<>();
    private static final HashMap<String, ItemStack> ITEM_STACK_MAP = new HashMap<>();
    private static final TreeMap<String, EntityType> entities = new TreeMap<>();
    private static final HashMap<String, BaseContent> contentMap = new HashMap<>();
    public static void loadAllContent() throws IllegalAccessException {
        loadMinecraftItems();
        loadEntities();
        loadBiome();
    }

    public static void loadEntities(){
        Class<EntityType> typeClass = EntityType.class;
        Field[] entityFields = typeClass.getFields();
        for(Field field : entityFields){
            if(field.getType() == typeClass){
                try {
                    EntityType entityType = (EntityType) field.get(null);
                    entities.put(entityType.getDescriptionId(), entityType);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (Map.Entry<String, EntityType> entry : entities.entrySet()) {
            String id = entry.getKey();
            EntityType entityType = entry.getValue();
            contentMap.put(id, new EntityContent(entityType));
        }
    }

    public static String getEntityNameById(String id){
        Dictionary.LOGGER.info("{}의 이름을 가져옵니다. {}", id, entities);
        return entities.get(id).getDescription().getString();
    }
    public static String getBiomeNameById(String id){
        Dictionary.LOGGER.info("{}의 이름을 가져옵니다. {}", id, biomes);
        return biomes.get(id);
    }

    /**
     * 마인크래프트 기본 아이템 전부 불러오기
     * 리플렉션 제외하면 모든 아이템을 가져올 수 없는 것 같음
     */
    public static void loadMinecraftItems() throws RuntimeException, IllegalAccessException {
        Field[] itemFields = Items.class.getFields();
        ItemStack[] itemStacks = new ItemStack[itemFields.length];
        for (Field itemField : itemFields) {
            if (itemField.getType() == Item.class) {
                ItemStack itemStack = new ItemStack((Item) itemField.get(null));
                ITEM_STACK_MAP.put(itemStack.getDescriptionId(), itemStack);
                ITEM_LIST.add(itemStack);
            }
        }
        for (ItemStack itemStack : ITEM_LIST) {
            String id = itemStack.getDescriptionId();
            contentMap.put(id, new ItemContent(itemStack));
        }
    }

    public static ArrayList<ItemStack> getItemList() {
        return ITEM_LIST;
    }


    public static void loadBiome(){
        Class typeClass = Biomes.class;
        Field[] biomeFields = typeClass.getFields();

        for(Field field : biomeFields){
            try {
                ResourceKey<Biome> biomeKey = (ResourceKey<Biome>) field.get(null);
                String location = "biome."+biomeKey.location().getNamespace()+"."+biomeKey.location().getPath();
                String biomeName = LangManager.getEnglishName(location);

                biomes.put(location ,biomeName);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        for (Map.Entry<String, String> entry : biomes.entrySet()) {
            String id = entry.getKey();
            String name = entry.getValue();
            contentMap.put(id, new BiomeContent(id));
        }
    }
    public static HashMap<String, ItemStack> getItemStackMap() {
        return ITEM_STACK_MAP;
    }
    public static String getContent(ItemStack itemStack) {
        Dictionary.LOGGER.info("getContent called for item: {}", itemStack.getDescriptionId());

        TagManager tagManager = TagManager.getTagManager();
        ItemSubData sub = tagManager.getItemTag(itemStack).getSubData();
        ItemGroupContent itemGroup = sub.getItemGroup(itemStack);
        ItemContent content = itemGroup.getContent(new IDictionaryAdapter.ItemStackAdapter(itemStack));

        //Dictionary.LOGGER.info("BaseContent retrieved - itemId: {}, description: {}", itemStack.getDescriptionId(), content.getDictionary(false));

        StringBuilder stringBuffer = new StringBuilder();
        if(sub.getSubDictionary() == null && content.getDictionary() == null) {
            stringBuffer.append(sub.getItemGroup(itemStack).getDictionary());
        } else {

            stringBuffer.append("아이템 설명:\n");
            stringBuffer.append(itemGroup.getDictionary()).append("\n\n");

            String itemDescription = content.getDictionary();

            //Dictionary.LOGGER.info("Final item description for {}: {}", itemStack.getDescriptionId(), itemDescription);

            if(itemDescription != null && !itemDescription.equals(DefaultDictionary.ITEM_DESC)) {
                return itemDescription;
            }

        }


        return stringBuffer.toString();
    }

}
