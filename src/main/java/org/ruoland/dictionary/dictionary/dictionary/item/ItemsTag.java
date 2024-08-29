package org.ruoland.dictionary.dictionary.dictionary.item;

import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseTags;

public class ItemsTag extends BaseTags<EnumItemTag, ItemSubData> {

    public ItemsTag(EnumItemTag enum_tag){
        super(enum_tag);
    }


    public ItemSubData getSubData() {
        //Dictionary.LOGGER.info(" 태그 이름: {}, {}", getTagName(), getTagSubMap().values());
        return getTagSubMap().get(EnumItemTag.valueOf(getTagName()));
    }

    @Override
    public void addSubData(EnumItemTag tag) {
        getTagSubMap().put(tag, new ItemSubData(tag));
    }


    // 이 메소드를 추가하여 아이템 설명을 가져올 수 있게 합니다.
    public String getItemDescription(String itemId) {
        ItemSubData itemSubData = getSubData();
        for (ItemGroupContent group : itemSubData.getGroupMap().values()) {
            ItemContent item = group.getContentMap().get(itemId);
            if (item != null) {
                return item.getDictionary();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ItemsTag{} " + super.toString();
    }
}