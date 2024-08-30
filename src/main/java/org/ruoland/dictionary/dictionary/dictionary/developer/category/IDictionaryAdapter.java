package org.ruoland.dictionary.dictionary.dictionary.developer.category;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import org.ruoland.dictionary.dictionary.dictionary.biome.BiomeContent;
import org.ruoland.dictionary.dictionary.dictionary.entity.EntityContent;
import org.ruoland.dictionary.dictionary.dictionary.entity.EnumEntityTag;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.manager.DataManager;

public interface IDictionaryAdapter<T, U extends BaseContent> {
    public String getID();
    String getType();
    T get();
    U create();
    public class LivingEntityAdapter implements  IDictionaryAdapter<EntityType, EntityContent>{
        EntityType entityType;
        EnumEntityTag tag;
        public LivingEntityAdapter(EntityType livingEntity) {
            this.entityType = livingEntity;
        }

        @Override
        public String getID() {
            return entityType.getDescriptionId();
        }

        @Override
        public EntityType get() {
            return entityType;
        }

        @Override
        public EntityContent create() {
            return new EntityContent(entityType);
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
            return DataManager.getBiomeNameById("");
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
