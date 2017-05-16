
app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html"
            })
            .state("index.dashboard", {
                url: "/dashboard/:dealerId/:productId/:tabId?:startDate/:endDate",
                templateUrl: "static/views/dashboard/dashboard.html",
                controller: 'UiController'
            })
//            .state("index.dashboard.widget", {
//                url: "/widget/:tabId?:startDate/:endDate",
//                templateUrl: "static/views/dashboard/widgets.html",
//                controller: 'WidgetController'
//            })
            .state("index.report", {
                url: "/reportIndex/:dealerId",
                templateUrl: "static/views/reports/reportIndex.html",
                controller: 'ReportIndexController'
            })
            .state("index.report.template", {
                url: "/template/:dealerId/:reportId?:startDate/:endDate",
                templateUrl: "static/views/reports/reportTemplate.html",
                controller: 'TemplateController',
                activetab: 'template'
            })
            .state("index.report.reports", {
                url: "/report?:startDate/:endDate",
                templateUrl: "static/views/reports/reports.html",
                controller: 'ReportController',
                activetab: 'report'
            })
            .state("index.report.newOrEdit", {
                url: "/newOrEdit/:dealerId/:reportId?:startDate/:endDate",
                templateUrl: "static/views/reports/newOrEditReports.html",
                controller: 'NewOrEditReportController',
            })
            .state("index.schedulerIndex", {
                url: "/schedulerIndex/:dealerId",
                templateUrl: "static/views/scheduler/schedulerIndex.html",
                controller: 'SchedulerIndexController'
            })
            .state("index.schedulerIndex.scheduler", {
                url: "/scheduler?:startDate/:endDate",
                templateUrl: "static/views/scheduler/scheduler.html",
                controller: 'SchedulerController'
            })
            .state("index.schedulerIndex.editOrNewScheduler", {
                url: "/editOrNewScheduler/:schedulerId?:startDate/:endDate",
                templateUrl: "static/views/scheduler/newOrEditScheduler.html",
                controller: 'NewOrEditSchedulerController'
            })
            .state("index.dataSource", {
                url: "/dataSource/:dealerId?:startDate/:endDate",
                templateUrl: "static/views/source/dataSource.html",
                controller: 'DataSourceController'
            })
            .state("index.dataSet", {
                url: "/dataSet/:dealerId?:startDate/:endDate",
                templateUrl: "static/views/source/dataSet.html",
                controller: 'DataSetController'
            })
            .state("index.map", {
                url: "mapReport",
                templateUrl: "static/views/admin/mapReport.html",
                controller: 'MapReportController'
            })
            .state("index.settings", {
                url: "settings",
                templateUrl: "static/views/fieldSettings/fieldSettings.html",
                controller: 'FieldSettingsController'
            });

    $urlRouterProvider.otherwise(function ($injector, $http) {
        $injector.get('$state').go('index.dashboard');
    });
//    $urlRouterProvider.otherwise('index/dashboard/1/2');
});
//
//Array.prototype.move = function (from, to) {
//    this.splice(to, 0, this.splice(from, 1)[0]);
//    return this;
//};
app.run(['$window', '$rootScope', '$stateParams',
    function ($window, $rootScope, $stateParams) {
        $rootScope.goBack = function () {
            $window.history.back();
        }

        $rootScope.printReport = function (dispButton, runUrl) {
            if (dispButton === true) {
                this.showButton = true;
            } else {
                this.showButton = false;
            }
            if (runUrl === true) {
                var url = "admin/proxy/downloadReport/" + $stateParams.reportId + "?dealerId=" + $stateParams.dealerId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate;
                $window.open(url);
            }
        }
    }]);