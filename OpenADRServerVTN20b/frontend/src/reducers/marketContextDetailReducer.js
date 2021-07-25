import * as types from '../constants/actionTypes'
import objectAssign from 'object-assign'
import initialState from './initialState'

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function marketContextDetailReducer (state = initialState.marketContextDetail, action) {
  let newState

  switch (action.type) {


      // MARKET CONTEXT
    case types.GET_MARKET_CONTEXT:
      return state

    case types.GET_MARKET_CONTEXT_SUCCESS:
      newState = objectAssign({}, state, {
        marketContext: action.payload
      })
      return newState

    case types.GET_MARKET_CONTEXT_ERROR:
      return state

    case types.CREATE_MARKET_CONTEXT:
      return state

    case types.CREATE_MARKET_CONTEXT_SUCCESS:
      return state

    case types.CREATE_MARKET_CONTEXT_ERROR:
      return state

    case types.DELETE_MARKET_CONTEXT:
      return state

    case types.DELETE_MARKET_CONTEXT_SUCCESS:
      return state

    case types.DELETE_MARKET_CONTEXT_ERROR:
      return state

  

    // LOCATION
    case types.LOCATION_CHANGE:
      if (action.payload.location.pathname.includes('/marketcontext/detail')) {
        return state
      } else {
        return initialState.marketContextDetail
      }

    default:
      return state
  }
}