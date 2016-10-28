app.controller("DashboardController", function ($scope, $http, $stateParams) {
    //chart()
    
    $http.get('static/datas/labels.json').success(function (response) {
        $scope.labels = response;
    });

    $http.get('static/datas/imageUrl.json').success(function (response) {
        $scope.chartTypes = response;
    });    
    var value = [];
    $scope.previewChart = function (chartType, panel) {
        panel.selectedChartType = chartType;
        console.log(panel.title + " " + panel.url + " " + chartType.type + " " + panel.chartId);
        value.dispChart = "previewChart"
        value.title = panel.title;
        value.url = panel.url;
        value.type = chartType.type;
        value.chartId = panel.chartId;
        console.log("Values : " + value.title + " " + value.url + " " + value.type);
       
        if (value.type === "line") {
            lineChart(value);
        } else if (value.type === "area") {
            areaChart(value);
        } else if (value.type === "bar") {
            barChart(value);
        } else if (value.type === "groupedbar") {
            groupedBarChart(value);
        } else if (value.type === "pie") {
            pieChart(value);
        } else if (value.type === "donut") {
            donutChart(value);
        } else if (value.type === "table") {
            table(value);
        }else {
            alert("No Charts Available");
        }

    };
    
    $scope.save = function (panel) {
        console.log(panel.selectedChartType)
        $scope.chartType = panel.selectedChartType;
        //console.log("save:"+panel.title + " " + panel.url + " " + chartType.type + " " + panel.chartId);
        value.dispChart = "widget"
        value.title = panel.title;
        value.url = panel.url;
        value.type = $scope.chartType.type;
        value.chartId = panel.chartId;
        console.log("Values : " + value.title + " " + value.url + " " + value.type);
       
        if (value.type === "line") {
            lineChart(value);
        } else if (value.type === "area") {
            areaChart(value);
        } else if (value.type === "bar") {
            barChart(value);
        } else if (value.type === "groupedbar") {
            groupedBarChart(value);
        } else if (value.type === "pie") {
            pieChart(value);
        } else if (value.type === "donut") {
            donutChart(value);
        } else if (value.type === "table") {
            table(value);
        }else {
            alert("No Charts Available");
        }

    };

    

    $scope.charts = [];
    $http.get('static/datas/d3chart.json').success(function (response, error) {
        angular.forEach(response, function (value, key) {
            $scope.charts = value;
        });
    });
});