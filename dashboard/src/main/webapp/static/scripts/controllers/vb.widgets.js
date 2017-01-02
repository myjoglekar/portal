app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout) {
    console.log($stateParams.startDate)
    console.log($stateParams.endDate)
    
    $scope.widgets = [];
    $scope.selectAggregations = [{name: 'None', value: ""}, {name: 'Sum', value: "sum"}, {name: 'Avg', value: "avg"}, {name: 'Count', value: "count"}, {name: 'Min', value: "min"}, {name: 'Max', value: "max"}];   //Aggregation Type-Popup
    $scope.selectGroupPriorities = [{num: 'None', value: ""}, {num: 1, value: 1}, {num: 2, value: 2}]
    $scope.selectDateDurations = [{duration: "None"}, {duration: "Last Week"}, {duration: "Last Three Months"}, {duration: "Last Six Months"}, {duration: "Last Six Months"}]; // Month Durations-Popup
    $scope.selectXAxis = [{label: 'None', value: ""}, {label: "X-1", value: 1}];
    $scope.selectYAxis = [{label: 'None', value: ""}, {label: "Y-1", value: 1}, {label: "Y-2", value: 2}];
    $scope.alignments = [{name: '', displayName: 'None'}, {name: "left", displayName: "Left"}, {name: "right", displayName: "Right"}, {name: "center", displayName: "Center"}];
    $scope.sorting = [{name: 'None', value: ''}, {name: 'asc', value: 1}, {name: 'dec', value: 0}];
    $scope.isEditPreviewColumn = false;

    $scope.editWidget = function (widget) {     //Edit widget
        $scope.tableDef(widget);
        $scope.selectedRow = widget.chartType;
        widget.previewUrl = widget.directUrl;
        widget.previewType = widget.chartType;
        widget.previewTitle = widget.widgetTitle;
        $scope.editChartType = widget.chartType;
        $scope.selectProductName(widget.productName, widget);
        console.log($scope.editChartType)
    };

    $scope.tableDef = function (widget) {      //Dynamic Url from columns Type data - Popup
        if (widget.columns) {
            widget.columns = widget.columns;
            if (widget.directUrl) {
                $http.get(widget.directUrl + "?fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    $scope.collectionFields = response.columnDefs;
                });
            }
        } else {
            if (widget.directUrl) {
                $http.get(widget.directUrl + "?fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    widget.columns = response.columnDefs;
                    $scope.collectionFields = response.columnDefs;
                });
            }
        }
    };

    $scope.selectProductName = function (productName, widget) {
        if (productName === null) {
            return;
        }
        $http.get("admin/user/datasets").success(function (response) {                //User Based Products and Urls
            $scope.userProducts = [];
            angular.forEach(response, function (value, key) {
                $scope.userProducts.push(key);
            })
            $scope.productFields = response[productName];
        });

    }
    $scope.selectProductName();

    $scope.changeUrl = function (url, widget) {
        var data = JSON.parse(url);
        widget.columns = [];
        $http.get(data.url + "?fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
            angular.forEach(response.columnDefs, function (value, key) {
                widget.columns.push({fieldName: value.fieldName, displayName: value.displayName,
                    agregationFunction: value.agregationFunction, displayFormat: value.displayFormat,
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

    function getWidgetItem() {      //Default Loading Items
        if (!$stateParams.tabId) {
            $stateParams.tabId = 1;
        }
        $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
            $scope.widgets = response;
        });
    }
    getWidgetItem();

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
    $scope.addWidget = function (newWidget) {       //Add Widget
        var data = {
            width: newWidget, 'minHeight': 25, columns: [], chartType: ""
        };
        $http({method: 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            $scope.widgets.push({id: response.id, width: newWidget, 'minHeight': 25, columns: []});
            $scope.newWidgetId = response.id;
            $scope.openPopup(response);
        });
    };


    $scope.deleteWidget = function (widget, index) {                            //Delete Widget
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
            $scope.widgets.splice(index, 1);
            $('.modal-backdrop').remove();
        });
    };

    $scope.addColumns = function (widget) {                                     //Add Columns - Popup
        widget.columns.unshift({});
    };

    $scope.saveColumn = function (widget, column) {                              //Delete Columns-Popup
        var data = {
            id: column.id,
            agregationFunction: column.agregationFunction,
            displayName: column.displayName,
            fieldName: column.fieldName,
            groupPriority: column.groupPriority,
            xAxis: column.xAxis,
            yAxis: column.yAxis,
            sortOrder: column.sortOrder,
            displayFormat: column.displayFormat
        };
    };

    $scope.deleteColumn = function (widgetColumns, index) {        //Delete Columns - Popup
        widgetColumns.splice(index, 1);
    };

    $http.get('static/datas/imageUrl.json').success(function (response) {       //Popup- Select Chart-Type Json
        $scope.chartTypes = response;
    });

    //DataSource
    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });

    //Data Source
    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });
    $scope.selectedDataSource = function (selectedItem) {
        $scope.selectItem = selectedItem;
        selectedItems(selectedItem);
    };

    //Data Set
    function selectedItems(selectedItem) {
        $http.get('admin/datasources/dataSet/' + selectedItem).success(function (response) {
            $scope.dataSets = response;
        });
        $http.get('admin/datasources/dataDimensions/' + selectedItem).success(function (response) {
            $scope.dataDimensions = response;
        });
    }

    $scope.objectHeader = [];
    $scope.previewChart = function (chartType, widget, index) {                 //Selected Chart type - Bind chart-type to showPreview()
        $scope.selectedRow = chartType.type;
        widget.widgetChartType = chartType.type;
        $scope.editChartType = chartType.type;
        $scope.setPreviewChartType = chartType.type;
        $scope.setPreviewColumn = widget;
    };

    $scope.showPreview = function (widget) {                                    //Show Preview Chart - Popup
        var data = JSON.parse(widget.productDisplayName);
        $scope.previewChartType = $scope.editChartType ? $scope.$scope.editChartType : widget.chartType;
        $scope.previewColumn = $scope.setPreviewColumn ? $scope.setPreviewColumn : widget;
        $scope.previewChartUrl = data.url;
        console.log($scope.editChartType)
    };

    $scope.save = function (widget) {
        console.log($scope.editChartType)
        var data = JSON.parse(widget.productDisplayName);
        widget.directUrl = data.url ? data.url : widget.directUrl;

        var data = {
            id: widget.id,
            chartType: $scope.editChartType ? $scope.editChartType : widget.chartType,
            directUrl: data.url,
            widgetTitle: widget.previewTitle,
            widgetColumns: widget.columns,
            productName: widget.productName,
            productDisplayName: widget.productDisplayName
        };
        widget.chartType = "";
        console.log($scope.editChartType)
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            widget.chartType = data.chartType;
        });
        console.log(widget.chartType)
        widget.widgetTitle = widget.previewTitle ? widget.previewTitle : widget.widgetTitle;
        //widget.chartType = $scope.editChartType ? $scope.editChartType : widget.chartType;
        widget.widgetColumns = widget.columns;
        console.log($scope.editChartType)

    };


    $scope.onDropComplete = function (index, widget, evt) {
        var otherObj = $scope.widgets[index];
        var otherIndex = $scope.widgets.indexOf(widget);
        $scope.widgets[index] = widget;
        $scope.widgets[otherIndex] = otherObj;
    };
});

app.directive('dynamicTable', function ($http, uiGridConstants, uiGridGroupingConstants, $timeout, $stateParams) {
    return{
        restrict: 'A',
        scope: {
            dynamicTableUrl: '@',
            widgetId: '@',
            widgetColumns: '@',
            setTableFn: '&'
        },
        template: '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<div id="grid1" class="grid full-height" ng-if="ajaxLoadingCompleted" ui-grid="gridOptions" ui-grid-grouping></div>',
//                '<div class="grid" ng-if="ajaxLoadingCompleted" ui-grid="gridOptions" style="height: 850px;" ui-grid-grouping  ng-style="getTableHeight()"></div>',
        link: function (scope, element, attr) {
//scope.rowData = []ui-if="gridData.data.length>0"
            scope.loadingTable = true;
            scope.gridOptions = {
//                rowHeight: 50,
                enableColumnMenus: false,
                enableGridMenu: false,
                enableRowSelection: false,
                enableGroupHeaderSelection: false,
                enableRowHeaderSelection: false,
                enableSorting: true,
                multiSelect: false,
                enableColumnResize: true,
                showColumnFooter: true,
                cellTooltip: true,
                enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
                enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            };
            var startDate = "";
            var endDate = "";
//                        
//            console.log(moment($('#daterange-btn').data('daterangepicker').startDate).format('YYYY-MM-DD HH:mm:ss'));
//            console.log(moment($('#daterange-btn').data('daterangepicker').endDate).format('YYYY-MM-DD HH:mm:ss'));
            var columnDefs = [];
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {

                columnDef = {
                    field: value.fieldName,
                    displayName: value.displayName,
//                        cellClass: 'space-numbers',
                    //width: "100%",
                    cellTooltip: true,
                    headerTooltip: true
                }
                var cellFormat = "";
                if (value.displayFormat) {
                    cellFormat = value.displayFormat;
                }
                var cellAlignment = "";
                if (value.alignment === 'left') {
                    cellAlignment = 'text-left';
                } else if (value.alignment === 'right') {
                    cellAlignment = 'text-right';
                } else {
                    cellAlignment = 'text-center';
                }
                columnDef.cellTemplate = '<div  class="ui-grid-cell-contents ' + cellAlignment + '"><span>{{COL_FIELD | gridDisplayFormat : "' + cellFormat + '"}}</span></div>';
                columnDef.footerCellTemplate = '<div class="' + cellAlignment + '" >{{col.getAggregationValue() | gridDisplayFormat:"' + cellFormat + '"}}</div>';

                if (value.agregationFunction == "sum") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.SUM,
                            columnDef.cellFilter = 'gridDisplayFormat:"dsaf"',
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
                }
                if (value.agregationFunction == "count") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.COUNT,
                            //columnDef.cellFilter = 'number: 2';
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
//                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD}}</span></div>'
//                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'

                }
                if (value.agregationFunction == "max") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.MAX,
                            //columnDef.cellFilter = 'number: 2';
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
//                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD}}</span></div>'
//                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'

                }
                if (value.agregationFunction == "avg") {
                    //columnDef.cellFilter = 'number: 2';
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.AVG,
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
//                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD | setDecimal:2 }}</span></div>'
//                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'
                    // columnDef.cellFilter = 'formatCaller'

                }
                if (value.agregationFunction == "min") {

                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.MIN,
//                                columnDef.cellFilter = 'number: 2';
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
//                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD}}</span></div>'
//                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'
                }
                if (value.groupPriority) {
                    columnDef.grouping = {groupPriority: value.groupPriority};
                }
                columnDefs.push(columnDef);
            });
            $http.get(scope.dynamicTableUrl + "?widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate).success(function (response) {
                scope.ajaxLoadingCompleted = true;
                scope.loadingTable = false;
                scope.gridOptions = {
//                    rowHeight: 50,
                    enableColumnMenus: false,
                    enableGridMenu: false,
                    enableRowSelection: false,
                    enableGroupHeaderSelection: false,
                    enableRowHeaderSelection: false,
                    enableSorting: true,
                    enableGroup: false,
                    multiSelect: false,
                    enableColumnResize: true,
                    data: response.data,
//                    data: response.data.slice(0, 14),
                    columnDefs: columnDefs,
                    showColumnFooter: true,
                    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
                    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER

                };
                function lineage() {
                    return this.name + ' of ' + this.parent;
                }

                function setHeight(extra) {
                    scope.height = ((scope.gridOptions.data.length * 30) + 30);
                    if (extra) {
                        scope.height += extra;
                    }
                    scope.api.grid.gridHeight = scope.height;
                }

//                scope.getTableHeight = function () {
//                    var rowHeight = 30; // your row height
//                    var headerHeight = 30; // your header height
//                    var minWidth = 150;
//                    return {
//                        height: (scope.gridOptions.data.length * rowHeight + headerHeight) + "px",
//                        //'min-width': minWidth + "px"
//                    };
//                };

            });
        }
    };
});

app.directive('lineChartDirective', function ($http, $stateParams) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingLine" class="text-center"><img src="static/img/logos/loader.gif"></div>',
        scope: {
            lineChartUrl: '@',
            widgetId: '@',
            setLineChartFn: '&',
            control: "=",
            collection: '@',
            widgetColumns: '@',
            lineChartId: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingLine = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var sortDataType = "number";
            var displayDataFormat = {};
            var y2 = {show: false, label: ''};
            var axes = {};
            var startDate = "";
            var endDate = "";
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        if (format.indexOf("%") > -1) {
                            return d3.format(format)(value / 100);
                        }
                        return d3.format(format)(value);
                    };
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                    axes[value.displayName] = 'y' + (value.yAxis > 1 ? 2 : '');
                }
                if (value.yAxis > 1) {
                    y2 = {show: true, label: ''};
                }
            });
            var xData = [];
            var xTicks = [];

            function sortResults(unsortedData, prop, asc) {
                sortedData = unsortedData.sort(function (a, b) {
                    if (asc) {
                        if (isNaN(a[prop])) {
                            return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(a[prop]) > parseInt(b[prop])) ? 1 : ((parseInt(a[prop]) < parseInt(b[prop])) ? -1 : 0);
                        }
                    } else {
                        if (isNaN(a[prop])) {
                            return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(b[prop]) > parseInt(a[prop])) ? 1 : ((parseInt(b[prop]) < parseInt(a[prop])) ? -1 : 0);
                        }
                    }
                });
                return sortedData;
            }

            if (scope.lineChartUrl) {

                $http.get(scope.lineChartUrl + "?widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate).success(function (response) {
                    scope.loadingLine = false;
                    scope.xAxis = [];
                    var loopCount = 0;
                    var chartData = response.data;
                    if (sortField != "") {
                        chartData = sortResults(chartData, sortField, sortOrder);
                    }
                    xTicks = [xAxis.fieldName];
                    xData = chartData.map(function (a) {
                        xTicks.push(loopCount);
                        loopCount++;
                        return a[xAxis.fieldName];
                    });

                    columns.push(xTicks);


                    angular.forEach(yAxis, function (value, key) {
                        ySeriesData = chartData.map(function (a) {
                            return a[value.fieldName] || "0";
                        });
                        ySeriesData.unshift(value.displayName);
                        columns.push(ySeriesData);
                    });
                    console.log(columns)

                    var chart = c3.generate({
                        bindto: element[0],
                        data: {
                            x: xAxis.fieldName,
                            columns: columns,
                            labels: labels,
                            axes: axes
                        },
                        tooltip: {show: false},
                        axis: {
                            x: {
                                tick: {
                                    format: function (x) {
                                        return xData[x];
                                    }
                                }
                            },
                            y2: {
                                show: true,
                                tick: {
                                    format: d3.format(".2f")
                                }
                            }
//                            y: {
//                                label: 'Impressions'
//                            },
//                            y2: {
//                                show: true,
//                                label: 'Clicks'
//                            }
                        },
                        grid: {
                            x: {
                                show: true
                            },
                            y: {
                                show: true
                            }
                        }
                    });
                });
            }
        }
    };
});

app.directive('barChartDirective', function ($http, $stateParams) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingBar" class="text-center"><img src="static/img/logos/loader.gif"></div>',
        scope: {
            barChartUrl: '@',
            widgetId: '@',
            setBarChartFn: '&',
            barChartId: '@',
            widgetColumns: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingBar = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var y2 = {show: false, label: ''};
            var axes = {};
            var startDate = "";
            var endDate = "";
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        if (format.indexOf("%") > -1) {
                            return d3.format(format)(value / 100);
                        }
                        return d3.format(format)(value);
                    };
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                    axes[value.displayName] = 'y' + (value.yAxis > 1 ? 2 : '');
                }
                if (value.yAxis > 1) {
                    y2 = {show: true, label: ''};
                }
            });
            var xData = [];
            var xTicks = [];

            function sortResults(unsortedData, prop, asc) {
                sortedData = unsortedData.sort(function (a, b) {
                    if (asc) {
                        if (isNaN(a[prop])) {
                            return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(a[prop]) > parseInt(b[prop])) ? 1 : ((parseInt(a[prop]) < parseInt(b[prop])) ? -1 : 0);
                        }
                    } else {
                        if (isNaN(a[prop])) {
                            return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(b[prop]) > parseInt(a[prop])) ? 1 : ((parseInt(b[prop]) < parseInt(a[prop])) ? -1 : 0);
                        }
                    }
                });
                return sortedData;
            }

            if (scope.barChartUrl) {
                $http.get(scope.barChartUrl + "?widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate).success(function (response) {
                    scope.loadingBar = false;
                    scope.xAxis = [];
                    var loopCount = 0;
                    var chartData = response.data;
                    chartData = sortResults(chartData, sortField, sortOrder);
                    xTicks = [xAxis.fieldName];
                    xData = chartData.map(function (a) {
                        xTicks.push(loopCount);
                        loopCount++;
                        return a[xAxis.fieldName];
                    });

                    columns.push(xTicks);


                    angular.forEach(yAxis, function (value, key) {
                        ySeriesData = chartData.map(function (a) {
                            return a[value.fieldName] || "0";
                        });
                        ySeriesData.unshift(value.displayName);
                        columns.push(ySeriesData);
                    });
                    console.log(columns)
                    console.log(labels)
                    var chart = c3.generate({
                        bindto: element[0],
                        data: {
                            x: xAxis.fieldName,
                            columns: columns,
                            labels: labels,
                            type: 'bar',
                            axes: axes
                        },
                        tooltip: {show: false},
                        axis: {
                            x: {
                                tick: {
                                    format: function (x) {
                                        return xData[x];
                                    }
                                }
                            },
                            y2: y2
                        },
                        grid: {
                            x: {
                                show: true
                            },
                            y: {
                                show: true
                            }
                        }
                    });
                });
            }
        }
    };
});
app.directive('pieChartDirective', function ($http, $stateParams) {
    return{
        restrict: 'AC',
        template: '<div ng-show="loadingPie" class="text-center"><img src="static/img/logos/loader.gif"></div>',
        scope: {
            pieChartUrl: '@',
            widgetId: '@',
            widgetColumns: '@',
            setPieChartFn: '&',
            pieChartId: '@',
            loadingPie: '&'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingPie = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var axes = {};
            var startDate = "";
            var endDate = "";
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        if (format.indexOf("%") > -1) {
                            return d3.format(format)(value / 100);
                        }
                        return d3.format(format)(value);
                    };
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                    axes[value.displayName] = 'y' + (value.yAxis > 1 ? 2 : '');
                }
                if (value.yAxis > 1) {
                    y2 = {show: true, label: ''};
                }
            });
            console.log(labels);
            var xData = [];
            var xTicks = [];

            function sortResults(unsortedData, prop, asc) {
                sortedData = unsortedData.sort(function (a, b) {
                    if (asc) {
                        if (isNaN(a[prop])) {
                            return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(a[prop]) > parseInt(b[prop])) ? 1 : ((parseInt(a[prop]) < parseInt(b[prop])) ? -1 : 0);
                        }
                    } else {
                        if (isNaN(a[prop])) {
                            return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(b[prop]) > parseInt(a[prop])) ? 1 : ((parseInt(b[prop]) < parseInt(a[prop])) ? -1 : 0);
                        }
                    }
                });
                return sortedData;
            }

            if (scope.pieChartUrl) {
                $http.get(scope.pieChartUrl + "?widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate).success(function (response) {
                    scope.loadingPie = false;
                    scope.xAxis = [];
                    var loopCount = 0;
                    var chartData = response.data;
                    chartData = sortResults(chartData, sortField, sortOrder);
                    xTicks = [xAxis.fieldName];
                    xData = chartData.map(function (a) {
                        xTicks.push(loopCount);
                        loopCount++;
                        return a[xAxis.fieldName];
                    });
                    console.log(xData);
                    columns.push(xTicks);
                    angular.forEach(yAxis, function (value, key) {
                        ySeriesData = chartData.map(function (a) {
                            return a[value.fieldName] || "0";
                        });
                        ySeriesData.unshift(value.displayName);
                        columns.push(ySeriesData);
                    });
                    console.log(columns)
                    console.log(ySeriesData)
                    console.log(columns)
                    console.log(yAxis)

                    var data = {};
                    var legends = [];
                    var yAxisField = yAxis[0];
                    chartData.forEach(function (e) {
                        legends.push(e[xAxis.fieldName]);
                        data[e[xAxis.fieldName]] = data[e[xAxis.fieldName]] ? data[e[xAxis.fieldName]] : 0 + e[yAxisField.fieldName] ? e[yAxisField.fieldName] : 0;
                    })

                    console.log("...........................");
                    console.log(data);
                    console.log(legends);

                    var chart = c3.generate({
                        bindto: element[0],
//                        data: {
//                            x: xAxis.fieldName,
//                            columns: data,
//                            labels: labels,
//                            type: 'pie'
//                        },
                        data: {
                            json: [data],
                            keys: {
                                value: xData,
                            },
                            type: 'pie'
                        },
                        tooltip: {show: false},
                        axis: {
                            x: {
                                tick: {
                                    format: function (x) {
                                        console.log(xData[x])
                                        return xData[x];
                                    }
                                }
                            }
                        },
                        grid: {
                            x: {
                                show: true
                            },
                            y: {
                                show: true
                            }
                        }
                    });
                });
            }
        }
    };
});

app.directive('areaChartDirective', function ($http, $stateParams) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingArea" class="text-center"><img src="static/img/logos/loader.gif"></div>',
        scope: {
            setPieChartFn: '&',
            widgetId: '@',
            areaChartUrl: '@',
            widgetColumns: '@',
            pieChartId: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingArea = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var y2 = {show: false, label: ''};
            var axes = {};
            var startDate = "";
            var endDate = "";
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        if (format.indexOf("%") > -1) {
                            return d3.format(format)(value / 100);
                        }
                        return d3.format(format)(value);
                    };
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                }
            });
            var xData = [];
            var xTicks = [];

            function sortResults(unsortedData, prop, asc) {
                sortedData = unsortedData.sort(function (a, b) {
                    if (asc) {
                        if (isNaN(a[prop])) {
                            return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(a[prop]) > parseInt(b[prop])) ? 1 : ((parseInt(a[prop]) < parseInt(b[prop])) ? -1 : 0);
                        }
                    } else {
                        if (isNaN(a[prop])) {
                            return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(b[prop]) > parseInt(a[prop])) ? 1 : ((parseInt(b[prop]) < parseInt(a[prop])) ? -1 : 0);
                        }
                    }
                });
                return sortedData;
            }

            if (scope.areaChartUrl) {
                $http.get(scope.areaChartUrl + "?widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate).success(function (response) {
                    scope.loadingArea = false;
                    scope.xAxis = [];
                    var loopCount = 0;
                    var chartData = response.data;
                    chartData = sortResults(chartData, sortField, sortOrder);
                    xTicks = [xAxis.fieldName];
                    xData = chartData.map(function (a) {
                        xTicks.push(loopCount);
                        loopCount++;
                        return a[xAxis.fieldName];
                    });
                    columns.push(xTicks);
                    angular.forEach(yAxis, function (value, key) {
                        ySeriesData = chartData.map(function (a) {
                            return a[value.fieldName] || "0";
                        });
                        ySeriesData.unshift(value.displayName);
                        columns.push(ySeriesData);
                    });
                    console.log('Line : ', columns)
                    var chart = c3.generate({
                        bindto: element[0],
                        data: {
                            x: xAxis.fieldName,
                            columns: columns,
                            labels: labels,
                            type: 'area',
                            axes: axes
                        },
                        tooltip: {show: false},
                        axis: {
                            x: {
                                tick: {
                                    format: function (x) {
                                        return xData[x];
                                    }
                                }
                            },
                            y2: {show: true}
                        },
                        grid: {
                            x: {
                                show: true
                            },
                            y: {
                                show: true
                            }
                        }
                    });
                });
            }
        }
    };
})
        .filter('setDecimal', function () {
            return function (input, places) {
                if (isNaN(input))
                    return input;
                var factor = "1" + Array(+(places > 0 && places + 1)).join("0");
                return Math.round(input * factor) / factor;
            };
        })
        .filter('gridDisplayFormat', function () {
            return function (input, formatString) {
                if (formatString) {
                    if (formatString.indexOf("%") > -1) {
                        return d3.format(formatString)(input / 100);
                    }
                    return d3.format(formatString)(input);
                }
                return input;
            }
        })
        .filter('capitalize', function () {
            return function (input) {
                return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
            }
        })
        .filter('xAxis', [function () {
                return function (chartXAxis) {
                    var xAxis = ['', 'x-1']
                    return xAxis[chartXAxis];
                }
            }])
        .filter('yAxis', [function () {
                return function (chartYAxis) {
                    var xAxis = ['', 'y-1', 'y-2']
                    return xAxis[chartYAxis];
                }
            }]);

