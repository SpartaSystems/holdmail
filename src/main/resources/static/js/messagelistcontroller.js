/*******************************************************************************
 * Copyright 2016 Sparta Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

(function () {
    'use strict';

    angular.module('HoldMailApp')

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
