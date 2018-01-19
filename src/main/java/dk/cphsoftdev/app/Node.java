package dk.cphsoftdev.app;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static dk.cphsoftdev.app.Message.MESSAGE_TYPE.*;


public class Node
{

    private String name;
    private String address;
    private int port;
    private List<Block> blockchain = new ArrayList();
    private List<Node> p2p;
    private int diff;

    private ServerSocket serverSocket;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor( 10 );

    private boolean listening = true;

    public Node()
    {
    }

    public Node( String name, String address, int port, Block block, List<Node> nodes )
    {
        this.name = name;
        this.address = address;
        this.port = port;
        this.p2p = nodes;
        this.diff = 2;
        blockchain.add( block );
    }

    public String getName()
    {
        return name;
    }

    public String getAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }

    public List<Block> getBlockchain()
    {
        return blockchain;
    }

    private Block getLatestBlock()
    {
        if ( blockchain.isEmpty() )
        {
            return null;
        }

        return blockchain.get( blockchain.size() - 1 );
    }

    public Block generateBlock()
    {
        if ( blockchain.isEmpty() )
        {
            return null;
        }

        Block previousBlock = getLatestBlock();
        if ( previousBlock == null )
        {
            return null;
        }

        Block block = new Block( previousBlock.getIndex() + 1, previousBlock.getHash(), "Data added by " + name );
        System.out.println( "New Block with name has been generated: " + name + block.toString() );

        // Notify all nodes
        NotifyAll( NOTIFY_NEW_BLOCK, block );

        return block;
    }

    public void addBlock( Block block )
    {
        if ( validateBlock( block ) )
        {
            blockchain.add( block );
        }
    }

    private boolean validateBlock( Block block )
    {
        Block latestBlock = getLatestBlock();

        if ( latestBlock == null )
        {
            return false;
        }

        int expected = latestBlock.getIndex() + 1;

        if ( block.getIndex() != expected )
        {
            System.out.println( String.format( "Invalid index. Expected: %s Actual: %s", expected, block.getIndex() ) );
            return false;
        }

        if ( !Objects.equals( block.getPreviousHash(), latestBlock.getHash() ) )
        {
            System.out.println( "Unmatched hash code" );
            return false;
        }

        return true;
    }

    private void NotifyAll( Message.MESSAGE_TYPE type, Block block )
    {
        p2p.forEach( p -> sendMessage( type, p.getAddress(), p.getPort(), block ) );
    }

    private void sendMessage( Message.MESSAGE_TYPE type, String host, int port, Block blocks )
    {
        try (
                final Socket peer = new Socket( host, port );
                final ObjectOutputStream out = new ObjectOutputStream( peer.getOutputStream() );
                final ObjectInputStream in = new ObjectInputStream( peer.getInputStream() ) )
        {
            Object fromPeer;
            while ( (fromPeer = in.readObject()) != null )
            {
                if ( fromPeer instanceof Message )
                {
                    final Message msg = (Message) fromPeer;
                    System.out.println( String.format( "%d received: %s", this.port, msg.toString() ) );
                    if ( READY == msg.TYPE )
                    {
                        out.writeObject( new Message.MessageBuilder()
                                .messageType( type )
                                .messageSubscriber( port )
                                .messagePublisher( this.port )
                                .messageBlocks( Arrays.asList( blocks ) ).build() );
                    }
                    else if ( RESPONSE_ALL == msg.TYPE )
                    {
                        if ( !msg.BLOCKS.isEmpty() && this.blockchain.size() == 1 )
                        {
                            blockchain = new ArrayList<>( msg.BLOCKS );
                        }
                        break;
                    }
                }
            }
        }
        catch ( UnknownHostException e )
        {
            System.err.println( String.format( "Unknown host %s %d", host, port ) );
        }
        catch ( IOException e )
        {
            System.err.println( String.format( "%s couldn't get I/O for the connection to %s. Retrying...%n", getPort(), port ) );
            try
            {
                Thread.sleep( 100 );
            }
            catch ( InterruptedException e1 )
            {
                e1.printStackTrace();
            }
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace();
        }
    }

    void startSocketServer()
    {
        executor.execute( () -> {
            try
            {
                serverSocket = new ServerSocket( port );
                System.out.println( String.format( "Server %s started", serverSocket.getLocalPort() ) );
                listening = true;
                while ( listening )
                {
                    final NodeThreading thread = new NodeThreading( Node.this, serverSocket.accept() );
                    thread.start();
                }
                serverSocket.close();
            }
            catch ( IOException e )
            {
                System.err.println( "Could not listen to port " + port );
            }
        } );
        NotifyAll( REQUEST_ALL, null );
    }


}
