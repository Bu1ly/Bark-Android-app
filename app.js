var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var index = require('./routes/index');
var users = require('./routes/users');


var router = express.Router();
var app = express();

var mongoose = require('mongoose');// DB connections

  // -- Connect to DB --

  var connection = mongoose.createConnection('mongodb://Bu1ly:danivolp@ds135798.mlab.com:35798/bark',function (error) {
      console.log("Trying to connect to the Mlab DB....\n");

      if(error){
          console.log("Warning! Error occurred!\n\n");
          console.log(error.name, "<- Is the error name\n", error.message , "<- Is the error message");
      }
      else{
          console.log("App is now connected to Mlab DB");
      }
  });

  module.exports = connection;   //if i want to use DB in other files;


// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', index);
app.use('/users', users);
//app.use('/abc',abc);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
