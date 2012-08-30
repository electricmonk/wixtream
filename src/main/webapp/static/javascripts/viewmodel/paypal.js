var ViewModel = function(instanceToken) {
    var self = this;
    self.payKey = ko.observable(null);

    self.preparePayment = function() {
        $.ajax({
            url: "/api/v1/pay/prepare-payment?instance=" + instanceToken +"&returnUrl=" + window.location.href,
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            success: function(response) {
                var url = "https://www.sandbox.paypal.com/incontext?token=" + response.token;
                window.location.href = url;
            }
        })
    }
};