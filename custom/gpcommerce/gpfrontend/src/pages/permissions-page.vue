<template>
   <permissions-page permissionColumns="ID,Type,Business Unit,Time Period,Amount,Status" :i18n= "messages.manageB2BOrg.userPermissions" />
</template>

<!--<template>
   <vx-permissions-details :i18n="messages.manageB2BOrg.userPermissions.permissionDetail"/>
 </template>-->

<script>
import permissionsPage from '../components/manage-b2b-org/vx-permisions-landing-page/vx-permisions-landing-page.vue';
import vxPermissionsDetails from '../components/manage-b2b-org/vx-permissions-details/vx-permissions-details.vue';
import messages from '../locale/messages';

export default {
  name: 'permisions-landing-page',
  components: {
    permissionsPage,
    vxPermissionsDetails,
  },
  data() {
    return {
      messages: messages['en-US'],
    };
  },
};
</script>
