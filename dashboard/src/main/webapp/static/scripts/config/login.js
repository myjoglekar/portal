var app = angular.module("loginApp", ['ngCookies', 'LocalStorageModule']);
app.controller("LoginController", function ($scope, $http, $window, $cookies, localStorageService, $timeout) {
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
                //$cookies.putObject("isAdmin", response.isAdmin);
                $window.location.href = 'index.html' 
                $scope.loadDashboard = false;
            }
        });
        $scope.login = "";
    };
});
