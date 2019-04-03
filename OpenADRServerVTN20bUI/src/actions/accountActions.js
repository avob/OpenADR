import * as types from '../constants/actionTypes';
import { history } from '../store/configureStore';

import { swaggerAction, jsonResponseContentType, multipartResponseContentType, saveData, parseJsonData } from './apiUtils';

export const loadAccountUser = () => {
  return swaggerAction(types.LOAD_ACCOUNT_USER, 
    (api) => {
      return api.apis[ 'account-controller' ].listUserUsingGET(jsonResponseContentType);
    }, 
    parseJsonData
  );
}

export const loadAccountApp = () => {
  return swaggerAction(types.LOAD_ACCOUNT_APP, 
    (api) => {
      return api.apis[ 'account-controller' ].listAppUsingGET(jsonResponseContentType);
    }, 
    parseJsonData
  );
}


export const loadLoginUser = (username) => {
  return swaggerAction(types.LOGIN_USER, 
    (api) => {
      return api.apis[ 'account-controller' ].registeredUserUsingGET(jsonResponseContentType);
    }, 
    parseJsonData
  );
}



