<template>
  <div>
    <h1>B2BOrg</h1>
    <div class="container">
        <vx-user-permissions-table />
    </div>
    <vx-business-unit-detail class="col-xs-12" :i18n="messages.manageB2BOrg.businessUnit"></vx-business-unit-detail>
    <vx-business-unit-landing class="col-xs-12" :i18n="messages.manageB2BOrg.businessUnitLanding"></vx-business-unit-landing>
    <vx-users-landing :i18n="messages.manageB2BOrg.usersLanding"></vx-users-landing>
    <vx-user-details :i18n="messages.manageB2BOrg.userDetails"></vx-user-details>
    <vx-user-groups-landing class="col-xs-12" :i18n="messages.manageB2BOrg.userGroupLanding"></vx-user-groups-landing>
    <vx-user-group-details class="col-xs-12" :i18n="messages.manageB2BOrg.userGroupDetails"></vx-user-group-details>
  </div>
</template>
<script>
import messages from '../locale/messages';

import vxBusinessUnitDetail from '../components/manage-b2b-org/vx-business-unit-detail/vx-business-unit-detail.vue';
import vxUserPermissionsTable from '../components/examples/vx-permissionsTable/vx-permissionsTable.vue';
import vxUsersLanding from '../components/manage-b2b-org/vx-users-landing/vx-users-landing.vue';
import vxBusinessUnitLanding from '../components/manage-b2b-org/vx-business-unit-landing/vx-business-unit-landing.vue';
import vxUserDetails from '../components/manage-b2b-org/vx-user-details/vx-user-details.vue';
import vxUserGroupsLanding from '../components/manage-b2b-org/vx-user-groups-landing/vx-user-groups-landing.vue';
import vxUserGroupDetails from '../components/manage-b2b-org/vx-user-group-details/vx-user-group-details.vue';

export default {
  name: 'Hello',
  components: {
    'vx-user-permissions-table': vxUserPermissionsTable,
    vxBusinessUnitDetail,
    vxUsersLanding,
    vxBusinessUnitLanding,
    vxUserDetails,
    vxUserGroupDetails,
    vxUserGroupsLanding,
  },
  data() {
    return {
      messages: messages['en-US'],
      unitDetailsData: {
        type: 'b2BUnitWsDTO',
        name: 'Services East',
        uid: 'Services East',
        active: true,
        addresses: [
          {
            billingAddress: false,
            country: {
              isocode: 'US',
            },
            defaultAddress: false,
            firstName: 'Mingmei',
            id: '8796101672983',
            lastName: 'Wang',
            line1: '700 E 50th Street',
            postalCode: '10022',
            town: 'New York',
            region: 'New York',
          },
        ],
        administrators: [
          {
            name: 'Lars Bauer',
            uid: 'lars.bauer@rustic-hw.com',
            currency: {
              active: false,
              isocode: 'USD',
              name: 'US Dollar',
              symbol: '$',
            },
            displayUid: 'lars.bauer@rustic-hw.com',
            firstName: 'Lars',
            lastName: 'Bauer',
            title: 'Mr.',
            titleCode: 'mr',
          },
        ],
        children: [
          {
            name: 'Services West',
            uid: 'Services West',
            active: false,
            selectable: false,
            selected: false,
          },
          {
            name: 'Services East',
            uid: 'Services East',
            active: true,
            selectable: false,
            selected: false,
          },
        ],
        customers: [
          {
            name: 'Lucas Kowalski',
            uid: 'lucas.kowalski@rustic-hw.com',
            active: true,
            currency: {
              active: false,
              isocode: 'USD',
              name: 'US Dollar',
              symbol: '$',
            },
            displayUid: 'lucas.kowalski@rustic-hw.com',
            firstName: 'Lucas',
            lastName: 'Kowalski',
            roles: ['b2bcustomergroup'],
            selected: false,
            title: 'Mr.',
            titleCode: 'mr',
            unit: {
              name: 'Services East',
              uid: 'Services East',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Mingmei Wang',
            uid: 'mingmei.wang@rustic-hw.com',
            active: true,
            currency: {
              active: false,
              isocode: 'USD',
              name: 'US Dollar',
              symbol: '$',
            },
            displayUid: 'mingmei.wang@rustic-hw.com',
            firstName: 'Mingmei',
            lastName: 'Wang',
            roles: ['b2bapprovergroup', 'b2bcustomergroup'],
            selected: false,
            title: 'Ms.',
            titleCode: 'ms',
            unit: {
              name: 'Services East',
              uid: 'Services East',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Ulf Becker',
            uid: 'ulf.becker@rustic-hw.com',
            active: true,
            currency: {
              active: false,
              isocode: 'USD',
              name: 'US Dollar',
              symbol: '$',
            },
            displayUid: 'ulf.becker@rustic-hw.com',
            firstName: 'Ulf',
            lastName: 'Becker',
            roles: ['b2bcustomergroup'],
            selected: false,
            title: 'Mr.',
            titleCode: 'mr',
            unit: {
              name: 'Services East',
              uid: 'Services East',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Alejandro Navarro',
            uid: 'alejandro.navarro@rustic-hw.com',
            active: true,
            currency: {
              active: false,
              isocode: 'USD',
              name: 'US Dollar',
              symbol: '$',
            },
            displayUid: 'alejandro.navarro@rustic-hw.com',
            firstName: 'Alejandro',
            lastName: 'Navarro',
            roles: ['b2bcustomergroup'],
            selected: false,
            title: 'Mr.',
            titleCode: 'mr',
            unit: {
              name: 'Services East',
              uid: 'Services East',
              active: true,
              selectable: false,
              selected: false,
            },
          },
        ],
        selectable: false,
        selected: false,
      },
      usersDetails: {
        users: [
          {
            name: 'Alejandro Navarro',
            uid: 'alejandro.navarro@rustic-hw.com',
            active: true,
            roles: ['b2bcustomergroup'],
            selected: false,
            unit: {
              name: 'Services East',
              uid: 'Services East',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Byung-soon Lee',
            uid: 'byung-soon.lee@rustic-hw.com',
            active: true,
            roles: ['b2bcustomergroup'],
            selected: false,
            unit: {
              name: 'Services West',
              uid: 'Services West',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Gi Sun',
            uid: 'gi.sun@rustic-hw.com',
            active: true,
            roles: ['b2bcustomergroup'],
            selected: false,
            unit: {
              name: 'Services West',
              uid: 'Services West',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Gi Sun',
            uid: 'gi.sun@pronto-hw.com',
            active: true,
            roles: ['b2bcustomergroup'],
            selected: false,
            unit: {
              name: 'Pronto Services',
              uid: 'Pronto Services',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'James Bell',
            uid: 'james.bell@rustic-hw.com',
            active: true,
            roles: ['b2bapprovergroup'],
            selected: false,
            unit: {
              name: 'Rustic Services',
              uid: 'Rustic Services',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'James Bell',
            uid: 'james.bell@pronto-hw.com',
            active: true,
            roles: ['b2bapprovergroup'],
            selected: false,
            unit: {
              name: 'Pronto Services',
              uid: 'Pronto Services',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Lars Bauer',
            uid: 'lars.bauer@rustic-hw.com',
            active: true,
            roles: ['b2badmingroup'],
            selected: false,
            unit: {
              name: 'Rustic Services',
              uid: 'Rustic Services',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Lucas Kowalski',
            uid: 'lucas.kowalski@rustic-hw.com',
            active: true,
            roles: ['b2bcustomergroup'],
            selected: false,
            unit: {
              name: 'Services East',
              uid: 'Services East',
              active: true,
              selectable: false,
              selected: false,
            },
          },
          {
            name: 'Matheu Silva',
            uid: 'matheu.silva@rustic-hw.com',
            active: true,
            roles: ['b2bapprovergroup', 'b2bcustomergroup', 'b2bmanagergroup'],
            selected: false,
            unit: {
              name: 'Services West',
              uid: 'Services West',
              active: true,
              selectable: false,
              selected: false,
            },
          },
        ],
      },
      unitLandingData: {
        active: true,
        addresses: [
          {
            billingAddress: true,
            country: {
              isocode: 'US',
              name: 'United States',
            },
            defaultAddress: false,
            email: 'akiro.nakamura@rustic-hw.com',
            firstName: 'Akiro',
            id: '8796095217687',
            lastName: 'Nakamura',
            line1: '999 South Wacker Drive',
            postalCode: '60606',
            shippingAddress: true,
            title: 'Mr',
            titleCode: 'mr',
            town: 'Chicago',
            visibleInAddressBook: true,
            phone: '1-866-433-5647',
          },
        ],
        children: [
          {
            active: true,
            addresses: [
              {
                billingAddress: true,
                country: {
                  isocode: 'US',
                },
                defaultAddress: false,
                firstName: 'Anil',
                id: '8796095250455',
                lastName: 'Gupta',
                line1: '3000 Lombard Street',
                postalCode: '94123',
                town: 'San Francisco',
              },
            ],
            children: [
              {
                active: true,
                addresses: [
                  {
                    billingAddress: true,
                    country: {
                      isocode: 'US',
                    },
                    defaultAddress: false,
                    firstName: 'Carla',
                    id: '8796095348759',
                    lastName: 'Torres',
                    line1: '1000 Bagby Street',
                    postalCode: 'Texas',
                    town: 'Houston',
                  },
                ],
                children: [],
                name: 'Custom Retail',
                noOfAdministrators: '345',
                noOfCustomers: '674',
                parent: 'Rustic Retail',
              },
            ],
            name: 'Rustic Retail',
            noOfAdministrators: '345',
            noOfCustomers: '674',
            parent: 'Rustic',
          },
          {
            active: true,
            children: [
              {
                active: true,
                addresses: [
                  {
                    billingAddress: true,
                    country: {
                      isocode: 'US',
                    },
                    defaultAddress: false,
                    firstName: 'Mingmei',
                    id: '8796095283223',
                    lastName: 'Wang',
                    line1: '700 E 50th Street',
                    postalCode: '10022',
                    town: 'New York',
                  },
                ],
                children: [],
                name: 'Services East',
                noOfAdministrators: '345',
                noOfCustomers: '674',
                parent: 'Rustic Services',
              },
              {
                active: true,
                addresses: [
                  {
                    billingAddress: true,
                    country: {
                      isocode: 'US',
                    },
                    defaultAddress: false,
                    firstName: 'Matheu',
                    id: '8796095315991',
                    lastName: 'Silva',
                    line1: '3000 Canyon Lake Drive',
                    postalCode: '90068',
                    town: 'Los Angeles',
                  },
                ],
                children: [],
                name: 'Services West',
                noOfAdministrators: '345',
                noOfCustomers: '674',
                parent: 'Rustic Services',
              },
            ],
            name: 'Rustic Services',
            noOfAdministrators: '345',
            noOfCustomers: '674',
            parent: 'Rustic',
          },
        ],
        name: 'Rustic',
        noOfAdministrators: '45',
        noOfCustomers: '64',
      },
    };
  },
};
</script>
