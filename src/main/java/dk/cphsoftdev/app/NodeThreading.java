package dk.cphsoftdev.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static dk.cphsoftdev.app.Message.MESSAGE_TYPE.*;

public class NodeThreading extends Thread
{
    private Socket client;
    private Node node;

    NodeThreading( Node node, Socket client )
    {
        super( node.getName() + System.currentTimeMillis() );
        this.node = node;
        this.client = client;
    }

    public void run()
    {
        try
        {
            ObjectOutputStream out = new ObjectOutputStream( client.getOutputStream() );
            ObjectInputStream in = new ObjectInputStream( client.getInputStream() );

            Message message = new Message.MessageBuilder().messagePublisher( node.getPort() ).messageType( READY ).build();
            out.writeObject( message );

            Object fromClient;

            while ( (fromClient = in.readObject()) != null )
            {

                if ( fromClient instanceof Message )
                {
                    Message msg = (Message) fromClient;
                    System.out.println( String.format( "%d received:  %s", node.getPort(), fromClient.toString() ) );
                    if ( NOTIFY_NEW_BLOCK == msg.TYPE )
                    {
                        if ( msg.BLOCKS.isEmpty() || msg.BLOCKS.size() > 1 )
                        {
                            System.err.println( "Invalid block received: " + msg.BLOCKS );
                        }
                        synchronized ( node )
                        {
                            node.addBlock( msg.BLOCKS.get( 0 ) );
                        }
                        break;
                    }
                    else if ( REQUEST_ALL == msg.TYPE )
                    {
                        out.writeObject( new Message.MessageBuilder()
                                .messagePublisher( node.getPort() )
                                .messageType( RESPONSE_ALL )
                                .messageBlocks( node.getBlockchain() )
                                .build() );
                        break;
                    }
                }
            }
            client.close();
        }
        catch ( IOException | ClassNotFoundException e )
        {
            e.printStackTrace();
        }
    }
}
