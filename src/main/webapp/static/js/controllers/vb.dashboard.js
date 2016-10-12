app.controller("DashboardController", function ($scope, $http) {
    //Add Panels
    $scope.panels = [];
    var uid = 7;
    $scope.add = function () {
        $scope.id = uid++;
        $scope.panels.push({chartId: $scope.id});
        console.log("Uid : ", $scope.id);
    };
    
    $http.get('static/datas/labels.json').success(function(response){
        $scope.labels = response;
    })

    $scope.onDropComplete = function (index, panel, evt) {
        var otherObj = $scope.panels[index];
        var otherIndex = $scope.panels.indexOf(panel);
        $scope.panels[index] = panel;
        $scope.panels[otherIndex] = otherObj;
    };

    $http.get('static/data/imageUrl.json').success(function (response) {
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
            $http.get(value.url).success(function (response) {
                dataPoints = response;
                var chart = new CanvasJS.Chart("canvasContainers" + value.chartId, {
                    theme: "theme2", //theme1
                    animationEnabled: false, // change to true
                    data: [
                        {
                            type: value.chartType,
                            dataPoints: response
                        }
                    ]
                });
                chart.render();
            });
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