import assignIn from 'lodash/assignIn';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';



export default {
  name: 'vx-communication-preferences',
  components: {
    flyoutBannerMixin,
    vxSpinner,
    vxDropdownPrimary,
    vxRadioButtonGroup,
  },
  props: ['i18n'],
  data() {
    return {
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      schema: [],
      frequency: {},
      frequencyDropdownValues: [],
      selectedFrequency: '',
      globals,
      questionAnswers: {},
    }
  },
  computed: {

  },
  mounted() {
    // Temporary Commenting
    this.$refs.communicationPreferencesSpinner.showSpinner();
    this.manageProfileShoppingListService.getCommunicationPreferences({}, this.handleCommunicationPreferences, this.handleCommunicationPreferencesError);
  },
  methods: {
    applyPreferences() {
      let requestObj = {};
      requestObj.marketingPreferences = [];
      this.schema.forEach(item => {
        item.marketingPreferencesAnsList.forEach(answer => {
          if (item.selectionType === 'RADIO') {
            requestObj.marketingPreferences.push({
              "preferenceTypeId": answer.preferenceTypeId,
              "selected": this.questionAnswers[item.question] == answer.preferenceTypeId,
            });
          } else {
            requestObj.marketingPreferences.push({
              "preferenceTypeId": answer.preferenceTypeId,
              "selected": answer.selected,
            });
          }
        });
      });
      requestObj.frequency = this.selectedFrequency;
      const requestConfig = {};
      requestConfig.data = requestObj;
      this.manageProfileShoppingListService.updatePreferences(requestConfig, this.handleUpdatePreferences, this.handleUpdatePreferencesError);
      this.$refs.communicationPreferencesSpinner.showSpinner();
    },
    handleCommunicationPreferences(response) {
      this.$refs.communicationPreferencesSpinner.hideSpinner();
      if (response) {
        this.schema = response.data.marketingPrefQuestionData;
        this.schema.forEach(item => {
          item.marketingPreferencesAnsList.forEach((component, index) => {
            item.marketingPreferencesAnsList[index] = this.renameObjectKeys(component, 'description', 'preferenceTypeId');
            if (item.selectionType === 'RADIO' && component.selected) {
              this.questionAnswers[item.question] = component.preferenceTypeId;
            }
            return true;
          });
          return true;
        });
        if (response.data.frequency) {
          this.frequency = response.data.frequency;
        }
        this.generateFrequencyDropdownValues();
      }
    },
    handleCommunicationPreferencesError(error) {
      this.$refs.communicationPreferencesSpinner.hideSpinner();
      if (error) {}
    },
    handleUpdatePreferences(response) {
      this.$emit('update-preferences-success', this.i18n.successMarketingPreferences);
      this.$refs.communicationPreferencesSpinner.hideSpinner();
    },
    handleUpdatePreferencesError(error) {
      this.$refs.communicationPreferencesSpinner.hideSpinner();
      if (error) {
      }
    },
    generateFrequencyDropdownValues() {
      if(this.frequency && this.frequency.freqEnumValues) {
        this.frequency.freqEnumValues.map((child, index) =>
          this.$set(this.frequencyDropdownValues, index, { label: child.split('-')[0], value: child.split('-')[0] }),
        );
        this.setSelectedFrequency();
      }
    },
    setSelectedFrequency() {
      if(this.frequency && this.frequency.freqEnumValues) {
        const selectedValues = this.frequency.freqEnumValues.filter(child => child.split('-')[1] === 'true')[0];
        if (selectedValues) {
          this.selectedFrequency = selectedValues.split('-')[0];
          this.$refs.frequencyDropdown.setDropdownValue(this.selectedFrequency);
        }
      }
    },
    renameObjectKeys(object, label, value) {
      const remapedObject = {
        label: object[label],
        value: object[value],
      };
      return assignIn({}, remapedObject, object);
    },
  },
};
