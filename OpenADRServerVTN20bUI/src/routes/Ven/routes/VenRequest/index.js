import { injectReducer } from '../../../../store/reducers'
import React from 'react'
import { IndexLink, Link } from 'react-router'

export default (store) => ({
  path : '/ven/:venId/request',
  getDisplayName: (params) => {
    return <span>
      <Link to={'/ven/'+params.venId}>{params.venId}</Link> 
      > request
    </span>;
  },
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const VenRequest = require('./containers/VenRequestContainer').default
      const reducer = require('./modules/venRequest').default

      /*  Add the reducer to the store on key 'counter'  */
      injectReducer(store, { key: 'venrequest', reducer })

      /*  Return getComponent   */
      cb(null, VenRequest)

    /* Webpack named bundle   */
    }, 'venrequest')
  }
})
