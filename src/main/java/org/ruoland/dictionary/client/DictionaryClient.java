package org.ruoland.dictionary.client;

import com.mojang.blaze3d.platform.InputConstants;
import eu.pb4.playerdata.api.PlayerDataApi;
import eu.pb4.playerdata.api.storage.NbtDataStorage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.lwjgl.glfw.GLFW;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.manager.TagManager;
import org.ruoland.dictionary.dictionary.gui.ContentScreen;
import org.ruoland.dictionary.dictionary.gui.ItemsTagScreen;

public class DictionaryClient implements ClientModInitializer {
    public static final NbtDataStorage DICTIONARY_DATA = new NbtDataStorage("dictionary");

    public static KeyMapping keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.dictionary.open", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_O, // The keycode of the key
            "category.dictionary.book" // The translation key of the keybinding's category.
    ));

    @Override
    public void onInitializeClient() {
        PlayerDataApi.register(DICTIONARY_DATA);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.screen == null) {

                if (InputConstants.isKeyDown(client.getWindow().getWindow(), InputConstants.KEY_O)) {
                    if(client.crosshairPickEntity instanceof LivingEntity livingEntity){
                        Dictionary.LOGGER.info("{} 가 감지 됐습니다.", client.crosshairPickEntity.getType());
                        ContentScreen entityScreen = new ContentScreen(null, livingEntity);
                        client.setScreen(entityScreen);
                    }
                    else {
                        ItemsTagScreen itemsTagScreen = new ItemsTagScreen(Component.literal("도감 종류"));
                        client.setScreen(itemsTagScreen);
                    }
                }

            }
        });
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            Minecraft mc = Minecraft.getInstance();
            if (InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_O)) {
                if (!(mc.screen instanceof ContentScreen)) {
                    Dictionary.LOGGER.info("아이템 클릭됨, {}", stack);
                    ContentScreen dictionary = new ContentScreen(mc.screen, stack, false);
                    String groupName = TagManager.getTagManager().getItemGroup(stack).getGroupName();
                    if(groupName == null) {
                        Dictionary.LOGGER.error("해당 아이템은 정보가 없는 것처럼 보입니다. (도감에서 그룹을 찾을 수 없음): {}", stack);
                        return;
                    }
                    //ItemsTagScreen subDataScreen = new ItemsTagScreen(Component.literal("asdf"));
                    mc.setScreen(dictionary);

                }
            }
        });


        ClientPickBlockGatherCallback.EVENT.register((player, result) -> {
            System.out.println(result.getType());
            return null;
        });

    }
}
