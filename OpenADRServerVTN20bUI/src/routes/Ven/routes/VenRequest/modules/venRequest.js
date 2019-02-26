import {actionHandlers} from '../../../../../components/ReactComponentLifecycle/modules/reactComponentLifecycle.js'
import {notifySuccess, notifyError} from '../../../../../components/NotificationHandler/modules/notificationHandler.js'

// ------------------------------------
// Constants
// ------------------------------------
export const FETCH_REQUESTEDREPORT = 'FETCH_REQUESTEDREPORT'
export const FETCH_REQUESTEDREPORT_SUCCESS = 'FETCH_REQUESTEDREPORT_SUCCESS'
export const FETCH_REQUESTEDREPORT_ERROR = 'FETCH_REQUESTEDREPORT_ERROR'

export const fetchRequestedReport = (venId) => {
  return (dispatch, getState) => {
      dispatch({
        type    : FETCH_REQUESTEDREPORT
        , swagger: function(api) {
          api.apis["oadr-20b-ven-controller"].viewReportRequestUsingGET({venID: venId}, {responseContentType: 'application/json'}, function(data){
            var reports = JSON.parse(data.data);
            dispatch({
              type    : FETCH_REQUESTEDREPORT_SUCCESS
              , payload: reports
              , notification: notifySuccess("Ven requests have been retrieved")
            });
          }, function(err){
            dispatch({
              type    : FETCH_REQUESTEDREPORT_ERROR
              , payload: err
              , notification: notifyError("Ven requests have not been retrieved")
            });
          })
          
        }
    })
  }
}


export const actions = {

}

const initialState = {
  requests: []
}

// ------------------------------------
// Action Handlers
// ------------------------------------
var ACTION_HANDLERS = {
 [FETCH_REQUESTEDREPORT]    : (state, action) => {
    return state; 
  },
  [FETCH_REQUESTEDREPORT_SUCCESS]    : (state, action) => {
    return {
      requests : action.payload
      
    }; 
  },
  [FETCH_REQUESTEDREPORT_ERROR]    : (state, action) => {
    return {
      error : action.payload
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
