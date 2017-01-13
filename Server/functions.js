
var express = require('express');
var bodyParser = require('body-parser');
var router = express.Router();

var jsonObj = bodyParser.json();
var user = require('./mongoConnect');



router.post('/',jsonObj , function(req, res){
    console.log("Hey!");
    var registerData = JSON.parse(JSON.stringify(req.body)); // get the user data as Json


    var userJason ={                     // create senior object and take the data according to Senior Schema
        dogName : registerData.dogName,
        gender : registerData.gender,
        age : registerData.age,
        ownerName: registerData.ownerName,
        sis: registerData.sis

    };

    // create new DB instance
    var newUser = new user(userJason);

    // save the newSenior to the DB
    user.createUser(newUser, function (err, user) {
        if (err){
            res.status(500).end("Error");
            console.log(user);
        }
        else
            res.status(200).end("Added", userJason, "to Users DB");
    });


});

module.exports = router;