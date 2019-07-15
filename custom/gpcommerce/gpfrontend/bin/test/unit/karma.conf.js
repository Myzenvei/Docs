// This is a karma config file. For more details see
//   http://karma-runner.github.io/0.13/config/configuration-file.html
// we are also using it with karma-webpack
//   https://github.com/webpack/karma-webpack

const webpackConfig = require('../../build/webpack.test.conf');
const grep = require('karma-webpack-grep');

module.exports = function karmaConfig(config) {
  webpackConfig.plugins = (webpackConfig.plugins || []).concat(grep({
    grep: config.grep,
    basePath: '.',
    testContext: '../../src/',
  }));

  config.set({
    // to run in additional browsers:
    // 1. install corresponding karma launcher
    //    http://karma-runner.github.io/0.13/config/browsers.html
    // 2. add it to the `browsers` array below.
    browsers: ['ChromeHeadless'], // ['ChromeHeadless', 'PhantomJS']
    frameworks: ['mocha', 'sinon-chai'],
    reporters: ['spec', 'coverage'],
    files: ['./index.js'],
    preprocessors: {
      './index.js': ['webpack', 'sourcemap'],
    },
    webpack: webpackConfig,
    webpackMiddleware: {
      noInfo: true,
    },
    // singleRun: false,
    // autoWatch: true,
    coverageReporter: {
      dir: './coverage',
      reporters: [{
        type: 'lcov',
        subdir: '.',
      }, {
        type: 'text-summary',
      },
      ],
    },
  });
};
