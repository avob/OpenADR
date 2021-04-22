import React from 'react'
import { Link } from 'react-router-dom'

import { connect } from 'react-redux'

import ListItem from '@material-ui/core/ListItem'
import ListItemIcon from '@material-ui/core/ListItemIcon'
import ListItemText from '@material-ui/core/ListItemText'

import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent'
import SettingsIcon from '@material-ui/icons/Settings'
import ExtensionIcon from '@material-ui/icons/Extension'
import GroupWorkIcon from '@material-ui/icons/GroupWork'

import CalendarTodayIcon from '@material-ui/icons/CalendarToday'
import AccountBoxIcon from '@material-ui/icons/AccountBox'

import Collapse from '@material-ui/core/Collapse'

import EventIcon from '@material-ui/icons/Event'


class NavigationMain extends React.Component {
 	constructor (props) {
    super(props)
    this.state = {}
  }

 	render () {
 			const user = this.props.user.user
 			const location = this.props.router.location
    const hasEventAccess = user && user.roles &&
				(user.roles.includes('ROLE_DRPROGRAM') || user.roles.includes('ROLE_ADMIN'))

    const hasVenAccess = user && user.roles &&
				(user.roles.includes('ROLE_DEVICE_MANAGER') || user.roles.includes('ROLE_ADMIN'))

    const hasVtnConfigurationAccess = user && user.roles && user.roles.includes('ROLE_ADMIN')

    const isEvent = location.pathname === '/event' || location.pathname.includes('/event/')
    const isVen = location.pathname === '/ven' || location.pathname.includes('/ven/')
    const isVtnConfiguration = location.pathname === '/vtn_configuration' || location.pathname.includes('/vtn_configuration/')
    const isMarketContext = location.pathname === '/marketcontext' || location.pathname.includes('/marketcontext/')


		  return (
		 <div>
		 	{(hasEventAccess)
		 		? <span>
				<ListItem button component={ Link } to="/marketcontext" >
		            <ListItemIcon fontSize="small">
		              <ExtensionIcon />
		            </ListItemIcon>
		            <ListItemText primary="MarketContexts" />
		          </ListItem>
				<Collapse in={isMarketContext} >
			          <ListItem button component={ Link } to="/marketcontext/known/unit" >
			            <ListItemIcon fontSize="small">
			              <AccountBoxIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Known Entities" />
			          </ListItem>
			      </Collapse>
			      </span>
		  	: null}

		 	{(hasEventAccess)
		 		? <span>
				<ListItem button
						component={ Link }
						to="/event">
					<ListItemIcon>
						<CalendarTodayIcon />
					</ListItemIcon>
					<ListItemText primary="Events" />
				</ListItem>
				<Collapse in={isEvent} >
			          <ListItem button component={ Link } to="/event/calendar" >
			            <ListItemIcon fontSize="small">
			              <EventIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Calendar" />
			          </ListItem>
			      </Collapse>
			      </span>
		  	: null}

		  {(hasVenAccess)
		    ? <span><ListItem button
		            component={ Link }
		            to="/ven">
		    <ListItemIcon>
		      <SettingsInputComponentIcon />
		    </ListItemIcon>
		    <ListItemText primary="VENs" />
		  </ListItem>
		  <Collapse in={isVen} >
			          <ListItem button component={ Link } to="/ven/resource" >
			            <ListItemIcon fontSize="small">
			              <SettingsInputComponentIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Resource" />
			          </ListItem>
			      </Collapse>
			      </span>
		    : null}

		  {(hasVtnConfigurationAccess)
		  	? <span>
		  	<ListItem button
		            component={ Link }
		            to="/vtn_configuration">
		    <ListItemIcon>
		      <SettingsIcon />
		    </ListItemIcon>
		    <ListItemText primary="VTN Config" />
		  </ListItem>
		  <Collapse in={isVtnConfiguration} >
			          
			          <ListItem button component={ Link } to="/vtn_configuration/group" >
			            <ListItemIcon fontSize="small">
			              <GroupWorkIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Groups" />
			          </ListItem>
			          <ListItem button component={ Link } to="/vtn_configuration/account/user" >
			            <ListItemIcon fontSize="small">
			              <AccountBoxIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Accounts" />
			          </ListItem>
			          
			          <ListItem button component={ Link } to="/vtn_configuration/swagger" >
			            <ListItemIcon fontSize="small">
			              <AccountBoxIcon color="disabled"/>
			            </ListItemIcon>
			            <ListItemText secondary="Rest API" />
			          </ListItem>
			      </Collapse>
		  </span>
		  : null}

		</div>
		  )
 	}
}

function mapStateToProps (state) {
  return {
    user: state.user,
    router: state.router
  }
}

function mapDispatchToProps (dispatch) {
  return {

  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NavigationMain)
