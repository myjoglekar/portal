app.controller("ReportController", function ($scope, $http, $stateParams) {
    console.log($stateParams.startDate)
    console.log($stateParams.endDate)
    $scope.reportWidgets = [];
    $scope.selectDurations = [{duration: "None"},
        {duration: "Last Week"}, {duration: "Last Three Months"},
        {duration: "Last Six Months"}, {duration: "Last Six Months"}]; // Month Durations-Popup

    console.log($stateParams.reportId)

    $scope.uploadLogo = "static/img/logos/digital1.png";       //Logo Upload
    $scope.imageUpload = function (event) {
        var files = event.target.files;
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var reader = new FileReader();
            reader.onload = $scope.imageIsLoaded;
            reader.readAsDataURL(file);
        }
    };
    $scope.imageIsLoaded = function (e) {
        $scope.$apply(function () {
            $scope.uploadLogo = e.target.result;
        });
    };

    $scope.saveReportData = function (report) {
        var data = {
            reportTitle: report.title,
            description: report.description,
            logo: window.btoa($scope.uploadLogo)
        }

        $http({method: 'POST', url: 'admin/ui/report', data: data}).success(function (response) {
            //$scope.reports = response
        });
        console.log($scope.uploadLogo);
        console.log(report)
    };
    $http.get("admin/ui/report").success(function (response) {
        $scope.reports = response;
        angular.forEach($scope.report, function (value, key) {
            $scope.logo = window.atob(value.logo);
            console.log($scope.logo)
        })
    });




    var uid = 10;
    $scope.addReportWidget = function (newWidget) {                                     //Add new Report Widget
        $scope.id = uid++;
        var data = {
            width: newWidget, 'minHeight': 25, columns: []
        };
        $scope.reportWidgets.push({id: $scope.id, width: newWidget, 'minHeight': 25, columns: []});
    };
    $scope.deleteReportWidget = function (reportWidget, index) {                            //Delete Widget
        //$http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
        $scope.reportWidgets.splice(index, 1);
        //  $('.modal-backdrop').remove();
        //});
    };
    $scope.dynamicLoadingUrl = function (reportWidget) {                                //Dynamic Url from columns Type data - Popup
        if (reportWidget.columns) {
            reportWidget.columns = reportWidget.columns;
            if (reportWidget.directUrl) {
                $http.get(reportWidget.directUrl + "?fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    $scope.collectionFields = response.columnDefs;
                });
            }
        } else {
            if (reportWidget.directUrl) {
                $http.get(reportWidget.directUrl + "?fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    reportWidget.columns = response.columnDefs;
                    $scope.collectionFields = response.columnDefs;
                });
            }
        }
    };
    $scope.editReportWidget = function (reportWidget) {                                     //Edit widget
        console.log(reportWidget.productName);
        $scope.dynamicLoadingUrl(reportWidget);
        $scope.selectedRow = reportWidget.chartType;
        reportWidget.previewUrl = reportWidget.directUrl;
        reportWidget.previewType = reportWidget.chartType;
        reportWidget.previewTitle = reportWidget.widgetTitle;
        $scope.editChartType = reportWidget.chartType;
        $scope.selectProductName(reportWidget.productName, reportWidget);
    };
    $scope.selectProductName = function (productName, reportWidget) {
        if (productName === null) {
            return;
        }
        console.log(productName);
        $http.get("admin/user/datasets").success(function (response) {                      //User Based Products and Urls
            $scope.userProducts = [];
            angular.forEach(response, function (value, key) {
                $scope.userProducts.push(key);
            });
            $scope.productFields = response[productName];
        });
    };
    $scope.selectProductName();
    $http.get('static/datas/imageUrl.json').success(function (response) {                   //Popup- Select Chart-Type Json
        $scope.chartTypes = response;
    });
    $scope.reportPreviewChart = function (chartType, reportWidget, index) {                 //Selected Chart type - Bind chart-type to showPreview()
        $scope.selectedRow = chartType.type;
        reportWidget.widgetChartType = chartType.type;
        $scope.changeTableColumns = chartType.type;
        //$scope.setPreviewColumn = widgetReport;
    };
    $scope.changeUrl = function (url, reportWidget) {                                       //Search dynamic Url
        var data = JSON.parse(url);
        console.log(data);
        reportWidget.columns = [];
        $http.get(data.url + "?fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
            angular.forEach(response.columnDefs, function (value, key) {
                reportWidget.columns.push({fieldName: value.fieldName, displayName: value.displayName,
                    agregationFunction: value.agregationFunction, displayFormat: value.displayFormat,
                    groupPriority: value.groupPriority, sortOrder: value.sortOrder, sortPriority: value.sortPriority});
            })
            $scope.previewFields = response.columnDefs;
            angular.forEach(response, function (value, key) {
                angular.forEach(value, function (value, key) {
                    $scope.collectionFields.push(value);
                });
            });
        });
    };
    $scope.showPreview = function (reportWidget) {                                    //Show Preview Chart - Pop
        console.log(reportWidget.widgetChartType)
        console.log(reportWidget.columns)
        var data = JSON.parse(reportWidget.productDisplayName);
        $scope.previewChartType = reportWidget.widgetChartType ? reportWidget.widgetChartType : reportWidget.chartType;
        $scope.previewColumn = reportWidget;//$scope.setPreviewColumn ? $scope.setPreviewColumn : widgetReport;
        $scope.previewChartUrl = data.url;
        //$scope.showPreviewChart = !$scope.showPreviewChart;                     //Hide & Show Preview Chart
    };

    $scope.saveReport = function (reportWidget) {
        console.log(reportWidget.widgetChartType)
        var data = JSON.parse(reportWidget.productDisplayName);
        reportWidget.directUrl = data.url ? data.url : reportWidget.directUrl;
        reportWidget.chartType = $scope.previewChartType ? $scope.previewChartType : reportWidget.chartType;
        reportWidget.widgetTitle = reportWidget.previewTitle ? reportWidget.previewTitle : reportWidget.widgetTitle;
        var data = {
            id: reportWidget.id,
            chartType: $scope.setPreviewChartType ? $scope.setPreviewChartType : reportWidget.chartType,
            directUrl: data.url,
            widgetTitle: reportWidget.previewTitle,
            widgetColumns: reportWidget.columns,
            productName: reportWidget.productName,
            productDisplayName: reportWidget.productDisplayName
        };
//        $http({method: widgetReport.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
//        });
        console.log(reportWidget.directUrl, data.url)
        console.log(reportWidget.widgetTitle)
    };

    $scope.bindData = function (reportWidget) {
        console.log(reportWidget)
    }
});
