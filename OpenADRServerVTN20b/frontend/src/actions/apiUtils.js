export function swaggerAction (actionType, getAction, getPayload, getNext) {
  return (dispatch, getState) => {
    dispatch( {
      type: actionType,
      swagger: function ( api ) {
          getAction(api)
          .then( (resp) => {
            var msg = {
              type: actionType + "_SUCCESS"
            }
            if(getPayload) {
              var payload = getPayload(resp);
              if(payload) {
                msg.payload = payload;
              }
            }
            console.log(resp.headers)
            if(resp.headers && resp.headers["x-total-count"] && resp.headers["x-total-page"]) {
            	msg.total = parseInt(resp.headers["x-total-count"]);
            	msg.totalPage = parseInt(resp.headers["x-total-page"]);
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