package org.ruoland.dictionary.dictionary.dictionary.entitycontent;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.fabricmc.fabric.impl.object.builder.FabricEntityTypeImpl;
import net.fabricmc.fabric.mixin.object.builder.DefaultAttributeRegistryAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.lang.reflect.Field;
import java.util.TreeMap;

public class EntityTag {
    private static final TreeMap<String, EntityType> entities = new TreeMap<>();
    public static void load(){
        System.out.println(DefaultAttributeRegistryAccessor.getRegistry().keySet());
        Class typeClass = EntityType.class;
        Field[] entityFields = typeClass.getFields();
        for(Field field : entityFields){
            if(field.getType() == typeClass){
                try {
                    System.out.println("태그" + field.get(null));

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
