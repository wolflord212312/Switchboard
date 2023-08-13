package dev.neuralnexus.tatercomms.common;

import dev.neuralnexus.tatercomms.common.discord.DiscordBot;
import dev.neuralnexus.tatercomms.common.listeners.player.CommonPlayerListener;
import dev.neuralnexus.tatercomms.common.listeners.server.CommonServerListener;
import dev.neuralnexus.tatercomms.common.relay.CommsRelay;
import dev.neuralnexus.taterlib.common.TaterLib;
import dev.neuralnexus.taterlib.common.event.player.PlayerEvents;
import dev.neuralnexus.taterlib.common.event.server.ServerEvents;
import dev.neuralnexus.taterlib.lib.dejvokep.boostedyaml.YamlDocument;
import dev.neuralnexus.taterlib.lib.dejvokep.boostedyaml.block.Block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Main class for the TaterComms plugin.
 */
public class TaterComms {
    private static YamlDocument config;
    private static Object logger;
    private static TaterComms singleton = null;
    private boolean STARTED = false;
    private static final ArrayList<Object> hooks = new ArrayList<>();
    private DiscordBot discord = null;
    private CommsRelay messageRelay = null;

    /**
     * Constructor for the TaterComms class.
     * @param configPath The path to the config file
     * @param logger The logger
     */
    public TaterComms(String configPath, Object logger) {
        singleton = this;
        TaterComms.logger = logger;

        // Config
        try {
            config = YamlDocument.create(new File("." + File.separator + configPath + File.separator + "TaterComms", "tatercomms.config.yml"),
                    Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("tatercomms.config.yml"))
            );
            config.reload();
        } catch (IOException | NullPointerException e) {
            useLogger("Failed to load tatercomms.config.yml!\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Getter for the singleton instance of the PanelServerManager class.
     * @return The singleton instance
     */
    public static TaterComms getInstance() {
        return singleton;
    }

    /**
     * Use whatever logger is being used.
     * @param message The message to log
     */
    public static void useLogger(String message) {
        if (logger instanceof java.util.logging.Logger) {
            ((java.util.logging.Logger) logger).info(message);
        } else if (logger instanceof org.slf4j.Logger) {
            ((org.slf4j.Logger) logger).info(message);
        } else {
            System.out.println(message);
        }
    }

    /**
     * Start TaterComms
     */
    public void start() {
        try {
            if (STARTED) {
                useLogger("TaterComms has already started!");
                return;
            }
            STARTED = true;

            // Get the Discord token from the config
            String token = config.getString("discord.token");
            if (token == null || token.equals("")) {
                useLogger("No Discord token found in tatercomms.config.yml!");
                return;
            }

            // Get server-channel mappings from the config
            HashMap<String, String> serverChannels = getServerChannels();
            if (serverChannels.isEmpty()) {
                useLogger("No server-channel mappings found in tatercomms.config.yml!");
                return;
            }

            HashMap<String, String> formatting = new HashMap<>();
            formatting.put("global", config.getString("formatting.global"));
            formatting.put("local", config.getString("formatting.local"));
            formatting.put("staff", config.getString("formatting.staff"));
            formatting.put("discord", config.getString("formatting.discord"));
            formatting.put("remote", config.getString("formatting.remote"));

            discord = new DiscordBot(token, serverChannels);
            messageRelay = new CommsRelay(formatting, discord);


            // Register player listeners
            PlayerEvents.ADVANCEMENT_FINISHED.register(CommonPlayerListener::onPlayerAdvancementFinished);
            PlayerEvents.DEATH.register(CommonPlayerListener::onPlayerDeath);
            PlayerEvents.LOGIN.register(CommonPlayerListener::onPlayerLogin);
            PlayerEvents.LOGOUT.register(CommonPlayerListener::onPlayerLogout);
            PlayerEvents.MESSAGE.register(CommonPlayerListener::onPlayerMessage);

            // Register server listeners
            ServerEvents.STARTED.register(CommonServerListener::onServerStarted);
            ServerEvents.STOPPED.register(CommonServerListener::onServerStopped);

            // Cancel chat and set message relay
            TaterLib.cancelChat = true;
            TaterLib.setMessageRelay(messageRelay);


            useLogger("TaterComms has been started!");
        } catch (Exception e) {
            useLogger("Failed to start TaterComms!");
            System.err.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Get the server name from the config
     * @return The server name
     */
    public static String getServerName() {
        return config.getString("server.name");
    }

    /**
     * Get server channels from the config file.
     * @return The map of server channels
     */
    public HashMap<String, String> getServerChannels() {
        HashMap<String, String> serverChannels = new HashMap<>();

        HashMap<String, Block> channelConfig = (HashMap<String, Block>) config.getBlock("discord.channels").getStoredValue();

        for (Map.Entry<String, Block> entry: channelConfig.entrySet()) {
            serverChannels.put(entry.getKey(), (String) entry.getValue().getStoredValue());
        }

        return serverChannels;
    }

    /**
     * Add a hook to the list of hooks
     * @param hook The hook to add
     */
    public static void addHook(Object hook) {
        hooks.add(hook);
    }

    /**
     * Get the Discord invite link
     */
    public static String getDiscordInviteLink() {
        return config.getString("discord.inviteUrl");
    }
}