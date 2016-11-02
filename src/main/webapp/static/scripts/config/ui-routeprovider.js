/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
            .state("dashboard.tab", {
                url: "/tab",
                templateUrl: "static/views/dashboard/dashboardTabs.html", 
                controller: 'TabController'
            })
            .state("dashboard.tab.widget", {
                url: "/widget/:widgetId",
                templateUrl: "static/views/dashboard/widgets.html",   
                controller:'WidgetController'
            })
            .state("report", {
                url: "/report/:reportId",
                templateUrl: "static/views/reports/createNewReports.html",
            });

    $urlRouterProvider.otherwise('/dashboard/dashboard');
});

