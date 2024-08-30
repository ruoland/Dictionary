package org.ruoland.dictionary.dictionary.dictionary.developer.category;

import com.google.gson.annotations.SerializedName;
import org.ruoland.dictionary.Dictionary;

import java.util.TreeMap;

public abstract class BaseTags<T extends Enum<T> & IEnumTag, E extends BaseSubData> {
    @SerializedName(value = "목록", alternate = {"아이템 태그", "엔티티 태그"})
    private final TreeMap<String, E> tagSubMap = new TreeMap<>();

    transient T enumTag;

    @Override
    public String toString() {
        return "BaseTags{" +
                "tagSubMap=" + tagSubMap +
                ", enumTag=" + enumTag +
                ", tag='" + tag + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @SerializedName("tag")
    String tag = "";

    public BaseTags(T enumTag){
        this.enumTag = enumTag;
        tag = enumTag.name();
        addSubData(enumTag);
    }

    public void addSubData(T tag){
        tagSubMap.put(tag.name(), createSubData(tag));
    }
    public TreeMap<String, E> getTagSubMap() {
        return tagSubMap;
    }
    protected abstract E createSubData(T tag);
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagName() {
        return tag;
    }


    public BaseSubData getSubData() {
        return tagSubMap.get(enumTag);
    }

    @SerializedName("문서 버전")
    private String version = Dictionary.VERSION;

}
