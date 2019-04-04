import { combineReducers } from 'redux';
import vtnConfiguration from './vtnConfigurationReducer';
import ven from './venReducer';
import ven_detail from './venDetailReducer';
import ven_detail_create_report from './venDetailCreateReportReducer';
import ven_detail_report from './venDetailReportReducer';
import ven_detail_report_request from './venDetailReportRequestReducer'
import ven_create from './venCreateReducer';
import account from './accountReducer';
import account_create from './accountCreateReducer'
import event from './eventReducer'
import event_detail from './eventDetailReducer'
import event_create from './eventCreateReducer';
import user from './loginReducer';





const rootReducer = combineReducers( {
  vtnConfiguration,
  ven,
  ven_detail,
  ven_detail_report,
  ven_detail_create_report,
  ven_detail_report_request,
  ven_create,
  account,
  account_create,
  event,
  event_detail,
  event_create,
  user
} );

export default rootReducer;
