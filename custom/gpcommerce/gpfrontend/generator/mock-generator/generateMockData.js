/* This script generates mock data for local development.
   This way you don't have to point to an actual API,
   but you can enjoy realistic, but randomized data,
   and rapid page loads due to local, static data.
 */

const jsf = require('json-schema-faker');
const faker = require('faker');
const fs = require('fs');
const path = require('path');

/**
 * Extending json schema faker with fake data
 */
jsf.extend('faker', () => faker);

/**
 * Mock schema references
 */
const globalMockDataSchema = require('../../src/data/mocks/global-mock-schema');
const userMockDataSchema = require('../../src/data/mocks/user/user-mock-schema');
const loginMockDataSchema = require('../../src/data/mocks/login/login-mock-schema');


/**
 * Global config mockup
 */
const globalMockDataSchemaJson = JSON.stringify(jsf(globalMockDataSchema));

fs.writeFile(path.join(__dirname, '../../src/data/mocks/global-mock-response.json'), globalMockDataSchemaJson, (err) => {
  if (err) {
    return console.log(err);
  }
  console.log('Global Mock data generated.');
});

function independentMockGeneration() {
  /**
   * configuration for user data
   */
  const userMockDataSchemaJson = JSON.stringify(jsf(userMockDataSchema));

  fs.writeFile(path.join(__dirname, '../../src/data/mocks/user/user-mock-response.json'), userMockDataSchemaJson, (err) => {
    if (err) {
      return console.log(err);
    }
    console.log('User Mock data generated.');
  });

  /**
   * configuration for login data
   */
  const loginMockDataSchemaJson = JSON.stringify(jsf(loginMockDataSchema));

  fs.writeFile(path.join(__dirname, '../../src/data/mocks/login/login-mock-response.json'), loginMockDataSchemaJson, (err) => {
    if (err) {
      return console.log(err);
    }
    console.log('Login Mock data generated.');
  });
}

independentMockGeneration();
