package com.github.sirblobman.discord.template.command;

import com.github.sirblobman.discord.template.DiscordBot;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SlashCommandManager {
    private final DiscordBot discordBot;
    private final Map<String, SlashCommand> commandMap = new HashMap<>();

    public SlashCommandManager(@NotNull DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    private @NotNull DiscordBot getDiscordBot() {
        return this.discordBot;
    }

    public @Nullable SlashCommand getCommand(@NotNull String commandName) {
        if (commandName.isBlank()) {
            return null;
        }

        String lowercase = commandName.toLowerCase();
        return this.commandMap.getOrDefault(lowercase, null);
    }

    public @NotNull Set<SlashCommand> getDiscordSlashCommandSet() {
        Collection<SlashCommand> valueColl = this.commandMap.values();
        return Set.copyOf(valueColl);
    }

    @SafeVarargs
    public final void registerCommands(Class<? extends SlashCommand>... commandClassArray) {
        for (Class<? extends SlashCommand> commandClass : commandClassArray) {
            registerCommand(commandClass);
        }
    }

    private void registerCommand(Class<? extends SlashCommand> commandClass) {
        try {
            DiscordBot discordBot = getDiscordBot();
            Constructor<? extends SlashCommand> constructor = commandClass.getConstructor(DiscordBot.class);
            SlashCommand command = constructor.newInstance(discordBot);

            CommandData commandData = command.getCommandData();
            String commandName = commandData.getName();
            this.commandMap.put(commandName, command);
        } catch (ReflectiveOperationException ex) {
            Logger logger = getDiscordBot().getLogger();
            logger.error("Failed to register a slash command:", ex);
        }
    }
}
