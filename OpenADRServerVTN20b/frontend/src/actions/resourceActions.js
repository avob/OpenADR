import * as types from '../constants/actionTypes';

import { swaggerAction, jsonResponseContentType, multipartResponseContentType, saveData, parseJsonData } from './apiUtils';


export const searchResource = (filters) => {
  var params = {venSearchDto: {filters: filters}};

  return swaggerAction(types.SEARCH_RESOURCE, 
    (api) => {
      return api.apis[ 'resource-controller' ].searchResourceUsingPOST(params, jsonResponseContentType);
    }, 
    parseJsonData
  );
}