/**
 * Created by Danny on 26-Dec-16.
 */



var mongoose = require('mongoose');
var Schema = mongoose.Schema;

//
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


// -- Create user schema -- //
var user = new Schema({
    dogName: String,
    ownerName: String,
    identity: String,
    sis: String,
    Email: String,
    session : String
});

// -- Connect collections to schema -- //
var User = connect.model('UserDB',user);

//module.exports('User');