app.controller('PanelController', function ($scope, $http, $stateParams, $timeout) {
    function getItem() {
        $http.get("static/datas/" + $stateParams.panelId + ".json").success(function (response) {
            $scope.panels = response;
        });
    }
    getItem();
//    $http.get("static/datas/" + $stateParams.panelId + ".json").success(function (response) {
//        $scope.panels = response;
//    });

    $http.get('static/datas/imageUrl.json').success(function (response) {
        $scope.chartTypes = response;
    });

    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });

    $scope.pageRefresh = function () {
        getItem();
    };

    $scope.panels = [];
    var uid = 10;
    $scope.add = function () {
        $scope.id = uid++;
        $scope.panels.push({chartId: "widget" + $scope.id, width: 2,
            minHeight: "25vh",
            widthClass: "col-md-4", chartType: "line"});
    };
    //Data Source
    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });

    $scope.selectedDataSource = function (selectedItem) {
        $scope.selectItem = selectedItem;
        selectedItems(selectedItem);
        //$scope.selectDataSet = selectedItem;
        console.log("Data Source : " + selectedItem)
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
        console.log("Name : " + $scope.dataSetName);
    };
    $scope.selectedDimensions = function (dimension, dataSet) {
        angular.forEach(dimension, function (value, key) {
            $scope.dimensionName = value.name;
        });
        $http.get('admin/datasources/dataDimensions/' + $scope.selectItem + '?' + 'dimensions=' + $scope.dimensionName + '&dataSet=' + $scope.dataSetName + '&sort=ga:sessions').success(function () {

        });
        console.log($scope.dimensionName, $scope.dataSetName);
    };


    var value = [];
    $scope.objectHeader = [];
    $scope.columns = []
    $scope.previewChart = function (chartType, panel) {
        $scope.previewChartType = chartType.type;
        $scope.previewChartUrl = panel.url;
        if (chartType.type == "table") {
            $http.get(panel.url).success(function (response) {
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

    $scope.save = function (panel) {
    };

    $scope.onDropComplete = function (index, panel, evt) {
        var otherObj = $scope.panels[index];
        var otherIndex = $scope.panels.indexOf(panel);
        $scope.panels[index] = panel;
        $scope.panels[otherIndex] = otherObj;
    };

    $scope.removePanel = function (index) {
        $scope.panels.splice(index, 1);
    };

    $scope.setLineFn = function (lineFn) {
        $scope.directiveLineFn = lineFn;
    };
    $scope.setAreaFn = function (areaFn) {
        $scope.directiveAreaFn = areaFn;
    };
});
app.directive('dynamicTable', function ($http) {
    return{
        restrict: 'A',
        scope:{
          dynamicTableUrl:'@'  
        },
        templateUrl: 'static/views/dashboard/dynamicTable.html',
        link: function (scope, element, attr) {
            scope.currentPage = 1;
            scope.pageSize = 3;
            scope.objectHeader = []
            $http.get(scope.dynamicTableUrl).success(function (response) {
                console.log("datas :", JSON.stringify(response))
                scope.columns = []
                angular.forEach(response, function(obj, header){
                scope.colName = obj.childItems.slice(0, 1);
                scope.tableItems = obj.childItems;
                    //console.log(obj.childItems)
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
        }
    }
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
        template: '<div id="lineChartDashboard{{lineChartId}}"></div>',
        link: function (scope, element, attr) {
            //scope.internalControl = scope.control || {};
            console.log("lineChart : " + scope.collection);
            console.log("lineChart Item : " + scope.lineChartId);
            console.log("lineChart Url : " + scope.lineChartUrl);

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
                        return d.label
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
                var graph = d3.select("#lineChartDashboard" + scope.lineChartId).append("svg:svg")
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
        template: '<div id="areaChartDashboard"></div>',
        link: function (scope, element, attr) {
            var margin = {top: 20, right: 20, bottom: 30, left: 50},
            width = 380 - margin.left - margin.right,
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

            var svg = d3.select("#areaChartDashboard").append("svg")
                    .attr("viewBox","0 0 380 250")
//                    .attr("width", width + margin.left + margin.right)
//                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

            scope.refreshWidgetArea = function () {
//                element.html('');
                d3.tsv(scope.areaChartUrl, function (error, areaData) {
                    var data = areaData;
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
            };
            scope.setAreaChartFn({areaChartFn: scope.refreshWidgetArea});
            scope.refreshWidgetArea();
        }
    };
});

app.directive('barChartDirective', function () {
    return{
        restrict: 'A',
        template: '<div id="barChartDashboard"></div>',
        scope: {
            // value: "@value",
            //collection: '@',
            barChartId: '@',
            barChartUrl: '@'
        },
        link: function (attr, element, scope) {
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
            var svg = d3.select("#barChartDashboard").append("svg")
                    .attr("viewBox", "0 0 380 250")
//                    .attr("width", width + margin.left + margin.right)
//                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform",
                            "translate(" + margin.left + "," + margin.top + ")");
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
    };
});
app.directive('pieChartDirective', function () {
    return{
        restrict: 'A',
        template: '<div id="pieChartDashboard"></div>',
        scope: {
            // value: "@value",
            //collection: '@',
            pieChartId: '@',
            pieChartUrl: '@'
        },
        link: function (attr, element, scope) {

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

            var svg = d3.select("#pieChartDashboard").append("svg")
                    .attr("viewBox","0 0 380 250") 
//                    .attr("width", width)
//                    .attr("height", height)
                    .append("g")
                    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

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
            }



//            var w = 300;
//            var h = 260;
//            var r = h / 2;
//            var color = d3.scale.category20b();
//
//            d3.json(scope.pieChartUrl, function (error, data) {
//                var vis = d3.select("#pieChartDashboard")
//                        .append("svg:svg")
//                        .data([data])
//                        .attr("width", w)
//                        .attr("height", h)
//                        .append("svg:g")
//                        .attr("transform", "translate(" + r + "," + r + ")");
//                var pie = d3.layout.pie().value(function (d) {
//                    return d.value;
//                });
//                var arc = d3.svg.arc().outerRadius(r);
//                var arcs = vis.selectAll("g.slice").data(pie).enter().append("svg:g").attr("class", "slice");
//                arcs.append("svg:path")
//                        .attr("fill", function (d, i) {
//                            return color(i);
//                        })
//                        .attr("d", function (d) {
//                            console.log(arc(d));
//                            return arc(d);
//                        });
//                arcs.append("svg:text").attr("transform", function (d) {
//                    d.innerRadius = 0;
//                    d.outerRadius = r;
//                    return "translate(" + arc.centroid(d) + ")";
//                }).attr("text-anchor", "middle").text(function (d, i) {
//                    return data[i].label;
//                });
//            });
        }
    };
});
app.directive('donutChartDirective', function () {
    return{
        restrict: 'A',
        template: '<div id="donutChartDashboard"></div>',
        scope: {
            // value: "@value",
            //collection: '@',
            donutChartId: '@',
            donutChartUrl: '@'
        },
        link: function (attr, element, scope) {
            var width = 300,
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

            var svg = d3.select("#donutChartDashboard").append("svg")
                    .attr("viewBox","0 0 380 250")
//                    .attr("width", width)
//                    .attr("height", height)
                    .append("g")
                    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

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



//            var width = 300;
//            var height = 260;
//            var radius = Math.min(width, height) / 2;
////    var width = 800;
////     var height = 250;
//
//            var color = d3.scale.ordinal()
//                    .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);
//            var arc = d3.svg.arc()
//                    .outerRadius(radius - 10)
//                    .innerRadius(radius - 70);
//            var pie = d3.layout.pie()
//                    .sort(null)
//                    .value(function (d) {
//                        return d.totalCrimes;
//                    });
//            var svg = d3.select("#donutChartDashboard").append("svg")
//                    .attr("width", width)
//                    .attr("height", height)
//                    .append("g")
//                    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
//            d3.json(scope.donutChartUrl, function (error, data) {
//                var g = svg.selectAll(".arc")
//                        .data(pie(data))
//                        .enter().append("g")
//                        .attr("class", "arc");
//                g.append("path")
//                        .attr("d", arc)
//                        .style("fill", function (d) {
//                            return color(d.data.crimeType);
//                        });
//                g.append("text")
//                        .attr("transform", function (d) {
//                            return "translate(" + arc.centroid(d) + ")";
//                        })
//                        .attr("dy", ".35em")
//                        .style("text-anchor", "middle")
//                        .text(function (d) {
//                            return d.data.crimeType;
//                        });
//            });
        }
    };
});



app.directive('groupedBarChartDirective', function () {
    return{
        restrict: 'A',
        template: '<div id="groupedBarChartDashboard"></div>',
        scope: {
            // value: "@value",
            //collection: '@',
            groupedBarChartId: '@',
            groupedBarChartUrl: '@'
        },
        link: function (attr, element, scope) {
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

            var svg = d3.select("#groupedBarChartDashboard").append("svg")
                    .attr("viewBox", "0 0 380 250")
//                    .attr("width", width + margin.left + margin.right)
//                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

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

        }
    };
});
