app.controller('UiController', function ($scope, $http, $stateParams, $state, $filter, $cookies) {
    
    $scope.selectTabID = $state;
    $scope.userName = $cookies.getObject("username");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;

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

    $scope.loadNewUrl = function () {
        try {
            $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

            $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
        } catch (e) {
        }
        console.log($stateParams);
        console.log($scope.getCurrentTab());
        console.log($scope.getCurrentPage());
        if ($scope.getCurrentPage() === "dashboard") {
            $state.go("index.dashboard." + $scope.getCurrentTab(), {productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "report") {
            //alert("GO");
            $state.go("index.report.reports", {productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
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
    //$scope.loadNewUrl();
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

    $http.get('admin/dealer').success(function (response) {
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

    var counter = 1;
    $scope.tabs = [];
    $scope.addTab = function (tab) {
        //$scope.tabs.push({tabName: tab.tabName, tabClose: true});
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
//        $("#deleteTabModal" + tab.id).on('hidden.bs.modal', function () {
//            console.log("Tab : " + $scope.tabs)
//            $(this).data('bs.modal', null);
//        });
        // $("#deleteTabModal" + tab.id).remove();
        //$('.modal-backdrop').remove();
        //$('body').removeClass("modal-open");
        $scope.tabs.splice(index, 1);
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
    //console.log($stateParams.reportId);
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
        //$http({method: 'GET', url: 'admin/ui/dbWidgetUpdateOrder/' + $stateParams.tabId + "?widgetOrder=" + widgetOrder});
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
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                    },
                    startDate: moment().subtract(29, 'days'),
                    endDate: moment()
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


})