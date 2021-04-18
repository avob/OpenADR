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


import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Paper from '@material-ui/core/Paper';

import { history, config } from '../store/configureStore';



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
  main: {
    width: 'auto',
    display: 'block', // Fix IE 11 issue.
    marginLeft: theme.spacing(3),
    marginRight: theme.spacing(1),
    [theme.breakpoints.up(400 + theme.spacing(6))]: {
      width: 400,
      marginLeft: 'auto',
      marginRight: 'auto',
    },
  },
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    padding: `${theme.spacing(2)}px ${theme.spacing(3)}px ${theme.spacing(3)}px`,
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    marginTop: theme.spacing(3),
  },
});

export class LoginPage extends React.Component {
  state = {
    value: 0,
    username:"",
    password:""
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
      if(this.props.history.location && this.props.history.location.state 
          && this.props.history.location.state.from) {
        history.push( this.props.history.location.state.from.pathname)
      }
      else {
        history.push( '/' )
      }
    }
  }

  handleUsernameChange = (e) => {
    this.setState({username: e.target.value});
  }

  handlePasswordChange = (e) => {
    this.setState({password: e.target.value});
  }

  handleSubmit = () => {
    config.username = this.state.username;
    config.password = this.state.password;
    this.setState({username: "", password: ""});
    this.props.accountActions.loadLoginUser();
  }

  render() {
    const {classes} = this.props;
    const {value} = this.state;

    var hasError = this.props.user.connectionError != null;
    var authenticationError = hasError 
        && ( ""+this.props.user.connectionError === "Error: Forbidden"
          || ""+this.props.user.connectionError === "Error: Unauthorized")

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

      {(hasError && authenticationError) ? <div>
        {/* "SSL Certificate authentication failed, must provide login / password"*/ }
          <main className={classes.main}>
      <CssBaseline />
           <Paper className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign in
        </Typography>
        <form className={classes.form}>
          <FormControl margin="normal" required fullWidth>
            <InputLabel htmlFor="username">Username</InputLabel>
            <Input autoComplete="username" autoFocus 
              onChange={this.handleUsernameChange}/>
          </FormControl>
          <FormControl margin="normal" required fullWidth>
            <InputLabel htmlFor="password">Password</InputLabel>
            <Input id="password" autoComplete="current-password"
              onChange={this.handlePasswordChange}/>
          </FormControl>
          <FormControlLabel
            control={<Checkbox value="remember" color="primary" />}
            label="Remember me"
          />
          <Button
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
            onClick={this.handleSubmit}
          >
            Sign in
          </Button>
        </form>
      </Paper>
        </main>
        </div>: null }

      {(hasError && !authenticationError) ? <div>
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
