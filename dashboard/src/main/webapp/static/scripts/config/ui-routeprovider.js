
app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html"
            })
            .state("index.dashboard", {
                url: "/dashboard/:dashboardId",
                templateUrl: "static/views/dashboard/dashboard.html",
            })
//            .state("dashboard.tab", {
//                url: "/tab",
//                templateUrl: "static/views/dashboard/dashboardTabs.html", 
//                controller: 'UiController'
//            })
            .state("header", {
                url: "/header/:dashboardId",
                templateUrl: "static/views/dashboard/dashboardTabs.html",
                controller: 'HeaderController'
            })
            .state("index.dashboard.widget", {
                url: "/widget/:tabId",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
            .state("report", {
                url: "/report/:dashboardId/:reportId",
                templateUrl: "static/views/reports/createNewReports.html",
                controller: 'ReportController'
            });

    $urlRouterProvider.otherwise('index/dashboard/dashboard/widget/2');
})
