package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private final MessageType messageType;

    /**
     * Message is the Parent Class of all MSG_something.
     * messageType must show a correlation between the message and his type.
     *
     * @param messageType The type of the Message being built.
     * @see MessageType For the possible messages.
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}