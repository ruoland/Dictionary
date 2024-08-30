package org.ruoland.dictionary.dictionary.dictionary.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseSubData;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IDictionaryAdapter;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IEnumTag;
import org.ruoland.dictionary.dictionary.dictionary.manager.TagManager;

public class ItemSubData extends BaseSubData<ItemGroupContent, IDictionaryAdapter.ItemStackAdapter> {

    public ItemSubData(IEnumTag tag){
        super(tag);
    }

    public ItemStack getFirstItem(){
        for(ItemGroupContent groupContent : getGroupMap().values()){
            return groupContent.getZeroItem();
        }
        return null;
    }

    public void addItemContentInGroup(ItemStack itemStack){
        String itemGroupCutID = TagManager.getTagManager().getCutID(itemStack.getDescriptionId());
        ItemGroupContent groupItemContent = getGroupMap().get(itemGroupCutID);

        if(groupItemContent == null) {
            Dictionary.LOGGER.debug("그룹이 없습니다. 그룹을 추가하고, 아이템을 추가합니다. {}", itemGroupCutID);
            groupItemContent = new ItemGroupContent();
            getGroupMap().put(itemGroupCutID, groupItemContent);
        }

        //그룹 아이템 내에서는 아이템 스택 아이디로
        groupItemContent.addToNewContent(new IDictionaryAdapter.ItemStackAdapter(itemStack));
        Dictionary.LOGGER.info("{}, {}가 추가되었습니다.", itemGroupCutID, itemStack.getDescriptionId());

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
//            itemGroupContentMap.put(EnumItemTag.ETC.name(), newGroupContent);
//        Dictionary.LOGGER.info(tag + " 검사 완료");
    }

    @Nullable
    public ItemGroupContent getItemGroup(ItemStack itemStack){
        String tagKey = TagManager.getTagManager().getCutID(itemStack.getDescriptionId());
        Dictionary.LOGGER.trace("{}를 찾기 시작. ",tagKey);

        if(getGroupMap().containsKey(tagKey)) {
            Dictionary.LOGGER.trace("{}를 찾았습니다.. ",tagKey);
            return getGroupMap().get(tagKey);
        }
        else if(getGroupMap().get("ETC") != null){
            Dictionary.LOGGER.debug("{}가 없습니다. ETC를 반환합니다. 그룹에 있는 콘텐츠 맵 {}",tagKey, getGroupMap().get("ETC").getContentMap());
            return getGroupMap().get("ETC");
        }
        else if(getGroupMap().get("ETC") == null){
            Dictionary.LOGGER.warn("ItemSubData에 ETC 조차 없습니다. 그룹이 아예 비어 있는 것처럼 보입니다. {}, {}",tagKey, getGroupMap());
            return null;
        }
        return null;
    }

}
