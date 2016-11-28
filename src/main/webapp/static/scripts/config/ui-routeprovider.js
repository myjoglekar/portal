
app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
//            .state("index", {
//                url: "/index",
//                templateUrl: "static/views/vb.index.html"
//            })
            .state("dashboard", {
                url: "/dashboard/:tabId",
                templateUrl: "static/views/dashboard/dashboard.html",
            })
//            .state("dashboard.tab", {
//                url: "/tab",
//                templateUrl: "static/views/dashboard/dashboardTabs.html", 
//                controller: 'UiController'
//            })
            .state("header", {
                url: "/header/:tabId",
                templateUrl: "static/views/dashboard/dashboardTabs.html", 
                controller: 'HeaderController'
            })
            .state("dashboard.widget", {
                url: "/widget/:widgetId",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
            .state("report", {
                url: "/report/:tabId/:reportId",
                templateUrl: "static/views/reports/createNewReports.html",
                controller: 'ReportController'
            });

    $urlRouterProvider.otherwise('/dashboard/dashboard/widget/:tabId');
})
//        .run(['$rootScope', '$state', '$stateParams',
//            function ($rootScope, $state, $stateParams) {
//                $rootScope.$state = $state;
//                $rootScope.$stateParams = $stateParams;
//            }])

