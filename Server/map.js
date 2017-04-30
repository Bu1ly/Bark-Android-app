var express = require('express');
var bodyParser = require('body-parser');
var User = require('./mongoConnect');
var router = express.Router();



router.get('/getAllUsers',function (req, res) {
    User.find({}, function(err,users) {
        if(err)
            res.status(500).end("Error");
        else {
            var UserMap = {};  //return object
            // fill up the  object
            User.forEach(function (user) {
                UserMap[User.ownerName] = user;
            });
        }});
});


router.get('/getUsersByRadius', function (req,res) {

    //GET USER COORDS TO CALC RADIUS
    var coords = req.body;

    //EMPTY
    if(!coords.coordX && !coords.coordY){
        console.log("No coords to check radius to, check req from client!");
        res.status(500);
        return null;
    }

    var x = coords.coordX;
    var y = coords.coordY;

    var calculatedRad = giveBorder(x,y);




});

// CALC THE RADIUS FROM EACH SIDE AND RETURN JSON FOR CLIENT
giveBorder = function(longi, langi) {

    var longiT = longi;
    var langiT = langi;



}