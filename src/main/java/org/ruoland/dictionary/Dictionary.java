package org.ruoland.dictionary;

import eu.pb4.playerdata.api.PlayerDataApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.client.DictionaryClient;
import org.ruoland.dictionary.dictionary.dictionary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Dictionary implements ModInitializer {
    public static final String MOD_ID = "dictionary";
    public static final String VERSION = "1.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            LangManager.loadLanguageMap();
            FileManager.getInstance().init();
            try {
                ItemManager.loadMinecraftItems();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            for(ServerPlayer player : server.getPlayerList().getPlayers())
                PlayerDataApi.setCustomDataFor(player, DictionaryClient.DICTIONARY_DATA, new CompoundTag());

            TagManager.getTagManager().loadTag();

            TagManager.getTagManager().sortTag();
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            for (Player player : server.getPlayerList().getPlayers()) {
                Inventory inventory = player.getInventory();

                for (ItemStack itemStack : inventory.items) {
                    PlayerDictionaryManager.addNewItem((ServerPlayer) player, itemStack.getItem());
                }
            }
            try {
                TagManager.getTagManager().saveTag();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


    }
}
