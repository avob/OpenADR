import React from 'react';
import { NavLink } from 'react-router-dom';

import { connect } from 'react-redux';

import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';

import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import SettingsIcon from '@material-ui/icons/Settings';
import ExtensionIcon from '@material-ui/icons/Extension';
import GroupWorkIcon from '@material-ui/icons/GroupWork';

import CalendarTodayIcon from '@material-ui/icons/CalendarToday';
import AccountBoxIcon from '@material-ui/icons/AccountBox';

import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import Collapse from '@material-ui/core/Collapse';

import EventIcon from '@material-ui/icons/Event';
import ListIcon from '@material-ui/icons/List';

import { history } from '../store/configureStore';

 class NavigationMain extends React.Component {

 	constructor( props ) {
		super( props );
		this.state = {}
	}

 	render() {
 			var user = this.props.user.user;
 			const router = this.props.router;
 			var location = this.props.router.location;
			const hasEventAccess = user && user.roles 
				&& (user.roles.includes("ROLE_DRPROGRAM") || user.roles.includes("ROLE_ADMIN"));

			const hasVenAccess = user && user.roles 
				&& (user.roles.includes("ROLE_DEVICE_MANAGER") || user.roles.includes("ROLE_ADMIN"))

			const hasVtnConfigurationAccess = user && user.roles && user.roles.includes("ROLE_ADMIN")

			const isEvent = location.pathname === '/event' || location.pathname.includes("/event/");
			const isVen = location.pathname === '/ven' || location.pathname.includes("/ven/")
			const isVtnConfiguration = location.pathname === '/vtn_configuration' || location.pathname.includes("/vtn_configuration/")

	
		  return (	
		 <div>
		 	{(hasEventAccess) ? 
		 		<span>
				<ListItem button
						component={ NavLink }
						to="/event">
					<ListItemIcon>
						<CalendarTodayIcon />
					</ListItemIcon>
					<ListItemText primary="Events" />
				</ListItem> 
				<Collapse in={isEvent} >
			          <ListItem button component={ NavLink } 
						to="/event">
			            <ListItemIcon fontSize="small">
			              <CalendarTodayIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Search"/>
			          </ListItem>
			          <ListItem button component={ NavLink } to="/event/calendar"  >
			            <ListItemIcon fontSize="small">
			              <EventIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Calendar" />
			          </ListItem>
			      </Collapse>
			      </span>
		  	: null}

		  {(hasVenAccess) ?  <span><ListItem button
		            component={ NavLink }
		            to="/ven">
		    <ListItemIcon>
		      <SettingsInputComponentIcon />
		    </ListItemIcon>
		    <ListItemText primary="VENs" />
		  </ListItem> 
		  <Collapse in={isVen} >
			          <ListItem button component={ NavLink } to="/ven/resource"  >
			            <ListItemIcon fontSize="small">
			              <SettingsInputComponentIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Resource" />
			          </ListItem>
			      </Collapse>
			      </span>: null}

		  {(hasVtnConfigurationAccess) ?  
		  	<span>
		  	<ListItem button
		            component={ NavLink }
		            to="/vtn_configuration">
		    <ListItemIcon>
		      <SettingsIcon />
		    </ListItemIcon>
		    <ListItemText primary="VTN Config" />
		  </ListItem> 
		  <Collapse in={isVtnConfiguration} >
			          <ListItem button component={ NavLink } 
						to="/vtn_configuration/parameter">
			            <ListItemIcon fontSize="small">
			              <SettingsIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Settings"/>
			          </ListItem>
			          <ListItem button component={ NavLink } to="/vtn_configuration/marketcontext"  >
			            <ListItemIcon fontSize="small">
			              <ExtensionIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="MarketContexts" />
			          </ListItem>
			          <ListItem button component={ NavLink } to="/vtn_configuration/group"  >
			            <ListItemIcon fontSize="small">
			              <GroupWorkIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Groups" />
			          </ListItem>
			          <ListItem button component={ NavLink } to="/vtn_configuration/account/user"  >
			            <ListItemIcon fontSize="small">
			              <AccountBoxIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Accounts" />
			          </ListItem>
			          <ListItem button component={ NavLink } to="/vtn_configuration/known/unit"  >
			            <ListItemIcon fontSize="small">
			              <AccountBoxIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Known Entities" />
			          </ListItem>
			      </Collapse>
		  </span>
		  : null}
		  
		</div>
		  );
 	}
}


function mapStateToProps( state ) {
  return {
    user: state.user,
    router: state.router
  };
}

function mapDispatchToProps( dispatch ) {
  return {

  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( NavigationMain );

