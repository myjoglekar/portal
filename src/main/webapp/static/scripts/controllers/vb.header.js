app.controller('HeaderController', function($scope, $http, $stateParams){
    $http.get('static/datas/dashboard.json').success(function(response){
        $scope.dashboards = response;
    })
    
    $stateParams.type = $scope.tabType;
    console.log("Header : "+$stateParams.type)
})