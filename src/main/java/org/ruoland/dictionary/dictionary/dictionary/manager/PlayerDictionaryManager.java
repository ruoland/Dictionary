package org.ruoland.dictionary.dictionary.dictionary.manager;

import eu.pb4.playerdata.api.PlayerDataApi;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import org.ruoland.dictionary.client.DictionaryClient;

public class PlayerDictionaryManager {

    public static void addNewItem(ServerPlayer player, Item item){

            getDictionaryTag(player).putBoolean(item.getDescriptionId(), true);

    }

    public static boolean hasItem(ServerPlayer player, Item item){
        return getDictionaryTag(player).getBoolean(item.getDescriptionId());
    }

    private static CompoundTag getDictionaryTag(ServerPlayer player){

        CompoundTag playerTag = PlayerDataApi.getCustomDataFor(player, DictionaryClient.DICTIONARY_DATA);
        if(playerTag == null)
            return new CompoundTag();
        CompoundTag dictonaryTag = playerTag.contains("dictionary") ? playerTag.getCompound("dictionary") : new CompoundTag();
        playerTag.put("dictionary", dictonaryTag);
        dictonaryTag = playerTag.getCompound("dictionary");
        return dictonaryTag;
    }

    public static void saveCompoundTag(ServerPlayer serverPlayer){

        Tag tag = PlayerDataApi.getCustomDataFor(serverPlayer, DictionaryClient.DICTIONARY_DATA);
        System.out.println("저장");
    }
}