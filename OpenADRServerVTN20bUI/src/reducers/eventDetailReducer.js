import * as types from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function eventDetailReducer( state = initialState.event_detail, action ) {
  let newState;

  switch (action.type) {

  	// EVENT
    case types.LOAD_EVENT_DETAIL:
      return state;

    case types.LOAD_EVENT_DETAIL_SUCCESS:
      newState = objectAssign( {}, state, {
        event: action.payload
      } );
      return newState;

    case types.LOAD_EVENT_DETAIL_ERROR:
      return state;


    case types.EVENT_ACTIVE:
      return state;

    case types.EVENT_ACTIVE_SUCCESS:
      return state;

    case types.EVENT_ACTIVE_ERROR:
      return state;

    case types.EVENT_CANCEL:
      return state;

    case types.EVENT_CANCEL_SUCCESS:
      return state;

    case types.EVENT_CANCEL_ERROR:
      return state;

    default:
      return state;
  }
}
