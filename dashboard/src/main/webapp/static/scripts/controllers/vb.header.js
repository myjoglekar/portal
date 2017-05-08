app.controller('HeaderController', function ($scope, $cookies, $http, $filter, $stateParams, $state, $location, $rootScope) {
    $scope.userName = $cookies.getObject("username");
    $scope.fullName = $cookies.getObject("fullname");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;
});
