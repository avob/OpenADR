import * as types from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';



// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function venCreateReducer( state = initialState.ven_create, action ) {
  let newState;

  switch (action.type) {

  	// PARAMETERS
    case types.LOAD_VTN_CONFIGURATION:
      return state;

    
    case types.LOAD_VTN_CONFIGURATION_SUCCESS:
      newState = objectAssign( {}, state, {
        parameters: action.payload
      } );
      return newState;

    case types.LOAD_VTN_CONFIGURATION_ERROR:
      return state;

     // VEN
     case types.CREATE_VEN:
      return state;

    case types.CREATE_VEN_SUCCESS:

      return state;

    case types.CREATE_VEN_ERROR:
      return state;

    case types.DELETE_VEN:
      return state;

    case types.DELETE_VEN_SUCCESS:
      
      return state;

    case types.DELETE_VEN_ERROR:
      return state;

    case types.LOCATION_CHANGE:
      return initialState.ven_create;
      
    default:
      return state;
  }
}
