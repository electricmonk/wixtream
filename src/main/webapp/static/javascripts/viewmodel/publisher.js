/**
 * Created with IntelliJ IDEA.
 * User: Omri
 * Date: 8/29/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
var viewModel;
(function () {
    var sessionsMapping = {
        'sessions':{
            'key':function (data) {
                return data.clientId;
            }
        }
    };

    var ViewModelDef = function () {
        var self = this;
        var activeSession = null;

        clockViewModel.call(this);

        self.userId = ko.observable('User' + (new Date).getTime().toString(36));
        self.pending = ko.observable({sessions:ko.observableArray([])});
        self.sessions = ko.computed(function () {
            return self.pending().sessions();
        });

        self.instanceId = window.widgetModel.instanceToken;
        self.activeSession = ko.observable(null);

        self.videoController = ko.observable(null);


        self.activeSessionChanged = function (activeSession, previousActiveSession) {

        };

        self.getList = function () {
            $.getJSON('/api/v1/chat/subscriber-list/'
                    , {instance:self.instanceId, clientId:self.userId()}
                    , function (data) {
                        data = _.sortBy(data, function (item) {
                            return item.created
                        });
                        if (ko.mapping.isMapped(self.pending())) {
                            ko.mapping.fromJS({sessions:data}, sessionsMapping, self.pending());
                        } else {
                            self.pending(ko.mapping.fromJS({sessions:data}, sessionsMapping));
                        }
                        setTimeout(self.getList, 1000);
                    }
            );
        };

        self.isActive = function (session) {
            return self.activeSession() && session.clientId() == self.activeSession().clientId;
        };

        self.acceptSubscriber = function (session) {
            if (!self.isActive(session)) {
                var data = {instance:self.instanceId, subscriberClientId:session.clientId};
                if (self.activeSession()) {
                    data.removePreviousSessionWithClientId = self.activeSession().clientId;
                }

                $.getJSON('/api/v1/chat/accept-subscriber/'
                        , data
                        , function (session) {
                            self.activeSession(session);
                            self.videoController(setupTokBox(session.openTokSession, true, self.videoController()));
                            self.startTimer();
                        }
                );
            } else {
                self.endActiveSession();
            }
        };

        self.endActiveSession = function () {
            if (self.activeSession()) {
                $.getJSON('/api/v1/chat/end-session/'
                        , {instance:self.instanceId, subscriberClientId:self.activeSession().clientId}
                );
                self.activeSession(null);
            }
            self.stopTimer();
            self.videoController(null);
        }
    };

    viewModel = new ViewModelDef();
    viewModel.getList();
    ko.applyBindings(viewModel);
})();