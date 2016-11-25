app.controller('UiController', function ($scope, $http, $stateParams, $state) {

    // $scope.dashboardName = $stateParams.tabId

//    $http.get("admin/ui/dashboard").success(function () {
//
//    })
//    console.log($stateParams.widgetId)
    if ($stateParams.tabId == 'dashboard') {
        $stateParams.tabId = 1;
    }

    console.log("tabId : " + $stateParams.tabId)
    $http.get("admin/ui/dbTabs/" + $stateParams.tabId).success(function (response) {
        $scope.tabs = response;
        console.log("id : "+response[0].id)
        $state.go("dashboard.widget",{widgetId: response[0].id});
    });

//    $http.get("admin/ui/dbWidget/1").success(function (response) {
//        $scope.widgets = response;
//        //$scope.defaultWidget = response[0];
//    });

    $scope.tabs = [];
    var uid = 7;
    $scope.addTab = function () {
        $scope.tabs.push({tabId: $scope.id, tabName: "New Tab", tabClose: "isClose"});
        var tabData = {
            tabName: 'New Tab'
        }
        $http({method: 'POST', url: 'admin/ui/dbTabs', data: tabData}).success(function (response) {

        });
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