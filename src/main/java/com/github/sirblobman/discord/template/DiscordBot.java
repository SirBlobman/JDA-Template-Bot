package com.github.sirblobman.discord.template;

import com.github.sirblobman.discord.template.command.CommandHelloWorld;
import com.github.sirblobman.discord.template.command.ListenerSlashCommands;
import com.github.sirblobman.discord.template.command.SlashCommand;
import com.github.sirblobman.discord.template.command.SlashCommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public final class DiscordBot {
    private final Logger logger;
    private final SlashCommandManager slashCommandManager;
    private JDA discordApi;

    public DiscordBot(@NotNull Logger logger) {
        this.logger = logger;
        this.slashCommandManager = new SlashCommandManager(this);
    }

    public @NotNull Logger getLogger() {
        return this.logger;
    }

    public @NotNull SlashCommandManager getSlashCommandManager() {
        return this.slashCommandManager;
    }

    public @NotNull JDA getDiscordApi() {
        return this.discordApi;
    }

    public void onLoad() {Logger logger = getLogger();
        logger.info("Loading Template Bot...");

        if (!setupDiscordApi()) {
            return;
        }

        setupShutdownHook();

        logger.info("Finished loading Template Bot.");
        onEnable();
    }

    private void onEnable() {
        Logger logger = getLogger();
        logger.info("Enabling Template Bot...");

        registerDiscordSlashCommands();
        registerListeners();

        logger.info("Successfully enabled Template Bot.");
    }

    private void onDisable() {
        JDA discordApi = getDiscordApi();
        discordApi.shutdownNow();
    }

    /**
     * @return {@code true} for successful API token and login. Otherwise {@code false}.
     */
    private boolean setupDiscordApi() {
        Logger logger = getLogger();
        String botToken = System.getenv("DISCORD_BOT_TOKEN");
        if (botToken == null || botToken.equalsIgnoreCase("<none>")) {
            logger.error("Missing or invalid environment variable for Discord Bot token.");
            return false;
        }

        JDABuilder apiBuilder = JDABuilder.createDefault(botToken);
        Activity activity = Activity.playing("Minecraft: Java Edition");
        apiBuilder.setActivity(activity);

        try {
            JDA discordApi = apiBuilder.build(); // Build API (Log in)
            this.discordApi = discordApi.awaitReady();
            logger.info("Successfully logged in.");

            List<String> scopeList = List.of("bot", "applications.commands"); // Add extra scopes
            discordApi.setRequiredScopes(scopeList);

            List<Permission> permissionList = List.of(Permission.ADMINISTRATOR); // Customize permissions
            String inviteURL = discordApi.getInviteUrl(permissionList);
            logger.info("Invite URL: " + inviteURL);
            return true;
        } catch (InvalidTokenException | IllegalArgumentException | InterruptedException | IllegalStateException ex) {
            logger.error("An error occurred while trying to login to Discord:", ex);
            return false;
        }
    }

    private void setupShutdownHook() {
        Thread shutdownThread = new Thread(this::onDisable);
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(shutdownThread);

        Logger logger = getLogger();
        logger.info("Successfully setup shutdown hook.");
    }

    private void registerDiscordSlashCommands() {
        SlashCommandManager slashCommandManager = getSlashCommandManager();
        slashCommandManager.registerCommands(CommandHelloWorld.class);

        Set<SlashCommand> commandSet = slashCommandManager.getDiscordSlashCommandSet();
        List<CommandData> commandDataList = commandSet.parallelStream().map(SlashCommand::getCommandData).toList();

        JDA discordAPI = getDiscordApi();
        Guild guild = discordAPI.getGuildById("<id>"); // Discord server that will have access to the commands.
        if (guild != null) {
            CommandListUpdateAction action = guild.updateCommands();
            action.addCommands(commandDataList).queue();
        }
    }

    private void registerListeners() {
        JDA discordApi = getDiscordApi();
        discordApi.addEventListener(new ListenerSlashCommands(this));
    }
}
