package org.ruoland.dictionary.dictionary.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.TagManager;

import java.util.LinkedList;
import java.util.TreeMap;

public class SubData {
    //@SerializedName("태그")
    private transient EnumTag tag;
    @SerializedName("요약 설명")
    private String subDictionary = DefaultDictionary.SHORT_DESC;

    @SerializedName("설명 대체")
    private boolean canReplace=true;

    @SerializedName("아이템 목록")
    private TreeMap<String, ItemGroupContent> itemGroupContentMap = new TreeMap<>();

    SubData(EnumTag tag){
        this.tag = tag;
    }

    /**
     중간 카테고리의 설명
     */
    public String getSubDictionary() {
        return subDictionary;
    }

    public boolean isReplace() {
        return canReplace;
    }

    /**
     * 그룹에 있는 아이템들이 전부 세 개 이하인 경우, 하나로 합침.
     */
    public void sortGroup(){
        boolean sort = true;
        for(ItemGroupContent groupContent : itemGroupContentMap.values()){
            if(groupContent.getContentMap().values().size() > 3){
                sort = false;
            }
        }
        if(sort){
            sort();
        }
    }

    private void sort(){
        LinkedList<ItemContent> itemContents = new LinkedList<>();
        for(ItemGroupContent groupContent : itemGroupContentMap.values()){
            itemContents.addAll(groupContent.getContentMap().values());
        }
        itemGroupContentMap.clear();
        ItemGroupContent groupContent = new ItemGroupContent(tag.name());
        for(ItemContent content : itemContents){
               groupContent.add(content.getItemStack());
        }
        itemGroupContentMap.put(tag.name(), groupContent);
    }
    public void addItemContent(ItemStack itemStack){
        String itemGroupCutID = TagManager.getTagManager().getItemCutID(itemStack);
        ItemGroupContent groupItemContent = itemGroupContentMap.get(itemGroupCutID);

        if(groupItemContent == null) {
            groupItemContent = new ItemGroupContent(itemGroupCutID);
            itemGroupContentMap.put(itemGroupCutID, groupItemContent);
        }

        //그룹 아이템 내에서는 아이템 스택 아이디로
        groupItemContent.add(itemStack);
    }

    public boolean hasGroup(ItemStack itemStack){
        return getItemGroup(itemStack) != null;
    }

    public boolean hasItem(ItemStack itemStack){
        ItemGroupContent groupContent = getItemGroup(itemStack);
        if(groupContent == null)
            return false;
        return groupContent.hasItem(itemStack);
    }

    public ItemGroupContent getItemGroup(ItemStack itemStack){
        return itemGroupContentMap.get(TagManager.getTagManager().getItemCutID(itemStack));
    }
    public TreeMap<String, ItemGroupContent> getGroupMap() {
        return itemGroupContentMap;
    }
}
