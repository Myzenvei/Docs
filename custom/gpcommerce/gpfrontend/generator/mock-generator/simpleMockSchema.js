const loginSchema = {
  type: 'object',
  properties: {
    users: {
      type: 'array',
      minItems: 3,
      maxItems: 5,
      items: {
        id: {
          type: 'number',
          unique: true,
          minimum: 1,
        },
        firstName: {
          type: 'string',
          faker: 'name.firstName',
        },
        lastName: {
          type: 'string',
          faker: 'name.lastName',
        },
        email: {
          type: 'string',
          faker: 'internet.email',
        },
        device: {
          type: 'string',
          pattern: '^/dev/[^/]+(/[^/]+)*$',
        },
        address: {
          type: 'object',
          required: ['streetName', 'streetAddress', 'state', 'zipCode'],
          properties: {
            streetName: {
              type: 'string',
              faker: 'address.streetName',
            },
            streetAddress: {
              type: 'string',
              faker: 'address.streetAddress',
            },
            state: {
              type: 'string',
              faker: 'address.state',
            },
            zipCode: {
              type: 'string',
              faker: 'address.zipCode',
            },
          },
        },
      },
      required: ['id', 'firstName', 'lastName', 'email', 'address'],
    },
  },
  required: ['users'],
};

module.exports = loginSchema;
