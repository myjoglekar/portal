app.controller('UiController', function ($scope, $http, $stateParams, $state) {

//     $scope.dashboardName = $stateParams.tabId

//    $http.get("admin/ui/dashboard").success(function () {
//
//    })
//    console.log($stateParams.widgetId)
    if ($stateParams.tabId == 'dashboard') {
        $stateParams.tabId = 1;
    }
    $scope.currentTabId = $stateParams.tabId

    console.log("tabId : " + $stateParams.tabId)
    $http.get("admin/ui/dbTabs/" + $stateParams.tabId).success(function (response) {
        $scope.tabs = response;
        angular.forEach(response, function (value, key) {
            $scope.dashboardName = value.dashboardId.dashboardTitle;
        })
        $state.go("dashboard.widget", {widgetId: response[0].id});
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
    
    $scope.reports = [];
    $scope.addParent = function () {
        $scope.reports.push({isEdit: true, childItems: []});
    };
    $scope.addChild = function (report) {
        report.childItems.push({isEdit: true});
    };
    $scope.save = function(item){
        console.log("Item Name : "+item);
    };
    //console.log($stateParams.reportId);
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    });


});