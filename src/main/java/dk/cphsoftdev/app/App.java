package dk.cphsoftdev.app;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping( value = "/api" )
public class App
{
    private static NodeEngine engine = new NodeEngine();

    @RequestMapping( value = "/", method = GET )
    public String index()
    {
        return "Welcome to our BlockChain Service!";
    }

    @RequestMapping( value = "/nodes", method = GET )
    public List<Node> getAllNodes()
    {
        return engine.getAllNodes();
    }

    @RequestMapping( value = "/node/{name}", method = GET )
    public Node getNode( @PathVariable String name )
    {
        return engine.getNode( name );
    }

    @RequestMapping( value = "/blockchain/{name}", method = GET )
    public List<Block> getNodeWithBlockChain( @PathVariable String name )
    {
        return engine.getNodeBlockchain( name );
    }

    @RequestMapping( value = "/mine/{nodename}", method = GET )
    public Block createBlock( @PathVariable String nodename )
    {
        return engine.createBlock( nodename );
    }

    @RequestMapping( value = "/addnode/{name}/{port}", method = GET )
    public Node addNode( @PathVariable String name, @PathVariable int port )
    {
        return engine.addNode( name, port );
    }
}
