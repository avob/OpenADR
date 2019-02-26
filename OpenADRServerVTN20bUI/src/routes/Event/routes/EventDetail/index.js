import { injectReducer } from '../../../../store/reducers'
import React from 'react'
import { IndexLink, Link } from 'react-router'

export default (store) => ({
  path : '/event/:eventId',
  getDisplayName: (params) => {
    return   <Link to={'/event/'+params.eventId}>{params.eventId}</Link>
  },
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const EventDetail = require('./containers/EventDetailContainer').default
      const reducer = require('./modules/eventDetail').default

      /*  Add the reducer to the store on key 'avobvenservice'  */
      injectReducer(store, { key: 'eventdetail', reducer })

      /*  Return getComponent   */
      cb(null, EventDetail)

    /* Webpack named bundle   */
    }, 'eventdetail')
  }
})
