app.controller('HeaderController', function ($scope, $http, $filter, $stateParams) {
    
        
    //$scope.selectName = "Select Dashboard";
    $scope.change = function (dashboard) {
        $scope.selectName = dashboard.productName;
        $scope.tabId = dashboard.id;
    };
    
    
    console.log($scope.tabId)
    
    
    $http.get('admin/ui/product').success(function (response) {
        $scope.dashboards = response;
        // $scope.searchProduct.unshift({"id": 0, "productName": "All Product"});
        $scope.name = $filter('filter')($scope.dashboards, {id: $stateParams.tabId?$stateParams.tabId:3})[0];
        $scope.selectName = $scope.name.productName;
        console.log($scope.selectName)
    });



    $http.get('admin/dealer').success(function (response) {
        $scope.dealers = response;
    });

});