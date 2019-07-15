import storageMixin from '../mixins/storage-mixin';

const kauthMixin = {
  data() {
    return {
      kochKeys: {
        KOCHCODE: 'kochCode',
        KOCHREDIRECT: 'kochRedirectUrl',
      },
    };
  },
  mixins: [storageMixin],
  methods: {
    deleteKauthData() {
      this.deleteStorage(this.kochKeys.KOCHREDIRECT);
      this.deleteStorage(this.kochKeys.KOCHCODE);
    },
  },
};

export default kauthMixin;
