package dk.cphsoftdev.app;

import java.util.ArrayList;
import java.util.List;

public class NodeEngine {

    private List<Node> nodes = new ArrayList();
    private static Block GB = new Block(0, "GenesisHash", "Genesis");

    public Node addNode(String name, int port){
        Node node = new Node(name, "localhost", port, GB, nodes);
        node.startSocketServer();
        nodes.add(node);
        return node;
    }

    public Block createBlock(String name){
        Node node = getNode(name);
        if(node != null){
            return node.generateBlock();
        }
        return null;
    }

    public Node getNode(String name){
        for(Node node : nodes){
            if(node.getName().equals(name)){
                return node;
            }
        }
        return null;
    }

    public List<Node> getAllNodes(){
        return nodes;
    }


    public List<Block> getNodeBlockchain(String name){
        Node node = getNode(name);
        if(node != null){
            return node.getBlockchain();
        }

        return null;
    }

}
