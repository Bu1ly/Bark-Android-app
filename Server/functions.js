
var express = require('express');
var bodyParser = require('body-parser');
var router = express.Router();
var jsonObj = bodyParser.json();
var user = require('./mongoConnect');
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
        sis: registerData.sis

    };

    // create new DB instance
    var newUser = new user(userJason);

   // console.log("The jSON obj before saving is: %j" + newUser.toString());


    // save the newSenior to the DB
    user.createUser(newUser, function (err, user) {
        if (err){
            res.status(500).end("Error");
            console.log(user);
        }
        else {
            res.status(200).json(newUser);
        }
    });
});



/*router.post('/login',jsonObj , function(req, res){
    console.log("Hey, Login");
    var registerData = req.body;
    console.log("Hey, Login " +JSON.parse(JSON.stringify(req.body)).toString());
    //JSON.parse(JSON.stringify(req.body)); // get the user data as Json

    res.status(200).end("LOgin OK");
});*/



router.post('/login',passport.authenticate('local', {session : false}), function (req,res) {

    /*passport.authenticate('local', {session : false});*/
    console.log("LOGIN IS Done!");
    var registerData = req.body;
    var data = jsonObj;

    var stringify = JSON.stringify(data);

    // create senior object and take the data according to Senior Schema
    var userJason = {
            dogName :registerData.dogName ,
            gender :registerData.gender,
            age : registerData.age,
            ownerName: registerData.ownerName,
            email: registerData.email,
            sis:registerData.sis
    };
    var pretty = JSON.stringify(userJason);



    console.log("this is pretty " + pretty);


    res.status(200).json(userJason);

});

module.exports = router;