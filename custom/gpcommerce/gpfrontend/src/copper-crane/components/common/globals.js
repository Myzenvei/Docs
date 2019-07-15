/* C+C Globals files */
import siteGlobals from '../../../components/common/globals';

let CJEVENTVal = '';

const globals = {
  ExtoleJSURL: 'https://refer.copperandcrane.com/core.js',
  /**
  +   * To set CJEVENTVal on local storage
  +   */
  getCJEVENT() {
    CJEVENTVal = (siteGlobals.getUrlParam('CJEVENT')) || (siteGlobals.getUrlParam('cjevent'));
    if (CJEVENTVal) {
      siteGlobals.setStorage('CJEVENT', CJEVENTVal);
    }
  },
  /**
  +   * To return coupon code on local storage
      * @param  {Object} oderDetails Order Data
  +   */
  getCouponCode(oderDetails) {
    const couponVal = [];
    if (oderDetails.appliedOrderPromotions) {
      oderDetails.appliedOrderPromotions.forEach((promotion) => {
        promotion.appliedCouponCodes.forEach((coupon) => {
          couponVal.push(coupon);
        });
      });
    }
    return couponVal.join();
  },
};

/* Gets called on every load and if the CJEVENTVal is available sets its value on local storage */

globals.getCJEVENT();

export default globals;
