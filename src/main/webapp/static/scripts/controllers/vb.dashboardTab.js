app.controller('UiController', function ($scope, $http, $stateParams) {

    // $scope.dashboardName = $stateParams.tabId

    $http.get("admin/ui/dashboard").success(function () {

    })
    console.log($stateParams.widgetId)


    $http.get("admin/ui/dbTabs/1").success(function (response) {
        $scope.tabs = response;
    });

    $http.get("admin/ui/dbWidget/1").success(function (response) {
        $scope.widgets = response;
        //$scope.defaultWidget = response[0];
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
//app.filter('capitalize', function() {
//    return function(input) {
//      return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
//    }
//});