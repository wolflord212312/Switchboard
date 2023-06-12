package ca.sperrer.p0t4t0sandwich.tatercomms.fabric;

import ca.sperrer.p0t4t0sandwich.tatercomms.fabric.listeners.FabricPlayerLoginListener;
import ca.sperrer.p0t4t0sandwich.tatercomms.common.TaterComms;
import ca.sperrer.p0t4t0sandwich.tatercomms.fabric.commands.FabricTemplateCommand;
import ca.sperrer.p0t4t0sandwich.tatercomms.fabric.listeners.FabricPlayerLogoutListener;
import ca.sperrer.p0t4t0sandwich.tatercomms.fabric.listeners.FabricPlayerMessageListener;
import ca.sperrer.p0t4t0sandwich.tatercomms.fabric.listeners.FabricServerStartedListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricMain implements DedicatedServerModInitializer {
    public TaterComms taterComms;

    // Logger
    public final Logger logger = LoggerFactory.getLogger("tatercomms");

    // Get server type
    public String getServerType() {
        return "Fabric";
    }

    // Singleton instance
    private static FabricMain instance;
    public static FabricMain getInstance() {
        return instance;
    }

    @Override
    public void onInitializeServer() {
        // Singleton instance
        instance = this;

        logger.info("[TaterComms]: TaterComms is running on " + getServerType() + ".");

        // Register event listeners
        ServerLifecycleEvents.SERVER_STARTED.register(new FabricServerStartedListener());
        ServerPlayConnectionEvents.JOIN.register(new FabricPlayerLoginListener());
        ServerPlayConnectionEvents.DISCONNECT.register(new FabricPlayerLogoutListener());
        ServerMessageEvents.CHAT_MESSAGE.register(new FabricPlayerMessageListener());

        // Register commands
        CommandRegistrationCallback.EVENT.register(FabricTemplateCommand::register);

        // Mod enable message
        logger.info("[TaterComms]: TaterComms has been enabled!");
    }
}