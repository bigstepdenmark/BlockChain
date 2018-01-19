# BlockChain

## Requirements
* Java 1.8 >
* Apache Maven

## Getting started
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

## Endpoints
* GET: http://localhost:7777/api/nodes
* GET: http://localhost:7777/api/node/{name}
* GET: http://localhost:7777/api/addnode/{name}/{port}
* GET: http://localhost:7777/api/mine/{name}
* GET: http://localhost:7777/api/blockchain/{name}

#### Get all nodes (method=GET)
```
➜ curl http://localhost:7777/api/nodes | jq
```
<img src="images/allnodes.png">

---

#### Get the node by given node name (method=GET)
```
➜ curl http://localhost:7777/api/node/{name} | jq
```
<img src="images/getnode.png">

---

#### Create new node (method=GET)
```
➜ curl http://localhost:7777/api/addnode/{name}/{port} | jq
```
<img src="images/addnode.png">

---

#### Create new block by given node name (method=GET)
```
➜ curl http://localhost:7777/api/mine/{name} | jq
```
<img src="images/mine.png">

---

#### Get all blockchains by given node name (method=GET)
```
➜ curl http://localhost:7777/api/blockchain/{name} | jq
```
<img src="images/blockchain.png">