import { applyMiddleware, compose, createStore as createReduxStore } from 'redux'
import thunk from 'redux-thunk'
import { browserHistory } from 'react-router'
import makeRootReducer from './reducers'
import { updateLocation } from './location'
import swaggerClient from 'redux-swagger-client'
import swagger from 'swagger-client'

import notificationSystemMiddleware from '../components/NotificationHandler/modules/notificationHandler'

import config from "configuration";

const createStore = (initialState = {}, reload) => {
  // ======================================================
  // Middleware Configuration
  // ======================================================
  // var login = localStorage.getItem("login");
  // var pass = localStorage.getItem("pass");
  // if(login == null || pass == null){
  //     login =prompt("Login?");
  //     pass = prompt("Pass?");
  //     localStorage.setItem("login", login);
  //     localStorage.setItem("pass", pass);

  // }
  // config.user = login;

  const swaggerOpts = {
    url:config.vtnSwaggerUrl,

    success: function(e){
      console.log("Successfully connect to Swagger Backend")
      config.isConnectionPending = false; 
      config.isConnected = true;
      reload(true); 
    },
    failure: function(e){
      console.error("Can't connect to Swagger Backend");
      console.log(e);
      config.isConnectionPending = false; 
      config.isConnected = false; 
      reload(false); 
    }
  };

 if(reload != null){
    config.isConnectionPending = true;
    config.isConnected = false;
    new swagger(swaggerOpts);
  }
    

  


  var swaggerMiddleware = swaggerClient(swaggerOpts);
  
  
  const middleware = [thunk
    , swaggerMiddleware
    , notificationSystemMiddleware()
  ]
  

  // ======================================================
  // Store Enhancers
  // ======================================================
  const enhancers = []
  let composeEnhancers = compose

  if (__DEV__) {
    if (typeof window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ === 'function') {
      composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    }
  }

  // ======================================================
  // Store Instantiation and HMR Setup
  // ======================================================

  const store = createReduxStore(
    makeRootReducer(),
    initialState,
    composeEnhancers(
      applyMiddleware(...middleware),
      ...enhancers
    )
  )
  store.asyncReducers = {}

  // To unsubscribe, invoke `store.unsubscribeHistory()` anytime
  store.unsubscribeHistory = browserHistory.listen(updateLocation(store))

  if (module.hot) {
    module.hot.accept('./reducers', () => {
      const reducers = require('./reducers').default
      store.replaceReducer(reducers(store.asyncReducers))
    })
  }

  return store
}

export default createStore
