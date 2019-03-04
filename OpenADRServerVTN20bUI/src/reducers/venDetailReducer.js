import * as types from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function venDetailReducer( state = initialState.ven_detail, action ) {
  let newState;

  switch (action.type) {

    // VENS
    case types.LOAD_VEN:
      return state;

    case types.LOAD_VEN_DETAIL_SUCCESS:
      console.log( action.payload )
      newState = objectAssign( {}, state, {
        ven_detail: action.payload
      } );
      return newState;

    case types.LOAD_VEN_DETAIL_ERROR:
      return state;


    default:
      return state;
  }
}
