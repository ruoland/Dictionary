package org.ruoland.dictionary.dictionary.dictionary;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class DictionaryCommand {

    private DictionaryCommand() {

    }
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("dic").executes(DictionaryCommand::execute));
    }

    public static int execute(CommandContext<CommandSourceStack> context){
        if(context.getSource().isPlayer()){
            Player player = context.getSource().getPlayer();

            player.sendSystemMessage(Component.literal("안녀여여엉"));
        }
        return Command.SINGLE_SUCCESS;
    }
}