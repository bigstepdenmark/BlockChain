package dk.cphsoftdev.app;


import java.io.Serializable;
import java.security.MessageDigest;

public class Block implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int index;
    private long timestamp;
    private String data;
    private String previousHash;
    private String hash;

    public Block()
    {

    }

    public Block( int index, String prevHash, String data )
    {
        this.index = index;
        this.previousHash = prevHash;
        this.data = data;
        timestamp = System.currentTimeMillis();
        hash = generateHash();
    }

    private String generateHash()
    {
        String base = String.valueOf( index ) + previousHash + String.valueOf( timestamp );

        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
            byte[] hash = digest.digest( base.getBytes( "UTF-8" ) );
            StringBuilder hexBuilder = new StringBuilder();

            for ( byte h : hash )
            {
                String hex = Integer.toHexString( 0xff & h );
                if ( hex.length() == 1 )
                {
                    hexBuilder.append( '0' );
                }
                hexBuilder.append( hex );
            }

            return hexBuilder.toString();
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( ex );
        }
    }


    public int getIndex()
    {
        return index;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public String getData()
    {
        return data;
    }

    public String getPreviousHash()
    {
        return previousHash;
    }

    public String getHash()
    {
        return hash;
    }

    public String toString()
    {
        return "Block {" + " Index: " + index + ", timestamp: " + timestamp + ", hash: " + hash + ", previousHash: " + previousHash + "}";
    }

}