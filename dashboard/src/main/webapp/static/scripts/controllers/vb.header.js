app.controller('HeaderController', function ($scope, $cookies, $http, $filter, $stateParams, $state, $location, $rootScope) {
    $scope.userName = $cookies.getObject("username");
    $scope.fullName = $cookies.getObject("fullname");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;
    
    console.log($scope.tabId)

    //$scope.selectTabID = $state;

    $scope.selectDealer = {};
    $http.get('admin/dealer').success(function (response) {
        //$scope.loadDefault(response[0].id);
        //$stateParams.dealerId = $stateParams.dealerId ? $stateParams.dealerId : response[0].id;
        //$state.go("index.dashboard.widget", {dealerId:$stateParams.dealerId?$stateParams.dealerId:response[0].id, tabId: $stateParams.tabId ? $stateParams.tabId : 1, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
        //$state.go('')
        $scope.dealers = response;
        $scope.name = $filter('filter')($scope.dealers, {id: $stateParams.dealerId})[0];
        $scope.selectDealer.selected = {dealerName: $scope.name.dealerName};
    });

    $scope.getDealerId = function (dealer) {
        console.log(dealer);
        $stateParams.dealerId = dealer.id;
        $rootScope.$emit('dealerChange', {dealerId: dealer.id});
        //$stateParams.tabId = "";
    };

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
    console.log("Day : " + $scope.getDay().toLocaleDateString("en-US"))
    if (!$stateParams.startDate) {
        $stateParams.startDate = $scope.firstDate;
    }
    if (!$stateParams.endDate) {
        $stateParams.endDate = $scope.lastDate;
    }



    console.log($stateParams.dealerId);

    $scope.loadDefault = function (defaultDealerId) {
        console.log($stateParams.dealerId);
        try {
            $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

            $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
        } catch (e) {
        }
        console.log($stateParams.tabId);
        console.log($scope.getCurrentTab());
        console.log($scope.getCurrentPage());
        if ($scope.getCurrentPage() === "dashboard") {
            $state.go("index.dashboard." + $scope.getCurrentTab(), {dealerId: $stateParams.dealerId ? $stateParams.dealerId : defaultDealerId, productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "reports") {
            $state.go("index.report.reports", {dealerId: $stateParams.dealerId ? $stateParams.dealerId : defaultDealerId, productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "newOrEdit") {
            $state.go("index.report.newOrEdit", {dealerId: $stateParams.dealerId ? $stateParams.dealerId : defaultDealerId, productId: $stateParams.productId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "dataSource") {
            $state.go("index.dataSource", {dealerId: $stateParams.dealerId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "dataSet") {
            $state.go("index.dataSet", {dealerId: $stateParams.dealerId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else {
            $location.path("/" + "?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
        }
    };


    $scope.loadNewUrl = function () {
        console.log($stateParams.dealerId);
        try {
            $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

            $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
        } catch (e) {
        }
        console.log($stateParams.tabId);
        console.log($scope.getCurrentTab());
        console.log($scope.getCurrentPage());
        if ($scope.getCurrentPage() === "dashboard") {
            $state.go("index.dashboard." + $scope.getCurrentTab(), {dealerId: $stateParams.dealerId, productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "reports") {
            $state.go("index.report.reports", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "newOrEdit") {
            $state.go("index.report.newOrEdit", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "dataSource") {
            $state.go("index.dataSource", {dealerId: $stateParams.dealerId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "dataSet") {
            $state.go("index.dataSet", {dealerId: $stateParams.dealerId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else {
            $location.path("/" + "?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
        }
    };
    $scope.getCurrentPage = function () {
        var url = window.location.href;
        console.log(url)
        if (url.indexOf("widget") > 0) {
            return "dashboard";
        }
        if (url.indexOf("newOrEdit") > 0) {
            return "newOrEdit";
        }
        if (url.indexOf("report") > 0) {
            return "reports";
        }
        if (url.indexOf("dataSource") > 0) {
            return "dataSource";
        }
        if (url.indexOf("dataSet") > 0) {
            return "dataSet";
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
//                        'Today': [moment(), moment()],
//                        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
//                        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
//                        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                        'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                    },
                    startDate: moment().subtract(29, 'days'),
                    endDate: moment(),
                    maxDate: new Date()
                },
        function (start, end) {
            $('#daterange-btn span').html(start.format('MM-DD-YYYY') + ' - ' + end.format('MM-DD-YYYY'));
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
