(function () {
    'use strict';

    angular.module('HoldMailApp')

        .controller('MailController', ['$scope', '$uibModal', '$http', '$routeParams', function ($scope, $modal, $http, $routeParams) {

            var mailCtrl = this;

            mailCtrl.items = [];

            mailCtrl.queryMails = function () {
                $http({
                    method: 'GET',
                    url: '/rest/messages'
                })
                .success(function (data, status) {
                    // TODO: temp order/truncate.  In future, use pretty pagination/infinite scrolling.
                    mailCtrl.items = data.reverse().slice(0,50);
                })
                .error(function (data, status) {
                    //alert("Failed to query mail service.");
                });
            };

            mailCtrl.hasNoResults = function () {

                return mailCtrl.items;

            };

            mailCtrl.rowClick = function (selectedMail) {

                var modalInstance = $modal.open({
                    templateUrl: 'modal.html',
                    controller: 'ModalCtrl as modalCtrl',
                    backdrop: "static",
                    windowClass: 'mail-details-modal',
                    modalFade: true,

                });

                modalInstance.selectedMail = selectedMail;
            };

            //alert('routeParams = ' + JSON.stringify($routeParams));

            mailCtrl.queryMails();

        }]);

}());
