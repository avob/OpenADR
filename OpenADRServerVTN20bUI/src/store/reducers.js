import { combineReducers } from 'redux'
import locationReducer from './location'
import {reducer as notifications} from 'react-notification-system-redux';

export const makeRootReducer = (asyncReducers) => {
  return combineReducers({
    location: locationReducer,notifications,
    ...asyncReducers
  })
}

export const injectReducer = (store, { key, reducer }) => {
  if (Object.hasOwnProperty.call(store.asyncReducers, key)) return

  store.asyncReducers[key] = reducer
  store.replaceReducer(makeRootReducer(store.asyncReducers))
}

export default makeRootReducer
