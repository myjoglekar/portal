app.controller('TabController', function ($scope, $http, $stateParams) {

$scope.dashboardName = $stateParams.tabId

    $http.get("static/datas/" + $stateParams.tabId + "Tab.json").success(function (response) {
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
//app.filter('capitalize', function() {
//    return function(input) {
//      return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
//    }
//});