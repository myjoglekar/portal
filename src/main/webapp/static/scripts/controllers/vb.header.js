app.controller('HeaderController', function ($scope, $http, $stateParams) {

    $http.get('static/datas/dashboard.json').success(function (response) {
        $scope.dashboards = response;
    });

    $scope.selectName = "Select Dashboard";
    $scope.change = function (dashboard) {
        $scope.selectName = dashboard.dashboardName;
    };
    
    $http.get('static/datas/dealer.json').success(function (response) {
        $scope.dealers = response;
    });
    
});