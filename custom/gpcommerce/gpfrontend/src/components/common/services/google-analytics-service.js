class GoogleAnalyticsService {
  /* global dataLayer */
  /**
   * trackSearchResults function
   * @param  {object}   dataObj contains search term and total result
   * @return {object}          null
   */
  trackSearchResults = (dataObj) => {
    const ecommerceObject = {
      event: 'searchResults',
      ecommerce: {
        search: dataObj,
      },
    };

    dataLayer.push(ecommerceObject);
  }

  /**
   * trackProductsPurchased function
   * @param  {object}   dataObj contains details of product purchased
   * @return {object}          null
   */
  trackProductsPurchased = (dataObj) => {
    const ecommerceObject = {
      event: 'purchase',
      ecommerce: {
        purchase: dataObj,
      },
    };

    dataLayer.push(ecommerceObject);
  }

  /**
   * trackImpressions function
   * @param  {object}   dataObj contains product details for PDP
   * @return {object}          null
   */
  trackImpressions = (dataObj, currencyCode) => {
    const ecommerceObject = {
      event: 'productImpressions',
      ecommerce: {
        currencyCode,
        impressions: dataObj,
      },
    };

    dataLayer.push(ecommerceObject);
  }

  /**
   * trackDetailImpressions function
   * @param  {object}   dataObj contains product details for PDP
   * @return {object}          null
   */
  trackDetailImpressions = (dataObj) => {
    const ecommerceObject = {
      event: 'productDetail',
      ecommerce: {
        detail: {
          actionField: {
            list: '',
          },
          products: dataObj,
        },
      },
    };

    dataLayer.push(ecommerceObject);
  }

  /**
   * trackAddToCart function
   * @param  {object}   dataObj contains details of product added to cart
   * @return {object}          null
   */
  trackAddToCart = (dataObj) => {
    const ecommerceObject = {
      event: 'addToCart',
      ecommerce: {
        add: dataObj,
      },
    };

    dataLayer.push(ecommerceObject);
  }

  /**
   * trackRemoveFromCart function
   * @param  {object}   dataObj contains details of product removed from cart
   * @return {object}          null
   */
  trackRemoveFromCart = (dataObj) => {
    const ecommerceObject = {
      event: 'removeFromCart',
      ecommerce: {
        remove: dataObj,
      },
    };

    dataLayer.push(ecommerceObject);
  }

  /**
   * trackCheckout function
   * @param  {object}   dataObj contains details of product during checkout
   * @return {object}          null
   */
  trackCheckout = (dataObj) => {
    if (dataObj) {
      const ecommerceObject = {
        event: 'checkout',
        ecommerce: {
          checkout: {
            actionField: {
              step: 1,
              option: 'Checkout Initialized',
            },
            products: dataObj,
          },
        },
      };

      dataLayer.push(ecommerceObject);
    }
  }
}

export default GoogleAnalyticsService;
