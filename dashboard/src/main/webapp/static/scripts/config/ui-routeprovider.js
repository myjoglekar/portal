
app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html"
            })
            .state("index.dashboard", {
                url: "/dashboard/:dealerId/:productId",
                templateUrl: "static/views/dashboard/dashboard.html",
            })
            .state("index.dashboard.widget", {
                url: "/widget/:tabId?:startDate/:endDate",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
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
                activetab: 'report'
            })
            .state("index.dataSource", {
                url: "/dataSource/:dealerId?:startDate/:endDate",
                templateUrl: "static/views/source/dataSource.html",
                controller: 'DataSourceController'
            }).state("index.dataSet", {
                url: "/dataSet/:dealerId?:startDate/:endDate",
                templateUrl: "static/views/source/dataSet.html",
                controller: 'DataSetController'
            });

    $urlRouterProvider.otherwise('index/dashboard/1/2');
});
//
//Array.prototype.move = function (from, to) {
//    this.splice(to, 0, this.splice(from, 1)[0]);
//    return this;
//};
