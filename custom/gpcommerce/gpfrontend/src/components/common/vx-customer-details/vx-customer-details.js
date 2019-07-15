import globals from '../globals';

export default {
  name: 'vx-customer-details',
  components: {},
  props: [
    'uid',
    'firstName',
    'lastName',
    'email',
    'contactNumber',
    // Additional For B2B
    'unit',
    'userRoles',
    'b2bUnitLevel',
    'b2bUserReviewStatus',
    'customerId',
    'userEmail',
  ],
  data() {
    return {
      globals,
    };
  },
  computed: {},
  created() {
    this.globals.uid = this.uid;
    this.globals.userInfo.firstName = this.firstName;
    this.globals.userInfo.lastName = this.lastName;
    this.globals.userInfo.email = this.email;
    this.globals.userInfo.contactNumber = this.contactNumber;
    this.globals.userInfo.unit = this.unit || '';
    this.globals.userInfo.userRoles = this.userRoles;
    this.globals.userInfo.b2bUnitLevel = this.b2bUnitLevel;
    this.globals.userInfo.b2bUserReviewStatus = this.b2bUserReviewStatus;
    this.globals.userInfo.customerId = this.customerId;
  },
  mounted() {
    if (this.globals.isGpXpress()) {
      this.globals.userInfo.userEmail = this.userEmail;
    }
  },
  methods: {},
};
