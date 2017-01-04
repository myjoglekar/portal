app.controller("MenuController", function ($scope, $http, $stateParams, $filter, $state) {
    $scope.reportId = $stateParams.reportId;
    console.log($stateParams.reportId)
    console.log($stateParams.tabId);

    $scope.productId = $stateParams.productId;

    $scope.setParamsDate = function () {
        // alert($stateParams.startDate + " - " + $stateParams.endDate)
        console.log($stateParams.startDate + " - " + $stateParams.endDate)
        $scope.startDate = $stateParams.startDate;
        $scope.endDate = $stateParams.endDate;
    }

    //try {
    //alert("Test : " + $stateParams.startDate + " - " + $stateParams.endDate);
    //$scope.startDate = $stateParams.startDate //? $stateParams.startDate : moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY');//$scope.startDate.setDate($scope.startDate.getDate() - 1);
//        $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
    //$scope.endDate = $stateParams.endDate //? $stateParams.endDate : moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY');
//        $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
    // } catch (e) {
    //}

    //$scope.tabId = $stateParams.tabId;
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

//    $scope.selectProductName = "Select Product";
//    $scope.changeProduct = function (product) {
//        $scope.selectProductName = product.productName;
//        $scope.dashboardId = product.id;
//    };
    $http.get('admin/ui/product').success(function (response) {
        $scope.products = response;
        // $scope.searchProduct.unshift({"id": 0, "productName": "All Product"});
        $scope.name = $filter('filter')($scope.products, {id: $stateParams.dashboardId})[0];
        $scope.selectProductName = $scope.name.productName;
        console.log($scope.selectProductName)
    });
})