import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as actions from '../../actions/vtnConfigurationActions';

import MarketContextList from '../MarketContext/MarketContextList';

import { withStyles } from '@material-ui/core/styles';

const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
  },
  dense: {
    marginTop: 19,
  },
  menu: {
    width: 200,
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 500,
  },
  card: {
    maxWidth: 350,
    minWidth: 350,
  },
  media: {
    height: 40,
    paddingTop: 10,
    paddingRight: 10
  },
  button: {
    margin: theme.spacing(1),
  },
  iconButton: {
    marginTop: 10
  },
});

export class MarketContextPage extends React.Component {
  state = {
  };

  componentDidMount() {
    this.props.actions.loadMarketContext();
  }


  render() {
    const {classes, marketContext} = this.props;
    return (
    <div className={ classes.root }>

      <MarketContextList classes={ classes }
                          marketContext={ marketContext.marketContext }
                          createMarketContext={ this.props.actions.createMarketContext }
                          updateMarketContext={ this.props.actions.updateMarketContext }
                          deleteMarketContext={ this.props.actions.deleteMarketContext } />  
                       
    </div>

    );
  }
}

MarketContextPage.propTypes = {
  actions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    marketContext: state.marketContext
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    actions: bindActionCreators( actions, dispatch )
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( MarketContextPage ) );
