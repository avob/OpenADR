import {actionHandlers} from '../../../../../components/ReactComponentLifecycle/modules/reactComponentLifecycle.js'
import {notifySuccess, notifyError} from '../../../../../components/NotificationHandler/modules/notificationHandler.js'

// ------------------------------------
// Constants
// ------------------------------------
export const FETCH_AVAILABLEREPORTDESCRIPTION = 'FETCH_AVAILABLEREPORTDESCRIPTION'
export const FETCH_AVAILABLEREPORTDESCRIPTION_SUCCESS = 'FETCH_AVAILABLEREPORTDESCRIPTION_SUCCESS'
export const FETCH_AVAILABLEREPORTDESCRIPTION_ERROR = 'FETCH_AVAILABLEREPORTDESCRIPTION_ERROR'

export const SUBSCRIBE_REPORTDESCRIPTION = 'SUBSCRIBE_REPORTDESCRIPTION'
export const SUBSCRIBE_REPORTDESCRIPTION_SUCCESS = 'SUBSCRIBE_REPORTDESCRIPTION_SUCCESS'
export const SUBSCRIBE_REPORTDESCRIPTION_ERROR = 'SUBSCRIBE_REPORTDESCRIPTION_ERROR'


export const UNSUBSCRIBE_REPORTDESCRIPTION = 'UNSUBSCRIBE_REPORTDESCRIPTION'
export const UNSUBSCRIBE_REPORTDESCRIPTION_SUCCESS = 'UNSUBSCRIBE_REPORTDESCRIPTION_SUCCESS'
export const UNSUBSCRIBE_REPORTDESCRIPTION_ERROR = 'UNSUBSCRIBE_REPORTDESCRIPTION_ERROR'

// ------------------------------------
// Actions
// ------------------------------------
export const subscribe = (venId, reportSpecifierId, rid, granularity, reportBackDuration, cb) => {
  return (dispatch, getState) => {
      var params = {
        venID: venId
        , reportSpecifierId: reportSpecifierId
        , rid: rid
      }
      if(reportBackDuration != null){
        params.reportBackDuration = reportBackDuration;
      }
      if(granularity != null){
        params.granularity = granularity;
      }
      dispatch({
        type    : SUBSCRIBE_REPORTDESCRIPTION
        , swagger: function(api) {
          api.apis["oadr-20b-ven-controller"].subscribeOtherReportCapabilityDescriptionRidUsingPOST(params, {responseContentType: 'application/json'}, function(data){
            dispatch({
              type    : SUBSCRIBE_REPORTDESCRIPTION_SUCCESS
            });
            cb();
          }, function(err){
            dispatch({
              type    : SUBSCRIBE_REPORTDESCRIPTION_ERROR
              , payload: err
            });
          })
          
        }
    
    })
  }
}

export const removeFromReport = (venId, reportSpecifierId, rid, cb) => {
  return (dispatch, getState) => {
      var params = {
        venID: venId
        , reportSpecifierId: reportSpecifierId
        , rid: rid
      }
  
      dispatch({
        type    : UNSUBSCRIBE_REPORTDESCRIPTION
        , swagger: function(api) {
          api.apis["oadr-20b-ven-controller"].removeSubscribeOtherReportCapabilityDescriptionRidUsingPOST(params, {responseContentType: 'application/json'}, function(data){
            dispatch({
              type    : UNSUBSCRIBE_REPORTDESCRIPTION_SUCCESS
            });
            cb();
          }, function(err){
            dispatch({
              type    : UNSUBSCRIBE_REPORTDESCRIPTION_ERROR
              , payload: err
            });
          })
          
        }
    
    })
  }
}

export const unsubscribe = (venId, reportRequestId, cb) => {
  return (dispatch, getState) => {
      var params = {
        venID: venId
        , reportRequestId: reportRequestId
      }

      dispatch({
        type    : UNSUBSCRIBE_REPORTDESCRIPTION
        , swagger: function(api) {
          api.apis["oadr-20b-ven-controller"].cancelSubscriptionReportRequestUsingPOST(params, {responseContentType: 'application/json'}, function(data){
            dispatch({
              type    : UNSUBSCRIBE_REPORTDESCRIPTION_SUCCESS
              , payload: venId
            });
            cb();
          }, function(err){
            dispatch({
              type    : UNSUBSCRIBE_REPORTDESCRIPTION_ERROR
              , payload: err
            });
          })
          
        }
    
    })
  }
}

export const fetchAvailableReportDescription = (venId, reportSpecifierId) => {
  return (dispatch, getState) => {
      dispatch({
        type    : FETCH_AVAILABLEREPORTDESCRIPTION
        , swagger: function(api) {
          api.apis["oadr-20b-ven-controller"].viewOtherReportCapabilityDescriptionUsingGET({venID: venId, reportSpecifierId: reportSpecifierId}, {responseContentType: 'application/json'}, function(data){
            var reportDescriptions = JSON.parse(data.data);
            var reportRequestId = "0";
            var reportRequestBackDuration = null;
            var reportRequestGranularity = null;
            for(var i in reportDescriptions){
              var desc = reportDescriptions[i];
              if(desc.reportRequestGranularity != null && desc.reportRequestBackDuration != null){
                reportRequestBackDuration = desc.reportRequestBackDuration;
                reportRequestGranularity = desc.reportRequestGranularity;
                reportRequestId = desc.reportRequestId;

              }
            }
            dispatch({
              type    : FETCH_AVAILABLEREPORTDESCRIPTION_SUCCESS
              , payload: reportDescriptions
              , reportRequest: {
                reportRequestId: reportRequestId
                , reportRequestBackDuration: reportRequestBackDuration
                , reportRequestGranularity: reportRequestGranularity
              }
              , notification: notifySuccess("Report descriptions have been retrieved")
            });
          }, function(err){
            dispatch({
              type    : FETCH_AVAILABLEREPORTDESCRIPTION_ERROR
              , payload: err
              , notification: notifyError("Report descriptions have not been retrieved")
            });
          })
          
        }
    })
  }
}

export const actions = {

}

const initialState = {
  reportDescriptions: []
}

// ------------------------------------
// Action Handlers
// ------------------------------------
var ACTION_HANDLERS = {
 [FETCH_AVAILABLEREPORTDESCRIPTION]    : (state, action) => {
    return state; 
  },
  [FETCH_AVAILABLEREPORTDESCRIPTION_SUCCESS]    : (state, action) => {
    return {
      reportDescriptions : action.payload
      , reportRequest: action.reportRequest
      
    }; 
  },
  [FETCH_AVAILABLEREPORTDESCRIPTION_ERROR]    : (state, action) => {
    return {
      error : action.payload
      , reportDescriptions : []
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
