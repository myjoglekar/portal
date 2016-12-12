app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout) {
    $scope.widgets = [];
    $scope.selectAgregations = [{name: 'sum'}, {name: 'avg'}, {name: 'count'}, {name: 'min'}, {name: 'max'}]
    $http.get('../api/admin/paid/accountDevice?fieldsOnly=true').success(function (response) {
        $scope.collectionFields = response.columnDefs;
        //console.log(response)
    });

    function getWidgetItem() {
        if (!$stateParams.widgetId) {
            $stateParams.widgetId = 1;
        }
        $http.get("admin/ui/dbWidget/" + $stateParams.widgetId).success(function (response) {
            $scope.widgets = response;
        });
    }
    getWidgetItem();

    $scope.addColumns = function (widget) {
        widget.columns.unshift({isEdit: true, isDelete: true})
    };

    $scope.saveColumn = function (widget, column) {
        var data = {
            id: column.id,
            agregationFunction: column.agregationFunction,
            displayName: column.displayName,
            fieldName: column.fieldName,
            groupPriority: column.groupPriority
        }
        $http({method: column.id ? 'PUT' : 'POST', url: 'admin/ui/widgetColumn/' + widget.id, data: data}).success(function (response) {
            column.id = response.id;
        });
    };

    //var uid = 100;
    $scope.addWidget = function (newWidget) {
        //$scope.id = uid++;
        $scope.widgets.push({width: newWidget.panelWidth, 'minHeight': 25, columns: []});

        var data = {
            width: newWidget.panelWidth, 'minHeight': 25, columns: []
        };

        $http({method: 'POST', url: 'admin/ui/dbWidget/' + $stateParams.widgetId, data: data}).success(function (response) {
            getWidgetItem();
        });
    };

    $scope.deleteWidget = function (widget) {
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
             //getWidgetItem();
        });
    };

    $scope.deleteColumn = function (index, widget, column) {
        $http({method: 'DELETE', url: 'admin/ui/widgetColumn/' + column.id}).success(function () {
            widget.columns.splice(index, 1);
        });
    };
    $http.get('static/datas/imageUrl.json').success(function (response) {
        $scope.chartTypes = response;
    });
    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });
    $scope.pageRefresh = function () {
        getWidgetItem();
    };
    $http.get("static/datas/panelSize.json").success(function (response) {
        $scope.newPanels = response;
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
    $scope.previewChart = function (chartType, widget) {
        widget.widgetChartType = chartType.type;
        $scope.widgetPreviewChart = widget.widgetChartType;
        $scope.previewChartType = "";
        $scope.columns = [];
        //$timeout(function () {
        $scope.previewChartType = chartType.type;
        $scope.previewColumn = widget;
        $scope.widgetPreviewChartUrl = widget.previewUrl;
        console.log($scope.previewColumn)
        console.log(widget.previewUrl)
        // }, 500);
    };
    $scope.save = function (widget) {

        var data = {
            id: widget.id,
            chartType: $scope.widgetPreviewChart,
            directUrl: $scope.widgetPreviewChartUrl
        }
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.widgetId, data: data}).success(function (response) {
            //getWidgetItem();
        })
        widget.directUrl = $scope.widgetPreviewChartUrl;
        widget.chartType = $scope.previewChartType;
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
        template: '<div ng-show="loadingPie" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<div class="grid" ng-if="ajaxLoadingCompleted" ui-grid="gridOptions" ui-grid-grouping></div>',
        link: function (scope, element, attr) {

            scope.loadingPie = true;
            scope.gridOptions = {
                enableColumnMenus: false,
                enableGridMenu: false,
                enableRowSelection: false,
                enableGroupHeaderSelection: false,
                enableRowHeaderSelection: false,
                enableSorting: false,
                multiSelect: false,
                enableColumnResize: false,
                showColumnFooter: true
            }
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

                if (value.agregationFunction == "sum") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.SUM,
                            columnDef.cellFilter = 'number: 2',
//                                columnDef.cellClass = 'space-numbers',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true,
                            columnDef.cellTemplate = '<div class="integerAlign"><span>{{COL_FIELD | number:2}}</span></div>'
                    columnDef.footerCellTemplate = '<div class="integerAlign" >{{col.getAggregationValue() | number:2 }}</div>'
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
                scope.loadingPie = false;
                scope.gridOptions = {
                    enableColumnMenus: false,
                    enableGridMenu: false,
                    enableRowSelection: false,
                    enableGroupHeaderSelection: false,
                    enableRowHeaderSelection: false,
                    enableSorting: false,
                    multiSelect: false,
                    enableColumnResize: false,
                    data: response.data,
//                    data: response.data.slice(0, 14),
                    columnDefs: columnDefs,
                    showColumnFooter: true
                };
                function lineage() {
                    return this.name + ' of ' + this.parent;
                }
            });
        }
    };
});

app.directive('lineChartDirective', function ($http) {
    return{
        restrict: 'A',
        replace: true,
        scope: {
            lineChartUrl: '@',
            setLineChartFn: '&',
            control: "=",
            collection: '@',
            widgetColumns: '@',
            lineChartId: '@',
        },
        link: function (scope, element, attr) {

            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                }
            });

            var xData = [];
            var xTicks = [];

            $http.get(scope.lineChartUrl).success(function (response) {
                scope.xAxis = [];
                var loopCount = 1;
                xTicks = [xAxis.fieldName];
                xData = response.data.map(function (a) {
                    xTicks.push(loopCount);
                    loopCount++;
                    return a[xAxis.fieldName];
                });
                columns.push(xTicks);
                angular.forEach(yAxis, function (value, key) {
                    ySeriesData = response.data.map(function (a) {
                        return a[value.fieldName];
                    });
                    ySeriesData.unshift(value.displayName);
                    columns.push(ySeriesData);
                })
                /*
                 angular.forEach(response.data, function (value, key) {
                 xTicks.push(loopCount);
                 xData.push(value[xAxis.fieldName]);
                 //console.log(value)
                 //console.log(key)
                 scope.xAxis.push(value.impressions)
                 console.log(scope.xAxis)
                 })
                 */
                console.log(columns);
                var chart = c3.generate({
                    bindto: element[0],
                    data: {
                        x: xAxis.fieldName,
                        columns: columns
                    },
                    grid: {
                        x: {
                            show: true
                        },
                        y: {
                            show: true,
                        }
                    },
                    axis: {
                        x: {
                            tick: {
                                format: function (x) {
                                    return xData[x];
                                }
                            }
                        }
                    }
                });
            });
        }
    };
});

app.directive('barChartDirective', function ($http) {
    return{
        restrict: 'A',
        scope: {
            barChartUrl: '@',
            setBarChartFn: '&',
            barChartId: '@',
            widgetColumns: '@'
        },
        link: function (scope, element, attr) {

            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;

            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                }
            });

            var xData = [];
            var xTicks = [];

            $http.get(scope.barChartUrl).success(function (response) {
                scope.xAxis = [];
                var loopCount = 1;
                xTicks = [xAxis.fieldName];
                xData = response.data.map(function (a) {
                    xTicks.push(loopCount);
                    loopCount++;
                    return a[xAxis.fieldName];
                });
                columns.push(xTicks);
                angular.forEach(yAxis, function (value, key) {
                    ySeriesData = response.data.map(function (a) {
                        return a[value.fieldName];
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
                        type: 'bar'
                    },
                    grid: {
                        x: {
                            show: true
                        },
                        y: {
                            show: true,
                        }
                    },
                    axis: {
                        x: {
                            tick: {
                                format: function (x) {
                                    return xData[x];
                                }
                            }
                        }
                    }
                });
            });
        }
    };
});
app.directive('pieChartDirective', function ($http) {
    return{
        restrict: 'AC',
        template: '<div ng-show="loadingPie" class="text-center" style="color: #228995;">Data Fetching ...</div>',
//        template: '<div class="text-center"><img ng-show="loadingPie" src="static/img/loading.gif"></div>',
        scope: {
            pieChartUrl: '@',
            widgetColumns: '@',
            setPieChartFn: '&',
            pieChartId: '@',
            loadingPie: '&'
        },
        link: function (scope, element, attr) {
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                }
            });

            var xData = [];
            var xTicks = [];

            $http.get(scope.pieChartUrl).success(function (response) {
                scope.xAxis = [];
                var loopCount = 1;
                xTicks = [xAxis.fieldName];
                xData = response.data.map(function (a) {
                    xTicks.push(loopCount);
                    loopCount++;
                    return a[xAxis.fieldName];
                });
                columns.push(xTicks);
                angular.forEach(yAxis, function (value, key) {
                    ySeriesData = response.data.map(function (a) {
                        return a[value.fieldName];
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
                        type: 'pie'
                    },
                    grid: {
                        x: {
                            show: true
                        },
                        y: {
                            show: true,
                        }
                    },
                    axis: {
                        x: {
                            tick: {
                                format: function (x) {
                                    return xData[x];
                                }
                            }
                        }
                    }
                });
            });
        }
    };
});
app.directive('areaChartDirective', function ($http) {
    return{
        restrict: 'A',
        scope: {
            setPieChartFn: '&',
            areaChartUrl: '@',
            widgetColumns: '@',
            pieChartId: '@'
        },
        link: function (scope, element, attr) {

            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            console.log(scope.widgetColumns)
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                }
            });

            var xData = [];
            var xTicks = [];

            $http.get(scope.areaChartUrl).success(function (response) {
                scope.xAxis = [];
                var loopCount = 1;
                xTicks = [xAxis.fieldName];
                xData = response.data.map(function (a) {
                    xTicks.push(loopCount);
                    loopCount++;
                    return a[xAxis.fieldName];
                });
                columns.push(xTicks);
                angular.forEach(yAxis, function (value, key) {
                    ySeriesData = response.data.map(function (a) {
                        return a[value.fieldName];
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
                        type: 'area'
                    },
                    grid: {
                        x: {
                            show: true
                        },
                        y: {
                            show: true,
                        }
                    },
                    axis: {
                        x: {
                            tick: {
                                format: function (x) {
                                    return xData[x];
                                }
                            }
                        }
                    }
                });
            });

//            var chart = c3.generate({
//                bindto: element[0],
//                data: {
//                    columns: [
//                        ['data1', 300, 350, 300, 0, 0, 0],
//                        ['data2', 130, 100, 140, 200, 150, 50]
//                    ],
//                    types: {
//                        data1: 'area',
//                        data2: 'area-spline'
//                    },
//                    colors: {
//                        data1: 'hotpink',
//                        data2: 'pink'
//                    }
//                },
//                grid: {
//                    x: {
//                        show: true,
////                        lines: [{
////                                value: 2,
////                                text: 'Label 2',
////                                class: 'lineFor2'
////                            }]
//                    },
//                    y: {
//                        show: true,
//                    }
//                }
//            });
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
        });

