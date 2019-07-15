import globals from '../../../../components/common/globals';
import {
  CJEventParms,
} from '../../../../components/common/mixins/vx-enums';
import brandGlobals from '../globals';

class CjEventService {
  /**
   * To initiate cjEvent integration on the Order confirmation
   * Triggers 1 Script tag
   * @param  {Object} orderDetails Order Data
   */
  initiateCJEventOnOrderConfirmation(orderDetails) {
    this.orderDetailsVal = orderDetails;
    if (this.orderDetailsVal) {
      const CJEVENT = globals.getStorage('CJEVENT');
      let url =
        `https://www.emjcd.com/tags/c?containerTagId=${CJEventParms.containerTagId}&CID=${CJEventParms.CID}&TYPE=${CJEventParms.TYPE}&CURRENCY=${this.orderDetailsVal.totalPrice.currencyIso}&OID=${this.orderDetailsVal.code}&CJEVENT=${CJEVENT}`;
      this.orderDetailsVal.entries.forEach((item, index) => {
        url += `&ITEM${index + 1}=${encodeURI(item.product.code)}`;
        url += `&AMT${index + 1}=${item.product.price.value}`;
        url += `&QTY${index + 1}=${item.quantity}`;
      });
      if (brandGlobals.getCouponCode(orderDetails)) {
        url += `&COUPON=${brandGlobals.getCouponCode(orderDetails)}`;
      }
      if (orderDetails.totalDiscounts.value) {
        url += `&DISCOUNT=${orderDetails.totalDiscounts.value}`;
      }
      const cjIframe = document.createElement('iframe');
      cjIframe.setAttribute('src', url);
      cjIframe.setAttribute('name', 'cj_conversion');
      cjIframe.setAttribute('scrolling', 'no');
      cjIframe.frameBorder = 0;
      cjIframe.style.width = 0;
      cjIframe.style.height = 0;
      document.body.appendChild(cjIframe);
    }
  }
}

export {
  CjEventService as
  default,
};
// export default cjEventService;
