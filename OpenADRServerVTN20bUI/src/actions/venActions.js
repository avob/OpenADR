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

var saveData = ( function () {
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
            saveData( data.data, ven.commonName + '-credentials.tar' )
            dispatch( {
              type: types.CREATE_VEN_SUCCESS,
              payload: ven
            } );
            history.push( '/ven' )
          } )
          .catch( err => {
            console.log( err )
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
            loadVen()( dispatch, getState )
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.DELETE_VEN_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const loadVenGroup = (username) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.LOAD_VEN_GROUP,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].listVenGroupUsingGET( {
          venID: username
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var group = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_VEN_GROUP_SUCCESS,
              payload: group
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.LOAD_VEN_GROUP_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const loadVenMarketContext = (username) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.LOAD_VEN_MARKET_CONTEXT,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].listVenMarketContextUsingGET( {
          venID: username
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var marketContext = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_VEN_MARKET_CONTEXT_SUCCESS,
              payload: marketContext
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.LOAD_VEN_MARKET_CONTEXT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const addVenMarketContext = (username, marketContextId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.ADD_VEN_MARKET_CONTEXT,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].addMarketContextToVenUsingPOST( {
          venID: username,
          marketContextId: marketContextId
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var marketContext = JSON.parse( data.data );
            loadVenMarketContext( username )( dispatch, getState )
            dispatch( {
              type: types.ADD_VEN_MARKET_CONTEXT_SUCCESS,
              payload: marketContext
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.ADD_VEN_MARKET_CONTEXT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const addVenGroup = (username, groupId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.ADD_VEN_GROUP,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].addGroupToVenUsingPOST( {
          venID: username,
          groupId: groupId
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var marketContext = JSON.parse( data.data );
            loadVenGroup( username )( dispatch, getState )
            dispatch( {
              type: types.ADD_VEN_GROUP_SUCCESS,
              payload: marketContext
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.ADD_VEN_GROUP_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}



export const removeVenMarketContext = (username, marketContextId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.REMOVE_VEN_MARKET_CONTEXT,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].deleteVenMarketContextUsingPOST( {
          venID: username,
          marketContextId: marketContextId
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            loadVenMarketContext( username )( dispatch, getState )
            dispatch( {
              type: types.REMOVE_VEN_MARKET_CONTEXT_SUCCESS,
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.REMOVE_VEN_MARKET_CONTEXT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const removeVenGroup = (username, groupId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.REMOVE_VEN_GROUP,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].deleteVenGroupUsingPOST( {
          venID: username,
          groupId: groupId
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            loadVenGroup( username )( dispatch, getState )
            dispatch( {
              type: types.REMOVE_VEN_GROUP_SUCCESS,
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.REMOVE_VEN_GROUP_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const registerPartyRequestReregistration = (username) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.REQUEST_REREGISTRATION_VEN,
      swagger: function ( api ) {
        api.apis[ 'oadr-20b-ven-controller' ].registerPartyRequestReregistrationUsingPOST( {
          venID: username,
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            dispatch( {
              type: types.REQUEST_REREGISTRATION_VEN_SUCCESS,
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.REQUEST_REREGISTRATION_VEN_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const registerPartyCancelPartyRegistration = (username) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.REQUEST_CANCEL_REGISTRATION_VEN,
      swagger: function ( api ) {
        api.apis[ 'oadr-20b-ven-controller' ].registerPartyCancelPartyRegistrationUsingPOST( {
          venID: username,
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            dispatch( {
              type: types.REQUEST_CANCEL_REGISTRATION_VEN_SUCCESS,
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.REQUEST_CANCEL_REGISTRATION_VEN_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const cleanRegistration = (username) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.REQUEST_CLEAN_REGISTRATION_VEN,
      swagger: function ( api ) {
        api.apis[ 'ven-controller' ].cleanRegistrationUsingPOST( {
          venID: username,
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            dispatch( {
              type: types.REQUEST_CLEAN_REGISTRATION_VEN_SUCCESS,
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.REQUEST_CLEAN_REGISTRATION_VEN_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const loadVenAvailableReport = (username) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.LOAD_VEN_AVAILABLE_REPORT,
      swagger: function ( api ) {
        api.apis[ 'oadr-20b-ven-controller' ].viewOtherReportCapabilityUsingGET( {
          venID: username,
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var report = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_VEN_AVAILABLE_REPORT_SUCCESS,
              payload: report
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.LOAD_VEN_AVAILABLE_REPORT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}

export const loadVenAvailableReportDescription = (username, reportSpecifierId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.LOAD_VEN_AVAILABLE_REPORT_DESCRIPTION,
      swagger: function ( api ) {
        api.apis[ 'oadr-20b-ven-controller' ].viewOtherReportCapabilityDescriptionUsingGET( {
          venID: username,
          reportSpecifierId: reportSpecifierId
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var report = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_VEN_AVAILABLE_REPORT_DESCRIPTION_SUCCESS,
              payload: report
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.LOAD_VEN_AVAILABLE_REPORT_DESCRIPTION_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}


export const loadVenRequestedReport = (username, reportSpecifierId) => {
  return (dispatch, getState) => {
    dispatch( {
      type: types.LOAD_VEN_REQUESTED_REPORT,
      swagger: function ( api ) {
        api.apis[ 'oadr-20b-ven-controller' ].viewReportRequestUsingGET( {
          venID: username,
          reportSpecifierId: reportSpecifierId
        }, {
          responseContentType: 'application/json'
        } )
          .then( data => {
            var report = JSON.parse( data.data );
            dispatch( {
              type: types.LOAD_VEN_REQUESTED_REPORT_SUCCESS,
              payload: report
            } );
          } )
          .catch( err => {
            console.log( err )
            dispatch( {
              type: types.LOAD_VEN_REQUESTED_REPORT_ERROR,
              payload: err
            } );
          } )
      }
    } )
  }
}




