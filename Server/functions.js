
var express = require('express');
var bodyParser = require('body-parser');
var router = express.Router();
var jsonObj = bodyParser.json();
var passport = require('./passportInit');
var fs = require('fs');
var path = require('path');
var uid = require('uid2');
var mime = require('mime');
var async = require("async");
var geodist = require('geodist');
var multer  = require('multer');
var upload = multer({ dest: './Server/photos/' });
var ensureLogin = require('connect-ensure-login');

/* Models required */
var User = require('./mongoConnect');
var Vaccine = require('./Vaccines');
var LostDog = require('./LostDog');

/* Default Path */
router.post('/',function (req,res) {
    var ipAddr = req.headers["x-forwarded-for"];
    if (ipAddr){
        var list = ipAddr.split(",");
        ipAddr = list[list.length-1];
    } else {
        ipAddr = req.connection.remoteAddress;
    }

    console.log("IM IN / path");
    res.status(200).end("/ Path is OK");
});

/* User Register API
 * Method : POST
 * payload : {
 *       dogName     : "",
 *       gender      : "",
 *       age         : "",
 *       ownerName   : "",
 *       email       : "",
 *       sis         : "",
 *       coordX      : "",
 *       coordY      : ""
 * }
 * Passport Authentication : false
 * */

router.post('/register', upload.single('photo') , function(req, res){
    var dataToSave = req.body;
    var newUser;
    if(req.file){ /* If Photo is uploaded */
        var newImg = fs.readFileSync(req.file.path);
        // encode the file as a base64 string.
        var encImg = newImg.toString('base64');
        var splitName = req.file.originalname.split('.');
        var fileNameToSave = path.dirname(process.mainModule.filename) + "/Server/photos/"+req.file.filename+"."+splitName[splitName.length - 1];
        fs.writeFile(fileNameToSave, encImg, 'base64', function(err) {
            fs.unlink(path.dirname(process.mainModule.filename) + "/Server/photos/"+req.file.filename);
            dataToSave["photo"] = fileNameToSave;
            dataToSave["photoBase64"] = encImg;
            dataToSave["photoExt"] = splitName[splitName.length - 1];
            dataToSave["lastActiveTime"] = new Date();
            //var registerData = req.body;
            // create new DB instance
            newUser = new User(dataToSave);
            User.createUser(newUser, function (err, user) {
                if (err){
                    res.status(500).end("Error");
                }
                else {
                    res.status(200).json(newUser);
                }
            });
        });
    }
    else{
        var userJason = {
            dogName : dataToSave.dogName,
            gender : dataToSave.gender,
            age : dataToSave.age,
            ownerName: dataToSave.ownerName,
            email: dataToSave.email,
            sis: dataToSave.sis,
            coordX: dataToSave.coordX,
            coordY: dataToSave.coordY,
            lastActiveTime : new Date()
        };
        // create new DB instance
        newUser = new User(userJason);
        User.createUser(newUser, function (err, user) {
            if (err){
                res.status(500).end("Error");
            }
            else {
                res.status(200).json(newUser);
            }
        });
    }
});


/* User Login API
 * Method : POST
 * payload : {
 *       email       : "example@example.com",
 *       sis         : ""
 * }
 * Passport Authentication : false
 * */
router.post('/login',passport.authenticate('local'), function (req,res) {
    var registerData = req.body;
    User.getUserByUserName(registerData.email,function(err,data){
        var userJason = {
            email: registerData.email,
            sis:registerData.sis,
            _id:data._id,       // Send _id in response as it is required in other API's
            ownerName:data.ownerName,
            dogName:data.dogName,
            gender:data.gender,
            photo : data.photo,
            photoBase64 : data.photoBase64,
            photoExt : data.photoExt
        };
        var stringify = JSON.stringify(userJason);
        // added By ASSAF - covert the string to JsonObject
        res.status(200).json(JSON.parse(stringify));
    });
});

/* Update User Details API
 * Method : POST
 * payload : {
 *       dogName     : "",
 *       gender      : "",
 *       age         : "",
 *       ownerName   : "",
 *       coordX      : "",
 *       coordY      : "",
 *       userId      : "_id from Login response"
 * }
 * Passport Authentication : true
 * */
router.post('/change_info', upload.single('photo') , function(req,res){
    var updateID = req.body.userId;
    var updateInfo = req.body;
    if(req.file){ /* If Photo is uploaded */
        var newImg = fs.readFileSync(req.file.path);
        // encode the file as a base64 string.
        var encImg = newImg.toString('base64');
        var splitName = req.file.originalname.split('.');
        var fileNameToSave = path.dirname(process.mainModule.filename) + "/Server/photos/"+req.file.filename+"."+splitName[splitName.length - 1];
        fs.writeFile(fileNameToSave, encImg, 'base64', function(err) {
            fs.unlink(path.dirname(process.mainModule.filename) + "/Server/photos/"+req.file.filename);
            User.findOneAndUpdate(
                {
                    _id : updateID
                },
                {
                    $set:{
                        dogName: updateInfo.dogName,
                        gender: updateInfo.gender,
                        age: updateInfo.age,
                        ownerName: updateInfo.ownerName,
                        coordX: updateInfo.coordX,
                        coordY: updateInfo.coordY,
                        photo : fileNameToSave,
                        photoBase64 : encImg,
                        photoExt : splitName[splitName.length - 1],
                        lastActiveTime : new Date()
                    }
                }, function(err,upObj){
                    if (err) {
                        res.status(500).end("Error, user not in DB");
                    }
                    res.status(200).end(JSON.parse("OK, changed info"));
                }
            );
        });
    }
    else{
        User.findOneAndUpdate(
            {
                _id : updateID
            },
            {
                $set:{
                    dogName: updateInfo.dogName,
                    gender: updateInfo.gender,
                    age: updateInfo.age,
                    ownerName: updateInfo.ownerName,
                    coordX: updateInfo.coordX,
                    coordY: updateInfo.coordY,
                    lastActiveTime : new Date()
                }
            }, function(err,upObj){
                if (err) {
                    res.status(500).end("Error, user not in DB");
                }
                res.status(200).end("OK, changed info");
            }
        );
    }

});

/* GET User Details API
 * Method : GET
 * params : {
 *     userId      : "_id from Login response"
 * }
 * Passport Authentication : true
 * */
router.get('/user_info/:userId',ensureLogin.ensureLoggedIn(),  function(req,res){
    var updateID = req.params.userId;
    User.findOne(
        {
            _id : updateID
        }, function(err,upObj){
            if (err || !upObj) {
                res.status(500).end("Error, user not in DB");
            }
            res.status(200).json(upObj);
        }
    );
});

/* GET Vaccines List API
 * Method : GET
 * params : {
 *     userId      : "_id from Login response"
 * }
 * Passport Authentication : true
 * */
router.get('/vaccines/:userId',  function(req,res){
    var updateID = req.params.userId;

    Vaccine.find(
        {
            userId : updateID
        },
        {},
        {
            sort:{
                _id : -1
            }
        },
        function(err,upObj){
            if (err || !upObj) {
                res.status(500).end("Error, user not in DB");
            }
            else if(upObj.length > 0){

                res.status(200).json(upObj);
            }
            else{

                res.status(200).end("No vaccines found");
            }
        }
    );
});

/* GET All users List API
 * Method : GET
 * Passport Authentication : false
 * */
router.get('/getAllUsers', function(req,res){
    User.find(
        {},
        {
            sis : false
        },
        {
            sort : {
                _id : -1
            }
        },
        function(err,upObj){
            if (err || !upObj) {
                res.status(500).end("Error, user not in DB");
            }
            else if(upObj.length > 0){

                res.status(200).json(upObj);
            }
            else{
                res.status(200).end("No users found");
            }
        }
    );
});

/* GET All lost dogs List API
 * Method : GET
 * Passport Authentication : false
 * */
router.get('/getLostDogsList', function(req,res){
    LostDog.find(
        {},
        {},
        {
            sort : {
                _id : -1
            }
        },
        function(err,upObj){
            if (err || !upObj) {
                res.status(500).end("Error, user not in DB");
            }
            else if(upObj.length > 0){
                res.status(200).json(upObj);
            }
            else{
                res.status(200).end("No dogs found");
            }
        }
    );
});

/* Add Lost Dog Details API
 * Method : POST
 * payload : {
 *       dogName     : "",
 *       gender      : "",
 *       age         : "",
 *       ownerName   : "",
 *       phone      : "",
 *       photo      : "optional"
 * }
 * Form-type: "multi-part"
 * Passport Authentication : false
 * */
router.post('/addLostDog', upload.single('photo'), function(req,res){
    var dataToSave = req.body;
    if(req.file){ /* If Photo is uploaded */
        var newImg = fs.readFileSync(req.file.path);
        // encode the file as a base64 string.
        var encImg = newImg.toString('base64');
        var splitName = req.file.originalname.split('.');
        var fileNameToSave = path.dirname(process.mainModule.filename) + "/Server/photos/"+req.file.filename+"."+splitName[splitName.length - 1];
        fs.writeFile(fileNameToSave, encImg, 'base64', function(err) {
            fs.unlink(path.dirname(process.mainModule.filename) + "/Server/photos/"+req.file.filename);
            dataToSave["photo"] = fileNameToSave;
            dataToSave["photoBase64"] = encImg;
            dataToSave["photoExt"] = splitName[splitName.length - 1];
            var LostDogModel = new LostDog(dataToSave);
            LostDogModel.save(function(err,upObj){
                    if (err) {
                        res.status(500).end("Data cannot be saved.");
                    }
                    else{
                        res.status(200).json(upObj);
                    }
                }
            );
        });
    }
    else{
        var LostDogModel = new LostDog(dataToSave);
        LostDogModel.save(function(err,upObj){
            if (err) {
                res.status(500).end("Data cannot be saved.");
            }
            else{
                res.status(200).json(upObj);
            }
        });
    }
});

/* Remove Lost Dog Entry from DB - API
 * Method : POST
 * payload : {
 *       id  : "_id of entry to be deleted."
 * }
 * Passport Authentication : false
 * */
router.post('/removeLostDog', function(req,res){
    var idToRemove = req.body.id;
    LostDog.remove(
        {
            _id : idToRemove
        },function(err, upObj){
            if (err) {
                res.status(500).end("Data cannot be saved.");
            }
            else{
                res.status(200).end("Entry Removed");
            }
        }
    );
});

/* Add Vaccine API
 * Method : POST
 * payload : {
 *       userId     : "_id from login response",
 *       name      : "",
 *       date      : "",
 *       expiry   : ""
 * }
 * Passport Authentication : true
 * */
router.post('/addVaccine',  function(req,res){
    var dataToSave = req.body;
    var VaccineModel = new Vaccine(dataToSave);
    VaccineModel.save(function(err,upObj){
        if (err) {
            res.status(500).end("Vaccine cannot be saved.");
        }
        else{
            res.status(200).json(upObj);
        }
    });
});

/* Get Users Within Distance
 * Method : POST
 * payload : {
 *       coordX     : "Lat",
 *       coordY      : "Long",
 *       range      : "in KM"
 * }
 * Passport Authentication : true
 * */
router.post('/getUsersWithinDistance', function(req,res){

    if(req.body.coordX && req.body.coordY && req.body.range){
        var selectedUsers = [];
        var currrentTime = new Date();
        var myLastDate = new Date(currrentTime - 15 * 60000);
        var criteria = {lastActiveTime : {$gt : myLastDate}};
        if(req.body.userId){
            criteria["_id"] = {
                $ne : req.body.userId
            }
        }
        User.find(
            criteria,
            {},
            {
                lean : true
            },
            function(err,data){
                if(data && data.length > 0){
                    var taskInParallel = [];
                    for (var key in data) {
                        (function (key) {
                            taskInParallel.push((function (key) {
                                return function (embeddedCB) {
                                    if(data[key].coordX && data[key].coordY){
                                        var dist = geodist(
                                            {
                                                lat: req.body.coordX,
                                                lon: req.body.coordY
                                            },
                                            {
                                                lat: data[key].coordX,
                                                lon: data[key].coordY
                                            },
                                            {
                                                exact: true,
                                                unit: 'km'
                                            }
                                        );
                                        if(parseFloat(dist) < parseFloat(req.body.range)){
                                            selectedUsers.push(data[key]);
                                        }
                                        embeddedCB();
                                    }
                                    else{
                                        embeddedCB();
                                    }
                                }
                            })(key))
                        }(key));
                    }
                    async.parallel(taskInParallel, function (err, result) {
                        if(err){
                            console.log("blablab2");
                            res.status(200).end("No Users Found");
                        }
                        else {
                            if(selectedUsers.length == 0){
                                console.log("blablab3");
                                res.status(200).end("No Users Found");
                            }
                            else{

                                res.status(200).json(selectedUsers);
                            }
                        }
                    });
                }
                else{
                    console.log("blablab4");
                    res.status(200).end("No Users Found");
                }
            });
    }
    else{
        res.status(500).end("Invalid Lat longs");
    }
});

module.exports = router;
