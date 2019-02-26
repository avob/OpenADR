import { injectReducer } from '../../../../store/reducers'

export default (store) => ({
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const EventList = require('./containers/EventListContainer').default
      const reducer = require('./modules/eventList').default

      /*  Add the reducer to the store on key 'avobvenservice'  */
      injectReducer(store, { key: 'event', reducer })

      /*  Return getComponent   */
      cb(null, EventList)

    /* Webpack named bundle   */
    }, 'event')
  }
})
