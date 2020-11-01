import * as types from '../constants/actionTypes';
import { history } from '../store/configureStore';

import { swaggerAction, jsonResponseContentType, multipartResponseContentType, saveData, parseJsonData } from './apiUtils';


export const loadVen = () => {
  return swaggerAction(types.LOAD_VEN, 
    (api) => {
      return api.apis[ 'ven-controller' ].listVenUsingGET(jsonResponseContentType);
    }, 
    parseJsonData
  );
}

export const searchVen = (filters, page, size) => {
  var params = {filters, page, size};
  return swaggerAction(types.SEARCH_VEN, 
    (api) => {
      return api.apis[ 'ven-controller' ].searchVenUsingPOST(params, jsonResponseContentType);
    }, 
    parseJsonData
  );
}

export const loadVenDetail = (username) => {
  return swaggerAction(types.LOAD_VEN_DETAIL, 
    (api) => {
      var params = { venID: username };
      return api.apis[ 'ven-controller' ].findVenByUsernameUsingGET(params, jsonResponseContentType);
    },
    parseJsonData
  );
}

export const createVen = (ven) => {
  return swaggerAction(types.CREATE_VEN, 
    (api) => {
      var params = { dto: ven };
      return api.apis[ 'ven-controller' ].createVenUsingPOST(params, multipartResponseContentType);
    },
    (data) => { saveData( data.data, ven.commonName + '-credentials.tar' ); history.push("/ven/") }
  );
}

export const deleteVen = (venId) => {
  return swaggerAction(types.DELETE_VEN, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'ven-controller' ].deleteVenByUsernameUsingDELETE(params, jsonResponseContentType);
    },
    () => { history.push("/ven/");  },
    (dispatch, getState) => { loadVen()(dispatch, getState) }
  );
}

export const loadVenGroup = (venId) => {
  return swaggerAction(types.LOAD_VEN_GROUP, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'ven-controller' ].listVenGroupUsingGET(params, jsonResponseContentType);
    },
    parseJsonData
  );
}

export const loadVenMarketContext = (venId) => {
  return swaggerAction(types.LOAD_VEN_MARKET_CONTEXT, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'ven-controller' ].listVenMarketContextUsingGET(params, jsonResponseContentType);
    },
    parseJsonData
  );
}

export const addVenMarketContext = (venId, marketContextId) => {
  return swaggerAction(types.ADD_VEN_MARKET_CONTEXT, 
    (api) => {
      var params = { venID: venId, marketContextId: marketContextId };
      return  api.apis[ 'ven-controller' ].addMarketContextToVenUsingPOST(params, jsonResponseContentType);
    },
    parseJsonData,
    (dispatch, getState) => { loadVenMarketContext( venId )( dispatch, getState ) }
  );
}

export const addVenGroup = (venId, groupId) => {
  return swaggerAction(types.ADD_VEN_GROUP, 
    (api) => {
      var params = { venID: venId, groupId: groupId };
      return  api.apis[ 'ven-controller' ].addGroupToVenUsingPOST(params, jsonResponseContentType);
    },
    parseJsonData,
    (dispatch, getState) => { loadVenGroup( venId )( dispatch, getState ) }
  );
}

export const removeVenMarketContext = (venId, marketContextId) => {
  return swaggerAction(types.REMOVE_VEN_MARKET_CONTEXT, 
    (api) => {
      var params = { venID: venId, marketContextId: marketContextId };
      return  api.apis[ 'ven-controller' ].deleteVenMarketContextUsingPOST(params, jsonResponseContentType);
    },
    null,
    (dispatch, getState) => { loadVenMarketContext( venId )( dispatch, getState ) }
  );
}

export const removeVenGroup = (venId, groupId) => {
  return swaggerAction(types.REMOVE_VEN_GROUP, 
    (api) => {
      var params = { venID: venId, groupId: groupId };
      return  api.apis[ 'ven-controller' ].deleteVenGroupUsingPOST(params, jsonResponseContentType);
    },
    null,
    (dispatch, getState) => { loadVenGroup( venId )( dispatch, getState ) }
  );
}

export const registerPartyRequestReregistration = (venId) => {
  return swaggerAction(types.REQUEST_REREGISTRATION_VEN, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'oadr-20b-ven-controller' ].registerPartyRequestReregistrationUsingPOST(params, jsonResponseContentType);
    },
  );
}

export const registerPartyCancelPartyRegistration = (venId) => {
  return swaggerAction(types.REQUEST_CANCEL_REGISTRATION_VEN, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'oadr-20b-ven-controller' ].registerPartyCancelPartyRegistrationUsingPOST(params, jsonResponseContentType);
    },
  );
}

export const cleanRegistration = (venId) => {
  return swaggerAction(types.REQUEST_CLEAN_REGISTRATION_VEN, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'ven-controller' ].cleanRegistrationUsingPOST(params, jsonResponseContentType);
    },
  );
}

export const loadVenAvailableReport = (venId, reportSpecifierId) => {
  return swaggerAction(types.LOAD_VEN_AVAILABLE_REPORT, 
    (api) => {
      var params = { venID: venId, reportSpecifierId:reportSpecifierId };
      return  api.apis[ 'oadr-20b-ven-controller' ].viewOtherReportCapabilityUsingGET(params, jsonResponseContentType);
    },
    parseJsonData
  );
}

export const loadVenAvailableReportDescription = (venId, reportSpecifierId) => {
  return swaggerAction(types.LOAD_VEN_AVAILABLE_REPORT_DESCRIPTION, 
    (api) => {
      var params = { venID: venId, reportSpecifierId: reportSpecifierId };
      return  api.apis[ 'oadr-20b-ven-controller' ].viewOtherReportCapabilityDescriptionUsingGET(params, jsonResponseContentType);
    },
    parseJsonData
  );
}

export const loadVenRequestedReport = (venId, reportRequestId) => {
  return swaggerAction(types.LOAD_VEN_REQUESTED_REPORT, 
    (api) => {
      var params = { venID: venId, reportRequestId: reportRequestId };
      return  api.apis[ 'oadr-20b-ven-controller' ].viewReportRequestUsingGET(params, jsonResponseContentType);
    },
    parseJsonData
  );
}

export const loadVenRequestedReportSpecifier = (venId, reportRequestId) => {
	  return swaggerAction(types.LOAD_VEN_REQUESTED_REPORT_SPECIFIER, 
	    (api) => {
	      var params = {venID: venId, criteria: { reportRequestId: [reportRequestId] }};
	      return  api.apis[ 'oadr-20b-ven-controller' ].viewReportRequestSpecifierUsingPOST(params, jsonResponseContentType);
	    },
	    parseJsonData
	  );
	}

export const requestRegisterReport  = (venId) => {
  return swaggerAction(types.REQUEST_VEN_REGISTER_REPORT, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'oadr-20b-ven-controller' ].requestRegisterReportUsingPOST(params, jsonResponseContentType);
    },
  );
}

export const sendRegisterReport  = (venId) => {
  return swaggerAction(types.SEND_VTN_REGISTER_REPORT, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'oadr-20b-ven-controller' ].sendRegisterReportUsingPOST(params, jsonResponseContentType);
    },
  );
}

export const createRequestedReport  = (venId, reportSpecifierId, start, end, rid) => {
  if(rid == null) {
    return swaggerAction(types.CREATE_REQUESTED_REPORT, 
      (api) => {
        var params = { venID: venId, reportSpecifierId: reportSpecifierId, start: start, end: end };
        return  api.apis[ 'oadr-20b-ven-controller' ].requestAllOtherReportCapabilityDescriptionRidUsingPOST(params, jsonResponseContentType);
      },
      () => { history.push("/ven/detail/"+venId+"/reports");  },
    );
  }
  else {
    return swaggerAction(types.CREATE_REQUESTED_REPORT, 
      (api) => {
        var params = { venID: venId, reportSpecifierId: reportSpecifierId, start: start, end: end, rid:rid.join(",") };
        return  api.apis[ 'oadr-20b-ven-controller' ].requestOtherReportCapabilityDescriptionRidUsingPOST(params, jsonResponseContentType);
      },
      () => { history.push("/ven/detail/"+venId+"/reports");  },
    );
  }
}


export const createRequestedReportSubscription  = (venId, reportSpecifierId, granularity, reportBackDuration, rid) => {

   if(rid == null) {
    return swaggerAction(types.CREATE_REQUESTED_REPORT, 
      (api) => {
        var params = { venID: venId
          , subscriptions: [{
              reportSpecifierId: reportSpecifierId
              , granularity: granularity
              , reportBackDuration: reportBackDuration
            }]
        };
        return  api.apis[ 'oadr-20b-ven-controller' ].subscribeOtherReportCapabilityDescriptionRidUsingPOST(params, jsonResponseContentType);
      },
      () => { history.push("/ven/detail/"+venId+"/reports");  },
    );
  }
  else {
    var ridMap = {};
    for(var i in rid) {
      ridMap[rid[i]] = true;
    }
    return swaggerAction(types.CREATE_REQUESTED_REPORT, 
      (api) => {
        var params = { venID: venId
          , reportSpecifierId: reportSpecifierId
          , granularity: granularity
          , reportBackDuration: reportBackDuration
          , rid:ridMap
        };
        return  api.apis[ 'oadr-20b-ven-controller' ].subscribeOtherReportCapabilityDescriptionRidUsingPOST(params, jsonResponseContentType);
      },
      () => { history.push("/ven/detail/"+venId+"/reports");  },
    );
  }
}

export const cancelRequestReportSubscription  = (venId, reportRequestId) => {
  return swaggerAction(types.CANCEL_REQUESTED_REPORT, 
    (api) => {
      var params = { venID: venId, reportRequestId:reportRequestId };
      return  api.apis[ 'oadr-20b-ven-controller' ].cancelSubscriptionReportRequestUsingPOST(params, jsonResponseContentType);
    },
    null,
    (dispatch, getState) => { loadVenRequestedReport( venId )( dispatch, getState ) }
  );
}


export const loadVenOpt = (venId) => {
  return swaggerAction(types.LOAD_VEN_OPT, 
    (api) => {
      var params = { venID: venId };
      return  api.apis[ 'oadr-20b-ven-controller' ].venOptUsingGET(params, jsonResponseContentType);
    },
    parseJsonData
  );
}

export const pageVenAvailableReport = (venId, page, size) => {
	  return swaggerAction(types.LOAD_VEN_AVAILABLE_REPORT, 
	    (api) => {
	      var params = { venID: venId, page:page, size: size };
	      return  api.apis[ 'oadr-20b-ven-controller' ].pageOtherReportCapabilityUsingGET(params, jsonResponseContentType);
	    },
	    parseJsonData
	  );
	}

export const pageVenRequestedReport = (venId, page, size) => {
	  return swaggerAction(types.LOAD_VEN_REQUESTED_REPORT, 
	    (api) => {
	      var params = { venID: venId, page:page, size: size };
	      return  api.apis[ 'oadr-20b-ven-controller' ].pageReportRequestUsingGET(params, jsonResponseContentType);
	    },
	    parseJsonData
	  );
	}


