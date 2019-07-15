angular.module('otMediaServiceModule', ['resourceLocationsModule', 'ngResource'])

    .factory('otMediaResourceService', function($resource, MEDIA_RESOURCE_URI) {

        var getMediaByCode = function(mediaCode) {
            return $resource(MEDIA_RESOURCE_URI + "/" + mediaCode, {}, {
                get: {
                    method: 'GET',
                    cache: false,
                    headers: {}
                }
            });
        };

        return {
            getMediaByCode: getMediaByCode
        };
    })

    .factory('otMediaService', function(otMediaResourceService) {
        var getMediaByCode = function(code) {
            return otMediaResourceService.getMediaByCode(code).get().$promise;
        };

        return {
            getMediaByCode: getMediaByCode
        };
    });
