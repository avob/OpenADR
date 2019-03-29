import * as types from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function eventReducer( state = initialState.event, action ) {
  let newState;

  switch (action.type) {

    // EVENT
    case types.LOAD_EVENT:
      return state;

    case types.LOAD_EVENT_SUCCESS:
      newState = objectAssign( {}, state, {
        event: action.payload
      } );
      return newState;

    case types.LOAD_EVENT_ERROR:
      return state;

    case types.SEARCH_EVENT:
      return state;

    case types.SEARCH_EVENT_SUCCESS:
      newState = objectAssign( {}, state, {
        event: action.payload
      } );
      return newState;

    case types.SEARCH_EVENT_ERROR:
      return state;
  
    case types.DELETE_EVENT:
      return state;

    case types.DELETE_EVENT_SUCCESS:
      return state;

    case types.DELETE_EVENT_ERROR:
      return state;

  	// MARKET CONTEXT
    case types.LOAD_MARKET_CONTEXT:
      return state;

    case types.LOAD_MARKET_CONTEXT_SUCCESS:
      newState = objectAssign( {}, state, {
        marketContext: action.payload
      } );
      return newState;

    case types.LOAD_MARKET_CONTEXT_ERROR:
      return state;

    // VEN
    case types.SEARCH_VEN:
      return state;

    case types.SEARCH_VEN_SUCCESS:
      newState = objectAssign( {}, state, {
        ven: action.payload
      } );
      return newState;

    case types.SEARCH_VEN_ERROR:
      return state;

    case types.LOCATION_CHANGE:
      if(action.payload.location.pathname.includes("/event")){
        return state;
      }
      else {
        return initialState.event;
      }
      

    default:
      return state;
  }
}
