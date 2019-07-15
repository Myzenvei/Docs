/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
angular.module('otMediaSelectorModule', [
        'ngResource',
        'seMediaAdvancedPropertiesModule',
        'mediaServiceModule',
        'searchMediaHandlerServiceModule',
        'seMediaPrinterModule',
        'otSeFileSelectorModule',
        'cmsSmarteditServicesModule',
        'resourceModule',
        'resourceLocationsModule',
        'cmsitemsRestServiceModule',
        'assetAssignmentModule'
    ])
    .constant('otSeMediaSelectorConstants', {
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
    .controller('otMediaSelectorController', function($q, $resource, mediaService, searchMediaHandlerService,
        otSeMediaSelectorConstants, assetsService, itemsResource,
        ITEMS_RESOURCE_URI, cmsitemsRestService,
        assetAssignmentService) {

        this.i18nKeys = otSeMediaSelectorConstants.I18N_KEYS;

        var assetsRoot = assetsService.getAssetsRoot();
        this.uploadIconUrl = assetsRoot + otSeMediaSelectorConstants.UPLOAD_ICON_URL;
        this.uploadIconDisabledUrl = assetsRoot + otSeMediaSelectorConstants.UPLOAD_ICON_DIS_URL;

        this.deleteIconUrl = assetsRoot + otSeMediaSelectorConstants.DELETE_ICON_URL;
        this.replaceIconUrl = assetsRoot + otSeMediaSelectorConstants.REPLACE_ICON_URL;
        this.advInfoIconUrl = assetsRoot + otSeMediaSelectorConstants.ADV_INFO_ICON_URL;

        this.media = {};
        this.thumbnailUrl = "";
        this.otmmAssetId = "";

        this.fetchMedia = function() {
            this.thumbnailUrl = "";
            this.otmmAssetId = "";

            var mediaUuid = this.model[this.qualifier];
            if (angular.isString(mediaUuid) && mediaUuid.length > 0) {
                mediaService.getMedia(mediaUuid).then(function(val) {
                    this.media = val;

                    var catalogId = this.editor.parameters.catalogId;
                    var catalogVersion = this.editor.parameters.catalogVersion;
                    var code = this.media.code;

                    assetAssignmentService.getOpenTextMedia(code, catalogId, catalogVersion).then(function(openTextMedia) {
                        if (angular.isString(openTextMedia.otmmThumbnailUrl) && openTextMedia.otmmThumbnailUrl.length > 0) {
                            this.thumbnailUrl = openTextMedia.otmmThumbnailUrl;
                        }
                        if (angular.isString(openTextMedia.otmmAssetId) && openTextMedia.otmmAssetId.length > 0) {
                            this.otmmAssetId = openTextMedia.otmmAssetId;
                        }
                        if (this.thumbnailUrl.length < 1) {
                            this.thumbnailUrl = this.media.url;
                        }
                        this.saveDummyMedia();
                    }.bind(this));
                }.bind(this));
            }
        };

        this.onRemoveMediaButtonClicked = function() {

            if (!this.isDummyMedia()) {

                // Here we have a timing problem.
                // If you save directly (without timeout) then SmartEdit sends data directly to
                // hybris server via PUT request.
                // But please don't ask me why this happens.
                setTimeout(function() {
                    delete this.model[this.qualifier];
                }.bind(this), 100);

                // And why the hell does it work without calling fetchMedia() ????
                // this.fetchMedia();
            }
        }.bind(this);

        this.isDummyMedia = function() {
            return angular.isString(this.media.code) && this.media.code.indexOf("otmmDummyMedia_") === 0;
        };

        this.onAssetAssignmentCompleted = function() {

            var cmsitemUuid = this.editor.component.uuid;

            cmsitemsRestService.getById(cmsitemUuid).then(function(cmsitem) {
                this.model[this.qualifier] = cmsitem.media[this.qualifier];
                this.fetchMedia();

                // SAPDC-2119: Get SAVE button activated with dummy value
                this.editor.component.otdummy = "otdummyvalue";

            }.bind(this));
        };

        // this.createDummyMediaRest = function () {
        //
        //     var deferred = $q.defer();
        //
        //     var dto = {
        //         catalogId: this.editor.parameters.catalogId,
        //         catalogVersion: this.editor.parameters.catalogVersion
        //     };
        //
        //     if (angular.isString(dto.catalogId) && dto.catalogId.length > 0 && angular.isString(dto.catalogVersion) && dto.catalogVersion.length > 0) {
        //
        //         $resource("/otmmws/damlink-ui/dummymedia").save(dto,
        //             function (response) {
        //                 deferred.resolve(response);
        //             }, function (error) {
        //                 deferred.reject("Failed to create dummy media");
        //             });
        //     } else {
        //         deferred.reject("No catalog version");
        //     }
        //
        //     return deferred.promise;
        // }.bind(this);

        // this.searchMediaByCodeCatalogVersion = function (code) {
        //     var deferred = $q.defer();
        //
        //     var catalogId = this.editor.parameters.catalogId;
        //     var catalogVersion = this.editor.parameters.catalogVersion;
        //     var params = encodeURIComponent("code:" + code + ",catalogId:" + catalogId + ",catalogVersion:" + catalogVersion);
        //
        //     $resource("/cmswebservices/v1/media?namedQuery=otmmMediaByCodeCatalogVersion&params=" +
        //         params + "&pageSize=5&currentPage=0").get(
        //         function (response) {
        //             deferred.resolve(response);
        //         }, function (error) {
        //             deferred.reject("Failed to create dummy media");
        //         });
        //
        //
        //     return deferred.promise;
        // }.bind(this);


        this.createDummyMedia = function() {

            if (angular.equals(this.model, {}) && this.qualifier === "en") {

                var catalogId = this.editor.parameters.catalogId;
                var catalogVersion = this.editor.parameters.catalogVersion;

                assetAssignmentService.createDummyMedia(catalogId, catalogVersion).then(function(dummyMedia) {

                    this.model[this.qualifier] = dummyMedia.uuid;
                    this.fetchMedia();

                }.bind(this), function(error) {
                    console.log(error);
                });
            }
        }.bind(this);

        this.saveDummyMedia = function() {

            if (this.isDummyMedia()) {
                var dummyMedia = {
                    catalogId: this.editor.parameters.catalogId,
                    catalogVersion: this.editor.parameters.catalogVersion,
                    code: this.media.code
                };
                assetAssignmentService.setDummyMediaToBeDeleted(dummyMedia);
            }
        }.bind(this);

        this.$onInit = function() {
            this.fetchMedia();
            this.createDummyMedia();
        };
    })
    .directive('otMediaSelector', function() {
        return {
            templateUrl: 'otMediaSelectorTemplate.html',
            restrict: 'E',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                editor: '=',
                qualifier: '=',
                deleteIcon: '=',
                replaceIcon: '=',
                advInfoIcon: '='
            },
            controller: 'otMediaSelectorController',
            controllerAs: 'ctrl'
        };
    });
