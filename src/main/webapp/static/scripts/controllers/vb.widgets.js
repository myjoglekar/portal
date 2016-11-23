app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout) {
    $scope.widgets = [];   

    function getItem() {
        $http.get("admin/ui/dbWidget/" + $stateParams.widgetId).success(function (response) {
            $scope.widgets = response;
            $scope.defaultWidget = response[0];
        });
    }
    getItem();

    $http.get('static/datas/imageUrl.json').success(function (response) {
        $scope.chartTypes = response;
    });

    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });

    $scope.pageRefresh = function () {
        getItem();
    };
    $http.get("static/datas/panelSize.json").success(function (response) {
        $scope.newPanels = response;
    });


    var uid = 10;
    $scope.addNewPanel = function (newPanel) {
        $scope.id = uid++;
        $scope.widgets.push({chartId: "widget" + $scope.id, chartType: "New Widget", width: newPanel.panelWidth});
        $("#" + $scope.isOpen).modal('show');
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
    $scope.setLineFn = function (lineFn) {
        $scope.directiveLineFn = lineFn;
    };
    $scope.setAreaFn = function (areaFn) {
        $scope.directiveAreaFn = areaFn;
    };
    $scope.setBarFn = function (barFn) {
        $scope.directiveBarFn = barFn;
    };
    $scope.setPieFn = function (pieFn) {
        $scope.directivePieFn = pieFn;
    };
    $scope.setDonutFn = function (donutFn) {
        $scope.directiveDonutFn = donutFn;
    };
    $scope.setGroupedBarFn = function (groupedBarFn) {
        $scope.directiveGroupedBarFn = groupedBarFn;
    };
    $scope.setTableFn = function (tableFn) {
        $scope.directiveTableFn = tableFn;
    };

});

app.directive('dynamicTable', function ($http) {
    return{
        restrict: 'A',
        scope: {
            dynamicTableUrl: '@',
            setTableFn: '&'
        },
        templateUrl: 'static/views/dashboard/dynamicTable.html',
        link: function (scope, element, attr) {
            scope.refreshTable = function () {
                scope.currentPage = 1;
                scope.pageSize = 3;
                scope.objectHeader = []
                $http.get(scope.dynamicTableUrl).success(function (response) {
                    scope.columns = []
                    angular.forEach(response, function (obj, header) {
                        scope.colName = obj.childItems.slice(0, 1);
                        scope.tableItems = obj.childItems;
                        angular.forEach(scope.colName, function (value, key) {
                            var arrayIndex = 0;
                            for (property in value) {
                                if (scope.objectHeader.indexOf(property) === -1) {
                                    scope.objectHeader.push(property);
                                }
                                scope.columns.push(
                                        {title: scope.objectHeader[arrayIndex], field: scope.objectHeader[arrayIndex], visible: true}
                                );
                                arrayIndex++;
                                scope.headerLength = scope.columns.length;
                            }
                        });
                    });
                });
            };
            scope.setTableFn({tableFn: scope.refreshTable});
            scope.refreshTable();
        }
    };
});
app.directive('previewDynamicTable', function () {
    return{
        restrict: 'A',
        template: '<table st-table="data" class="table table-bordered table-responsive">' +
                '<thead><tr>' +
                '<th class="text-uppercase info" ng-repeat="col in columns">' + '<form class="form-inline">' +
                '<div class="checkbox">' +
                '<label><input type="checkbox">{{col.title}}</label>' +
                '</div>' +
                '</form></th>' +
                '' +
                '</tr></thead>' +
                '<tbody>' +
                '<tr ng-repeat="value in data">' +
                '<td ng-repeat="col in columns"> {{value[col.field] | limitTo: 5}} </td>' +
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
//            collectionData = JSON.parse(scope.collection);
//            console.log(collectionData.chartId);
            var m = [30, 80, 30, 70]; // margins
            var w = 300// width
            var h = 200 // height
//    var w = 400// width
//    var h = 200 // height
//        var w = 1000 - m[1] - m[3]; // width
//        var h = 400 - m[0] - m[2]; // height
            var x_dim_accessor = function (d) {
                return d.elapsed
            };
            var y_dim_accessor = function (d) {
                return d.throughput
            };
            var x_range;
            var y_range;
            scope.refreshWidgetLine = function () {
                element.html('');
                d3.json(scope.lineChartUrl, function (error, json) {
                    var data;
                    if (!json)
                        json = error; //not sure why error seems to contain the data!...
                    if (json[0] && json[0].summary) {
                        data = json[0].summary;
                    } else {
                        data = json;
                    }
                    x_range = [
                        d3.min(data, x_dim_accessor),
                        d3.max(data, x_dim_accessor)
                    ];
                    y_range = [
                        d3.min(data, y_dim_accessor),
                        d3.max(data, y_dim_accessor)
                    ];
                    var data2 = data.filter(function (d) {
                        return d.label;
                    }).sort(function (a, b) {
                        return x_dim_accessor(a) - x_dim_accessor(b);
                    });
                    renderLineChart(data2);
                });
            };
            scope.setLineChartFn({lineChartFn: scope.refreshWidgetLine});
            scope.refreshWidgetLine();
            function renderLineChart(data) {
                var x = d3.scale.linear().domain(x_range).range([0, w]);
                var y = d3.scale.linear().domain(y_range).range([h, 0]);
                var line = d3.svg.line()
                        .x(function (d, i) {
                            return x(x_dim_accessor(d));
                        })
                        .y(function (d) {
                            return y(y_dim_accessor(d));
                        })
                var graph = d3.select(element[0]).append("svg:svg")
//                        .attr("width", w + m[1] + m[3])
//                        .attr("height", h + m[0] + m[2])
                        .attr("viewBox", "0 0 380 250")
                        .append("svg:g")
                        .attr("transform", "translate(" + m[3] + "," + m[0] + ")");
                var xAxis = d3.svg.axis().scale(x).tickSize(-h).tickSubdivide(true);
                graph.append("svg:g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + h + ")")
                        .call(xAxis);
                var yAxisLeft = d3.svg.axis().scale(y).ticks(4).orient("left");
                graph.append("svg:g")
                        .attr("class", "y axis")
                        .attr("transform", "translate(-25,0)")
                        .call(yAxisLeft);
                graph.append("svg:path").attr("d", line(data));
            }
        }
    };
});
app.directive('areaChartDirective', function () {
    return{
        restrict: 'A',
        replace: true,
        scope: {
            setAreaChartFn: '&',
            areaChartId: '@',
            areaChartUrl: '@'
        },
        //template: '<div id="1{{areaChartId}}">{{areaChartId?"0":"1"}}</div>',
        link: function (scope, element, attr) {
            var margin = {top: 20, right: 20, bottom: 30, left: 50},
                    width = 360 - margin.left - margin.right,
                    height = 260 - margin.top - margin.bottom;

            var parseDate = d3.time.format("%d-%b-%y").parse;

            var x = d3.time.scale()
                    .range([0, width]);

            var y = d3.scale.linear()
                    .range([height, 0]);

            var xAxis = d3.svg.axis()
                    .scale(x)
                    .orient("bottom");

            var yAxis = d3.svg.axis()
                    .scale(y)
                    .orient("left");

            var area = d3.svg.area()
                    .x(function (d) {
                        return x(d.date);
                    })
                    .y0(height)
                    .y1(function (d) {
                        return y(d.close);
                    });
            console.log($('#widgetwidget1'));
            console.log($('#' + scope.areaChartId).attr('id'));

            var svg = d3.select(element[0]).append("svg")
                    .attr("viewBox", "0 0 380 250")
//    .attr("width", width + margin.left + margin.right)
//    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
            scope.refreshWidgetArea = function () {
                d3.tsv(scope.areaChartUrl, function (error, data) {
                    if (error)
                        throw error;

                    data.forEach(function (d) {
                        d.date = parseDate(d.date);
                        d.close = +d.close;
                    });

                    x.domain(d3.extent(data, function (d) {
                        return d.date;
                    }));
                    y.domain([0, d3.max(data, function (d) {
                            return d.close;
                        })]);

                    svg.append("path")
                            .datum(data)
                            .attr("class", "area")
                            .attr("d", area);

                    svg.append("g")
                            .attr("class", "x axis")
                            .attr("transform", "translate(0," + height + ")")
                            .call(xAxis);

                    svg.append("g")
                            .attr("class", "y axis")
                            .call(yAxis)
                            .append("text")
                            .attr("transform", "rotate(-90)")
                            .attr("y", 6)
                            .attr("dy", ".71em")
                            .style("text-anchor", "end")
                            .text("Price ($)");
                });

            }
            scope.setAreaChartFn({areaChartFn: scope.refreshWidgetArea});
            scope.refreshWidgetArea();
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
            var margin = {top: 40, right: 40, bottom: 40, left: 40},
                    width = 420 - margin.left - margin.right,
                    height = 260 - margin.top - margin.bottom;
            var x = d3.scale.ordinal().rangeRoundBands([0, width], .05);
            var y = d3.scale.linear().range([height, 0]);
            var xAxis = d3.svg.axis()
                    .scale(x)
                    .orient("bottom")
            var yAxis = d3.svg.axis()
                    .scale(y)
                    .orient("left")
                    .ticks(10);
            var svg = d3.select(element[0]).append("svg")
                    .attr("viewBox", "0 0 380 250")
//                    .attr("width", width + margin.left + margin.right)
//                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform",
                            "translate(" + margin.left + "," + margin.top + ")");

            scope.refreshWidgetBar = function () {
//                element.html('');
                d3.json(scope.barChartUrl, function (error, barData) {
                    var data = barData.slice(0, 10)
                    data.forEach(function (d) {
                        d.Letter = d.Letter;
                        d.Freq = +d.Freq;
                    });
                    x.domain(data.map(function (d) {
                        return d.Letter;
                    }));
                    y.domain([0, d3.max(data, function (d) {
                            return d.Freq;
                        })]);
                    svg.append("g")
                            .attr("class", "x axis")
                            .attr("transform", "translate(0," + height + ")")
                            .call(xAxis)
                            .selectAll("text")
                            .style("text-anchor", "end")
                            .attr("dx", "-.8em")
                            .attr("dy", "-.55em")
                            .attr("transform", "rotate(-90)");
                    svg.append("g")
                            .attr("class", "y axis")
                            .call(yAxis)
                            .append("text")
                            .attr("transform", "rotate(-90)")
                            .attr("y", 5)
                            .attr("dy", ".71em")
                            .style("text-anchor", "end")
                            .text("Frequency");
                    // Add bar chart
                    svg.selectAll("bar")
                            .data(data)
                            .enter().append("rect")
                            .attr("class", "bar")
                            .attr("x", function (d) {
                                return x(d.Letter);
                            })
                            .attr("width", x.rangeBand())
                            .attr("y", function (d) {
                                return y(d.Freq);
                            })
                            .attr("height", function (d) {
                                return height - y(d.Freq);
                            });
                });
            }
            scope.setBarChartFn({barChartFn: scope.refreshWidgetBar});
            scope.refreshWidgetBar();
        }
    };
});
app.directive('pieChartDirective', function () {
    return{
        restrict: 'A',
//        template: '<div id="pieChartDashboard"></div>',
        scope: {
            // value: "@value
            // ",
            //collection: '@',
            setPieChartFn: '&',
            pieChartId: '@',
            pieChartUrl: '@'
        },
        link: function (scope, element, attr) {

            var width = 300,
                    height = 260,
                    radius = Math.min(width, height) / 2;
            var color = d3.scale.ordinal()
                    .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);
            var arc = d3.svg.arc()
                    .outerRadius(radius - 10)
                    .innerRadius(0);
            var labelArc = d3.svg.arc()
                    .outerRadius(radius - 40)
                    .innerRadius(radius - 40);
            var pie = d3.layout.pie()
                    .sort(null)
                    .value(function (d) {
                        return d.population;
                    });
            var svg = d3.select(element[0]).append("svg")
                    .attr("viewBox", "0 0 380 250")
//                    .attr("width", width)
//                    .attr("height", height)
                    .append("g")
                    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");


            scope.refreshWidgetPie = function () {
//                element.html('');
                d3.csv(scope.pieChartUrl, type, function (error, data) {
                    if (error)
                        throw error;
                    var g = svg.selectAll(".arc")
                            .data(pie(data))
                            .enter().append("g")
                            .attr("class", "arc");
                    g.append("path")
                            .attr("d", arc)
                            .style("fill", function (d) {
                                return color(d.data.color);
                            });
                    g.append("text")
                            .attr("transform", function (d) {
                                return "translate(" + labelArc.centroid(d) + ")";
                            })
                            .attr("dy", ".35em")
                            .text(function (d) {
                                return d.data.color;
                            });
                });
                function type(d) {
                    d.population = +d.population;
                    return d;
                }//            
            }
            scope.setPieChartFn({pieChartFn: scope.refreshWidgetPie});
            scope.refreshWidgetPie();
        }
    };
});
app.directive('donutChartDirective', function () {
    return{
        restrict: 'A',
//        template: '<div id="donutChartDashboard"></div>',
        scope: {
            // value: "@value",
            //collection: '@',
            setDonutChartFn: '&',
            donutChartId: '@',
            donutChartUrl: '@'
        },
        link: function (scope, element, attr) {
            var width = 320,
                    height = 260,
                    radius = Math.min(width, height) / 2;
            var color = d3.scale.ordinal()
                    .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);
            var arc = d3.svg.arc()
                    .outerRadius(radius - 10)
                    .innerRadius(radius - 70);
            var pie = d3.layout.pie()
                    .sort(null)
                    .value(function (d) {
                        return d.population;
                    });
            var svg = d3.select(element[0]).append("svg")
                    .attr("viewBox", "0 0 380 250")
//                    .attr("width", width)
//                    .attr("height", height)
                    .append("g")
                    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

            scope.refreshWidgetDonut = function () {
//                element.html('');
                d3.csv(scope.donutChartUrl, type, function (error, data) {
                    if (error)
                        throw error;
                    var g = svg.selectAll(".arc")
                            .data(pie(data))
                            .enter().append("g")
                            .attr("class", "arc");
                    g.append("path")
                            .attr("d", arc)
                            .style("fill", function (d) {
                                return color(d.data.age);
                            });
                    g.append("text")
                            .attr("transform", function (d) {
                                return "translate(" + arc.centroid(d) + ")";
                            })
                            .attr("dy", ".35em")
                            .text(function (d) {
                                return d.data.age;
                            });
                });
                function type(d) {
                    d.population = +d.population;
                    return d;
                }
                ;
            }
            scope.setDonutChartFn({donutChartFn: scope.refreshWidgetDonut});
            scope.refreshWidgetDonut();
        }
    };
});

app.directive('groupedBarChartDirective', function () {
    return{
        restrict: 'A',
//        template: '<div id="groupedBarChartDashboard"></div>',
        scope: {
            // value: "@value",
            //collection: '@',
            setGroupedBarChartFn: '&',
            groupedBarChartId: '@',
            groupedBarChartUrl: '@'
        },
        link: function (scope, element, attr) {
            var margin = {top: 20, right: 20, bottom: 30, left: 50},
                    width = 380 - margin.left - margin.right,
                    height = 260 - margin.top - margin.bottom;
            var x0 = d3.scale.ordinal()
                    .rangeRoundBands([0, width], .1);
            var x1 = d3.scale.ordinal();
            var y = d3.scale.linear()
                    .range([height, 0]);
            var color = d3.scale.ordinal()
                    .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);
            var xAxis = d3.svg.axis()
                    .scale(x0)
                    .orient("bottom");
            var yAxis = d3.svg.axis()
                    .scale(y)
                    .orient("left")
                    .tickFormat(d3.format(".2s"));
            var svg = d3.select(element[0]).append("svg")
                    .attr("viewBox", "0 0 380 250")
//                    .attr("width", width + margin.left + margin.right)
//                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

            scope.refreshWidgetGroupedBar = function () {
//                               element.html('');
                d3.csv(scope.groupedBarChartUrl, function (error, data) {
                    if (error)
                        throw error;
                    var ageNames = d3.keys(data[0]).filter(function (key) {
                        return key !== "State";
                    });
                    data.forEach(function (d) {
                        d.ages = ageNames.map(function (name) {
                            return {name: name, value: +d[name]};
                        });
                    });
                    x0.domain(data.map(function (d) {
                        return d.State;
                    }));
                    x1.domain(ageNames).rangeRoundBands([0, x0.rangeBand()]);
                    y.domain([0, d3.max(data, function (d) {
                            return d3.max(d.ages, function (d) {
                                return d.value;
                            });
                        })]);
                    svg.append("g")
                            .attr("class", "x axis")
                            .attr("transform", "translate(0," + height + ")")
                            .call(xAxis);
                    svg.append("g")
                            .attr("class", "y axis")
                            .call(yAxis)
                            .append("text")
                            .attr("transform", "rotate(-90)")
                            .attr("y", 6)
                            .attr("dy", ".71em")
                            .style("text-anchor", "end")
                            .text("Population");
                    var state = svg.selectAll(".state")
                            .data(data)
                            .enter().append("g")
                            .attr("class", "state")
                            .attr("transform", function (d) {
                                return "translate(" + x0(d.State) + ",0)";
                            });
                    state.selectAll("rect")
                            .data(function (d) {
                                return d.ages;
                            })
                            .enter().append("rect")
                            .attr("width", x1.rangeBand())
                            .attr("x", function (d) {
                                return x1(d.name);
                            })
                            .attr("y", function (d) {
                                return y(d.value);
                            })
                            .attr("height", function (d) {
                                return height - y(d.value);
                            })
                            .style("fill", function (d) {
                                return color(d.name);
                            });
                    var legend = svg.selectAll(".legend")
                            .data(ageNames.slice().reverse())
                            .enter().append("g")
                            .attr("class", "legend")
                            .attr("transform", function (d, i) {
                                return "translate(0," + i * 20 + ")";
                            });
                    legend.append("rect")
                            .attr("x", width - 18)
                            .attr("width", 18)
                            .attr("height", 18)
                            .style("fill", color);
                    legend.append("text")
                            .attr("x", width - 24)
                            .attr("y", 9)
                            .attr("dy", ".35em")
                            .style("text-anchor", "end")
                            .text(function (d) {
                                return d;
                            });
                });
            };
            scope.setGroupedBarChartFn({groupedBarChartFn: scope.refreshWidgetGroupedBar});
            scope.refreshWidgetGroupedBar();
        }
    };
});

