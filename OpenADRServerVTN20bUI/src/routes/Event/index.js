// We only need to import the modules necessary for initial render
import EventPageLayout from './layouts/EventPageLayout/EventPageLayout'

import EventList from './routes/EventList'
import EventDetail from './routes/EventDetail'

export const createRoutes = function(store){
  var list = EventList(store);
  return {
    path        : '/event',
    component   : EventPageLayout,
    indexRoute  : list,
    getDisplayName: () => {
      return "Event";
    },
    childRoutes : [
      EventDetail(store)
    ]
  }
}

export default createRoutes
