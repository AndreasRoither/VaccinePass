const express = require('express')
const app = express()
const { Client } = require('pg');


var connectionString = "postgres://admin:123@database:5432/postgres";

const client = new Client({
    connectionString: connectionString
});
client.connect();


app.get('/', (req, res) => res.send('Hello world'));


app.get('/register', (req, res) => {

});


app.get('/login', (req, res) => {

});




app.listen(3000, () => {
    console.log("Listening on port 3000");
})