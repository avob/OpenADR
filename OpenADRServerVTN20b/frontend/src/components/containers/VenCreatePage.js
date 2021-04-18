import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';

import * as venActions from '../../actions/venActions';

import { withStyles } from '@material-ui/core/styles';
import Divider from '@material-ui/core/Divider';

import VenCreate from '../VenCreate/VenCreate'

import amber from '@material-ui/core/colors/amber';

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
  stepper: {
    backgroundColor: '#fafafa'
  },
  warning: {
    backgroundColor: amber[ 700 ],
  },
  icon: {
    fontSize: 20,
  },
  iconVariant: {
    opacity: 0.9,
    marginRight: theme.spacing(1),
  },
  message: {
    display: 'flex',
  },
  iconButton: {
    marginTop: 10
  },
});

export class VenCreatePage extends React.Component {
  state = {
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
  };


  componentDidMount() {
    this.props.vtnConfigurationActions.loadVtnConfiguration();
  }

  render() {
    const {classes, ven_create} = this.props;
    return (
    <div className={ classes.root }>
      <Divider variant="middle" />
       <VenCreate classes={ classes }
                                    vtnConfiguration={ ven_create.parameters }
                                    createVen={ this.props.venActions.createVen } />
    </div>

    );
  }
}

VenCreatePage.propTypes = {
  vtnConfigurationActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    ven_create: state.ven_create
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch ),
    venActions: bindActionCreators( venActions, dispatch ),
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( VenCreatePage ) );
