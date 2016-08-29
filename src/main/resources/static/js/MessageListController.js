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

        .controller('MessageListController', ['$scope', '$uibModal', 'MessageService',
            function ($scope, $uibModal, MessageService) {

                var mainCtrl = this;

                mainCtrl.items = [];
                mainCtrl.busy = false;
                mainCtrl.noMorePages = false;
                mainCtrl.page = 0;
                mainCtrl.size = 40;

                mainCtrl.clearAndFetchMessages = function () {
                    mainCtrl.items = [];
                    mainCtrl.page = 0;
                    mainCtrl.noMorePages = false;
                    mainCtrl.fetchMessages();
                };

                mainCtrl.fetchMessages = function () {

                    if (this.busy || this.noMorePages) {
                        return;
                    }

                    // service is async; tell controller/infinite scroll to back off until response comes through
                    this.busy = true;

                    MessageService.getMessageList(mainCtrl.size, mainCtrl.page, $scope.recipientEmail)
                        .then(function (response) {

                            var messages = response.data.messages;

                            mainCtrl.items = mainCtrl.items.concat(messages);
                            mainCtrl.noMorePages = messages.length < 1;
                            mainCtrl.busy = false;
                            mainCtrl.page = mainCtrl.page + 1;

                        }, function () {
                            console.log('Service failed to query message list');

                            // keep busy set otherwise infinite scroll will go crazy making followup calls
                            mainCtrl.busy = true;
                        });

                };


                mainCtrl.rowClick = function (selectedMail) {

                    MessageService.getMessageDetail(selectedMail.messageId)
                        .then(function (response) {

                            var modalInstance = $uibModal.open({
                                templateUrl: 'modal.html',
                                controller: 'ModalCtrl as modalCtrl',
                                backdrop: "static",
                                windowClass: 'mail-details-modal',
                                modalFade: true

                            });

                            modalInstance.message = response.data;

                        }, function () {
                            console.log('Service failed to query message details');
                        });

                };

                mainCtrl.clearAndFetchMessages();

            }]);


}());
