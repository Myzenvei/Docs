import vxDropdownPrimary from '../../../components/common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxMapLocator from '../../../components/common/vx-map-locator/vx-map-locator.vue';
import googleMapsMixin from '../../../components/common/mixins/google-maps-mixin';
import ViewSiteContentService from '../../../components/common/services/view-site-content-service';
import vxSpinner from '../../../components/common/vx-spinner/vx-spinner.vue';
import globals from '../../../components/common/globals';

export default {
  name: 'mg-find-a-store',
  components: {
    vxDropdownPrimary,
    vxMapLocator,
    vxSpinner,
  },
  mixins: [googleMapsMixin],
  props: {
    i18n: {
      type: Object
    }
  },
  data() {
    return {
      globals,
      noStores: true,
      paginate: ['productLinks'],
      storeModel: {
        distanceRange: '',
        zipcode: '',
        productId: ''
      },
      location: {},
      storeLocatorService: new ViewSiteContentService(),
      distanceList: [{
        label: '0-5 miles',
        value: '5',
      },
      {
        label: '6-10 miles',
        value: '10',
      }, {
        label: '10-15 miles',
        value: '15',
      }, {
        label: '15+ miles',
        value: '100',
      }
      ],
      productLinks: [],
      /**
       * :todo need to get from backoffice
       */
      productList: [],
      paginate: ['productLinks'],
      onlineStores: [{
        imgURL: `${globals.assetsPath}images/mardi-gras/logo_retail_amazonfresh.png`,
        navURL: 'https://www.amazon.com/s/ref=sr_nr_p_89_0?fst=as%3Aoff&rh=n%3A3760901%2Cn%3A15342811%2Cn%3A15342841%2Cn%3A15347411%2Ck%3Amardi+gras+napkins%2Cp_89%3AMardi+Gras&keywords=mardi+gras+napkins&ie=UTF8&qid=1471534246&rnid=2528832011',
        imgAlt: 'amazon',
      },
      {
        imgURL: `${globals.assetsPath}images/mardi-gras/logo_retail_jet.png`,
        navURL: 'https://jet.com/search?term=mardi%20gras%20napkins',
        imgAlt: 'jet',
      },
      {
        imgURL: `${globals.assetsPath}images/mardi-gras/logo_retail_walmart.png`,
        navURL: 'https://www.walmart.com/ip/Mardi-Gras-Napkins-250-count-Prints/14224695',
        imgAlt: 'walmart',
      },
      {
        imgURL: `${globals.assetsPath}images/mardi-gras/logo_heb-retailer.png`,
        navURL: 'https://shop.hebtoyou.com/shop/categories/591?brand_name=Mardi%20Gras',
        imgAlt: 'heb',
      }
      ]
    }
  },
  computed: {

  },
  mounted() {
    this.initializeProductService();
    this.$on('loction', (loc) => {
      loc.name = this.location.name;
      loc.address = this.location.address;
      loc.zip = this.location.zip;
      loc.phone = this.location.phone;
      loc.city = this.location.city;
      loc.state = this.location.state;
      this.$refs.storemap.testGeoLocationMarker(loc);
    });
  },
  provide() {
    return {
      getMap: this.$refs.storemap,
    };
  },
  methods: {
    initializeProductService() {
      this.storeLocatorService.getProductList({}, this.handlegetProductIDResponse, this.handleProductsError);
    },
    handlegetProductIDResponse(response) {
      this.manipulateResponse(response.data.gpStoreProducts);
      // console.log(response.data)
    },
    manipulateResponse(data) {
      data.map((value, index) => {
        this.productList.push({
          label: value.description,
          value: value.code
        })
      })
      /**
       * On page load setting the dropdown options to first index
       */
      this.$refs.productTypeDropdown.setDropdownLabel(this.productList[0].label);
      this.$refs.productTypeDropdown.setDropdownValue(this.productList[0].value);
      this.$refs.distanceRangeDropdown.setDropdownLabel(this.distanceList[0].label);
      this.$refs.distanceRangeDropdown.setDropdownValue(this.distanceList[0].value);
      this.storeModel.distanceRange = this.distanceList[0].value;
      this.storeModel.productId = this.productList[0].value;
    },
    searchStores() {
      this.$refs.spinner.showSpinner();
      this.noStores = false;
      this.productLinks = [];
      this.storeLocatorService.store({}, this.handleProductResponse, this.handleProductsError, this.storeModel.productId, this.storeModel.zipcode, this.storeModel.distanceRange);
    },
    handleProductResponse(response) {
      this.productLinks = response.data.RESULTS.STORES ?
        response.data.RESULTS.STORES.STORE : [];
      if (this.productLinks.length) {
        this.location = {
          ...this.location,
          lat: response.data.RESULTS.STORES.STORE[0].LATITUDE,
          lng: response.data.RESULTS.STORES.STORE[0].LONGITUDE,
          name: response.data.RESULTS.STORES.STORE[0].NAME,
          address: response.data.RESULTS.STORES.STORE[0].ADDRESS,
          city: response.data.RESULTS.STORES.STORE[0].CITY,
          state: response.data.RESULTS.STORES.STORE[0].STATE,
          zip: response.data.RESULTS.STORES.STORE[0].ZIP,
          phone: response.data.RESULTS.STORES.STORE[0].PHONE,
        };
        this.mapLoaded();
      } else {
        this.noStores = true;
      }
      this.$refs.spinner.hideSpinner();
    },
    handleProductsError(error) { },
    createFormattedAddress(add, city, state, zip) {
      return `${add},${city},${state},${zip},USA`;
    },
    mapLoaded() {
      if (this.productLinks.length > 0) {
        const self = this;
        const formattedAddress = this.createFormattedAddress(this.location.address,
          this.location.city, this.location.state, this.location.zip);
        this.testAddressZipcodeToGeoLocation(formattedAddress);
        /* Keeping it for future if IRI modifies coordinates
         this.$refs.storemap.testGeoLocationMarker(this.location); */
      } else {
        this.noStores = true;
      }
    },
    storeSelected(selectedStore) {
      this.$nextTick(() => {
        if (this.$refs.storemap) {
          this.location.name = selectedStore.NAME;
          this.location.address = selectedStore.ADDRESS;
          this.location.zip = selectedStore.ZIP;
          this.location.phone = selectedStore.PHONE;
          this.location.city = selectedStore.CITY;
          this.location.state = selectedStore.STATE;
          const formattedAddress = this.createFormattedAddress(selectedStore.ADDRESS,
            selectedStore.CITY, selectedStore.STATE, selectedStore.ZIP);
          this.testAddressZipcodeToGeoLocation(formattedAddress);
          /* Keeping it for future if IRI modifies coordinates
           * this.$refs.storemap.testGeoLocationMarker({
            lat: selectedStore.LATITUDE,
            lng: selectedStore.LONGITUDE,
            name: selectedStore.NAME,
            address: selectedStore.ADDRESS,
          }); */
        }
      })
    }
  }
}
