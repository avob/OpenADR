import { combineReducers } from 'redux';
import vtnConfiguration from './vtnConfigurationReducer';

const rootReducer = combineReducers({
  vtnConfiguration,
});

export default rootReducer;
