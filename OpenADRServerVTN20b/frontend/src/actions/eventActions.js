import * as types from '../constants/actionTypes'
import { history } from '../store/configureStore'

import { swaggerAction, jsonResponseContentType, parseJsonData } from './apiUtils'

export const loadEvent = (start, end) => {
  return swaggerAction(types.LOAD_EVENT,
    (api) => {
      return api.apis['demand-response-controller'].listUsingGET(jsonResponseContentType)
    },
    parseJsonData
  )
}

export const searchEvent = (filters, sorts, start, end, page, size) => {
  const params = { filters, start, end, page, size }
  params.demandResponseEventSearch = { filters, sorts }
  return swaggerAction(types.SEARCH_EVENT,
    (api) => {
      return api.apis['demand-response-controller'].searchUsingPOST(params, jsonResponseContentType)
    },
    parseJsonData
  )
}

export const createEvent = (dto) => {
  const params = { event: dto }
  return swaggerAction(types.CREATE_EVENT,
    (api) => {
      return api.apis['demand-response-controller'].createUsingPOST(params, jsonResponseContentType)
    },
    (data) => { history.push('/event/') }

  )
}

export const updateEvent = (id, dto) => {
  const params = { id: id, event: dto }
  return swaggerAction(types.UPDATE_EVENT,
    (api) => {
      return api.apis['demand-response-controller'].updateUsingPUT(params, jsonResponseContentType)
    },
    parseJsonData
  )
}

export const deleteEvent = (id) => {
  return swaggerAction(types.DELETE_EVENT,
    (api) => {
      const params = { id: id }
      return api.apis['demand-response-controller'].deleteUsingDELETE(params, jsonResponseContentType)
    },
    () => { history.push('/event/') },
    (dispatch, getState) => { loadEvent()(dispatch, getState) }
  )
}

export const loadEventDetail = (id) => {
  const params = { id: id }
  return swaggerAction(types.LOAD_EVENT_DETAIL,
    (api) => {
      return api.apis['demand-response-controller'].readUsingGET(params, jsonResponseContentType)
    },
    parseJsonData
  )
}

export const publishEvent = (id) => {
  const params = { id: id }
  return swaggerAction(types.PUBLISH_EVENT,
    (api) => {
      return api.apis['demand-response-controller'].publishUsingPOST(params, jsonResponseContentType)
    },
    null,
    (dispatch, getState) => { loadEventDetail(id)(dispatch, getState) }
  )
}

export const activeEvent = (id) => {
  const params = { id: id }
  return swaggerAction(types.ACTIVE_EVENT,
    (api) => {
      return api.apis['demand-response-controller'].activeUsingPOST(params, jsonResponseContentType)
    },
    null,
    (dispatch, getState) => { loadEventDetail(id)(dispatch, getState) }
  )
}

export const cancelEvent = (id) => {
  const params = { id: id }
  return swaggerAction(types.CANCEL_EVENT,
    (api) => {
      return api.apis['demand-response-controller'].cancelUsingPOST(params, jsonResponseContentType)
    },
    null,
    (dispatch, getState) => { loadEventDetail(id)(dispatch, getState) }
  )
}

export const loadEventVenResponse = (id) => {
  const params = { id: id }
  return swaggerAction(types.LOAD_EVENT_VEN_RESPONSE,
    (api) => {
      return api.apis['demand-response-controller'].readVenDemandResponseEventUsingGET(params, jsonResponseContentType)
    },
    parseJsonData
  )
}
