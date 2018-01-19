package dk.cphsoftdev.app;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable
{

    private static final long serialVersionUID = 1L;

    int PUBLISHER;
    int SUBSCRIBER;
    MESSAGE_TYPE TYPE;
    List<Block> BLOCKS;

    public enum MESSAGE_TYPE
    {
        READY, NOTIFY_NEW_BLOCK, REQUEST_ALL, RESPONSE_ALL
    }

    public String toString()
    {
        return "Message - Type: " + TYPE + ", Publisher: " + PUBLISHER + ", Subscriber: " + SUBSCRIBER + ", " + BLOCKS;
    }

    static class MessageBuilder
    {
        private final Message message = new Message();

        MessageBuilder messagePublisher( final int publisher )
        {
            message.PUBLISHER = publisher;
            return this;
        }

        MessageBuilder messageSubscriber( final int subscriber )
        {
            message.SUBSCRIBER = subscriber;
            return this;
        }

        MessageBuilder messageType( final MESSAGE_TYPE type )
        {
            message.TYPE = type;
            return this;
        }

        MessageBuilder messageBlocks( final List<Block> blocks )
        {
            message.BLOCKS = blocks;
            return this;
        }

        Message build()
        {
            return message;
        }

    }
}

