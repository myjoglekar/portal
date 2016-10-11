/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
            .state("dashboard", {
                url: "/dashboard",
                templateUrl: "static/views/vb.index.html"
            });

    $urlRouterProvider.otherwise('/dashboard');
});

