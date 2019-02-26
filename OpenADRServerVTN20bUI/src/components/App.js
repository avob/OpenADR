import React from 'react'
import { browserHistory, Router } from 'react-router'
import { Provider } from 'react-redux'
import PropTypes from 'prop-types'
import { useBasename } from 'history'
import NotificationSystem from 'react-notification-system'
import notificationHandler from './NotificationHandler/modules/notificationHandler'

import config from "configuration";


class App extends React.Component {
  static propTypes = {
    store: PropTypes.object.isRequired,
    routes: PropTypes.object.isRequired,
  }

  componentDidMount() {
    // this._notificationSystem = this.refs.notificationSystem;
    config.notificationSystem = this.refs.notificationSystem;
    // this.props.fetchVen();
  }

  componentWillReceiveProps(nextProps){
    // displayNotification(this._notificationSystem, nextProps);
  }

  componentWillUnmount(){
    this._notificationSystem = null;
    // this.props.unmount();
  }

  shouldComponentUpdate () {
    return false
  }

  render () {
    return (
      <Provider store={this.props.store}>
        <div style={{ height: '100%' }}>
          <Router history={useBasename(() => browserHistory)({ basename: BASENAME })}  
          children={this.props.routes} />
          <NotificationSystem ref="notificationSystem"/>
        </div>
      </Provider>
    )
  }
}

export default App
