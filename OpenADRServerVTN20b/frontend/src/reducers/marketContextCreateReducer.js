import * as types from '../constants/actionTypes'
import objectAssign from 'object-assign'
import initialState from './initialState'

// IMPORTANT: Note that with Redux, state should NEVER be changed.
// State is considered immutable. Instead,
// create a copy of the state passed and set new values on the copy.
// Note that I'm using Object.assign to create a copy of current state
// and update values on the copy.
export default function marketContextDetailReducer (state = initialState.marketContextCreate, action) {
  let newState

  switch (action.type) {


      // MARKET CONTEXT

    case types.GET_MARKET_CONTEXT:
      return state

    case types.GET_MARKET_CONTEXT_SUCCESS:
      newState = objectAssign({}, state, {
        marketContext: action.payload
      })
      return newState

    case types.GET_MARKET_CONTEXT_ERROR:
      return state

    case types.CREATE_MARKET_CONTEXT:
      return state

    case types.CREATE_MARKET_CONTEXT_SUCCESS:
      return state

    case types.CREATE_MARKET_CONTEXT_ERROR:
      return state


    // DEFINITION

    case types.FIND_SIGNAL_NAME:
      return state

    case types.FIND_SIGNAL_NAME_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: action.payload,
          signalType: state.definition.signalType,
          itemDescription: state.definition.itemDescription,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName: state.definition.reportName,
          reportType: state.definition.reportType,
          readingType: state.definition.readingType,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_SIGNAL_NAME_ERROR:
      return state

    case types.FIND_SIGNAL_TYPE:
      return state

    case types.FIND_SIGNAL_TYPE_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: action.payload,
          itemDescription: state.definition.itemDescription,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName: state.definition.reportName,
          reportType: state.definition.reportType,
          readingType: state.definition.readingType,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_SIGNAL_TYPE_ERROR:
      return state


    case types.FIND_UNIT_ITEM_DESCRIPTION:
      return state

    case types.FIND_UNIT_ITEM_DESCRIPTION_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: state.definition.signalType,
          itemDescription: action.payload,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName: state.definition.reportName,
          reportType: state.definition.reportType,
          readingType: state.definition.readingType,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_UNIT_ITEM_DESCRIPTION_ERROR:
      return state


    case types.FIND_UNIT_ITEM_UNITS:
      return state

    case types.FIND_UNIT_ITEM_UNITS_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: state.definition.signalType,
          itemDescription: state.definition.itemDescription,
          itemUnits: action.payload,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName: state.definition.reportName,
          reportType: state.definition.reportType,
          readingType: state.definition.readingType,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_UNIT_ITEM_UNITS_ERROR:
      return state


    case types.FIND_UNIT_SI_SCALE_CODE:
      return state

    case types.FIND_UNIT_SI_SCALE_CODE_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: state.definition.signalType,
          itemDescription: state.definition.itemDescription,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: action.payload,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName: state.definition.reportName,
          reportType: state.definition.reportType,
          readingType: state.definition.readingType,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_UNIT_SI_SCALE_CODE_ERROR:
      return state


    case types.FIND_TARGET_ENDDEVICEASSET:
      return state

    case types.FIND_TARGET_ENDDEVICEASSET_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: state.definition.signalType,
          itemDescription: state.definition.itemDescription,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: action.payload,
          reportName: state.definition.reportName,
          reportType: state.definition.reportType,
          readingType: state.definition.readingType,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_TARGET_ENDDEVICEASSET_ERROR:
      return state


    case types.FIND_REPORT_REPORTNAME:
      return state

    case types.FIND_REPORT_REPORTNAME_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: state.definition.signalType,
          itemDescription: state.definition.itemDescription,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName: action.payload,
          reportType: state.definition.reportType,
          readingType: state.definition.readingType,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_REPORT_REPORTNAME_ERROR:
      return state


    case types.FIND_REPORT_REPORTTYPE:
      return state

    case types.FIND_REPORT_REPORTTYPE_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: state.definition.signalType,
          itemDescription: state.definition.itemDescription,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName:  state.definition.reportName,
          reportType: action.payload,
          readingType: state.definition.readingType,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_REPORT_REPORTTYPE_ERROR:
      return state

    case types.FIND_REPORT_READINGTYPE:
      return state

    case types.FIND_REPORT_READINGTYPE_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: state.definition.signalType,
          itemDescription: state.definition.itemDescription,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName:  state.definition.reportName,
          reportType: state.definition.reportType,
          readingType: action.payload,
          payloadType: state.definition.payloadType
        }
      })
      return newState

    case types.FIND_REPORT_READINGTYPE_ERROR:
      return state

    case types.FIND_REPORT_PAYLOADTYPE:
      return state

    case types.FIND_REPORT_PAYLOADTYPE_SUCCESS:
      newState = objectAssign({}, state, {
        definition: {
          signalName: state.definition.signalName,
          signalType: state.definition.signalType,
          itemDescription: state.definition.itemDescription,
          itemUnits: state.definition.itemUnits,
          itemUnitsCurrency: state.definition.itemUnitsCurrency,
          siScaleCode: state.definition.siScaleCode,
          endDeviceAsset: state.definition.endDeviceAsset,
          reportName:  state.definition.reportName,
          reportType: state.definition.reportType,
          readingType: state.definition.readingType,
          payloadType: action.payload
        }
      })
      return newState

    case types.FIND_REPORT_PAYLOADTYPE_ERROR:
      return state


     // VEN
    case types.SEARCH_VEN:
      return state

    case types.SEARCH_VEN_SUCCESS:
      newState = objectAssign({}, state, {
        ven: action.payload
      })
      return newState

      // GROUPS
    case types.LOAD_GROUP:
      return state

    case types.LOAD_GROUP_SUCCESS:
      newState = objectAssign({}, state, {
        group: action.payload
      })
      return newState

    case types.LOAD_GROUP_ERROR:
      return state
  

    // LOCATION

    case types.LOCATION_CHANGE:
      if (action.payload.location.pathname.includes('/marketcontext/create')) {
        return state
      } else {
        return initialState.marketContextCreate
      }

    default:
      return state
  }
}
