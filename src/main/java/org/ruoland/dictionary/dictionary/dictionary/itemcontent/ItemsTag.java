package org.ruoland.dictionary.dictionary.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;
import org.ruoland.dictionary.Dictionary;

import java.util.EnumMap;

public class ItemsTag {

    @SerializedName("아이템 태그")
    private final EnumMap<EnumTag, SubData> tagSubMap = new EnumMap<>(EnumTag.class);

    transient String thisName;

    EnumTag tag;

    public ItemsTag(EnumTag tag){
        this.thisName = tag.name();
        this.tag = tag;
        tagSubMap.put(tag, new SubData(tag));
    }

    public String getTagName() {
        return tag.name();
    }

    public SubData getSubData() {
        return tagSubMap.get(tag);
    }

    @SerializedName("문서 버전")
    private String version = Dictionary.VERSION;

    // 이 메소드를 추가하여 아이템 설명을 설정할 수 있게 합니다.
    public void setItemDescription(String itemId, String description) {
        SubData subData = getSubData();
        for (ItemGroupContent group : subData.getGroupMap().values()) {
            ItemContent item = group.getContentMap().get(itemId);
            if (item != null) {
                item.setDictionary(description);
                return;
            }
        }
    }

    // 이 메소드를 추가하여 아이템 설명을 가져올 수 있게 합니다.
    public String getItemDescription(String itemId) {
        SubData subData = getSubData();
        for (ItemGroupContent group : subData.getGroupMap().values()) {
            ItemContent item = group.getContentMap().get(itemId);
            if (item != null) {
                return item.getDictionary(true);
            }
        }
        return null;
    }
}