app.controller("ReportController", function ($scope, $http, $stateParams) {
    console.log($stateParams.reportId)
    console.log($stateParams.startDate)
    console.log($stateParams.endDate)
    $scope.reportWidgets = [];
    $scope.selectDurations = [{duration: "None"},
        {duration: "Last Week"}, {duration: "Last Three Months"},
        {duration: "Last Six Months"}, {duration: "Last Six Months"}]; // Month Durations-Popup

    console.log($stateParams.reportId)

    $http.get("admin/ui/report").success(function (response) {
        $scope.reports = response;
        angular.forEach($scope.report, function (value, key) {
            $scope.logo = window.atob(value.logo);
            console.log($scope.logo)
        })
    });
});
