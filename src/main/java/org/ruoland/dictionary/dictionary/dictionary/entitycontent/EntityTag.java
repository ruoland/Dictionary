package org.ruoland.dictionary.dictionary.dictionary.entitycontent;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.fabricmc.fabric.impl.object.builder.FabricEntityTypeImpl;
import net.fabricmc.fabric.mixin.object.builder.DefaultAttributeRegistryAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Creeper;
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
        return entities.get(id).getDescription().getString();
    }
}
