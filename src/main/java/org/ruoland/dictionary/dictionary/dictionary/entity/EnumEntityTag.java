package org.ruoland.dictionary.dictionary.dictionary.entity;

import org.ruoland.dictionary.dictionary.dictionary.developer.category.IEnumTag;
import org.ruoland.dictionary.dictionary.dictionary.item.DefaultDictionary;

public enum EnumEntityTag implements IEnumTag {
    ANIMAL(DefaultDictionary.ANIMAL_NAME),
    MOB(DefaultDictionary.MOB_NAME),
    CREATURE(DefaultDictionary.CREATURE_NAME),
    MISC(DefaultDictionary.MISC);
    final String[] strings;
    private EntitiesTag entityTag;

    EnumEntityTag(String... str){
        this.strings = str;
    }

    @Override
    public EnumEntityTag value(String tags) {
        return valueOf(tags);
    }
    public boolean containsKey(String keyword){
        for(String key : strings){
            if(keyword.equals(key))
                return true;
        }
        return false;
    }




}
