import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';

import PropTypes from 'prop-types';
import { ConnectedRouter } from 'connected-react-router';
import { Provider } from 'react-redux';

import configureStore, { history } from './store/configureStore';
const store = configureStore();


class Root extends React.Component {
  render() {
    const {store, history} = this.props;
    return (
    <Provider store={ store }>
      <ConnectedRouter history={ history }>
        <App />
      </ConnectedRouter>
    </Provider>
    );
  }
}

Root.propTypes = {
  store: PropTypes.object.isRequired,
  history: PropTypes.object.isRequired
};

ReactDOM.render(<Root store={ store } history={ history } />, document.getElementById('root'));
registerServiceWorker();
