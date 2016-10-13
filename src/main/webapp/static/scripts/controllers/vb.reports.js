/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller("ReportController", function ($scope, $http, $stateParams) {
    $scope.currentPage = 1;
    $scope.pageSize = 10;
    $scope.reports = [];
    $scope.sortType = 'name'; // set the default sort type
    $scope.sortReverse = false;

    $scope.addParent = function () {
        $scope.reports.push({isEdit: true, childItems: []});
    }
    $scope.addChild = function (report) {
        report.childItems.push({isEdit: true});
        console.log(report.childItems)
    }
    console.log($stateParams.reportId);
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    })
    $scope.childID = $stateParams.reportId;
    $scope.datas = [];
    $scope.objectHeader = [];
    if ($stateParams.reportId) {
        $http.get('static/datas/' + $stateParams.reportId + '.json').success(function (response) {
            angular.forEach(response, function (value, key) {
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
                    console.log($scope.columns)
                }


//                console.log($scope.objectHeader)
                $scope.datas.push(value);
            });
        });
    }
    $scope.displayedCollection = [].concat($scope.datas);


    $scope.sortColumn = $scope.columns;
    console.log($scope.objectHeader[0])
    $scope.toggleSort = function (index) {
        if ($scope.sortColumn === $scope.objectHeader[index]) {
            $scope.reverse = !$scope.reverse;
        }
        $scope.sortColumn = $scope.objectHeader[index];
    }

//Dir-Paginations
    $scope.pageChangeHandler = function (num) {
        console.log('meals page changed to ' + num);
    };

});
