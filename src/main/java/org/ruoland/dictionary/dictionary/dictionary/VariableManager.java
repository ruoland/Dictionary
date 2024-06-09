package org.ruoland.dictionary.dictionary.dictionary;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemContent;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class VariableManager {

    public static String getVariable(ItemStack itemStack, String content){
        content = content.replaceAll("%damage%", "" + itemStack.getDamageValue());
        content = content.replaceAll("%name%", "" + itemStack.getDisplayName().getString());
        content = content.replaceAll("%maxStackSize%", "" + itemStack.getMaxStackSize());
        while(content.contains("%id:")){
            String cut = cutVarId(content, true);
            String value = TagManager.getTagManager().findGroupByItemID(cutVarId(content,false)).getGroupName();

            content = content.replace(cut,  value);

        }
        return content;
    }


    /**
     * @param var 를 전달받아서 var 에 있는 세부 데이터를 분리함
     *
     */
    private static void cutVarData(String var){
        if(!var.contains("/")){
            return;
        }

        else{
            String[] split = var.split("/");
            String groupID =split[0];
            String item = split[1];
            String request = split[2];
            ItemContent itemContent = TagManager.getTagManager().findGroupByItemID(groupID).getContentMap().get(item);
            ItemStack itemStack =  itemContent.getItemStack();
            boolean canEnchanted = itemStack.isEnchantable();//인챈트 가능?

            boolean canDestroy = itemStack.isDamageableItem();// 파괴 가능?
            boolean canStackable = itemStack.isStackable();//여러개 집기 가능?
            boolean isGrow = false;
            boolean canBonemeal;
            int maxDamage = itemStack.getMaxDamage();//내구성 얼마나?
            int maxStackSize = itemStack.getMaxStackSize();//몇개까지?

            int defense = 0;
            if(itemStack.getItem() instanceof BlockItem blockItem){
                if(blockItem.getBlock() instanceof BonemealableBlock bonemealableBlock){
                        isGrow = true;
                        canBonemeal = true;
                }
            }
            if(itemStack.getItem() instanceof ArmorItem armorItem){
                defense = armorItem.getDefense();
                
            }

        }
    }
    private static String cutVarId(String content, boolean fullID){
        int id = content.indexOf("%id:");
        String variableId = content.substring(id, content.indexOf("%", id+1)+1);

        if(!fullID)
            variableId = variableId.replace("%id:", "").replace("%", "");

        System.out.println("???" +variableId);
        return variableId;
    }


}
