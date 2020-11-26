# Server

**Attention:**
This Sever supports live update of the Node.js application. After saving the changes, the Node.js sever automatically restarts.

The server consists of the Node.js application, a PostgreSQL database and the PostgreSQL client tool PgAdmin4.  
For starting the server, you have several options:

1. Start it with docker-compose (recommended)
2. Start it with docker
3. Start it without docker

## 1. Docker-Compose Startup

Navigate to the directory in which the `docker-compose.yml` file is and run the command:
> docker-compose up

## 2. Docker Startup

(Written when only Node.js app existed)
Navigate to the `restAPI` folder and execute this commands:
> docker build -t imageName .
> docker run -d -p 3000:3000 imageName

If you want to change something in the server, you can make these changes directly on the host machine and the Node.js sever will recognize these changes and restarts itself. In order enable these "live updates" run this command instead:
> docker run -d -p 3000:3000 -v "//c:/path/to/folder/restApi":/app imageName

## 3. Manual Startup

Before you can start, you have to install node.js, postgres and optionally pgadmin beforehand.

### Installation

If you (for any reason whatsoever) cannot have Docker installed, then you need to install all the dependencies manually.

#### Node.js

Just follow the instructions of [this tutorial](https://www.osstuff.com/how-to-install-node-js-and-npm-on-windows/).

#### Postgres

Just follow the instructions of [this tutorial](https://www.postgresqltutorial.com/install-postgresql/).#### Postgres

#### PgAdmin4

Just follow the instructions of [this tutorial](https://freegistutorial.com/how-to-install-pgadmin-4-on-windows-10/).
