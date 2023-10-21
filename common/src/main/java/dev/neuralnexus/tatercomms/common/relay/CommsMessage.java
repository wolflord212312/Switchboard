package dev.neuralnexus.tatercomms.common.relay;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.neuralnexus.tatercomms.common.listeners.player.CommonPlayerListener;
import dev.neuralnexus.tatercomms.common.listeners.server.CommonServerListener;
import dev.neuralnexus.taterlib.common.abstractions.player.AbstractPlayer;
import dev.neuralnexus.taterlib.lib.gson.Gson;
import dev.neuralnexus.taterlib.lib.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class for relaying messages between the server and Discord
 */
public class CommsMessage {
    private final CommsSender sender;
    private final String channel;
    private final String message;

    /**
     * Constructor for the CommsMessage class
     * @param sender The sender
     * @param channel The channel
     * @param message The message
     */
    public CommsMessage(CommsSender sender, String channel, String message) {
        this.sender = sender;
        this.channel = channel;
        this.message = message;
    }

    /**
     * Constructor for the CommsMessage class
     * @param sender The sender
     * @param channel The channel
     * @param message The message
     */
    public CommsMessage(CommsSender sender, MessageType channel, String message) {
        this(sender, channel.getIdentifier(), message);
    }

    /**
     * Constructor for the CommsMessage class
     * @param serverName The server name
     * @param channel The channel
     * @param message The message
     */
    public CommsMessage(String serverName, String channel, String message) {
        this(new CommsSender(serverName), channel, message);
    }

    /**
     * Constructor for the CommsMessage class
     * @param serverName The server name
     * @param channel The channel
     * @param message The message
     */
    public CommsMessage(String serverName, MessageType channel, String message) {
        this(new CommsSender(serverName), channel.getIdentifier(), message);
    }

    /**
     * Constructor for the CommsMessage class
     * @param sender The sender
     * @param channel The channel
     * @param message The message
     */
    public CommsMessage(AbstractPlayer sender, String channel, String message) {
        this(new CommsSender(sender), channel, message);
    }

    /**
     * Constructor for the CommsMessage class
     * @param sender The sender
     * @param channel The channel
     * @param message The message
     */
    public CommsMessage(AbstractPlayer sender, MessageType channel, String message) {
        this(new CommsSender(sender), channel.getIdentifier(), message);
    }

    /**
     * Getter for the sender
     * @return The sender
     */
    public CommsSender getSender() {
        return this.sender;
    }

    /**
     * Getter for the channel
     * @return The channel
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Getter for the message
     * @return The message
     */
    public String getMessage() {
        return this.message;
    }

    static Gson gson = new GsonBuilder().create();

    /**
     * Message channel parser
     * @param args The arguments
     */
    public static void parseMessageChannel(Object[] args) {
        CommsMessage message;
        byte[] data = (byte[]) args[1];
        //
        System.out.println(args[0]);
        System.out.println("Received message: " + new String(data));
        //
        try {
            message = CommsMessage.fromByteArray(data);
        } catch (Exception e) {
            // TODO: Make this less jank
            try {
                // Forge Support
                message = gson.fromJson(new String(Arrays.copyOfRange(data, 7, data.length)), CommsMessage.class);
            } catch (Exception ex) {
                // Fabric Support
                message = gson.fromJson(new String(Arrays.copyOfRange(data, 4, data.length)), CommsMessage.class);
            }
        }

        MessageType messageType = MessageType.fromIdentifier(message.getChannel());
        //
        System.out.println("Message type: " + messageType);
        System.out.println("Decoded message: " + message.toJSON());
        //
        switch (messageType) {
            case PLAYER_ADVANCEMENT_FINISHED:
                //
                System.out.println("Advancement finished");
                //
                CommonPlayerListener.onPlayerAdvancementFinished(new Object[]{message.getSender(), message.getMessage()});
                break;
            case PLAYER_DEATH:
                //
                System.out.println("Player death");
                //
                CommonPlayerListener.onPlayerDeath(new Object[]{message.getSender(), message.getMessage()});
                break;
            case PLAYER_LOGIN:
                //
                System.out.println("Player login");
                //
                CommonPlayerListener.onPlayerLogin(new Object[]{message.getSender(), message.getMessage()});
                break;
            case PLAYER_LOGOUT:
                //
                System.out.println("Player logout");
                //
                CommonPlayerListener.onPlayerLogout(new Object[]{message.getSender(), message.getMessage()});
                break;
            case PLAYER_MESSAGE:
                //
                System.out.println("Player message");
                //
                CommonPlayerListener.onPlayerMessage(new Object[]{message.getSender(), message.getMessage()});
                break;
            case SERVER_STARTED:
                //
                System.out.println("Server started");
                //
                CommonServerListener.onServerStarted(new Object[]{message.getSender().getServerName()});
                break;
            case SERVER_STOPPED:
                //
                System.out.println("Server stopped");
                //
                CommonServerListener.onServerStopped(new Object[]{message.getSender().getServerName()});
                break;
        }
    }

    /**
     * Enum for the different types of messages that can be sent
     */
    public enum MessageType {
        PLAYER_ADVANCEMENT_FINISHED("tc:p_adv_fin"),
        PLAYER_DEATH("tc:p_death"),
        PLAYER_LOGIN("tc:p_login"),
        PLAYER_LOGOUT("tc:p_logout"),
        PLAYER_MESSAGE("tc:p_msg"),
        SERVER_STARTED("tc:s_start"),
        SERVER_STOPPED("tc:s_stop");

        private final String identifier;

        MessageType(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public static Set<String> getTypes() {
            return Arrays.stream(MessageType.values()).map(MessageType::getIdentifier).collect(Collectors.toSet());
        }

        public static MessageType fromIdentifier(String identifier) {
            return Arrays.stream(MessageType.values()).filter(messageType -> messageType.getIdentifier().equals(identifier)).findFirst().orElse(null);
        }
    }

    public byte[] toByteArray() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(gson.toJson(this));
        return out.toByteArray();
    }

    public static CommsMessage fromByteArray(byte[] data) {
        ByteArrayDataInput in = ByteStreams.newDataInput(data);
        String json = in.readUTF();
        return gson.fromJson(json, CommsMessage.class);
    }

    public String toJSON() {
        return gson.toJson(this);
    }
}
