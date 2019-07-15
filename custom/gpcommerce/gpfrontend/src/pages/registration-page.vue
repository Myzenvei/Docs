<template>
<div class="header-container">
  <h1>{{ msg }}</h1>

  <div class="main__inner-wrapper">
    <div class="container-fluid">
      <!-- <img :src="globals.assetsPath+'images/vue.png'" /> -->
      <div class="row">
        <div class="col-sm-6">
          <vx-forgot-password :i18n="messages.registrationLogin.passwordExpiry" is-expiry></vx-forgot-password>
        </div>
        <div class="col-sm-6">
          <vx-update-password heading="Update Password" action-url='/someurl/somepoint' :i18n="messages.registrationLogin.updatePassword"></vx-update-password>
        </div>
        <div class="col-sm-12">
          <vx-create-account action-url='/someurl/somepoint' :i18n="messages.registrationLogin.createAccount"></vx-create-account>
        </div>

      </div>
    </div>
  </div>
</div>
</template>

<script>
import vxForgotPassword from '../components/registration-login/vx-forgot-password/vx-forgot-password.vue';
import vxUpdatePassword from '../components/registration-login/vx-update-password/vx-update-password.vue';
import vxCreateAccount from '../components/registration-login/vx-create-account/vx-create-account.vue';
import messages from '../locale/messages';

export default {
  name: 'Registration',
  components: {
    vxForgotPassword,
    vxUpdatePassword,
    vxCreateAccount,
  },
  data() {
    return {
      msg: 'User Registration',
      messages: messages['en-US'],
    };
  },
};
</script>
