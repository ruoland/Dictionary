package org.ruoland.dictionary.dictionary.dictionary.manager;

import net.minecraft.world.item.ItemStack;

public class VariableManager {

    public static String replaceVariable(ItemStack itemStack, String content) {
        if(content == null)
            return null;

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
