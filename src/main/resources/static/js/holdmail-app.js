(function () {
    'use strict';

    var HoldMailApp = angular.module('HoldMailApp', ['ui.bootstrap', 'angular-growl']);

    HoldMailApp.config([

        'growlProvider', function (growlProvider) {
            growlProvider.globalTimeToLive({success: 3000, error: -1, warning: 3000, info: 3000});
        }]);


}());
