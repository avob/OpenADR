import Swagger from 'swagger-client';

var swaggerClient = null;

var loadClient = function ( url ) {
  return new Promise( (resolve, reject) => {
    if ( swaggerClient != null ) {
      resolve( swaggerClient );
    } else {
      return Swagger( url ).then( client => {
        swaggerClient = client; resolve( client )
      } )
    }
  } );
}

export default function swaggerMiddleware( opts ) {
  return store => next => action => {
    console.log( action.type )
    if ( !action.swagger ) {
      return next( action );
    }

    loadClient( opts.url )
      .then( (client) => {
        action.swagger( client );
      } )


  }
}
