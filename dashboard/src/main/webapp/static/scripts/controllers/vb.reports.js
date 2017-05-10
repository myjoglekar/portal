app.controller("ReportController", function ($scope, $http, $stateParams, $state, $window, $rootScope) {
    
    $rootScope.printReport(false, false)
    console.log($stateParams.startDate);
    console.log($stateParams.endDate);
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.reportId = $stateParams.reportId ? $stateParams.reportId : 0;
    $scope.dealerId = $stateParams.dealerId ? $stateParams.dealerId : 0;
    $scope.reports = []
    $scope.reportWidgets = [];

    $http.get("admin/report/getReport").success(function (response) {
        $scope.reports = response;
        angular.forEach($scope.report, function (value, key) {
            //$scope.logo = window.atob(value.logo);
        });
    });

    $scope.deleteReport = function (report, index) {
        $http({method: 'DELETE', url: 'admin/report/report/' + report.id}).success(function (response) {
            $scope.reports.splice(index, 1);
        });
    };

    $scope.downloadReport = function (report) {
        var url = "admin/proxy/downloadReport/" + report.id + "?dealerId=" + $stateParams.dealerId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate;
        $window.open(url);
        //state.transitionTo('index.reportIndex',null,{reload: true});
    };

    // http://localhost:8080/VizBoard/admin/proxy/downloadReport/" + reportId + "?dealerId=" + dealerId + "&startDate=" + startDateStr + "&endDate=" + endDateStr;
});

