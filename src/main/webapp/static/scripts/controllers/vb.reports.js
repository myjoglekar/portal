/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller("ReportController", function ($scope, $http, $stateParams) {
    $scope.reports = [];
    $scope.addParent = function () {
        $scope.reports.push({parentName: "New Parent Name", childItems: []});
    }
    $scope.addChild = function (report) {
        report.childItems.push({childName: "New Child Name"});
    }
    console.log($stateParams.reportId);
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    })
    $scope.parentID = $stateParams.reportId;
    $scope.datas = [];
    if ($stateParams.reportId) {
        $http.get('static/datas/' + $stateParams.reportId + '.json').success(function (response) {
            angular.forEach(response, function (value, key) {
                $scope.datas.push(value);
            })
        })
    }

});
