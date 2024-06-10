package org.ruoland.dictionary.dictionary.dictionary.itemcontent;

import java.util.EnumMap;

public enum EnumTag {
    ANIMAL(DefaultDictionary.ANIMAL, "rabbit","chicken", "cow", "dog", "cat", "villager", "polarbear", "panda", "sheep", "papago"),
    ARMOR(DefaultDictionary.ARMOR, "helmet", "chestplate", "leggings", "boots", "armor"),
    TOOLS(DefaultDictionary.TOOLS, "compass", "axe", "pickaxe", "shovel", "hoe", "bucket"),
    FOOD(DefaultDictionary.FOOD, "beef", "cookie", "cake", "porkchop", "cooked", "bread", "apple", "chicken", "stew"),
    FENCE(DefaultDictionary.FENCE, "fence", "gate", "wall"),
    BANNER(DefaultDictionary.BANNER, "banner", "pattern"),
    WOOD(DefaultDictionary.WOOD, "wood", "log", "leaves", "planks"),
    COLOR_BLOCK(DefaultDictionary.COLOR_BLOCK, "glass", "pane", "terracotta", "carpet", "wool", "concrete", "powder"),
    CORAL(DefaultDictionary.CORAL, "coral", "fan"),
    NETHER(DefaultDictionary.NETHER, "netherrack", "crimson", "warped", "ghast", "blaze", "nether", "glowstone"),
    MUSIC(DefaultDictionary.MUSIC, "music", "disc"),
    FARM(DefaultDictionary.FARM, "seeds", "beetroot", "wheat", "berries", "potato", "carrot", "melon", "hay", "cane"),
    SAPLING(DefaultDictionary.SAPLING, "sapling"),
    DECO(DefaultDictionary.DECO, "sign", "slab", "stairs", "bed", "candle", "book"),
    DOOR(DefaultDictionary.DOOR, "door", "trapdoor"),
    COPPER(DefaultDictionary.COPPER, "copper", "oxidized", "weathered", "exposed"),
    ORE(DefaultDictionary.ORE, "ore", "raw", "ingot", "nugget", "emerald", "coal"),
    DYE(DefaultDictionary.DYE, "dye"),
    FISH(DefaultDictionary.FISH, "salmon", "fish", "pufferfish"),
    SHERD(DefaultDictionary.SHERD, "sherd"),
    SWORD(DefaultDictionary.SWORD, "sword"),
    RIDING(DefaultDictionary.RIDING, "boat", "saddle"),
    MINECART(DefaultDictionary.MINECART, "minecart", "rail"),
    REDSTONE(DefaultDictionary.REDSTONE, "button", "plate", "redstone", "lever", "dispenser", "dropper", "observer", "hopper", "piston", "repeater", "daylight", "lilac","lily"),
    SANDSTONE("sandstone", "sand"),
    FLOWER("tulip", "daisy", "flower", "dandelion", "bush", "sunflower", "Allium", "Poppy", "Orchid", "Azure", "cornflower", "lily", "lilac", "rose", "peony"),
    DIRT("dirt", "grass", "gravel", "podzol"),
    POLISHED("polished"),
    POTION("potion"),
    TEMPLATE("template"),
    ORE_BLOCK("redstone_block", "lapis_block", "gold_block", "diamond_block", "copper_block"),
    ETC, ENDER("chorus", "box", "ender", "chorus_fruit"), SPECIAL(), PROJECTILE(), MINING();

    private static final EnumMap<EnumTag, String> tagDictionary = new EnumMap<>(EnumTag.class);
    private final String[] strings;
    private ItemsTag itemsTag;

    EnumTag(String... str){
        this.strings = str;
    }

    public boolean containsKey(String keyword){
        for(String key : strings){
            if(keyword.equals(key))
                return true;
        }
        return false;
    }

    public ItemsTag getItemTag(){
        if(itemsTag == null)
            itemsTag = new ItemsTag(this);
        return itemsTag;
    }

    public String getTagDictionary(){
        tagDictionary.put(ARMOR, "방어구입니다. 방어구는 가죽, 철, 금(내구성, 방어력 낮음), 다이아몬드, 네더라이트(최종 아이템) 아이템으" +
                "로 모자, 갑옷, 바지, 신발을 제작할 수 있으며, 세상을 모험하다 보면 사슬 갑옷도 얻을 수 있습니다.\\n" +
                "가죽 갑옷은 염료 아이템과 조합하여 염색할 수 있습니다. (가마솥에 물을 채우고, 우클릭 하면 원래대로 돌릴 수 있습니다)\\n" +
                "투구의 경우 방어력이 낮고, 거북 등딱지란 투구도 존재합니다.(거북 인갑으로 제작합니다.). \\n" +
                "갑옷은 방어력이 가장 높고, 또 재료도 많이 들어갑니다.\\n" +
                "바지의 경우 신속한 잠행이란 마법 부여를 가질 수 있습니다.(웅크린 상태에서 빠르게 이동하는 마법.)\\n" +
                " 신발의 경우 가벼운 착지나 물갈퀴, 차가운 걸음, 등 다양한 마법 부여를 가질 수 있습니다");

        tagDictionary.put(TOOLS, "게임에 필수적인 도구들입니다.");
        return "";


    }

}
