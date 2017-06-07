app.controller('HeaderController', function ($scope, $cookies, $http, $filter, $stateParams, $state, $location, $rootScope) {
    $scope.userName = $cookies.getObject("username");
    $scope.fullName = $cookies.getObject("fullname");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;
     $(document).ready(function () {
        $('.user-LogOut').popover({
            html: true,
            content: function () {
                return $('#userLogout').html();
            }
        });
    });

    $(document).on('click', function (e) {
        $('[data-toggle="popover"],[data-original-title]').each(function () {

            if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                $(this).popover('hide').data('bs.popover').inState.click = false
            }

        });
    });
});
