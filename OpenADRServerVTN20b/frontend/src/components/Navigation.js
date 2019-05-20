import React from 'react';
import { NavLink } from 'react-router-dom';

import { connect } from 'react-redux';

import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';

import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import SettingsIcon from '@material-ui/icons/Settings';

import CalendarTodayIcon from '@material-ui/icons/CalendarToday';
import AccountBoxIcon from '@material-ui/icons/AccountBox';


 class NavigationMain extends React.Component {
 	render() {
 		var user = this.props.user.user;
			const hasEventAccess = user && user.roles 
				&& (user.roles.includes("ROLE_DRPROGRAM") || user.roles.includes("ROLE_ADMIN"));

			const hasVenAccess = user && user.roles 
				&& (user.roles.includes("ROLE_DEVICE_MANAGER") || user.roles.includes("ROLE_ADMIN"))

			const hasVtnConfigurationAccess = user && user.roles && user.roles.includes("ROLE_ADMIN")

			const hasAccountAccess = user && user.roles && user.roles.includes("ROLE_ADMIN")

		  return (
		 <div>
		 	{(hasEventAccess) ? <ListItem button
		            component={ NavLink }
		            to="/event">
		    <ListItemIcon>
		      <CalendarTodayIcon />
		    </ListItemIcon>
		    <ListItemText primary="Events" />
		  </ListItem> : null}

		  {(hasVenAccess) ?  <ListItem button
		            component={ NavLink }
		            to="/ven">
		    <ListItemIcon>
		      <SettingsInputComponentIcon />
		    </ListItemIcon>
		    <ListItemText primary="VENs" />
		  </ListItem> : null}

		  {(hasVtnConfigurationAccess) ?  <ListItem button
		            component={ NavLink }
		            to="/vtn_configuration">
		    <ListItemIcon>
		      <SettingsIcon />
		    </ListItemIcon>
		    <ListItemText primary="VTN Config" />
		  </ListItem> : null}

		   {(hasAccountAccess) ?  <ListItem button
		            component={ NavLink }
		            to="/account">
		    <ListItemIcon>
		      <AccountBoxIcon />
		    </ListItemIcon>
		    <ListItemText primary="Accounts" />
		  </ListItem> : null}
		  
		 
		  

		  

		  

		  {/*
		  <ListItem button
		            component={ NavLink }
		            to="/data">
		    <ListItemIcon>
		      <ShowChartIcon />
		    </ListItemIcon>
		    <ListItemText primary="Data" />
		  </ListItem>

		  */}
		  
		</div>
		  );
 	}
}


function mapStateToProps( state ) {
  return {
    user: state.user
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

