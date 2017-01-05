
app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html"
            })
            .state("index.dashboard", {
                url: "/dashboard/:productId",
                templateUrl: "static/views/dashboard/dashboard.html",
            })
//            .state("dashboard.tab", {
//                url: "/tab",
//                templateUrl: "static/views/dashboard/dashboardTabs.html", 
//                controller: 'UiController'
//            })
//            .state("header", {
//                url: "/header/:productId",
//                templateUrl: "static/views/dashboard/dashboardTabs.html",
//                controller: 'HeaderController'
//            })
            .state("index.dashboard.widget", {
                url: "/widget/:productId/:tabId?:startDate/:endDate",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
            .state("index.report", {
                url: "/reportIndex",
                templateUrl: "static/views/reports/reportIndex.html",
                controller: 'ReportIndexController'
            })
            .state("index.report.template", {
                url: "/template/:reportId?:startDate/:endDate",
                templateUrl: "static/views/reports/reportTemplate.html",
                controller: 'TemplateController',
                activetab: 'template'
            })
            .state("index.report.reports", {
                url: "/report/:reportId?:startDate/:endDate",
                templateUrl: "static/views/reports/reports.html",
                controller: 'ReportController',
                activetab: 'report'
            })
            .state("index.report.newOrEdit", {
                url: "/newOrEdit/:reportId?:startDate/:endDate",
                templateUrl: "static/views/reports/newOrEditReports.html",
                controller: 'NewOrEditReportController',
                activetab: 'report'
            });

    $urlRouterProvider.otherwise('index/dashboard/dashboard/widget/2/2?29/23');
});
