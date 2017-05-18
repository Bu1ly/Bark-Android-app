var express = require('express');
//var mongoose = require('mongoose');
var bodyParser = require('body-parser');
var User = require('./mongoConnect');
var router = express.Router();




//RADIUS preferences.
const radius = 50;




router.post('/getUsersByRadius', function (req,res) {

    //GET USER COORDS TO CALC RADIUS
    var coords = req.body;

    //EMPTY
    if(!coords.coordX && !coords.coordY){
        console.log("No coords to check radius to, check req from client!");
        res.status(500).end();
    }

    var Cx = coords.coordX;
    var Cy = coords.coordY;






});

// CALC THE RADIUS FROM EACH SIDE AND RETURN PERIMITER AS JSON FOR CLIENT
var giveBorder = function(Cx, Cy) {

    var Cx = longi;
    var Cy = langi;

    // Border
    var left = longiT - radius;
    var up = langiT - radius;
    var right = longiT - radius;
    var down = langiT - radius;




};

var pointInCircle = function (x, y, Cx, Cy, radius) {
    var distancesquared = (x - cx) * (x - cx) + (y - cy) * (y - cy);
    return distancesquared <= radius * radius;
};

function pointInCircle(point, radius, center)
{
    return (google.maps.geometry.spherical.computeDistanceBetween(point, center) <= radius)
}




//GET ALL USERS IN DB

router.get('/users', function (req,res) {
    User.find({}, function(err,users) {
        if(err)
            res.status(500).end("Error");
        else {
            var SeniorMap = {};  //return Senior object
            // fill up the Senior object
            users.forEach(function (user)
            {
                SeniorMap[user._id] = user;
            });

            res.end(JSON.stringify(SeniorMap, null, "\n"));
            //e.g: JSON.stringify(new Date(2006, 0, 2, 15, 4, 5)) --> '"2006-01-02T15:04:05.000Z"'
        }
    });
});
