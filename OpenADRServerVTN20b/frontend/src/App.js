/* eslint-disable import/no-named-as-default */
import {  Route, Switch, Redirect } from 'react-router-dom';

import LoginPage from './components/LoginPage';
import AboutPage from './components/AboutPage';
import HomePage from './components/HomePage';
import NotFoundPage from './components/NotFoundPage';
import VtnConfigurationPage from './components/containers/VtnConfigurationPage';
import VenPage from './components/containers/VenPage'
import AccountPage from './components/containers/AccountPage'
import AccountUserCreatePage from './components/containers/AccountUserCreatePage'
import AccountAppCreatePage from './components/containers/AccountAppCreatePage'
import EventPage from './components/containers/EventPage'
import EventDetailPage from './components/containers/EventDetailPage'
import EventCreatePage from './components/containers/EventCreatePage'
import VenDetailPage from './components/containers/VenDetailPage'
import VenDetailCreateReportPage from './components/containers/VenDetailCreateReportPage'
import VenDetailReportPage from './components/containers/VenDetailReportPage'
import VenDetailReportRequestPage from './components/containers/VenDetailReportRequestPage'


import VenCreatePage from './components/containers/VenCreatePage'

import PropTypes from 'prop-types';
import React from 'react';



import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';


import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';

import MenuIcon from '@material-ui/icons/Menu';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';

import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';


import NavigationMain from './components/Navigation';



import { history, config } from './store/configureStore';



const drawerWidth = 240;

const styles = theme => ({
  root: {
    display: 'flex',
  },
  toolbar: {
    paddingRight: 24, // keep right padding when drawer closed
  },
  toolbarIcon: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    ...theme.mixins.toolbar,
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create( [ 'width', 'margin' ], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    } ),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create( [ 'width', 'margin' ], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    } ),
  },
  menuButton: {
    marginLeft: 12,
    marginRight: 36,
  },
  menuButtonHidden: {
    display: 'none',
  },
  title: {
    flexGrow: 1,
  },
  drawerPaper: {
    position: 'relative',
    whiteSpace: 'nowrap',
    width: drawerWidth,
    transition: theme.transitions.create( 'width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    } ),
  },
  drawerPaperClose: {
    overflowX: 'hidden',
    transition: theme.transitions.create( 'width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    } ),
    width: theme.spacing.unit * 7,
    [ theme.breakpoints.up( 'sm' )]: {
      width: theme.spacing.unit * 9,
    },
  },
  appBarSpacer: theme.mixins.toolbar,
  content: {
    flexGrow: 1,
    padding: theme.spacing.unit * 3,
    height: '100vh',
    overflow: 'auto',
  },
  chartContainer: {
    marginLeft: -22,
  },
  tableContainer: {
    height: 320,
  },
  h5: {
    marginBottom: theme.spacing.unit * 2,
  },
});

function PrivateRoute({ component: Component, ...rest }) {
  return (
    <Route
      {...rest}
      render={props =>
        config.isConnected ? (
          <Component {...props} />
        ) : (
          <Redirect
            to={{
              pathname: "/login",
              state: { from: props.location }
            }}
          />
        )
      }
    />
  );
}

// This is a class-based component because the current
// version of hot reloading won't hot reload a stateless
// component at the top-level.

class App extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      open: true,
      anchorEl: null, 
      config: null
    };

  }
  
  handleDrawerOpen = () => {
    this.setState( {
      open: true
    } );
  };

  handleDrawerClose = () => {
    this.setState( {
      open: false
    } );
  };

  handleProfileMenuOpen = event => {
    this.setState({ anchorEl: event.currentTarget });
  };

  handleMenuClose = () => {
    this.setState({ anchorEl: null });
  };

  handleSignOut = () => {
    this.handleMenuClose();
    config.username = null;
    config.password = null;
    config.user = null;
    config.isConnected = false;
    config.isConnectionPending = false;
    history.push("/login")
  }


  render() {
    const {classes} = this.props;
    const { anchorEl } = this.state;
    const isMenuOpen = Boolean(anchorEl);
    const renderMenu = (
      <Menu
        anchorEl={anchorEl}
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        open={isMenuOpen}
        onClose={this.handleMenuClose}
      >
        <MenuItem key="menu_items_profile" onClick={this.handleMenuClose}>Profile</MenuItem>
        <MenuItem key="menu_items_myaccount" onClick={this.handleMenuClose}>My account</MenuItem>


        {config.user && config.user.roles.map(row => (
           <MenuItem key={"menu_items"+row}>{row}</MenuItem>
        ))}
        <MenuItem key="menu_items_signout" onClick={this.handleSignOut}>Sign Out</MenuItem>
      </Menu>
    );

    return (
    <div className={ classes.root }>
      <CssBaseline />
      <AppBar position="absolute" className={ classNames( classes.appBar, this.state.open && classes.appBarShift ) }>
        <Toolbar disableGutters={ !this.state.open } className={ classes.toolbar }>
          <IconButton color="inherit"
                      aria-label="Open drawer"
                      onClick={ this.handleDrawerOpen }
                      className={ classNames(
                                    classes.menuButton,
                                    this.state.open && classes.menuButtonHidden,
                                  ) }>
            <MenuIcon />
          </IconButton>
          <Typography component="h1"
                      variant="h6"
                      color="inherit"
                      noWrap
                      className={ classes.title }>
            VTN Control
          </Typography>
        {/* 
          <IconButton color="inherit">
            <Badge badgeContent={ 4 } color="secondary">
             {(config.isConnected) ? config.user.username: ""} <AccountBoxIcon />
            </Badge>
          </IconButton>
          */}
          <IconButton
                aria-owns={isMenuOpen ? 'material-appbar' : undefined}
                aria-haspopup="true"
                onClick={this.handleProfileMenuOpen}
                color="inherit"
              >
                <AccountCircleIcon />
              </IconButton>
        </Toolbar>
      </AppBar>
      {renderMenu}
      <Drawer variant="permanent"
              classes={ { paper: classNames( classes.drawerPaper, !this.state.open && classes.drawerPaperClose ), } }
              open={ this.state.open }>
        <div className={ classes.toolbarIcon }>
          <IconButton onClick={ this.handleDrawerClose }>
            <ChevronLeftIcon />
          </IconButton>
        </div>
        <Divider />
        <List>
          <NavigationMain classes={classes}/>
        </List>
        <Divider />
        <List>

        </List>
      </Drawer>
      <main className={ classes.content }>
        <div className={ classes.appBarSpacer } />
        {(config.connectionError == null) ? <Switch basename="/testvtn">

          <PrivateRoute exact
                 path="/"
                 component={ HomePage } />


                 
          <Route path="/login" component={ LoginPage } />
          <Route path="/about" component={ AboutPage } />

          <PrivateRoute path="/account/app/create" component={ AccountAppCreatePage } />
          <PrivateRoute path="/account/user/create" component={ AccountUserCreatePage } />
          <PrivateRoute path="/account/:panel(user|app)" component={ AccountPage } />
          <PrivateRoute path="/account" component={ AccountPage } />

          AccountAppCreatePage

          <PrivateRoute path="/vtn_configuration/:panel(marketcontext|group|parameter)" component={ VtnConfigurationPage } />
          <PrivateRoute path="/vtn_configuration" component={ VtnConfigurationPage } />
          
          <PrivateRoute path="/ven/detail/:username/reports/:reportSpecifierId/requests/:reportRequestId" component={ VenDetailReportRequestPage } />
          <PrivateRoute path="/ven/detail/:username/reports/:reportSpecifierId/create" component={ VenDetailCreateReportPage } />
          <PrivateRoute path="/ven/detail/:username/reports/:reportSpecifierId" component={ VenDetailReportPage } />
          <PrivateRoute path="/ven/detail/:username/:panel(settings|reports|optschedules)" component={ VenDetailPage } />
          <PrivateRoute path="/ven/detail/:username" component={ VenDetailPage } />
          <PrivateRoute path="/ven/create" component={ VenCreatePage } />
          <PrivateRoute path="/ven" component={ VenPage } />

          <PrivateRoute path="/event/detail/:id/:panel(descriptor|activeperiod|signal|target|venresponse)" component={ EventDetailPage } />
          <PrivateRoute path="/event/detail/:id" component={ EventDetailPage } />
          <PrivateRoute path="/event/create" component={ EventCreatePage } />
          <PrivateRoute path="/event/:panel(list|calendar)" component={ EventPage } />
          <PrivateRoute path="/event" component={ EventPage } />

          <Route component={ NotFoundPage } />

        </Switch>: null}


        {(config.connectionError != null) ? <div>
          {console.log(config.connectionError)}
        </div>: null}
        
        
        
      </main>
    </div>
    );
  }
}

App.propTypes = {
  children: PropTypes.element
};

export default withStyles( styles )( App );




