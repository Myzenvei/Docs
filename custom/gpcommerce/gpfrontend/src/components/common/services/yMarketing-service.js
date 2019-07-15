class YMarketingService {
  /**
   * trackAddToCart function
   * @param  {object}   productCode productCode required fields
   * @param  {object}   quantityAdded quantityAdded required fields
   * @param  {object}   cartData cartData required fields
   * @return {object}          null
   */
  trackAddToCart = (productCode, quantityAdded, cartData) => {
    window.trackAddToCart_piwik(productCode, quantityAdded, cartData);
  }

  /**
   * trackRemoveFromCart function
   * @param  {object}   productCode productCode required fields
   * @param  {object}   initialQuantity initialQuantity required fields
   * @param  {object}   cartData cartData required fields
   * @return {object}          null
   */
  trackRemoveFromCart = (productCode, initialQuantity, cartData) => {
    window.trackRemoveFromCart_piwik(productCode, initialQuantity, cartData);
  }

  /**
   * trackUpdateCart function
   * @param  {object}   productCode productCode required fields
   * @param  {object}   initialQuantity initialQuantity required fields
   * @param  {object}   newQuantity newQuantity required fields
   * @param  {object}   cartData cartData required fields
   * @return {object}          null
   */
  trackUpdateCart = (productCode, initialQuantity, newQuantity, cartData) => {
    window.trackUpdateCart_piwik(productCode, initialQuantity, newQuantity, cartData);
  }
}

// export default yMarketingService
export {
  YMarketingService as
  default,
};
