import GoogleAnalyticsService from './google-analytics-service';

const googleAnalyticsService = new GoogleAnalyticsService();

class AnalyticsService {
  /**
   * trackImpressions function
   * @param  {object}   dataObj contains product details for PDP
   * @return {object}          null
   */
  trackImpressions = (dataObj) => {
    let impressions = [];
    let currencyCode = '';

    if (dataObj && dataObj.price) {
      currencyCode = dataObj.price.currencyIso; // Local currency
      impressions = [
        {
          name: dataObj.name,
          id: dataObj.code,
          price: dataObj.price.value,
          brand: '',
          category: '',
          variant: '',
          list: '',
          position: '',
        },
      ];
      googleAnalyticsService.trackImpressions(impressions, currencyCode);
    }
  };

  /**
   * trackDetailImpressions function
   * @param  {object}   dataObj contains product details for PDP
   * @return {object}          null
   */
  trackDetailImpressions = (dataObj) => {
    let impressions = [];

    if (dataObj && dataObj.price) {
      impressions = [
        {
          name: dataObj.name,
          id: dataObj.code,
          price: dataObj.price.value,
          brand: '',
          category: '',
          variant: '',
        },
      ];
      googleAnalyticsService.trackDetailImpressions(impressions);
    }
  };

  /**
   * trackSearchResults function
   * @param  {object}   dataObj contains search term and total result
   * @return {object}          null
   */
  trackSearchResults = (dataObj) => {
    let productsSearched = {};

    if (dataObj && dataObj.pagination) {
      productsSearched = {
        searchTerm: dataObj.freeTextSearch,
        totalSearchResults: dataObj.pagination.totalResults,
      };
      googleAnalyticsService.trackSearchResults(productsSearched);
    }
  };

  /**
   * trackAddToCart function
   * @param  {object}   dataObj contains details of product added to cart
   * @return {object}          null
   */
  trackAddToCart = (dataObj) => {
    let productAdded = {};

    if (dataObj) {
      productAdded = {
        products: [
          {
            id: dataObj.code,
            name: dataObj.name,
            quantity: dataObj.quantity,
          },
        ],
      };
      googleAnalyticsService.trackAddToCart(productAdded);
    }
  };

  /**
   * trackRemoveFromCart function
   * @param  {object}   dataObj contains details of product removed from cart
   * @return {object}          null
   */
  trackRemoveFromCart = (dataObj) => {
    let productRemoved = {};

    if (dataObj) {
      productRemoved = {
        products: [
          {
            id: dataObj.code,
            name: dataObj.name,
            quantity: dataObj.quantity,
          },
        ],
      };
      googleAnalyticsService.trackRemoveFromCart(productRemoved);
    }
  };

  /**
   * trackProductPurchased function
   * @param  {object}   transactionObj contains details of the transaction
   * @param  {object}   dataObj contains details of product purchased
   * @return {object}          null
   */
  trackProductPurchased = (dataObj) => {
    let productPurchased = {};
    const products = [];
    const orderPromotions = [];

    if (dataObj) {
      if (dataObj.appliedOrderPromotions) {
        dataObj.appliedOrderPromotions.forEach((promotion) => {
          promotion.appliedCouponCodes.forEach((coupon) => {
            orderPromotions.push(coupon);
          });
        });
      }
      dataObj.entries.forEach((product) => {
        const productPromotions = [];
        if (dataObj.appliedProductPromotions) {
          dataObj.appliedProductPromotions.forEach((promotion) => {
            productPromotions.push(promotion.promotion.code);
          });
        }
        products.push({
          name: product.product.name,
          id: product.product.code,
          price: product.basePrice.value,
          brand: '',
          category: '',
          variant: '',
          quantity: product.quantity,
          coupon: productPromotions.toString(),
        });
      });
      productPurchased = {
        actionField: {
          id: dataObj.code, // Transaction ID. Required for purchases and refunds.
          affiliation: '',
          revenue: dataObj.totalPrice.value, // Total transaction value (incl. tax and shipping)
          tax: dataObj.totalTax.value,
          shipping: dataObj.deliveryCost ? dataObj.deliveryCost.value : 0,
          coupon: orderPromotions.toString(),
        },
        products,
      };
      googleAnalyticsService.trackProductsPurchased(productPurchased);
    }
  };

  /**
   * trackCheckout function
   * @param  {object}   dataObj contains details of product during checkout
   * @return {object}          null
   */
  trackCheckout = (dataObj) => {
    let checkoutProducts = {};
    const products = [];

    if (dataObj) {
      dataObj.forEach((product) => {
        products.push({
          name: product.product.name,
          id: product.product.code,
          price: product.basePrice.value,
          brand: '',
          category: '',
          variant: '',
          quantity: product.quantity,
          coupon: '',
        });
      });
      checkoutProducts = {
        products,
      };
      googleAnalyticsService.trackCheckout(checkoutProducts);
    }
  };
}

// export default AnalyticsService;
export default AnalyticsService;
