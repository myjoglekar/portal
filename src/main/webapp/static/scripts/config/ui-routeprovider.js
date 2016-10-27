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
                url: "/dashboard",
                templateUrl: "static/views/dashboard/dashboard.html", 
                controller: 'DashboardController'
            })
            .state("dashboard.tab", {
                url: "/tab/:tabId",
                templateUrl: "static/views/dashboard/dashboardTab.html",                
            })
            .state("dashboard.tab.panel", {
                url: "/panel/:panelId",
                templateUrl: "static/views/dashboard/panels.html",                
            })
            .state("report", {
                url: "/report/:reportId",
                templateUrl: "static/views/reports/createNewReports.html",
            });

    $urlRouterProvider.otherwise('/dashboard');
});

