app.controller('UiController', function ($scope, $http, $stateParams, $state, $filter, $cookies, $timeout, localStorageService) {
    $scope.permission = localStorageService.get("permission");

    $scope.selectTabID = $state;
    $scope.userName = $cookies.getObject("username");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;

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

    $http.get('admin/user/sampleDealers').success(function (response) {
        $scope.dealers = response;
    });

    $scope.loadTab = true;
    $http.get("admin/ui/dbTabs/" + $stateParams.productId).success(function (response) {
        console.log(response)
        $scope.loadTab = false;
        $scope.tabs = response;
        console.log(response)
        angular.forEach(response, function (value, key) {
            $scope.dashboardName = value.dashboardId.dashboardTitle;
        });
        $scope.startId = response[0].id ? response[0].id : 0;
        $state.go("index.dashboard.widget", {tabId: $stateParams.tabId ? $stateParams.tabId : $scope.startId, reload: true});
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
    };

    $scope.deleteTab = function (index, tab) {
        $http({method: 'DELETE', url: 'admin/ui/dbTab/' + tab.id}).success(function (response) {
        });
        console.log(tab)
    };

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
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    });

    $scope.onDropTabComplete = function (index, tab, evt) {
        var otherObj = $scope.tabs[index];
        var otherIndex = $scope.tabs.indexOf(tab);
        $scope.tabs[index] = tab;
        $scope.tabs[otherIndex] = otherObj;
        console.log($scope.tabs);
        var tabOrder = $scope.tabs.map(function (value, key) {
            return value.id;
        }).join(',');
        console.log(tabOrder);
        var data = {tabOrder: tabOrder};
        $http({method: 'GET', url: 'admin/ui/dbTabUpdateOrder/' + $stateParams.productId + "?tabOrder=" + tabOrder});
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
        }
        $http({method: 'PUT', url: 'admin/ui/dbTabs/' + $stateParams.productId, data: data})
        tab.editing = false;
        $scope.editedItem = null;

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
