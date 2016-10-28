app.controller('PanelController', function ($scope, $http, $stateParams, $timeout) {
    $http.get("static/datas/" + $stateParams.panelId + ".json").success(function (response) {
        $scope.panels = response;
    });

    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });

    $scope.panels = [];
    var uid = 7;
    $scope.add = function () {
        $scope.id = uid++;
        $scope.panels.push({chartId: "widget" + $scope.id});
    };

    $scope.dataSet = function () {
        console.log($scope.datasource)
    }

    $scope.onDropComplete = function (index, panel, evt) {
        var otherObj = $scope.panels[index];
        var otherIndex = $scope.panels.indexOf(panel);
        $scope.panels[index] = panel;
        $scope.panels[otherIndex] = otherObj;
    };

    $scope.removePanel = function (index) {
        $scope.panels.splice(index, 1);
    };
    // angular.$apply();
    //chart();
//    $timeout(function(){chart()}, 1000)

});
app.directive('dynamicTable', function () {
    return{
        restrict: 'A',
        templateUrl: 'static/views/dashboard/dynamicTable.html'
    }
});
app.directive('lineChartDirective', function () {
    return{
        restrict: 'A',
        template: '<div id="lineChartDashboard"></div>',
        scope: {
            value: "@value"
        },
        link: function (attr, element, scope) {
            var m = [30, 80, 30, 70]; // margins
            var w = 200// width
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
            d3.json('static/datas/lineChart.json', function (error, json) {
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
                var graph = d3.select("#lineChartDashboard").append("svg:svg")
                        .attr("width", w + m[1] + m[3])
                        .attr("height", h + m[0] + m[2])
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
        template: '<div id="areaChartDashboard"></div>',
        scope: {
            value: "@value"
        },
        link: function (attr, element, scope) {
//    var margin = {top: 20, right: 20, bottom: 30, left: 50},
//    width = 960 - margin.left - margin.right,
//            height = 500 - margin.top - margin.bottom;
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
                    .attr("width", width + margin.left + margin.right)
                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
            d3.json('static/datas/areaChart.json', function (error, json) {
                var data;
                if (!json)
                    json = error; //not sure why error seems to contain the data!...
                if (json[0] && json[0].summary) {
                    data = json[0].summary;
                } else {
                    data = json;
                }
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
    };
});
app.directive('barChartDirective', function () {
    return{
        restrict: 'A',
        template: '<div id="barChartDashboard"></div>',
        scope: {
            value: "@value"
        },
        link: function (attr, element, scope) {
            var margin = {top: 40, right: 40, bottom: 40, left: 40},
            width = 400 - margin.left - margin.right,
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
                    .attr("width", width + margin.left + margin.right)
                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform",
                            "translate(" + margin.left + "," + margin.top + ")");
            d3.json('static/datas/barChart.json', function (error, data) {
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
            value: "@value"
        },
        link: function (attr, element, scope) {
//    var w = 300;
//    var h = 200;
            var w = 300;
            var h = 260;
            var r = h / 2;
            var color = d3.scale.category20b();

            d3.json('static/datas/pieChart.json', function (error, data) {
                var vis = d3.select("#pieChartDashboard")
                        .append("svg:svg")
                        .data([data])
                        .attr("width", w)
                        .attr("height", h)
                        .append("svg:g")
                        .attr("transform", "translate(" + r + "," + r + ")");
                var pie = d3.layout.pie().value(function (d) {
                    return d.value;
                });
                var arc = d3.svg.arc().outerRadius(r);
                var arcs = vis.selectAll("g.slice").data(pie).enter().append("svg:g").attr("class", "slice");
                arcs.append("svg:path")
                        .attr("fill", function (d, i) {
                            return color(i);
                        })
                        .attr("d", function (d) {
                            console.log(arc(d));
                            return arc(d);
                        });
                arcs.append("svg:text").attr("transform", function (d) {
                    d.innerRadius = 0;
                    d.outerRadius = r;
                    return "translate(" + arc.centroid(d) + ")";
                }).attr("text-anchor", "middle").text(function (d, i) {
                    return data[i].label;
                });
            });
        }
    };
});
app.directive('donutChartDirective', function () {
    return{
        restrict: 'A',
        template: '<div id="donutChartDashboard"></div>',
        scope: {
            value: "@value"
        },
        link: function (attr, element, scope) {
            var width = 300;
            var height = 260;
            var radius = Math.min(width, height) / 2;
//    var width = 800;
//     var height = 250;

            var color = d3.scale.ordinal()
                    .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);
            var arc = d3.svg.arc()
                    .outerRadius(radius - 10)
                    .innerRadius(radius - 70);
            var pie = d3.layout.pie()
                    .sort(null)
                    .value(function (d) {
                        return d.totalCrimes;
                    });
            var svg = d3.select("#donutChartDashboard").append("svg")
                    .attr("width", width)
                    .attr("height", height)
                    .append("g")
                    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
            d3.json('static/datas/donutChart.json', function (error, data) {
                var g = svg.selectAll(".arc")
                        .data(pie(data))
                        .enter().append("g")
                        .attr("class", "arc");
                g.append("path")
                        .attr("d", arc)
                        .style("fill", function (d) {
                            return color(d.data.crimeType);
                        });
                g.append("text")
                        .attr("transform", function (d) {
                            return "translate(" + arc.centroid(d) + ")";
                        })
                        .attr("dy", ".35em")
                        .style("text-anchor", "middle")
                        .text(function (d) {
                            return d.data.crimeType;
                        });
            });
        }
    };
});