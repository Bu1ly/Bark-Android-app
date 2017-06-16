/**
 * Created by Danny on 18-Jan-17.
 */

    var passport = require('passport');
    var LocalStrategy = require('passport-local').Strategy;
    var User = require('./mongoConnect');


    passport.serializeUser(function (user, done) {
        done(null, user.id);
    });

    passport.deserializeUser(function (id, done) {
        User.getUserById(id, function (err, user) {
            done(err, user);
        });
    });

    passport.use('local', new LocalStrategy({usernameField: 'email', passwordField: 'sis'},
        function (username, password, done) {
            User.getUserByUserName(username, function (err, user) {
                if (err) {
                    console.log("No such User!!\n");
                    //res.message('No such User!!');
                    throw err;
                }
                if (!user) {
                    console.log("Unknown User....\n");
                    return done(null, false, {message: 'Unknown User!'});
                }

                User.comparePassword(password, user.sis, function (err, isMatch) {
                    if (err) {
                        console.log("Wrong Password!\n");
                        throw err;
                    }
                    if (isMatch) {
                        console.log("Found a matching password!\n",user);
                        return done(null, user);
                    }
                    else
                        return done(null, false, {message: 'Invalid password'});
                });
            });
        }));





module.exports = passport;