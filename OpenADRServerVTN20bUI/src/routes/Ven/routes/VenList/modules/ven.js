import {actionHandlers} from '../../../../../components/ReactComponentLifecycle/modules/reactComponentLifecycle.js'
import {notifySuccess, notifyError} from '../../../../../components/NotificationHandler/modules/notificationHandler.js'

// ------------------------------------
// Constants
// ------------------------------------
export const FETCH_VEN = 'FETCH_VEN'
export const FETCH_VEN_SUCCESS = 'FETCH_VEN_SUCCESS'
export const FETCH_VEN_ERROR = 'FETCH_VEN_ERROR'

export const CREATE_VEN = 'CREATE_VEN'
export const CREATE_VEN_SUCCESS = 'CREATE_VEN_SUCCESS'
export const CREATE_VEN_ERROR = 'CREATE_VEN_ERROR'

// ------------------------------------
// Actions
// ------------------------------------

export const fetchVen = () => {
	return (dispatch, getState) => {
      dispatch({
        type    : FETCH_VEN
        , swagger: function(api) {
          api.apis["ven-controller"].listVenUsingGET( {responseContentType: 'application/json'}, function(data){
      		var vens = JSON.parse(data.data);
      		dispatch({
      			type: FETCH_VEN_SUCCESS
      			, payload: vens
            , notification: notifySuccess("Vens have been retrieved")
      		})

          }, function(err){
      		dispatch({
      			type: FETCH_VEN_ERROR
      			, payload: err
      		})
          });
        }
    })
  }
}

export const createVen = (ven) => {
  return (dispatch, getState) => {
      if(ven.type == "password") {
        var params = {
          dto: {
            username: ven.login,
            password: ven.password,
            oadrName:ven.name
          }
        }
        dispatch({
          type    : CREATE_VEN
          , swagger: function(api) {
            api.apis["ven-controller"].createVenUsingPOST(params, {responseContentType: 'application/json'}, function(data){
            var vens = JSON.parse(data.data);
            dispatch({
              type: CREATE_VEN_SUCCESS
              , notification: notifySuccess("Vens have been created")
            })

            }, function(err){
            dispatch({
              type: CREATE_VEN_ERROR
              , payload: err
              , notification: notifyError("Vens have not been created")
            })
            });
          }
        })
      }
      else if(ven.type == "x509_RSA") {
        var params = {
          dto: {
            type: "RSA",
            venCN: ven.name,
            venName:ven.name
          }
        }
        dispatch({
          type    : CREATE_VEN
          , swagger: function(api) {
            api.apis["oadr-20b-generate-x-509-ven-controller"].createX509VenUsingPOST(params, {responseContentType: 'application/octet-stream'}, function(data){
            dispatch({
              type: CREATE_VEN_SUCCESS
              , notification: notifySuccess("Vens have been created")
            })

            }, function(err){
            dispatch({
              type: CREATE_VEN_ERROR
              , payload: err
              , notification: notifyError("Vens have not been created")
            })
            });
          }
        })
      }
      else if(ven.type == "x509_ECC") {
        var params = {
          dto: {
            type: "ECC",
            venCN: ven.name,
            venName:ven.name
          }
        }
        dispatch({
          type    : CREATE_VEN
          , swagger: function(api) {
            api.apis["oadr-20b-generate-x-509-ven-controller"].createX509VenUsingPOST(params, {responseContentType: 'application/octet-stream'}, function(data){
            dispatch({
              type: CREATE_VEN_SUCCESS
              , notification: notifySuccess("Vens have been created")
            })

            }, function(err){
            dispatch({
              type: CREATE_VEN_ERROR
              , payload: err
              , notification: notifyError("Vens have not been created")
            })
            });
          }
        })
      }
      
  }
}

export const actions = {

}

const initialState = {
  vens: []
}

// ------------------------------------
// Action Handlers
// ------------------------------------
var ACTION_HANDLERS = {
	[FETCH_VEN] : (state, action) => {
		return state; 
	}
	, [FETCH_VEN_SUCCESS] : (state, action) => {
		return {
			vens : action.payload
		}; 
	}
	, [FETCH_VEN_ERROR] : (state, action) => {
		return {
			errors : action.payload
			, vens: []
		}; 
	}
  , [CREATE_VEN_ERROR] : (state, action) => {
    return {
      errors : action.payload
    }; 
  }
}

ACTION_HANDLERS = actionHandlers(ACTION_HANDLERS, initialState);

// ------------------------------------
// Reducer
// ------------------------------------

export default function reducer (state = initialState, action) {
  const handler = ACTION_HANDLERS[action.type]
  return handler ? handler(state, action) : state
}
