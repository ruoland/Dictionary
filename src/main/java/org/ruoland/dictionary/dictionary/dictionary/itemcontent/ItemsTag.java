package org.ruoland.dictionary.dictionary.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;
import org.ruoland.dictionary.Dictionary;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.TreeMap;

//파일 이름
public class ItemsTag {

    @SerializedName("아이템 태그")
    private final EnumMap<EnumTag, SubData> tagSubMap = new EnumMap<>(EnumTag.class);
    //파일 이름
    transient String thisName;

    EnumTag tag;
    public ItemsTag(EnumTag tag){
        this.thisName = tag.name();
        this.tag = tag;

        tagSubMap.put(tag, new SubData(tag));

    }


    public String getTagName() {
        return thisName;
    }

    public SubData getSubData() {
        return tagSubMap.get(tag);
    }

    @SerializedName("문서 버전")
    private String version = Dictionary.VERSION;

}
