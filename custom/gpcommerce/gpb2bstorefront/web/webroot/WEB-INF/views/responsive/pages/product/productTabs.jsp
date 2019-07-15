<vx-bazaar-voice :bv-client="'${bvClient}'" :gp-bv-id="'${gpbvId}'" :bv-env="'${bvEnv}'" :bv-locale="'${bvLocale}'"
    :product-id="'${product.code}'"></vx-bazaar-voice>

<vx-tab-container :i18n="messages.pdpTabContainer" :is-bazaar-voice="'${isBazaarVoiceEnabled}'"
    :is-related-enabled="${isRelatedEnabled}" :search-browse-i18n="messages.searchBrowse.productTile">
</vx-tab-container>