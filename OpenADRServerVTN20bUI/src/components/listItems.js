import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import AssignmentIcon from '@material-ui/icons/Assignment';

import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import SettingsIcon from '@material-ui/icons/Settings';
import ShowChartIcon from '@material-ui/icons/ShowChart';

import CalendarTodayIcon from '@material-ui/icons/CalendarToday';
import AccountBoxIcon from '@material-ui/icons/AccountBox';


import { NavLink } from 'react-router-dom';

export const mainListItems = (
<div>
  <ListItem button
            component={ NavLink }
            to="/event">
    <ListItemIcon>
      <CalendarTodayIcon />
    </ListItemIcon>
    <ListItemText primary="Events" />
  </ListItem>
  <ListItem button
            component={ NavLink }
            to="/ven">
    <ListItemIcon>
      <SettingsInputComponentIcon />
    </ListItemIcon>
    <ListItemText primary="VENs" />
  </ListItem>
  <ListItem button
            component={ NavLink }
            to="/vtn_configuration">
    <ListItemIcon>
      <SettingsIcon />
    </ListItemIcon>
    <ListItemText primary="VTN Config" />
  </ListItem>

  

  <ListItem button
            component={ NavLink }
            to="/account">
    <ListItemIcon>
      <AccountBoxIcon />
    </ListItemIcon>
    <ListItemText primary="Accounts" />
  </ListItem>

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

export const secondaryListItems = (
<div>

  {/*

  <ListSubheader inset>
    Exports
  </ListSubheader>
  <ListItem button>
    <ListItemIcon>
      <AssignmentIcon />
    </ListItemIcon>
    <ListItemText primary="Export VENs" />
  </ListItem>

  */}

  
</div>
);
