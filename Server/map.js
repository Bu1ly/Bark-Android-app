var express = require('express');
var bodyParser = require('body-parser');
var User = require('./mongoConnect');
var router = express.Router();

router.get('/getAllCoords',function (req, res) {
    User.find({}, function(err,users) {
        if(err)
            res.status(500).end("Error");
        else {
            var UserMap = {};  //return Senior object
            // fill up the Senior object
            User.forEach(function (user) {
                UserMap[User.ownerName] = user;
            });
        }});
});