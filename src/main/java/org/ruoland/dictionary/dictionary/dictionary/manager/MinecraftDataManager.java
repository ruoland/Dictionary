package org.ruoland.dictionary.dictionary.dictionary.manager;


import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class MinecraftDataManager {
    private static final Logger log = LoggerFactory.getLogger(BiomeManager.class);

    private static final ArrayList<ItemStack> ITEM_LIST = new ArrayList<>();
    private static final HashMap<String, ItemStack> ITEM_STACK_MAP = new HashMap<>();

    public static ArrayList<ItemStack> getItemList() {
        return ITEM_LIST;
    }

    public static HashMap<String, ItemStack> getItemStackMap() {
        return ITEM_STACK_MAP;
    }

    public static ItemStack[] loadItem() throws RuntimeException, IllegalAccessException {
        Field[] itemFields = Items.class.getFields();
        ItemStack[] itemStacks = new ItemStack[itemFields.length];
        for (int i = 0; i < itemFields.length; i++) {
            if (itemFields[i].getType() == Item.class) {
                ItemStack itemStack = new ItemStack((Item) itemFields[i].get(null));
                ITEM_STACK_MAP.put(itemStack.getDescriptionId(), itemStack);
                ITEM_LIST.add(itemStack);
            }
        }
        return itemStacks;
    }
    private static final TreeMap<String, EntityType> entities = new TreeMap<>();
    public static void loadEntities(){
        Class typeClass = EntityType.class;
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
    }

    public static TreeMap<String, EntityType> getEntities() {
        return entities;
    }

    private static final TreeMap<String, String> biomes = new TreeMap<>();
    public static void loadBiome(){
        Class typeClass = Biomes.class;
        Field[] biomeFields = typeClass.getFields();

        for(Field field : biomeFields){
            try {
                ResourceKey<Biome> biomeKey = (ResourceKey<Biome>) field.get(null);
                String location = "biome."+biomeKey.location().getNamespace()+"."+biomeKey.location().getPath();
                String biomeName = LangManager.getBiomeName(location);

                biomes.put(location ,biomeName);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static TreeMap<String, String> getBiomes() {
        return biomes;
    }
}
