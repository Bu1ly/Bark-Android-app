
var express = require('express');
var bodyParser = require('body-parser');
var router = express.Router();
var jsonObj = bodyParser.json();
var User = require('./mongoConnect');
var passport = require('./passportInit');


router.post('/',function (req,res) {
    console.log("IM IN / path");
    res.status(200).end("/ Path is OK");
});

router.post('/register',jsonObj , function(req, res){
    console.log("Hey, new User  registration...");
    var registerData = req.body;
        //JSON.parse(JSON.stringify(req.body)); // get the user data as Json

// create user object and take the data according to User Schema
    var userJason ={
        dogName : registerData.dogName,
        gender : registerData.gender,
        age : registerData.age,
        ownerName: registerData.ownerName,
        email: registerData.email,
        sis: registerData.sis,
        coordX: registerData.coordX,
        coordY: registerData.coordY
    };


    // create new DB instance
    var newUser = new User(userJason);

   // console.log("The jSON obj before saving is: %j" + newUser.toString());

    console.log("BEFORE1");
    // save the newSenior to the DB
    User.createUser(newUser, function (err, user) {
        if (err){
            res.status(500).end("Error");
            console.log(user);
        }
        else {
            //res.message('User is now registered!');
            res.status(200).json(newUser);
        }
    });
    console.log("Finish");
});




router.post('/login',passport.authenticate('local'), function (req,res) {


    console.log("LOGIN IS Done!");
    var registerData = req.body;


    // create senior object and take the data according to Senior Schema
    var userJason = {
            dogName :registerData.dogName ,
            gender :registerData.gender,
            age : registerData.age,
            ownerName: registerData.ownerName,
            email: registerData.email,
            sis:registerData.sis
    };
    var stringify = JSON.stringify(userJason);
//    console.log("this is pretty " + pretty);


    res.status(200).json(stringify);

});

module.exports = router;