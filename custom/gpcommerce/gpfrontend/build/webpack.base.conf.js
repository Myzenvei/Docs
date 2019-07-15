const path = require('path');
const utils = require('./utils');
const config = require('../config');
const vueLoaderConfig = require('./vue-loader.conf');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const CleanCSSPlugin = require('less-plugin-clean-css');
const glob = require('glob');
const globby = require('globby');

const useEslint =
  process.env.NODE_ENV === 'production'
    ? config.build.useEslint
    : config.dev.useEslint;
/* Brands supported */

/**
 * updated the brand names as per the site-ids provided and also updated the folder names
 */
const brands = [
  'all',
  'gp-pro',
  'mardi-gras',
  'vanity-fair',
  'gp-employee',
  'dixie',
  'b2c-white-label',
  'b2b-white-label',
  'gp-xpress',
  'copper-crane',
  'brawny',
  'innovia',
  'sparkle',
];
const files = ['css', 'js'];
let commonEntryLessFiles,
  componentEntryLessFiles,
  entryFiles = {};

/* getting brand argument passed in the command line */
const brandSelected = process.env.brand || null;
const filesToBuild = process.env.entry || null;
let brandName;

const createLintingRule = () => ({
  test: /\.(js|vue)$/,
  loader: 'eslint-loader',
  enforce: 'pre',
  include: [resolve('src'), resolve('test')],
  options: {
    formatter: require('eslint-friendly-formatter'),
    emitWarning: !config.dev.showEslintErrorsInOverlay,
  },
});

const configRules = [
  ...(useEslint ? [createLintingRule()] : []),
  {
    test: /\.vue$/,
    loader: 'vue-loader',
    options: vueLoaderConfig,
  },
  {
    test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
    loader: 'url-loader',
    options: {
      limit: 10000,
      name: utils.assetsPath('img/[name].[ext]'),
    },
  },
  {
    test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
    loader: 'url-loader',
    options: {
      limit: 10000,
      name: utils.assetsPath('media/[name].[ext]'),
    },
  },
  {
    test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
    loader: 'url-loader',
    options: {
      limit: 10000,
      name: utils.assetsPath('fonts/[name].[ext]'),
    },
  },
];

function resolve(dir) {
  return path.join(__dirname, '..', dir);
}

/* Check and validate brand argument passed */
if (brands.includes(brandSelected)) {
  brandName = brandSelected;
  console.info(`Brand Selected: ${brandSelected} is supported`);
} else {
  console.info(
    `Brand argument: ${
      brandSelected
    } passed do not exist please select brand input from the following list`,
  );
  console.info('Brands', brands);
  return;
}

/* Checking files to build based files entry type parameter passed */
if (filesToBuild) {
  if (files.includes(filesToBuild.toLowerCase())) {
    console.info('Building files of type', filesToBuild);
  } else {
    console.info(
      `files argument: ${
        filesToBuild
      } passed do not exist please select file input from the following list`,
    );
    console.info('files', files);
    return;
  }
}

/**
 * Brand Entry Files
 */

/*  Common entry files */
if (!filesToBuild || (filesToBuild && filesToBuild.toLowerCase() === 'css')) {
  entryFiles['bundle.common.min'] = globby.sync([
    `${__dirname}../../src/styles/styles.less`,
  ]);

  if (brandSelected === 'all') {
    brands.forEach((name) => {
      if (name !== 'all') {
        /* Brand Component entry files */
        entryFiles[`${name}.styles.min`] = glob.sync(
          `${__dirname}../../src/${name}/styles/${name}-styles.less`,
        );
      }
    });
  } else {
    /* Brand Component entry files */
    entryFiles[`${brandSelected}.styles.min`] = glob.sync(
      `${__dirname
      }../../src/${brandSelected}/styles/${brandSelected}-styles.less`,
    );
  }

  /* Loading CSS loader */
  configRules.push({
    test: /\.css$/,
    use: ExtractTextPlugin.extract({
      fallback: 'style-loader',
      use: 'css-loader',
    }),
  });
}

/* Verifying the condition if it is js only */
if (!filesToBuild || (filesToBuild && filesToBuild.toLowerCase() === 'js')) {
  /**
   * Checking the condition if the brandName
   *  choosen is valid or not otherwise
   *  we will be trggering build for all brandNames
   */
  if (brandName && brandName !== 'all') {
    entryFiles[brandName] = `./src/${brandName}/${brandName}.js`;
  } else if (brandName === 'all') {
    brands.forEach((name) => {
      if (name !== 'all') {
        entryFiles[name] = `./src/${name}/${name}.js`;
      }
    });
  }
  /* Loading Js loader */
  configRules.push({
    test: /\.js$/,
    loader: 'babel-loader',
    include: [
      resolve('src'),
      resolve('test'),
      resolve('node_modules/webpack-dev-server/client'),
    ],
  });
}

module.exports = {
  context: path.resolve(__dirname, '../'),
  entry: entryFiles,
  output: {
    path: config.build.assetsRoot,
    filename: '[name]',
    publicPath:
      process.env.NODE_ENV === 'production'
        ? config.build.assetsPublicPath
        : config.dev.assetsPublicPath,
  },
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      vue$: 'vue/dist/vue.esm.js',
      '@': resolve('src'),
      components: resolve(__dirname, 'src', 'components'),
      common: resolve(__dirname, 'src', 'components', 'common'),
      modules: resolve(__dirname, 'src', 'modules'),
    },
  },
  module: {
    rules: configRules,
  },
  plugins: [
    // lessGlobPlugin,
    new ExtractTextPlugin(`${config.build.assetsSubDirectory}/css/[name].css`),
  ],
  node: {
    // prevent webpack from injecting useless setImmediate polyfill because Vue
    // source contains it (although only uses it if it's native).
    setImmediate: false,
    // prevent webpack from injecting mocks to Node native modules
    // that does not make sense for the client
    dgram: 'empty',
    fs: 'empty',
    net: 'empty',
    tls: 'empty',
    child_process: 'empty',
  },
};
