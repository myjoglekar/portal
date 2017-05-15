app.controller('MapReportController', function ($scope, $http) {

    $scope.mapReports = []

    $scope.addMapReport = function () {
        $scope.mapReports.push({isEdit: true})
    };

    $http.get('admin/mapreport/mapReport').success(function (response) {
        $scope.mapReports = response;
    });

    $scope.saveMapReport = function (mapReport) {
        var data = {
            id: mapReport.id,
            reportName: mapReport.reportName,
            mapUrlPath: mapReport.mapUrlPath,
            level: mapReport.level,
            segment: mapReport.segment
        };

        $http({method: mapReport.id ? 'PUT' : 'POST', url: 'admin/mapreport/mapReport', data: data}).success(function (response) {

        });
    };

});