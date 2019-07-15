/*
 *
 */
angular.module('seMediaContainerFieldModule', [
        'seMediaUploadFormModule',
        'seMediaFormatModule',
        'seErrorsListModule',
        'seFileValidationServiceModule',
        'otSeFileSelectorModule',
        'cmsSmarteditServicesModule',
        'otUiConfigurationServiceModule',
        'cmsitemsRestServiceModule',
        'otmmsmarteditContainerTemplates'
    ])
    .constant('seMediaContainerFieldConstants', {
        I18N_KEYS: {
            REPLACE_ALL: 'otmmsmartedit.replace.all'
        },
        REPLACE_ICON_URL: '/images/replace_image_small.png'
    })
    .controller('seMediaContainerFieldController', function(seMediaContainerFieldConstants, seFileValidationService,
        assetsService, otUiConfigurationService,
        cmsitemsRestService) {
        this.image = {};
        this.fileErrors = [];

        this.i18nKeys = seMediaContainerFieldConstants.I18N_KEYS;

        var assetsRoot = assetsService.getAssetsRoot();
        this.replaceIconUrl = assetsRoot + seMediaContainerFieldConstants.REPLACE_ICON_URL;

        this.fileSelected = function(files, format) {
            var previousFormat = this.image.format;
            this.resetImage();

            if (files.length === 1) {
                seFileValidationService.validate(files[0], this.fileErrors).then(function() {
                    this.image = {
                        file: files[0],
                        format: format || previousFormat
                    };
                }.bind(this));
            }
        };

        this.mediaChangedTrigger = 0;
        this.onAssetAssignmentCompleted = function() {

            var cmsitemUuid = this.editor.component.uuid;

            cmsitemsRestService.getById(cmsitemUuid).then(function(cmsitem) {

                this.model[this.qualifier] = cmsitem.media[this.qualifier];

                this.editor.component.media = cmsitem.media;

                // SAPDC-2119: Get SAVE button activated with dummy value
                this.editor.component.otdummy = "otdummyvalue";

                // Increase trigger in order to reload all medias
                // even though media code hasn't been changed
                this.mediaChangedTrigger++;

            }.bind(this));
        };

        this.resetImage = function() {
            this.fileErrors = [];
            this.image = {};
        };

        this.imageUploaded = function(uuid) {
            if (this.model && this.model[this.qualifier]) {
                this.model[this.qualifier][this.image.format] = uuid;
            } else {
                this.model[this.qualifier] = {};
                this.model[this.qualifier][this.image.format] = uuid;
            }

            this.resetImage();
        };

        this.imageDeleted = function(format) {
            delete this.model[this.qualifier][format];
        };

        this.isFormatUnderEdit = function(format) {
            return format === this.image.format;
        };

        this.isReplaceAllVisible = false;

        this.$onInit = function() {
            otUiConfigurationService.initialize(this.editor.component.catalogVersion, this.editor.component.uid, this.field.qualifier).then(function() {
                this.isReplaceAllVisible = otUiConfigurationService.getBoolean("isReplaceAllVisible");
            }.bind(this));
        };
    })
    .directive('seMediaContainerField', function() {
        return {
            templateUrl: 'otMediaContainerFieldTemplate.html',
            restrict: 'E',
            controller: 'seMediaContainerFieldController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                editor: '=',
                qualifier: '='
            }
        };
    });
