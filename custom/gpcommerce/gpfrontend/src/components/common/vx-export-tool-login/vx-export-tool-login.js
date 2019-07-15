import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

export default {
  name: 'vx-export-tool-login',
  components: {},
  props: {
    i18n: {
      type: Object,
      required: true,
    },
  },
  mixins: [detectDeviceMixin],
  data() {
    return {
      user: {},
      userName: '',
      password: '',
      loginError: '',
    };
  },
  computed: {

  },
  mounted() {

  },
  updated() {
    this.user.userName = this.userName;
    this.user.password = this.password;
  },
  methods: {
    loginClicked() {

    },
  },
};
