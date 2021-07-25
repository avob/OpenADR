import * as types from '../constants/actionTypes'

import { swaggerAction, jsonResponseContentType, parseJsonData } from './apiUtils'

export const findSignalName = () => {

  return swaggerAction(types.FIND_SIGNAL_NAME,
    (api) => {
      return api.apis['definition-controller'].findSignalNameUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findSignalType = () => {

  return swaggerAction(types.FIND_SIGNAL_TYPE,
    (api) => {
      return api.apis['definition-controller'].findSignalTypeUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findUnitItemDescription = () => {

  return swaggerAction(types.FIND_UNIT_ITEM_DESCRIPTION,
    (api) => {
      return api.apis['definition-controller'].findUnitItemDescriptionUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findUnitItemUnits = (description = null) => {
	const params = { description }
  return swaggerAction(types.FIND_UNIT_ITEM_UNITS,
    (api) => {
      return api.apis['definition-controller'].findUnitItemUnitsUsingGET(params, jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findUnitSiScaleCode = () => {

  return swaggerAction(types.FIND_UNIT_SI_SCALE_CODE,
    (api) => {
      return api.apis['definition-controller'].findUnitSiScaleCodeUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findEndDeviceAsset = () => {

  return swaggerAction(types.FIND_TARGET_ENDDEVICEASSET,
    (api) => {
      return api.apis['definition-controller'].findEndDeviceAssetUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findReportName = () => {

  return swaggerAction(types.FIND_REPORT_REPORTNAME,
    (api) => {
      return api.apis['definition-controller'].findReportNameUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findReportType = () => {

  return swaggerAction(types.FIND_REPORT_REPORTTYPE,
    (api) => {
      return api.apis['definition-controller'].findReportTypeUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findReadingType = () => {

  return swaggerAction(types.FIND_REPORT_READINGTYPE,
    (api) => {
      return api.apis['definition-controller'].findReadingTypeUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const findPayloadType = () => {

  return swaggerAction(types.FIND_REPORT_PAYLOADTYPE,
    (api) => {
      return api.apis['definition-controller'].findPayloadTypeUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}




