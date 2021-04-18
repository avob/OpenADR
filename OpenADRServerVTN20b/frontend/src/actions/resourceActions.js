import * as types from '../constants/actionTypes'

import { swaggerAction, jsonResponseContentType, parseJsonData } from './apiUtils'

export const searchResourceRoot = (filters) => {
  const params = { venSearchDto: { filters: filters } }

  return swaggerAction(types.SEARCH_RESOURCE_ROOT,
    (api) => {
      return api.apis['resource-controller'].searchResourceRootUsingPOST(params, jsonResponseContentType)
    },
    parseJsonData
  )
}

export const searchResourceData = (venInternalId, dataId) => {
  const params = { venInternalId }

  return swaggerAction(types.SEARCH_RESOURCE_DATA,
    (api) => {
      return api.apis['resource-controller'].searchResourceDataUsingPOST(params, jsonResponseContentType)
    },
    (data) => {
    	return {
    		data: parseJsonData(data),
    		dataId: dataId
    	}
    }
  )
}
