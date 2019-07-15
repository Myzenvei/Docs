import {
  eventBus
} from '../../../modules/event-bus';
import vxModal from '../vx-modal/vx-modal.vue';

export default {
  name: 'vx-video-player',
  props: {
    videoOptions: {
      type: String,
      default: '?modestbranding=1&rel=0&controls=1&showinfo=0&html5=1&autoplay=1'
    }
  },
  components: {
    vxModal,
  },
  data() {
    return {
      videoSRC: '',
      videoSRCWithOptions: ''
    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {

  },
  created() {
    let videoPlayerInstance = this;
    eventBus.$on('open-player', function (videoSRC) {
      videoPlayerInstance.videoSRC = videoSRC;
      videoPlayerInstance.videoSRCWithOptions =
        videoPlayerInstance.videoSRC + videoPlayerInstance.videoOptions;
      // Note: AutoPlay doesn't work in loclahost
      videoPlayerInstance.$refs.banner__video.open();
    });
  }
}
