import * as types from '../constants/actionTypes'
import objectAssign from 'object-assign'
import initialState from './initialState'

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function vtnConfigurationReducer (state = initialState.known, action) {
  let newState
  switch (action.type) {
   
    // KNOWN
    case types.SEARCH_KNOWN_UNIT:
      return state

    case types.SEARCH_KNOWN_UNIT_SUCCESS:
      newState = objectAssign({}, state, {
        unit: action.payload,
        
      })
      return newState

    case types.SEARCH_KNOWN_UNIT_ERROR:
      return state

    case types.SEARCH_KNOWN_SIGNAL:
      return state

    case types.SEARCH_KNOWN_SIGNAL_SUCCESS:
      newState = objectAssign({}, state, {
        signal: action.payload,
        
      })
      return newState

    case types.SEARCH_KNOWN_SIGNAL_ERROR:
      return state

    case types.SEARCH_KNOWN_REPORT:
      return state

    case types.SEARCH_KNOWN_REPORT_SUCCESS:
      console.log(action.payload)
      newState = objectAssign({}, state, {
        report: action.payload
        
      })
      return newState

    case types.SEARCH_KNOWN_REPORT_ERROR:
      return state

    // LOCATION
    case types.LOCATION_CHANGE:
      if (action.payload.location.pathname.includes('/marketcontext/known')) {
        return state
      } else {
        return initialState.known
      }

    default:
      return state
  }
}
