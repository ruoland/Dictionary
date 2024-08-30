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

    public T getGroup(String id){
        String tagKey = TagManager.getTagManager().getCutID(id);
        Dictionary.LOGGER.warn("[그룹 찾기]{}를 그룹에서 찾기 시작. ",tagKey);

        if(groupContentMap.containsKey(tagKey)) {
            Dictionary.LOGGER.warn("[그룹 찾기]{}를 그룹에서 찾았습니다.. 해당 그룹의 값: {}",tagKey, groupContentMap.get(tagKey));
            return groupContentMap.get(tagKey);
        }
        else if(groupContentMap.get("ETC") != null){
            Dictionary.LOGGER.warn("[그룹 찾기]{}를 찾지 못했습니다 대신 ETC를 반환합니다. 그룹에 있는 콘텐츠 맵{}",tagKey, groupContentMap.get("ETC").getContentMap());
            return groupContentMap.get("ETC");
        }
        else
            Dictionary.LOGGER.warn("[그룹 찾기]그룹 자체가 비어있는 것처럼 보입니다. 그룹에 있는 콘텐츠 맵{}",tagKey, groupContentMap);
        return null;

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
