package org.ruoland.dictionary.dictionary;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.ruoland.dictionary.dictionary.dictionary.TagManager;

import java.util.function.Supplier;

public class DictionaryCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("dictionary")
            .then(Commands.literal("reload")
                .requires(source -> source.hasPermission(2)) // OP 레벨 2 이상 필요
                .executes(context -> {
                    try {
                        TagManager.getTagManager().loadTag();
                        context.getSource().sendSuccess((Supplier<Component>) Component.literal("Dictionary reloaded successfully."), true);
                    } catch (Exception e) {
                        context.getSource().sendFailure(Component.literal("Failed to reload dictionary: " + e.getMessage()));
                        e.printStackTrace();
                    }
                    return Command.SINGLE_SUCCESS;
                })
            )
        );
    }
}