// ------------------------------------
// Constants
// ------------------------------------
export const FETCH_VTN_DETAIL = 'FETCH_VTN_DETAIL'
export const FETCH_VTN_DETAIL_SUCCESS = 'FETCH_VTN_DETAIL_SUCCESS'
export const FETCH_VTN_DETAIL_ERROR = 'FETCH_VTN_DETAIL_ERROR'


// ------------------------------------
// Actions
// ------------------------------------

export const fetchVtnDetail = (venId) => {
  return (dispatch, getState) => {
      dispatch({
        type    : FETCH_VTN_DETAIL
        , swagger: function(api) {
           api.apis["oadr-20b-vtn-controller"].viewConfUsingGET( {responseContentType: 'application/json'}, function(data){
            var vtn = JSON.parse(data.data);
            dispatch({
              type    : FETCH_VTN_DETAIL_SUCCESS
              , payload: vtn
            });
          }, function(err){
            dispatch({
              type    : FETCH_VTN_DETAIL_ERROR
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
	[FETCH_VTN_DETAIL] : (state, action) => {
		return state; 
	}
	, [FETCH_VTN_DETAIL_SUCCESS] : (state, action) => {
		return {
			vtn : action.payload
		}; 
	}
	, [FETCH_VTN_DETAIL_ERROR] : (state, action) => {
		return {
			errors : action.payload
			, vtn : null
		}; 
	}
  
}

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = {
	vtn: null
}
export default function reducer (state = initialState, action) {
  const handler = ACTION_HANDLERS[action.type]
  return handler ? handler(state, action) : state
}
