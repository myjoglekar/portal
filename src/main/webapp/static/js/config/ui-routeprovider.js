/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html"
            })
            .state("index.dashboard", {
                url: "/dashboard",
                templateUrl: "static/views/dashboard/dashboard.html"
            })
            .state("index.report", {
                url: "/report",
                templateUrl: "static/views/reports/createNewReports.html",
            });

    $urlRouterProvider.otherwise('/index');
});

