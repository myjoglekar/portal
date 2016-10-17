/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller("ReportController", function ($scope, $http, $stateParams) {
    $scope.currentPage = 1;
    $scope.pageSize = 10;
    $scope.reports = [];
    $scope.addParent = function () {
        $scope.reports.push({isEdit: true, childItems: []});
    };
    $scope.addChild = function (report) {
        report.childItems.push({isEdit: true});
    };
    $scope.save = function(item){
        console.log("Item Name : "+item);
    };
    console.log($stateParams.reportId);
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    });
    $scope.childID = $stateParams.reportId;
    $scope.datas = [];

    //Table 
    $scope.objectHeader = [];
    $scope.groupProperty = "Select";
    $scope.orderProperty = "Select";
    if ($stateParams.reportId) {
        $http.get('static/datas/' + $stateParams.reportId + '.json').success(function (response) {
            angular.forEach(response, function (value, key) {
                $scope.datas.push(value);
                //iterate header Key
                var arrayIndex = 0;
                $scope.columns = []
                for (property in value) {
                    if ($scope.objectHeader.indexOf(property) === -1) {
                        $scope.objectHeader.push(property);
                    }
                    $scope.columns.push(
                            {title: $scope.objectHeader[arrayIndex], field: $scope.objectHeader[arrayIndex], visible: true}
                    );
                    arrayIndex++;
                    $scope.headerLength = $scope.columns.length;
                }
            });
        });
    }

//Sort-table from header
    $scope.sortColumn = $scope.columns;
    console.log($scope.objectHeader[0])
    $scope.reverse = false;
    $scope.toggleSort = function (index) {
        if ($scope.sortColumn === $scope.objectHeader[index]) {
            $scope.reverse = !$scope.reverse;
        }
        $scope.sortColumn = $scope.objectHeader[index];
    }

//Dir-Paginations
    $scope.pageChangeHandler = function (num) {
        console.log('reports page changed to ' + num);
    };

});
