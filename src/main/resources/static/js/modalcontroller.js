
(function () {
    'use strict';

    angular.module('HoldMailApp')
        .controller('ModalCtrl', ['$scope', '$uibModalInstance', function ($scope, $modalInstance) {

            $scope.selectedMail = $modalInstance.selectedMail;

            var modalCtrl = this;

            modalCtrl.close = function () {
                $modalInstance.close();
            };


        }]);

}());
