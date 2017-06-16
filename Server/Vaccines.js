/**
 * Created by Danny on 03-April-17.
 */

var mongoose = require('mongoose');
var Schema = mongoose.Schema;


// -- Create  schemas and export-- //
var vaccines = new Schema({
    userId: {type: Schema.ObjectId, ref: 'usersDB', required: true , index : true},
    name: String,
    date: Date,
    expiry: Date
});

/// -- Connect collections to schema  -- ///

var lostDog = module.exports = mongoose.model('vaccines', vaccines);
