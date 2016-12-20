app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout) {
    $scope.widgets = [];
    $scope.selectAggregations = [{name: 'sum'}, {name: 'avg'}, {name: 'count'}, {name: 'min'}, {name: 'max'}];   //Aggregation Type-Popup
    $scope.selectDateDurations = [{duration: "Last Week"}, {duration: "Last Three Months"}, {duration: "Last Six Months"}, {duration: "Last Six Months"}]; // Month Durations-Popup
    $scope.isEditPreviewColumn = false;
    $scope.tableDef = function (widget) {      //Dynamic Url from columns Type data - Popup
        if (widget.directUrl) {
            $http.get(widget.directUrl + "?fieldsOnly=true").success(function (response) {
                $scope.collectionFields = [];
                $scope.collectionFields = response.columnDefs;
            });
        }
    };

    $scope.changeUrl = function (url) {
        $http.get(url + "?fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
            angular.forEach(response, function (value, key) {
                angular.forEach(value, function(value, key){                    
                $scope.collectionFields.push(value);
                });
            });
        });
    };

    $scope.deleteUnSelectColumn = function (index, widget, collectionField) {
        $scope.collectionFields.splice(index, 1);
        console.log($scope.collectionFields)
    }
    
    $scope.addSelectColumn = function(){
        $scope.unshift({isEditPreviewColumn: true, isDelete: true})
    }

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

    $scope.addWidget = function (newWidget) {       //Add Widget
        var data = {
            width: newWidget.panelWidth, 'minHeight': 25, columns: []
        };
        $http({method: 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            console.log(response.id)
            $scope.widgets.push({id: response.id, width: newWidget.panelWidth, 'minHeight': 25, columns: []});
            $scope.newWidgetId = response.id;
            // getWidgetItem();
        });
    };

    $scope.deleteWidget = function (widget, index) {        //Delete Widget
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
            $scope.widgets.splice(index, 1);
            $('.modal-backdrop').remove();
        });
    };

    $scope.addColumns = function (widget) {      //Add Columns - Popup
        widget.columns.unshift({isEdit: true, isDelete: true})
    };

    $scope.saveColumn = function (widget, column) {     //Delete Columns-Popup
        console.log($scope.newWidgetId)
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
        $http({method: column.id ? 'PUT' : 'POST', url: 'admin/ui/widgetColumn/' + widget.id, data: data}).success(function (response) {
            column.id = response.id;
        });
    };

    $scope.deleteColumn = function (index, widget, column) {        //Delete Columns - Popup
        $http({method: 'DELETE', url: 'admin/ui/widgetColumn/' + column.id}).success(function () {
        });
        widget.columns.splice(index, 1);
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

    $scope.dataSetName = [];
//    $scope.selectedDataSet = function (dataSet) {
//        angular.forEach(dataSet, function (value, key) {
//            // console.log(value.name)
//            $scope.dataSetName.push(value.name);
//            dataSet = '';
//        });
//    };
//    $scope.selectedDimensions = function (dimension, dataSet) {
//        angular.forEach(dimension, function (value, key) {
//            $scope.dimensionName = value.name;
//        });
//        $http.get('admin/datasources/dataDimensions/' + $scope.selectItem + '?' + 'dimensions=' + $scope.dimensionName + '&dataSet=' + $scope.dataSetName + '&sort=ga:sessions').success(function () {
//
//        });
//    };
    $scope.objectHeader = [];
    $scope.previewChart = function (chartType, widget, index) {     //Selected Chart type - Bind chart-type to showPreview()

        $scope.selectedRow = chartType.type;

        widget.widgetChartType = chartType.type;
        //$scope.widgetPreviewChart = widget.widgetChartType;

        $scope.setPreviewChartType = chartType.type;
        $scope.setPreviewColumn = widget;
        //$scope.previewChartType = chartType.type;
        //$scope.previewColumn = widget;

//        $scope.previewChartType = "";
//        $scope.columns = [];
        //$timeout(function () {
//        $scope.widgetPreviewChartUrl = widget.previewUrl;
        // }, 500);
    };

    $scope.showPreview = function (widget) {        //Show Preview Chart - Popup
        $scope.previewChartType = $scope.setPreviewChartType ? $scope.setPreviewChartType : widget.chartType;
        $scope.previewColumn = $scope.setPreviewColumn ? $scope.setPreviewColumn : widget;
    };

    $scope.editWidget = function (widget) {     //Edit widget
        $scope.tableDef(widget);
        $scope.selectedRow = widget.chartType;
        widget.previewUrl = widget.directUrl;
        widget.previewType = widget.chartType;
        widget.previewTitle = widget.widgetTitle;
    };

    $scope.save = function (widget) {
        console.log($scope.collectionFields)
        console.log($scope.newWidgetId)
        var data = {
            id: widget.id,
            chartType: $scope.previewChartType ? $scope.previewChartType : widget.chartType,
            directUrl: widget.previewUrl,
            widgetTitle: widget.previewTitle,
            widgetColumns : $scope.collectionFields
        };
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
        });
        widget.chartType = $scope.previewChartType ? $scope.previewChartType : widget.chartType;
        widget.directUrl = widget.previewUrl ? widget.previewUrl : widget.directUrl;
        widget.widgetTitle = widget.previewTitle ? widget.previewTitle : widget.widgetTitle;
    };

    $scope.onDropComplete = function (index, widget, evt) {
        var otherObj = $scope.widgets[index];
        var otherIndex = $scope.widgets.indexOf(widget);
        $scope.widgets[index] = widget;
        $scope.widgets[otherIndex] = otherObj;
    };
    //widget refresh
//    $scope.setLineFn = function (lineFn) {
//        $scope.directiveLineFn = lineFn;
//    };
//    $scope.setAreaFn = function (areaFn) {
//        $scope.directiveAreaFn = areaFn;
//    };
//    $scope.setBarFn = function (barFn) {
//        $scope.directiveBarFn = barFn;
//    };
//    $scope.setPieFn = function (pieFn) {
//        $scope.directivePieFn = pieFn;
//    };
//    $scope.setDonutFn = function (donutFn) {
//        $scope.directiveDonutFn = donutFn;
//    };
//    $scope.setGroupedBarFn = function (groupedBarFn) {
//        $scope.directiveGroupedBarFn = groupedBarFn;
//    };
//    $scope.setTableFn = function (tableFn) {
//        $scope.directiveTableFn = tableFn;
//    };

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
                '<div class="grid" ng-if="ajaxLoadingCompleted" ui-grid="gridOptions" ui-grid-grouping></div>',
//                '<div class="grid" ng-if="ajaxLoadingCompleted" ui-grid="gridOptions" style="height: 850px;" ui-grid-grouping></div>',
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
                enableSorting: false,
                multiSelect: false,
                enableColumnResize: true,
                showColumnFooter: true,
                cellTooltip: true,
                enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
                enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            };
            var startDate = "";
            var endDate = "";
            var columnDefs = [];
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {

                columnDef = {
                    field: value.fieldName,
                    displayName: value.displayName,
//                        cellClass: 'space-numbers',
                    cellTooltip: true,
                    headerTooltip: true
                }
                var cellFormat = "";
                if (value.displayFormat) {
                    cellFormat = value.displayFormat;
                }
                columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD | gridDisplayFormat : "' + cellFormat + '"}}</span></div>';
                columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | gridDisplayFormat:"' + cellFormat + '"}}</div>';

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
                            columnDef.headerTooltip = true,
                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD}}</span></div>'
                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'

                }
                if (value.agregationFunction == "max") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.MAX,
                            //columnDef.cellFilter = 'number: 2';
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true,
                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD}}</span></div>'
                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'

                }
                if (value.agregationFunction == "avg") {
                    //columnDef.cellFilter = 'number: 2';
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.AVG,
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true,
                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD | setDecimal:2 }}</span></div>'
                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'
                    // columnDef.cellFilter = 'formatCaller'

                }
                if (value.agregationFunction == "min") {

                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.MIN,
//                                columnDef.cellFilter = 'number: 2';
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true,
                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD}}</span></div>'
                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'
                }
                if (value.groupPriority) {
                    columnDef.grouping = {groupPriority: value.groupPriority};
                }
                //console.log(columnDef);
                columnDefs.push(columnDef);
            });
            //console.log(columnDefs);
            $http.get(scope.dynamicTableUrl + "?widgetId=" + scope.widgetId + "&startDate=" + startDate + "&endDate=" + endDate).success(function (response) {
                scope.ajaxLoadingCompleted = true;
                scope.loadingTable = false;
                scope.gridOptions = {
//                    rowHeight: 50,
                    enableColumnMenus: false,
                    enableGridMenu: false,
                    enableRowSelection: false,
                    enableGroupHeaderSelection: false,
                    enableRowHeaderSelection: false,
                    enableSorting: false,
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

                scope.getTableHeight = function () {
                    var rowHeight = 30; // your row height
                    var headerHeight = 30; // your header height
                    var minWidth = 150;
                    return {
                        height: (scope.gridOptions.data.length * rowHeight + headerHeight) + "px",
                        'min-width': minWidth + "px"
                    };
                };

            });
        }
    };
});

app.directive('lineChartDirective', function ($http) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingLine" class="text-center"><img src="static/img/logos/loader.gif"></div>',
        scope: {
            lineChartUrl: '@',
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
            var displayDataFormat = {};
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {

                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    if (!labels["format"]) {
                        labels = {format: {}};
                    }
                    labels["format"][displayName] = function (value) {
                        return d3.format(format)(value);
                    }
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                }
            });
            console.log(labels);
            var xData = [];
            var xTicks = [];

            function sortResults(unsortedData, prop, asc) {
                sortedData = unsortedData.sort(function (a, b) {
                    if (asc) {
                        return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                    } else {
                        return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                    }
                });
                return sortedData;
            }

            if (scope.lineChartUrl) {
                $http.get(scope.lineChartUrl).success(function (response) {
                    scope.loadingLine = false;
                    scope.xAxis = [];
                    var loopCount = 0;
                    var chartData = response.data;
                    chartData = sortResults(chartData, sortField, 1);
                    xTicks = [xAxis.fieldName];
                    xData = chartData.map(function (a) {
                        xTicks.push(loopCount);
                        loopCount++;
                        return a[xAxis.fieldName];
                    });
                    columns.push(xTicks);
                    angular.forEach(yAxis, function (value, key) {
                        ySeriesData = chartData.map(function (a) {
                            return a[value.fieldName];
                        });
                        ySeriesData.unshift(value.displayName);
                        columns.push(ySeriesData);
                    });

//                    labels = {
//                        format1: {
//                            'Cost': function (value) {
//                                return "X"
//                            }
//                        }
//                    };
                    displayFormat = {'Cost': function (value) {
                            return "X"
                        }};
                    var chart = c3.generate({
                        bindto: element[0],
                        data: {
                            x: xAxis.fieldName,
                            columns: columns,
                            labels: labels
                        },
                        tooltip: {show: false},
                        axis: {
                            x: {
                                tick: {
                                    format: function (x) {
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

app.directive('barChartDirective', function ($http) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingBar" class="text-center"><img src="static/img/logos/loader.gif"></div>',
        scope: {
            barChartUrl: '@',
            setBarChartFn: '&',
            barChartId: '@',
            widgetColumns: '@'
        },
        link: function (scope, element, attr) {
            scope.loadingBar = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;

            var sortField = "";
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (value.sortOrder) {
                    sortField = value.fieldName;
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
                        return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                    } else {
                        return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                    }
                });
                return sortedData;
            }

            if (scope.barChartUrl) {
                $http.get(scope.barChartUrl).success(function (response) {
                    scope.loadingBar = false;
                    scope.xAxis = [];
                    var loopCount = 0;
                    var chartData = response.data;
                    chartData = sortResults(chartData, sortField, 1);
                    xTicks = [xAxis.fieldName];
                    xData = chartData.map(function (a) {
                        xTicks.push(loopCount);
                        loopCount++;
                        return a[xAxis.fieldName];
                    });

                    columns.push(xTicks);


                    angular.forEach(yAxis, function (value, key) {
                        ySeriesData = chartData.map(function (a) {
                            return a[value.fieldName];
                        });
                        ySeriesData.unshift(value.displayName);
                        columns.push(ySeriesData);
                    });

                    var chart = c3.generate({
                        bindto: element[0],
                        data: {
                            x: xAxis.fieldName,
                            columns: columns,
                            labels: true,
                            type: 'bar'
                        },
                        axis: {
                            x: {
                                tick: {
                                    format: function (x) {
//                                    console.log("TESTING");
//                                    console.log(x);
//                                    console.log(xData[x]);
//                                    console.log(xData);
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
app.directive('pieChartDirective', function ($http) {
    return{
        restrict: 'AC',
        template: '<div ng-show="loadingPie" class="text-center"><img src="static/img/logos/loader.gif"></div>',
        scope: {
            pieChartUrl: '@',
            widgetColumns: '@',
            setPieChartFn: '&',
            pieChartId: '@',
            loadingPie: '&'
        },
        link: function (scope, element, attr) {
            scope.loadingPie = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";

            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (value.sortOrder) {
                    sortField = value.fieldName;
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
                        return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                    } else {
                        return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                    }
                });
                return sortedData;
            }

            if (scope.pieChartUrl) {
                $http.get(scope.pieChartUrl).success(function (response) {
                    scope.loadingPie = false;
                    scope.xAxis = [];
                    var loopCount = 0;
                    var chartData = response.data;
                    chartData = sortResults(chartData, sortField, 1);
                    xTicks = [xAxis.fieldName];
                    xData = chartData.map(function (a) {
                        xTicks.push(loopCount);
                        loopCount++;
                        return a[xAxis.fieldName];
                    });
                    columns.push(xTicks);
                    angular.forEach(yAxis, function (value, key) {
                        ySeriesData = chartData.map(function (a) {
                            return a[value.fieldName];
                        });
                        ySeriesData.unshift(value.displayName);
                        columns.push(ySeriesData);
                    });

                    var chart = c3.generate({
                        bindto: element[0],
                        data: {
                            x: xAxis.fieldName,
                            columns: columns,
                            labels: true,
                            type: 'pie'
                        },
                        axis: {
                            x: {
                                tick: {
                                    format: function (x) {
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

app.directive('areaChartDirective', function ($http) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingArea" class="text-center"><img src="static/img/logos/loader.gif"></div>',
        scope: {
            setPieChartFn: '&',
            areaChartUrl: '@',
            widgetColumns: '@',
            pieChartId: '@'
        },
        link: function (scope, element, attr) {
            scope.loadingArea = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";

            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (value.sortOrder) {
                    sortField = value.fieldName;
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
                        return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                    } else {
                        return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                    }
                });
                return sortedData;
            }

            if (scope.areaChartUrl) {
                $http.get(scope.areaChartUrl).success(function (response) {
                    scope.loadingArea = false;
                    scope.xAxis = [];
                    var loopCount = 0;
                    var chartData = response.data;
                    chartData = sortResults(chartData, sortField, 1);
                    xTicks = [xAxis.fieldName];
                    xData = chartData.map(function (a) {
                        xTicks.push(loopCount);
                        loopCount++;
                        return a[xAxis.fieldName];
                    });
                    columns.push(xTicks);
                    angular.forEach(yAxis, function (value, key) {
                        ySeriesData = chartData.map(function (a) {
                            return a[value.fieldName];
                        });
                        ySeriesData.unshift(value.displayName);
                        columns.push(ySeriesData);
                    });

                    var chart = c3.generate({
                        bindto: element[0],
                        data: {
                            x: xAxis.fieldName,
                            columns: columns,
                            labels: true,
                            type: 'area'
                        },
                        axis: {
                            x: {
                                tick: {
                                    format: function (x) {
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
})
        .filter('setDecimal', function () {
            return function (input, places) {
                if (isNaN(input))
                    return input;
                var factor = "1" + Array(+(places > 0 && places + 1)).join("0");
                return Math.round(input * factor) / factor;
            };
        })
                .filter('gridDisplayFormat', function(){
                    return function(input, formatString) {
                        if(formatString) {
                            return d3.format(formatString)(input);
                        }
                        return input;
                    }
                })

