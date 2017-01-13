/**
 * Created by Danny on 26-Dec-16.
 */

var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var Schema = mongoose.Schema;

//config strings
var cfg = require('./cfg');
var address = cfg.mongoUrl;


/// -- MONGODB CONNECTION -- ///

var connect = mongoose.createConnection(address,function (error) {
    console.log("Trying to connect to the Mlab DB....\n");
    if(error){
        console.log("Warning! Error occurred!\n\n");
        console.log(error.name, "<- Is the error name\n", error.message , "<- Is the error message");
    }
    else{
        console.log("App is now connected to Mlab DB");
    }
});


// -- Create user schema and export-- //
var user = new Schema({
    dogName: String,
    gender: String,
    age: String,
    ownerName: String,
    sis: String,
    session : String
});

/// -- Connect collections to schema  -- ///


var User = module.exports = connect.model('usersDB', user);

/// -- Export -- ///

module.exports.createUser = function (newUser, callback) {
    bcrypt.genSalt(10, function (err,salt) {
        bcrypt.hash(newUser.sis, salt, function (err, hash) {
            newUser.sis = hash;
            newUser.save(callback)
        });
    });
};
