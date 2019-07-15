import globals from '../../common/globals';
import vxLiveChat from '../vx-live-chat/vx-live-chat.vue';
import vxServiceTicket from '../vx-service-ticket/vx-service-ticket.vue';


export default {
  name: 'vx-contact-us',
  components: {
    vxLiveChat,
    vxServiceTicket,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
      default: {},
    },
    liveChatTheme: {
      type: String,
      required: false,
    },
    showContactUs: {
      type: Boolean,
      required: true,
      default: true,
    },
    imageTextData: {
      type: Object,
      required: false,
    },
    isLiveChatEnabled: {
      type: Boolean,
      default: false,
      required: true,
    },
    /**
     * topic of inquiry dropdown data
     */
    topicOfInquiry: {
      type: String,
      default: '',
      required: true,
    },
  },
  data() {
    return {
      globals,
    };
  },
  computed: {

  },
  mounted() {

  },
  methods: {

  },
};
