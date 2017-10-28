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

        .service('MessageService', ['$http', function ($http) {

            var MESSAGES_ENDPOINT = '/rest/messages';

            var messageSvc = ({
                messages: [],
                messageDetail: null,
                getMessageList: getMessageList,
                getMessageDetail: getMessageDetail,
                getMessageHTMLURI: getMessageHTMLURI,
                forwardMessage: forwardMessage
            });
            return messageSvc;

            function getMessageHTMLURI(messageId) {
                return MESSAGES_ENDPOINT + '/' + messageId + '/html'
            }

            /**
             * Fetch the list of messages from the backend
             * @param size The max number of messages to return
             * @param page The page number, if the result set is greater than {@link size} messages
             * @param recipientEmail [optional] The email address for the recipient whose emails are being queried
             */
            function getMessageList(size, page, recipientEmail) {

                var params = ['size=' + size, 'page=' + page];
                if (recipientEmail) {
                    params.push('recipient=' + encodeURIComponent(recipientEmail));
                }
                var messageListURI = MESSAGES_ENDPOINT + '?' + params.join('&');

                return $http.get(messageListURI)
                    .then(function (data) {
                        return data;
                    })
                    .catch(function (data, status) {
                        logError('Failed GET to ' + messageListURI, data, status);
                    });
            }

            /**
             * Fetch the full details for a specific message
             * @param messageId The message ID
             */
            function getMessageDetail(messageId) {

                var messageDetailURI = MESSAGES_ENDPOINT + '/' + messageId;
                return $http.get(messageDetailURI)
                    .then(function (data) {
                        return data;
                    })
                    .catch(function (data, status) {
                        logError('Failed GET to ' + messageDetailURI, data, status);
                    });

            }

            /**
             * Forward the specified message to the desired recipient email
             * @param messageId The message ID
             * @param recipientEmail The recipient email
             */
            function forwardMessage(messageId, recipientEmail) {

                var messageForwardURI = MESSAGES_ENDPOINT + '/' + messageId + '/forward';

                return $http.post(messageForwardURI, {
                    recipient: recipientEmail
                })
                    .catch(function (data, status) {
                        logError('Failed POST to ' + messageForwardURI, data, status);
                    });
            }

            function logError(message, data, status) {
                console.log(message + ' [status=' + status + ', message=' + JSON.stringify(data) + ']');

            }

        }]);

}());
