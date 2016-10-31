app.controller("DashboardController", function ($scope, $http, $stateParams) {
    //chart()
    
    $http.get('static/datas/labels.json').success(function (response) {
        $scope.labels = response;
    });

    
    

    $scope.charts = [];
    $http.get('static/datas/d3chart.json').success(function (response, error) {
        angular.forEach(response, function (value, key) {
            $scope.charts = value;
        });
    });
});