import * as types from '../constants/actionTypes';
import { history } from '../store/configureStore';

import { swaggerAction, jsonResponseContentType, multipartResponseContentType, saveData, parseJsonData } from './apiUtils';

export const loadLoginUser = (username) => {
  return swaggerAction(types.LOGIN_USER, 
    (api) => {
      return api.apis[ 'account-controller' ].registeredUserUsingGET(jsonResponseContentType);
    }, 
    parseJsonData
  );
}

export const loadAccountUser = () => {
  return swaggerAction(types.LOAD_ACCOUNT_USER, 
    (api) => {
      return api.apis[ 'account-controller' ].listUserUsingGET(jsonResponseContentType);
    }, 
    parseJsonData
  );
}

export const createUser = (user) => {
  return swaggerAction(types.CREATE_USER, 
    (api) => {
      var params = { dto: user };
      return api.apis[ 'account-controller' ].createUserUsingPOST(params, multipartResponseContentType);
    },
    (data) => { saveData( data.data, user.commonName + '-credentials.tar' ); history.push("/account/user") }
  );
}

export const deleteUser = (username) => {
  return swaggerAction(types.DELETE_USER, 
    (api) => {
      var params = { username: username };
      return  api.apis[ 'account-controller' ].deleteUserUsingDELETE(params, jsonResponseContentType);
    },
    () => { history.push("/account/user");  },
    (dispatch, getState) => { loadAccountUser()(dispatch, getState) }
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

export const createApp = (app) => {
  return swaggerAction(types.CREATE_APP, 
    (api) => {
      var params = { dto: app };
      return api.apis[ 'account-controller' ].createAppUsingPOST(params, multipartResponseContentType);
    },
    (data) => { saveData( data.data, app.commonName + '-credentials.tar' ); history.push("/account/app") }
  );
}

export const deleteApp = (username) => {
  return swaggerAction(types.DELETE_APP, 
    (api) => {
      var params = { username: username };
      return  api.apis[ 'account-controller' ].deleteAppUsingDELETE(params, jsonResponseContentType);
    },
    () => { history.push("/account/app");  },
    (dispatch, getState) => { loadAccountApp()(dispatch, getState) }
  );
}



