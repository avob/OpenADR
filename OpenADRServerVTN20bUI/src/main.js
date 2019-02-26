import React from 'react'
import ReactDOM from 'react-dom'
import createStore from './store/createStore'
import './styles/main.scss'

import config from "configuration";

let reload = (isConnected) => {

  const newStore = createStore(window.__INITIAL_STATE__);
  const App = require('./components/App').default
  const routes = require('./routes/index').default(newStore)

  ReactDOM.unmountComponentAtNode(MOUNT_NODE)
  ReactDOM.render(
    <App store={newStore} routes={routes}/>,
    MOUNT_NODE
  )

}

// Store Initialization
// ------------------------------------
const store = createStore(window.__INITIAL_STATE__, reload)

// Theme Setup
// ------------------------------------
const MOUNT_NODE_THEME = document.getElementById('bootstrap_theme')
MOUNT_NODE_THEME.href = config.vtnBootstrapTheme;

const MOUNT_NODE_TITLE = document.getElementById('title');
MOUNT_NODE_TITLE.innerHTML = config.vtnPlatform + " - AvobVTNInjector";


const MOUNT_NODE_FAVICO = document.getElementById('favico');
MOUNT_NODE_FAVICO.href = config.basePath + "avob_favico.png"



// Render Setup
// ------------------------------------
const MOUNT_NODE = document.getElementById('root')

let render = () => {
  const App = require('./components/App').default
  const routes = require('./routes/index').default(store)

  ReactDOM.render(
    <App store={store} routes={routes}/>,
    MOUNT_NODE
  )
}



// Development Tools
// ------------------------------------
if (__DEV__) {
  if (module.hot) {
    const renderApp = render
    const renderError = (error) => {
      const RedBox = require('redbox-react').default

      ReactDOM.render(<RedBox error={error} />, MOUNT_NODE)
    }

    render = () => {
      try {
        renderApp()
      } catch (e) {
        console.error(e)
        renderError(e)
      }
    }

    // Setup hot module replacement
    module.hot.accept([
      './components/App',
      './routes/index',
    ], () =>
      setImmediate(() => {
        ReactDOM.unmountComponentAtNode(MOUNT_NODE)
        render()
      })
    )
  }
}

// Let's Go!
// ------------------------------------
if (!__TEST__) render()

export default reload;
