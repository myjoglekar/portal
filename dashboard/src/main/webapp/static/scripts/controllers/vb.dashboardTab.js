app.controller('UiController', function ($scope, $http, $stateParams, $state) {
    $scope.selectTabID = $state;
    //$scope.match = $stateParams.tabId
    //console.log($scope.s.params.tabId)
    //console.log("Dashboard : "+sessionStorage);
    $scope.setActiveTab = function (activeTab) {
        sessionStorage.setItem("activeTab", activeTab);
    };
    
    // Get active tab from localStorage
    $scope.getActiveTab = function () {
        return sessionStorage.getItem("activeTab");
    };
    
    // Check if current tab is active
    $scope.isActiveTab = function (tabName, index) {
        var activeTab = $scope.getActiveTab();
        return (activeTab === tabName || (activeTab === null && index === 0));
    }
    
    
    
    var counter = 1;
    $scope.tabs = [];

    var addTab = function () {
        $scope.tabs.push({tabName: "New Tab", title: 'Tab ' + counter, content: 'Tab ' + counter, tabClose: 'isClose'});
        counter++;
        $scope.tabs[$scope.tabs.length - 1].active = true;
    };

    var removeTab = function (event, index) {
        event.preventDefault();
        event.stopPropagation();
        $scope.tabs.splice(index, 1);
    };

    $scope.addTab = addTab;
    $scope.removeTab = removeTab;

    for (var i = 0; i < 5; i++) {
        addTab();
    }

//     $scope.dashboardName = $stateParams.tabId

//    $http.get("admin/ui/dashboard").success(function () {
//
//    })
//    console.log($stateParams.widgetId)
    if ($stateParams.tabId == 'dashboard') {
        $stateParams.tabId = 1;
    }
    //$scope.currentTabId = $stateParams.tabId

    console.log("tabId : " + $stateParams.tabId)
    $http.get("admin/ui/dbTabs/" + $stateParams.tabId).success(function (response) {
        $scope.tabs = response;
        angular.forEach(response, function (value, key) {
            $scope.dashboardName = value.dashboardId.dashboardTitle;
        })
        $state.go("dashboard.widget", {widgetId: $stateParams.widgetId ? $stateParams.widgetId : response[0].id});
    });

//    $http.get("admin/ui/dbWidget/1").success(function (response) {
//        $scope.widgets = response;
//        //$scope.defaultWidget = response[0];
//    });

//    $scope.tabs = [];
//    var uid = 7;
//    $scope.addTab = function () {
//        $scope.tabs.push({tabId: $scope.id, tabName: "New Tab", tabClose: "isClose"});
//        var tabData = {
//            tabName: 'New Tab'
//        }
//        $http({method: 'POST', url: 'admin/ui/dbTabs', data: tabData}).success(function (response) {
//
//        });
//    };
//    console.log($scope.tabs);
//
//    $scope.deleteTab = function (index, tab) {
//        tab.tabItems.splice(index, 1);
//    };

    $scope.reports = [];
    $scope.addParent = function () {
        $scope.reports.push({isEdit: true, childItems: []});
    };
    $scope.addChild = function (report) {
        report.childItems.push({isEdit: true});
    };
    $scope.save = function (item) {
        console.log("Item Name : " + item);
    };
    //console.log($stateParams.reportId);
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    });


})
//        .directive('tabHighlight', [function () {
//                return {
//                    restrict: 'A',
//                    link: function (scope, element) {
//                        var x, y, initial_background = '#c3d5e6';
//
//                        element
//                                .removeAttr('style')
//                                .mousemove(function (e) {
//                                    // Add highlight effect on inactive tabs
//                                    if (!element.hasClass('active'))
//                                    {
//                                        x = e.pageX - this.offsetLeft;
//                                        y = e.pageY - this.offsetTop;
//
//                                        element
//                                                .css({background: '-moz-radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background})
//                                                .css({background: '-webkit-radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background})
//                                                .css({background: 'radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background});
//                                    }
//                                })
//                                .mouseout(function () {
//                                    element.removeAttr('style');
//                                });
//                    }
//                };
//            }]);
//        .directive('activeLink', ['$location', function (location) {
//                return {
//                    restrict: 'A',
//                    link: function (scope, element, attrs, controller) {
//                        var clazz = attrs.activeLink;
//                        var path = attrs.href;
//                        path = path.substring(1); //hack because path does not return including hashbang
//                        scope.location = location;
//                        scope.$watch('location.path()', function (newPath) {
//                            if (path === newPath) {
//                                element.addClass(clazz);
//                            } else {
//                                element.removeClass(clazz);
//                            }
//                        });
//                    }
//                };
//            }]);