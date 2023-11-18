package dev.neuralnexus.tatercomms.common.relay;

import dev.neuralnexus.taterlib.common.entity.Entity;
import dev.neuralnexus.taterlib.common.event.player.*;
import dev.neuralnexus.taterlib.common.event.pluginmessages.PluginMessageEvent;
import dev.neuralnexus.taterlib.common.event.server.ServerStartedEvent;
import dev.neuralnexus.taterlib.common.event.server.ServerStoppedEvent;
import dev.neuralnexus.taterlib.common.inventory.ItemStack;
import dev.neuralnexus.taterlib.common.player.Player;
import dev.neuralnexus.taterlib.common.server.Server;

import java.util.List;
import java.util.Set;

/**
 * Comms Event abstractions.
 */
public class CommsEvents {
    public static class CommsPlayerAdvancementFinishedEvent implements PlayerAdvancementEvent.AdvancementFinished {
        private final CommsMessage message;

        public CommsPlayerAdvancementFinishedEvent(CommsMessage message) {
            this.message = message;
        }

        @Override
        public String getAdvancement() {
            return message.getMessage();
        }

        @Override
        public Player getPlayer() {
            return message.getSender();
        }
    }

    public static class CommsPlayerDeathEvent implements PlayerDeathEvent {
        private final CommsMessage message;

        public CommsPlayerDeathEvent(CommsMessage message) {
            this.message = message;
        }

        @Override
        public Player getPlayer() {
            return message.getSender();
        }

        @Override
        public String getDeathMessage() {
            return message.getMessage();
        }

        @Override
        public void setDeathMessage(String s) {}

        @Override
        public boolean hasKeepInventory() {
            return false;
        }

        @Override
        public void setKeepInventory(boolean b) {}

        @Override
        public List<ItemStack> getDrops() {
            return null;
        }

        @Override
        public void setDrops(List<ItemStack> list) {}

        @Override
        public void clearDrops() {}

        @Override
        public int getDroppedExp() {
            return 0;
        }

        @Override
        public void setDroppedExp(int i) {}

        @Override
        public Entity getEntity() {
            return null;
        }
    }

    public static class CommsPlayerLoginEvent implements PlayerLoginEvent {
        private final CommsMessage message;

        public CommsPlayerLoginEvent(CommsMessage message) {
            this.message = message;
        }

        @Override
        public Player getPlayer() {
            return message.getSender();
        }

        @Override
        public String getLoginMessage() {
            return message.getMessage();
        }

        @Override
        public void setLoginMessage(String s) {}
    }

    public static class CommsPlayerLogoutEvent implements PlayerLogoutEvent {
        private final CommsMessage message;

        public CommsPlayerLogoutEvent(CommsMessage message) {
            this.message = message;
        }

        @Override
        public Player getPlayer() {
            return message.getSender();
        }

        @Override
        public String getLogoutMessage() {
            return message.getMessage();
        }

        @Override
        public void setLogoutMessage(String s) {}
    }

    public static class CommsPlayerMessageEvent implements PlayerMessageEvent {
        private final CommsMessage message;

        public CommsPlayerMessageEvent(CommsMessage message) {
            this.message = message;
        }

        @Override
        public Player getPlayer() {
            return message.getSender();
        }

        @Override
        public String getMessage() {
            return message.getMessage();
        }

        @Override
        public Set<Player> recipients() {
            return null;
        }

        @Override
        public void setMessage(String s) {}

        @Override
        public void setRecipients(Set<Player> set) {}

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public void setCancelled(boolean b) {}
    }

    public static class CommsServer implements Server {
        private final CommsMessage message;

        public CommsServer(CommsMessage message) {
            this.message = message;
        }

        @Override
        public String getName() {
            return message.getSender().getServerName();
        }

        @Override
        public Set<Player> getOnlinePlayers() {
            return null;
        }
    }

    public static class CommsServerStartedEvent implements ServerStartedEvent {
        private final CommsMessage message;

        public CommsServerStartedEvent(CommsMessage message) {
            this.message = message;
        }

        @Override
        public Server getServer() {
            return new CommsServer(message);
        }
    }

    public static class CommsServerStoppedEvent implements ServerStoppedEvent {
        private final CommsMessage message;

        public CommsServerStoppedEvent(CommsMessage message) {
            this.message = message;
        }

        @Override
        public Server getServer() {
            return new CommsServer(message);
        }
    }

    public static class CommsPluginMessageEvent implements PluginMessageEvent.Server {
        private final CommsMessage message;

        public CommsPluginMessageEvent(CommsMessage message) {
            this.message = message;
        }

        @Override
        public String getChannel() {
            return message.getMessage();
        }

        @Override
        public byte[] getData() {
            return message.toByteArray();
        }

        @Override
        public Object getServer() {
            return message.getSender().getServerName();
        }
    }
}