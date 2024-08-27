package org.ruoland.dictionary.dictionary.dictionary;


import com.google.common.collect.ImmutableMap;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class LangManager {
    private static Map<String, String> languageMap = new HashMap<>();

    public static String getEnglishName(ItemStack itemStack){
        if(languageMap.isEmpty())
            loadLanguageMap();
        if(itemStack == null)
            return "?";
        return languageMap.get(itemStack.getDescriptionId());

    }
    public static String getBiomeName(String location){
        if(languageMap.isEmpty())
            loadLanguageMap();
        if(location == null)
            return "?";

        return languageMap.get(location);

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
            final Map<String, String> map = builder.build();
            languageMap = map;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
