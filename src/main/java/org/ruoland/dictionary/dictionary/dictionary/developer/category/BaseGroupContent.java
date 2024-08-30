package org.ruoland.dictionary.dictionary.dictionary.developer.category;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.biome.BiomeContent;
import org.ruoland.dictionary.dictionary.dictionary.entity.EntityContent;
import org.ruoland.dictionary.dictionary.dictionary.item.DefaultDictionary;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;

import java.util.HashMap;

public abstract class BaseGroupContent<T extends IDictionaryAdapter, U extends BaseContent> {

    @SerializedName("그룹 대표 이름")
    String groupName = DefaultDictionary.GROUP_NAME;

    @SerializedName("그룹 설명")
    String dictionary = DefaultDictionary.GROUP_DESC;

    @SerializedName(value = "아이템들", alternate = {"그룹 목록"})
    private HashMap<String, U> baseContentMap = new HashMap<>();

    public HashMap<String, U> getContentMap() {
        return baseContentMap;
    }
    public void clear(){
        baseContentMap.clear();
    }
    public String getDictionary() {
        return dictionary;
    }

    public boolean existCheck(String id) {
        if (getContentMap().containsKey(id)) {
            Dictionary.LOGGER.trace("아이템이 이미 그룹에 존재합니다.: {}", id);
            return true;
        }
        return false;
    }
    public void addToNewContent(T adapter){
        BaseContent newContent;
        if(getContentMap().containsKey(adapter.getID())){
            Dictionary.LOGGER.trace("이미 존재하는 도감: {}. {}, {}", adapter.getID(), adapter.getType(), adapter.get());
            return;
        }
        switch (adapter.getType()) {
            case "ItemStack":

                newContent = new ItemContent((ItemStack) adapter.get());
                break;
            case "Biome":
                newContent = new BiomeContent(adapter.getID());
                break;
            case "Entity":
                newContent = new EntityContent((EntityType) adapter.get());
                break;
            default:
                newContent = null;
                Dictionary.LOGGER.error("타입을 찾을 수 없음. 콘텐츠 추가 불가능 {}, obj{}", adapter, adapter.getType());
        }


        getContentMap().put(adapter.getID(), (U) adapter.create());
        Dictionary.LOGGER.info("새로운 아이템을 그룹에 추가하였습니다.: {}, {}", newContent.id, newContent.getDictionary());
    }

    public U getContent(T id){
        return baseContentMap.get(id.getID());
    }

    public boolean hasItem(String id){
        for(U content : baseContentMap.values()){
            if(content.getLocalizationId().equals(id))
                return true;
        }
        return false;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return "BaseGroupContent{" +
                "groupName='" + groupName + '\'' +
                ", dictionary='" + dictionary + '\'' +
                ", baseContentMap=" + baseContentMap +
                '}';
    }
}
