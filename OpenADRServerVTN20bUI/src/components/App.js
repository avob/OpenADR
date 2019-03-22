/* eslint-disable import/no-named-as-default */
import {  Route, Switch } from 'react-router-dom';

import AboutPage from './AboutPage';
import HomePage from './HomePage';
import NotFoundPage from './NotFoundPage';
import VtnConfigurationPage from './containers/VtnConfigurationPage';
import VenPage from './containers/VenPage'
import AccountPage from './containers/AccountPage'
import EventPage from './containers/EventPage'
import EventDetailPage from './containers/EventDetailPage'
import EventCreatePage from './containers/EventCreatePage'
import VenDetailPage from './containers/VenDetailPage'
import VenDetailCreateReportPage from './containers/VenDetailCreateReportPage'
import VenDetailReportPage from './containers/VenDetailReportPage'
import VenDetailReportRequestPage from './containers/VenDetailReportRequestPage'






import VenCreatePage from './containers/VenCreatePage'

import PropTypes from 'prop-types';
import React from 'react';
import { hot } from 'react-hot-loader';

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
import Badge from '@material-ui/core/Badge';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import NotificationsIcon from '@material-ui/icons/Notifications';
import { mainListItems, secondaryListItems } from './listItems';


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

// This is a class-based component because the current
// version of hot reloading won't hot reload a stateless
// component at the top-level.

class App extends React.Component {

  state = {
    open: true,
  };

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


  render() {
    const {classes} = this.props;

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
            Dashboard
          </Typography>
          <IconButton color="inherit">
            <Badge badgeContent={ 4 } color="secondary">
              <NotificationsIcon />
            </Badge>
          </IconButton>
        </Toolbar>
      </AppBar>
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
          { mainListItems }
        </List>
        <Divider />
        <List>
          { secondaryListItems }
        </List>
      </Drawer>
      <main className={ classes.content }>
        <div className={ classes.appBarSpacer } />
        <Switch>

          <Route exact
                 path="/"
                 component={ HomePage } />

          <Route path="/about" component={ AboutPage } />

          <Route path="/vtn_configuration/:panel(marketcontext|group|parameter)" component={ VtnConfigurationPage } />
          <Route path="/vtn_configuration" component={ VtnConfigurationPage } />
          

          <Route path="/ven/detail/:username/reports/:reportSpecifierId/requests/:reportRequestId" component={ VenDetailReportRequestPage } />
          <Route path="/ven/detail/:username/reports/:reportSpecifierId/create" component={ VenDetailCreateReportPage } />

          <Route path="/ven/detail/:username/reports/:reportSpecifierId" component={ VenDetailReportPage } />

          <Route path="/ven/detail/:username/:panel(settings|reports|optschedules)" component={ VenDetailPage } />
          <Route path="/ven/detail/:username" component={ VenDetailPage } />
          
          <Route path="/ven/create" component={ VenCreatePage } />

          <Route path="/ven" component={ VenPage } />

          <Route path="/account" component={ AccountPage } />

          <Route path="/event/detail/:id/:panel(descriptor|activeperiod|signal|target|venresponse)" component={ EventDetailPage } />
          <Route path="/event/detail/:id" component={ EventDetailPage } />

          <Route path="/event/create" component={ EventCreatePage } />
          <Route path="/event/:panel(list|calendar)" component={ EventPage } />
          <Route path="/event" component={ EventPage } />

          <Route component={ NotFoundPage } />

        </Switch>
      </main>
    </div>
    );
  }
}

App.propTypes = {
  children: PropTypes.element
};

export default hot( module )( withStyles( styles )( App ) );
