import * as types from '../constants/actionTypes';

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
