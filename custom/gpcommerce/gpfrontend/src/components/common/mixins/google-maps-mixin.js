import globals from '../../common/globals';

/* Lint pacification */
/* eslint no-unused-vars: ['error', {'args': 'none'}] */
/* eslint no-console: ['error', { allow: ['log'] }] */

const GoogleMapsLoader = require('google-maps');

const googleMapsMixin = {
  components: {},
  data() {
    return {
      globals,
      map: Object,
      distributors: Object,
      loadedGoogleMapsAPI: Object,
      markers: [],
      alternate: false,
      infoWindow: Object,
      makerDefaultLocation: {
        lat: 33.7570058, // TODO should be coming from backend
        lng: -84.38088, // TODO should be coming from backend
      },
      markerGeoLocation: Object,
      google: '',
    };
  },
  created() {
    // TODO this key should be configurable from backend
    GoogleMapsLoader.KEY = this.globals.googleMapsKey;
    // TODO this options should be coming from constants
    GoogleMapsLoader.LANGUAGE = 'en';
    GoogleMapsLoader.SENSOR = true;
    const mapLocatorObj = this;
    GoogleMapsLoader.load((google) => {
      mapLocatorObj.google = google;
      this.$emit('mapMounted');
    });
  },
  methods: {
    /**
     * Get Geo Location
     * @param  {Function} cb callback function
     */
    geoLocationHandler(cb) {
      const mapLocatorObj = this;
      let positionObj = {};
      // HTML5 geolocation and resetting map to the center
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            mapLocatorObj.markerGeoLocation.lat = position.coords.latitude;
            mapLocatorObj.markerGeoLocation.lng = position.coords.longitude;
            positionObj = new mapLocatorObj.google.maps.LatLng(
              position.coords.latitude,
              position.coords.longitude,
            );
            mapLocatorObj.map.setCenter(positionObj);
            cb(true);
          },
          () => {
            mapLocatorObj.handleNoGeolocation(true);
            cb();
          },
        );
      } else {
        mapLocatorObj.handleNoGeolocation(false);
        cb();
      }
    },

    /**
     * Handle no Geolocation
     * @param  {boolean} errorFlag boolean flag
     */
    handleNoGeolocation(errorFlag) {
      let content;
      if (errorFlag) {
        content = 'Error: The Geolocation service failed.';
      } else {
        content = "Error: Your browser doesn't support geolocation.";
      }
      const options = {
        map: this.map,
        position: new this.google.maps.LatLng(
          this.makerDefaultLocation.lat,
          this.makerDefaultLocation.lng,
        ),
        content,
      };

      // var infowindow = new this.google.maps.InfoWindow(options);
      this.map.setCenter(options.position);
    },

    /**
     * Creates defualt Info window object
     * @param  {Object} data any data to use for infowindow
     * @return {Object}      infoWindow object
     */
    createInfoWindow(data) {
      const infoWindow = new this.google.maps.InfoWindow({
        content: '<div class="info-window">Info Window</div>',
      });
      return infoWindow;
    },

    /**
     * Get Directions from given location
     * @param  {Object} location lat and lng details
     * @return {Object}          directions details
     */
    getDirectionsLink(location) {
      return String.format(
        'https://www.google.com/maps?daddr={0},{1}',
        location.Lat,
        location.Lng,
      );
    },

    /**
     * Returns current Geo location
     * @return {Object} marker geo location data - lat, lng details
     */
    getGeoLocation() {
      return this.markerGeoLocation;
    },

    /**
     * return latitude and longitude based on address
     * @param  {Object}   addressData [description]
     * @param  {requestCallback} callback    [description]
     */
    getLatitudeLongitude(addressData, callback) {
      const self = this;
      // setting defualt location
      const address = addressData || 'U.S.A';
      // Initialize the Geocoder
      const geocoder = new self.google.maps.Geocoder();
      if (geocoder) {
        geocoder.geocode(
          {
            address,
          },
          (results, status) => {
            if (status === self.google.maps.GeocoderStatus.OK) {
              callback(results[0]);
            }
          },
        );
      }
    },

    /**
     * Return pin code based on geolocation
     * @param  {string} zipcode zipcode value
     * @param  {requestCallback} cb callback function
     */
    getLocationFromZipcode(zipcode, cb) {
      this.getLatitudeLongitude(zipcode.toString(), (result) => {
        cb({
          lat: result.geometry.location.lat(),
          lng: result.geometry.location.lng(),
        });
      });
    },

    /**
     * Testing address zipcode to GeoLocation
     * @param  {string} zipcode zipcode value
     */
    testAddressZipcodeToGeoLocation(zipcode) {
      const mapLocatorObj = this;
      mapLocatorObj.getLocationFromZipcode(zipcode, (location) => {
        /** Before this mixin got created we used to call this method directly.
         * mapLocatorObj.testGeoLocationMarker(location); */
        /** To check whether we are getting the address properly or not.
         * console.log('zipcode::', zipcode, '--', location); */
        mapLocatorObj.$emit('loction', location);
      });
    },
  },
};

/**
 * @mixin
 */
export default googleMapsMixin;
