const path = require('path');
const vueLoaderConfig = require('./build/vue-loader.conf');

// const styleguidist = require('vue-styleguidist');

module.exports = {
  components: [
    'src/components/common/stepper-control/stepper-control.vue',
    'src/components/common/radio-button-group/radio-button-group.vue',
  ],
  webpackConfig: Object.assign({},
    require('./build/webpack.base.conf.js'), {
      /* Custom config options */
      module: {
        rules: [
          // Vue loader
          {
            test: /\.vue$/,
            exclude: /node_modules/,
            loader: 'vue-loader',
            options: vueLoaderConfig,
          },
          {
            // Babel loader, will use your project’s .babelrc
            test: /\.js$/,
            loader: 'babel-loader',
            exclude: /node_modules/,
            include: /src/
          },
          // less loader
          {
            test: /\.less$/,
            loader: 'css-loader'
          },
          // css loader
          {
            test: /\.css$/,
            use: ['style-loader', 'css-loader']
          }
        ]
      }
    }),
  showUsage: true,
  require: [
    path.join(__dirname, 'dist/static/css/bundle.common.min.css'),
    path.join(__dirname, 'dist/static/css/bundle.component.min.css'),
  ]
};
