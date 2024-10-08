package org.ruoland.dictionary;

import eu.pb4.playerdata.api.PlayerDataApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.client.DictionaryClient;
import org.ruoland.dictionary.dictionary.dictionary.*;
import org.ruoland.dictionary.dictionary.dictionary.biome.BiomeTag;
import org.ruoland.dictionary.dictionary.dictionary.entitycontent.EntityTag;

import java.io.IOException;
import java.util.List;

public class Dictionary implements ModInitializer {
    public static final String MOD_ID = "dictionary";
    public static final String VERSION = "1.0";
    public static final DictionaryLogger LOGGER = new DictionaryLogger(Dictionary.class);

    @Override
    public void onInitialize() {


        ServerLifecycleEvents.SERVER_STARTING.register(server -> {

            LangManager.loadLanguageMap();
            Dictionary.LOGGER.info("언어 맵 불러오기 완료");
            FileManager.getInstance().init();
            Dictionary.LOGGER.info("파일 초기화 완료");

            try {
                ItemManager.loadMinecraftItems();
                Dictionary.LOGGER.info("모든 아이템 정보 불러오기 완료");
                EntityTag.load();
                BiomeTag.load();
                Dictionary.LOGGER.info("모든 엔티티 정보 불러오기 완료");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            for(ServerPlayer player : server.getPlayerList().getPlayers()) {
                PlayerDataApi.setCustomDataFor(player, DictionaryClient.DICTIONARY_DATA, new CompoundTag());
                Dictionary.LOGGER.info("플레이어 데이터 생성 완료");
            }

            TagManager.getTagManager().loadTag();
            Dictionary.LOGGER.info("태그 불러오기 완료");
            TagManager.getTagManager().sortTag();

            List<String> messages = DictionaryLogger.getErrorMessages();
            if(!messages.isEmpty())
                for(String message : messages){
                    server.getPlayerList().broadcastSystemMessage(Component.literal(message), false);
                    System.out.println(message+ " 에러 메세지 ㅈ출력");
                }
        });
        ServerLifecycleEvents.AFTER_SAVE.register((server, flush, force) -> {
            try {
                TagManager.getTagManager().saveTag();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            TagManager.getTagManager().sortTag();
            Dictionary.LOGGER.info("저장 전 태그 정렬 시작");
            for (Player player : server.getPlayerList().getPlayers()) {
                Inventory inventory = player.getInventory();

                for (ItemStack itemStack : inventory.items) {
                    PlayerDictionaryManager.addNewItem((ServerPlayer) player, itemStack.getItem());
                }
            }
            try {
                TagManager.getTagManager().saveTag();
                Dictionary.LOGGER.info("태그 저장 완료");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


    }
}
