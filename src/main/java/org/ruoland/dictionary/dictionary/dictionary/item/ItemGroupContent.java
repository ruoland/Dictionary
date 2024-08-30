package org.ruoland.dictionary.dictionary.dictionary.item;

import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IDictionaryAdapter;

public class ItemGroupContent extends BaseGroupContent<IDictionaryAdapter.ItemStackAdapter, ItemContent> {

    public ItemStack getZeroItem(){
        for(ItemContent content : getContentMap().values()){
            return content.getItemStack();
        }
        return null;
    }


    @Override
    public ItemContent getContent(IDictionaryAdapter.ItemStackAdapter id) {
        ItemContent itemContent = super.getContent(id);
        Dictionary.LOGGER.trace("아이템 도감 설명을 가져옵니다. {}, {}", itemContent, getContentMap().get(id.getID()));
        if(itemContent != null)
            itemContent.setItemStack(id.get());
        else
            Dictionary.LOGGER.info("아이템 콘텐츠를 찾지 못했습니다.. {}, {}, {}, 맵 리스트 \n{}", id.getID(), id.getType(), id.get(), getContentMap());
        return itemContent;
    }
}