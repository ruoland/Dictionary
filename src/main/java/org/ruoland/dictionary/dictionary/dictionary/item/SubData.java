package org.ruoland.dictionary.dictionary.dictionary.item;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.manager.TagManager;

import java.util.TreeMap;

public class SubData {

    @SerializedName("요약 설명")
    private String subDictionary = DefaultDictionary.SHORT_DESC;


    @SerializedName("아이템 그룹")
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

    public ItemStack getZeroItem(){
        for(ItemGroupContent groupContent : itemGroupContentMap.values()){
            System.out.println("서브데이터에서 " +groupContent.getZeroItem());
            return groupContent.getZeroItem();
        }
        return null;
    }

    public void addItemContent(ItemStack itemStack){
        String itemGroupCutID = TagManager.getTagManager().getItemCutID(itemStack);
        ItemGroupContent groupItemContent = itemGroupContentMap.get(itemGroupCutID);

        if(groupItemContent == null) {
            groupItemContent = new ItemGroupContent();
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

    public void sortGroup(){
//        Dictionary.LOGGER.info(tag + " 정리 중");
//
//        ArrayList<String> removeTagList = new ArrayList<>();
//        ItemGroupContent newGroupContent = new ItemGroupContent();
//
//        for (String key : getGroupMap().keySet()) {
//            ItemGroupContent groupContent = getGroupMap().get(key);
//
//            if (groupContent.getContentMap().values().size() < 3) {
//                newGroupContent.addAll(groupContent);
//                removeTagList.add(key);
//                Dictionary.LOGGER.info("키 세 개 이하인 그룹 발견: "+key);
//            }
//        }
//
//        for(String removeTag : removeTagList){
//            itemGroupContentMap.remove(removeTag);
//            Dictionary.LOGGER.info("3개 이하인 태그를 제거함:" + removeTag);
//        }
//
//        if(!removeTagList.isEmpty())
//            itemGroupContentMap.put(EnumTag.ETC.name(), newGroupContent);
//        Dictionary.LOGGER.info(tag + " 검사 완료");
    }

    public ItemGroupContent getItemGroup(ItemStack itemStack){
        String tagKey = TagManager.getTagManager().getItemCutID(itemStack);
        Dictionary.LOGGER.warn("{}를 찾기 시작. ",tagKey);

        if(itemGroupContentMap.containsKey(tagKey)) {
            Dictionary.LOGGER.warn("{}를 찾았습니다.. ",tagKey);
            return itemGroupContentMap.get(tagKey);
        }
        else {
            Dictionary.LOGGER.warn("{}가 없습니다. ETC를 반환합니다. 그룹에 있는 콘텐츠 맵{}",tagKey, itemGroupContentMap.get("ETC").getContentMap());
            return itemGroupContentMap.get("ETC");
        }
        
    }
    public TreeMap<String, ItemGroupContent> getGroupMap() {
        return itemGroupContentMap;
    }
    @SerializedName("설명 대체")
    private boolean canReplace=true;

    @SerializedName("태그")
    public EnumTag tag;

}
