
app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html"
            })
            .state("index.dashboard", {
                url: "/dashboard/:dashboardTypeId/:productId",
                templateUrl: "static/views/dashboard/dashboard.html",
            })
//            .state("dashboard.tab", {
//                url: "/tab",
//                templateUrl: "static/views/dashboard/dashboardTabs.html", 
//                controller: 'UiController'
//            })
            .state("header", {
                url: "/header/:productId",
                templateUrl: "static/views/dashboard/dashboardTabs.html",
                controller: 'HeaderController'
            })
            .state("index.dashboard.widget", {
                url: "/widget/:productId/:tabId",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
            .state("index.report", {
                url: "/reportIndex/:dashboardTypeId/:productId",
                templateUrl: "static/views/reports/reportIndex.html",
                controller: 'ReportIndexController'
            })
            .state("index.report.template", {
                url: "/template/:productId",
                templateUrl: "static/views/reports/reportTemplate.html",
                controller: 'TemplateController',
                activetab: 'template'
            })
            .state("index.report.reports", {
                url: "/report/:productId",
                templateUrl: "static/views/reports/reports.html",
                controller: 'ReportController',
                activetab: 'report'
            })
            .state("index.report.newOrEdit", {
                url: "/newOrEdit/:productId/:reportId",
                templateUrl: "static/views/reports/newOrEditReports.html",
                controller: 'ReportController',
                activetab: 'report'
            });

    $urlRouterProvider.otherwise('index/dashboard/1/dashboard/widget/2/2');
});
