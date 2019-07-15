<template>
<div class="header-container">
  <h1>{{ msg }}</h1>

  <vx-spinner
    ref="spinner"
    :image="globals.assetsPath+'images/spinner.gif'"
    text='Loading...'
    full-screen>
  </vx-spinner>

 <button class="btn btn-primary" @click='loadSpinner'>Launch Full Spinner</button>

  <div class="main__inner-wrapper">
    <div>
    <div class="row">
      <div class="col-xs-12">
        <vx-product-tile>
           <template slot="top-right">
              <span class="col-xs-2 px-xs-0">
                <span class="cart-icons icon-trash" @click="deleteCartItem('sdf')"></span>
              </span>
            </template>
            <template slot="image-slot">
              <img src="globals.assetsPath+'images/'">
            </template>
            <template slot="product-icons-slot">
             <div class="pt-xs-3 pb-xs-3">
              <span class="cart-icons icon-certification pr-xs-2"></span>
              <span class="cart-icons icon-shipping pr-xs-2"></span>
              <span class="cart-icons icon-online pr-xs-2"></span>
              <span class="cart-icons icon-subscription pr-xs-2"></span>
              <span class="cart-icons icon-seasonal"></span>
            </div>
            </template>
            <template slot="details">
              <p class="product-title mb-xs-2">
                <a href=''>productEntry.product.name</a>
              </p>
              <p class="product-code mb-xs-2">productEntry.product.code</p>
              <p class="product-stock mb-xs-3">productEntry.product.stock.stockLevelStatus</p>
              <p class="product-price mb-xs-3">
                <span class="product-current-price">productEntry.basePrice.formattedValue</span>
                <span class="product-old-price">
                  <strike>productEntry.weblistPrice.formattedValue</strike>
                </span>
                <span>
                  <p class="product-promo mb-xs-3">promotion Code</p>
                  <p class="product-quantity">i18n.quantity</p>
                  <span>
                    <vx-stepper-control></vx-stepper-control>
                    <p class="product-promo mb-xs-3">promotion Code</p>
                  </span>
                </span>
              </p>
            </template>
            <template slot="right-section-slot">
              <p class="product-discount">-productEntry.totalPrice.formattedValue</p>
              <p class="product-total-price">productEntry.totalPrice.formattedValue</p>
            </template>
          </vx-product-tile>
      </div>
    </div>
      <div class="row">
        <div class="col-xs-12">
          <vx-accordion heading="Accordian Heading" is-slot-based>
            <div slot="component">This will be your component</div>
          </vx-accordion>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-8">
            <vx-add-edit-address :i18n="messages.common.address"></vx-add-edit-address>
        </div>
       <div class="col-sm-4">
        </div>
        </div>
        <div class="col-xs-12">
          <vx-rich-text :richTextMarkup= richTextMarkup />
        </div>
      </div>
    </div>
    <vx-product-solution :tabsData= tabsData />
    <section-title
      :sectionTitle= sectionTitle
    />
    <faq-comp :accordionGroupData= accordionGroupData />
    <vx-sbs :sbs-data= mktgSBSData />
    <!-- Card Component -->
  <div class="container-fluid">
    <div class="row">
      <div class="col-xs-12">
        <h1>Card Component</h1>
      </div>
      <div class="col-xs-12">
        <vx-profile-card :card-classes="preferenceCardClasses" :is-default-height="preferenceCardDefaultHeight" v-bind:has-footer="false">
          <template slot="heading">
            <li class="card-heading">
            <h4>{{personalDetailsTitle}}</h4>
          </li>
          </template>
          <template slot="details">
            <li class="card-item" v-for="item in personalDetails" :key="item">{{item}}</li>
          </template>
          <template slot="icons">
            <li class="card-icon">
              <a @click="logMessage">
                <span class="icon-default icon-small icon-trash"></span>
              </a>
            </li>
          </template>
          <template slot="footer">
            &#8203;
          </template>
        </vx-profile-card>
      </div>
      <div class="col-xs-12">
        <vx-profile-card :has-icons="unitCardIcons" v-bind:has-footer="false">
          <template slot="heading">
            <li class="card-heading-link">
              <a class="#">
                <h4>Unit ID comes here</h4>
              </a>
            </li>
          </template>
          <template slot="details">
            <li class="card-item">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor</li>
          </template>
          <template slot="footer">
            &#8203;
          </template>
        </vx-profile-card>
      </div>
      <div class="col-xs-12">
        <vx-profile-card v-bind:is-disabled="isAddressDisable" v-bind:has-icons="true" v-bind:has-footer="true">
          <template slot="heading">
            {{""}}
          </template>
          <template slot="details">
            <li class="card-item" v-for="item in addressDetails" :key="item">{{item}}</li>
          </template>
          <template slot="footer">
            <p class="d-flex col-xs-12 align-items-center justify-content-between" v-show="isAddressDisable">
              <span class="card-item card-item-bold">Default Address</span>
              <a class="link-regular link-bold" href="#">Enable</a>
            </p>
          </template>
        </vx-profile-card>
      </div>
      <div class="col-xs-6">
        <vx-profile-card v-bind:card-type="'social'" v-bind:is-default-height="true">
          <template slot="heading">
            <h4>Social Accounts</h4>
            <a href="">
              <span class="icon-default icon-big icon-edit"></span>
            </a>
          </template>
          <template slot="details">
            <div class="row d-flex align-items-center justify-content-between">
              <div class="card-items align-items-center d-flex">
                <span class="icon-default icon-big icon-facebook"></span>
                <span class="pl-xs-2">Facebook</span>
              </div>
              <div class="card-item-link">
                <a href="#" class="link-regular link-bold">Connect</a>
              </div>
            </div>
          </template>
        </vx-profile-card>
      </div>
      <div class="col-xs-6">
        <vx-profile-card v-bind:card-type="'add'" v-bind:is-default-height="true">
          <template slot="details">
            <a href="#">
              <span class="icon-big icon-plus"></span>
              <span class="ml-xs-1 link-large link-bold">Add Address</span>
            </a>
          </template>
        </vx-profile-card>
      </div>
      <div class="col-xs-6">
        <vx-profile-card v-bind:card-type="'column'" v-bind:has-icons="false" v-bind:is-default-height="false" v-bind:has-footer="false">
          <template slot="heading">
            <li class="card-heading">
              <h4>Parent Business Unit</h4>
            </li>
          </template>
          <template slot="details">
            <ul class="col-xs-6">
              <li class="card-item" v-for="item in personalDetails" :key="item">{{item}}</li>
            </ul>
            <ul class="col-xs-6">
              <li class="card-item" v-for="item in personalDetails" :key="item">{{item}}</li>
            </ul>
          </template>
        </vx-profile-card>
      </div>
    </div>
  </div>
  <div class="col-xs-12">
    <vx-form-generator :schema="schema" v-model="formData"></vx-form-generator>
  </div>
  </div>
</template>

<script>
import vxSpinnner from '../components/common/vx-spinner/vx-spinner.vue';
// import vxProductSupport from '../components/view-site-content/vx-product-support/vx-product-support.vue';
import vxRichTextCompnent from '../components/common/vx-rich-text/vx-rich-text.vue';
import vxProductSolution from '../components/view-site-content/vx-product-category-solution/vx-product-category-solution.vue';
import VXSbsComp from '../components/view-site-content/vx-side-by-side/vx-side-by-side.vue';
import globals from '../components/common/globals';
import faqComp from '../components/view-site-content/vx-faq/vx-faq.vue';
import VxSectionTitle from '../components/common/vx-title/vx-title.vue';
import vxaddEditAddress from '../components/common/vx-add-edit-address/vx-add-edit-address.vue';
import messages from '../locale/messages';
import vxCuratedList from '../components/view-site-content/vx-curated-list/vx-curated-list.vue';
import vxAccordion from '../components/common/vx-accordion/vx-accordion.vue';

import vxProfileCard from '../components/common/vx-profile-card/vx-profile-card.vue';
import vxProductTile from '../components/common/vx-product-tile/vx-product-tile.vue';
import vxStepperControl from '../components/common/vx-stepper-control/vx-stepper-control.vue';
import vxFormGenerator from '../components/common/vx-form-generator/vx-form-generator.vue';

export default {
  name: 'CommonComponents',
  components: {
    'vx-rich-text': vxRichTextCompnent,
    'vx-product-solution': vxProductSolution,
    'vx-spinner': vxSpinnner,
    'faq-comp': faqComp,
    'section-title': VxSectionTitle,
    'vx-sbs': VXSbsComp,
    'vx-curated-list': vxCuratedList,
    'vx-add-edit-address': vxaddEditAddress,
    vxAccordion,
    vxProfileCard,
    vxProductTile,
    vxStepperControl,
    vxFormGenerator,
  },
  data() {
    return {
      msg: 'Common Components',
      messages: messages['en-US'],
      globals,
      sectionTitle: {
        tag: 'h1',
        title: 'Frequently Asked Questions',
      },
      accordionGroupData: '<que>Question1 /^[a-zA-Z0-9!@#$%^&*()_+-=[]{};\'"\\|,.<>/?]*$/?</que><ans>answer1</ans><que>Question2</que><ans>answer2</ans><que>Question3</que><ans>answer3</ans><que>Question4</que><ans>answer4</ans><que>Question5</que><ans>answer5</ans>',
      mktgSBSData: {
        sbsGlobalBG: '#c0c0c0',
        sbsGlobalBgImgD: 'https://fakeimg.pl/600x300/DDDDDD',
        sbsGlobalBgImgT: 'https://fakeimg.pl/600x300/DDDDFF',
        sbsGlobalBgImgM: 'https://fakeimg.pl/600x300/DDDDCC',
        sbsMktgData: [
          {
            sbsImage: 'https://fakeimg.pl/300x300',
            sbsTitle: 'Title1',
            sbsTitleColor: '#ffffff',
            sbsBody:
              'You just have to put your image size after our URL. Only the first parameter is mandatory. There are options too, you can pass a text, or change some colors.',
            sbsBodyTxtColor: '#ffffff',
            ctaText: 'Learn More',
            ctaLink: '#link1',
            ctaColor: '#ffffff',
            isExternal: true,
            sbsBodyBg: '#b1b2b3',
          },
          {
            sbsImage: 'https://fakeimg.pl/300x305',
            sbsTitle: 'Title2',
            sbsTitleColor: '#ffffff',
            sbsBody:
              '1You just have to put your image size after our URL. Only the first parameter is mandatory. There are options too, you can pass a text, or change some colors.',
            ctaText: 'Learn More',
            sbsBodyTxtColor: '#ffffff',
            ctaLink: '#link1',
            ctaColor: '#ffffff',
            isExternal: false,
            sbsBodyBg: '#b1b2b3',
          },
        ],
      },
      richTextMarkup: '<p>This is <sub>subscripted</sub> text.</p>',
      tabsData: {
        type: 'categoryHierarchyWsDTO',
        id: 'breakroomps',
        name: 'Break Room Product Solution',
        url: '/category/breakroomps',
        brochure: 'http://sandeep.com',
        learnMore: 'http://lear.com',
        subcategories: [
          {
            id: 'restroom',
            name: 'Restroom',
            url: '/category/restroom',
            brochure: 'http://broucherlink.com',
            learnMore: 'http://learnmorelink.com',
            subcategories: [],
          },
          {
            id: 'consumption',
            name: 'Consumption',
            url: '/category/consumption',
            subcategories: [],
          },
          {
            id: 'foodsafety',
            name: 'Food Safety',
            url: '/category/foodsafety',
            subcategories: [],
          },
        ],
      },
      personalDetails: ['<first last name>', '<email>', '<phone>', '<roll>'],
      personalDetailsTitle: 'Personal Details',
      addressDetails: [
        '<first last name>',
        '<company>',
        '<country>',
        '<address1>',
        '<address2>',
        '<city> <state/province> <zip/postal code>',
      ],
      isAddressDisable: true,
      preferenceCardClasses: 'px-xs-3 py-xs-3 pl-sm-4 pr-sm-3',
      preferenceCardDefaultHeight: true,
      unitCardIcons: false,
      schema: [
        {
          fieldType: 'vxTextInput',
          placeholder: 'First Name',
          label: 'First Name',
          name: 'firstName',
        },
        {
          fieldType: 'vxTextInput',
          placeholder: 'Last Name',
          label: 'Last Name',
          name: 'lastName',
        },
        {
          fieldType: 'vxCheckboxField',
          name: 'brandOfferBrawny212',
          description: 'Get updates on Brawny products',
          preferenceTypeId: 'brandOfferBrawny212',
          selected: false,
        },
        {
          fieldType: 'vxCheckboxField',
          name: 'premiumUsers212',
          description: 'Get updates on Brawny premium products',
          preferenceTypeId: 'brandOfferBrawny213',
          selected: false,
        },
      ],
      formData: {
        firstName: 'Evan',
      },
      preferences: [
        {
          name: 'brandOfferBrawny212',
          description: 'Get updates on Brawny products',
          preferenceTypeId: 'brandOfferBrawny212',
          selected: 'false',
        },
        {
          name: 'premiumUsers212',
          description: 'Get updates on Brawny premium products',
          preferenceTypeId: 'brandOfferBrawny213',
          selected: 'false',
        },
      ],
    };
  },
  methods: {
    loadSpinner() {
      this.$refs.spinner.showSpinner();
    },
    logMessage() {
      this.personalDetailsTitle = 'Hardik Joshi';
    },
  },
};
</script>
