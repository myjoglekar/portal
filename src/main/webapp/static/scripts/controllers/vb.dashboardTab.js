app.controller('TabController', function ($scope, $http, $stateParams) {
    $http.get("static/datas/" + $stateParams.tabId + "Tab.json").success(function (response) {
        $scope.tabs = response;
    });    
    console.log("StateParams Tab : " + $stateParams)
})