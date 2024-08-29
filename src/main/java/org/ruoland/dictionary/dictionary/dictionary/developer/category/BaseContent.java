package org.ruoland.dictionary.dictionary.dictionary.developer.category;

import com.google.gson.annotations.SerializedName;
import org.ruoland.dictionary.dictionary.dictionary.manager.LangManager;

public abstract class BaseContent {
    @SerializedName(value = "아이디", alternate = {"아이템 아이디", "엔티티 아이디", "바이옴 아이디"})
    String id = "";
    @SerializedName(value = "영어 이름", alternate = {"아이템 영어 이름", "엔티티 영어 이름", "바이옴 영어 이름"})
    String englishName;

    public BaseContent(String id) {
        this.id = id;
        this.englishName = LangManager.getEnglishName(id);
        //Dictionary.LOGGER.info("ItemContent created for {}: description = {}", id, dictionary);
    }

    @SerializedName(value = "설명", alternate = {"아이템 설명", "엔티티 설명", "바이옴 설명"})
    protected String dictionary;

    public void setDictionary(String newDictionary) {
        this.dictionary = newDictionary;

    }

    public String getID(){
        return id;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getDictionary(){
        return dictionary;
    }

    @Override
    public String toString() {
        return "BaseContent{" +
                "id='" + id + '\'' +
                ", englishName='" + englishName + '\'' +
                ", dictionary='" + dictionary + '\'' +
                '}';
    }
}
