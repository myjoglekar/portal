app.controller('TabController', function ($scope, $http, $stateParams) {

    $http.get("static/datas/" + $stateParams.dashboardId + "Tab.json").success(function (response) {
        $scope.tabs = response;
    });

    $scope.tabs = [];
    var uid = 7;
    $scope.addTab = function (tab) {
        $scope.id = uid++;
        tab.tabItems.push({tabId: $scope.id, tabName: "New Tab", tabClose: "isClose"});
    };
    console.log($scope.tabs);

    $scope.deleteTab = function (index, tab) {
        tab.tabItems.splice(index, 1);
    };

});