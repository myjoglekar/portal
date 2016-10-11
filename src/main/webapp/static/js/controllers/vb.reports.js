/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller("ReportController", function ($scope, $http) {
    $scope.reports = [];
    $scope.add=function(){
        $scope.reports.push({name:"New Report"});
    };
});
