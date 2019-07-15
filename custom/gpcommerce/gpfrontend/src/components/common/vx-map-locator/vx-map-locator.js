/* Map locator js handlers */

/* Lint pacification */
/* eslint no-unused-vars: ['error', {'args': 'none'}] */
/* eslint no-console: ['error', { allow: ['log'] }] */

import googleMapsMixin from '../../common/mixins/google-maps-mixin';
import globals from '../../common/globals';

/**
 * Map locator
 */
export default {
  name: 'vx-map-locator',
  components: {},
  mixins: [googleMapsMixin],
  props: {
    mapOnPageLoad: {
      type: Boolean,
      default: false,
    },
    mapTheme: {
      type: Array,
      default() {
        return [
          {
            hue: '#2D5B99',
          },
          {
            gamma: 0.5,
          },
          {
            weight: 0.5,
          },
        ];
      },
    },
    iconConfig: {
      type: Object,
      default() {
        return {
          pinNumber: '1',
          pinColor: '009400',
        };
      },
    },
  },
  data() {
    return {};
  },
  computed: {},
  created() {},
  mounted() {
    this.$on('mapMounted', () => {
      if (this.mapOnPageLoad) {
        this.initMap(this.google);
      } else {
        this.initMap(this.google);
        this.$emit('mapLoaded');
        this.mapOnPageLoad = true;
      }
    });
    if (!this.mapOnPageLoad) {
      this.initMap(this.google);
      this.$emit('mapLoaded');
    }
  },
  methods: {
    /**
     * Intiazile Google maps
     * @param  {Object} google google maps object
     * @param  {requestCallback} cb callback function
     */
    initMap(google, cb) {
      // const mapLocatorObj = this;
      const featureOpts = [{
        stylers: this.mapTheme,
      },];

      const mapOptions = {
        center: {
          lat: this.makerDefaultLocation.lat,
          lng: this.makerDefaultLocation.lng,
        },
        zoom: 15,
        zoomControl: true,
        minZoom: 1,
        scrollwheel: false,
        // mapTypeId: google.maps.MapTypeId.ROADMAP
      };

      this.map = new google.maps.Map(
        document.getElementById('map-canvas'),
        mapOptions,
      );

      this.map.setOptions({
        styles: featureOpts,
      });

      /* Create Info window (since we are not showing any info window)
      mapLocatorObj.infoWindow = mapLocatorObj.createInfoWindow();
      mapLocatorObj.geoLocationHandler(function(isGeoEnabled) {
        TODO TESTING GEOLOCATION MARKER
        TODO REMOVE this Invocation
        mapLocatorObj.testGeoLocationMarker();
        mapLocatorObj.testAddressZipcodeToGeoLocation('4500 PRINCESS ANN RD');
        if (cb) {
          cb({
            mapCreated: true,
            isGeoEnabled: isGeoEnabled,
          });
        }
      });
      listen for click in the map and close infoboxes
      google.maps.event.addListener(this.map, 'click', function(e) {
        mapLocatorObj.infoWindow.close();
      }); */
    },

    /**
     * Test GeoLocation Marker location
     * @param  {Object} loc contains latitude and longitude values
     */
    testGeoLocationMarker(loc) {
      const mapLocatorObj = this;
      const location = loc || mapLocatorObj.location;

      const sampleMarker = mapLocatorObj.createMarker(
        Math.random().toString(),
        location.lat,
        location.lng,
        `<h2 class="store-name">${location.name}</h2><br><span class="store-address">${location.address} ${location.city} ${location.state}${location.zip} ${location.phone}</span>`,
      );
      this.markers.push(sampleMarker);
    },

    /**
     * Create makers
     * @param  {String} title title value
     * @param  {Number} lat   latitude value
     * @param  {Number} lng   longitude value
     * @param  {String} html  html content
     * @return {Object} retruns marker object
     */
    createMarker(title, lat, lng, _html) {
      this.clearMap();
      const mapLocatorObj = this;
      // TODO edit default Maker content
      // let defaultHTML = html || '<h1>Marker Head</h1><p>Marker Content</p>';
      const position = new mapLocatorObj.google.maps.LatLng(lat, lng);
      mapLocatorObj.map.setCenter(position);
      mapLocatorObj.map.setZoom(17);
      mapLocatorObj.map.panTo(position);
      const marker = new mapLocatorObj.google.maps.Marker({
        map: mapLocatorObj.map,
        position,
        title,
        animation: mapLocatorObj.google.maps.Animation.DROP,
        icon: `//chart.apis.google.com/chart?chst=d_map_pin_letter&chld=${this.iconConfig.pinNumber}|${this.iconConfig.pinColor}|ffffff`,
      });

      const infowindow = new mapLocatorObj.google.maps.InfoWindow({
        content: _html,
      });
      marker.addListener('click', () => {
        if (!globals.isMardigras()) {
          this.$emit('markerClick');
        }
        else {
          infowindow.open(mapLocatorObj.map, marker);
        }
      });
      //customization of infowindow
      mapLocatorObj.google.maps.event.addListener(infowindow, 'domready', () => {
        const infoElement = $('.gm-style-iw').prev();
        const boxWrapper = infoElement[0].childNodes[1];
        const boxContainer = infoElement[0].childNodes[3];
        const arrowWrapperLeft = infoElement[0].childNodes[2].childNodes[0].childNodes[0];
        const arrowWrapperRight = infoElement[0].childNodes[2].childNodes[1].childNodes[0];
        const infoButton = $('.gm-style-iw').next('button');
        const btnIcon = infoButton[0].childNodes[0];
        //:todo need to pass these as props if we require this in white label

        $(boxWrapper).css({
          borderRadius: 8,
        });
        $(boxContainer).css({
          border: '1px solid #888', // if you wanna override new border
          borderRadius: 8,
          boxShadow: 'none',
        });
        $(arrowWrapperLeft).css({
          borderLeft: '1px solid #888',
          zIndex: 9,
          top: 2,
          borderTop: '1px solid #fff',
          boxShadow: 'none',
        });
        $(arrowWrapperRight).css({
          borderRight: '1px solid #888',
          zIndex: 9,
          top: 2,
          borderTop: '1px solid #fff',
          boxShadow: 'none',
        });
        infoButton.css({
          opacity: 1
        });
        $(btnIcon).attr('src', 'data:image/svg+xml;base64,PHN2ZyBoZWlnaHQ9IjE0cHgiIHdpZHRoPSIxNHB4IiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB2ZXJzaW9uPSIxLjEiPjxwYXRoIGQ9Ik03LDBDMy4xMzQsMCwwLDMuMTM0LDAsN2MwLDMuODY3LDMuMTM0LDcsNyw3YzMuODY3LDAsNy0zLjEzMyw3LTdDMTQsMy4xMzQsMTAuODY3LDAsNywweiBNMTAuNSw5LjVsLTEsMUw3LDhsLTIuNSwyLjVsLTEtMUw2LDdMMy41LDQuNWwxLTFMNyw2bDIuNS0yLjVsMSwxTDgsN0wxMC41LDkuNXoiLz48L3N2Zz4=');
        // whatever you want to do once the DOM is ready
      });
      // Seeting up marker conten

      return marker;
    },

    /**
     * Display markers based on data array
     * @param  {Object}   data data Object
     * @param  {Function} cb   [description]
     */
    displayMarkers(data, cb) {
      // const markers = [];
      // Clear existing markers
      this.clearMap();
      // const bounds = new this.google.maps.LatLngBounds();
      // Loop through multiple locations and invoke markers function
      if (cb) {
        cb();
      }
    },

    /**
     * Clears all map markers
     */
    clearMap() {
      // TODO Clear Map Markers
      for (let i = 0; i < this.markers.length; i += 1) {
        this.markers[i].setMap(null);
      }
      this.markers = [];
    },
  },
};
