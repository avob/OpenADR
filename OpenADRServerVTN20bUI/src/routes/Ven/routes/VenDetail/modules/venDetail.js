import {actionHandlers} from '../../../../../components/ReactComponentLifecycle/modules/reactComponentLifecycle.js'
import {notifySuccess, notifyError} from '../../../../../components/NotificationHandler/modules/notificationHandler.js'



// ------------------------------------
// Constants
// ------------------------------------
export const FETCH_VEN_DETAIL = 'FETCH_VEN_DETAIL'
export const FETCH_VEN_DETAIL_SUCCESS = 'FETCH_VEN_DETAIL_SUCCESS'
export const FETCH_VEN_DETAIL_ERROR = 'FETCH_VEN_DETAIL_ERROR'

export const REQUEST_VEN_REGISTER_AVOBVENSERVICE = 'REQUEST_VEN_REGISTER_AVOBVENSERVICE'
export const REQUEST_VEN_REGISTER_AVOBVENSERVICE_SUCCESS = 'REQUEST_VEN_REGISTER_AVOBVENSERVICE_SUCCESS'
export const REQUEST_VEN_REGISTER_AVOBVENSERVICE_ERROR = 'REQUEST_VEN_REGISTER_AVOBVENSERVICE_ERROR'

export const SEND_VEN_REGISTER_AVOBVENSERVICE = 'SEND_VEN_REGISTER_AVOBVENSERVICE'
export const SEND_VEN_REGISTER_AVOBVENSERVICE_SUCCESS = 'SEND_VEN_REGISTER_AVOBVENSERVICE_SUCCESS'
export const SEND_VEN_REGISTER_AVOBVENSERVICE_ERROR = 'SEND_VEN_REGISTER_AVOBVENSERVICE_ERROR'

export const REQUEST_REREGISTRATION = 'REQUEST_REREGISTRATION'
export const REQUEST_REREGISTRATION_SUCCESS = 'REQUEST_REREGISTRATION_SUCCESS'
export const REQUEST_REREGISTRATION_ERROR = 'REQUEST_REREGISTRATION_ERROR'

export const CANCEL_REGISTRATION = 'CANCEL_REGISTRATION'
export const CANCEL_REGISTRATION_SUCCESS = 'CANCEL_REGISTRATION_SUCCESS'
export const CANCEL_REGISTRATION_ERROR = 'CANCEL_REGISTRATION_ERROR'

export const UPDATE_VEN = 'UPDATE_VEN'
export const UPDATE_VEN_SUCCESS = 'UPDATE_VEN_SUCCESS'
export const UPDATE_VEN_ERROR = 'UPDATE_VEN_ERROR'


var dispatchSuccessActionHandler = function(dispatch, action, message){
  return function(){
    var toDispatch = {
      type    : action
      , notification: notifySuccess(message)
    }
    dispatch(toDispatch);
  }
}

var dispatchErrorActionHandler = function(dispatch, action, message){
  return function(){
    var toDispatch = {
      type    : action
      , notification: notifySuccess(message)
    }
    dispatch(toDispatch);
  }
}

// ------------------------------------
// Actions
// ------------------------------------
export const requestReregistration = function(venId) {
    return (dispatch, getState) => {
        dispatch({
          type    : REQUEST_REREGISTRATION
          , swagger: function(api) {
              api.apis["oadr-20b-ven-controller"].registerPartyRequestReregistrationUsingPOST(
                {venID: venId}
                , dispatchSuccessActionHandler(dispatch
                  , REQUEST_REREGISTRATION_SUCCESS
                  , "Ven re-register payload have been requested")
                , dispatchErrorActionHandler(dispatch
                  , REQUEST_REREGISTRATION_ERROR
                  , "Ven re-register payload have not been requested")
              );
          }
      })
    }
}

export const cancelRegistration = function(venId) {
    return (dispatch, getState) => {
        dispatch({
          type    : CANCEL_REGISTRATION
          , swagger: function(api) {
              api.apis["oadr-20b-ven-controller"].registerPartyCancelPartyRegistrationUsingPOST(
                {venID: venId}
                , dispatchSuccessActionHandler(dispatch
                  , CANCEL_REGISTRATION_SUCCESS
                  , "Ven cancel registration payload have been sent")
                , dispatchErrorActionHandler(dispatch
                  , CANCEL_REGISTRATION_ERROR
                  , "Ven cancel registration payload have not been sent")
              );
          }
      })
    }
}

export const requestVenRegisterService = function(venId) {
    return (dispatch, getState) => {
        dispatch({
          type    : REQUEST_VEN_REGISTER_AVOBVENSERVICE
          , swagger: function(api) {
            api.apis["oadr-20b-ven-controller"].requestRegisterReportUsingPOST(
              {venID: venId}
              , dispatchSuccessActionHandler(dispatch
                , REQUEST_VEN_REGISTER_AVOBVENSERVICE_SUCCESS
                , "Ven register payload have been requested")
              , dispatchErrorActionHandler(dispatch
                , REQUEST_VEN_REGISTER_AVOBVENSERVICE_ERROR
                , "Ven register payload have not been requested")
            );
          }
      })
    }
}

export const sendVenRegisterService = function(venId) {
    return (dispatch, getState) => {
        dispatch({
          type    : SEND_VEN_REGISTER_AVOBVENSERVICE
          , swagger: function(api) {
            api.apis["oadr-20b-ven-controller"].sendRegisterReportUsingPOST(
              {venID: venId}
              , dispatchSuccessActionHandler(dispatch
                , SEND_VEN_REGISTER_AVOBVENSERVICE_SUCCESS
                , "Vtn register payload have been sent")
              , dispatchErrorActionHandler(dispatch
                , SEND_VEN_REGISTER_AVOBVENSERVICE_ERROR
                , "Vtn register payload have not been sent")
            );
              
          }
      })
    }
}

export const fetchVenDetail = (venId) => {
  return (dispatch, getState) => {
      dispatch({
        type    : FETCH_VEN_DETAIL
        , swagger: function(api) {
           api.apis["ven-controller"].findVenByUsernameUsingGET({venID: venId}, {responseContentType: 'application/json'}, function(data){
            var ven = JSON.parse(data.data);
            dispatch({
              type    : FETCH_VEN_DETAIL_SUCCESS
              , payload: ven
              , notification: notifySuccess("Ven details have been retrieved")
            });
          }, function(err){
            dispatch({
              type    : FETCH_VEN_DETAIL_ERROR
              , payload: err
              , notification: notifyError("Ven details have not been retrieved")
            });
          })
          
        }
    })
  }
}

export const updateVen = (ven) => {
  return (dispatch, getState) => {
      dispatch({
        type    : UPDATE_VEN
        , swagger: function(api) {
           api.apis["ven-controller"].updateVenUsingPUT({venID: ven.username, dto:ven}, {responseContentType: 'application/json'}, function(data){
            var ven = JSON.parse(data.data);
            dispatch({
              type    : UPDATE_VEN_SUCCESS
              , payload: ven
              , notification: notifySuccess("Ven details have been retrieved")
            });
          }, function(err){
            dispatch({
              type    : UPDATE_VEN_ERROR
              , payload: err
              , notification: notifyError("Ven details have not been retrieved")
            });
          })
          
        }
    })
  }
}

export const actions = {

}


const initialState = {
  ven: {}
  , venId: null
}

// ------------------------------------
// Action Handlers
// ------------------------------------
var ACTION_HANDLERS = {
	[FETCH_VEN_DETAIL_SUCCESS] : (state, action) => {
		return {
			ven : action.payload
			, venId: action.payload.username
		}; 
	}
	, [FETCH_VEN_DETAIL_ERROR] : (state, action) => {
		return {
			errors : action.payload
			, ven : {}
			, venId: null
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
