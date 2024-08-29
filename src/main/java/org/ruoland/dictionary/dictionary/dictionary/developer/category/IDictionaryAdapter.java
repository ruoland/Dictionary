package org.ruoland.dictionary.dictionary.dictionary.developer.category;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import org.ruoland.dictionary.dictionary.dictionary.biome.BiomeContent;
import org.ruoland.dictionary.dictionary.dictionary.entity.EntityContent;
import org.ruoland.dictionary.dictionary.dictionary.entity.EnumEntityTag;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.manager.ContentManager;

public interface IDictionaryAdapter<T, U extends BaseContent> {
    public String getID();
    String getType();
    T get();
    U create();
    public class LivingEntityAdapter implements  IDictionaryAdapter<LivingEntity, EntityContent>{
        LivingEntity livingEntity;
        EnumEntityTag tag;
        public LivingEntityAdapter(LivingEntity livingEntity) {
            this.livingEntity = livingEntity;
        }

        @Override
        public String getID() {
            return livingEntity.getType().getDescriptionId();
        }

        @Override
        public LivingEntity get() {
            return livingEntity;
        }

        @Override
        public EntityContent create() {
            return new EntityContent(livingEntity);
        }

        @Override
        public String getType() {
            return "Entity";
        }
    }

    public class ItemStackAdapter implements  IDictionaryAdapter<ItemStack, ItemContent>{
        ItemStack itemStack;

        public ItemStackAdapter(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public String getID() {
            return itemStack.getDescriptionId();
        }
        public String getType(){
            return "ItemStack";
        }

        @Override
        public ItemStack get() {
            return itemStack;
        }

        @Override
        public ItemContent create() {
            return new ItemContent(itemStack);
        }
    }

    class BiomeAdapter implements IDictionaryAdapter<Biome, BiomeContent>{
        Biome biome;

        public BiomeAdapter(Biome biome) {
            this.biome = biome;
        }

        @Override
        public String getID() {
            //TODO 일단 바이옴 저장관리부터 구현해야 가능할듯
            return ContentManager.getBiomeNameById("");
        }
        public String getType(){
            return "Biome";
        }

        @Override
        public Biome get() {
            return biome;
        }

        @Override
        public BiomeContent create() {
            //TODO
            return new BiomeContent("");
        }
    }

}
