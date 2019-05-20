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

const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: theme.spacing.unit,
    marginRight: theme.spacing.unit,
  },
  dense: {
    marginTop: 19,
  },
  menu: {
    width: 200,
  },
  formControl: {
    margin: theme.spacing.unit,
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
    margin: theme.spacing.unit,
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
    marginRight: theme.spacing.unit,
  },
  message: {
    display: 'flex',
  },
  iconButton: {
    marginTop: 10
  },
});

export class AccountPage extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/account/user")
        break;
      case 1:
        history.push("/account/app")
        break;
      default:
        break;
    }
  };


  componentDidMount() {
    this.props.accountActions.loadAccountUser();
    this.props.accountActions.loadAccountApp();
    switch(this.props.match.params.panel){
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
      { value === 0 && <TabContainer>
                        <AccountUser classes={classes} user={account.user} deleteUser={this.props.accountActions.deleteUser}/>
                       </TabContainer> }
      { value === 1 && <TabContainer>
                          <AccountApp classes={classes} app={account.app} deleteApp={this.props.accountActions.deleteApp}/>
                       </TabContainer> }
    </div>

    );
  }
}

AccountPage.propTypes = {
  accountActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    account: state.account
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    accountActions: bindActionCreators( accountActions, dispatch ),
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( AccountPage ) );
