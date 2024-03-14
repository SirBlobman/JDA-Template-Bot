package com.github.sirblobman.discord.template.command;

import com.github.sirblobman.discord.template.DiscordBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public final class CommandHelloWorld extends SlashCommand {
    public CommandHelloWorld(@NotNull DiscordBot discordBot) {
        super(discordBot);
    }

    @Override
    public @NotNull CommandData getCommandData() {
        return Commands.slash("hello-world", "Hello World!");
    }

    @Override
    public @NotNull MessageCreateData execute(@NotNull SlashCommandInteractionEvent e) {
        Member member = e.getMember();
        if (member == null) {
            return MessageCreateData.fromContent("Only server members can execute this command.");
        }

        return MessageCreateData.fromContent("Hello " + member.getAsMention() + " !");
    }
}
