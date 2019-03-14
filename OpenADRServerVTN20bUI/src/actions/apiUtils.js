export function swaggerAction (actionType, getAction, getPayload, getNext) {
  return (dispatch, getState) => {
    dispatch( {
      type: actionType,
      swagger: function ( api ) {
          getAction(api)
          .then( data => {
           
            var msg = {
              type: actionType + "_SUCCESS"
            }
            if(getPayload) {
              var payload = getPayload(data);
              if(payload) {
                msg.payload = payload;
              }
            }
            dispatch(msg);
            if(getNext) {
              getNext(dispatch, getState);
            }
          } )
          .catch( err => {
            dispatch( {
              type: actionType + "_ERROR",
              payload: err
            } );
          } )
      }
    } )
  }
}

export var jsonResponseContentType = {
      responseContentType: 'application/json'
}

export var multipartResponseContentType = {
      responseContentType: 'multipart/form-data'
}

export var saveData = ( function () {
  var a = document.createElement( 'a' );
  document.body.appendChild( a );
  a.style = 'display: none';
  return function ( data, fileName ) {
     var url = window.URL.createObjectURL( data );
      a.href = url;
      a.download = fileName;
      a.click();
      window.URL.revokeObjectURL( url );
    
  };
}());

export var parseJsonData = (data) => {
  return JSON.parse( data.data );
}