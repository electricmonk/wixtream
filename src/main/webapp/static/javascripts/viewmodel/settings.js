var ViewModel = function(settingsModel) {
    var self = this;
    self.settings = ko.observable(settingsModel.settings);

    self.update = function() {
        $.ajax({
            url: "/settings/save?instance=" + settingsModel.instanceToken,
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: ko.toJSON(self.settings),
            success: function() {
                alert("Your settings have been saved.")
            }
        })
    }
};