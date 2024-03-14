package com.github.sirblobman.discord.template.command;

import com.github.sirblobman.discord.template.DiscordBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SlashCommand {
    private final DiscordBot discordBot;

    public SlashCommand(@NotNull DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    protected final @NotNull DiscordBot getDiscordBot() {
        return this.discordBot;
    }

    public boolean isEphemeral() {
        return false;
    }

    public void onAutoComplete(@NotNull CommandAutoCompleteInteraction e) {
        // Do Nothing
    }

    public abstract @NotNull CommandData getCommandData();
    public abstract @Nullable MessageCreateData execute(@NotNull SlashCommandInteractionEvent e);
}
