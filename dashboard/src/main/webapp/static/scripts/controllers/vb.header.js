app.controller('HeaderController', function ($scope, $cookies, $http, $filter, $stateParams, $state) {
    $scope.userName = $cookies.getObject("username");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;

    console.log("Header : ")
    console.log($stateParams.startDate)

    
//    $scope.startDate = new Date()
//    $scope.startDate = $stateParams.startDate ? $stateParams.startDate : $scope.startDate.setDate($scope.startDate.getDate() - 29);
//    $scope.endDate = $stateParams.endDate ? $stateParams.endDate : new Date();
//    try {
//        $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
//
//        $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.endDate;
//    } catch (e) {
//    }


    $scope.loadNewUrl = function () {
//        try {
//            $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
//
//            $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.endDate;
//        } catch (e) {
//        }
        console.log($stateParams);
        console.log($scope.getCurrentTab());
        console.log($scope.getCurrentPage());
        if ($scope.getCurrentPage() === "dashboard") {
            $state.go("index.dashboard." + $scope.getCurrentTab(), {productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
        } else if ($scope.getCurrentPage() === "report") {
            //alert("GO");
            $state.go("index.report.reports", {productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
        } else {
            $location.path("/" + "?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
        }
    };
    $scope.getCurrentPage = function () {
        var url = window.location.href;
        if (url.indexOf("widget") > 0) {
            return "dashboard";
        }
        if (url.indexOf("report") > 0) {
            return "report";
        }
        return "dashboard";
    };
    $scope.getCurrentTab = function () {
        var url = window.location.href;
        if (url.indexOf("widget") > 0) {
            return "widget";
        }
        return "widget";
    };


    //$scope.startDate = new Date();
    //$scope.startDate.setDate($scope.startDate.getDate() - 1);
    //$scope.endDate = new Date();

//    $scope.defaultDate = new Date();
//    $scope.defaultDate.setDate($scope.defaultDate.getDate() - 1);



    console.log($stateParams.productId);
    console.log($stateParams.tabId);
    console.log($stateParams.dashboardTypeId);

//    $scope.selectDashboardType = "Select Type";
//    $scope.dashboardTypes = [{dashboardType: 1, name: "Dashboard"}, {dashboardType: 2, name: "Reports"}];
//    $scope.selectDashboardName = $filter('filter')($scope.dashboardTypes, {dashboardType: $stateParams.dashboardTypeId})[0];
//    $scope.selectDashboardType = $scope.selectDashboardName.name;
//    $scope.selectDashboard = function (name) {
//        $scope.selectDashboardType = name;
//    };

    $scope.selectProductName = "Select Product";
    $scope.changeProduct = function (product) {
        $scope.selectProductName = product.productName;
        $scope.productId = product.id;
    };
    $http.get('admin/ui/product').success(function (response) {
        $scope.products = response;
        // $scope.searchProduct.unshift({"id": 0, "productName": "All Product"});
        $scope.name = $filter('filter')($scope.products, {id: $stateParams.productId})[0];
        $scope.selectProductName = $scope.name.productName;
        console.log($scope.selectProductName);
    });

    $http.get('admin/dealer').success(function (response) {
        $scope.dealers = response;
    });

//    $http.get("admin/ui/dbTabs/" + $stateParams.productId).success(function (response) {
//        $scope.tabs = response;
//        console.log(response)
//        angular.forEach(response, function (value, key) {
//            $scope.dashboardName = value.dashboardId.dashboardTitle;
//        });
//       // $state.go("index.dashboard.widget", {tabId: $stateParams.tabId ? $stateParams.tabId : response[0].id, reload: true});
////        $stateParams.tabId = $stateParams.tabId ? $stateParams.tabId : response[0].id
//    });

    var dates = $(".pull-right i").text();
    $(function () {
        //Initialize Select2 Elements
        $(".select2").select2();
        //Datemask dd/mm/yyyy
        $("#datemask").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});
        //Datemask2 mm/dd/yyyy
        $("#datemask2").inputmask("mm/dd/yyyy", {"placeholder": "mm/dd/yyyy"});
        //Money Euro
        $("[data-mask]").inputmask();
        //Date range picker
        $('#reservation').daterangepicker();
        //Date range picker with time picker
        $('#reservationtime').daterangepicker({timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'});
        //Date range as a button
        $('#daterange-btn').daterangepicker(
                {
                    ranges: {
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                    },
                    startDate: moment().subtract(29, 'days'),
                    endDate: moment()
                },
                function (start, end) {
                    $('#daterange-btn span').html(start.format('MMMM/DD/YYYY') + ' - ' + end.format('MMMM/DD/YYYY'));
                }
        );
        //Date picker
        $('#datepicker').datepicker({
            autoclose: true
        });
        //iCheck for checkbox and radio inputs
        $('input[type="checkbox"].minimal,  input[type="radio"].minimal').iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        //Red color scheme for iCheck
        $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
            checkboxClass: 'icheckbox_minimal-red',
            radioClass: 'iradio_minimal-red'
        });
        //Flat red color scheme for iCheck
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });
        //Colorpicker
        $(".my-colorpicker1").colorpicker();
        //color picker with addon
        $(".my-colorpicker2").colorpicker();
        //Timepicker
        $(".timepicker").timepicker({
            showInputs: false
        });
    });
});
