/**
 * Created with IntelliJ IDEA.
 * User: Omri
 * Date: 8/31/12
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
function clockViewModel() {
    var self = this;
    var timerIntervalId = null;
    var startTime = null;

    self.elapsedSessionTime = ko.observable(getSessionTimeString());

    self.startTimer = function () {
        self.endTimer();
        startTime = new Date();
        timerIntervalId = setInterval(function () {
            self.elapsedSessionTime(getSessionTimeString(startTime));
        }, 200);
    };

    self.endTimer = function () {
        if (timerIntervalId) {
            clearInterval(timerIntervalId);
        }
        self.elapsedSessionTime(getSessionTimeString());
    };

    function getSessionTimeString(startTime) {
        if (!startTime) {
            return "00:00";
        }
        var elapsed = parseInt((new Date().getTime() - startTime.getTime())/1000);
        var seconds = elapsed % 60;
        var minutes = parseInt(elapsed / 60);
        var hours = parseInt(minutes /60);
        minutes = minutes % 60;

        var result = hours ? hours + ':' : '';
        result += minutes<10 ? '0' + minutes : minutes;
        result += ':';
        result += seconds <10 ? '0' + seconds : seconds;

        return result;
    }
}
