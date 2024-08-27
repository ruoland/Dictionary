package org.ruoland.dictionary.dictionary.dictionary;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.entitycontent.EntityTag;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemGroupContent;

import java.util.ArrayList;
import java.util.Arrays;

public class VariableManager {

    public static String replaceVariable(ItemStack itemStack, String content) {
        if (itemStack == null) {
            content = content.replaceAll("%damage%", "(불러오지 못함)");
            content = content.replaceAll("%name%", "(불러오지 못함)");
            content = content.replaceAll("%maxStackSize%", "(불러오지 못함");
        } else {
            content = content.replaceAll("%damage%", "" + itemStack.getDamageValue());
            content = content.replaceAll("%name%", itemStack.getDisplayName().getString());
            content = content.replaceAll("%maxStackSize%", "" + itemStack.getMaxStackSize());
        }
        content = content.replaceAll("%groupName%", TagManager.getTagManager().getItemGroup(itemStack).getGroupName());
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
