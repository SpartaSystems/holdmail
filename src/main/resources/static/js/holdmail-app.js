(function () {
    'use strict';

    var holdmailApp = angular.module('HoldMailApp', ['ui.bootstrap','ngRoute']);

    holdmailApp.config(['$routeProvider',
        function($routeProvider) {
            $routeProvider.when('/mails', {
                            templateUrl: 'index.html',
                            controller: 'MessageListController'
                            })
                            .when('/mails/:mailId', {
                                templateUrl: 'index.html',
                                controller: 'MessageListController'
                            })
                            .otherwise({
                                redirectTo: '/mails'
                            });
        }]);

}());
