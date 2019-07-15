/*
 */
angular.module('otSeFileSelectorModule', ['ngResource', 'openAssetAssignmentDialogModule', 'alertServiceModule'])
    .controller('otSeFileSelectorController', function($resource, $log, $scope, openAssetAssignmentDialogService, alertService) {
        this.imageRoot = this.imageRoot || "";
        this.disabled = this.disabled || false;
        this.customClass = this.customClass || "";
        this.itemId = this.itemId || "";
        this.itemCatalogVersion = this.itemCatalogVersion || "";
        this.attributeId = this.attributeId || "";
        this.locale = this.locale || "";
        this.mediaFormat = this.mediaFormat || "";
        this.pristineMedia = this.pristineMedia || "";
        this.otmmAssetId = this.otmmAssetId || "";
        this.disableCropping = angular.isString(this.disableCropping) && this.disableCropping === "true";

        //var context = {
        //    itemPk: this.itemPk,
        //    attributeId: this.attributeId,
        //    locale: this.locale,
        //    mediaFormat: this.mediaFormat
        //};

        $scope.openAssetAssignmentDialog = function() {

            if (angular.isString(this.itemId) && this.itemId.length > 0) {

                openAssetAssignmentDialogService.openAssetAssignmentDialog({
                    itemId: this.itemId,
                    itemCatalogVersion: this.itemCatalogVersion,
                    attributeId: this.attributeId,
                    locale: this.locale,
                    mediaFormat: this.mediaFormat,
                    pristineMedia: this.pristineMedia,
                    disableCropping: this.disableCropping,
                    otmmAssetId: this.otmmAssetId
                }, function() {
                    this.onAssetAssignmentCompleted();
                }.bind(this));

            } else {
                alertService.pushAlerts([{
                    successful: false,
                    message: "Please SAVE and reopen dialog to continue.",
                    closeable: true
                }]);
            }
        }.bind(this);


        this.$onInit = function() {

        };

    })
    .directive('otSeFileSelector', function() {
        return {
            templateUrl: 'otSeFileSelectorTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'otSeFileSelectorController',
            controllerAs: 'ctrl',
            bindToController: {
                itemId: '=',
                itemCatalogVersion: '=',
                attributeId: '=',
                locale: '=',
                mediaFormat: '=?', // <-- the '?' makes it optional
                imageRoot: '=',
                uploadIcon: '=',
                labelI18nKey: '=',
                acceptedFileTypes: '=',
                customClass: '=',
                disabled: '=',
                onFileSelect: '&',
                onAssetAssignmentCompleted: '&',
                pristineMedia: '=',
                iconRightSide: '@',
                disableCropping: '@',
                otmmAssetId: '='
            },
            link: function($scope, $element) {}
        };
    });
