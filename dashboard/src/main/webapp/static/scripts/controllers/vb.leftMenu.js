app.controller("MenuController", function ($scope, $http, $stateParams, $filter, $state) {
    $scope.reportId = $stateParams.reportId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.productId = $stateParams.productId;
    $scope.reportId = $stateParams.reportId;
    $scope.dealerId = $stateParams.dealerId;


    $scope.setParamsDate = function () {
        $scope.startDate = $stateParams.startDate;
        $scope.endDate = $stateParams.endDate;
        $scope.dealerId = $stateParams.dealerId;
    }
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

    $http.get('admin/ui/product').success(function (response) {
        $scope.products = response;
        console.log(response)
        // $scope.searchProduct.unshift({"id": 0, "productName": "All Product"});
        $scope.name = $filter('filter')($scope.products, {id: $stateParams.dashboardId})[0];
        $scope.selectProductName = $scope.name.productName;
        console.log($scope.selectProductName)
    });


})