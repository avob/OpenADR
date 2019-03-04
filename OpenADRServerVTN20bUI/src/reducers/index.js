import { combineReducers } from 'redux';
import vtnConfiguration from './vtnConfigurationReducer';
import ven from './venReducer';
import ven_detail from './venDetailReducer';
import ven_create from './venCreateReducer';
import account from './accountReducer';
import event from './eventReducer';

const rootReducer = combineReducers( {
  vtnConfiguration,
  ven,
  ven_detail,
  ven_create,
  account,
  event
} );

export default rootReducer;
