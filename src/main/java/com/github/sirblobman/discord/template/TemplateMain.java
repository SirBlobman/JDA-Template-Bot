package com.github.sirblobman.discord.template;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public final class TemplateMain {
    public static void main(String @NotNull ... args) {
        Logger logger = LogManager.getLogger("Template Bot");
        DiscordBot discordBot = new DiscordBot(logger);
        discordBot.onLoad();
    }
}
