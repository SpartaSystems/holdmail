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

describe('Message Service Tests', function () {

    var MessageService, httpBackend;

    beforeEach(angular.mock.module('HoldMailApp'));

    beforeEach(inject(function (_MessageService_, $httpBackend) {

        MessageService = _MessageService_;
        httpBackend = $httpBackend;
    }));

    afterEach(function() {
        httpBackend.verifyNoOutstandingExpectation();
        httpBackend.verifyNoOutstandingRequest();
    });


    describe('| getMessageHTMLURI', function () {

        it(" - should return the expected URI", function () {

            var messageId = 55934;
            expect(MessageService.getMessageHTMLURI(messageId)).toEqual('/rest/messages/' + messageId + '/html');
        });

    });

    describe('| getMessageList', function () {

        it(" - should query message list endpoint using all params", function () {

            var size = 10;
            var page = 101;
            var user = 'herp@derp.com';

            httpBackend.whenGET('/rest/messages?size=' + size + '&page=' + page + '&recipient=' + encodeURIComponent(user)).respond({
                messages: ['a', 'b', 'c']
            });
            MessageService.getMessageList(size, page, user).then(function (response) {
                expect(response.data.messages).toEqual(['a', 'b', 'c']);
            });
            httpBackend.flush();
        });

        it(" - should not query using recipient if parameter was undefined ", function () {

            var size = 50;
            var page = 200;

            httpBackend.whenGET('/rest/messages?size=' + size + '&page=' + page).respond({
                messages: ['all', 'messages']
            });
            MessageService.getMessageList(size, page).then(function (response) {
                expect(response.data.messages).toEqual(['all', 'messages']);
            });
            httpBackend.flush();
        });

    });


    describe('| getMessageDetail', function () {

        it(" - should query message detail endpoint", function () {

            var messageId = 93122;

            httpBackend.whenGET('/rest/messages/' + messageId).respond({
                'some': 'object'
            });
            MessageService.getMessageDetail(messageId).then(function (response) {
                expect(response.data).toEqual({'some': 'object'});
            });
            httpBackend.flush();
        });

    });


    describe('| forwardMessage', function () {

        it(" - should POST to message forward endpoint", function () {

            var messageId = 33432;
            var recpientEmail = 'herp@derp.com';

            httpBackend.expectPOST('/rest/messages/' + messageId + '/forward', {recipient: recpientEmail}).respond({});

            MessageService.forwardMessage(messageId, recpientEmail);

            httpBackend.flush();
        });

    });


});

