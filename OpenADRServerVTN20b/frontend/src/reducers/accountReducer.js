import * as types from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function accountReducer( state = initialState.account, action ) {
  let newState;

  switch (action.type) {

  	case types.LOAD_ACCOUNT_USER:
      return state;

    case types.LOAD_ACCOUNT_USER_SUCCESS:
      newState = objectAssign( {}, state, {
        user: action.payload
      } );
      return newState;

    case types.LOAD_ACCOUNT_USER_ERROR:
      return state;

     case types.LOAD_ACCOUNT_APP:
      return state;

    case types.LOAD_ACCOUNT_APP_SUCCESS:
      newState = objectAssign( {}, state, {
        app: action.payload
      } );
      return newState;

    case types.LOAD_ACCOUNT_APP_ERROR:
      return state;

    case types.LOCATION_CHANGE:
      if(action.payload.location.pathname.includes("/account")){
        return state;
      }
      else {
        return initialState.account;
      }

    default:
      return state;
  }
}