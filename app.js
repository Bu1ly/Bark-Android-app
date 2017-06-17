var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');

var mongoose = require('mongoose');
var passport = require('passport');
var expressValidator = require('express-validator');


var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var session = require('express-session');

var flash = require('connect-flash');

var index = require('./routes/index');
var users = require('./routes/users');
var functions = require('./Server/functions');// Routing for functions
var engines = require('consolidate');
//config strings
var cfg = require('./Server/cfg');
var address = cfg.mongoUrl;

var multer  = require('multer');
var upload = multer({ dest: 'uploads/' });


/// -- MONGODB CONNECTION -- ///

var connect = mongoose.connect(address,function (error) {
    console.log("Trying to connect to the Mlab DB....\n");
    if(error){
        console.log("Warning! Error occurred!\n\n");
        console.log(error.name, "<- Is the error name\n", error.message , "<- Is the error message");
    }
    else{
        console.log("App is now connected to Mlab DB");
    }
});

var app = express();
app.listen(8000);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));//static files folder.var engines = require('consolidate');


app.set('view engine', 'jade');

// -- Middleware Express session
app.use(session({
    secret : 'secret',
    resave : true,
    saveUninitialized : true
}));



/// -- Passport init -- ////
app.use(passport.initialize());
app.use(passport.session());

/// -- Connect Flash -- ///
app.use(flash());



// In this example, the formParam value is going to get morphed into form body format useful for printing.
app.use(expressValidator({
    errorFormatter: function(param, msg, value) {
        var namespace = param.split('.')
            , root    = namespace.shift()
            , formParam = root;

        while(namespace.length) {
            formParam += '[' + namespace.shift() + ']';
        }
        return {
            param : formParam,
            msg   : msg,
            value : value
        };
    }
}));

// /// -- Setting global vars for flash messages -- ///
//
// app.use(function(req,res,next){
//     res.locals.success_msg = req.flash('success_msg');
//     res.locals.error_msg = req.flash('error_msg');
//     res.locals.error = req.flash('error');
//     next();
// });



// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));


app.use('/',functions);



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
