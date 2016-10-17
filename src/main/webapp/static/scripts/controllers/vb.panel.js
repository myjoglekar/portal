app.controller('PanelController', function ($scope, $http, $stateParams) {
    $http.get("static/datas/" + $stateParams.panelId + ".json").success(function (response) {
        $scope.panels = response;
    });
})