app.controller('UiController', function ($scope, $http, $stateParams, $state, $filter, $cookies, $timeout, $location, localStorageService, $rootScope, $window) {
    $scope.permission = localStorageService.get("permission");
    $scope.userName = $cookies.getObject("username");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;
    $scope.dealers = [];
    $scope.products = [];
    $scope.tabs = [];

    $http.get('admin/dealer').success(function (response) {
        $scope.dealers = response;
        var changeDealerId = $stateParams.dealerId;
        $stateParams.dealerId = changeDealerId ? changeDealerId : response[0].id;
        getProduct();
    });

    function getProduct() {
        $http.get('admin/ui/product/' + $stateParams.dealerId).success(function (response) {
            $scope.products = response;
            var changeProductId = $stateParams.productId ? $stateParams.productId : response[0].id;
            $stateParams.productId = changeProductId;
            getTabs();
        });
    }


    function getTabs() {
        $http.get("admin/ui/dbTabs/" + $stateParams.productId).success(function (response) {
            $scope.tabs = response;
            console.log(response)
            angular.forEach(response, function (value, key) {
                $scope.dashboardName = value.dashboardId.dashboardTitle;
            });
            if (!response) {
                return;
            }

            $stateParams.tabId = $stateParams.tabId ? $stateParams.tabId : response[0].id;
//            $scope.loadNewUrl()
            $state.go("index.dashboard", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
            getWidgetItem()
        });
//        console.log($scope.tabs)
    }

    $scope.getDealerId = function (dealer) {
        $stateParams.productId = "";
        $stateParams.tabId = "";
        console.log(dealer);
        $stateParams.dealerId = dealer.id;
//        $rootScope.$emit('dealerChange', {dealerId: dealer.id});
    };

    $scope.setParamsDate = function (product) {
        $stateParams.productId = product.id;
        $stateParams.tabId = "";
        $scope.startDate = $stateParams.startDate;
        $scope.endDate = $stateParams.endDate;
        $scope.dealerId = $stateParams.dealerId;
    };

    $scope.setParams = function () {
        $scope.dealerId = $stateParams.dealerId;
        $scope.startDate = $stateParams.startDate;
        $scope.endDate = $stateParams.endDate;
    }

    $scope.setTabParams = function (tab) {
        $stateParams.tabId = tab.id;
        //$state.go("index.dashboard", {tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
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
    };

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
//        alert("Load Url")
        console.log($stateParams.dealerId);
        console.log($stateParams.productId);
        try {
            $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

            $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
        } catch (e) {
        }
        console.log($stateParams.tabId);
        console.log($scope.getCurrentTab());
        console.log($scope.getCurrentPage());
        if ($scope.getCurrentPage() === "dashboard") {
//            $state.go("index.dashboard", {tabId: $stateParams.tabId});
            $state.go("index.dashboard", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, tabId: $stateParams.tabId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "reports") {
            $state.go("index.report.reports", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "newOrEdit") {
            $state.go("index.report.newOrEdit", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "scheduler") {
            $state.go("index.schedulerIndex.scheduler", {dealerId: $stateParams.dealerId, productId: $stateParams.productId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "dataSource") {
            $state.go("index.dataSource", {dealerId: $stateParams.dealerId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "dataSet") {
            $state.go("index.dataSet", {dealerId: $stateParams.dealerId, startDate: $scope.startDate, endDate: $scope.endDate});
        } else if ($scope.getCurrentPage() === "mapReport") {
            $state.go("index.map", {startDate: $scope.startDate, endDate: $scope.endDate});
        }  else if ($scope.getCurrentPage() === "viewFavouritesWidget") {
            $state.go("index.viewFavouritesWidget", {startDate: $scope.startDate, endDate: $scope.endDate});
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
        if (url.indexOf("scheduler") > 0) {
            return "scheduler";
        }
        if (url.indexOf("dataSource") > 0) {
            return "dataSource";
        }
        if (url.indexOf("dataSet") > 0) {
            return "dataSet";
        }
        if (url.indexOf("mapReport") > 0) {
            return "mapReport";
        }
        if (url.indexOf("viewFavouritesWidget") > 0) {
            return "viewFavouritesWidget";
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

    $(document).ready(function (e) {
        $(document).on('click', '.applyBtn', function () {
            $scope.loadNewUrl();
        });

        $(document).on('click', '.table-condensed .month', function () {
            var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            var selectedMonth = $(this).text();
            var splitMY = selectedMonth.split(" ");
            var monthvalue = $.inArray(splitMY[0], months);
            var FirstDay = new Date(splitMY[1], monthvalue, 1);
            var LastDay = new Date(splitMY[1], monthvalue + 1, 0);

            $("input[name='daterangepicker_start']").daterangepicker({
                singleDatePicker: false,
                startDate: FirstDay
            });

            $("input[name='daterangepicker_end']").daterangepicker({
                singleDatePicker: false,
                startDate: LastDay
            });
        });

        $(".ranges ul").find("li").addClass("custom-picker");
        $(document).on("click", ".ranges ul li", function (e) {
            $scope.loadNewUrl();
        })
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
                    startDate: $stateParams.startDate ? $stateParams.startDate : moment().subtract(29, 'days'),
                    endDate: $stateParams.endDate ? $stateParams.endDate : moment(),
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

//    $scope.activeTab = $stateParams.tabId;
//    $scope.setTab = function (newTab) {
//        $scope.activeTab = newTab;
//    };
//
//    $scope.isSet = function (tabNum) {
//        return $scope.activeTab === tabNum;
//    };


    //<================================ Tabs ==================================>

    $scope.editDashboardTab = function (tab) {
        var data = {
            tabNames: tab.tabName
        };
        $scope.tab = data;
        $timeout(function () {
            $('#editTab' + tab.id).modal('show');
        }, 100);
    };

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
            $http.get("admin/ui/dbTabs/" + $stateParams.productId).success(function (response) {
                $scope.tabs = response;
                angular.forEach(response, function (value, key) {
                    $scope.dashboardName = value.dashboardId.dashboardTitle;
                });
                $state.go("index.dashboard", {dealerId: $stateParams.dealerId, tabId: response[0].id, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
            });
        });
    };

    $scope.moveItem = function (list, from, to) {
        list.splice(to, 0, list.splice(from, 1)[0]);
        return list;
    };

    $scope.onDropTabComplete = function (index, tab, evt) {
        if (tab !== "" && tab !== null) {
            var otherObj = $scope.tabs[index];
            var otherIndex = $scope.tabs.indexOf(tab);

            $scope.tabs = $scope.moveItem($scope.tabs, otherIndex, index);
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
    };

    $scope.doneEditing = function (tab) {
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
    };

    //<========================= Widgets ======================================>

    $scope.selectAggregations = [
        {name: 'None', value: ""},
        {name: 'Sum', value: "sum"},
        {name: 'CTR', value: "ctr"},
        {name: 'CPC', value: "cpc"},
        {name: 'CPS', value: "cps"},
        {name: 'CPA', value: "cpa"},
        {name: 'Avg', value: "avg"},
        {name: 'Count', value: "count"},
        {name: 'Min', value: "min"},
        {name: 'Max', value: "max"},
        {name: 'CPL', value: "cpl"},
        {name: 'CPLC', value: "cplc"},
        {name: 'CPComment', value: "cpcomment"},
        {name: 'CPostE', value: "cposte"},
        {name: 'CPageE', value: "cpagee"},
        {name: 'CPP', value: "cpp"},
        {name: 'CPR', value: "cpr"}

    ];   //Aggregation Type-Popup
    $scope.selectGroupPriorities = [
        {num: 'None', value: ""},
        {num: 1, value: 1},
        {num: 2, value: 2}
    ];
    $scope.selectDateDurations = [
        {duration: "None", value: 'none'},
        {duration: "Today", value: 'today'},
        {duration: "Last N days", value: ''},
        {duration: "Last N Weeks", value: ''},
        {duration: "Last N Months", value: ''},
        {duration: "This Month", value: 'thisMonth'},
        {duration: "This Year", value: 'thisYear'},
        {duration: "Last Year", value: 'lastYear'},
        {duration: "Yesterday", value: 'yesterday'},
        {duration: "Custom", value: 'custom'}
    ]; // Month Durations-Popup
    $scope.selectXAxis = [
        {label: 'None', value: ""},
        {label: "X-1", value: 1}
    ];
    $scope.selectYAxis = [
        {label: 'None', value: ""},
        {label: "Y-1", value: 1},
        {label: "Y-2", value: 2}
    ];
    $scope.alignments = [
        {name: '', displayName: 'None'},
        {name: "left", displayName: "Left"},
        {name: "right", displayName: "Right"},
        {name: "center", displayName: "Center"}
    ];
    $scope.sorting = [
        {name: 'None', value: ''},
        {name: 'asc', value: 'asc'},
        {name: 'desc', value: 'desc'}
    ];
    $scope.tableWrapText = [
        {name: 'None', value: ''},
        {name: 'Yes', value: "yes"}
    ];
    $scope.hideOptions = [
        {name: 'Yes', value: 1},
        {name: 'No', value: ''}
    ];
    $scope.isEditPreviewColumn = false;

    $scope.downloadPdf = function () {
        var url = "admin/proxy/download/" + $stateParams.tabId + "?dealerId=" + $stateParams.dealerId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate;
        $window.open(url);
    };

    $scope.widgets = [];
    function getWidgetItem() {      //Default Loading Items
//        if (!$stateParams.tabId) {
//            $stateParams.tabId = 1;
//        }
        $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
            var widgetItems = [];
            widgetItems = response;
            $http.get("admin/fav/getAllFav/").success(function (favResponse) {
                widgetItems.forEach(function (value, key) {
                    favWidget = $.grep(favResponse, function (b) {
                        return b.id === value.id;
                    });
                    if (favWidget.length > 0) {
                        value.isFav = true;
                    } else {
                        value.isFav = false;
                    }
                });
            })
            
            $scope.widgets = widgetItems;
            console.log("Widget Items : ==================>>>>")
            console.log(response)
        });
    }
    
    $scope.toggleFavourite = function (widget) {
        var isFav = widget.isFav;
        if (isFav) {
            $http({method: 'POST', url: "admin/fav/removeFav/" + widget.id});
            widget.isFav = false;
        } else {
            $http({method: 'POST', url: "admin/fav/setFav/" + widget.id});
            widget.isFav = true;
        }
    }

    $http.get('admin/report/getReport').success(function (response) {
        console.log(response)
        $scope.reportList = response;
    });

    $scope.showReportWidgetName = false;
    $scope.selectReport = function (reportWidget) {
        $scope.showReportWidgetName = false;
        $scope.reportWidgetTitle = []
        $scope.reportLogo = reportWidget.logo;
        $scope.reportDescription = reportWidget.description;
        $http.get("admin/report/reportWidget/" + reportWidget.id).success(function (response) {
            if (response.length > 0) {
                $scope.showReportWidgetName = true;
                $scope.reportWidgetTitle = response;
                $scope.showReportEmptyMessage = false;
            } else {
                $scope.showReportEmptyMessage = true;
                $scope.reportEmptyMessage = "No Data Found";
            }
        });
    };

    $scope.addWidgetToReport = function (widget) {
        console.log(widget)
        var data = {};
        data.widgetId = widget.id;
        data.reportId = widget.reportWidget.id;
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/report/reportWidget', data: data}).success(function (response) {
        });
        $scope.reportLogo = "";
        $scope.reportDescription = "";
        $scope.reportWidgetTitle = "";
        $scope.showReportWidgetName = false;
    };


//    getWidgetItem();
    $scope.collectionField = {};
    $scope.dispName = function (currentColumn) {
        $scope.filterName = $filter('filter')($scope.collectionFields, {fieldName: currentColumn.fieldName})[0];
        currentColumn.displayName = $scope.filterName.displayName;
    };

    $scope.editWidget = function (widget) {     //Edit widget
        $scope.tableDef(widget);
        $scope.selectedRow = widget.chartType;
        widget.previewUrl = widget.directUrl;
        widget.previewType = widget.chartType;
        widget.previewTitle = widget.widgetTitle;
        $scope.editChartType = widget.chartType;
        $scope.selectProductName(widget.productName, widget);
    };

    $scope.tableDef = function (widget) {      //Dynamic Url from columns Type data - Popup
        if (widget.columns) {
            widget.columns = widget.columns;
            if (widget.directUrl) {
                $http.get("admin/proxy/getJson?url=" + widget.directUrl + "&fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    $scope.collectionFields = response.columnDefs;
                });
            }
        } else {
            if (widget.directUrl) {
                $http.get("admin/proxy/getJson?url=" + widget.directUrl + "&fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    widget.columns = response.columnDefs;
                    $scope.collectionFields = response.columnDefs;
                });
            }
        }
    };

    $scope.selectProductName = function (productName, widget) {
        $scope.name = []
        console.log(widget)
//        if (productName === null) {
//            return;
//        }
        $http.get("admin/user/datasets").success(function (response) {                //User Based Products and Urls

            $scope.userProducts = [];
            $http.get('admin/ui/product/' + $stateParams.dealerId).success(function (response) {
                $scope.products = response;
                console.log(response)
            });
            console.log($scope.products)
            console.log("Product")
            $scope.name = $filter('filter')($scope.products, {id: $stateParams.productId})[0];
//            if(!$scope.name){
//                return;
//            }
            $scope.selectName = $scope.name.productName;
            angular.forEach(response, function (value, key) {
                if ($scope.selectName == key) {
                    $scope.userProducts.push(key);
                }
            });
            $scope.productFields = response[productName];
        });

    }
//    $scope.selectProductName();

    $scope.changeUrl = function (displayName, widget) {
        angular.forEach($scope.productFields, function (value, key) {
            if (value.productDisplayName == displayName) {
                $scope.requiredUrl = value.url;
            }
        })
        //var searchUrl = $filter('filter')($scope.productFields, {productDisplayName: displayName})[0];
        widget.previewUrl = $scope.requiredUrl;
        widget.columns = [];
        $http.get("admin/proxy/getJson?url=" + $scope.requiredUrl + "&fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
            angular.forEach(response.columnDefs, function (value, key) {
                widget.columns.push({fieldName: value.fieldName, displayName: value.displayName,
                    agregationFunction: value.agregationFunction, displayFormat: value.displayFormat, fieldType: value.type,
                    groupPriority: value.groupPriority, sortOrder: value.sortOrder, sortPriority: value.sortPriority});
            })
            $scope.previewFields = response.columnDefs;
            angular.forEach(response, function (value, key) {
                angular.forEach(value, function (value, key) {
                    $scope.collectionFields.push(value);
                });
            });
        });
    };

    $scope.pageRefresh = function () {          //Page Refresh
        getWidgetItem();
    };
    $http.get("static/datas/panelSize.json").success(function (response) {      //Default Panel in Ui
        $scope.newWidgets = response;
    });

    $scope.openPopup = function (response) {
        $timeout(function () {
            $scope.editWidget(response);
            $('#preview' + response.id).modal('show');
        }, 100);
    };
    $scope.addWidget = function (newWidget) {
        alert()      //Add Widget
        var data = {
            width: newWidget, 'minHeight': 25, columns: [], chartType: ""
        };
        $http({method: 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            $scope.widgets.unshift({id: response.id, width: newWidget, 'minHeight': 25, columns: [], tableFooter: 1, zeroSuppression: 1});
            $scope.newWidgetId = response.id;
            $scope.openPopup(response);
        });
    };
    $scope.deleteWidget = function (widget, index) {                            //Delete Widget
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
            $scope.widgets.splice(index, 1);
        });
    };

    $scope.deleteColumn = function (widgetColumns, index) {        //Delete Columns - Popup
        widgetColumns.splice(index, 1);
    };

    $http.get('static/datas/imageUrl.json').success(function (response) {       //Popup- Select Chart-Type Json
        $scope.chartTypes = response;
    });

    //DataSource
//    $http.get('admin/datasources').success(function (response) {
//        $scope.datasources = response;
//        console.log(1)
//        console.log(response)
//    });
//
//    //Data Source
//    $http.get('admin/datasources').success(function (response) {
//        console.log("2")
//        console.log(response)
//        $scope.datasources = response;
//    });
    $scope.selectedDataSource = function (selectedItem) {
        $scope.selectItem = selectedItem;
        selectedItems(selectedItem);
    };

    $scope.objectHeader = [];
    $scope.previewChart = function (chartType, widget, index) {                 //Selected Chart type - Bind chart-type to showPreview()
        $scope.selectedRow = chartType.type;
        widget.widgetChartType = chartType.type;
        $scope.editChartType = chartType.type;
        $scope.setPreviewChartType = chartType.type;
        $scope.setPreviewColumn = widget;
    };

    $scope.showPreview = function (widget) {                                    //Show Preview Chart - Popup
        $scope.previewChartType = $scope.editChartType ? $scope.editChartType : widget.chartType;
        $scope.previewColumn = $scope.setPreviewColumn ? $scope.setPreviewColumn : widget;
        $scope.previewChartUrl = widget.previewUrl;
    };

    $scope.dateDuration = function (widget, selectDateDuration) {
        widget.dateDuration = selectDateDuration.duration;
    };

    $scope.save = function (widget) {
        widget.directUrl = widget.previewUrl ? widget.previewUrl : widget.directUrl;
        var widgetColumnsData = [];
        angular.forEach(widget.columns, function (value, key) {
            var hideColumn = value.columnHide;
            if (value.groupPriority > 0) {
                hideColumn = 1;
            }

            var columnData = {
                id: value.id,
                fieldName: value.fieldName,
                displayName: value.displayName,
                agregationFunction: value.agregationFunction,
                groupPriority: isNaN(value.groupPriority) ? null : value.groupPriority,
                xAxis: isNaN(value.xAxis) ? null : value.xAxis,
                yAxis: isNaN(value.yAxis) ? null : value.yAxis,
                sortOrder: value.sortOrder,
                displayFormat: value.displayFormat,
                alignment: value.alignment,
                baseFieldName: value.baseFieldName,
                fieldGenerationFields: value.fieldGenerationFields,
                fieldGenerationFunction: value.fieldGenerationFunction,
                fieldType: value.fieldType,
                functionParameters: value.functionParameters,
                remarks: value.remarks,
                sortPriority: isNaN(value.sortPriority) ? null : value.sortPriority,
                width: isNaN(value.width) ? null : value.width,
                wrapText: value.wrapText,
                xAxisLabel: value.xAxisLabel,
                yAxisLabel: value.yAxisLabel,
                columnHide: hideColumn
            };
            widgetColumnsData.push(columnData);
        });
//        if (widget.dateDuration) {
//            widget.customRange = "";
//        }

        if (widget.dateDuration == 'None' || widget.dateDuration == "This Month" || widget.dateDuration == "This Year" || widget.dateDuration == "Last Year" || widget.dateDuration == "Yesterday" || widget.dateDuration == "Custom") {
            widget.frequencyDuration = "";
        }

        var data = {
            id: widget.id,
            chartType: $scope.editChartType ? $scope.editChartType : widget.chartType,
            directUrl: widget.previewUrl,
            widgetTitle: widget.previewTitle,
            widgetColumns: widgetColumnsData,
            productName: widget.productName,
            productDisplayName: widget.productDisplayName,
            tableFooter: widget.tableFooter,
            zeroSuppression: widget.zeroSuppression,
            maxRecord: widget.maxRecord,
            dateDuration: widget.dateDuration,
            frequencyDuration: widget.frequencyDuration,
            customRange: widget.customRange
        };
        widget.chartType = "";
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            widget.columns = response.columns; // widget.columns;
            widget.chartType = data.chartType;
        });
        widget.widgetTitle = widget.previewTitle ? widget.previewTitle : widget.widgetTitle;
//        widget.dateDuration = widget.dateDuration;
//        widget.frequencyDuration = widget.frequencyDuration;
//        widget.customRange = widget.customRange;
        widget.widgetColumns = widgetColumnsData; // widget.columns;
    };

    $scope.closeWidget = function (widget) {
        $scope.widget = "";
    };

//    Array.prototype.move = function (from, to) {
//        this.splice(to, 0, this.splice(from, 1)[0]);
//        return this;
//    };

    $scope.moveWidget = function (list, from, to) {
        list.splice(to, 0, list.splice(from, 1)[0]);
        return list;
    };

    $scope.onDropComplete = function (index, widget, evt) {

        if (widget !== "" && widget !== null) {
            var otherObj = $scope.widgets[index];
            var otherIndex = $scope.widgets.indexOf(widget);
//            $scope.widgets.move(otherIndex, index);
            $scope.widgets = $scope.moveWidget($scope.widgets, otherIndex, index);
//            $scope.widgets[index] = widget;
//            $scope.widgets[otherIndex] = otherObj;
            var widgetOrder = $scope.widgets.map(function (value, key) {
                if (value) {
                    return value.id;
                }
            }).join(',');
            var data = {widgetOrder: widgetOrder};
            if (widgetOrder) {
                $http({method: 'GET', url: 'admin/ui/dbWidgetUpdateOrder/' + $stateParams.tabId + "?widgetOrder=" + widgetOrder});
            }
        }
    };

});

app.directive('ngBlur', function () {
    return function (scope, elem, attrs) {
        elem.bind('blur', function () {
            scope.$apply(attrs.ngBlur);
        });
    };
})

app.directive('ngFocus', function ($timeout) {
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
