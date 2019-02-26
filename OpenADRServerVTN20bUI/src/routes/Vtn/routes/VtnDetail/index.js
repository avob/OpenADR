import { injectReducer } from '../../../../store/reducers'

export default (store) => ({
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const VtnDetailContainer = require('./containers/VtnDetailContainer').default
      const reducer = require('./modules/vtnDetail').default

      /*  Add the reducer to the store on key 'counter'  */
      injectReducer(store, { key: 'vtndetail', reducer })

      /*  Return getComponent   */
      cb(null, VtnDetailContainer)

    /* Webpack named bundle   */
    }, 'vtndetail')
  }
})


