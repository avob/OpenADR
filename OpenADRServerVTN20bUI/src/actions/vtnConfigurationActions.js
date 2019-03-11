import * as types from '../constants/actionTypes';

export const loadVtnConfiguration = () => {
  return (dispatch) => {
    dispatch( {
      type: types.LOAD_VTN_CONFIGURATION,
      swagger: function ( api ) {
        api.apis[ 'oadr-20b-vtn-controller' ].viewConfUsingGET( {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var vtn = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_VTN_CONFIGURATION_SUCCESS,
              payload: vtn
            } );
          } )
          .catch( err => {
            dispatch( {
              type: types.LOAD_VTN_CONFIGURATION_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const loadMarketContext = () => {
  return (dispatch) => {
    dispatch( {
      type: types.LOAD_MARKET_CONTEXT,
      swagger: function ( api ) {
        api.apis[ 'market-context-controller' ].listMarketContextUsingGET( {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var marketContext = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_MARKET_CONTEXT_SUCCESS,
              payload: marketContext
            } );
          } )
          .catch( err => {
            dispatch( {
              type: types.LOAD_MARKET_CONTEXT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const createMarketContext = (marketContext) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.CREATE_MARKET_CONTEXT,
      swagger: function ( api ) {
        api.apis[ 'market-context-controller' ].createMarketContextUsingPOST( {
          dto: marketContext
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var marketContext = JSON.parse( data.data );

            dispatch( {
              type: types.CREATE_MARKET_CONTEXT_SUCCESS,
              payload: marketContext
            } );
            loadMarketContext()( dispatch, getState );
          } )
          .catch( err => {
            dispatch( {
              type: types.CREATE_MARKET_CONTEXT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const updateMarketContext = (marketContext) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.CREATE_MARKET_CONTEXT,
      swagger: function ( api ) {
        api.apis[ 'market-context-controller' ].updateMarketContextUsingPUT( {
          dto: marketContext
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var marketContext = JSON.parse( data.data );

            dispatch( {
              type: types.CREATE_MARKET_CONTEXT_SUCCESS,
              payload: marketContext
            } );
            loadMarketContext()( dispatch, getState );
          } )
          .catch( err => {
            dispatch( {
              type: types.CREATE_MARKET_CONTEXT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const deleteMarketContext = (marketContextId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.DELETE_MARKET_CONTEXT,
      swagger: function ( api ) {
        api.apis[ 'market-context-controller' ].deleteMarketContextByIdUsingDELETE( {
          marketContextId
        }, {
          responseContentType: 'application/json'
        } )
          .then( () => {
            dispatch( {
              type: types.DELETE_MARKET_CONTEXT_SUCCESS
            } );
            loadMarketContext()( dispatch, getState );
          } )
          .catch( err => {
            dispatch( {
              type: types.DELETE_MARKET_CONTEXT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}
// GROUPS

export const loadGroup = () => {
  return (dispatch) => {
    dispatch( {
      type: types.LOAD_GROUP,
      swagger: function ( api ) {
        api.apis[ 'group-controller' ].listGroupUsingGET( {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var marketContext = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_GROUP_SUCCESS,
              payload: marketContext
            } );
          } )
          .catch( err => {
            dispatch( {
              type: types.LOAD_GROUP_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const createGroup = (group) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.CREATE_GROUP,
      swagger: function ( api ) {
        api.apis[ 'group-controller' ].createGroupUsingPOST( {
          dto: group
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var group = JSON.parse( data.data );

            dispatch( {
              type: types.CREATE_GROUP_SUCCESS,
              payload: group
            } );
            loadGroup()( dispatch, getState );
          } )
          .catch( err => {
            dispatch( {
              type: types.CREATE_GROUP_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const updateGroup = (group) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.CREATE_GROUP,
      swagger: function ( api ) {
        api.apis[ 'group-controller' ].updateGroupUsingPUT( {
          dto: group
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var group = JSON.parse( data.data );

            dispatch( {
              type: types.CREATE_GROUP_SUCCESS,
              payload: group
            } );
            loadGroup()( dispatch, getState );
          } )
          .catch( err => {
            dispatch( {
              type: types.CREATE_GROUP_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const deleteGroup = (groupId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.DELETE_GROUP,
      swagger: function ( api ) {
        api.apis[ 'group-controller' ].deleteGroupByIdUsingDELETE( {
          groupId
        }, {
          responseContentType: 'application/json'
        } )
          .then( () => {
            dispatch( {
              type: types.DELETE_GROUP_SUCCESS
            } );
            loadGroup()( dispatch, getState );
          } )
          .catch( err => {
            dispatch( {
              type: types.DELETE_GROUP_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}
