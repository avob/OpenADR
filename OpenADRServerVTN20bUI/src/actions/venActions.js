import * as types from '../constants/actionTypes';
import { history } from '../store/configureStore';


export const loadVen = () => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.LOAD_VEN,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].listVenUsingGET( {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var ven = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_VEN_SUCCESS,
              payload: ven
            } );
          } )
          .catch( err => {
            dispatch( {
              type: types.LOAD_VEN_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const loadVenDetail = (username) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.LOAD_VEN_DETAIL,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].findVenByUsernameUsingGET( {
          venID: username
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var ven = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_VEN_DETAIL_SUCCESS,
              payload: ven
            } );
          } )
          .catch( err => {
            dispatch( {
              type: types.LOAD_VEN_DETAIL_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

var saveData = (function () {
    var a = document.createElement("a");
    document.body.appendChild(a);
    a.style = "display: none";
    return function (data, fileName) {

        var url = window.URL.createObjectURL(data);
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
    };
}());

export const createVen = (ven) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.CREATE_VEN,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].createVenUsingPOST( {
          dto: ven
        }, {
          responseContentType: 'multipart/form-data'
        } )
          .then( data => {
            saveData(data.data, "creadentials.tar")
            dispatch( {
              type: types.CREATE_VEN_SUCCESS,
              payload: ven
            } );
            history.push( '/ven' )
          } )
          .catch( err => {
            console.log(err)
            dispatch( {
              type: types.CREATE_VEN_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const deleteVen = (venId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.DELETE_VEN,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].deleteVenByUsernameUsingDELETE( {
          venID: venId
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
       
            dispatch( {
              type: types.DELETE_VEN_SUCCESS,
            } );
            loadVen()(dispatch, getState)
          } )
          .catch( err => {
            console.log(err)
            dispatch( {
              type: types.DELETE_VEN_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}
