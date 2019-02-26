import { injectReducer } from '../../../../store/reducers'

export default (store) => ({
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const Ven = require('./containers/VenContainer').default
      const reducer = require('./modules/ven').default

      /*  Add the reducer to the store on key 'counter'  */
      injectReducer(store, { key: 'ven', reducer })

      /*  Return getComponent   */
      cb(null, Ven)

    /* Webpack named bundle   */
    }, 'ven')
  }
})


