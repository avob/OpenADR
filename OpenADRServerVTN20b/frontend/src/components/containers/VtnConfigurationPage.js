import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as actions from '../../actions/vtnConfigurationActions';
import * as accountActions from '../../actions/accountActions';

import VtnConfigurationParameter from '../VtnConfiguration/VtnConfigurationParameter';
import VtnConfigurationGroup from '../VtnConfiguration/VtnConfigurationGroup';
import VtnConfigurationAccount from '../VtnConfiguration/VtnConfigurationAccount';

import { withStyles } from '@material-ui/core/styles';

import { history } from '../../store/configureStore';

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

export class VtnConfigurationPage extends React.Component {
  state = {
    value: 0,
  };

  componentDidMount() {
    this.props.actions.loadVtnConfiguration();
    this.props.actions.loadGroup();
    this.props.accountActions.loadAccountUser();
    this.props.accountActions.loadAccountApp();
    switch(this.props.match.params.panel){
      case "parameter":
        this.setState({value:0});
        break;
      case "group":
        this.setState({value:2});
        break;
      case "account":
        this.setState({value:3});
        break;
      case "known":
        this.setState({value:4});
        break;
      default:
        break;
    }
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if(history.location.pathname === "/vtn_configuration" && this.state.value !== 0) {
      this.setState({value:0});
    } else if(history.location.pathname === "/vtn_configuration/parameter" && this.state.value !== 0) {
      this.setState({value:0});
    } else if(history.location.pathname === "/vtn_configuration/group" && this.state.value !== 2) {
      this.setState({value:2});
    }  else if(history.location.pathname.startsWith('/vtn_configuration/account') && this.state.value !== 3) {
      this.setState({value:3});
    }
  }

  render() {
    const {classes, vtnConfiguration} = this.props;
    const {value} = this.state;
    const accountPanel = this.props.match.params.accountPanel || "user";
    return (
    <div className={ classes.root }>

      { value === 0 && <VtnConfigurationParameter classes={ classes } vtnConfiguration={ vtnConfiguration.parameters } /> }

      { value === 2 && <VtnConfigurationGroup classes={ classes }
                                                group={ vtnConfiguration.group }
                                                createGroup={ this.props.actions.createGroup }
                                                updateGroup={ this.props.actions.updateGroup }
                                                deleteGroup={ this.props.actions.deleteGroup } /> }
      

      { value === 3 && <VtnConfigurationAccount classes={ classes } account={ vtnConfiguration.account } accountPanel={accountPanel}
                           deleteUser={this.props.accountActions.deleteUser} deleteApp={this.props.accountActions.deleteApp}/> }


                       
    </div>

    );
  }
}

VtnConfigurationPage.propTypes = {
  actions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    vtnConfiguration: state.vtnConfiguration
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    actions: bindActionCreators( actions, dispatch )
    , accountActions: bindActionCreators( accountActions, dispatch )
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( VtnConfigurationPage ) );
