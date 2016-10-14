app.controller("DashboardController", function ($scope, $http) {
    //Add Panels 
    $scope.panels = [];
    var uid = 7;
    $scope.add = function () {alert("Test")
        $scope.id = uid++;
        $scope.panels.push({chartId: $scope.id});
        console.log("Uid : ", $scope.id);
    };

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

    $scope.previewChart = function (chartType, panel) {
        panel.selectedChartType = chartType;
        $http.get(panel.url).success(function (response) {
            dataPoints = response;
            //console.log("dataPoints : "+dataPoints)
        });
    };

    //Default Load Chart
    $http.get('static/datas/defaultPanel.json').success(function (response) {
        $scope.panels = response;
        angular.forEach(response, function (value, key) {
//            if (value.chartType == "line") {
//                lineChart(value);
//            } else if (value.chartType == "area") {
               // areaChart(value)
//            }
        });
    });

    $scope.charts = [];
    $http.get('static/datas/d3chart.json').success(function (response, error) {
        angular.forEach(response, function (value, key) {
            $scope.charts = value;
            console.log(value);
        });
    });
});