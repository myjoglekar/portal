app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout) {
    $scope.widgets = [];
    console.log("Widget : " + $stateParams.widgetId)
//alert('test')
    function getWidgetItem() {
        if (!$stateParams.widgetId) {
            $stateParams.widgetId = 1
        }
        console.log("Widget 1 : " + $stateParams.widgetId)
        $http.get("admin/ui/dbWidget/" + $stateParams.widgetId).success(function (response) {
            $scope.widgets = response;
            $scope.defaultWidget = response[0];
        });
    }
    getWidgetItem();

    $http.get('static/datas/imageUrl.json').success(function (response) {
        $scope.chartTypes = response;
    });
//    $http.get('../api/admin/paid/accountDevice').success(function (response) {
//       // $scope.chartTypes = response;
//    });

    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });

    $scope.pageRefresh = function () {
        getWidgetItem();
    };
    $http.get("static/datas/panelSize.json").success(function (response) {
        $scope.newPanels = response;
    });

    var uid = 100;
    $scope.addNewPanel = function (newPanel) {
        $scope.id = uid++;
        $scope.widgets.push({id: $scope.id, chartType: "", width: newPanel.panelWidth});
       // $("#" + $scope.isOpen).modal('show');
    };


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
        console.log("Data Set : " + selectedItem)
        $http.get('admin/datasources/dataSet/' + selectedItem).success(function (response) {
            $scope.dataSets = response;
        });
        $http.get('admin/datasources/dataDimensions/' + selectedItem).success(function (response) {
            $scope.dataDimensions = response;
        });
    }

    $scope.dataSetName = [];
    $scope.selectedDataSet = function (dataSet) {
        angular.forEach(dataSet, function (value, key) {
            console.log(value.name)
            $scope.dataSetName.push(value.name);
            dataSet = '';
        });
    };

    $scope.selectedDimensions = function (dimension, dataSet) {
        angular.forEach(dimension, function (value, key) {
            $scope.dimensionName = value.name;
        });
        $http.get('admin/datasources/dataDimensions/' + $scope.selectItem + '?' + 'dimensions=' + $scope.dimensionName + '&dataSet=' + $scope.dataSetName + '&sort=ga:sessions').success(function () {

        });
    };

    $scope.objectHeader = [];
    $scope.previewChart = function (chartType, widget) {
        $scope.columns = [];
        $scope.previewChartType = chartType.type;
        $scope.previewChartUrl = widget.url;
        if (chartType.type === "table") {
            $http.get(widget.url).success(function (response) {
                $scope.data = response.slice(0, 4);
                $scope.colName = response.slice(0, 1);
                angular.forEach($scope.colName, function (value, key) {
                    var arrayIndex = 0;
                    for (property in value) {
                        if ($scope.objectHeader.indexOf(property) === -1) {
                            $scope.objectHeader.push(property);
                        }
                        $scope.columns.push(
                                {title: $scope.objectHeader[arrayIndex], field: $scope.objectHeader[arrayIndex], visible: true}
                        );
                        arrayIndex++;
                        $scope.headerLength = $scope.columns.length;
                    }
                });
            });
        }
    };

    $scope.save = function (widget) {

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

app.directive('dynamicTable', function ($http) {
    return{
        restrict: 'A',
        scope: {
            dynamicTableUrl: '@',
            setTableFn: '&'
        },
//        templateUrl: 'static/views/dashboard/dynamicTable.html',
        template: '<div ng-show="loadingPie" class="text-center" style="color: #228995;">Data Fetching ...</div>' +
                '<div class="grid" ui-grid="gridOptions" ui-grid-grouping></div>',
        link: function (scope, element, attr) {
            scope.loadingPie = true;
            scope.gridOptions = {}
            if (scope.dynamicTableUrl) {
                $http.get(scope.dynamicTableUrl).success(function (response) {
                    scope.loadingPie = false;
                    scope.columnDefs = []
                    scope.data = response;
                    //console.log(response)
                    angular.forEach(response[0], function (value, key) {
                        scope.columnDefs.push({name: key, enableCellEdit: true})
                    })
//                scope.columnDefs = [
//                    {name: 'device', enableCellEdit: true},
//                    {name: 'impressions', enableCellEdit: true},
//                    {name: 'clicks', enableCellEdit: false},
//                    {name: 'ctr', enableCellEdit: false},
//                    {name: 'averagePosition', enableCellEdit: false},
//                    {name: 'cost', enableCellEdit: false},
//                    {name: 'averageCpc', enableCellEdit: false},
//                    {name: 'conversions', enableCellEdit: false},
//                    {name: 'cpa', enableCellEdit: false},
//                    {name: 'searchImpressionsShare', enableCellEdit: false},
//                    {name: 'source', enableCellEdit: false}
//                ];
                    scope.gridOptions = {columnDefs: scope.columnDefs, data: scope.data};
                })
                console.log(scope.columnDefs)
                console.log(scope.data)
//            scope.data = [
//                {name: 'Bob', title: 'CEO', 'total': 10},
//                {name: 'Bob', title: 'Lowly Developer', 'total': 100},
//                {name: 'Frank', title: 'Lowly Developer', 'total': 20}
//            ];

//            scope.columnDefs = [
//                {name: 'device', enableCellEdit: true},
//                {name: 'impressions', enableCellEdit: true},
//                {name: 'clicks', enableCellEdit: false}
//                {name: 'ctr', enableCellEdit: false}
//                {name: 'averagePosition', enableCellEdit: false}
//                {name: 'cost', enableCellEdit: false}
//                {name: 'averageCpc', enableCellEdit: false}
//                {name: 'conversions', enableCellEdit: false}
//                {name: 'cpa', enableCellEdit: false}
//                {name: 'searchImpressionsShare', enableCellEdit: false}
//                {name: 'source', enableCellEdit: false}
//            ];



//            $http.get(scope.dynamicTableUrl).success(function (response) {
//
//            });

            }
        }
    };
});
app.directive('previewDynamicTable', function () {
    return{
        restrict: 'A',
        template: '<table st-table="data" class="table table-bordered table-responsive">' +
                '<thead class="text-uppercase info">' +
                '<th>Dealer Name</th>' +
                '<th>Product Name</th>' +
                '<th>Count</th>' +
                '</thead>' +
                '<tbody>' +
                '<tr>' +
                '<td>Dealer</td>' +
                '<td>Paid</td>' +
                '<td>10</td>' +
                '</tr>' +
                '<tr>' +
                '<td>Dealer</td>' +
                '<td>Paid</td>' +
                '<td>10</td>' +
                '</tr>' +
                '<tr>' +
                '<td>Dealer</td>' +
                '<td>Paid</td>' +
                '<td>10</td>' +
                '</tr>' +
                '<tr>' +
                '<td>Dealer</td>' +
                '<td>Paid</td>' +
                '<td>10</td>' +
                '</tr>' +
                '<tr>' +
                '<td>Dealer</td>' +
                '<td>Paid</td>' +
                '<td>10</td>' +
                '</tr>' +
                '<tr>' +
                '<td>Dealer</td>' +
                '<td>Paid</td>' +
                '<td>10</td>' +
                '</tr>' +
                '<tr>' +
                '<td>Dealer</td>' +
                '<td>Paid</td>' +
                '<td>10</td>' +
                '</tr>' +
                '</tbody>' +
                '</table>'
    };
});
app.directive('lineChartDirective', function () {
    return{
        restrict: 'A',
        replace: true,
        scope: {
            setLineChartFn: '&',
            control: "=",
            collection: '@',
            lineChartId: '@',
            lineChartUrl: '@',
        },
        link: function (scope, element, attr) {
            console.log("Widget : ", 'test')

            var chart = c3.generate({
                bindto: element[0],
                data: {
                    columns: [
                        ['data1', 30, 200, 100, 400, 150, 250],
                        ['data2', 50, 20, 10, 40, 15, 25]
                    ]
                },
                grid: {
                    x: {
                        show: true,
//                        lines: [{
//                                value: 2,
//                                text: 'Label 2',
//                                class: 'lineFor2'
//                            }]
                    },
                    y: {
                        show: true,
                    }
                }
            });
        }
    };
});
app.directive('barChartDirective', function () {
    return{
        restrict: 'A',
        scope: {
            setBarChartFn: '&',
            barChartId: '@',
            barChartUrl: '@'
        },
        link: function (scope, element, attr) {
            var chart = c3.generate({
                bindto: element[0],
                data: {
                    columns: [
                        ['data1', 30, 200, 100, 400, 150, 250]
                    ],
                    type: 'bar',
                    colors: {
                        data1: '#74C4C6'
                    }
                },
                grid: {
                    x: {
                        show: true,
//                        lines: [{
//                                value: 2,
//                                text: 'Label 2',
//                                class: 'lineFor2'
//                            }]
                    },
                    y: {
                        show: true,
                    }
                }
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
            setPieChartFn: '&',
            pieChartId: '@',
            pieChartUrl: '@',
            loadingPie: '&'
        },
        link: function (scope, element, attr) {
            scope.loadingPie = true;
            if (scope.pieChartUrl) {
                $http.get(scope.pieChartUrl).success(function (response) {
                    scope.items = []
                    scope.loadingPie = false;
                    console.log(response)
                    angular.forEach(response, function (value, key) {
                        scope.items.push({upload: value.conversions, name: value.source});
                        console.log(scope.items)
//                    //console.log(value.conversions+" - "+value.source)
//                    scope.data.push([value.conversions, value.source])
//                    console.log(scope.data)
//                    angular.forEach(value, function(data, header){                        
//                    })
                    })
                    //if (scope.items.length > 0) {
                    var data = {};
                    var sites = [];
                    scope.items.forEach(function (e) {
                        sites.push(e.name);
                        data[e.name] = e.upload;
                    })
                    var chart = c3.generate({
                        bindto: element[0],
                        data: {
                            json: [data],
                            keys: {
                                value: sites,
                            },
                            type: 'pie',
                            colors: {
                                Paid: '#74C4C6',
                                Display: '#228995',
                                SEO: '#5A717A',
                                Overall: '#3D464D',
                                'Dynamic Display': '#F1883C',
                            },
                        },
                        pie: {
                            label: {
                                format: function (value, ratio, id) {
                                    return value;
                                }
                            }
                        }
//                donut: {
//                    title: "Dogs love:",
//                }
                    });
                    // }
                })
            }
        }
    };
});
app.directive('areaChartDirective', function () {
    return{
        restrict: 'A',
        scope: {
            setPieChartFn: '&',
            pieChartId: '@',
            pieChartUrl: '@'
        },
        link: function (scope, element, attr) {
            var chart = c3.generate({
                bindto: element[0],
                data: {
                    columns: [
                        ['data1', 300, 350, 300, 0, 0, 0],
                        ['data2', 130, 100, 140, 200, 150, 50]
                    ],
                    types: {
                        data1: 'area',
                        data2: 'area-spline'
                    },
                    colors: {
                        data1: 'hotpink',
                        data2: 'pink'
                    }
                },
                grid: {
                    x: {
                        show: true,
//                        lines: [{
//                                value: 2,
//                                text: 'Label 2',
//                                class: 'lineFor2'
//                            }]
                    },
                    y: {
                        show: true,
                    }
                }
            });
        }
    };
});

