import { injectReducer } from '../../../../store/reducers'
import React from 'react'
import { IndexLink, Link } from 'react-router'

export default (store) => ({
  path : '/ven/:venId/report',
  getDisplayName: (params) => {
    return <span>
      <Link to={'/ven/'+params.venId}>{params.venId}</Link> > <Link to={'/ven/'+params.venId+ "/report"} >report</Link>
    </span>;
  },
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const VenReport = require('./containers/VenReportContainer').default
      const reducer = require('./modules/venReport').default

      /*  Add the reducer to the store on key 'counter'  */
      injectReducer(store, { key: 'venreport', reducer })

      /*  Return getComponent   */
      cb(null, VenReport)

    /* Webpack named bundle   */
    }, 'venreport')
  }
})
