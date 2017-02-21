app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout, $filter, localStorageService, $sce, $window) {
    $scope.permission = localStorageService.get("permission");
    $scope.addToPdf = function (data) {
        console.log("Adding to pdf");
        console.log(data);
    }
    $scope.selectAggregations = [
        {name: 'None', value: ""},
        {name: 'Sum', value: "sum"},
        {name: 'CTR', value: "ctr"},
        {name: 'CPC', value: "cpc"},
        {name: 'CPS', value: "cps"},
        {name: 'CPA', value: "cpa"},
        {name: 'Avg', value: "avg"},
        {name: 'Count', value: "count"},
        {name: 'Min', value: "min"},
        {name: 'Max', value: "max"},
        {name: 'CPL', value: "cpl"},
        {name: 'CPLC', value: "cplc"},
        {name: 'CPComment', value: "cpcomment"},
        {name: 'CPostE', value: "cposte"},
        {name: 'CPageE', value: "cpagee"},
        {name: 'CPP', value: "cpp"},
        {name: 'CPR', value: "cpr"}

    ];   //Aggregation Type-Popup
    $scope.selectGroupPriorities = [
        {num: 'None', value: ""},
        {num: 1, value: 1},
        {num: 2, value: 2}
    ];
    $scope.selectDateDurations = [
        {duration: "None", value: 'none'},
        {duration: "Today", value: 'today'},
        {duration: "Last N days", value: ''},
        {duration: "Last N Weeks", value: ''},
        {duration: "Last N Months", value: ''},
        {duration: "This Month", value: 'thisMonth'},
        {duration: "This Year", value: 'thisYear'},
        {duration: "Last Year", value: 'lastYear'},
        {duration: "Yesterday", value: 'yesterday'},
        {duration: "Custom", value: 'custom'}
    ]; // Month Durations-Popup
    $scope.selectXAxis = [
        {label: 'None', value: ""},
        {label: "X-1", value: 1}
    ];
    $scope.selectYAxis = [
        {label: 'None', value: ""},
        {label: "Y-1", value: 1},
        {label: "Y-2", value: 2}
    ];
    $scope.alignments = [
        {name: '', displayName: 'None'},
        {name: "left", displayName: "Left"},
        {name: "right", displayName: "Right"},
        {name: "center", displayName: "Center"}
    ];
    $scope.sorting = [
        {name: 'None', value: ''},
        {name: 'asc', value: 'asc'},
        {name: 'desc', value: 'desc'}
    ];
    $scope.tableWrapText = [
        {name: 'None', value: ''},
        {name: 'Yes', value: "yes"}
    ];
    $scope.hideOptions = [
        {name: 'Yes', value: 1},
        {name: 'No', value: ''}
    ];
    $scope.isEditPreviewColumn = false;

    $scope.downloadPdf = function () { 
        var url = "admin/proxy/download/" + $stateParams.tabId + "?dealerId=" + $stateParams.dealerId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate;
        $window.open(url);
    };


    $('.dropdown-menu input').click(function (e) {
        e.stopPropagation();
    });

//    $('.dropdown-menu li').click(function () {
//
//        $('.dropdown-toggle b').remove().appendTo($('.dropdown-toggle').text($(this).text()));
//    });

    $scope.widgets = [];
    function getWidgetItem() {      //Default Loading Items
        if (!$stateParams.tabId) {
            $stateParams.tabId = 1;
        }
        $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
            $scope.widgets = response;
        });
    }
    getWidgetItem();
    $scope.collectionField = {};
    $scope.dispName = function (currentColumn) {
        $scope.filterName = $filter('filter')($scope.collectionFields, {fieldName: currentColumn.fieldName})[0];
        currentColumn.displayName = $scope.filterName.displayName;
    };

    $scope.editWidget = function (widget) {     //Edit widget
        $scope.tableDef(widget);
        $scope.selectedRow = widget.chartType;
        widget.previewUrl = widget.directUrl;
        widget.previewType = widget.chartType;
        widget.previewTitle = widget.widgetTitle;
        $scope.editChartType = widget.chartType;
        $scope.selectProductName(widget.productName, widget);
    };

    $scope.tableDef = function (widget) {      //Dynamic Url from columns Type data - Popup
        if (widget.columns) {
            widget.columns = widget.columns;
            if (widget.directUrl) {
                $http.get("admin/proxy/getJson?url=" + widget.directUrl + "&fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    $scope.collectionFields = response.columnDefs;
                });
            }
        } else {
            if (widget.directUrl) {
                $http.get("admin/proxy/getJson?url=" + widget.directUrl + "&fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    widget.columns = response.columnDefs;
                    $scope.collectionFields = response.columnDefs;
                });
            }
        }
    };

    $scope.selectProductName = function (productName, widget) {
        console.log(productName)
        if (productName === null) {
            return;
        }
        $http.get("admin/user/datasets").success(function (response) {                //User Based Products and Urls
            $scope.userProducts = [];
            $http.get('admin/ui/product/' + $stateParams.dealerId).success(function (response) {
                $scope.products = response;
            });
            console.log("Product")
            $scope.name = $filter('filter')($scope.products, {id: $stateParams.productId})[0];
            $scope.selectName = $scope.name.productName;
            angular.forEach(response, function (value, key) {
                if ($scope.selectName == key) {
                    $scope.userProducts.push(key);
                }
            });
            $scope.productFields = response[productName];
        });

    }
    $scope.selectProductName();

    $scope.changeUrl = function (displayName, widget) {
        angular.forEach($scope.productFields, function (value, key) {
            if (value.productDisplayName == displayName) {
                $scope.requiredUrl = value.url;
            }
        })
        //var searchUrl = $filter('filter')($scope.productFields, {productDisplayName: displayName})[0];
        widget.previewUrl = $scope.requiredUrl;
        widget.columns = [];
        $http.get("admin/proxy/getJson?url=" + $scope.requiredUrl + "&fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
            angular.forEach(response.columnDefs, function (value, key) {
                widget.columns.push({fieldName: value.fieldName, displayName: value.displayName,
                    agregationFunction: value.agregationFunction, displayFormat: value.displayFormat, fieldType: value.type,
                    groupPriority: value.groupPriority, sortOrder: value.sortOrder, sortPriority: value.sortPriority});
            })
            $scope.previewFields = response.columnDefs;
            angular.forEach(response, function (value, key) {
                angular.forEach(value, function (value, key) {
                    $scope.collectionFields.push(value);
                });
            });
        });
    };

    $scope.pageRefresh = function () {          //Page Refresh
        getWidgetItem();
    };
    $http.get("static/datas/panelSize.json").success(function (response) {      //Default Panel in Ui
        $scope.newWidgets = response;
    });

    $scope.openPopup = function (response) {
        $timeout(function () {
            $scope.editWidget(response);
            $('#preview' + response.id).modal('show');
        }, 100);
    };
    $scope.addWidget = function (newWidget) {       //Add Widget
        var data = {
            width: newWidget, 'minHeight': 25, columns: [], chartType: ""
        };
        $http({method: 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            $scope.widgets.unshift({id: response.id, width: newWidget, 'minHeight': 25, columns: [], tableFooter: 1, zeroSuppression: 1});
            $scope.newWidgetId = response.id;
            $scope.openPopup(response);
        });
    };


    $scope.deleteWidget = function (widget, index) {                            //Delete Widget
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
            $scope.widgets.splice(index, 1);
        });
    };

    $scope.addColumns = function (widget) {                                     //Add Columns - Popup
        widget.columns.unshift({isEdit: true});
    };

    $scope.saveColumn = function (widget, column) {                              //Delete Columns-Popup
        var data = {
            id: column.id,
            agregationFunction: column.agregationFunction,
            displayName: column.displayName,
            fieldName: column.fieldName,
            groupPriority: column.groupPriority,
            xAxis: column.xAxis,
            yAxis: column.yAxis,
            sortOrder: column.sortOrder,
            displayFormat: column.displayFormat
        };
    };

    $scope.deleteColumn = function (widgetColumns, index) {        //Delete Columns - Popup
        widgetColumns.splice(index, 1);
    };

    $http.get('static/datas/imageUrl.json').success(function (response) {       //Popup- Select Chart-Type Json
        $scope.chartTypes = response;
    });

    //DataSource
    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });

    //Data Source
    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });
    $scope.selectedDataSource = function (selectedItem) {
        $scope.selectItem = selectedItem;
        selectedItems(selectedItem);
    };

    $scope.objectHeader = [];
    $scope.previewChart = function (chartType, widget, index) {                 //Selected Chart type - Bind chart-type to showPreview()
        $scope.selectedRow = chartType.type;
        widget.widgetChartType = chartType.type;
        $scope.editChartType = chartType.type;
        $scope.setPreviewChartType = chartType.type;
        $scope.setPreviewColumn = widget;
    };

    $scope.showPreview = function (widget) {                                    //Show Preview Chart - Popup
        $scope.previewChartType = $scope.editChartType ? $scope.editChartType : widget.chartType;
        $scope.previewColumn = $scope.setPreviewColumn ? $scope.setPreviewColumn : widget;
        $scope.previewChartUrl = widget.previewUrl;
    };

    $scope.dateDuration = function (widget, selectDateDuration) {
        widget.dateDuration = selectDateDuration.duration;
    };

    $scope.save = function (widget) {
        widget.directUrl = widget.previewUrl ? widget.previewUrl : widget.directUrl;
        var widgetColumnsData = [];
        angular.forEach(widget.columns, function (value, key) {
            var hideColumn = value.columnHide;
            if (value.groupPriority > 0) {
                hideColumn = 1;
            }

            var columnData = {
                id: value.id,
                fieldName: value.fieldName,
                displayName: value.displayName,
                agregationFunction: value.agregationFunction,
                groupPriority: isNaN(value.groupPriority) ? null : value.groupPriority,
                xAxis: isNaN(value.xAxis) ? null : value.xAxis,
                yAxis: isNaN(value.yAxis) ? null : value.yAxis,
                sortOrder: value.sortOrder,
                displayFormat: value.displayFormat,
                alignment: value.alignment,
                baseFieldName: value.baseFieldName,
                fieldGenerationFields: value.fieldGenerationFields,
                fieldGenerationFunction: value.fieldGenerationFunction,
                fieldType: value.fieldType,
                functionParameters: value.functionParameters,
                remarks: value.remarks,
                sortPriority: isNaN(value.sortPriority) ? null : value.sortPriority,
                width: isNaN(value.width) ? null : value.width,
                wrapText: value.wrapText,
                xAxisLabel: value.xAxisLabel,
                yAxisLabel: value.yAxisLabel,
                columnHide: hideColumn
            };
            widgetColumnsData.push(columnData);
        });
//        if (widget.dateDuration) {
//            widget.customRange = "";
//        }

        if (widget.dateDuration == 'None' || widget.dateDuration == "This Month" || widget.dateDuration == "This Year" || widget.dateDuration == "Last Year" || widget.dateDuration == "Yesterday" || widget.dateDuration == "Custom") {
            widget.frequencyDuration = "";
        }

        var data = {
            id: widget.id,
            chartType: $scope.editChartType ? $scope.editChartType : widget.chartType,
            directUrl: widget.previewUrl,
            widgetTitle: widget.previewTitle,
            widgetColumns: widgetColumnsData,
            productName: widget.productName,
            productDisplayName: widget.productDisplayName,
            tableFooter: widget.tableFooter,
            zeroSuppression: widget.zeroSuppression,
            maxRecord: widget.maxRecord,
            dateDuration: widget.dateDuration,
            frequencyDuration: widget.frequencyDuration,
            customRange: widget.customRange
        };
        widget.chartType = "";
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            widget.columns = response.columns; // widget.columns;
            widget.chartType = data.chartType;
        });
        widget.widgetTitle = widget.previewTitle ? widget.previewTitle : widget.widgetTitle;
//        widget.dateDuration = widget.dateDuration;
//        widget.frequencyDuration = widget.frequencyDuration;
//        widget.customRange = widget.customRange;
        widget.widgetColumns = widgetColumnsData; // widget.columns;
    };

    $scope.closeWidget = function (widget) {
        $scope.widget = "";
    };

//    Array.prototype.move = function (from, to) {
//        this.splice(to, 0, this.splice(from, 1)[0]);
//        return this;
//    };

    $scope.moveWidget = function (list, from, to) {
        list.splice(to, 0, list.splice(from, 1)[0]);
        return list;
    };

    $scope.onDropComplete = function (index, widget, evt) {

        if (widget !== "" && widget !== null) {
            var otherObj = $scope.widgets[index];
            var otherIndex = $scope.widgets.indexOf(widget);
//            $scope.widgets.move(otherIndex, index);
            $scope.widgets = $scope.moveWidget($scope.widgets, otherIndex, index);
//            $scope.widgets[index] = widget;
//            $scope.widgets[otherIndex] = otherObj;
            var widgetOrder = $scope.widgets.map(function (value, key) {
                if (value) {
                    return value.id;
                }
            }).join(',');
            var data = {widgetOrder: widgetOrder};
            if (widgetOrder) {
                $http({method: 'GET', url: 'admin/ui/dbWidgetUpdateOrder/' + $stateParams.tabId + "?widgetOrder=" + widgetOrder});
            }
        }
    };

    function splitCamelCase(s) {
        return s.split(/(?=[A-Z])/).join(' ');
    }

    function makeUnselectable(node) {
        if (node.nodeType == 1) {
            node.setAttribute("unselectable", "on");
        }
        var child = node.firstChild;
        while (child) {
            makeUnselectable(child);
            child = child.nextSibling;
        }
    }
});

app.directive('dateRangePicker', function () {
    return{
        restrict: 'A',
//        template: '<input type="text" name="daterange" value="01/01/2015 1:30 PM - 01/01/2015 2:00 PM" />',
//        template: '<div id="reportrange" class="pull-right" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc; width: 100%">' +
//                '<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;' +
//                '<span></span> <b class="caret"></b>' + '</div>',
        link: function (scope, element, attr) {
            
//             $(element[0]).daterangepicker(
//                {
//                    ranges: {
////                        'Today': [moment(), moment()],
////                        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
////                        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
////                        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
//                        'This Month': [moment().startOf('month'), moment().endOf(new Date())],
//                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
//                    },
//                    startDate:moment().subtract(29, 'days'),
//                    endDate:moment(),
//                    maxDate: new Date()
//                },
//                function (start, end) {
//                    $('.widget-datePicker').html(start.format('MM-DD-YYYY') + ' - ' + end.format('MM-DD-YYYY'));
//                }
//        );
            
            
//            
            $(function () {
                $('input[name="daterange"]').daterangepicker({
                    // timePicker: true,
                    //timePickerIncrement: 30,
                    locale: {
                        format: 'MM/DD/YYYY'
                    }
                });
            });
        }
    };
});

app.directive('dynamicTable', function ($http, $filter, $stateParams, $sce) {
    return{
        restrict: 'A',
        scope: {
            whattodo: "=",
            dynamicTableUrl: '@',
            widgetId: '@',
            widgetColumns: '@',
            tableFooter: '@',
            widgetObj: '@',
            widgetDateDuration: '@',
            widgetFrequencyDuration: '@',
            widgetCustomRange: '@',
        },
        template: '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<table ng-if="ajaxLoadingCompleted" class="table table-responsive table-bordered table-l2t" ng-hide="hideEmptyTable">' +
                '<thead><tr>' +
                '<th class="info table-bg" ng-if="groupingName" class="text-center">' +
                '<i style="cursor: pointer" ng-click="groupingData.$hideRows = !groupingData.$hideRows; hideAll(groupingData, groupingData.$hideRows, true); selected_Row = !selected_Row" class="fa" ng-class="{\'fa-plus-circle\': !selected_Row, \'fa-minus-circle\': selected_Row}"></i>' +
                ' Group</th>' +
                '<th class="text-capitalize info table-bg" ng-repeat="col in columns" ng-if="col.columnHide == null">' +
                '<div ng-click="initData(col)" class="text-{{col.alignment}}">{{col.displayName}}<i ng-if="col.sortOrder==\'asc\'" class="fa fa-sort-asc"></i><i ng-if="col.sortOrder==\'desc\'" class="fa fa-sort-desc"></i></div>' +
                '</th>' +
                '</tr></thead>' +
                //'<tbody dir-paginate="grouping in groupingData | orderBy: sortColumn:reverse | itemsPerPage: pageSize" current-page="currentPage"">' +
                '<tbody ng-repeat="grouping in groupingData.data">' +
                '<tr ng-if="!isZeroRow(grouping, columns)" class="text-capitalize text-info info">' +
                '<td class="group-bg" ng-if="groupingName">' +
                '<i style="cursor: pointer" class="fa" ng-click="grouping.$hideRows = !grouping.$hideRows; hideParent(grouping, grouping.$hideRows); hideChild(grouping.data, false)" ng-class="{\'fa-plus-circle\': !grouping.$hideRows, \'fa-minus-circle\': grouping.$hideRows}"></i>' +
                //                ' {{grouping._groupField}} : {{grouping._key}}' +
                ' <span ng-bind-html="grouping._key"></span>' +
                '</td>' +
                '<td ng-repeat="col in columns" style="width: {{col.width}}%" ng-if="col.columnHide == null">' +
                //                    '<div><span style="float: {{col.alignment}}">{{format(col, grouping[col.fieldName])}}</span></div>' +
                '<div class="text-{{col.alignment}}"><span ng-bind-html="format(col, grouping[col.fieldName])"></span></div>' +
                '</td>' +
                '</tr>' +
                '<tr ng-if="!isZeroRow(item, columns)" ng-show="grouping.$hideRows" ng-repeat-start="item in grouping.data" class="text-capitalize text-info info">' +
                '<td class="right-group">' +
                '<i ng-if="item._groupField && item.data.length != 1" style="cursor: pointer" class="fa" ng-click="item.$hideRows = !item.$hideRows; hideChild(item, item.$hideRows)" ng-class="{\'fa-plus-circle\': !item.$hideRows, \'fa-minus-circle\': item.$hideRows}"></i>' +
                //                ' {{item._groupField}} : {{item._key}}</td>' +
                ' <span ng-bind-html="item._key"></span></td>' +
                '<td style="background-color: #d7dedc" ng-repeat="col in columns" ng-if="col.columnHide == null">' +
                '<div class="text-{{col.alignment}}"><span ng-bind-html="format(col, item[col.fieldName])"></span></div>' + //ng-bind-html-unsafe=todo.text
                '</td>' +
                '</tr>' +
                '<tr ng-show="item.$hideRows" ng-if="!isZeroRow(childItem, columns) && item.data.length != 1" ng-repeat="childItem in item.data" ng-repeat-end><td></td>' +
                '<td ng-repeat="col in columns" style="width: {{col.width}}%" ng-if="col.columnHide == null">' +
                '<div class="text-{{col.alignment}}"><span ng-bind-html="format(col, childItem[col.fieldName])"></span></div>' +
                '</td>' +
                '</tr>' +
                '</tbody>' +
                '<tfoot ng-if="displayFooter == \'true\'">' +
                '<tr> {{initTotalPrint()}}' +
                '<td ng-if="groupingName">{{ showTotal() }}</td>' +
                //'<td ng-repeat="col in columns" class="col.alignment[groupingData]">{{format(col, groupingData[col.fieldName])}}</td>' +
                '<td ng-repeat="col in columns" ng-if="col.columnHide == null">' +
                '<div ng-if="totalShown == 1" class="text-{{col.alignment}}"><span ng-bind-html="format(col, groupingData[col.fieldName])"></span></div>' +
                '<div ng-if="totalShown != 1" class="text-{{col.alignment}}">{{ showTotal() }}</div>' +
                '</td>' +
                '</tr>' +
                '</tfoot>' +
                '</table>' + '<div class="text-center" ng-show="hideEmptyTable">{{tableEmptyMessage}}</div>', //+
        //'<dir-pagination-controls boundary-links="true" on-page-change="pageChangeHandler(newPageNumber)" template-url="static/views/reports/pagination.tpl.html"></dir-pagination-controls>',
        link: function (scope, element, attr) {
            scope.html = '<p class="text-color">Your html code</p>';
//            scope.pdfFunction({test:"Test"});
            scope.totalShown = 0;
            scope.displayFooter = scope.tableFooter;
            scope.loadingTable = true;
            scope.clickRow = function () {
                scope.grouping.$hideRows = false;
            };
            scope.showTotal = function () {
                scope.totalShown = 1;
                return "Total :"
            }
            scope.initTotalPrint = function () {
                scope.totalShown = 0;
                return "";
            }
            scope.hideParent = function (grouping, hideStatus) {
                if (!grouping)
                    return;
                angular.forEach(grouping.data, function (value, key) {
                    if (hideStatus == false) {
                        value.$hideRows = hideStatus;
                        scope.hideParent(value, hideStatus);
                        scope.hideParent(value.data, false)
                    }
                    // scope.hideChild(value.data, false)
                    //value.data.$hideRows = true;
                });
            };

            scope.hideChild = function (item, hideStatus) {
                // console.log(item);
                if (!item)
                    return;
                angular.forEach(item, function (value, key) {
                    value.$hideRows = hideStatus;

                    if (hideStatus == false) {
                        scope.hideChild(value, hideStatus);
                    }
                    //value.data.$hideRows = true;
                });
            };

            scope.hideAll = function (grouping, hideStatus) {
                if (!grouping)
                    return;
                angular.forEach(grouping.data, function (value, key) {
                    value.$hideRows = hideStatus;
//                    if (value.data)
//                        return;
                    if (hideStatus == false) {
                        scope.hideAll(value, hideStatus);
                        scope.hideAll(value.data, false)
                    }
                });
            };
            scope.doSomething = function (ev) {
                var element = ev.srcElement ? ev.srcElement : ev.target;
            };

            //scope.currentPage = 1;
            //scope.pageSize = 10;
            // console.log
            scope.columns = [];
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                scope.columns.push(value);
            });

            scope.isZeroRow = function (row, col) {
                var widgetData = JSON.parse(scope.widgetObj);
                if (widgetData.zeroSuppression == false) {
                    return false;
                }
                var zeroRow = true;
                angular.forEach(col, function (value, key) {
                    var fieldName = value.fieldName;
                    var fieldValue = Number(row[fieldName]);
                    if (!isNaN(fieldValue) && fieldValue != 0) {
                        zeroRow = false;
                        return zeroRow;
                    }
                });
                return zeroRow;
            }

            scope.format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
                    if (Number.isNaN(value)) {
                        return "-";
                    }
                    if (column.displayFormat.indexOf("%") > -1) {
                        // return d3.format(column.displayFormat)(value / 100);
                    }
                    return d3.format(column.displayFormat)(value);
                }
                return value;
            };

            var groupByFields = []; // ['device', 'campaignName'];
            var aggreagtionList = [];
            var sortFields = [];
            for (var i = 0; i < scope.columns.length; i++) {
                if (scope.columns[i].groupPriority) {
                    groupByFields.push(scope.columns[i].fieldName);
                }
                if (scope.columns[i].agregationFunction) {
                    aggreagtionList.push({fieldname: scope.columns[i].fieldName, aggregationType: scope.columns[i].agregationFunction});
                }
                if (scope.columns[i].sortOrder) {
                    sortFields.push({fieldName: scope.columns[i].fieldName, sortOrder: scope.columns[i].sortOrder, fieldType: scope.columns[i].fieldType});
                }
            }
            var fullAggreagtionList = aggreagtionList;
            $http.get("admin/proxy/getJson?url=" + scope.dynamicTableUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId).success(function (response) {
//            $http.get("admin/proxy/getJson?url=" + scope.dynamicTableUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId + "&dateDuration=" + scope.widgetDateDuration + "&frequencyDuration=" + scope.widgetFrequencyDuration + "&customRange=" + scope.customRange).success(function (response) {

                scope.ajaxLoadingCompleted = true;
                scope.loadingTable = false;
                if (!response.data) {
                    return;
                }
                var pdfData = {};
                if (response.data.length === 0) {
                    scope.tableEmptyMessage = "No Data Found";
                    scope.hideEmptyTable = true;
                    pdfData[scope.widgetId] = "No Data Found";
                } else {
                    var responseData = response.data;
                    scope.orignalData = response.data;
                    pdfData[scope.widgetId] = scope.orignalData;
                    responseData = scope.orderData(responseData, sortFields);
                    var widgetData = JSON.parse(scope.widgetObj);
                    if (widgetData.maxRecord > 0) {
                        responseData = responseData.slice(0, widgetData.maxRecord);
                    }

                    if (groupByFields && groupByFields.length > 0) {
                        scope.groupingName = groupByFields;
                        groupedData = scope.group(responseData, groupByFields, aggreagtionList);
                        var dataToPush = {};
                        dataToPush = angular.extend(dataToPush, aggregate(responseData, fullAggreagtionList));
                        dataToPush.data = groupedData;
                        scope.groupingData = dataToPush;
                    } else {
                        var dataToPush = {};
                        dataToPush = angular.extend(dataToPush, aggregate(responseData, fullAggreagtionList));
                        dataToPush.data = responseData;
                        scope.groupingData = dataToPush;
//                    scope.groupingData = $sce.trustAsHtml(dataToPush);
                    }
                }
                //alert("CAlling");
                console.log(pdfData);
                // scope.pdfFunction({test: pdfData});
            });

            scope.initData = function (col) {
                angular.forEach(scope.columns, function (value, key) {
                    if (value.fieldName != col.fieldName) {
                        value.sortOrder = "";
                    }
                })
                if (col.sortOrder == "asc") {
                    col.sortOrder = "desc";
                } else {
                    col.sortOrder = "asc";
                }
                var sortFields = [];
                sortFields.push({fieldName: col.fieldName, sortOrder: col.sortOrder, fieldType: col.fieldType});
                console.log(sortFields);
                var responseData = scope.orignalData;
                // scope.orignalData = response.data;
                responseData = scope.orderData(responseData, sortFields);
                var widgetData = JSON.parse(scope.widgetObj);
                if (widgetData.maxRecord > 0) {
                    responseData = responseData.slice(0, widgetData.maxRecord);
                }

                if (groupByFields && groupByFields.length > 0) {
                    scope.groupingName = groupByFields;
                    groupedData = scope.group(responseData, groupByFields, aggreagtionList);
                    var dataToPush = {};
                    dataToPush = angular.extend(dataToPush, aggregate(responseData, fullAggreagtionList));
                    dataToPush.data = groupedData;
                    scope.groupingData = dataToPush;
                } else {
                    var dataToPush = {};
                    dataToPush = angular.extend(dataToPush, aggregate(responseData, fullAggreagtionList));
                    dataToPush.data = responseData;
                    scope.groupingData = dataToPush;
//                    scope.groupingData = $sce.trustAsHtml(dataToPush);
                }
            }

            scope.sortColumn = scope.columns;
            scope.objectHeader = [];
            scope.reverse = false;
            scope.toggleSort = function (index) {
                if (scope.sortColumn === scope.objectHeader[index]) {
                    scope.reverse = !scope.reverse;
                }
                scope.sortColumn = scope.objectHeader[index];
            };

//Dir-Paginations
            scope.pageChangeHandler = function (num) {
                console.log('reports page changed to ' + num);
            };

            scope.sum = function (list, fieldname) {
                var sum = 0;
                for (var i in list)
                {
                    if (isNaN(list[i][fieldname])) {

                    } else {
                        sum = sum + Number(list[i][fieldname]);
                    }
                }
                return sum;
            };

            scope.calculatedMetric = function (list, name, field1, field2) {
                var value1 = scope.sum(list, field1);
                var value2 = scope.sum(list, field2);
                var returnValue = "0";
                if (value1 && value2) {
                    returnValue = value1 / value2;
                }
                if (name == "ctr") {
                    // returnValue = returnValue * 100;
                }
                return returnValue;
            };

            listOfCalculatedFunction = [
                {name: 'ctr', field1: 'clicks', field2: 'impressions'},
                {name: 'cpa', field1: 'cost', field2: 'conversions'},
                {name: 'cpc', field1: 'cost', field2: 'clicks'},
                {name: 'cps', field1: 'spend', field2: 'clicks'},
                {name: 'cpr', field1: 'spend', field2: 'actions_post_reaction'},
                {name: 'ctl', field1: 'spend', field2: 'actions_like'},
                {name: 'cplc', field1: 'spend', field2: 'actions_link_click'},
                {name: 'cpcomment', field1: 'spend', field2: 'actions_comment'},
                {name: 'cposte', field1: 'spend', field2: 'actions_post_engagement'},
                {name: 'cpagee', field1: 'spend', field2: 'actions_page_engagement'},
                {name: 'cpp', field1: 'spend', field2: 'actions_post'},
            ];

            function aggregate(list, aggreationList) {
                var returnValue = {};
                angular.forEach(aggreationList, function (value, key) {
                    if (value.aggregationType == "sum") {
                        returnValue[value.fieldname] = scope.sum(list, value.fieldname);
                    }
                    if (value.aggregationType == "avg") {
                        returnValue[value.fieldname] = scope.sum(list, value.fieldname) / list.length;
                    }
                    if (value.aggregationType == "count") {
                        returnValue[value.fieldname] = list.length;
                    }
                    if (value.aggregationType == "min") {
                        returnValue[value.fieldname] = Math.min.apply(Math, list.map(function (currentValue) {
                            return Number(currentValue[value.fieldname]);
                        }));
                    }
                    if (value.aggregationType == "max") {
                        returnValue[value.fieldname] = Math.max.apply(Math, list.map(function (currentValue) {
                            return Number(currentValue[value.fieldname]);
                        }));
                    }
                    angular.forEach(listOfCalculatedFunction, function (calculatedFn, key) {
                        if (calculatedFn.name == value.aggregationType) {
                            returnValue[value.fieldname] = scope.calculatedMetric(list, calculatedFn.name, calculatedFn.field1, calculatedFn.field2);
                        }
                    });
                    if (Number.isNaN(returnValue)) {
                        returnValue[value.fieldname] = "-";
                    }
                });
                return returnValue;
            }

            scope.orderData = function (list, fieldnames) {
                if (fieldnames.length == 0) {
                    return list;
                }
                var fieldsOrder = [];
                angular.forEach(fieldnames, function (value, key) {
                    if (value.fieldType == "string") {
                        if (value.sortOrder == "asc") {
                            fieldsOrder.push(value.fieldName);
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push("-" + value.fieldName);
                        }
                        console.log(fieldsOrder);
                    } else if (value.fieldType == "number") {
                        if (value.sortOrder == "asc") {
                            //fieldsOrder.push(value.fieldname);
                            fieldsOrder.push(function (a) {

                                var parsedValue = parseFloat(a[value.fieldName]);
                                console.log(parsedValue);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                var parsedValue = parseFloat(a[value.fieldName]);
                                console.log(parsedValue);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return -1 * parsedValue;
                            });
                        }
                    } else if (value.fieldType == "date") {
                        if (value.sortOrder == "asc") {
                            //fieldsOrder.push(value.fieldname);
                            fieldsOrder.push(function (a) {

                                var parsedDate = new Date(a[value.fieldName]);
                                var parsedValue = parsedDate.getTime() / 1000;
                                console.log(parsedValue);
                                console.log("TIME ---> " + parsedValue);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                var parsedDate = new Date(a[value.fieldName]);
                                var parsedValue = parsedDate.getTime() / 1000;
                                console.log(parsedValue);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return -1 * parsedValue;
                            });
                        }
                    } else {
                        if (value.sortOrder == "asc") {
                            //fieldsOrder.push(value.fieldname);
                            fieldsOrder.push(function (a) {

                                var parsedValue = parseFloat(a[value.fieldName]);
                                console.log(parsedValue);
                                if (isNaN(parsedValue)) {
                                    return a[value.fieldName];
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                return -1 * parseFloat(a[value.fieldName])
                            });
                        }
                    }
                });
                return $filter('orderBy')(list, fieldsOrder);
            }
            scope.group = function (list, fieldnames, aggreationList) {
                var currentFields = fieldnames;
                if (fieldnames.length == 0)
                    return list;
                var actualList = list;
                var data = [];
                var groupingField = currentFields[0];
                var currentListGrouped = $filter('groupBy')(actualList, groupingField);
                var currentFields = currentFields.splice(1);
                angular.forEach(currentListGrouped, function (value1, key1) {
                    var dataToPush = {};
                    dataToPush._key = key1;
                    dataToPush[groupingField] = key1;
                    dataToPush._groupField = groupingField;
                    dataToPush = angular.extend(dataToPush, aggregate(value1, fullAggreagtionList));
                    dataToPush.data = scope.group(value1, currentFields, aggreationList);
                    data.push(dataToPush);
                });
                return data;
            };
        }
    };
});

app.directive('dynamictable', function ($http, uiGridConstants, uiGridGroupingConstants, $timeout, $stateParams, stats) {
    return{
        restrict: 'A',
        scope: {
            dynamicTableUrl: '@',
            widgetId: '@',
            widgetColumns: '@',
            setTableFn: '&'
        },
        template: '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<div id="grid1" class="grid full-height" ng-if="ajaxLoadingCompleted" ui-grid="gridOptions" ui-grid-grouping></div>',
        link: function (scope, element, attr) {
            scope.loadingTable = true;
            scope.gridOptions = {
                enableColumnMenus: false,
                enableGridMenu: false,
                enableRowSelection: false,
                enableGroupHeaderSelection: false,
                enableRowHeaderSelection: false,
                enableSorting: true,
                multiSelect: false,
                enableColumnResize: true,
                showColumnFooter: true,
                cellTooltip: true,
                enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
                enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            };
            var startDate = "";
            var endDate = "";
            var columnDefs = [];
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {

                columnDef = {
                    field: value.fieldName,
                    displayName: value.displayName,
//                        cellClass: 'space-numbers',
                    //width: "100%",
                    cellTooltip: true,
                    headerTooltip: true
                }
                if (value.width) {
                    columnDef.width = value.width + "%";
                }
                var cellFormat = "";
                if (value.displayFormat) {
                    cellFormat = value.displayFormat;
                }
                var cellAlignment = "";
                var cellWrapText = "";
                if (value.alignment === 'left') {
                    cellAlignment = 'text-left';
                } else if (value.alignment === 'right') {
                    cellAlignment = 'text-right';
                } else {
                    cellAlignment = 'text-center';
                }
                if (value.wrapText) {
                    cellWrapText = "wrap";
                }
                columnDef.cellTemplate = '<div  class="ui-grid-cell-contents ' + cellAlignment + " " + cellWrapText + '"><span>{{COL_FIELD | gridDisplayFormat : "' + cellFormat + '"}}</span></div>';
                columnDef.footerCellTemplate = '<div class="' + cellAlignment + '" >{{col.getAggregationValue() | gridDisplayFormat:"' + cellFormat + '"}}</div>';

                if (value.agregationFunction == "ctr") {
                    columnDef.aggregationType = stats.aggregator.ctrFooter,
                            columnDef.treeAggregation = {type: uiGridGroupingConstants.aggregation.CUSTOM},
                            columnDef.customTreeAggregationFn = stats.aggregator.ctr,
                            columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.SUM,
                            columnDef.cellFilter = 'gridDisplayFormat:"dsaf"',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
                }
                if (value.agregationFunction == "sum") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.SUM,
                            columnDef.cellFilter = 'gridDisplayFormat:"dsaf"',
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
                }
                if (value.agregationFunction == "count") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.COUNT,
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
                }
                if (value.agregationFunction == "max") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.MAX,
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
                }
                if (value.agregationFunction == "avg") {
                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.AVG,
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
                }
                if (value.agregationFunction == "min") {

                    columnDef.treeAggregationType = uiGridGroupingConstants.aggregation.MIN,
                            columnDef.cellTooltip = true,
                            columnDef.headerTooltip = true
                }
                if (value.groupPriority) {
                    columnDef.grouping = {groupPriority: value.groupPriority};
                }
                columnDefs.push(columnDef);
            });

            $http.get("admin/proxy/getJson?url=" + scope.dynamicTableUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId).success(function (response) {
                scope.ajaxLoadingCompleted = true;
                scope.loadingTable = false;
                scope.gridOptions = {
                    enableColumnMenus: false,
                    enableGridMenu: false,
                    enableRowSelection: false,
                    enableGroupHeaderSelection: false,
                    enableRowHeaderSelection: false,
                    enableSorting: true,
                    enableGroup: false,
                    multiSelect: false,
                    enableColumnResize: true,
                    data: response.data,
                    columnDefs: columnDefs,
                    showColumnFooter: true,
                    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
                    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER

                };
                function lineage() {
                    return this.name + ' of ' + this.parent;
                }

                function setHeight(extra) {
                    scope.height = ((scope.gridOptions.data.length * 30) + 30);
                    if (extra) {
                        scope.height += extra;
                    }
                    scope.api.grid.gridHeight = scope.height;
                }
            });
        }
    };
});

app.directive('tickerDirective', function ($http, $stateParams) {
    return{
        restrict: 'AE',
        template: '<div ng-show="loadingTicker" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<div ng-hide="loadingTicker" class="panel panel-default relative pnl-aln">' +
                '<div class="m-b-10" ng-hide="hideEmptyTicker">' +
                '<span>{{tickerTitle}}</span><br>' +
                '<span class="text-lg tickers">{{totalValue}}</span>' +
                '</div>' +
                '<div ng-show="hideEmptyTicker">{{tickerEmptyMessage}}</div>' +
                '</div>',
        scope: {
            tickerUrl: '@',
            tickerId: '@',
            tickerColumns: '@',
            tickerDateDuration: '@',
            tickerFrequencyDuration: '@',
            tickerCustomRange: '@'
        },
        link: function (scope, element, attr) {
            scope.loadingTicker = true;
            var tickerName;
            angular.forEach(JSON.parse(scope.tickerColumns), function (value, key) {
                scope.tickerTitle = value.displayName;
                tickerName = {fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat}
            });

            var format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
                    if (Number.isNaN(value)) {
                        return "-";
                    }
                    if (column.displayFormat.indexOf("%") > -1) {
                        // return d3.format(column.displayFormat)(value / 100);
                    }
                    return d3.format(column.displayFormat)(value);
                }
                return value;
            };

            var setData = [];
            var data = [];

            $http.get("admin/proxy/getJson?url=" + scope.tickerUrl + "&widgetId=" + scope.tickerId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId).success(function (response) {
//            $http.get("admin/proxy/getJson?url=" + scope.tickerUrl + "&widgetId=" + scope.tickerId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId + "&dateDuration=" + scope.widgetDateDuration + "&frequencyDuration=" + scope.widgetFrequencyDuration + "&customRange=" + scope.customRange).success(function (response) {
                scope.loadingTicker = false;
//                if(!response){
//                    return;
//                }
                if (response.length === 0) {
                    scope.tickerEmptyMessage = "No Data Found";
                    scope.hideEmptyTicker = true;
                } else {
                    if (!response) {
                        return;
                    }
                    var tickerData = response.data;
                    var loopCount = 0;
                    data = [tickerName.fieldName];
                    setData = tickerData.map(function (a) {
                        data.push(loopCount);
                        loopCount++;
                        return a[tickerName.fieldName];
                    });
                    var total = 0;
                    for (var i = 0; i < setData.length; i++) {
                        total += parseFloat(setData[i]);
                    }
                    scope.totalValue = format(tickerName, total);
                }
            });
        }
    };
});

app.directive('lineChartDirective', function ($http, $filter, $stateParams) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingLine" class="text-center"><img src="static/img/logos/loader.gif"></div>' +
                '<div ng-show="hideEmptyLine" class="text-center">{{lineEmptyMessage}}</div>',
        scope: {
            lineChartUrl: '@',
            widgetId: '@',
            setLineChartFn: '&',
            control: "=",
            collection: '@',
            widgetColumns: '@',
            lineChartId: '@',
            widgetDateDuration: '@',
            widgetFrequencyDuration: '@',
            widgetCustomRange: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingLine = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var sortDataType = "number";
            var displayDataFormat = {};
            var y2 = {show: false, label: ''};
            var axes = {};
            var startDate = "";
            var endDate = "";
            var sortFields = [];

            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                console.log(value)
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        if (format.indexOf("%") > -1) {
                            //return d3.format(format)(value / 100);
                        }
                        return d3.format(format)(value);
                    };
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                    axes[value.displayName] = 'y' + (value.yAxis > 1 ? 2 : '');
                }
                if (value.yAxis > 1) {
                    y2 = {show: true, label: ''};
                }
                if (value.sortOrder) {
                    sortFields.push({fieldName: value.fieldName, sortOrder: value.sortOrder, fieldType: value.fieldType});
                }
            });
            var xData = [];
            var xTicks = [];
            scope.orderData = function (list, fieldnames) {
                if (fieldnames.length == 0) {
                    return list;
                }
                var fieldsOrder = [];
                angular.forEach(fieldnames, function (value, key) {
                    if (value.fieldType == "string") {
                        if (value.sortOrder == "asc") {
                            fieldsOrder.push(value.fieldName);
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push("-" + value.fieldName);
                        }
                        console.log(fieldsOrder);
                    } else if (value.fieldType == "number") {
                        if (value.sortOrder == "asc") {
                            //fieldsOrder.push(value.fieldname);
                            fieldsOrder.push(function (a) {

                                var parsedValue = parseFloat(a[value.fieldName]);
                                console.log(parsedValue);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                var parsedValue = parseFloat(a[value.fieldName]);
                                console.log(parsedValue);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return -1 * parsedValue;
                            });
                        }
                    } else if (value.fieldType == "date") {
                        if (value.sortOrder == "asc") {
                            //fieldsOrder.push(value.fieldname);
                            fieldsOrder.push(function (a) {

                                var parsedDate = new Date(a[value.fieldName]);
                                var parsedValue = parsedDate.getTime() / 1000;
                                console.log(parsedValue);
                                console.log("TIME ---> " + parsedValue);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                var parsedDate = new Date(a[value.fieldName]);
                                var parsedValue = parsedDate.getTime() / 1000;
                                console.log(parsedValue);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return -1 * parsedValue;
                            });
                        }
                    } else {
                        if (value.sortOrder == "asc") {
                            //fieldsOrder.push(value.fieldname);
                            fieldsOrder.push(function (a) {

                                var parsedValue = parseFloat(a[value.fieldName]);
                                console.log(parsedValue);
                                if (isNaN(parsedValue)) {
                                    return a[value.fieldName];
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                return -1 * parseFloat(a[value.fieldName])
                            });
                        }
                    }
                });
                return $filter('orderBy')(list, fieldsOrder);
            }

//            function sortResults(unsortedData, prop, asc) {
//                sortedData = unsortedData.sort(function (a, b) {
//                    if (asc) {
//                        if (isNaN(a[prop])) {
//                            return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
//                        } else {
//                            return (parseInt(a[prop]) > parseInt(b[prop])) ? 1 : ((parseInt(a[prop]) < parseInt(b[prop])) ? -1 : 0);
//                        }
//                    } else {
//                        if (isNaN(a[prop])) {
//                            return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
//                        } else {
//                            return (parseInt(b[prop]) > parseInt(a[prop])) ? 1 : ((parseInt(b[prop]) < parseInt(a[prop])) ? -1 : 0);
//                        }
//                    }
//                });
//                return sortedData;
//            }

            if (scope.lineChartUrl) {
                $http.get("admin/proxy/getJson?url=" + scope.lineChartUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId).success(function (response) {
//                $http.get("admin/proxy/getJson?url=" + scope.lineChartUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId + "&dateDuration=" + scope.widgetDateDuration + "&frequencyDuration=" + scope.widgetFrequencyDuration + "&customRange=" + scope.customRange).success(function (response) {
                    scope.loadingLine = false;
                    if (response.data.length === 0) {
                        scope.lineEmptyMessage = "No Data Found";
                        scope.hideEmptyLine = true;
                    } else {
                        scope.xAxis = [];
                        var loopCount = 0;
                        var chartData = response.data;
                        if (sortFields.length > 0) {
                            chartData = scope.orderData(chartData, sortFields);
                        }
                        xTicks = [xAxis.fieldName];
                        xData = chartData.map(function (a) {
                            xTicks.push(loopCount);
                            loopCount++;
                            return a[xAxis.fieldName];
                        });
                        columns.push(xTicks);

                        angular.forEach(yAxis, function (value, key) {
                            ySeriesData = chartData.map(function (a) {
                                return a[value.fieldName] || "0";
                            });
                            ySeriesData.unshift(value.displayName);
                            columns.push(ySeriesData);
                        });
                        var chart = c3.generate({
                            bindto: element[0],
                            data: {
                                x: xAxis.fieldName,
                                columns: columns,
                                labels: labels,
                                axes: axes
                            },
                            tooltip: {show: false},
                            axis: {
                                x: {
                                    tick: {
                                        format: function (x) {
                                            return xData[x];
                                        }
                                    }
                                },
                                y2: {
                                    show: true,
                                    tick: {
                                        format: d3.format(".2f")
                                    }
                                }
                            },
                            grid: {
                                x: {
                                    show: true
                                },
                                y: {
                                    show: true
                                }
                            }
                        });
                    }
                });
            }
        }
    };
});

app.directive('barChartDirective', function ($http, $stateParams) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingBar" class="text-center"><img src="static/img/logos/loader.gif"></div>' +
                '<div ng-show="hideEmptyBar" class="text-center">{{barEmptyMessage}}</div>',
        scope: {
            barChartUrl: '@',
            widgetId: '@',
            setBarChartFn: '&',
            barChartId: '@',
            widgetColumns: '@',
            widgetDateDuration: '@',
            widgetFrequencyDuration: '@',
            widgetCustomRange: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingBar = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var y2 = {show: false, label: ''};
            var axes = {};
            var startDate = "";
            var endDate = "";
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        if (format.indexOf("%") > -1) {
                            //return d3.format(format)(value / 100);
                        }
                        return d3.format(format)(value);
                    };
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                    axes[value.displayName] = 'y' + (value.yAxis > 1 ? 2 : '');
                }
                if (value.yAxis > 1) {
                    y2 = {show: true, label: ''};
                }
            });
            var xData = [];
            var xTicks = [];

            function sortResults(unsortedData, prop, asc) {
                sortedData = unsortedData.sort(function (a, b) {
                    if (asc) {
                        if (isNaN(a[prop])) {
                            return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(a[prop]) > parseInt(b[prop])) ? 1 : ((parseInt(a[prop]) < parseInt(b[prop])) ? -1 : 0);
                        }
                    } else {
                        if (isNaN(a[prop])) {
                            return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(b[prop]) > parseInt(a[prop])) ? 1 : ((parseInt(b[prop]) < parseInt(a[prop])) ? -1 : 0);
                        }
                    }
                });
                return sortedData;
            }

            if (scope.barChartUrl) {

                $http.get("admin/proxy/getJson?url=" + scope.barChartUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId).success(function (response) {
//                $http.get("admin/proxy/getJson?url=" + scope.barChartUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId + "&dateDuration=" + scope.widgetDateDuration + "&frequencyDuration=" + scope.widgetFrequencyDuration + "&customRange=" + scope.customRange).success(function (response) {
                    scope.loadingBar = false;
                    if (response.data.length === 0) {
                        scope.barEmptyMessage = "No Data Found";
                        scope.hideEmptyBar = true;
                    } else {
                        scope.xAxis = [];
                        var loopCount = 0;
                        var chartData = response.data;
                        chartData = sortResults(chartData, sortField, sortOrder);
                        xTicks = [xAxis.fieldName];
                        xData = chartData.map(function (a) {
                            xTicks.push(loopCount);
                            loopCount++;
                            return a[xAxis.fieldName];
                        });

                        columns.push(xTicks);

                        angular.forEach(yAxis, function (value, key) {
                            ySeriesData = chartData.map(function (a) {
                                return a[value.fieldName] || "0";
                            });
                            ySeriesData.unshift(value.displayName);
                            columns.push(ySeriesData);
                        });
                        var chart = c3.generate({
                            bindto: element[0],
                            data: {
                                x: xAxis.fieldName,
                                columns: columns,
                                labels: labels,
                                type: 'bar',
                                axes: axes
                            },
                            tooltip: {show: false},
                            axis: {
                                x: {
                                    tick: {
                                        format: function (x) {
                                            return xData[x];
                                        }
                                    }
                                },
                                y2: y2
                            },
                            grid: {
                                x: {
                                    show: true
                                },
                                y: {
                                    show: true
                                }
                            }
                        });
                    }
                });
            }
        }
    };
});
app.directive('pieChartDirective', function ($http, $stateParams) {
    return{
        restrict: 'AC',
        template: '<div ng-show="loadingPie" class="text-center"><img src="static/img/logos/loader.gif"></div>' +
                '<div ng-show="hideEmptyPie" class="text-center">{{pieEmptyMessage}}</div>',
        scope: {
            pieChartUrl: '@',
            widgetId: '@',
            widgetColumns: '@',
            setPieChartFn: '&',
            pieChartId: '@',
            loadingPie: '&',
            widgetDateDuration: '@',
            widgetFrequencyDuration: '@',
            widgetCustomRange: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingPie = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var axes = {};
            var startDate = "";
            var endDate = "";
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        if (format.indexOf("%") > -1) {
                            //return d3.format(format)(value / 100);
                        }
                        return d3.format(format)(value);
                    };
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                    axes[value.displayName] = 'y' + (value.yAxis > 1 ? 2 : '');
                }
                if (value.yAxis > 1) {
                    y2 = {show: true, label: ''};
                }
            });
            var xData = [];
            var xTicks = [];

            function sortResults(unsortedData, prop, asc) {
                sortedData = unsortedData.sort(function (a, b) {
                    if (asc) {
                        if (isNaN(a[prop])) {
                            return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(a[prop]) > parseInt(b[prop])) ? 1 : ((parseInt(a[prop]) < parseInt(b[prop])) ? -1 : 0);
                        }
                    } else {
                        if (isNaN(a[prop])) {
                            return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(b[prop]) > parseInt(a[prop])) ? 1 : ((parseInt(b[prop]) < parseInt(a[prop])) ? -1 : 0);
                        }
                    }
                });
                return sortedData;
            }

            if (scope.pieChartUrl) {
                $http.get("admin/proxy/getJson?url=" + scope.pieChartUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId).success(function (response) {
//                $http.get("admin/proxy/getJson?url=" + scope.pieChartUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId + "&dateDuration=" + scope.widgetDateDuration + "&frequencyDuration=" + scope.widgetFrequencyDuration + "&customRange=" + scope.customRange).success(function (response) {
                    scope.loadingPie = false;
                    if (response.data.length === 0) {
                        scope.pieEmptyMessage = "No Data Found";
                        scope.hideEmptyPie = true;
                    } else {
                        scope.xAxis = [];
                        var loopCount = 0;
                        var chartData = response.data;
                        chartData = sortResults(chartData, sortField, sortOrder);
                        xTicks = [xAxis.fieldName];
                        xData = chartData.map(function (a) {
                            xTicks.push(loopCount);
                            loopCount++;
                            return a[xAxis.fieldName];
                        });
                        columns.push(xTicks);
                        angular.forEach(yAxis, function (value, key) {
                            ySeriesData = chartData.map(function (a) {
                                return a[value.fieldName] || "0";
                            });
                            ySeriesData.unshift(value.displayName);
                            columns.push(ySeriesData);
                        });

                        var data = {};
                        var legends = [];
                        var yAxisField = yAxis[0];
                        chartData.forEach(function (e) {
                            legends.push(e[xAxis.fieldName]);
                            data[e[xAxis.fieldName]] = data[e[xAxis.fieldName]] ? data[e[xAxis.fieldName]] : 0 + e[yAxisField.fieldName] ? e[yAxisField.fieldName] : 0;
                        })

                        var chart = c3.generate({
                            bindto: element[0],
//                        data: {
//                            x: xAxis.fieldName,
//                            columns: data,
//                            labels: labels,
//                            type: 'pie'
//                        },
                            data: {
                                json: [data],
                                keys: {
                                    value: xData,
                                },
                                type: 'pie'
                            },
                            tooltip: {show: false},
                            axis: {
                                x: {
                                    tick: {
                                        format: function (x) {
                                            return xData[x];
                                        }
                                    }
                                }
                            },
                            grid: {
                                x: {
                                    show: true
                                },
                                y: {
                                    show: true
                                }
                            }
                        });
                    }
                });
            }
        }
    };
});

app.directive('areaChartDirective', function ($http, $stateParams) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingArea" class="text-center"><img src="static/img/logos/loader.gif"></div>' +
                '<div ng-show="hideEmptyArea" class="text-center">{{areaEmptyMessage}}</div>',
        scope: {
            setPieChartFn: '&',
            widgetId: '@',
            areaChartUrl: '@',
            widgetColumns: '@',
            pieChartId: '@',
            widgetDateDuration: '@',
            widgetFrequencyDuration: '@',
            widgetCustomRange: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingArea = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var y2 = {show: false, label: ''};
            var axes = {};
            var startDate = "";
            var endDate = "";
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        if (format.indexOf("%") > -1) {
                            //return d3.format(format)(value / 100);
                        }
                        return d3.format(format)(value);
                    };
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                }
            });
            var xData = [];
            var xTicks = [];

            function sortResults(unsortedData, prop, asc) {
                sortedData = unsortedData.sort(function (a, b) {
                    if (asc) {
                        if (isNaN(a[prop])) {
                            return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(a[prop]) > parseInt(b[prop])) ? 1 : ((parseInt(a[prop]) < parseInt(b[prop])) ? -1 : 0);
                        }
                    } else {
                        if (isNaN(a[prop])) {
                            return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
                        } else {
                            return (parseInt(b[prop]) > parseInt(a[prop])) ? 1 : ((parseInt(b[prop]) < parseInt(a[prop])) ? -1 : 0);
                        }
                    }
                });
                return sortedData;
            }

            if (scope.areaChartUrl) {

                $http.get("admin/proxy/getJson?url=" + scope.areaChartUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId).success(function (response) {
//                $http.get("admin/proxy/getJson?url=" + scope.areaChartUrl + "&widgetId=" + scope.widgetId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&dealerId=" + $stateParams.dealerId + "&dateDuration=" + scope.widgetDateDuration + "&frequencyDuration=" + scope.widgetFrequencyDuration + "&customRange=" + scope.customRange).success(function (response) {
                    scope.loadingArea = false;
                    if (response.data.length === 0) {
                        scope.areaEmptyMessage = "No Data Found";
                        scope.hideEmptyArea = true;
                    } else {
                        scope.xAxis = [];
                        var loopCount = 0;
                        var chartData = response.data;
                        chartData = sortResults(chartData, sortField, sortOrder);
                        xTicks = [xAxis.fieldName];
                        xData = chartData.map(function (a) {
                            xTicks.push(loopCount);
                            loopCount++;
                            return a[xAxis.fieldName];
                        });
                        columns.push(xTicks);
                        angular.forEach(yAxis, function (value, key) {
                            ySeriesData = chartData.map(function (a) {
                                return a[value.fieldName] || "0";
                            });
                            ySeriesData.unshift(value.displayName);
                            columns.push(ySeriesData);
                        });
                        var chart = c3.generate({
                            bindto: element[0],
                            data: {
                                x: xAxis.fieldName,
                                columns: columns,
                                labels: labels,
                                type: 'area',
                                axes: axes
                            },
                            tooltip: {show: false},
                            axis: {
                                x: {
                                    tick: {
                                        format: function (x) {
                                            return xData[x];
                                        }
                                    }
                                },
                                y2: {show: true}
                            },
                            grid: {
                                x: {
                                    show: true
                                },
                                y: {
                                    show: true
                                }
                            }
                        });
                    }
                });
            }
        }
    };
})
        .filter('setDecimal', function () {
            return function (input, places) {
                if (isNaN(input))
                    return input;
                var factor = "1" + Array(+(places > 0 && places + 1)).join("0");
                return Math.round(input * factor) / factor;
            };
        })
        .filter('gridDisplayFormat', function () {
            return function (input, formatString) {
                if (formatString) {
                    if (formatString.indexOf("%") > -1) {
                        //return d3.format(formatString)(input / 100);
                    }
                    return d3.format(formatString)(input);
                }
                return input;
            }
        })
        .filter('capitalize', function () {
            return function (input) {
                return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
            }
        })
        .filter('xAxis', [function () {
                return function (chartXAxis) {
                    var xAxis = ['', 'x-1']
                    return xAxis[chartXAxis];
                }
            }])
        .filter('yAxis', [function () {
                return function (chartYAxis) {
                    var yAxis = ['', 'y-1', 'y-2']
                    return yAxis[chartYAxis];
                }
            }])
        .filter('hideColumn', [function () {
                return function (chartYAxis) {
                    var hideColumn = ['No', 'Yes']
                    return hideColumn[chartYAxis];
                };
            }]);
//        .filter('titleCase', function () {
//            return function (input) {
//                input = input || '';
//                return input.replace(/\w\S*/g, function (txt) {
//                    return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
//                });
//            };
//        })
app.filter('to_trusted', ['$sce', function ($sce) {
        return function (text) {
            var filterData;
            if (angular.isNumber(text)) {
                filterData = $sce.trustAsHtml(text).toString();
                alert("Integer : " + filterData)
            } else {
                filterData = $sce.trustAsHtml(text);
                alert("String : " + filterData)
            }
            return filterData;
        };
    }]);

app.service('stats', function ($filter) {
    var coreAccumulate = function (aggregation, value) {
        initAggregation(aggregation);
        if (angular.isUndefined(aggregation.stats.accumulator)) {
            aggregation.stats.accumulator = [];
        }
        aggregation.stats.accumulator.push(value);
    };

    var initAggregation = function (aggregation) {
        /* To be used in conjunction with the cleanup finalizer */
        if (angular.isUndefined(aggregation.stats)) {
            aggregation.stats = {sum: 0, impressionsSum: 0, clicksSum: 0, costSum: 0, conversionsSum: 0};
        }
    };
    var initProperty = function (obj, prop) {
        /* To be used in conjunction with the cleanup finalizer */
        if (angular.isUndefined(obj[prop])) {
            obj[prop] = 0;
        }
    };

    var increment = function (obj, prop) {
        /* if the property on obj is undefined, sets to 1, otherwise increments by one */
        if (angular.isUndefined(obj[prop])) {
            obj[prop] = 1;
        } else {
            obj[prop]++;
        }
    };

    var service = {
        aggregator: {
            accumulate: {
                /* This is to be used with the uiGrid customTreeAggregationFn definition,
                 * to accumulate all of the data into an array for sorting or other operations by customTreeAggregationFinalizerFn
                 * In general this strategy is not the most efficient way to generate grouped statistics, but
                 * sometime is the only way.
                 */
                numValue: function (aggregation, fieldValue, numValue) {
                    return coreAccumulate(aggregation, numValue);
                },
                fieldValue: function (aggregation, fieldValue) {
                    return coreAccumulate(aggregation, fieldValue);
                }
            },
            ctr: function (aggregation, fieldValue, numValue, row) {

                initAggregation(aggregation);
                increment(aggregation.stats, 'count');
                aggregation.stats.clicksSum += row.entity.clicks;
                aggregation.stats.impressionsSum += row.entity.impressions;

                aggregation.value = (aggregation.stats.clicksSum * 100) / aggregation.stats.impressionsSum; //row.entity.balance;
            },
            ctrFooter: function (x, y, z) {
                // Need Sum of balance/sum of age
                var clicksColumn = $filter('filter')(this.grid.columns, {displayName: 'Clicks'})[0];
                var impressionsColumn = $filter('filter')(this.grid.columns, {displayName: 'Impressions'})[0];
                clicksColumn.updateAggregationValue();
                impressionsColumn.updateAggregationValue();
                var aggregatedClicks = clicksColumn.aggregationValue;
                var aggregatedImpressions = impressionsColumn.aggregationValue;
                if (aggregatedClicks && aggregatedImpressions) {
                    if (isNaN(aggregatedClicks)) {
                        aggregatedClicks = Number(aggregatedClicks.replace(/[^0-9.]/g, ""));
                    }
                    if (isNaN(aggregatedImpressions)) {
                        aggregatedImpressions = Number(aggregatedImpressions.replace(/[^0-9.]/g, ""));
                    }
                    return (aggregatedClicks * 100) / aggregatedImpressions;
                }
                return "";
            },
            cpc: function (aggregation, fieldValue, numValue, row) {

                initAggregation(aggregation);
                increment(aggregation.stats, 'count');
                aggregation.stats.clicksSum += row.entity.clicks;
                aggregation.stats.costSum += row.entity.cost;
                //aggregation.stats.sum += row.entity.clicks * row.entity.age;

                aggregation.value = aggregation.stats.costSum / aggregation.stats.clicksSum; //row.entity.balance;
            },
            cpcFooter: function (x, y, z) {
                // Need Sum of balance/sum of age
                var clicksColumn = $filter('filter')(this.grid.columns, {displayName: 'Clicks'})[0];
                var costColumn = $filter('filter')(this.grid.columns, {displayName: 'Cost'})[0];
                clicksColumn.updateAggregationValue();
                costColumn.updateAggregationValue();
                var aggregatedClicks = clicksColumn.aggregationValue;
                var aggregatedCost = costColumn.aggregationValue;
                if (aggregatedClicks && aggregatedCost) {
                    if (isNaN(aggregatedClicks)) {
                        aggregatedClicks = Number(aggregatedClicks.replace(/[^0-9.]/g, ""));
                    }
                    if (isNaN(aggregatedCost)) {
                        aggregatedCost = Number(aggregatedCost.replace(/[^0-9.]/g, ""));
                    }
                    return aggregatedCost / aggregatedClicks;
                }
                return "";
            },
            cpa: function (aggregation, fieldValue, numValue, row) {

                initAggregation(aggregation);
                increment(aggregation.stats, 'count');
                aggregation.stats.conversionsSum += row.entity.conversions;
                aggregation.stats.costSum += row.entity.cost;
                aggregation.value = aggregation.stats.costSum / aggregation.stats.conversionsSum; //row.entity.balance;
            },
            cpaFooter: function (x, y, z) {
                // Need Sum of balance/sum of age
                var conversionsColumn = $filter('filter')(this.grid.columns, {displayName: 'Conversions'})[0];
                var costColumn = $filter('filter')(this.grid.columns, {displayName: 'Cost'})[0];
                conversionsColumn.updateAggregationValue();
                costColumn.updateAggregationValue();
                var aggregatedConversions = conversionsColumn.aggregationValue;
                var aggregatedCost = costColumn.aggregationValue;
                if (aggregatedConversions && aggregatedCost) {
                    if (isNaN(aggregatedConversions)) {
                        aggregatedConversions = Number(aggregatedConversions.replace(/[^0-9.]/g, ""));
                    }
                    if (isNaN(aggregatedCost)) {
                        aggregatedCost = Number(aggregatedCost.replace(/[^0-9.]/g, ""));
                    }
                    return aggregatedCost / aggregatedConversions;
                }
                return "";
            }
        },
        finalizer: {
            cleanup: function (aggregation) {
                delete aggregation.stats;
                if (angular.isUndefined(aggregation.rendered)) {
                    aggregation.rendered = aggregation.value;
                }
            },
            ctr: function (aggregation) {
                //service.finalizer.variance(aggregation);
                aggregation.value = 20000;
                //aggregation.rendered = 30000;
            }
        },
    };

    return service;
});
