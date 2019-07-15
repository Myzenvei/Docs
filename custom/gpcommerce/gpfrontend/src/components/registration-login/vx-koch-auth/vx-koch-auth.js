import globals from '../../common/globals.js';
import kauthMixin from '../../common/mixins/kauth-mixin.js';


export default {
  name: 'vx-koch-auth',
  mixins: [kauthMixin],
  components: {},
  props: {
    kochUrl: String
  },
  data () {
    return {
      globals: globals
    }
  },
  computed: {

  },
  mounted () {
     if(this.globals.isEmployeeStore())
     {
        if(this.kochUrl)
        {
          this.globals.externalNavigations.kauthUrl = this.kochUrl;
        }
        var kochCode = this.globals.getUrlParam('code');
        // If the code is present in the Url
        if(kochCode)
        {
          // Set in the local Storage
          this.setStorage(this.kochKeys.KOCHCODE, kochCode);

           // Handle the redirection
          var kochRedirectUrl = this.getStorage(this.kochKeys.KOCHREDIRECT);
          if(kochRedirectUrl)
          {
             this.globals.navigateToUrl(kochRedirectUrl);
          }
        }
     }

  },
  methods: {

  }
}
