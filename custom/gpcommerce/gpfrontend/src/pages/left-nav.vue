<template class="container">
  <div class="main__inner-wrapper">
    <div class="container">
      <div class="row">
        <div class="col-sm-3">
          <vx-left-nav :leftNavData="leftNavData" :i18n="messages.manageProfileShoppingLists.leftNav"></vx-left-nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import globals from '../components/common/globals';
import vxLeftNav from '../components/manage-profile-shopping-lists/vx-left-nav/vx-left-nav.vue';
import messages from '../locale/messages';

export default {
  name: 'leftNav',
  components: {
    'vx-left-nav': vxLeftNav,
  },
  data() {
    return {
      msg: 'Left Navigation',
      globals,
      messages: messages['en-US'],
      sectionTitle: {
        tag: 'h1',
        title: 'Frequently Asked Questions',
      },
      leftNavData: [
        {
          title: 'your account',
          links: [
            {
              linkText: 'Profile & Preferences',
              linkTo: '/',
            },
            {
              linkText: 'Addresses',
              linkTo: '#',
            },
            {
              linkText: 'Payment Methods',
              linkTo: '#',
            },
            {
              linkText: 'Lists',
              linkTo: '#',
            },
          ],
        },
        {
          title: 'your company',
          links: [
            {
              linkText: 'Business Units',
              linkTo: '#',
            },
            {
              linkText: 'Users',
              linkTo: '#',
            },
            {
              linkText: 'User Groups',
              linkTo: '#',
            },
            {
              linkText: 'Permissions',
              linkTo: '#',
            },
            {
              linkText: 'Order Approvals',
              linkTo: '#',
            },
            {
              linkText: 'Order History',
              linkTo: '#',
            },
          ],
        },
      ],
    };
  },
  methods: {},
};
</script>
