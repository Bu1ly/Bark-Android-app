/**
 * Created by Danny on 03-April-17.
 */

var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var Schema = mongoose.Schema;


// -- Create  schemas and export-- //
var lost = new Schema({
    dogName: String,
    gender: String,
    age: String,
    ownerName: String,
    phone : String
});

/// -- Connect collections to schema  -- ///

var lostDog = module.exports = mongoose.model('lostD', lost);
