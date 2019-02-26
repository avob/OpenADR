// ------------------------------------
// Constants
// ------------------------------------
export const FETCH_EVENT = 'FETCH_EVENT'
export const FETCH_EVENT_SUCCESS = 'FETCH_EVENT_SUCCESS'
export const FETCH_EVENT_ERROR = 'FETCH_EVENT_ERROR'

export const CREATE_EVENT = 'FETCH_EVENTREQUEST'
export const CREATE_EVENT_SUCCESS = 'CREATE_EVENT_SUCCESS'
export const CREATE_EVENT_ERROR = 'CREATE_EVENT_ERROR'

// ------------------------------------
// Actions
// ------------------------------------
export const fetchEvent = () => {
  return (dispatch, getState) => {
      dispatch({
        type    : FETCH_EVENT
        , swagger: function(api) {
          api.apis["oadr-20b-event-controller"].listEventUsingGET({responseContentType: 'application/json'}, function(data){
            var events = JSON.parse(data.data);
            dispatch({
              type    : FETCH_EVENT_SUCCESS
              , payload: events
            });
          }, function(err){
            dispatch({
              type    : FETCH_EVENT_ERROR
              , payload: err
            });
          })
          
        }
    })
  }
}

export const createEvent = (request) => {
  return (dispatch, getState) => {
      dispatch({
        type    : CREATE_EVENT
        , swagger: function(api) {
          api.apis["oadr-20b-event-controller"].createEventUsingPOST(request, {responseContentType: 'application/json'}, function(data){
            var requests = JSON.parse(data.data);
            dispatch({
              type    : CREATE_EVENT_SUCCESS
              , payload: requests
            });
          }, function(err){
            dispatch({
              type    : CREATE_EVENT_ERROR
              , payload: err
            });
          })
          
        }
    })
  }
}

export const actions = {
 fetchEvent
 , createEvent
}

// ------------------------------------
// Action Handlers
// ------------------------------------
const ACTION_HANDLERS = {
  [FETCH_EVENT]    : (state, action) => {
    return state; 
  },
  [FETCH_EVENT_SUCCESS]    : (state, action) => {
    return {
      events : action.payload
    }; 
  },
  [FETCH_EVENT_ERROR]    : (state, action) => {
    return {
      error : action.payload
    }; 
  },
  
  [CREATE_EVENT]    : (state, action) => {
    return state; 
  },
  [CREATE_EVENT_SUCCESS]    : (state, action) => {
    return state;  
  },
  [CREATE_EVENT_ERROR]    : (state, action) => {
    return state;  
  }
}

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = {
  events: []
  , error: null
}
export default function reducer (state = initialState, action) {
  const handler = ACTION_HANDLERS[action.type]
  return handler ? handler(state, action) : state
}
