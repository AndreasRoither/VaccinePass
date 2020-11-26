const express = require('express')
const app = express()
const { Client } = require('pg');
const bcrypt = require('bcrypt');

var connectionString = "postgres://admin:123@database:5432/postgres";

const client = new Client({
    connectionString: connectionString
});
client.connect();

app.use(express.json());
app.get('/', (req, res) => res.send('Hello world'));


app.post('/register', (req, response) => { 
    console.log(req.body);
    var user = User.fromJson(JSON.stringify(req.body));
    console.log("----------------");
    console.log(user.name);
    console.log("----------------");
    added = false;
    bcrypt.hash(user.password, 1, (err, hash) => {
        user.password = hash;

        var queryString = `INSERT INTO users(
            name, lastname, password, mail, birth, weight, height, bloodtype, maindoctor
            ) VALUES(` + "'" + user.name + "'" + `,` + "'" + user.lastname + "'" +  `,` + "'" + user.password + "'" + `,` + "'" + user.mail + "'" + 
            `,` + "TO_DATE(" + "'" + user.birth + "'" + ", 'DD/MM/YYYY')" + `,` + "'" + user.weight + "'" + `,` + "'" + user.height + "'" 
            + `,` + "'" + user.bloodtype + "'" + `,` + "'" + user.maindoctor +  "'" + `)`;
        
    
        client.query(queryString, (err, res) => {
            if (err !== undefined && err != null) {
                response.json({added: false})
                // log the error to console
                console.log("Postgres INSERT error:", err);
            
                // get the keys for the error
                var keys = Object.keys(err);
                console.log("\nkeys for Postgres error:", keys);
            
                // get the error position of SQL string
                console.log("Postgres error position:", err.position);

              }
            
              // check if the response is not 'undefined'
              if (res !== undefined && res != null) {
                // log the response to console
                console.log("Postgres response:", res);
            
                // get the keys for the response object
                var keys = Object.keys(res);
            
                // log the response keys to console
                console.log("\nkeys type:", typeof keys);
                console.log("keys for Postgres response:", keys);
            
                if (res.rowCount > 0) {
                  console.log("# of records inserted:", res.rowCount);
                  response.json({added: true});
                } else {
                  console.log("No records were inserted.");
                 response.json({added: false});
                }
              }
        }) 
    });
    
});

app.get('/login', (req, response) => {
    var loginCredentials = JSON.parse(JSON.stringify(req.body));
    var email = loginCredentials.email;
    console.log(email);
    var password = loginCredentials.password;
    console.log(password);
    var hash = "";
    var queryString = "SELECT password FROM users WHERE mail = " + "'" + email + "'";
    var success = false;
    client.query(queryString)
        .then(
            res => {
                hash = res.rows[0].password;
                console.log(hash);

                bcrypt.compare(password, hash)
                        .then(
                            result => {
                                success = result 
                                console.log(success)
                                response.json({mail: email, password: password, validation: success}); 
                            }).catch(e => console.error(e.stack))                          
            }).catch(e => console.error(e.stack))
});

app.listen(3000, () => {
    console.log("Listening on port 3000");
})


var User = function (name, lastname, password, mail, birth, weight, height, bloodtype, maindoctor){
    this.name = name;
    this.lastname = lastname;
    this.password = password;
    this.mail = mail;
    this.birth = birth;
    this.weight = weight;
    this.height = height;
    this.bloodtype = bloodtype;
    this.maindoctor = maindoctor;

    this.toJson = function (){
        return ("{" +
            "\"name\":\"" + this.name + "\"," +
            "\"lastname\":\"" + this.lastname + "\"," +
            "\"password\":" + this.password + "," +
            "\"mail\":" + this.mail + "," +
            "\"birth\":" + this.birth + "," +
            "\"weight\":" + this.weight + "," +
            "\"height\":" + this.height + "," +
            "\"bloodtype\":" + this.bloodtype + "," +
            "\"maindoctor\":" + this.maindoctor +
        "}");
    };
};

User.fromJson = function (json) {
    var obj = JSON.parse (json);
    return new User (obj.name, obj.lastname, obj.password, obj.mail, obj.birth, obj.weight, obj.height, obj.bloodtype, obj.maindoctor);
};