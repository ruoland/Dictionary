package org.ruoland.dictionary.dictionary.dictionary.entitycontent;

import org.ruoland.dictionary.dictionary.dictionary.itemcontent.DefaultDictionary;

public enum EnumEntityTag {
    ENTITY_ANIMAL(DefaultDictionary.ANIMAL_NAME),
    MOB(DefaultDictionary.MOB_NAME);
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
