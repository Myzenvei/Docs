import vxImageTile from '../vx-image-tile/vx-image-tile.vue';
import vxTextTile from '../vx-text-tile/vx-text-tile.vue';

export default {
  name: 'vx-image-text',
  components: {
    'vx-image-tile': vxImageTile,
    'vx-text-tile': vxTextTile
  },
  props: {
    imageTileData: {
      type: Object
    },
    textTileData: {
      type: Object
    },
    componentTheme: {
      type: String
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

  }
}
