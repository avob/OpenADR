import Swagger from 'swagger-client';
import * as types from '../constants/actionTypes';

import { config, history } from './configureStore';


var swaggerClient = null;

var responseInterceptor=  (res) => {
     console.log(res);
  }


var loadClient = function ( url, dispatch) {
  return new Promise( (resolve, reject) => {
    if ( swaggerClient != null ) {
      resolve( swaggerClient );

    } else {
      var params = {url:url, responseInterceptor: responseInterceptor
        , requestInterceptor: req => {
          if(config.username != null && config.password != null) {
            var encoded = btoa(config.username +":"+config.password);
            req.headers["Authorization"] = "Basic "+ encoded
          }
        }};

      
      return Swagger( params ).then( client => {
        config.isConnected = true
        config.isConnectionPending = false
        // dispatch({
        //   type: types.LOGIN_USER_SUCCESS
        // })
        swaggerClient = client; 
        resolve( client );
      } ).catch(err => {
        config.isConnected = false
        config.isConnectionPending = false
        dispatch({
          type: types.LOGIN_USER_ERROR,
          payload: err
        });
        history.push("/login")
        swaggerClient = null;
      });
    }
  } );
}

export default function swaggerMiddleware( opts ) {
  return store => next => action => {
    
    console.log( action.type )
    if(action.type.indexOf("_ERROR") > -1){
      console.log(action.payload)
    }

    if ( !action.swagger ) {
      return next( action );
    }

    loadClient( opts.url, store.dispatch, config )
      .then( (client) => {
        action.swagger( client );
      } )


  }
}
