package org.ruoland.dictionary.dictionary.dictionary.entity;

import com.google.gson.annotations.SerializedName;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseSubData;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IDictionaryAdapter;

import java.util.TreeMap;

public class EntitySubData extends BaseSubData<EntityGroupContent, IDictionaryAdapter.EntityTypeAdapter> {
    @SerializedName("출신")
    private String modid = "minecraft";

    @SerializedName("개체 목록")
    private TreeMap<EnumEntityTag, EntityContent> entityTagMap = new TreeMap<>();


    EntitySubData(EnumEntityTag tag){
        super(tag);
    }

}
