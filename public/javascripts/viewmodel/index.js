(function () {
    var viewModel = {
        sessions:ko.observableArray([]),
        userId:ko.observable('New user' + new Date),

        addSession:function () {
            $.getJSON('/create-session/', function (data) {
                viewModel.sessions.push(data);
            });
        },
        killSession:function (sessionIdObj) {
            viewModel.sessions.remove(sessionIdObj);
        },
        joinAsPublisher:function (sessionObj) {
            joinSession(sessionObj.sessionId, 'PUBLISHER', viewModel.userId());
        },
        joinAsSubscriber:function (sessionObj) {
            joinSession(sessionObj.sessionId, 'PUBLISHER', viewModel.userId());
        }
    };

    ko.applyBindings(viewModel);

    function joinSession(sessionId, role, userId) {
        $.getJSON('/get-token/',
                {sessionId:sessionId, role:role, connection_data:'userId:' + userId},
                function (data) {
                    window.open('/sessions/' + sessionId + '/' + data.token + '/' + userId + '/', '_blank');
                    return false;
                });
    }

})();
