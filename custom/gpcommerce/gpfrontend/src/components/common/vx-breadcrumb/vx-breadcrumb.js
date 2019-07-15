/* Breadcrumb component will be used to display breadcrumbs on the page
   For now, it will only work on PDP pages, as the hybris services will
   only return accurate data for PDP pages  */

import globals from '../globals';

export default {
  name: 'vx-breadcrumb',
  components: {},
  props: ['breadcrumbs'],
  data() {
    return {
      globals,
    };
  },
  computed: {},
  mounted() {},
  methods: {},
};
