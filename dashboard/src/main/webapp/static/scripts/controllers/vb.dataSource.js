app.controller("DataSourceController", function ($scope, $stateParams, $http) {
    console.log($stateParams.dealerId);

    $http.get('admin/ui/dataSource').success(function (response) {
        $scope.dataSources = response;
    });

    $scope.addDataSource = function () {
        $scope.dataSources.unshift({isEdit: true});
    };

    $scope.saveDataSource = function (dataSource) {
        var data = {
            id: dataSource.id,
            connectionString: dataSource.connectionString,
            name: dataSource.name,
            password: dataSource.password,
            userName: dataSource.userName
        };
        $http({method: dataSource.id ? 'PUT' : 'POST', url: 'admin/ui/dataSource', data: data}).success(function (response) {

        });
    };

    $scope.deleteDataSource = function (dataSource, index) {
        $http({method: 'DELETE', url: 'admin/ui/dataSource/' + dataSource.id}).success(function () {
            $scope.dataSources.splice(index, 1);
        })
    };
});