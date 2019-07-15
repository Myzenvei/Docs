import vxTextInput from "../vx-text-input/vx-text-input.vue";
import vxCheckboxField from "../vx-checkbox-field/vx-checkbox-field.vue";

export default {
  name: 'vx-form-generator',
  components: {
    vxTextInput,
    vxCheckboxField,
  },
  props: ['schema', 'value'],
  data() {
    return {
      formData: this.value || {}
    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    updateForm(field, value) {
      // this.$set(this.formData[field.questionId], field.preferenceTypeId, value);
      this.$set(this.formData, field.preferenceTypeId, value);
      this.$emit('input', this.formData)
    }
  }
}
