import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as accountActions from '../actions/accountActions';


import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

import amber from '@material-ui/core/colors/amber';

import { history } from '../store/configureStore';




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

export class LoginPage extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
  };


  componentDidMount() {
    this.props.accountActions.loadLoginUser();
  }

  componentWillReceiveProps(nextProps) {
    if(nextProps.user.isConnected){
      if(this.props.history.location.state.from) {
        history.push( this.props.history.location.state.from.pathname)
      }
      else {
        history.push( '/ven' )
      }
    }
  }

  render() {
    const {classes, account} = this.props;
    const {value} = this.state;

    var hasError = this.props.user.connectionError != null;
    return (
    <div className={ classes.root }>
      {(!hasError) ? <div>
          <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Login" />
      </Tabs>
      <Divider variant="middle" />

      { value === 0 && <TabContainer>
          
                       </TabContainer> }

        </div>: null }

      {(hasError) ? <div>
        { "Can't connect to VTN backend:" + this.props.user.connectionError}
        </div>: null }
        
      

    </div>

    );
  }
}

LoginPage.propTypes = {
  accountActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    user: state.user
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
)( withStyles( styles )( LoginPage ) );
