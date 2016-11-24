
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
            .state("dashboard.widget", {
                url: "/widget/:widgetId",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
            .state("report", {
                url: "/report/:reportId",
                templateUrl: "static/views/reports/createNewReports.html",
            });

    $urlRouterProvider.otherwise('/dashboard/dashboard/widget/1');
});
//        .run(['$rootScope', '$state', '$stateParams',
//            function ($rootScope, $state, $stateParams) {
//                $rootScope.$state = $state;
//                $rootScope.$stateParams = $stateParams;
//            }])

