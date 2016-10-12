/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller("ReportController", function ($scope, $http, $stateParams) {
    $scope.reports = [];
    $scope.add = function () {
        $scope.reports.push({childName: "New Report"});
    };
    console.log($stateParams.reportId);
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    })
    $scope.parentID = $stateParams.reportId
    if ($stateParams.reportId) {
        $http.get('static/datas/' + $stateParams.reportId + '.json').success(function (response) {
            $scope.data = response;
            console.log($scope.data);
        })
    }

});
