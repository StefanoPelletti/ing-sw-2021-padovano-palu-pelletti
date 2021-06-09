package it.polimi.ingsw.networking.message;

import java.io.Serializable;


/**
 * The abstract class Message provides a network-independent mechanism to exchange information between
 * the different modules of this Program.
 * <p>
 * By design, there should not be any reference to non-messaging entities.
 * The messages are intended to be dense bunches of data, and should not perform any operation beside getting and setting internal information.
 */
public abstract class Message implements Serializable {
    /**
     * an enumerator attribute representing the type of Message
     */
    private final MessageType messageType;

    /**
     * Super constructor of the abstract class Message.
     * <p>
     * By design requires the specific kind of message it is being built.
     *
     * @param messageType the type of the Message being built
     * @see MessageType the possible types of messages
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }


    /**
     * Returns the type of this message
     *
     * @return the type of the message
     */
    public MessageType getMessageType() {
        return messageType;
    }
}