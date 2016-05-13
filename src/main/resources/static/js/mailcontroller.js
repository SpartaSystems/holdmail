(function () {
    'use strict';

    angular.module('HoldMailApp')

        .controller('MailController', ['$scope', '$uibModal', '$http', '$routeParams', function ($scope, $modal, $http, $routeParams) {

            var mailCtrl = this;

            mailCtrl.items = [];

            mailCtrl.loadMessageList = function () {
                $http({
                    method: 'GET',
                    url: '/rest/messages'
                })
                    .success(function (data, status) {
                        mailCtrl.items = data.messages;
                    })
                    .error(function (data, status) {
                        //alert("Failed to query mail service.");
                    });
            };


            mailCtrl.hasNoResults = function () {

                return mailCtrl.items;

            };

            mailCtrl.rowClick = function (selectedMail) {

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

            //alert('routeParams = ' + JSON.stringify($routeParams));

            mailCtrl.loadMessageList();

        }]);

}());
