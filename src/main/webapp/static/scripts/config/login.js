var app = angular.module("loginApp", ['ngCookies']);
app.controller("LoginController", function ($scope, $http, $window, $cookies) {
    $scope.authenticate = function (login) {
        $scope.errorMessage = "";
        if (!$scope.adminForm.$valid) {
            return;
        }
        $http({method: "POST", url: "admin/user/login", data: login}).success(function (response) {
            if (!response.username) {
                $scope.errorMessage = response.errorMessage;
            } else {
                $cookies.putObject("username", response.username);
                $cookies.putObject("isAdmin", response.isAdmin);
                $window.location.href = 'index.html';
            }
        });
        $scope.login = "";
    };
});
