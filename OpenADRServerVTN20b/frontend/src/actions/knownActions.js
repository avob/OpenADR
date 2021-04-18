import * as types from '../constants/actionTypes'

import { swaggerAction, jsonResponseContentType, parseJsonData } from './apiUtils'

export const searchKnownUnit = (knownUnit) => {
  const params = { knownUnit }

  return swaggerAction(types.SEARCH_KNOWN_UNIT,
    (api) => {
      return api.apis['known-controller'].searchKnownUnitUsingPOST(params, jsonResponseContentType)
    },
    parseJsonData
  )
}

export const searchKnownSignal = (knownSignal) => {
  const params = { knownSignal }

  return swaggerAction(types.SEARCH_KNOWN_SIGNAL,
    (api) => {
      return api.apis['known-controller'].searchKnownSignalUsingPOST(params, jsonResponseContentType)
    },
    parseJsonData
  )
}

export const searchKnownReport = (knownReport) => {
  const params = { knownReport }

  return swaggerAction(types.SEARCH_KNOWN_REPORT,
    (api) => {
      return api.apis['known-controller'].searchKnownReportUsingPOST(params, jsonResponseContentType)
    },
    parseJsonData
  )
}
