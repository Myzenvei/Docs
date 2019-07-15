/*
 *
 */
angular.module('seMediaFormatModule', [
        'mediaServiceModule',
        'seFileSelectorModule',
        'seFileValidationServiceModule',
        'cmsSmarteditServicesModule',
        'assetAssignmentModule'
    ])
    .constant('seMediaFormatConstants', {
        I18N_KEYS: {
            UPLOAD: 'se.media.format.upload',
            REPLACE: 'otmmsmartedit.replace',
            UNDER_EDIT: 'se.media.format.under.edit',
            REMOVE: 'otmmsmartedit.remove',
            PICK_ASSET: 'otmmsmartedit.pick.asset',
            REPLACE_ALL: 'otmmsmartedit.replace.all'
        },
        UPLOAD_ICON_URL: '/images/upload_image.png',
        UPLOAD_ICON_DIS_URL: '/images/upload_image_disabled.png',
        DELETE_ICON_URL: '/images/remove_image_small.png',
        REPLACE_ICON_URL: '/images/replace_image_small.png',
        ADV_INFO_ICON_URL: '/images/info_image_small.png'
    })
    .controller('seMediaFormatController', function(mediaService, seMediaFormatConstants, seFileValidationServiceConstants,
        assetsService, $scope, assetAssignmentService) {
        this.i18nKeys = seMediaFormatConstants.I18N_KEYS;
        this.acceptedFileTypes = seFileValidationServiceConstants.ACCEPTED_FILE_TYPES;

        var assetsRoot = assetsService.getAssetsRoot();
        this.uploadIconUrl = assetsRoot + seMediaFormatConstants.UPLOAD_ICON_URL;
        this.uploadIconDisabledUrl = assetsRoot + seMediaFormatConstants.UPLOAD_ICON_DIS_URL;

        this.deleteIconUrl = assetsRoot + seMediaFormatConstants.DELETE_ICON_URL;
        this.replaceIconUrl = assetsRoot + seMediaFormatConstants.REPLACE_ICON_URL;
        this.advInfoIconUrl = assetsRoot + seMediaFormatConstants.ADV_INFO_ICON_URL;

        this.fetchMedia = function() {
            this.thumbnailUrl = "";
            this.otmmAssetId = "";

            mediaService.getMedia(this.mediaUuid).then(function(val) {

                var catalogId = this.editorParameters.catalogId;
                var catalogVersion = this.editorParameters.catalogVersion;
                var code = val.code;

                assetAssignmentService.getOpenTextMedia(code, catalogId, catalogVersion).then(function(openTextMedia) {
                    if (angular.isString(openTextMedia.otmmThumbnailUrl) && openTextMedia.otmmThumbnailUrl.length > 0) {
                        this.thumbnailUrl = openTextMedia.otmmThumbnailUrl;
                    }
                    if (angular.isString(openTextMedia.otmmAssetId) && openTextMedia.otmmAssetId.length > 0) {
                        this.otmmAssetId = openTextMedia.otmmAssetId;
                    }
                    if (this.thumbnailUrl.length < 1) {
                        this.thumbnailUrl = val.url;
                    }

                    this.media = val;
                    this.saveDummyMedia();
                }.bind(this));
            }.bind(this));
        };

        this.isDummyMedia = function() {
            return angular.isString(this.media.code) && this.media.code.indexOf("otmmDummyMedia_") === 0;
        };

        this.createDummyMedia = function() {

            if ((!angular.isString(this.itemId) || this.itemId.length === 0) && this.mediaFormat === "widescreen" && this.locale === "en") {

                var catalogId = this.editorParameters.catalogId;
                var catalogVersion = this.editorParameters.catalogVersion;

                assetAssignmentService.createDummyMedia(catalogId, catalogVersion).then(function(dummyMedia) {

                    this.mediaUuid = dummyMedia.uuid;
                    // this.fetchMedia();

                }.bind(this), function(error) {
                    console.log(error);
                });
            }
        }.bind(this);

        this.saveDummyMedia = function() {

            if (this.isDummyMedia()) {
                var dummyMedia = {
                    catalogId: this.editorParameters.catalogId,
                    catalogVersion: this.editorParameters.catalogVersion,
                    code: this.media.code
                };
                assetAssignmentService.setDummyMediaToBeDeleted(dummyMedia);
            }
        }.bind(this);

        this.isMediaCodeValid = function() {
            return angular.isString(this.mediaUuid) && this.mediaUuid.length > 0;
        };

        this.isMediaPreviewEnabled = function() {
            return this.isMediaCodeValid() && !this.isUnderEdit && this.media && this.media.code;
        };

        this.isMediaEditEnabled = function() {
            return !this.isMediaCodeValid() && !this.isUnderEdit;
        };

        this.getErrors = function() {
            return (this.errors || []).filter(function(error) {
                return error.format === this.mediaFormat;
            }.bind(this)).map(function(error) {
                return error.message;
            });
        };

        this.$onInit = function() {
            if (this.isMediaCodeValid()) {
                this.fetchMedia();
            }
            this.mediaFormatI18NKey = "otmmsmartedit.media.format." + this.mediaFormat;
            this.createDummyMedia();
        };

        $scope.$watch(function() {
            return this.mediaUuid + "____" + this.mediaChangedTrigger;
        }.bind(this), function(current, old) {
            if (this.isMediaCodeValid()) {
                this.fetchMedia();
            } else {
                this.media = {};
                this.thumbnailUrl = "";
                this.otmmAssetId = "";
            }
        }.bind(this));
    })
    .component('seMediaFormat', {
        templateUrl: 'otMediaFormatTemplate.html',
        controller: 'seMediaFormatController',
        controllerAs: 'ctrl',
        bindings: {
            mediaUuid: '=',
            mediaFormat: '=',
            isUnderEdit: '=',
            errors: '=',
            onFileSelect: '&?',
            onDelete: '&?',

            locale: '=',
            attributeId: '=',
            itemId: '=',
            itemCatalogVersion: '=',
            mediaChangedTrigger: '=',
            onAssetAssignmentCompleted: '&',
            pristineMedia: '=',
            editorParameters: '='
        }
    });
