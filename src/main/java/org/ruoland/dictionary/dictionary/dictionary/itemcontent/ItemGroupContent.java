package org.ruoland.dictionary.dictionary.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class ItemGroupContent {

    @SerializedName("그룹 대표 이름")
    String groupName = DefaultDictionary.GROUP_NAME;
    
    @SerializedName("그룹 설명")
    String dictionary = DefaultDictionary.GROUP_DESC;

    @SerializedName("아이템들")
    private HashMap<String, ItemContent> itemContentMap = new HashMap<>();

    public ItemGroupContent(){

    }

    public ItemStack getZeroItem(){
        for(ItemContent content : itemContentMap.values()){
            System.out.println("그룹에서 : "+content.getItemStack());
            return content.getItemStack();
        }
        return null;
    }
    public HashMap<String, ItemContent> getContentMap() {
        return itemContentMap;
    }

    public void clear(){
        itemContentMap.clear();;
    }
    public String getDictionary() {
        return dictionary;
    }

    public void add(ItemStack itemStack){
        itemContentMap.put(itemStack.getDescriptionId(), new ItemContent(itemStack));
    }
    public void addAll(ItemGroupContent groupContent){
        this.itemContentMap.putAll(groupContent.getContentMap());
    }
    public ItemContent getItemContent(ItemStack stack){
        return itemContentMap.get(stack.getDescriptionId());
    }

    public boolean hasItem(ItemStack itemStack){
        for(ItemContent content : itemContentMap.values()){
            if(itemStack.getDescriptionId().equals(content.itemID))
                return true;
        }
        return false;
    }

    public String getGroupName() {
        return groupName;
    }


}