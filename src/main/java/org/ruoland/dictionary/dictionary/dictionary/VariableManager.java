package org.ruoland.dictionary.dictionary.dictionary;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemGroupContent;

import java.util.ArrayList;
import java.util.Arrays;

public class VariableManager {

    public static String replaceVariable(ItemStack itemStack, String content){
        content = content.replaceAll("%damage%", "" + itemStack.getDamageValue());
        content = content.replaceAll("%name%", itemStack.getDisplayName().getString());
        content = content.replaceAll("%maxStackSize%", "" + itemStack.getMaxStackSize());

        return content;
    }

    public static ArrayList<ItemGroupContent> getGroupFromId(String content){
        ArrayList<ItemGroupContent> arrayList = new ArrayList<>();
        while(content.contains("%id:")){
            ItemGroupContent groupContent =TagManager.getTagManager().findGroupByItemID(cutVarId(content,false));
            String cut = cutVarId(content, true);
            arrayList.add(groupContent);
            content = content.replace(cut, groupContent.getGroupName());

        }
        return arrayList;
    }

    /**
     * @param var 를 전달받아서 var 에 있는 세부 데이터를 분리함
     *
     */
    private static void cutVarData(String var){
        if(var.split("\\.").length > 2){
            return;
        }

        else{
            //아이템 그룹 찾기 %redstone.lever%
            String[] itemGroups = cutVarId(var, false).split("\\.");
            String groupID =itemGroups[0];
            String item = itemGroups[1];

            var = var.replace(cutVarId(var, true), "");
            //아이템 요청 찾기 %id:redstone.lever%/%fuel%

            ItemContent itemContent = TagManager.getTagManager().findGroupByItemID(groupID).getContentMap().get(item);
            if (var.contains("/%")) {
                String[] itemRequests = var.split("/%");
                System.out.println("변수 감지 "+Arrays.toString(itemRequests));
                Var variable = buildVariable(itemContent.getItemStack());
                for (int i = 0; i < itemRequests.length; i++) {
                    if (variable.varMap.containsKey(itemRequests[i])) {
                        System.out.println("맵 "+variable.varMap.get(itemRequests[i]));
                        var = var.replace("/%" + itemRequests[i], "" + variable.varMap.get(itemRequests[i]));
                    }
                }
            }
        }
    }

    private static Var buildVariable(ItemStack itemStack){
        Var.VarBuilder variable = Var.VarBuilder.varBuilder();
        variable.isEnchantable(itemStack.isEnchantable());//인챈트 가능?
        variable.canStackable(itemStack.isStackable());//여러개 집기 가능?
        variable.maxDamage(itemStack.getMaxDamage());//내구성 얼마나?
        variable.maxStackSize(itemStack.getMaxStackSize());//몇개까지?
        variable.isFuel(AbstractFurnaceBlockEntity.isFuel(itemStack)); //연료로 사용 가능?
        variable.canEat(itemStack.getItem().components().has(DataComponents.FOOD)); //연료로 사용 가능?

        if (itemStack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof BonemealableBlock bonemealableBlock) {
                variable.isGrow(true);
                variable.canBonemeal(true);
            }
        }
        if (itemStack.getItem() instanceof ArmorItem armorItem) {
            variable.defense(armorItem.getDefense());
        }
        return variable.build();
    }

    public static String cutVarId(String content, boolean fullID){
        int id = content.indexOf("%id:");
        String variableId = content.substring(id, content.indexOf("%", id+1)+1);

        if(!fullID)
            variableId = variableId.replace("%id:", "").replace("%", "");


        return variableId;
    }


}
