app.controller('UiController', function ($scope, $http, $stateParams, $state, $filter, $cookies) {
    $scope.selectTabID = $state;

    $scope.userName = $cookies.getObject("username");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;

    $scope.endDate = new Date();
    $scope.startDate = new Date();
    try {
        $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

        $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.endDate;
    } catch (e) {
    }


    $scope.loadNewUrl = function () {
        try {
            $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

            $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.endDate;
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

$scope.loadTab = true;
    $http.get("admin/ui/dbTabs/" + $stateParams.productId).success(function (response) {
        $scope.loadTab = false;
        $scope.tabs = response;
        console.log(response)
        angular.forEach(response, function (value, key) {
            $scope.dashboardName = value.dashboardId.dashboardTitle;
        });
        $state.go("index.dashboard.widget", {tabId: $stateParams.tabId ? $stateParams.tabId : response[0].id, reload: true});
//        $stateParams.tabId = $stateParams.tabId ? $stateParams.tabId : response[0].id
    });

    var dates = $(".pull-right i").text();


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
    $scope.addTab = function (tab) {
        $scope.tabs.push({tabName: tab.tabName, tabClose: true});
        var data = {
            tabName: tab.tabName
        };
        $http({method: 'POST', url: 'admin/ui/dbTabs/' + $stateParams.productId, data: data}).success(function (response) {
            tab.tabClose = false;
        })
    };

    var removeTab = function (event, index) {
        event.preventDefault();
        event.stopPropagation();
        $scope.tabs.splice(index, 1);
    };

    //$scope.addTab = addTab;
    $scope.removeTab = removeTab;

    for (var i = 0; i < 5; i++) {
        // addTab();
    }


//    $http.get("admin/ui/dashboard").success(function () {
//
//    })
//    console.log($stateParams.tabId)
    if ($stateParams.productId == 'dashboard') {
        $stateParams.productId = 2;
    }
    //$scope.currentTabId = $stateParams.tabId

    console.log("productId : " + $stateParams.productId)
    $http.get("admin/ui/dbTabs/" + $stateParams.productId).success(function (response) {
        $scope.tabs = response;
        console.log(response)
        angular.forEach(response, function (value, key) {
            $scope.dashboardName = value.dashboardId.dashboardTitle;
        });
        $state.go("index.dashboard.widget", {tabId: $stateParams.tabId ? $stateParams.tabId : response[0].id, reload: true});
//        $stateParams.tabId = $stateParams.tabId ? $stateParams.tabId : response[0].id
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
                    startDate: moment().subtract(new Date(), 'days'),
                    endDate: moment()
                },
                function (start, end) {
                    $('#daterange-btn span').html(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
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