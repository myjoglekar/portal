app.controller("DashboardController", function ($scope, $http, $stateParams) {
    //Add Panels 
    $scope.panels = [];
    var uid = 7;
    $scope.add = function () {
        $scope.id = uid++;
        $scope.panels.push({chartId: $scope.id});
    };    

    //Default Load Tab   

//    $http.get("static/datas/" + $stateParams.tabId + ".json").success(function (response) {
//        $scope.panels = response;
//    });


    $scope.onDropComplete = function (index, panel, evt) {
        var otherObj = $scope.panels[index];
        var otherIndex = $scope.panels.indexOf(panel);
        $scope.panels[index] = panel;
        $scope.panels[otherIndex] = otherObj;
    };

    $http.get('static/datas/labels.json').success(function (response) {
        $scope.labels = response;
    });

    $http.get('static/datas/imageUrl.json').success(function (response) {
        $scope.chartTypes = response;
    });

    var dataPoints = [];
    var value = [];
    $scope.previewChart = function (chartType, panel) {
        panel.selectedChartType = chartType;
        console.log(panel.title + " " + panel.url + " " + chartType.type + " " + panel.chartId);
        value.title = panel.title;
        value.url = panel.url;
        value.type = chartType.type;
        value.chartId = panel.chartId;
        console.log("Values : " + value.title + " " + value.url + " " + value.type);
//        $http.get(panel.url).success(function (response) {
//            dataPoints = response;           

        if (value.type == "line") {
            lineChart(value);
        } else if (value.type == "area") {
            areaChart(value);
        } else if (value.type == "bar") {
            barChart(value);
        } else if (value.type == "groupedbar") {
            groupedBarChart(value);
        } else if (value.type == "pie") {
            pieChart(value);
        } else if (value.type == "donut") {
            donutChart(value);
        } else {
            alert("No Charts Available");
        }

        //console.log("dataPoints : "+dataPoints)
        //});
    };

    //Default Load Chart
//    $http.get('static/datas/defaultPanel.json').success(function (response) {
//        $scope.panels = response;
//        angular.forEach(response, function (value, key) {
////            if (value.chartType == "line") {
////                lineChart(value);
////            } else if (value.chartType == "area") {
//            // areaChart(value)
////            }
//        });
//    });

    $scope.charts = [];
    $http.get('static/datas/d3chart.json').success(function (response, error) {
        angular.forEach(response, function (value, key) {
            $scope.charts = value;
        });
    });
});