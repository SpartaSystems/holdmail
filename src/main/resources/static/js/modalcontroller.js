(function () {
    'use strict';

    angular.module('HoldMailApp')
        .controller('ModalCtrl', ['$scope', '$uibModalInstance', '$http', function ($scope, $modalInstance, $http) {

            $scope.message = $modalInstance.message;

            // todo: let backend determine content type
            $scope.messageDownloadURL = '/rest/messages/' + $scope.message.messageId + '/content?mode=HTML';

            var modalCtrl = this;

            modalCtrl.close = function () {
                $modalInstance.close();
            };


        }]);

}());
