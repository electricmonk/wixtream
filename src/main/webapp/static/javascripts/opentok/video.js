function setupTokBox(openTokSession, isPublisher, existingController) {
    if (!openTokSession) {
        return null;
    }

    if (existingController && existingController.openTokSession.sessionId == openTokSession.sessionId) {
        return existingController;
    }

    var apiKey = openTokSession.apiKey;
    var sessionId = openTokSession.sessionId;
    var token = isPublisher ? openTokSession.publisherToken : openTokSession.subscriberToken;

    var session;
    var publisher;
    var subscribers = {};
    var VIDEO_WIDTH = 440;
    var VIDEO_HEIGHT = 290;

    TB.addEventListener("exception", exceptionHandler);

    // Un-comment the following to set automatic logging:
    // TB.setLogLevel(TB.DEBUG);

    if (TB.checkSystemRequirements() != TB.HAS_REQUIREMENTS) {
        alert("You don't have the minimum requirements to run this application."
                + "Please upgrade to the latest version of Flash.");
    } else {
        session = TB.initSession(sessionId);	// Initialize session

        // Add event listeners to the session
        session.addEventListener('sessionConnected', sessionConnectedHandler);
        session.addEventListener('sessionDisconnected', sessionDisconnectedHandler);
        session.addEventListener('connectionCreated', connectionCreatedHandler);
        session.addEventListener('connectionDestroyed', connectionDestroyedHandler);
        session.addEventListener('streamCreated', streamCreatedHandler);
        session.addEventListener('streamDestroyed', streamDestroyedHandler);
    }

    session.connect(apiKey, token);

    //--------------------------------------
    //  LINK CLICK HANDLERS
    //--------------------------------------

    //--------------------------------------
    //  OPENTOK EVENT HANDLERS
    //--------------------------------------

    function publishOrPerish() {
        if (!publisher) {
            var parentDiv = document.getElementById("myCamera");
            var publisherDiv = document.createElement('div'); // Create a div for the publisher to replace
            publisherDiv.setAttribute('id', 'opentok_publisher');
            parentDiv.appendChild(publisherDiv);
            var publisherProps = {width:VIDEO_WIDTH / 3, height:VIDEO_HEIGHT / 3};
            publisher = TB.initPublisher(apiKey, publisherDiv.id, publisherProps);  // Pass the replacement div id and properties
            session.publish(publisher);
        }
    }

    function sessionConnectedHandler(event) {
        // Subscribe to all streams currently in the Session
        for (var i = 0; i < event.streams.length; i++) {
            addStream(event.streams[i]);
        }

        isPublisher && publishOrPerish();
    }

    function streamCreatedHandler(event) {
        // Subscribe to the newly created streams
        for (var i = 0; i < event.streams.length; i++) {
            addStream(event.streams[i]);
        }
        publishOrPerish();
    }

    function streamDestroyedHandler(event) {
        // This signals that a stream was destroyed. Any Subscribers will automatically be removed.
        // This default behaviour can be prevented using event.preventDefault()
    }

    function sessionDisconnectedHandler(event) {
        // This signals that the user was disconnected from the Session. Any subscribers and publishers
        // will automatically be removed. This default behaviour can be prevented using event.preventDefault()
        publisher = null;
    }

    function connectionDestroyedHandler(event) {
        // This signals that connections were destroyed
    }

    function connectionCreatedHandler(event) {
        // This signals new connections have been created.
    }

    /*
     If you un-comment the call to TB.addEventListener("exception", exceptionHandler) above, OpenTok calls the
     exceptionHandler() method when exception events occur. You can modify this method to further process exception events.
     If you un-comment the call to TB.setLogLevel(), above, OpenTok automatically displays exception event messages.
     */
    function exceptionHandler(event) {
        if (event.code == 1013) {
            console.error("This page is trying to connect a third client to an OpenTok peer-to-peer session. "
                    + "Only two clients can connect to peer-to-peer sessions.");
        } else {
            console.error("Exception: " + event.code + "::" + event.message);
        }
    }

//--------------------------------------
//  HELPER METHODS
//--------------------------------------

    function addStream(stream) {
        // Check if this is the stream that I am publishing, and if so do not publish.
        if (stream.connection.connectionId == session.connection.connectionId) {
            return;
        }
        var subscriberDiv = document.createElement('div'); // Create a div for the subscriber to replace
        subscriberDiv.setAttribute('id', stream.streamId); // Give the replacement div the id of the stream as its id.
        document.getElementById("subscribers").appendChild(subscriberDiv);
        var subscriberProps = {width:VIDEO_WIDTH, height:VIDEO_HEIGHT};
        subscribers[stream.streamId] = session.subscribe(stream, subscriberDiv.id, subscriberProps);
    }


    return {
        disconnect:function () {
            session.disconnect();
        },

        openTokSession:openTokSession
    };
}