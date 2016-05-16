(function () {
    'use strict';

    angular.module('HoldMailApp', ['ui.bootstrap'])

        .controller('MessageListController', ['$scope', '$uibModal', '$http', function ($scope, $modal, $http) {

            var mainCtrl = this;

            mainCtrl.items = [];

            mainCtrl.loadMessageList = function () {

                var findMessagesREST = '/rest/messages';

                if($scope.recipientEmail) {
                    findMessagesREST = '/rest/messages?recipient=' + $scope.recipientEmail;
                }

                mainCtrl.items = [];

                $http({
                    method: 'GET',
                    url: findMessagesREST
                })
                    .success(function (data, status) {
                        mainCtrl.items = data.messages;
                    })
                    .error(function (data, status) {
                        //alert("Failed to query mail service.");
                    });
            };


            mainCtrl.hasNoResults = function () {

                return mainCtrl.items;

            };

            mainCtrl.rowClick = function (selectedMail) {

                $http({
                    method: 'GET',
                    url: '/rest/messages/' + selectedMail.messageId
                })
                    .success(function (data, status) {

                        var modalInstance = $modal.open({
                            templateUrl: 'modal.html',
                            controller: 'ModalCtrl as modalCtrl',
                            backdrop: "static",
                            windowClass: 'mail-details-modal',
                            modalFade: true

                        });

                        modalInstance.message = data;

                    })
                    .error(function (data, status) {
                        //alert("Failed to query mail service.");
                    });



            };

            mainCtrl.loadMessageList();

        }]);

}());
