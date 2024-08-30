package org.ruoland.dictionary.dictionary.dictionary.entity;

import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseTags;

public class EntitiesTag extends BaseTags<EnumEntityTag, EntitySubData> {

    public EntitiesTag(EnumEntityTag tag) {
        super(tag);
    }

    @Override
    public void addSubData(EnumEntityTag tag) {
        getTagSubMap().put(tag.name(), new EntitySubData(tag));
    }

    @Override
    protected EntitySubData createSubData(EnumEntityTag tag) {
        return new EntitySubData(tag);
    }

    @Override
    public EntitySubData getSubData() {
        return getTagSubMap().get(getTagName());
    }
}
