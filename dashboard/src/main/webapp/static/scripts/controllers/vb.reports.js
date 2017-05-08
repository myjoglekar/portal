app.controller("ReportController", function ($scope, $http, $stateParams) {
    console.log($stateParams.startDate);
    console.log($stateParams.endDate);
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.reportId = $stateParams.reportId ? $stateParams.reportId : 0;
    $scope.dealerId = $stateParams.dealerId ? $stateParams.dealerId : 0;
    $scope.reportWidgets = [];

    $http.get("admin/report/getReport").success(function (response) {
        $scope.reports = response;
        angular.forEach($scope.report, function (value, key) {
            //$scope.logo = window.atob(value.logo);
        });
    });
});
