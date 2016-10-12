/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller("ReportController", function ($scope, $http, $stateParams) {
    $scope.reports = [];
    $scope.addParent = function () {
        $scope.reports.push({isEdit: true, childItems: []});
    }
//    var uid = 5;
    $scope.addChild = function (report) {
//        report.child = uid++
        report.childItems.push({isEdit: true});
        console.log(report.childItems)
    }
    
    console.log($stateParams.reportId);
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    })
    $scope.childID = $stateParams.reportId;
    $scope.datas = [];
    if ($stateParams.reportId) {
        $http.get('static/datas/' + $stateParams.reportId + '.json').success(function (response) {
            angular.forEach(response, function (value, key) {
                $scope.datas.push(value);
            })
        })
    }

});
