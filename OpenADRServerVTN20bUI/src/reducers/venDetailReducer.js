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
      newState = objectAssign( {}, state, {
        ven: action.payload
      } );
      return newState;

    case types.LOAD_VEN_DETAIL_ERROR:
      return state;

    case types.LOAD_VEN_MARKET_CONTEXT:
      return state;

    case types.LOAD_VEN_MARKET_CONTEXT_SUCCESS:
      newState = objectAssign( {}, state, {
        venMarketContext: action.payload
      } );
      return newState;

    case types.LOAD_VEN_MARKET_CONTEXT_ERROR:
      return state;

    case types.LOAD_VEN_GROUP:
      return state;

    case types.LOAD_VEN_GROUP_SUCCESS:
      newState = objectAssign( {}, state, {
        venGroup: action.payload
      } );
      return newState;

    case types.LOAD_VEN_GROUP_ERROR:
      return state;





     case types.ADD_VEN_MARKET_CONTEXT:
      return state;

    case types.ADD_VEN_MARKET_CONTEXT_SUCCESS:
      return state;


    case types.ADD_VEN_MARKET_CONTEXT_ERROR:
      return state;


      case types.REMOVE_VEN_MARKET_CONTEXT:
      return state;

    case types.REMOVE_VEN_MARKET_CONTEXT_SUCCESS:
      return state;


    case types.REMOVE_VEN_MARKET_CONTEXT_ERROR:
      return state;


     case types.ADD_VEN_GROUP:
      return state;

    case types.ADD_VEN_GROUP_SUCCESS:
      return state;


    case types.ADD_VEN_GROUP_ERROR:
      return state;


      case types.REMOVE_VEN_GROUP:
      return state;

    case types.REMOVE_VEN_GROUP_SUCCESS:
      return state;


    case types.REMOVE_VEN_GROUP_ERROR:
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

    default:
      return state;
  }
}
