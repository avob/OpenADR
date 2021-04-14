import * as types from '../constants/actionTypes';

import { swaggerAction, jsonResponseContentType, multipartResponseContentType, saveData, parseJsonData } from './apiUtils';


export const searchKnownUnit = (knownUnit) => {
  var params = {knownUnit};

  return swaggerAction(types.SEARCH_KNOWN_UNIT, 
    (api) => {
      console.log(api.apis[ 'known-unit-controller' ])
      return api.apis[ 'known-unit-controller' ].searchKnownUnitUsingPOST(params, jsonResponseContentType);
    }, 
    parseJsonData
  );
}

export const searchKnownSignal = (knownSignal) => {
  var params = {knownSignal};

  return swaggerAction(types.SEARCH_KNOWN_SIGNAL, 
    (api) => {
      return api.apis[ 'known-signal-controller' ].searchKnownSignalUsingPOST(params, jsonResponseContentType);
    }, 
    parseJsonData
  );
}