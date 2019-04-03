import Swagger from 'swagger-client';
import * as types from '../constants/actionTypes';


var swaggerClient = null;
var connectionPending = true;

var responseInterceptor=  (res) => {
    // console.log(res);
  }


var loadClient = function ( url, dispatch, config) {
  return new Promise( (resolve, reject) => {
    if ( swaggerClient != null ) {
      resolve( swaggerClient );

    } else {
      
      return Swagger( {url:url, responseInterceptor: responseInterceptor} ).then( client => {
        config.isConnected = true
        config.isConnectionPending = false
        swaggerClient = client; 
        resolve( client );
      } )
    }
  } );
}

export default function swaggerMiddleware( opts, config ) {
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
