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
  // console.log(state)
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


    // ACCOUNTS
    case types.LOAD_ACCOUNT_USER:
      return state;

    case types.LOAD_ACCOUNT_USER_SUCCESS:
      newState = objectAssign( {}, state, {
        account: {
          user: action.payload
          , app:state.account.app
        }
      } );
      return newState;

    case types.LOAD_ACCOUNT_USER_ERROR:
      return state;

     case types.LOAD_ACCOUNT_APP:
      return state;

    case types.LOAD_ACCOUNT_APP_SUCCESS:
      newState = objectAssign( {}, state, {
        account: {
          app: action.payload
          , user:state.account.user
        }
      } );
      return newState;

    case types.LOAD_ACCOUNT_APP_ERROR:
      return state;



    // KNOWN
    case types.SEARCH_KNOWN_UNIT:
      return state;

    case types.SEARCH_KNOWN_UNIT_SUCCESS:
      newState = objectAssign( {}, state, {
        known: {
          unit: action.payload
          , signal:state.known.signal
        }
      } );
      return newState;

    case types.SEARCH_KNOWN_UNIT_ERROR:
      return state;


    case types.SEARCH_KNOWN_SIGNAL:
      return state;

    case types.SEARCH_KNOWN_SIGNAL_SUCCESS:
      newState = objectAssign( {}, state, {
        known: {
          unit: state.known.unit
          , signal: action.payload
        }
      } );
      return newState;

    case types.SEARCH_KNOWN_SIGNAL_ERROR:
      return state;



    // LOCATION
    case types.LOCATION_CHANGE:
      if(action.payload.location.pathname.includes("/vtn_configuration")){
        return state;
      }
      else {
        return initialState.vtnConfiguration;
      }

    default:
      return state;
  }
}
