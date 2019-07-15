const storeLocatorMixin = {
  data() {
    return {
      storeLocation: '',
      selectedStore: {
        NAME: '',
        ADDRESS: '',
        PHONE: '',
        img: '',
        STORE_ID: '',
        CITY: '',
        STATE: '',
        ZIP: '',
        URL: '',
      },
      prevIndex: 0,
    };
  },
  methods: {
    storeSelected(store, isDesktop, isStoreSelected, index) {
      this.selectedStore.NAME = store.name;
      this.selectedStore.ADDRESS = store.address.line1;
      this.selectedStore.PHONE = store.address.phone;
      this.selectedStore.img = `https://${store.STORE_LOGO}`;
      this.selectedStore.STORE_ID = store.address.id;
      this.selectedStore.CITY = store.address.town;
      this.selectedStore.STATE = store.address.region ? store.address.region.isocodeShort : '';
      this.selectedStore.ZIP = store.address.postalCode;
      this.selectedStore.URL = store.address.url ? store.address.url : '';
      this.storeLocation = {
        lat: store.geoPoint.latitude,
        lng: store.geoPoint.longitude,
      };

      if (!isDesktop && this.isMobile()) {
        const device = this.getDevice();
        if (device === 'IOS') {
          window.open(`maps://maps.google.com/maps?q=${this.storeLocation.lat},${this.storeLocation.lng}&zoom=14&amp;`);
        } else if (device === 'ANDROID') {
          window.open(
            `https://maps.google.com/?q=${this.selectedStore.ADDRESS}
            &zoom=14&amp;ll=`,
          );
        }
      }
      // this section is executed only when the function is fired from vx-store-list
      if (isStoreSelected) {
        this.isListMounted = false;
        if (index !== 0) {
          (this.$refs.store0)[0].classList.remove('active');
        }
        // In Below Condition, emitting will be restricted on clicking the active store again
        if (this.prevIndex !== index) {
          this.$emit('storeLocation', this.storeLocation);
          this.$emit('selectedStore', this.selectedStore);
          this.prevIndex = index;
        }
      }
    },
  },
};

export default storeLocatorMixin;
