/*
 */
angular.module('openAssetAssignmentDialogModule', ['ngResource', 'modalServiceModule', 'assetAssignmentModule', 'storageServiceModule', 'otUiConfigurationServiceModule', 'languageServiceModule'])
    .factory('openAssetAssignmentDialogService', function($q, $log, $resource, modalService, otUiConfigurationService, languageService, MODAL_BUTTON_ACTIONS) {
        var thisService = {

            context: null,

            openAssetAssignmentDialog: function(context, onAssetAssignmentCompleted) {
                $log.info('openAssetAssignmentDialog(): ENTER context=' + JSON.stringify(context));

                this.context = context;

                // var iframe = "<iframe onload=\"otDamlinkOnAssetAssignmentIframeLoad()\" width=\"100%\" height=\"750px\" id=\"ot-damlink-asset-assignment-iframe\" src=\"/clemens/damlink-smartedit-ui.html\"></iframe>";
                var iframe = "<iframe onload=\"otDamlinkOnAssetAssignmentIframeLoad()\" width=\"100%\" height=\"750px\" id=\"ot-damlink-asset-assignment-iframe\" sandbox=\"allow-scripts allow-same-origin allow-popups\" src=\"/otmmws/otmmProxy/ot-damlink/damlink-ui/static/damlink-smartedit-ui.html\"></iframe>";

                languageService.getResolveLocale().then(function(locale) {

                    otUiConfigurationService.initialize(
                        thisService.context.itemCatalogVersion,
                        thisService.context.itemId,
                        thisService.context.attributeId).then(function(itemPk) {

                        thisService.context.itemPk = itemPk;

                        thisService.context.currentLocale = locale;

                        modalService.open({
                            title: "otmmsmartedit.assetassignmentdialog.title",
                            templateInline: iframe,
                            controller: "openAssetAssignmentDialogModalController",

                            //buttons: [
                            //    {
                            //        id: 'startAssetAssignment',
                            //        label: "Start Asset Assignment",
                            //        action: MODAL_BUTTON_ACTIONS.CLOSE
                            //    },
                            //    {
                            //        label: "Cancel",
                            //        action: MODAL_BUTTON_ACTIONS.DISMISS
                            //    }
                            //]
                            // cssClasses: "ot-damlink-asset-assignment-modal-dialog yFrontModal"
                            cssClasses: "ot-damlink-asset-assignment-modal-dialog"
                        }).then(function() {
                            onAssetAssignmentCompleted();
                        }, function() {
                            // Cancel Asset Assignment Dialog
                        });
                    });
                });

                $log.info('openAssetAssignmentDialog(): LEAVE');

            }
        };
        return thisService;
    })

    .controller('openAssetAssignmentDialogModalController', function($scope, $q, $resource, assetAssignmentService, storageService, otUiConfigurationService, languageService, openAssetAssignmentDialogService) {


        otDamlinkOnAssetAssignmentIframeLoad = function() {

            var frame = document.getElementById("ot-damlink-asset-assignment-iframe");
            var fwin = frame.contentWindow;

            fwin.otDamLinkStartParameters = {
                $resource: $resource,

                angular: angular,

                dismissModalDialog: function() {
                    $scope.modalController.dismiss();
                },

                closeModalDialog: function() {
                    $scope.modalController.close();
                },

                performAssetAssignment: assetAssignmentService.performAssetAssignment,

                context: openAssetAssignmentDialogService.context,

                uiconfiguration: {
                    hybrisSystemId: otUiConfigurationService.getHybrisSystemId(),
                    configurationId: otUiConfigurationService.getConfigurationId()
                },

                isMultiSelectEnabled: false,

                alreadyAssignedAssetId: openAssetAssignmentDialogService.context.otmmAssetId,

                currentLocale: openAssetAssignmentDialogService.context.currentLocale,

                getAccessToken: function() {
                    var deferred = $q.defer();

                    storageService.getAuthToken("/authorizationserver/oauth/token").then(function(authToken) {
                        if (authToken !== undefined && authToken.access_token !== undefined) {
                            deferred.resolve(authToken.access_token);
                        } else {
                            deferred.reject("authToken seems to be corrupted: " + authToken);
                        }
                    }, function(error) {
                        deferred.reject(error);
                    });

                    return deferred.promise;
                },

                mediaFormat: openAssetAssignmentDialogService.context.mediaFormat,

                disableCropping: openAssetAssignmentDialogService.context.disableCropping
            };
        };
    });
