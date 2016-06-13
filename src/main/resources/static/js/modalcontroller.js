(function () {
    'use strict';

    angular.module('HoldMailApp')

        .controller('ModalCtrl', ['$scope', '$uibModalInstance', 'growl', '$http', function ($scope, $modalInstance, growl, $http) {

            $scope.message = $modalInstance.message;

            // referenced in the iframe for the HTML view tab
            $scope.messageHTMLURL = '/rest/messages/' + $scope.message.messageId + '/html';

            var modalCtrl = this;

            modalCtrl.close = function () {
                $modalInstance.close();
            };

            modalCtrl.forwardMail = function () {

                var messageId = $scope.message.messageId;
                var recipient = $scope.forwardRecipient;

                $http
                    .post('/rest/messages/' + messageId + '/forward', {
                        recipient: recipient
                    })
                    .success(function () {
                        growl.success("Mail " + messageId + ' successfully sent to <b>' + recipient + '</b>', {});
                    })
                    .error(function (data, status) {
                        console.log("couldn't forward email " + JSON.stringify(data));
                        growl.error("The server rejected the request (it probably didn't like that email address " +
                            "- see the logs for more info).", {});
                    });

            }

        }]);

}());
