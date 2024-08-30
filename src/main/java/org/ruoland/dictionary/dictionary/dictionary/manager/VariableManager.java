package org.ruoland.dictionary.dictionary.dictionary.manager;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IDictionaryAdapter;

public class VariableManager {

    public static String replaceVariable(IDictionaryAdapter adapter, @Nullable String content) {
        if(content == null)
            return null;

        if(adapter instanceof IDictionaryAdapter.ItemStackAdapter itemStackAdapter)
            return itemStack(itemStackAdapter.get(), content);

        else if(adapter instanceof IDictionaryAdapter.LivingEntityAdapter livingEntityAdapter)
            return livingEntity(livingEntityAdapter.get(), content);

        else if(adapter instanceof IDictionaryAdapter.BiomeAdapter biomeAdapter)
            return biome(biomeAdapter.get(), content);

        return content;
    }
    /*
    TODO 엔티티 변수도 대응할 수 있게 해야 함
     */
    private static String livingEntity(EntityType type, String content){

        return content;
    }
    private static String biome(Biome biomeID, String content){

        return content;
    }
    private static String itemStack(ItemStack itemStack, String content){

        if (itemStack == null) {
            content = content.replaceAll("%damage%", "(불러오지 못함)");
            content = content.replaceAll("%name%", "(불러오지 못함)");
            content = content.replaceAll("%maxStackSize%", "(불러오지 못함");
        }
        else {
            content = content.replaceAll("%groupName%", TagManager.getTagManager().getItemGroup(itemStack).getGroupName());
            content = content.replaceAll("%damage%", "" + itemStack.getDamageValue());
            content = content.replaceAll("%name%", itemStack.getDisplayName().getString());
            content = content.replaceAll("%maxStackSize%", "" + itemStack.getMaxStackSize());
        }
        return content;
    }

    public static String cutVarId(String content, boolean fullID, boolean isGroup) {
        String prefix = isGroup ? "%group_id:" : "%id:";

        int id = content.indexOf(prefix);
        String variableId = content.substring(id, content.indexOf("%", id + prefix.length()) + 1);

        if (!fullID) {
            variableId = variableId.replace(prefix, "").replace("%", "");

        }

        return variableId;
    }



}
