package org.ruoland.dictionary.dictionary.dictionary.biome;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.ruoland.dictionary.dictionary.dictionary.LangManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.TreeMap;

public class BiomeTag {
    private static final TreeMap<String, String> biomes = new TreeMap<>();
    private static final Logger log = LoggerFactory.getLogger(BiomeTag.class);

    public static void load(){
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

}
