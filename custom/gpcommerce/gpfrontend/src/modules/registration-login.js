import vxLogin from '../components/registration-login/vx-login/vx-login.vue';
import vxKochAuth from '../components/registration-login/vx-koch-auth/vx-koch-auth.vue';
import vxSocialLogin from '../components/registration-login/vx-social-login/vx-social-login.vue';
import vxCreateAccount from '../components/registration-login/vx-create-account/vx-create-account.vue';
import vxForgotPassword from '../components/registration-login/vx-forgot-password/vx-forgot-password.vue';
import vxUpdatePassword from '../components/registration-login/vx-update-password/vx-update-password.vue';

const registerLoginModule = {
  components: {
    vxKochAuth,
    vxLogin,
    vxSocialLogin,
    vxCreateAccount,
    vxForgotPassword,
    vxUpdatePassword,
  },
};

export default registerLoginModule;
