app.controller('UiController', function ($scope, $http, $stateParams, $state, $filter, $cookies, $timeout, localStorageService) {
    $scope.permission = localStorageService.get("permission");

    $scope.selectTabID = $state;
    $scope.userName = $cookies.getObject("username");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;
    console.log($stateParams.startDate + " - " + $stateParams.endDate);
    $scope.dataCheck = function () {
        console.log($stateParams.startDate + " - " + $stateParams.endDate);
    }

    $scope.toDate = function (strDate) {
        if (!strDate) {
            return new Date();
        }
        var from = strDate.split("/");
        var f = new Date(from[2], from[0] - 1, from[1]);
        return f;
    };

    $scope.getDay = function () {
        var today = new Date();
        var yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 29);
        return yesterday;
    }

    $scope.firstDate = $stateParams.startDate ? $scope.toDate(decodeURIComponent($stateParams.startDate)) : $scope.getDay().toLocaleDateString("en-US");
    $scope.lastDate = $stateParams.endDate ? $scope.toDate(decodeURIComponent($stateParams.endDate)) : new Date().toLocaleDateString("en-US");

    if (!$stateParams.startDate) {
        $stateParams.startDate = $scope.firstDate;
    }
    if (!$stateParams.endDate) {
        $stateParams.endDate = $scope.lastDate;
    }

    try {
        $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

        $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
    } catch (e) {
    }


    $scope.editDashboardTab = function (tab) {
        var data = {
            tabNames: tab.tabName
        }
        $scope.tab = data;
        $timeout(function () {
            $('#editTab' + tab.id).modal('show');
        }, 100);
    };

    $scope.selectProductName = "Select Product";
    $scope.changeProduct = function (product) {
        $scope.selectProductName = product.productName;
        $scope.productId = product.id;
    };
    $http.get('admin/ui/product').success(function (response) {
        $scope.products = response;
        $scope.name = $filter('filter')($scope.products, {id: $stateParams.productId})[0];
        $scope.selectProductName = $scope.name.productName;
        console.log($scope.selectProductName);
    });    

    $scope.loadTab = true;
    $http.get("admin/ui/dbTabs/" + $stateParams.productId).success(function (response) {
        $scope.loadTab = false;
        $scope.tabs = response;
        angular.forEach(response, function (value, key) {
            $scope.dashboardName = value.dashboardId.dashboardTitle;
        });
        $scope.startId = response[0].id ? response[0].id : 0;
        $state.go("index.dashboard.widget", {tabId: $stateParams.tabId ? $stateParams.tabId : $scope.startId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
    });

    var dates = $(".pull-right i").text();

    $scope.tabs = [];
    $scope.addTab = function (tab) {
        var data = {
            tabName: tab.tabName
        };
        $http({method: 'POST', url: 'admin/ui/dbTabs/' + $stateParams.productId, data: data}).success(function (response) {
            $scope.tabs.push({id: response.id, tabName: tab.tabName, tabClose: true});
        });
        tab.tabName = "";
    };

    $scope.deleteTab = function (index, tab) {
        $http({method: 'DELETE', url: 'admin/ui/dbTab/' + tab.id}).success(function (response) {
            $scope.tabs.splice(index, 1);
        });
    };

    $scope.reports = [];
    $scope.addParent = function () {
        $scope.reports.push({isEdit: true, childItems: []});
    };
    $scope.addChild = function (report) {
        report.childItems.push({isEdit: true});
    };

    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    });

    $scope.onDropTabComplete = function (index, tab, evt) {
        if (tab !== "" && tab !== null) {
            var otherObj = $scope.tabs[index];
            var otherIndex = $scope.tabs.indexOf(tab);
            $scope.tabs[index] = tab;
            $scope.tabs[otherIndex] = otherObj;
            console.log($scope.tabs);
            var tabOrder = $scope.tabs.map(function (value, key) {
                if (value) {
                    return value.id;
                }
            }).join(',');
            if (tabOrder) {
                $http({method: 'GET', url: 'admin/ui/dbTabUpdateOrder/' + $stateParams.productId + "?tabOrder=" + tabOrder});
            }
        }
    };

    $scope.editedItem = null;
    $scope.startEditing = function (tab) {
        tab.editing = true;
        $scope.editedItem = tab;
    }

    $scope.doneEditing = function (tab) {
        console.log(tab)
        console.log(tab.tabName)
        var data = {
            id: tab.id,
            createdTime: tab.createdTime,
            dashboardId: tab.dashboardId,
            modifiedTime: tab.modifiedTime,
            remarks: tab.remarks,
            status: tab.status,
            tabName: tab.tabName,
            tabOrder: tab.tabOrder
        };
        $http({method: 'PUT', url: 'admin/ui/dbTabs/' + $stateParams.productId, data: data})
        tab.editing = false;
        $scope.editedItem = null;
        //tab.tabName = "";
    };
})
        .directive('ngBlur', function () {
            return function (scope, elem, attrs) {
                elem.bind('blur', function () {
                    scope.$apply(attrs.ngBlur);
                });
            };
        })

        .directive('ngFocus', function ($timeout) {
            return function (scope, elem, attrs) {
                scope.$watch(attrs.ngFocus, function (newval) {
                    if (newval) {
                        $timeout(function () {
                            elem[0].focus();
                        }, 0, false);
                    }
                });
            };
        });
