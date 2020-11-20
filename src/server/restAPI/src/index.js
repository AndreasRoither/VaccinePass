const express = require('express')
const app = express()
const { Client } = require('pg');


var connectionString = "postgres://admin:123@localhost:5432/postgres";

const client = new Client({
    connectionString: connectionString
});


app.get('/', (req, res) => res.send('Hello world asdfasdf'));


app.get('/register', (req, res) => {

});


app.get('/login', (req, res) => {

});


//app.get('/changepw')
//app.get('/updateuserinfo')




app.listen(3000, () => {
    console.log("Listening on port 3000");
})