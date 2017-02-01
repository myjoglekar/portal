app.controller('ReportIndexController', function ($scope, $stateParams, $state) {
    console.log($scope.productId)
    console.log($scope.reportId)
    console.log($scope.startDate)
    console.log($scope.endDate)
    console.log($scope.locationId)
    $scope.$state = $state;//Find Active Tabs
    $scope.productId = $stateParams.productId ? $stateParams.productId : 0;
    $scope.reportId = $stateParams.reportId ? $stateParams.reportId : 0;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.locationId = $stateParams.locationId;
});