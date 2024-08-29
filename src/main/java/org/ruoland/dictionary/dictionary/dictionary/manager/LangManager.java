package org.ruoland.dictionary.dictionary.dictionary.manager;


import com.google.common.collect.ImmutableMap;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class LangManager {
    private static Map<String, String> languageMap = new HashMap<>();


    public static String getEnglishName(String id){
        if(languageMap.isEmpty())
            loadLanguageMap();
        if(id == null)
            return "?";

        return languageMap.get(id);

    }
    public static String getBiomeNameKor(String location){
        return I18n.get(location);
    }
    /**
     * init 메서드가 실행 될 때 실행해야 합니다!
     */
    public static void loadLanguageMap(){
        try {
            Method tableField = Language.class.getDeclaredMethod("parseTranslations", BiConsumer.class, String.class);
            tableField.setAccessible(true);
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            Objects.requireNonNull(builder);
            BiConsumer<String, String> biConsumer = builder::put;
            tableField.invoke(Language.getInstance(), biConsumer, "/assets/minecraft/lang/en_us.json");
            languageMap = builder.build();
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
