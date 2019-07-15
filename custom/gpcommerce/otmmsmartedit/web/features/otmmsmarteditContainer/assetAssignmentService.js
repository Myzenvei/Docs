/*
 */
angular.module('assetAssignmentModule', ['ngResource', 'eventServiceModule'])
    .factory('assetAssignmentService', function($q, $log, $resource, systemEventService) {
        var thisService = {

            pristineMedia: null,
            dummyMedia: {},

            isRegisteredForEvents: false,

            performAssetAssignment: function(assignmentUiData, context) {

                var deferred = $q.defer();

                var imageCropParameters = null;
                if (assignmentUiData.imageCropParameters !== undefined && assignmentUiData.imageCropParameters !== null) {
                    imageCropParameters = assignmentUiData.imageCropParameters;
                }

                var assignmentInfo = {
                    assets: assignmentUiData.selectedAssets,
                    itemPk: context.itemPk,
                    attributeInfo: {
                        id: context.attributeId,
                        locale: context.locale
                    },
                    mediaFormat: context.mediaFormat,
                    assignmentLabel: assignmentUiData.assignmentLabel,
                    automaticallyUpdateToLatestAssetVersion: assignmentUiData.automaticallyUpdateToLatestAssetVersion,
                    selectedAssetDeliveryPk: "",
                    imageCropParameters: imageCropParameters
                };

                thisService._getAndSavePristineMedia(context.itemPk, context.attributeId, context.locale).then(function() {

                    $resource("/otmmws/damlink-ui/assetassignment").save(assignmentInfo, function(response) {
                        thisService._registerForEvents();
                        deferred.resolve(response);
                    }, function(error) {
                        thisService._registerForEvents();
                        deferred.reject(error);
                    });
                });

                return deferred.promise;
            },

            setDummyMediaToBeDeleted: function(dummyMedia) {
                thisService.dummyMedia = dummyMedia;
            },

            createDummyMedia: function(catalogId, catalogVersion) {

                var deferred = $q.defer();

                thisService._createDummyMediaRest(catalogId, catalogVersion).then(function(result) {
                    thisService._searchMediaByCodeCatalogVersion(result.code, catalogId, catalogVersion).then(function(result) {
                        if (angular.isArray(result.media) && result.media.length === 1) {
                            var dummyMedia = result.media[0];
                            deferred.resolve(dummyMedia);
                        } else {
                            deferred.reject("Wrong result of _searchMediaByCodeCatalogVersion.");
                        }
                    }.bind(this), function(error) {
                        deferred.reject(error);
                    });
                }.bind(this), function(error) {
                    deferred.reject(error);
                });

                return deferred.promise;
            },

            _createDummyMediaRest: function(catalogId, catalogVersion) {

                var deferred = $q.defer();

                var dto = {
                    catalogId: catalogId,
                    catalogVersion: catalogVersion
                };

                if (angular.isString(dto.catalogId) && dto.catalogId.length > 0 && angular.isString(dto.catalogVersion) && dto.catalogVersion.length > 0) {

                    $resource("/otmmws/damlink-ui/dummymedia").save(dto,
                        function(response) {
                            deferred.resolve(response);
                        },
                        function(error) {
                            deferred.reject("Failed to create dummy media");
                        });
                } else {
                    deferred.reject("No catalog version");
                }

                return deferred.promise;
            },

            _searchMediaByCodeCatalogVersion: function(code, catalogId, catalogVersion) {

                var deferred = $q.defer();

                var params = encodeURIComponent("code:" + code + ",catalogId:" + catalogId + ",catalogVersion:" + catalogVersion);

                $resource("/cmswebservices/v1/media?namedQuery=otmmMediaByCodeCatalogVersion&params=" +
                    params + "&pageSize=5&currentPage=0").get(
                    function(response) {
                        deferred.resolve(response);
                    },
                    function(error) {
                        deferred.reject("Failed to retrieve dummy media uuid.");
                    });


                return deferred.promise;
            },

            getOpenTextMedia: function(code, catalogId, catalogVersion) {

                var deferred = $q.defer();

                $resource("/otmmws/damlink-ui/catalogversion/" + encodeURIComponent(catalogId) + "/" + encodeURIComponent(catalogVersion) + "/opentextmedia?code=" + encodeURIComponent(code)).get(
                    function(response) {
                        deferred.resolve(response);
                    },
                    function(error) {
                        deferred.reject("Failed to retrieve OpenTextMedia.");
                    });

                return deferred.promise;
            },

            _deleteDummyMedia: function() {

                if (angular.isString(thisService.dummyMedia.catalogId) && thisService.dummyMedia.catalogId.length > 0 &&
                    angular.isString(thisService.dummyMedia.catalogVersion) && thisService.dummyMedia.catalogVersion.length > 0 &&
                    angular.isString(thisService.dummyMedia.code) && thisService.dummyMedia.code.length > 0) {

                    $resource("/otmmws/damlink-ui/dummymedia").delete(this.dummyMedia,
                        function(response) {
                            thisService.dummyMedia = {};
                        },
                        function(error) {
                            thisService.dummyMedia = {};
                        });
                }
            },


            _getAndSavePristineMedia: function(itemPk, attributeId, locale) {

                var deferred = $q.defer();

                var hasPristineForThisLocaleAlreadyLoaded = false;
                if (thisService.pristineMedia !== null) {
                    hasPristineForThisLocaleAlreadyLoaded = thisService.pristineMedia.localizedAttributes.find(function(localizedAttribute) {
                        return localizedAttribute.locale === locale;
                    }) !== undefined;
                }

                if (hasPristineForThisLocaleAlreadyLoaded) {
                    deferred.resolve();
                } else {
                    $resource("/otmmws/damlink-ui/item/" + itemPk + "/attribute/" + attributeId + "/locale/" + locale + "/cancelassignmentdata").get(function(response) {
                        if (thisService.pristineMedia === null) {

                            thisService.pristineMedia = {
                                attributeId: response.attributeId,
                                itemPk: response.itemPk,
                                localizedAttributes: response.localizedAttributes
                            };
                        } else {
                            thisService.pristineMedia.localizedAttributes.push(response.localizedAttributes[0]);
                        }
                        deferred.resolve();
                    }, function(error) {
                        deferred.reject(error);
                    });
                }

                return deferred.promise;
            },

            _registerForEvents: function() {
                if (!thisService.isRegisteredForEvents) {
                    systemEventService.registerEventHandler("sm-editor-save", thisService._onSave);
                    systemEventService.registerEventHandler("sm-editor-cancel", thisService._onCancel);
                    thisService.isRegisteredForEvents = true;
                }
            },

            _unRegisterForEvents: function() {
                if (thisService.isRegisteredForEvents) {
                    systemEventService.unRegisterEventHandler("sm-editor-save", thisService._onSave);
                    systemEventService.unRegisterEventHandler("sm-editor-cancel", thisService._onCancel);
                    thisService.isRegisteredForEvents = false;
                }
            },

            _onSave: function(event, result) {
                thisService._unRegisterForEvents();
                thisService.pristineMedia = null;

                thisService._deleteDummyMedia();
            },

            _onCancel: function(event, result) {
                thisService._unRegisterForEvents();
                thisService._cancelAssignment().then(function(response) {
                    thisService.pristineMedia = null;
                });

            },

            _cancelAssignment: function() {
                var deferred = $q.defer();

                $resource("/otmmws/damlink-ui/cancelassetassignment").save(thisService.pristineMedia, function(response) {
                    deferred.resolve(response);
                }, function(error) {
                    deferred.reject(error);
                });

                return deferred.promise;
            }
        };

        return thisService;
    });
