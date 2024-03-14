package com.github.sirblobman.discord.template.command;

import com.github.sirblobman.discord.template.DiscordBot;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public final class ListenerSlashCommands extends ListenerAdapter {
    private final DiscordBot discordBot;

    public ListenerSlashCommands(@NotNull DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    private @NotNull DiscordBot getDiscordBot() {
        return this.discordBot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        DiscordBot discordBot = getDiscordBot();
        SlashCommandManager slashCommandManager = discordBot.getSlashCommandManager();
        InteractionHook interaction = e.getHook();

        String commandName = e.getName();
        SlashCommand command = slashCommandManager.getCommand(commandName);
        if (command == null) {
            e.reply("Unknown command '/" + commandName + "'.").queue();
            return;
        }

        boolean ephemeral = command.isEphemeral();
        e.deferReply(ephemeral).queue();

        MessageCreateData message = command.execute(e);
        if (message != null) {
            interaction.sendMessage(message).queue();
        } else {
            interaction.sendMessage("Done.").queue();
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent e) {
        DiscordBot discordBot = getDiscordBot();
        SlashCommandManager commandManager = discordBot.getSlashCommandManager();

        String commandName = e.getName();
        SlashCommand command = commandManager.getCommand(commandName);
        if (command == null) {
            return;
        }

        command.onAutoComplete(e);
    }
}
