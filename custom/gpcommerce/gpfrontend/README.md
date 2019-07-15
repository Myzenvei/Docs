# README

> A Vue.js Project with component generator, Webapck, Eslint(airbnb rules), Less integration, unit testing (mocha and karma)

## Prerequisites

- Node v8.3.0 +
- NPM v5.6.0 +

### Technologies

The client side application is comprised of the following technologies:

#### For generating Static Markup on Server Side (component Library)

- [Vue.js](https://vuejs.org)
  Vue is a progressive framework for building user interfaces. Unlike other monolithic frameworks, Vue is designed from the ground up to be incrementally adoptable. The core library is focused on the view layer only, and is easy to pick up and integrate with other libraries or existing projects.

#### HTML, CSS and JavaScript

- BEM - Block Element Modifier is a methodology that helps you to create reusable components and code sharing in front-end development (http://getbem.com/)
- [TODO check this and remove] SASS - Sass is the most mature, stable, and powerful professional grade CSS extension language in the world (https://sass-lang.com/)
- LESS - Less (which stands for Leaner Style Sheets) is a backwards-compatible language extension for CSS. This is the official documentation for Less, the language and Less.js, the JavaScript tool that converts your Less styles to CSS styles. (http://lesscss.org/)

#### Build Tools, Transpiler & Package Manager

- [Babel](https://babeljs.io/)
- [Webpack](https://webpack.js.org/)
- [NPM](https://www.npmjs.com/)

### Developer Tools

#### [VS Code](https://code.visualstudio.com/)

Visual Studio Code is a free code editor redefined and optimized for building and debugging modern web and cloud applications.

##### VS Code - User Preference

```json
{
  "editor.formatOnSave": true,
  "[handlebars]": {
    "editor.formatOnSave": true
  },
  "editor.renderWhitespace": "all",
  "prettier.singleQuote": true,
  "prettier.trailingComma": "all",
  "prettier.arrowParens": "always",
  "workbench.editor.enablePreview": false,
  "prettier.eslintIntegration": true,
  "window.zoomLevel": 1
}
```

##### VS Code Plugins

- [Editor Config](https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig)
- [Prettier - Code formatter](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode)
- [JavaScript ESLint](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint)
- [SASS linter](https://marketplace.visualstudio.com/items?itemName=glen-84.sass-lint)

### Getting Started - Code & Environment - Setup

- Run the following command to resolve ssl issues before npm install

```bash
$ npm install npm -g --ca=null

or (try this only if the above command is not working)

$ npm config set strict-ssl false
```

- Go to the root folder (where package.json is available) and run the following command:s

```bash
$ npm install
```

- Go to the root folder and then to generator folder (where package.json is available) and run the following command:s

```bash
$ npm install
```

- Go back to the root folder and then run below command to start the server:

For Mac
```bash
$ brand=<brand-name> npm start
```
For Windows
```bash
$ set brand=<brand-name>&&npm run start
```

## <brand-name> is name of the brand i.e. gp-pro, gp-employee-store, dixie, mardi-gras, vanity-fair

## Example: brand=gp-pro npm run dev

- In order to test the frontend application on mobile we need to run the below command.
- It will by default run on 0.0.0.0:8080. We need to replace the host name [0.0.0.0] with your machine IP address.
- Ex: xx.xx.xx.xx:8080 will open the application on the devices or any other laptops which are on the same network.

For Mac
```bash
$ brand=<brand-name> npm run host
```

For Windows
```bash
$ set brand=<brand-name>&&npm run host
```

#### Create FE Components

Use the below command to create any new component:

```bash
$ npm run vue-component <component-name>
(or)
$ node generator/module-generator <component-name>

example:
$ npm run vue-component sample
```

Below command is used to create a component in a given folder

```bash
$ npm run vue-component <folder-path>/<component-name>
(or)
$ node generator/module-generator <folder-path>/<component-name>

example:
$ npm run vue-component parent/sample
```

#### Creating Mock APIs

API Blueprint is A powerful high-level API description language for web APIs. Check out the [examples](https://github.com/apiaryio/api-blueprint/tree/master/examples) on API Blueprint for creating mock apis.

Note: Refer src/data/mocks/mock-sample-api.md for sample mock structure.

#### Run Mock server

Run below command

```bash
$ npm install -g drakov
```

Set the attribute is-mock="true" in application-auth component to run the application with mocks. Mock server is running with port '3000'.

Mock server is running with dev command

For Mac
```bash
$ brand=<brand-name> npm run dev
```

For Windows
```bash
$ set brand=<brand-name>&&npm run dev
```

## <brand-name> is name of the brand i.e. gp-pro, gp-employee, dixie, mardi-gras, vanity-fair

## Example: brand=gp-pro npm run dev

To run the mock-server independently for other environments

```bash
$ npm run mock-server
```

## Additional commands

```bash
# serve with hot reload at localhost:8080
## For Mac:
brand=<brand-name> npm run dev
## For Windows:
set brand=<brand-name>&&npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report

# to copy or update webroot/_ui/common folder in gpcommercestorefront and gpb2bstorefront
gulp

# To build and copy into gpcommercestorefront and gpb2bstorefront
## For Mac:
brand=<brand-name> npm run deploy
## For Windows:
set brand=<brand-name>&&npm run deploy

## <brand-name> is name of the brand i.e. gp-pro, gp-employee, dixie, mardi-gras, vanity-fair or all (to build and deploy all the brands)
## Example: set brand=all&&npm run deploy

# To build only specific files i.e. CSS and Js files and then copy into  gpcommercestorefront and gpb2bstorefront
## For Mac:
brand=<brand-name> entry=<file-type> npm run deploy
## For Windows: (TO validate and Update !!)
set brand=<brand-name>&&entry=<file-type>&&npm run deploy
## <brand-name> is name of the brand i.e. gp-pro, gp-employee, dixie, mardi-gras, vanity-fair or all (to build and deploy all the brands)
## <file-type> will be js or css
## Example: brand=gp-pro entry=js npm run deploy

# run all tests
npm test

# run individual component test
npm run karma <folder-path>/<spec-file>.js
## Note: <folder-path> should be from components folder
## Example: npm run karma /components/test/test.spec.js

# run unit tests
npm run unit

# run e2e tests
npm run e2e

# Generate style guide
npm run styleguide

# Generate style guide for offline
npm run styleguide:build

Note: Before starting styelguide, make sure dist folder is created/updated by using 'npm run build' command. CSS from dist folder will be referenced in style guide.
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).
