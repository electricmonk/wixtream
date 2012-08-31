/**
 * Created with IntelliJ IDEA.
 * User: Omri
 * Date: 8/29/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
var viewModel;
(function () {
    var ViewModelDef = function () {
        debugger; // TODO sivan, remove this mother fucker line
        var self = this;
        self.userId = ko.observable('User' + (new Date).getTime().toString(36));
        self.session = ko.observable(null);
        self.videoController = ko.observable(null);
        self.status = ko.computed(function () {
            if (self.session() == null) {
                return 'not_requested';
            }
            if (self.session().openTokSession() == null) {
                return 'waiting';
            }
            return 'connected';
        });

        self.getStatus = function () {
            $.getJSON('/api/v1/chat/subscriber-status/'
                    , {instance:widgetModel.instanceToken, clientId:self.userId()}
                    , function (sessionStatus) {
                        if (self.session()) {
                            ko.mapping.fromJS(sessionStatus, self.session());
                        } else {
                            self.session(ko.mapping.fromJS(sessionStatus));
                        }

                        var tmp = setupTokBox(sessionStatus.openTokSession, false, self.videoController());
                        if (tmp != self.videoController()) {
                            self.videoController(tmp);
                        }

                        setTimeout(self.getStatus, 1000);
                    }
            );
        };

        self.requestChat = function () {
            $.getJSON('/api/v1/chat/subscribe/'
                    , {instance:widgetModel.instanceToken, clientId:self.userId()}
                    , function () {
                        self.getStatus();
                    }
            );
        };

    };

    viewModel = new ViewModelDef();
    ko.applyBindings(viewModel);
})();