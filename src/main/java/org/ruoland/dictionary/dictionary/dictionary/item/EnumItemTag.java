package org.ruoland.dictionary.dictionary.dictionary.item;

import org.ruoland.dictionary.dictionary.dictionary.developer.category.IEnumTag;

public enum EnumItemTag implements IEnumTag {

    ARMOR(DefaultDictionary.ARMOR_NAME, "helmet", "chestplate", "leggings", "boots", "armor"),
    TOOLS(DefaultDictionary.TOOLS_NAME, "compass", "axe", "pickaxe", "shovel", "hoe", "bucket", "Clock", "Flint and Steel","Elytra", "Spyglass"),
    FOOD(DefaultDictionary.FOOD_NAME, "beef", "cookie", "cake", "porkchop", "cooked", "bread", "apple", "chicken", "stew"),
    FENCE(DefaultDictionary.FENCE_NAME, "fence", "gate", "wall"),
    BANNER(DefaultDictionary.BANNER_NAME, "banner", "pattern"),
    WOOD(DefaultDictionary.WOOD_NAME, "wood", "log", "leaves", "planks"),
    COLOR_BLOCK(DefaultDictionary.COLOR_BLOCK_NAME, "glass", "pane", "terracotta", "carpet", "wool", "concrete", "powder"),
    CORAL(DefaultDictionary.CORAL_NAME, "coral", "fan"),
    NETHER(DefaultDictionary.NETHER_NAME, "netherrack", "crimson", "warped", "ghast", "blaze", "nether", "glowstone"),
    MUSIC(DefaultDictionary.MUSIC_NAME, "music", "disc"),
    FARM(DefaultDictionary.FARM_NAME, "seeds", "beetroot", "wheat", "berries", "potato", "carrot", "melon", "hay", "cane"),
    SAPLING(DefaultDictionary.SAPLING_NAME, "sapling"),
    DECO(DefaultDictionary.DECO_NAME, "sign", "slab", "stairs", "bed", "candle", "book"),
    DOOR(DefaultDictionary.DOOR_NAME, "door", "trapdoor"),
    COPPER(DefaultDictionary.COPPER_NAME, "copper", "oxidized", "weathered", "exposed"),
    ORE(DefaultDictionary.ORE_NAME, "ore", "raw", "ingot", "nugget", "emerald", "coal"),
    DYE(DefaultDictionary.DYE_NAME, "dye"),
    FISH(DefaultDictionary.FISH_NAME, "salmon", "fish", "pufferfish"),
    SHERD(DefaultDictionary.SHERD_NAME, "sherd"),
    SWORD(DefaultDictionary.SWORD_NAME, "sword"),
    RIDING(DefaultDictionary.RIDING_NAME, "boat", "saddle"),
    MINECART(DefaultDictionary.MINECART_NAME, "minecart", "rail"),
    REDSTONE(DefaultDictionary.REDSTONE_NAME, "button", "plate", "redstone", "lever", "dispenser", "dropper", "observer", "hopper", "piston", "repeater", "daylight", "lilac","lily"),
    SANDSTONE(DefaultDictionary.SANDSTONE_NAME, "sandstone", "sand"),
    FLOWER(DefaultDictionary.FLOWER_NAME, "tulip", "daisy", "flower", "dandelion", "bush", "sunflower", "Allium", "Poppy", "Orchid", "Azure", "cornflower", "lily", "lilac", "rose", "peony"),
    DIRT(DefaultDictionary.DIRT_NAME, "dirt", "grass", "gravel", "podzol"),
    POLISHED(DefaultDictionary.POLISHED_NAME, "polished"),
    POTION(DefaultDictionary.POTION_NAME, "potion"),
    TEMPLATE(DefaultDictionary.TEMPLATE_NAME,"template"),
    ORE_BLOCK(DefaultDictionary.ORE_BLOCK_NAME, "redstone_block", "lapis_block", "gold_block", "diamond_block", "copper_block"),
    ETC(DefaultDictionary.ETC_NAME),
    ENDER(DefaultDictionary.ENDER_NAME, "chorus", "box", "ender", "chorus_fruit"),
    SPECIAL(DefaultDictionary.SPECIAL_NAME),
    PROJECTILE(DefaultDictionary.PROJECTILE_NAME),
    MINING(DefaultDictionary.MINING_NAME);

    private final String[] strings;
    private ItemsTag itemsTag;

    EnumItemTag(String... str){
        this.strings = str;
    }

    public boolean containsKey(String keyword){
        for(String key : strings){
            if(keyword.equals(key))
                return true;
        }
        return false;
    }

    @Override
    public EnumItemTag value(String tags) {
        return valueOf(tags);
    }

    public ItemsTag getItemTag(){
        if(itemsTag == null)
            itemsTag = new ItemsTag(this);
        return itemsTag;
    }


}
