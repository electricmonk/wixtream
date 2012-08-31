/**
 * Created with IntelliJ IDEA.
 * User: Omri
 * Date: 8/31/12
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
function getSessionTimeString(startTime) {
    if (!startTime) {
        return "00:00";
    }
    var elapsed = new Date().getTime() - startTime.getTime();
    elapsed %= 1000;
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