import { createStore, compose, applyMiddleware } from 'redux';
import reduxImmutableStateInvariant from 'redux-immutable-state-invariant';
import thunk from 'redux-thunk';
import {createBrowserHistory} from 'history';
// 'routerMiddleware': the new way of storing route changes with redux middleware since rrV4.
import { connectRouter, routerMiddleware } from 'connected-react-router';
import swaggerMiddleware from './swaggerMiddleware'
import swagger from 'swagger-client'
import rootReducer from '../reducers';
// publicPath env
console.log(process.env)
const publicPath = process.env.REACT_APP_BASENAME || null;

export const history = createBrowserHistory({ basename: publicPath });
const connectRouterHistory = connectRouter( history );

var swaggerUrl = process.env.REACT_APP_SWAGGER_URL || 'https://vtn.oadr.com:8181/testvtn/v2/api-docs';
export var config = {
//		vtnSwaggerUrl: 'https://192.168.1.11:8181/testvtn/v2/api-docs',
//  vtnSwaggerUrl: 'https://192.168.10.42:8181/testvtn/v2/api-docs',
  vtnSwaggerUrl: swaggerUrl,
  isConnectionPending: true,
  isConnected: false
};

function configureSwaggerMiddleware() {

  swagger.http.withCredentials = true
  const swaggerOpts = {
    url: config.vtnSwaggerUrl
  };
  return swaggerMiddleware( swaggerOpts, config );
}

function configureStoreProd( initialState ) {
  const reactRouterMiddleware = routerMiddleware( history );


  const middlewares = [
    thunk,
    reactRouterMiddleware,
    configureSwaggerMiddleware(config),
  ];

  return createStore(
    connectRouterHistory( rootReducer ),
    initialState,
    compose( applyMiddleware( ...middlewares ) )
  );
}

function configureStoreDev( initialState ) {
  const reactRouterMiddleware = routerMiddleware( history );
  const middlewares = [
    reduxImmutableStateInvariant(),
    thunk,
    reactRouterMiddleware,
    configureSwaggerMiddleware(),
  ];

  const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose; // add support for Redux dev tools
  const store = createStore(
    connectRouterHistory( rootReducer ),
    initialState,
    composeEnhancers( applyMiddleware( ...middlewares ) )
  );

  if ( module.hot ) {
    // Enable Webpack hot module replacement for reducers
    module.hot.accept( '../reducers', () => {
      const nextRootReducer = require( '../reducers' ).default; // eslint-disable-line global-require
      store.replaceReducer( connectRouterHistory( nextRootReducer ) );
    } );
  }

  return store;
}

const configureStore = process.env.NODE_ENV === 'production' ? configureStoreProd : configureStoreDev;

export default configureStore;


