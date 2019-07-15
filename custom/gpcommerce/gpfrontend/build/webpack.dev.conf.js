'use strict';
const utils = require('./utils');
const webpack = require('webpack');
const config = require('../config');
const merge = require('webpack-merge');
const path = require('path');
const baseWebpackConfig = require('./webpack.base.conf');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const FriendlyErrorsPlugin = require('friendly-errors-webpack-plugin');
const portfinder = require('portfinder');
const fs = require('fs');
const HOST = process.env.HOST;
const PORT = process.env.PORT && Number(process.env.PORT);

/* Brand argument passed in the command line */
const brandSelected = process.env.brand || null;

/* Avoid starting Dev server if no brand is passed.*/
if (!brandSelected) {
  return;
}

const devWebpackConfig = merge(baseWebpackConfig, {
  module: {
    rules: utils.styleLoaders({
      sourceMap: config.dev.cssSourceMap,
      usePostCSS: true,
    }),
  },
  // cheap-module-eval-source-map is faster for development
  devtool: config.dev.devtool,

  // these devServer options should be customized in /config/index.js
  devServer: {
    clientLogLevel: 'warning',
    historyApiFallback: {
      rewrites: [{
        from: /.*/,
        to: path.posix.join(config.dev.assetsPublicPath,
          'src/' + brandSelected + '/' + brandSelected + '.html'),
      }, ],
    },
    hot: true,
    contentBase: false, // since we use CopyWebpackPlugin.
    compress: true,
    host: HOST || config.dev.host,
    port: PORT || config.dev.port,
    open: config.dev.autoOpenBrowser,
    overlay: config.dev.errorOverlay ? {
      warnings: false,
      errors: true
    } : false,
    publicPath: config.dev.assetsPublicPath,
    proxy: config.dev.proxyTable,
    quiet: true, // necessary for FriendlyErrorsPlugin
    watchOptions: {
      poll: config.dev.poll,
    },
    before(app) { //used to fetch the local mocked data
      app.all('/.api**', (req, res) => { // it will make the request as /.api****
        const requestedPath = req.path; // returns the request path
        const derivedMockPath = `${requestedPath}/${req.method}`; // returns the reqpath and method
        // the file name should be the {req.method}.json
        const mockPath = path.resolve(__dirname, `../static/assets/data/mock/`); // mock file needs to be located in this path where static will be in the same level as `src`
        const jsonResponse = JSON.parse(
          fs.readFileSync(`${mockPath}${derivedMockPath}.json`, 'utf8') //reads the mocked json file
        );
        setTimeout(() => {
          res.json(jsonResponse);
        }, 1000);
      });
    }
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env': require('../config/dev.env'),
    }),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NamedModulesPlugin(), // HMR shows correct file names in console on update.
    new webpack.NoEmitOnErrorsPlugin(),
    // https://github.com/ampedandwired/html-webpack-plugin
    new HtmlWebpackPlugin({
      filename: 'src/' + brandSelected + '/' + brandSelected + '.html',
      template: 'src/' + brandSelected + '/' + brandSelected + '.html',
      inject: true,
    }),
    // copy custom static assets
    new CopyWebpackPlugin([{
      from: path.resolve(__dirname, '../assets'),
      to: config.dev.assetsSubDirectory + '/assets',
      ignore: ['.*'],
    }, ]),
  ],
});

module.exports = new Promise((resolve, reject) => {
  portfinder.basePort = process.env.PORT || config.dev.port;
  portfinder.getPort((err, port) => {
    if (err) {
      reject(err);
    } else {
      // publish the new Port, necessary for e2e tests
      process.env.PORT = port;
      // add port to devServer config
      devWebpackConfig.devServer.port = port;

      // Add FriendlyErrorsPlugin
      devWebpackConfig.plugins.push(
        new FriendlyErrorsPlugin({
          compilationSuccessInfo: {
            messages: [
              `Your application is running here: http://${
                devWebpackConfig.devServer.host
              }:${port}`,
            ],
          },
          onErrors: config.dev.notifyOnErrors ?
            utils.createNotifierCallback() : undefined,
        })
      );

      resolve(devWebpackConfig);
    }
  });
});
