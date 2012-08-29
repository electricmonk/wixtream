
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.render('index', { title: 'Express' });
};

exports.session = function(req, res) {
    res.render('session', req.params);
}