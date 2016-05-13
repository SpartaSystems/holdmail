(function () {
    'use strict';

    angular.module('HoldMailApp')
        .controller('ModalCtrl', ['$scope', '$uibModalInstance', '$http', function ($scope, $modalInstance, $http) {

            $scope.message = $modalInstance.message;

            var modalCtrl = this;

            modalCtrl.close = function () {
                $modalInstance.close();
            };


        }]);

}());
