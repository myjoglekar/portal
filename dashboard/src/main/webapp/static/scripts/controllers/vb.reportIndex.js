app.controller('ReportIndexController', function ($scope, $stateParams, $state) {
    $scope.$state = $state;//Find Active Tabs
    $scope.productId = $stateParams.productId ? $stateParams.productId : 0;
    $scope.reportId = $stateParams.reportId ? $stateParams.reportId : 0;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.dealerId = $stateParams.dealerId;
});