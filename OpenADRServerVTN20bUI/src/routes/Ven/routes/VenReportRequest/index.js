import { injectReducer } from '../../../../store/reducers'
import React from 'react'
import { IndexLink, Link } from 'react-router'

export default (store) => ({
  path : '/ven/:venId/report/:reportSpecifierId/request',
  getDisplayName: (params) => {
    return <span>
      <Link to={'/ven/'+params.venId}>{params.venId}</Link> 
      > <Link to={'/ven/'+params.venId+ "/report"} >report</Link>
      > <Link to={'/ven/'+params.venId+ "/report/"+params.reportSpecifierId + "/request"} >{params.reportSpecifierId}</Link> 
    </span>;
  },
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const VenReportRequest = require('./containers/VenReportRequestContainer').default
      const reducer = require('./modules/venReportRequest').default

      /*  Add the reducer to the store on key 'counter'  */
      injectReducer(store, { key: 'venreportrequest', reducer })

      /*  Return getComponent   */
      cb(null, VenReportRequest)

    /* Webpack named bundle   */
    }, 'venreportrequest')
  }
})
