/**
 * Created with IntelliJ IDEA.
 * User: Omri
 * Date: 8/29/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
var viewModel;
(function () {
    ko.bindingHandlers.expressCheckout = {
        init: function(element, valueAccessor, allBindingsAccessor, viewModel) {
            window.dg = new PAYPAL.apps.DGFlow({ trigger: valueAccessor(), expType:"instant"});
        }
    };

    var ViewModelDef = function () {
        clockViewModel.call(this);

        var self = this;
        self.userId = ko.observable('User' + (new Date).getTime().toString(36));
        self.session = ko.observable(null);
        self.videoController = ko.observable(null);
        self.status = ko.computed(function () {
            if (self.session() == null) {
                return {name: 'not_requested'};
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
                            self.startTimer();
                        }

                        setTimeout(self.getStatus, 1000);
                    }
            );
        };

        self.subscribeToChat = function(userId) {
            self.userId(userId);
            self.getStatus();
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