/*
 */
angular.module('otUiConfigurationServiceModule', ['ngResource', 'eventServiceModule'])
    .factory('otUiConfigurationService', function($q, $log, $resource, systemEventService) {
        var thisService = {


            _hybrisSystemId: null,
            _configurationId: null,
            _uiConfiguration: {
                isReplaceAllVisible: false
            },

            _getUiConfigStartParams: function(itemPk, attributeId) {

                var deferred = $q.defer();

                $resource("/otmmws/damlink-ui/uiconfiguration?itemPk=" + itemPk + "&attributeId=" + attributeId).get(function(response) {

                    deferred.resolve(response);
                }, function(error) {
                    deferred.reject(error);
                });

                return deferred.promise;
            },

            _getItemPk: function(itemCatalogVersion, itemId) {

                var deferred = $q.defer();

                $resource("/otmmws/damlink-ui/catalogversion/" + encodeURI(itemCatalogVersion) + "/item/" + encodeURI(itemId) + "/itempk").get(
                    function(response) {
                        deferred.resolve(response);
                    },
                    function(error) {
                        deferred.reject(error);
                    });

                return deferred.promise;
            },

            _getUiConfiguration: function(configurationId) {

                var deferred = $q.defer();

                $resource("/otmmws/otmmProxy/ot-damlink/api/config/" + thisService._hybrisSystemId + "/" + configurationId).get(function(response) {

                    deferred.resolve(response);
                }, function(error) {
                    deferred.reject(error);
                });

                return deferred.promise;
            },

            initialize: function(itemCatalogVersion, itemId, attributeId) {
                var deferred = $q.defer();

                if (angular.isString(itemCatalogVersion) && itemCatalogVersion.length > 0 && angular.isString(itemId) && itemId.length > 0) {

                    thisService._getItemPk(itemCatalogVersion, itemId).then(function(response) {

                        var itemPk = response.itemPk;

                        thisService._initialize2(itemPk, attributeId).then(function(itemPk) {
                            deferred.resolve(itemPk);
                        });
                    }, function(error) {
                        deferred.reject(error);
                    });
                } else {
                    thisService._initialize2("", attributeId).then(function(itemPk) {
                        deferred.resolve(itemPk);
                    });
                }
                return deferred.promise;
            },

            _initialize2: function(itemPk, attributeId) {
                var deferred = $q.defer();

                thisService._getUiConfigStartParams(itemPk, attributeId).then(function(uiConfigParams) {

                    thisService._hybrisSystemId = uiConfigParams.hybrisSystemId;
                    thisService._configurationId = uiConfigParams.configurationId;

                    thisService._getUiConfiguration("defaultConfig").then(function(defaultConfigResult) {
                        thisService._mergePropertiesIntoUiConfigIfPossible(defaultConfigResult, "defaultConfig");

                        thisService._getUiConfiguration(thisService._configurationId).then(function(configIdResult) {
                            thisService._mergePropertiesIntoUiConfigIfPossible(configIdResult, thisService._configurationId);

                            // angular.forEach(thisService._uiConfiguration, function (value, key) {
                            //     console.log("+++ " + key + "=" + value);
                            // });

                            deferred.resolve(itemPk);
                        }, function(error) {
                            // Ignore error and go on
                            deferred.resolve(itemPk);
                        });

                    }, function(error) {
                        // Ignore error and go on
                        deferred.resolve(itemPk);
                    });
                }, function(error) {
                    // Ignore error and go on
                    deferred.resolve(itemPk);
                });
                return deferred.promise;
            },

            getProperty: function(propertyKey) {
                var propertyValue = thisService._uiConfiguration[propertyKey];
                return propertyValue !== undefined ? propertyValue : null;
            },

            getBoolean: function(propertyKey) {
                var value = thisService.getProperty(propertyKey);
                if (angular.isString(value)) {
                    if ("true" === value) {
                        return true;
                    } else if ("false" === value) {
                        return false;
                    }
                } else if (value === true || value === false) {
                    return value;
                } else {
                    return null;
                }
            },

            getHybrisSystemId: function() {
                return thisService._hybrisSystemId;
            },

            getConfigurationId: function() {
                return thisService._configurationId;
            },

            _mergePropertiesIntoUiConfigIfPossible: function(restCallResult, configId) {
                if (angular.isObject(restCallResult[thisService._hybrisSystemId]) && angular.isObject(restCallResult[thisService._hybrisSystemId][configId])) {
                    thisService._mergePropertiesIntoUiConfig(restCallResult[thisService._hybrisSystemId][configId]);
                }
            },

            _mergePropertiesIntoUiConfig: function(keyValuePairs) {
                angular.forEach(keyValuePairs, function(value, key) {
                    if (value !== null && value !== "") {
                        var finalValue = value;

                        if (value.indexOf("[") === 0) {
                            var tmpValue = JSON.parse(value);
                            if (angular.isArray(tmpValue)) {
                                finalValue = tmpValue;
                            }
                        }

                        thisService._uiConfiguration[key] = finalValue;
                    }
                });
            }
        };

        return thisService;
    });
