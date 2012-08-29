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
            $.getJSON('/get-token/',
                    {sessionId:sessionObj.sessionId, role:'publisher', connection_data:'userId:' + viewModel.userId()},
                    function (data) {
                        window.open('/publish/' + sessionObj.sessionId + '/' + data.token + '/' + viewModel.userId() + '/', '_blank');
                    });
        },
        joinAsSubscriber:function (sessionObj) {
            $.getJSON('/get-token/',
                    {sessionId:sessionObj.sessionId, role:'subscriber', connection_data:'userId:' + viewModel.userId()},
                    function (data) {
                        window.open('/subscribe/' + sessionObj.sessionId + '/' + data.token + '/' + viewModel.userId() + '/', '_blank');
                    });
        }
    };

    ko.applyBindings(viewModel);


})();
