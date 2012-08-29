var http = require('https');
var OpenTok = require('opentok');

var key = '17466472';
var secret = 'f10140ce6d9604d5f1415040376f2589fd09af14';
var opentok = new OpenTok.OpenTokSDK(key, secret);


exports.createSession = function (req, res) {
    var location = '192.168.28.245';

    opentok.createSession(location, function (sessionId) {
        res.json({sessionId:sessionId});
    });
};

exports.createToken = function (req, res) {
    var token = opentok.generateToken(req.query);

    res.json({token:token});
};

