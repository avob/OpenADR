import * as types from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function vtnConfigurationReducer( state = initialState.vtnConfiguration, action ) {
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

    case types.CREATE_MARKET_CONTEXT:
      return state;

    case types.CREATE_MARKET_CONTEXT_SUCCESS:
      return state;

    case types.CREATE_MARKET_CONTEXT_ERROR:
      return state;

    case types.DELETE_MARKET_CONTEXT:
      return state;

    case types.DELETE_MARKET_CONTEXT_SUCCESS:
      return state;

    case types.DELETE_MARKET_CONTEXT_ERROR:
      return state;

      // GROUPS
    case types.LOAD_GROUP:
      return state;

    case types.LOAD_GROUP_SUCCESS:
      newState = objectAssign( {}, state, {
        group: action.payload
      } );
      return newState;

    case types.LOAD_GROUP_ERROR:
      return state;

    case types.CREATE_GROUP:
      return state;

    case types.CREATE_GROUP_SUCCESS:
      return state;

    case types.CREATE_GROUP_ERROR:
      return state;

    case types.DELETE_GROUP:
      return state;

    case types.DELETE_GROUP_SUCCESS:
      return state;

    case types.DELETE_GROUP_ERROR:
      return state;

    case types.LOCATION_CHANGE:
      if(action.payload.location.pathname.includes("/vtn_configuration")){
        return state;
      }
      else {
        return initialState.event;
      }
      

    default:
      return state;
  }
}
