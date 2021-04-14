import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as accountActions from '../../actions/accountActions';

import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

import amber from '@material-ui/core/colors/amber';


import AccountUser from '../Account/AccountUser'
import AccountApp from '../Account/AccountApp'

import { history } from '../../store/configureStore';




function TabContainer( props ) {
  return (
  <Typography component="div" style={ { padding: 8 * 3 } }>
    { props.children }
  </Typography>
  );
}

export class VtnConfigurationAccount extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/vtn_configuration/account/user")
        break;
      case 1:
        history.push("/vtn_configuration/account/app")
        break;
      default:
        break;
    }
  };


  componentDidMount() {
    switch(this.props.accountPanel){
      case "user":
        this.setState({value:0});
        break;
      case "app":
        this.setState({value:1});
        break;
      default:
        break;
    }
  }


  render() {
    const {classes, account} = this.props;
    const {value} = this.state;
    return (
    <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Users" />
        <Tab label="Apps" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <AccountUser classes={classes} user={account.user} deleteUser={this.props.deleteUser}/>
                        }
      { value === 1 && <AccountApp classes={classes} app={account.app} deleteApp={this.props.deleteApp}/>
                       }
    </div>

    );
  }
}

export default VtnConfigurationAccount;
