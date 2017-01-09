app.controller("ReportController", function ($scope, $http, $stateParams) {

    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.reportId = $stateParams.reportId ? $stateParams.reportId : 0;
    $scope.dealerId = $stateParams.dealerId ? $stateParams.dealerId : 0;
    $scope.reportWidgets = [];

    console.log($stateParams.reportId)

    $http.get("admin/ui/report").success(function (response) {
        $scope.reports = response;
        angular.forEach($scope.report, function (value, key) {
            $scope.logo = window.atob(value.logo);
            console.log(value.logo)
        })
    });
});
