import * as types from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function venDetailReportRequestReducer( state = initialState.ven_detail_report_request, action ) {
  let newState;

  switch (action.type) {

    // REQUESTS
    case types.LOAD_VEN_REQUESTED_REPORT:
      return state;

    case types.LOAD_VEN_REQUESTED_REPORT_SUCCESS:
      newState = objectAssign( {}, state, {
        requestedReport: action.payload
      } );
      return newState;

    case types.LOAD_VEN_REQUESTED_REPORT_ERROR:
      return state;
      
    case types.LOAD_VEN_REQUESTED_REPORT_SPECIFIER:
        return state;

      case types.LOAD_VEN_REQUESTED_REPORT_SPECIFIER_SUCCESS:
        newState = objectAssign( {}, state, {
          requestedReportSpecifier: action.payload
        } );
        return newState;

      case types.LOAD_VEN_REQUESTED_REPORT_SPECIFIER_ERROR:
        return state;

     // REPORTS
    case types.LOAD_VEN_AVAILABLE_REPORT:
      return state;

    case types.LOAD_VEN_AVAILABLE_REPORT_SUCCESS:
      newState = objectAssign( {}, state, {
        availableReport: action.payload[0]
      } );
      return newState;

    case types.LOAD_VEN_AVAILABLE_REPORT_ERROR:
      return state;
      
      

    case types.LOCATION_CHANGE:
      return initialState.ven_detail_report_request;

    default:
      return state;
  }
}
