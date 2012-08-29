/**
 * Created with IntelliJ IDEA.
 * User: Omri
 * Date: 8/29/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
(function(){
    var viewModel = {
        userId: ko.observable('User'+(new Date).getTime().toString(36)),
        session: ko.observable(null),

        status: ko.computed(function() {
            if (viewModel.session() == null) {
                return 'not_request';
            }
            if (viewModel.session().openTokSession() == null) {
                return 'waiting';
            }
            return 'connected';
        })
    };
})();