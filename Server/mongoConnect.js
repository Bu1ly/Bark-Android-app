

var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var Schema = mongoose.Schema;


// -- Create  schemas and export-- //
var user = new Schema({
    dogName: String,
    gender: String,
    age: String,
    ownerName: String,
    email: String,
    sis: String,
    coordX: String,
    coordY: String

});

/// -- Connect collections to schema  -- ///

var User = module.exports = mongoose.model('usersDB', user);

/// -- Export -- ///

module.exports.createUser = function (newUser, callback) {
    bcrypt.genSalt(10, function (err,salt) {
        bcrypt.hash(newUser.sis, salt, function (err, hash) {
            newUser.sis = hash;
            newUser.save(callback)
        });
    });
};

module.exports.getUsersList = function(callback) {
    var query = {};
    User.find(query, callback);
};

module.exports.getUserByUserName = function (username, callback) {
    var query = {username: username};
    User.findOne(query, callback);
};

module.exports.getUserById = function (id, callback){
    console.log("BEFORE");
    User.findById(id, callback);
    console.log("BEFORE");
};

module.exports.comparePassword = function(suggestedPassword, hash, callback){
    bcrypt.compare(suggestedPassword, hash, function (err, isMatch) {
        if (err)
            console.log(err);
        callback(null, isMatch);
    });
};


