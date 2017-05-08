app.controller("LoginController", function ($scope, $http, $window, $cookies, localStorageService, $timeout, $state, $location) {
    $scope.loadDashboard = true;
    $scope.authenticate = function (login) {
        $scope.errorMessage = "";
        if (!$scope.adminForm.$valid) {
            return;
        }
        $http({method: "POST", url: "admin/user/login", data: login}).success(function (response) {
            if (!response.authData) {
                $scope.errorMessage = response.errorMessage;
            } else {
                $cookies.putObject("fullname", response.authData.fullName);
                $cookies.putObject("username", response.authData.userName);
                localStorageService.set("permission", response.authData.permission)
//                $location.path("index/dashboard/1/2") 
                //$cookies.putObject("isAdmin", response.isAdmin);
                $window.location.href = 'index.html' 
//                $window.location.href = 'index.html#/index/dashboard/1/2' 
//$state.go('index.dashboard', {dealerId: 1, productId: 2})
                $scope.loadDashboard = false;
            }
        });
        $scope.login = "";
    };
});
