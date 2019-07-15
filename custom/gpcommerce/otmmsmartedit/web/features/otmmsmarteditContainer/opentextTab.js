(function() {
    angular.module('opentextTabModule', ['ngResource', 'modalServiceModule', 'openAssetAssignmentDialogModule'])

        .controller('opentextTabInnerController', function($q, $log, $scope, $resource, $http, $templateCache, openAssetAssignmentDialogService) {

            $scope.restUrl = "/otmmws/damlink-ui/folder/root/children";
            $scope.restResponse = "";


            $scope.executeRestUrl = function() {
                $log.info('executeRestUrl(): ENTER restUrl=' + $scope.restUrl);

                $resource($scope.restUrl).get(function(response) {
                    $scope.restResponse = JSON.stringify(response, null, 4);
                }, function(error) {
                    $scope.restResponse = JSON.stringify(error, null, 4);
                });

                $log.info('executeRestUrl(): LEAVE');
            };

            $scope.openAssetAssignmentDialog = function() {
                openAssetAssignmentDialogService.openAssetAssignmentDialog();
            };

            $scope.loadDamlinkHtml = function() {

                return $q(function(resolve, reject) {

                    //var damlinkHtmlUrl = "/otmmws/damlink-ui/damlink-uiiiiii.html";
                    //var damlinkHtmlUrl = "/otmmws/index.jsp";
                    //var damlinkHtmlUrl = "/otmmws/damlink-ui.jsp";
                    //var damlinkHtmlUrl = "/otmmws/damlink-ui.html";
                    var damlinkHtmlUrl = "/otmmws/damlink-ui/damlink-ui.html";

                    var damlinkHtml = $templateCache.get(damlinkHtmlUrl);
                    if (damlinkHtml === undefined) {
                        $http({
                            method: 'GET',
                            url: damlinkHtmlUrl
                        }).then(function(response) {
                            if (response.status === 200) {
                                $templateCache.put(damlinkHtmlUrl, response.data);
                                resolve($templateCache.get(damlinkHtmlUrl));
                            } else {
                                reject(response);
                            }
                        }, function(error) {
                            reject(error);
                        });
                    } else {
                        resolve(damlinkHtml);
                    }
                });

            };
        })


        .directive('opentextTab', function($log, $q) {
            return {
                restrict: 'E',
                transclude: false,
                templateUrl: 'web/features/otmmsmarteditContainer/tabs/opentext/opentextTabInnerTemplate.html',
                scope: {
                    componentId: '=',
                    componentType: '=',
                    tabId: '='
                },
                link: function(scope) {

                }
            };
        });
})();
