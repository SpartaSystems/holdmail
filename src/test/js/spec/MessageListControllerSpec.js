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



require('angular');
require('angular-mocks/ngMock');

require('../../../main/resources/static/js/HoldmailApp');
require('../../../main/resources/static/js/MessageService');
require('../../../main/resources/static/js/MessageListController');

describe('MessageListController Tests', function () {

    var ctrl, scope, $uibModal, MessageService, $q;

    var MESSAGE_LIST_RESPONSE = {
        data: {
            messages: [{a:'whatever'}, {b:'whatever'}, {c:'whatever'}]
        }
    };

    var MESSAGE_DETAILS_RESPONSE = {
        data: {
            thisEntry: 'does not matter'
        }
    };

    beforeEach(function () {
        angular.mock.module('HoldMailApp');

    });

    beforeEach(inject(function ($rootScope, $controller, _$uibModal_, _MessageService_, _$q_) {

        scope = $rootScope.$new();
        $uibModal = _$uibModal_;
        MessageService = _MessageService_;
        $q = _$q_;

        var messageListDefer = _$q_.defer();
        messageListDefer.resolve(MESSAGE_LIST_RESPONSE);
        spyOn(_MessageService_, 'getMessageList').and.returnValue(messageListDefer.promise);

        var messageDetailDefer = _$q_.defer();
        messageDetailDefer.resolve(MESSAGE_DETAILS_RESPONSE);
        spyOn(_MessageService_, 'getMessageDetail').and.returnValue(messageDetailDefer.promise);


        ctrl = $controller('MessageListController', {
            $scope: scope,
            $uibModal: $uibModal,
            MessageService: _MessageService_
        });

    }));

    describe('| initial state', function () {

        it(' - should have expected functions', function () {
            expect(ctrl.clearAndFetchMessages).toBeDefined();
            expect(ctrl.fetchMessages).toBeDefined();
            expect(ctrl.rowClick).toBeDefined();
        });

    });

    describe('| clearAndFetchMessages', function () {

        it(' - should reset variables and call fetchMessages()', function () {

            ctrl.items = [1, 2, 3];
            ctrl.page = 2;
            ctrl.noMorePages = true;
            spyOn(ctrl, 'fetchMessages').and.callFake(NO_OP);

            ctrl.clearAndFetchMessages();

            expect(ctrl.items.length).toEqual(0);
            expect(ctrl.page).toEqual(0);
            expect(ctrl.noMorePages).toBeFalsy();
            expect(ctrl.fetchMessages).toHaveBeenCalled();

        });

    });

    describe('| showEmptyMessagesPane', function () {

        it(' - should only return true if not busy and messages present', function () {

            ctrl.busy = false;
            ctrl.items = [];
            expect(ctrl.showEmptyMessagesPane()).toBeTruthy();

            ctrl.busy = true;
            ctrl.items = [];
            expect(ctrl.showEmptyMessagesPane()).toBeFalsy();

            ctrl.busy = false;
            ctrl.items = [1,2,3];
            expect(ctrl.showEmptyMessagesPane()).toBeFalsy();

            ctrl.busy = true;
            ctrl.items = [1,2,3];
            expect(ctrl.showEmptyMessagesPane()).toBeFalsy();
        });

    });

    describe('| fetchMessages', function () {

        it(' - should do nothing if busy is true', function () {

            MessageService.getMessageList = jasmine.createSpy().and.callFake(NO_OP);
            ctrl.busy = true;
            ctrl.noMorePages = false;

            ctrl.fetchMessages();

            expect(MessageService.getMessageList).not.toHaveBeenCalled();
            expect(ctrl.busy).toBeTruthy();

        });

        it(' - should do nothing if noMorePages true', function () {

            MessageService.getMessageList = jasmine.createSpy().and.callFake(NO_OP);
            ctrl.busy = false;
            ctrl.noMorePages = true;

            ctrl.fetchMessages();

            expect(MessageService.getMessageList).not.toHaveBeenCalled();
            expect(ctrl.busy).toBeFalsy();

        });

        it(' - should call service and set busy when controller ready', function () {

            scope.$digest();

            ctrl.busy = false;
            ctrl.noMorePages = false;

            ctrl.fetchMessages();

            expect(MessageService.getMessageList).toHaveBeenCalled();
            expect(ctrl.busy).toBeTruthy();

        });

        it(' - should set message list', function () {

            scope.$digest();

            ctrl.busy = false;
            ctrl.noMorePages = false;

            ctrl.fetchMessages();

            expect(MessageService.getMessageList).toHaveBeenCalledWith(40, 0, undefined);

            expect(ctrl.items).toEqual(MESSAGE_LIST_RESPONSE.data.messages);


        });

    });


    describe('| rowClick', function () {

        it(' - should call message service', function () {

            scope.$digest();

            ctrl.rowClick({messageId: 3333});

            expect(MessageService.getMessageDetail).toHaveBeenCalledWith(3333);

        });

    });

    var NO_OP = function () {
    }

});

