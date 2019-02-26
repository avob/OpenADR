import {actionHandlers} from '../../../../../components/ReactComponentLifecycle/modules/reactComponentLifecycle.js'
import {notifySuccess, notifyError} from '../../../../../components/NotificationHandler/modules/notificationHandler.js'

// ------------------------------------
// Constants
// ------------------------------------
export const FETCH_AVAILABLEREPORT = 'FETCH_AVAILABLEREPORT'
export const FETCH_AVAILABLEREPORT_SUCCESS = 'FETCH_AVAILABLEREPORT_SUCCESS'
export const FETCH_AVAILABLEREPORT_ERROR = 'FETCH_AVAILABLEREPORT_ERROR'




export const fetchAvailableReport = (venId) => {
  return (dispatch, getState) => {
      dispatch({
        type    : FETCH_AVAILABLEREPORT
        , swagger: function(api) {
          api.apis["oadr-20b-ven-controller"].viewOtherReportCapabilityUsingGET({venID: venId}, {responseContentType: 'application/json'}, function(data){
            var reports = JSON.parse(data.data);
            dispatch({
              type    : FETCH_AVAILABLEREPORT_SUCCESS
              , payload: reports
              , notification: notifySuccess("Reports have been retrieved")
            });
          }, function(err){
            dispatch({
              type    : FETCH_AVAILABLEREPORT_ERROR
              , payload: err
              , notification: notifyError("Reports have not been retrieved")
            });
          })
          
        }
    })
  }
}

export const actions = {

}

const initialState = {
  reports: []
}

// ------------------------------------
// Action Handlers
// ------------------------------------
var ACTION_HANDLERS = {
 [FETCH_AVAILABLEREPORT]    : (state, action) => {
    return state; 
  },
  [FETCH_AVAILABLEREPORT_SUCCESS]    : (state, action) => {
    return {
      reports : action.payload
    }; 
  },
  [FETCH_AVAILABLEREPORT_ERROR]    : (state, action) => {
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
