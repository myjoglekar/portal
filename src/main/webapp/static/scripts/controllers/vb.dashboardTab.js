app.controller('TabController', function ($scope, $http, $stateParams) {
    $http.get("static/datas/" + $stateParams.tabId + "Tab.json").success(function (response) {
        $scope.tabs = response;
    });

    $scope.tabs = [];
    var uid = 7;
    $scope.addTab = function (tab) {
        $scope.id = uid++;
        tab.tabItems.push({tabId: "$scope.id", tabName: "New Tab", tabClose: "isClose"});
    };
    console.log("StateParams Tab : " + $stateParams);

    console.log($scope.tabs);
    $scope.removeTab = function (index) {
        $scope.tabs.splice(index, 1);

    };
});