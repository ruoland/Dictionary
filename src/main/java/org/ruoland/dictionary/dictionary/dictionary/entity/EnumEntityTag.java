package org.ruoland.dictionary.dictionary.dictionary.entity;

import org.ruoland.dictionary.dictionary.dictionary.item.DefaultDictionary;

public enum EnumEntityTag {
    ANIMAL(DefaultDictionary.ANIMAL_NAME),
    MOB(DefaultDictionary.MOB_NAME),
    CREATURE(DefaultDictionary.CREATURE_NAME);
    String[] strings;

    EnumEntityTag(String... str){
        this.strings = str;
    }

    public boolean containsKey(String keyword){
        for(String key : strings){
            if(keyword.equals(key))
                return true;
        }
        return false;
    }


}
