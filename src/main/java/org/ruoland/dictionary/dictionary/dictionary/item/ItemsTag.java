package org.ruoland.dictionary.dictionary.dictionary.item;

import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseTags;

public class ItemsTag extends BaseTags<EnumItemTag, ItemSubData> {

    public ItemsTag(EnumItemTag enum_tag){
        super(enum_tag);
    }

    public ItemSubData getSubData() {
        return getTagSubMap().get(getTagName());
    }

    @Override
    public void addSubData(EnumItemTag tag) {
        getTagSubMap().put(tag.name(), new ItemSubData(tag));
    }

    @Override
    protected ItemSubData createSubData(EnumItemTag tag) {
        return new ItemSubData(tag);
    }

    @Override
    public String toString() {
        return "ItemsTag{} " + super.toString();
    }
}