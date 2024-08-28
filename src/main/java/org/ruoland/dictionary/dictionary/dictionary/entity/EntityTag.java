package org.ruoland.dictionary.dictionary.dictionary.entity;

import net.minecraft.world.entity.EntityType;
import org.ruoland.dictionary.Dictionary;

import java.lang.reflect.Field;
import java.util.TreeMap;

public class EntityTag {
    private static final TreeMap<String, EntityType> entities = new TreeMap<>();
    public static void load(){
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

    public static String getEntityNameById(String id){
        Dictionary.LOGGER.info("{}의 이름을 가져옵니다. {}", id, entities);
        return entities.get(id).getDescription().getString();
    }
}
