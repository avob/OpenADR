import * as types from '../constants/actionTypes'
import objectAssign from 'object-assign'
import initialState from './initialState'

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function resourceReducer (state = initialState.resource, action) {
  let newState

  switch (action.type) {
  	// RESOURCE
    case types.SEARCH_RESOURCE_ROOT:
      return state

    case types.SEARCH_RESOURCE_ROOT_SUCCESS:
      newState = objectAssign({}, state, {
        resource: action.payload
      })
      return newState

    case types.SEARCH_RESOURCE_ROOT_ERROR:
      return state

    case types.SEARCH_RESOURCE_DATA:
      return state

    case types.SEARCH_RESOURCE_DATA_SUCCESS:
      newState = objectAssign({}, state, {
        data: action.payload.data,
        dataId: action.payload.dataId
      })
      return newState

    case types.SEARCH_RESOURCE_DATA_ERROR:
      return state

    // EVENTS
    case types.SEARCH_EVENT:
      return state

    case types.SEARCH_EVENT_SUCCESS:
      newState = objectAssign({}, state, {
        event: action.payload
      })
      return newState

    case types.SEARCH_EVENT_ERROR:
      return state

      // MARKET CONTEXT
    case types.LOAD_MARKET_CONTEXT:
      return state

    case types.LOAD_MARKET_CONTEXT_SUCCESS:
      newState = objectAssign({}, state, {
        marketContext: action.payload
      })
      return newState

    case types.LOAD_MARKET_CONTEXT_ERROR:
      return state

      // GROUPS
    case types.LOAD_GROUP:
      return state

    case types.LOAD_GROUP_SUCCESS:
      newState = objectAssign({}, state, {
        group: action.payload
      })
      return newState

    case types.LOAD_GROUP_ERROR:
      return state

    // LOCATION
    case types.LOCATION_CHANGE:
      if (action.payload.location.pathname.includes('/ven/resource')) {
        return state
      } else {
        return initialState.resource
      }

    default:
      return state
  }
}
