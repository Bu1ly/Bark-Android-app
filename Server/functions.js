
var express = require('express');
var bodyParser = require('body-parser');
var router = express.Router();
var passport = require('passport');
var localStrategy = require('passport-local').Strategy;
var jsonObj = bodyParser.json();
var user = require('./mongoConnect');



router.post('/',jsonObj , function(req, res){
    console.log("Hey, new User  registration...");
    var registerData = JSON.parse(JSON.stringify(req.body)); // get the user data as Json

// create user object and take the data according to User Schema
    var userJason ={
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

passport.serializeUser(function (user, done) {
    done(null, user.id)
});

passport.deserializeUser(function (id, done) {
    User.getUserByid(id, function (err, user) {
        done(err, user);
    });
});

passport.use(new localStrategy(
    function (username, password, done) {
        User.getUserByUsername(username, function (err, user) {
            if(err) throw  err;
            if(!user){
                return done(null, false, {message: 'Unknown User' });
            }

            User.comparePassword(sis, user.sis, function (err, isMatch) {
                if(err)
                    throw err;
                if(isMatch)
                    return done(null, user);
                else
                    return done(null, false, {message: 'Invalid password'});
            });
        });
    }));

router.post('/login',passport.authenticate('local', {successRedirect:'/', failureRedirect:'/', failureFlash: false}),
    function (req,res) {
        res.redirect('/');
});

module.exports = router;