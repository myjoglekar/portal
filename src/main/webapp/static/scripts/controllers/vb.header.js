app.controller('HeaderController', function ($scope, $http, $stateParams) {

    $http.get('admin/dealerProduct').success(function (response) {
       $scope.dashboards = response;
    });

    $scope.selectName = "Select Dashboard";
    $scope.change = function (dashboard) {
        $scope.selectName = dashboard.productName;
    };
    
    
    $http.get('admin/dealer').success(function (response) {
        $scope.dealers = response;
    });
    
});