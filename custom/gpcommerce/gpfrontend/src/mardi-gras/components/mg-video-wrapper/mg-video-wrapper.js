import {
  eventBus
} from '../../../modules/event-bus';
import videoPlayer from '../../../components/common/vx-video-player/vx-video-player.vue';
import vxModal from '../../../components/common/vx-modal/vx-modal.vue';

export default {
  name: 'mg-video-wrapper',
  components: {
    videoPlayer,
    vxModal
  },
  props: {
    videoWrapperData: {
      type: Object
    }
  },
  data() {
    return {

    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    openVideoModal(videoSRC) {
      eventBus.$emit('open-player', videoSRC);
    }
  }
}
