//app.controller("MenuController", function ($scope, $http, $stateParams, $filter, $state, $rootScope) {
//
//
//    console.log("TabId : ")
//    console.log($stateParams.tabId)
//
//
//    $scope.reportId = $stateParams.reportId;
//    $scope.startDate = $stateParams.startDate;
//    $scope.endDate = $stateParams.endDate;
//    $scope.productId = $stateParams.productId;
//    $scope.reportId = $stateParams.reportId;
//    $scope.dealerId = $stateParams.dealerId;
//
//
//    $scope.setParamsDate = function (product) {
//        console.log("TabId : ")
//        console.log($stateParams.tabId)
//        console.log(product.id)
//        console.log(product)
//        $stateParams.productId = product.id;
//        $scope.startDate = $stateParams.startDate;
//        $scope.endDate = $stateParams.endDate;
//        $scope.dealerId = $stateParams.dealerId;
////        $http.get("admin/ui/dbTabs/" + $stateParams.productId).success(function (response) {
////            $stateParams.tabId = response[0].id;
////        });
//        //$stateParams.tabId = "";
//    };
//
////    $scope.reports = [];
////    $scope.addParent = function () {
////        $scope.reports.push({isEdit: true, childItems: []});
////    };
////    $scope.addChild = function (report) {
////        report.childItems.push({isEdit: true});
////    };
////    $scope.save = function (item) {
////        console.log("Item Name : " + item);
////    };
////    $http.get('static/datas/report.json').success(function (response) {
////        $scope.reports = response;
////    });
////
////    $http.get('admin/ui/product/' + $stateParams.dealerId).success(function (response) {
////        $scope.products = response;
////        $scope.name = $filter('filter')($scope.products, {id: $stateParams.productId})[0];
////        $scope.selectProductName = $scope.name.productName;
////    });
////
////    $rootScope.$on('dealerChange', function (event, args) {
////        $http.get('admin/ui/product/' + $stateParams.dealerId).success(function (response) {
////            $scope.products = response;
////            $stateParams.productId = response[0].id;
////            $scope.name = $filter('filter')($scope.products, {id: $stateParams.productId})[0];
////            $scope.selectProductName = $scope.name.productName;
////        });
////    });
//
//
//    $scope.loadNewUrl = function () {
//        try {
//            $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
//
//            $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
//        } catch (e) {
//        }
//        if ($scope.getCurrentPage() === "dashboard") {
//            $state.go("index.dashboard." + $scope.getCurrentTab(), {dealerId: $stateParams.dealerId, productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
//        } else if ($scope.getCurrentPage() === "reports") {
//            $state.go("index.report.reports", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
//        } else if ($scope.getCurrentPage() === "newOrEdit") {
//            $state.go("index.report.newOrEdit", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, startDate: $scope.startDate, endDate: $scope.endDate});
//        } //else if ($scope.getCurrentPage() === "dataSource") {
////            $state.go("index.dataSource", {dealerId: $stateParams.dealerId, startDate: $scope.startDate, endDate: $scope.endDate});
////        } else if ($scope.getCurrentPage() === "dataSet") {
////            $state.go("index.dataSet", {dealerId: $stateParams.dealerId, startDate: $scope.startDate, endDate: $scope.endDate});
////        } else {
////            $location.path("/" + "?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
////        }
//    };
//    $scope.getCurrentPage = function () {
//        var url = window.location.href;
//        console.log(url)
//        if (url.indexOf("widget") > 0) {
//            return "dashboard";
//        }
//        if (url.indexOf("newOrEdit") > 0) {
//            return "newOrEdit";
//        }
//        if (url.indexOf("report") > 0) {
//            return "reports";
//        }
////        if (url.indexOf("dataSource") > 0) {
////            return "dataSource";
////        }
////        if (url.indexOf("dataSet") > 0) {
////            return "dataSet";
////        }
//        return "dashboard";
//    };
//    $scope.getCurrentTab = function () {
//        var url = window.location.href;
//        if (url.indexOf("widget") > 0) {
//            return "widget";
//        }
//        return "widget";
//    };
//});
