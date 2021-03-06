const express = require('express')
const app = express()
const { Client } = require('pg');
const bcrypt = require('bcrypt');
var CryptoJS = require("crypto-js");
const constants = require('constants');

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
    bcrypt.hash(user.password, 1, (err, hash) => {
        user.password = hash;
        console.log("hashed password")
        var queryString = `INSERT INTO users(
            name, lastname, password, mail, birth, weight, height, bloodtype, maindoctor
            ) VALUES(` + "'" + user.name + "'" + `,` + "'" + user.lastname + "'" +  `,` + "'" + user.password + "'" + `,` + "'" + user.mail + "'" + 
            `,` + "TO_DATE(" + "'" + user.birth + "'" + ", 'DD/MM/YYYY')" + `,` + "'" + user.weight + "'" + `,` + "'" + user.height + "'" 
            + `,` + "'" + user.bloodtype + "'" + `,` + "'" + user.maindoctor +  "'" + `)`;
        
    
        client.query(queryString, (err, res) => {
            if (err !== undefined && err != null) {
                response.json({name: user.name, mail: user.mail, success: false});
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
                  response.json({name: user.name, mail: user.mail, success: true});
                } else {
                  console.log("No records were inserted.");
                  response.json({name: user.name, mail: user.mail, success: false});
                }
              }
        }) 
    });
    
});

app.post('/registerDoctor', (req, response) => { 
    console.log(req.body);
    var doctor = Doctor.fromJson(JSON.stringify(req.body));
    console.log("----------------");
    console.log(doctor.name);
    console.log("----------------");
    bcrypt.hash(doctor.password, 1, (err, hash) => {
        doctor.password = hash;

        var queryString = `INSERT INTO doctors(name, password, mail, public_key) 
                           VALUES(` + "'" + doctor.name + "'" + `,` + "'" + doctor.password + "'" + `,` + "'" + doctor.mail + "'" + "," + "'" + doctor.publicKey + "'" + `)`;
        
    
        client.query(queryString, (err, res) => {
            if (err !== undefined && err != null) {
                response.json({name: doctor.name, mail: doctor.mail, success: false});
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
                  response.json({name: doctor.name, mail: doctor.mail, success: true});
                } else {
                  console.log("No records were inserted.");
                  response.json({name: doctor.name, mail: doctor.mail, success: false});
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
    var queryString = "SELECT name, password FROM users WHERE mail = " + "'" + email + "'";
    var success = false;
    client.query(queryString)
        .then(
            res => {
                hash = res.rows[0].password;
                name = res.rows[0].name;
                console.log(hash);

                bcrypt.compare(password, hash)
                        .then(
                            result => {
                                success = result 
                                console.log(success)
                                response.json({name: name, mail: email, success: success}); 
                            }).catch(e => console.error(e.stack))                          
            }).catch(e => console.error(e.stack))
});


app.post('/loginDoctor', (req, response) => {
    var loginCredentials = JSON.parse(JSON.stringify(req.body));
    var email = loginCredentials.mail;
    console.log(email);
    var password = loginCredentials.password;
    console.log(password);
    var hash = "";
    var queryString = "SELECT name, password FROM doctors WHERE mail = " + "'" + email + "'";
    var success = false;
    client.query(queryString)
        .then(
            res => {
                hash = res.rows[0].password;
                name = res.rows[0].name;
                console.log(hash);

                bcrypt.compare(password, hash)
                        .then(
                            result => {
                                success = result 
                                console.log(success)
                                response.json({name: name, mail: email, success: success}); 
                            }).catch(e => console.error(e.stack))                          
            }).catch(e => console.error(e.stack))
});


app.post('/addVaccine', (req, response) => {
    var success = true;



    console.log("received vacccine!!!!!!!!!");
    var signedVaccinationObject = JSON.parse(JSON.stringify(req.body))
    var receivedSignature = signedVaccinationObject.signature;
    var receivedData = signedVaccinationObject.data;
    var receivedDataObject = JSON.parse(receivedData);

    var queryString = "SELECT public_key FROM doctors WHERE mail = " + "'" + receivedDataObject.doctorId + "'";

    client.query(queryString)
        .then(
            res => {

                var success = false;
                var publicKey = CryptoJS.enc.Base64.parse(res.rows[0].public_key);
                var decryptedMessage = decrypt(receivedSignature, publicKey);

                if (decryptedMessage === receivedData) {
                    success = true;
                    console.log("Vaccine verified!!!");
                }
                
                response.json({success: success})
             
            }).catch(e => console.error(e.stack))
})


function decrypt(message = '', key = ''){
    var code = CryptoJS.AES.decrypt(message, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
        });
    var decryptedMessage = code.toString(CryptoJS.enc.Utf8);

    return decryptedMessage;
}


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


var Doctor = function (name, password, mail, publicKey){
    this.name = name;
    this.password = password;
    this.mail = mail;
    this.publicKey = publicKey;

    this.toJson = function (){
        return ("{" +
            "\"name\":\"" + this.name + "\"," +
            "\"password\":" + this.password + "," +
            "\"mail\":" + this.mail + "," +
            "\"publicKey\":" + this.mail + "," +
        "}");
    };
};

Doctor.fromJson = function (json) {
    var obj = JSON.parse (json);
    return new Doctor (obj.name, obj.password, obj.mail, obj.publicKey);
};