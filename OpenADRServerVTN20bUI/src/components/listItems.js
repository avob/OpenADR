import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import DashboardIcon from '@material-ui/icons/Dashboard';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import PeopleIcon from '@material-ui/icons/People';
import BarChartIcon from '@material-ui/icons/BarChart';
import LayersIcon from '@material-ui/icons/Layers';
import AssignmentIcon from '@material-ui/icons/Assignment';

import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import SettingsIcon from '@material-ui/icons/Settings';

import CalendarTodayIcon from '@material-ui/icons/CalendarToday';
import AccountBoxIcon from '@material-ui/icons/AccountBox';


import { NavLink, Route, Switch } from 'react-router-dom';

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
</div>
);

export const secondaryListItems = (
<div>
  <ListSubheader inset>
    Exports
  </ListSubheader>
  <ListItem button>
    <ListItemIcon>
      <AssignmentIcon />
    </ListItemIcon>
    <ListItemText primary="Export VENs" />
  </ListItem>
</div>
);
