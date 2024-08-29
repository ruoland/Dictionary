package org.ruoland.dictionary.dictionary.dictionary.entity;

import com.google.gson.annotations.SerializedName;

import java.util.EnumMap;

public class EntitiesTag {
    @SerializedName("엔티티 태그")
    private final EnumMap<EnumEntityTag, EntitySubData> tagContentMap = new EnumMap<>(EnumEntityTag.class);

    @SerializedName("tag")
    transient EnumEntityTag tag;

    public EntitiesTag(EnumEntityTag tag) {
        this.tag = tag;
        tagContentMap.put(tag, new EntitySubData(tag));
    }

    public String getTagName(){
        return tag.name();
    }

    public void getSubData(){

    }
}
