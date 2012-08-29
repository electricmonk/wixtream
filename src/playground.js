/**
 * Created with IntelliJ IDEA.
 * User: Omri
 * Date: 8/29/12
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
var http = require('https');
var OpenTok = require('opentok');

var key = '17466472';
var secret  = 'f10140ce6d9604d5f1415040376f2589fd09af14';
var opentok = new OpenTok.OpenTokSDK(key, secret);
var location = '192.168.28.245';
var sessionId;
//
//opentok.createSession(location, function(result){
//    sessionId = result;
//
//
//    var token = opentok.generateToken({session_id:session_id, role:OpenTok.RoleConstants.PUBLISHER, connection_data:"userId:42"});
//});
//
