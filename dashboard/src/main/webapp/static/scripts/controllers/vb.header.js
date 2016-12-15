app.controller('HeaderController', function ($scope, $cookies, $http, $filter, $stateParams, $state) {
    $scope.userName = $cookies.getObject("username");
    console.log($stateParams)
    $scope.selectName = "Select Product";
    $scope.change = function (product) {
        //$state.go('dashboard.widget', { tabId: product.id });
        $scope.selectName = product.productName;
        $scope.dashboardId = product.id;
       // $stateParams.tabId = product.id;
        //$scope.cookieProducId = $stateParams.tabId
    };
    
    var dates=$(".pull-right i").text();
//    alert(dates)
//    console.log("Header : ", $scope.cookieProducId)
//
//    console.log($scope.tabId)


    $http.get('admin/ui/product').success(function (response) {
        $scope.products = response;
        // $scope.searchProduct.unshift({"id": 0, "productName": "All Product"});
        //$scope.name = $filter('filter')($scope.dashboards, {id: $stateParams.tabId?$stateParams.tabId:3})[0];
        //$scope.selectName = $scope.name.productName;
    });



    $http.get('admin/dealer').success(function (response) {
        $scope.dealers = response;
    });
    
    
    $(function () {
        //Initialize Select2 Elements
        $(".select2").select2();

        //Datemask dd/mm/yyyy
        $("#datemask").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});
        //Datemask2 mm/dd/yyyy
        $("#datemask2").inputmask("mm/dd/yyyy", {"placeholder": "mm/dd/yyyy"});
        //Money Euro
        $("[data-mask]").inputmask();

        //Date range picker
        $('#reservation').daterangepicker();
        //Date range picker with time picker
        $('#reservationtime').daterangepicker({timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'});
        //Date range as a button
        $('#daterange-btn').daterangepicker(
                {
                    ranges: {
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                    },
                    startDate: moment().subtract(29, 'days'),
                    endDate: moment()
                },
                function (start, end) {
                    $('#daterange-btn span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
                }
        );

        //Date picker
        $('#datepicker').datepicker({
            autoclose: true
        });

        //iCheck for checkbox and radio inputs
        $('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        //Red color scheme for iCheck
        $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
            checkboxClass: 'icheckbox_minimal-red',
            radioClass: 'iradio_minimal-red'
        });
        //Flat red color scheme for iCheck
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });

        //Colorpicker
        $(".my-colorpicker1").colorpicker();
        //color picker with addon
        $(".my-colorpicker2").colorpicker();

        //Timepicker
        $(".timepicker").timepicker({
            showInputs: false
        });
    });

});