const ip = require('ip')
const NODE_ENV = process.env.NODE_ENV || 'development'

const OUTPUT_DIR = process.env.OUTPUT_DIR || '/var/www/html/Server_OadrVtnInjectorUI/'
const BASE_PATH = process.env.BASE_PATH || '/'

const VTN_SWAGGER_URL = process.env.VTN_SWAGGER_URL || "https://vtn.oadr.com:8181/testvtn/v2/api-docs"
const VTN_SWAGGER_PASS = process.env.VTN_SWAGGER_PASS || null;
const VTN_SWAGGER_USER = process.env.VTN_SWAGGER_USER || null;

const VTN_PLATFORM = process.env.VTN_PLATFORM || "DEV";
const VTN_BOOTSTRAP_THEME = process.env.VTN_BOOTSTRAP_THEME || "https://bootswatch.com/3/cerulean/bootstrap.min.css";
// const VTN_BOOTSTRAP_THEME = process.env.VTN_BOOTSTRAP_THEME || "https://bootswatch.com/3/united/bootstrap.min.css"; //prod1
// const VTN_BOOTSTRAP_THEME = process.env.VTN_BOOTSTRAP_THEME || "https://bootswatch.com/3/cosmo/bootstrap.min.css"; // sandbox


console.log("NODE_ENV: "+NODE_ENV);
console.log("OUTPUT_DIR: "+OUTPUT_DIR);
console.log("BASE_PATH: "+BASE_PATH);
console.log("VTN_SWAGGER_URL: "+VTN_SWAGGER_URL);
console.log("VTN_SWAGGER_PASS: "+VTN_SWAGGER_PASS);
console.log("VTN_SWAGGER_USER: "+VTN_SWAGGER_USER);

console.log("VTN_PLATFORM: "+VTN_PLATFORM);
console.log("VTN_BOOTSTRAP_THEME: "+VTN_BOOTSTRAP_THEME);

module.exports = {
  /** The environment to use when building the project */
  env: NODE_ENV,
  /** The full path to the project's root directory */
  basePath: __dirname,
  /** The name of the directory containing the appliaction source code */
  srcDir: 'src',
  /** The file name of the application's entry point */
  main: 'main',
  /** The name of the directory in which to emit compiled assets */
   /** The name of the directory in which to emit compiled assets */
  // outDir: 'dist',
  outDir: OUTPUT_DIR,
  /** The base path for all projects assets (relative to the website root) */
  publicPath: NODE_ENV === 'development' ? `https://vtn.oadr.com:3000` : '',
  baseName:  BASE_PATH,
   // baseName: '/',
  /** Whether to generate sourcemaps */
  sourcemaps: true,
  /** A hash map of keys that the compiler should treat as external to the project */
  externals: {
    configuration: JSON.stringify({
      vtnSwaggerUrl: VTN_SWAGGER_URL,
      vtnSwaggerUser: VTN_SWAGGER_USER,
      vtnSwaggerPass: VTN_SWAGGER_PASS,
      vtnPlatform: VTN_PLATFORM,
      vtnBootstrapTheme: VTN_BOOTSTRAP_THEME,
      basePath: BASE_PATH
    })
  },
  /** A hash map of variables and their values to expose globally */
  globals: {
    
  },
  /** Whether to enable verbose logging */
  verbose: false,
  /** The list of modules to bundle separately from the core application code */
  vendors: [
    'react',
    'react-dom',
    'redux',
    'react-redux',
    'redux-thunk',
    'react-router',
  ],
}
