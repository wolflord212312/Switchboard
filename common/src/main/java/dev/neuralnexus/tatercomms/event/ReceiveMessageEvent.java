package dev.neuralnexus.tatercomms.event;

import dev.neuralnexus.tatercomms.api.TaterCommsAPIProvider;
import dev.neuralnexus.tatercomms.api.message.Message;
import dev.neuralnexus.taterlib.event.Event;

/**
 * Receive message event.
 */
public class ReceiveMessageEvent implements Event {
    private final Message message;

    public ReceiveMessageEvent(Message message) {
        if (message.isRemote()) {
            message.setPlaceHolderMessage(TaterCommsAPIProvider.get().getFormatting("remote"));
        } else if (message.isGlobal()) {
            message.setPlaceHolderMessage(TaterCommsAPIProvider.get().getFormatting("global"));
        }
        this.message = message;
    }

    /**
     * Get the message.
     *
     * @return The message.
     */
    public Message getMessage() {
        return message;
    }
}
