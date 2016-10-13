app.controller("DashboardController", function ($scope, $http) {
    //Add Panels 
    $scope.panels = [];
    var uid = 7;
    $scope.add = function () {
        $scope.id = uid++;
        $scope.panels.push({chartId: $scope.id});
        console.log("Uid : ", $scope.id);
    };



//    //Start Line Chart
//
//    var margin = {top: 20, right: 20, bottom: 30, left: 50},
//    width = 960 - margin.left - margin.right,
//            height = 500 - margin.top - margin.bottom;
//
//    var formatDate = d3.time.format("%d-%b-%y");
//
//    var x = d3.time.scale()
//            .range([0, width]);
//
//    var y = d3.scale.linear()
//            .range([height, 0]);
//    var xAxis = d3.svg.axis()
//            .scale(x)
//            .orient("bottom");
//
//    var yAxis = d3.svg.axis()
//            .scale(y)
//            .orient("left");
//
//    var line = d3.svg.line()
//            .x(function (d) {
//                return x(d.date);
//            })
//            .y(function (d) {
//                return y(d.close);
//            });
//
//    var svg = d3.select("#lineChart").append("svg")
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom)
//            .append("g")
//            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//    
//    //End Line Chart
    
    
    $http.get('static/datas/labels.json').success(function (response) {
        $scope.labels = response;
    });

    $scope.charts = [];
    $http.get('static/datas/d3chart.json').success(function (response, error) {
//       $scope.charts=response; 
        angular.forEach(response, function (value, key) {
            $scope.charts = value;
            console.log(value);
            
            if (value.type === 'line') {
               // $("#div1").load("static/views/charts/lineChart.html");
//                d3.tsv("static/datas/lineChart.tsv", type, function (error, data) {
//                    if (error)
//                        throw error;
//
//                    x.domain(d3.extent(data, function (d) {
//                        return d.date;
//                    }));
//                    y.domain(d3.extent(data, function (d) {
//                        return d.close;
//                    }));
//
//                    svg.append("g")
//                            .attr("class", "x axis")
//                            .attr("transform", "translate(0," + height + ")")
//                            .call(xAxis);
//
//                    svg.append("g")
//                            .attr("class", "y axis")
//                            .call(yAxis)
//                            .append("text")
//                            .attr("transform", "rotate(-90)")
//                            .attr("y", 6)
//                            .attr("dy", ".71em")
//                            .style("text-anchor", "end")
//                            .text("Price ($)");
//
//                    svg.append("path")
//                            .datum(data)
//                            .attr("class", "line")
//                            .attr("d", line);
//                });
//                var formatDate = d3.time.format("%d-%b-%y");
//                function type(d) {
//                    d.date = formatDate.parse(d.date);
//                    d.close = +d.close;
//                    return d;
//                }
            }

        });
    });

    $scope.onDropComplete = function (index, panel, evt) {
        var otherObj = $scope.panels[index];
        var otherIndex = $scope.panels.indexOf(panel);
        $scope.panels[index] = panel;
        $scope.panels[otherIndex] = otherObj;
    };

    $http.get('static/datas/imageUrl.json').success(function (response) {
        $scope.chartTypes = response;
        //console.log($scope.chartTypes.url);

    });

    var dataPoints = [];

    $scope.previewChart = function (chartType, panel) {
        panel.selectedChartType = chartType;
        console.log(chartType.type, panel.url);
        $http.get(panel.url).success(function (response) {
            dataPoints = response;
            //console.log("dataPoints : "+dataPoints)
            var chart = new CanvasJS.Chart('chartpreview' + panel.chartId, {
                theme: "theme2", //theme1
                animationEnabled: false, // change to true
                data: [
                    {
                        type: chartType.type,
                        dataPoints: dataPoints
                    }
                ]
            });
            chart.render();
        });
    };

    //Default Load Chart
    $http.get('static/datas/defaultPanel.json').success(function (response) {
        $scope.panels = response;
        angular.forEach(response, function (value, key) {
//            $http.get(value.url).success(function (response) {
//                dataPoints = response;
//                var chart = new CanvasJS.Chart("canvasContainers" + value.chartId, {
//                    theme: "theme2", //theme1
//                    animationEnabled: false, // change to true
//                    data: [
//                        {
//                            type: value.chartType,
//                            dataPoints: response
//                        }
//                    ]
//                });
//                chart.render();
//            });
        });
    });


    var Employee = [];

    $scope.save = function (panel) {
        console.log("Print Chart Type " + panel.selectedChartType.type);
        //console.log("Type : "+panel.chart)
        //console.log("Type : "+panel.chartType.url +" "+panel.chartType.type +" "+panel.chartType.name)
        $http.get(panel.url).success(function (response) {
            Employee = response;
            dataPoints = Employee;
            console.log(panel.chartId);
            console.log(panel.url);
            console.log(panel.chartType);
            console.log(panel);
            var chart = new CanvasJS.Chart("canvasContainers" + panel.chartId, {
                theme: "theme2", //theme1
                animationEnabled: false, // change to true
                data: [
                    {
                        type: panel.selectedChartType.type,
                        dataPoints: dataPoints
                    }
                ]
            });
            chart.render();
        });
    };
});