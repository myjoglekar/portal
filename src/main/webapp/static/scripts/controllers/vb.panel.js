app.controller('PanelController', function ($scope, $http, $stateParams) {
    $http.get("static/datas/" + $stateParams.panelId + ".json").success(function (response) {
        $scope.panels = response;
    });

    $scope.panels = [];
    var uid = 7;
    $scope.add = function () {
        $scope.id = uid++;
        $scope.panels.push({chartId: $scope.id});
    };
});