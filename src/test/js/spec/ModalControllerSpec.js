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
require('../../../main/resources/static/js/ModalController');

describe('ModalController Tests', function () {

    var ctrl, scope, growl, MessageService, $q;

    var MESSAGE = {id: 9543543, dummy: 'message'};

    // manually creating modal, since injection causes: Unknown provider: $uibModalInstanceProvider
    var $uibModalInstance = {
        message: MESSAGE,
        close: function () {
        }
    };

    beforeEach(function () {
        angular.mock.module('HoldMailApp');

    });

    beforeEach(inject(function ($rootScope, $controller, _growl_, _MessageService_, _$q_) {

        scope = $rootScope.$new();
        growl = _growl_;
        MessageService = _MessageService_;
        $q = _$q_;

        spyOn(_MessageService_, 'getMessageHTMLURI').and.returnValue('herp-derp-html-url');

        var defer = _$q_.defer();
        defer.resolve();
        spyOn(_MessageService_, 'forwardMessage').and.returnValue(defer.promise);

        ctrl = $controller('ModalCtrl', {
            $scope: scope,
            $uibModalInstance: $uibModalInstance,
            growl: _growl_,
            MessageService: _MessageService_
        });

    }));


    describe('| initial state', function () {


        it(' - variables initialized correctly', function () {

            expect(ctrl.close).toBeDefined();
            expect(ctrl.forwardMail).toBeDefined();
            expect(scope.message).toEqual(MESSAGE);

        });

        it(' - messageHTMLURI configured correctly', function () {

            expect(scope.messageHTMLURL).toEqual('herp-derp-html-url');

        });

    });

    describe('| close', function () {

        it(' - should close the uibModalInstance', function () {

            spyOn($uibModalInstance, 'close').and.callFake(function () {
            });

            ctrl.close();

            expect($uibModalInstance.close).toHaveBeenCalled();

        });

    });


    describe('| forward mail', function () {

        it(' - should growl on success', function() {

            var recpient = 'spongebob@squarepants.com';
            var messageId = 34234;

            spyOn(growl, 'success').and.callFake(function() {});

            scope.forwardRecipient = recpient;
            scope.message = {
                messageId: messageId
            };

            ctrl.forwardMail();

            scope.$digest();
            scope.$apply();

            expect(growl.success).toHaveBeenCalledWith('Mail ' + messageId
                + ' successfully sent to <b>' + recpient + '</b>', {});

        });

    });


});

