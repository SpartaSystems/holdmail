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
                    this.busy = true;

                    MessageService.getMessageList(mainCtrl.size, mainCtrl.page, $scope.recipientEmail)
                        .success(function (data) {

                            mainCtrl.items = mainCtrl.items.concat(data.messages);
                            mainCtrl.noMorePages = data.messages.length < 1;
                            mainCtrl.busy = false;
                            mainCtrl.page = mainCtrl.page + 1;

                        })
                        .error(function () {
                            console.log('Service failed to query message list');
                            mainCtrl.busy = true;
                        });

                };

                mainCtrl.hasNoResults = function () {

                    return mainCtrl.items;

                };

                mainCtrl.rowClick = function (selectedMail) {

                    MessageService.getMessageDetail(selectedMail.messageId)
                        .success(function (data) {

                            var modalInstance = $uibModal.open({
                                templateUrl: 'modal.html',
                                controller: 'ModalCtrl as modalCtrl',
                                backdrop: "static",
                                windowClass: 'mail-details-modal',
                                modalFade: true

                            });

                            modalInstance.message = data;

                        })
                        .error(function () {
                            console.log('Service failed to query message details');
                        });

                };

                mainCtrl.clearAndFetchMessages();

            }]);


}());
