# BlockChai

### Requirements
* Java 1.8 >
* Apache Maven
----

### Getting started

1. Clone or just download the repository.
```bash
➜ git clone https://github.com/bigstepdenmark/BlockChain.git
```

2. Build the project
```bash
➜ mvn package
```
<img src="images/1.png">

3. Start the application (run the generated .jar file)
```bash
➜ java -jar target/BlockChainSI.one-jar.jar
```
<img src="images/2.png">

---

### Endpoints

Get all nodes (method=GET)
```
➜ http://localhost:7777/api/nodes
```
<img src="images/allnodes.png">

Get the node by given node name (method=GET)
```
➜ http://localhost:7777/api/node/{name}
```
<img src="images/getnode.png">

Create new node (method=GET)
```
➜ http://localhost:7777/api/addnode/{name}/{port}
```
<img src="images/addnode.png">

Create new block by given node name (method=GET)
```
➜ http://localhost:7777/api/mine/{name}
```
<img src="images/mine.png">

Get all blockchains by given node name (method=GET)
```
➜ http://localhost:7777/api/blockchain/{name}
```
<img src="images/blockchain.png">

