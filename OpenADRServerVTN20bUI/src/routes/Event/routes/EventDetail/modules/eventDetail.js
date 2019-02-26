// ------------------------------------
// Constants
// ------------------------------------
export const FETCH_EVENT_DETAIL = 'FETCH_EVENT_DETAIL'
export const FETCH_EVENT_DETAIL_SUCCESS = 'FETCH_EVENT_DETAIL_SUCCESS'
export const FETCH_EVENT_DETAIL_ERROR = 'FETCH_EVENT_DETAIL_ERROR'


// ------------------------------------
// Actions
// ------------------------------------
export const fetchEventDetail = (eventId) => {
  return (dispatch, getState) => {
      dispatch({
        type    : FETCH_EVENT_DETAIL
        , swagger: function(api) {
           api.apis["oadr-20b-event-controller"].findEventByEventIdUsingGET({eventId: eventId}, {responseContentType: 'application/json'}, function(data){
            var ven = JSON.parse(data.data);
            dispatch({
              type    : FETCH_EVENT_DETAIL_SUCCESS
              , payload: ven
            });
          }, function(err){
            dispatch({
              type    : FETCH_EVENT_DETAIL_ERROR
              , payload: err
            });
          })
          
        }
    })
  }
}

export const actions = {

}

// ------------------------------------
// Action Handlers
// ------------------------------------
const ACTION_HANDLERS = {
	[FETCH_EVENT_DETAIL] : (state, action) => {
		return state; 
	}
	, [FETCH_EVENT_DETAIL_SUCCESS] : (state, action) => {
		return {
			event : action.payload
		}; 
	}
	, [FETCH_EVENT_DETAIL_ERROR] : (state, action) => {
		return {
			errors : action.payload
			, event : null
		}; 
	}
 

  
}

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = {
	event: {}
}
export default function reducer (state = initialState, action) {
  const handler = ACTION_HANDLERS[action.type]
  return handler ? handler(state, action) : state
}
