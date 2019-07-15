<template>
<div>

<vx-profile-preferences :i18n="messages.manageProfileShoppingLists.profilePreferences">
</vx-profile-preferences>
</div>
</template>
<script>
import vxProfilePreferences from '../components/manage-profile-shopping-lists/vx-profile-preferences/vx-profile-preferences.vue';
import messages from '../locale/messages';

export default {
  name: 'profile-page',
  components: {
    'vx-profile-preferences': vxProfilePreferences,
  },
  created() {
    const socialShareScript = document.createElement('script');

    socialShareScript.setAttribute('src', 'https://static.addtoany.com/menu/page.js');
    document.head.appendChild(socialShareScript);
  },
  data() {
    return {
      msg: 'Profile Page',
      messages: messages['en-US'],
    };
  },
};
</script>
