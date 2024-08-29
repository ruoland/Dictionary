package org.ruoland.dictionary.dictionary.dictionary.developer.category;
import com.google.gson.annotations.SerializedName;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.item.DefaultDictionary;
import org.ruoland.dictionary.dictionary.dictionary.manager.TagManager;

import java.util.TreeMap;

public class BaseSubData<T extends BaseGroupContent, U extends IDictionaryAdapter> {


    private transient IEnumTag enumTag;
    @SerializedName("요약 설명")
    private String subDictionary = DefaultDictionary.SHORT_DESC;

    @SerializedName(value="그룹", alternate = {"아이템 그룹", "엔티티 그룹"})
    private TreeMap<String, T> groupContentMap = new TreeMap<>();

    protected BaseSubData(IEnumTag tag){
        this.enumTag = tag;
    }

    /**
     중간 카테고리의 설명
     */
    public String getSubDictionary() {
        return subDictionary;
    }


    public boolean hasGroup(U item){
        return getGroup(item.getID()) != null;
    }

    public boolean hasContent(String id){
        T groupContent = getGroup(id);
        if(groupContent == null)
            return false;
        return groupContent.hasItem(id);
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

    public T getGroup(String id){
        String tagKey = TagManager.getTagManager().getCutID(id);
        Dictionary.LOGGER.warn("[그룹 찾기]{}를 그룹에서 찾기 시작. ",tagKey);

        if(groupContentMap.containsKey(tagKey)) {
            Dictionary.LOGGER.warn("[그룹 찾기]{}를 그룹에서 찾았습니다.. 해당 그룹의 값: {}",tagKey, groupContentMap.get(tagKey));
            return groupContentMap.get(tagKey);
        }
        else {
            Dictionary.LOGGER.warn("[그룹 찾기]{}를 찾지 못했습니다 대신 ETC를 반환합니다. 그룹에 있는 콘텐츠 맵{}",tagKey, groupContentMap.get("ETC").getGroupContentMap());
            return groupContentMap.get("ETC");
        }

    }
    public TreeMap<String, T> getGroupMap() {
        return groupContentMap;
    }
    @SerializedName("설명 대체")
    private boolean canReplace=true;

    @SerializedName("태그")
    public String tag;
    //public transient IEnumTag tag;

    @Override
    public String toString() {
        return "BaseSubData{" +
                "subDictionary='" + subDictionary + '\'' +
                ", groupContentMap=" + groupContentMap +
                ", canReplace=" + canReplace +
                ", tag=" + tag +
                '}';
    }
}
