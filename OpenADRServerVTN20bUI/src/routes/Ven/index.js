// We only need to import the modules necessary for initial render
import VenPageLayout from './layouts/VenPageLayout/VenPageLayout'
import VenListRoute from './routes/VenList'
import VenDetailRoute from './routes/VenDetail'
import VenReportRoute from './routes/VenReport'
import VenReportDescriptionRoute from './routes/VenReportDescription'
import VenReportRequest from './routes/VenReportRequest'
import VenRequest from './routes/VenRequest'


/*  Note: Instead of using JSX, we recommend using react-router
    PlainRoute objects to build route definitions.   */

export const createRoutes = function(store){
  var list = VenListRoute(store)
  return {
    path        : '/ven',
    component   : VenPageLayout,
    indexRoute  : list,
    getDisplayName: () => {
      return "Ven";
    },
    childRoutes : [
      VenDetailRoute(store)
      , VenReportRoute(store)
      , VenReportDescriptionRoute(store)
      , VenReportRequest(store)
      , VenRequest(store)
    ]
  }
}

/*  Note: childRoutes can be chunked or otherwise loaded programmatically
    using getChildRoutes with the following signature:

    getChildRoutes (location, cb) {
      require.ensure([], (require) => {
        cb(null, [
          // Remove imports!
          require('./Counter').default(store)
        ])
      })
    }

    However, this is not necessary for code-splitting! It simply provides
    an API for async route definitions. Your code splitting should occur
    inside the route `getComponent` function, since it is only invoked
    when the route exists and matches.
*/

export default createRoutes
