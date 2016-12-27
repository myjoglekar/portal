app.controller('ReportIndexController', function ($scope, $stateParams, $state) {
    $scope.$state = $state;//Find Active Tabs
    $scope.productId = $stateParams.productId;
});