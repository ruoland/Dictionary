package org.ruoland.dictionary.dictionary.dictionary;

import org.jetbrains.annotations.Nullable;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.DefaultDictionary;

import java.util.HashMap;

public class Var {
    HashMap<String, String> varMap = new HashMap<>();


    public String get(String keyword){
        if(varMap.containsKey(keyword))
            return varMap.get(keyword);
        throw new NullPointerException();
    }




    public static final class VarBuilder {
        private boolean isEnchantable;
        private boolean canStackable;
        private boolean isGrow;
        private boolean canBonemeal;
        private int maxDamage;
        private int maxStackSize;
        private int defense;
        private boolean isFuel;

        private VarBuilder() {
        }

        public static VarBuilder varBuilder() {
            return new VarBuilder();
        }

        public VarBuilder isEnchantable(boolean canEnchanted) {
            this.isEnchantable = canEnchanted;
            return this;
        }

        public VarBuilder canDestroy(boolean canDestroy) {
            this.canDestroy = canDestroy;
            return this;
        }

        public VarBuilder canStackable(boolean canStackable) {
            this.canStackable = canStackable;
            return this;
        }

        public VarBuilder isGrow(boolean isGrow) {
            this.isGrow = isGrow;
            return this;
        }

        public VarBuilder canBonemeal(boolean canBonemeal) {
            this.canBonemeal = canBonemeal;
            return this;
        }

        public VarBuilder maxDamage(int maxDamage) {
            this.maxDamage = maxDamage;
            return this;
        }

        public VarBuilder maxStackSize(int maxStackSize) {
            this.maxStackSize = maxStackSize;
            return this;
        }

        public VarBuilder defense(int defense) {
            this.defense = defense;
            return this;
        }

        public Var build() {
            Var var = new Var();
            var.varMap.put("%stackable%", DefaultDictionary.CAN_STACKABLE+canStackable);
            var.varMap.put("%maxDamage%", DefaultDictionary.MAX_DAMAGE + maxDamage);
            var.varMap.put("%growable%", DefaultDictionary.CAN_GROW + isGrow);
            var.varMap.put("%maxStackSize%", DefaultDictionary.MAX_STACK_SIZE+maxStackSize);
            var.varMap.put("%isEnchantable%", DefaultDictionary.ENCHANTABLE + isEnchantable);
            var.varMap.put("%canBonemeal%", DefaultDictionary.BONEMEAL+canBonemeal);
            var.varMap.put("%defense%", DefaultDictionary.DEFENSE + defense);
            var.varMap.put("%isFuel%", DefaultDictionary.FUEL + isFuel);

            return var;
        }

        public VarBuilder isFuel(boolean fuel) {
            this.isFuel = fuel;
            return this;
        }
    }
}