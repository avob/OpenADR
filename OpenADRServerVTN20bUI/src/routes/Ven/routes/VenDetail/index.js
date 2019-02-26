import { injectReducer } from '../../../../store/reducers'
import React from 'react'
import { IndexLink, Link } from 'react-router'

import { combineReducers } from 'redux'

export default (store) => ({
  path : '/ven/:venId',
  getDisplayName: (params) => {
    return   <Link to={'/ven/'+params.venId}>{params.venId}</Link>
  },
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const VenDetail = require('./containers/VenDetailContainer').default
      const reducer = require('./modules/venDetail').default

      /*  Add the reducer to the store on key 'counter'  */
      injectReducer(store, { key: 'vendetail', reducer})
      // injectReducer(store, { key: 'vendetail', lifecycleReducer})
      

      /*  Return getComponent   */
      cb(null, VenDetail)

    /* Webpack named bundle   */
    }, 'vendetail')
  }
})
