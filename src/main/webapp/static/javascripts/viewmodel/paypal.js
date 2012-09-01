var ViewModel = function(instanceToken) {
    var self = this;
    self.payKey = ko.observable(null);

    self.preparePayment = function() {
        $.ajax({
            url: "/api/v1/pay/prepare-payment?instance=" + instanceToken +"&returnUrl=" + window.location.href,
            type: "POST",
//            dataType: "json",
            contentType: "application/json",
            success: function(response) {
                window.location.href = response;
            }
        })
    }
};