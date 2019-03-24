import React from 'react';


import Avatar from '@material-ui/core/Avatar';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import ListItemText from '@material-ui/core/ListItemText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';

import ExtensionIcon from '@material-ui/icons/Extension';
import GroupWorkIcon from '@material-ui/icons/GroupWork';
import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';

import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';


export function MarketContextSelectDialog( props ) {
  return (
  <Dialog open={ props.open } onClose={ () => {
                                        props.close()
                                      } }>
    <DialogTitle>
      { props.title }
    </DialogTitle>
    <div>
      <List>
        { props.marketContext.map( context => (
            <ListItem button
                      onClick={ () => {
                                  props.close( context )
                                } }
                      key={ context.name }>
              <ListItemAvatar>
                <Avatar style={ { backgroundColor: context.color } }>
                  <ExtensionIcon />
                </Avatar>
              </ListItemAvatar>
              <ListItemText primary={ context.name } />
            </ListItem>
          ) ) }
      </List>
    </div>
  </Dialog>
  );
}

export function GroupSelectDialog( props ) {
  return (
  <Dialog open={ props.open } onClose={ () => {
                                        props.close()
                                      } }>
    <DialogTitle>
      { props.title }
    </DialogTitle>
    <div>
      <List>
        { props.group.map( g => (
            <ListItem button
                      onClick={ () => {
                                  props.close( g )
                                } }
                      key={ g.name }>
              <ListItemAvatar>
                <Avatar style={ { backgroundColor: '#bbb' } }>
                  <GroupWorkIcon />
                </Avatar>
              </ListItemAvatar>
              <ListItemText primary={ g.name } />
            </ListItem>
          ) ) }
      </List>
    </div>
  </Dialog>
  );
}

export function TargetSelectDialog( props ) {
  return (
  <Dialog open={ props.open } onClose={ () => {
                                        props.close()
                                      } }>
    <DialogTitle>
      { props.title }
    </DialogTitle>
    <div>
      <List>
        <ListItem button
                  onClick={ () => {
                              props.close( "group" )
                            } } >
          <ListItemAvatar>
            <Avatar style={ { backgroundColor: '#bbb' } }>
              <GroupWorkIcon /> 
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Group" />
        </ListItem>
        <ListItem button
                  onClick={ () => {
                              props.close( "ven" )
                            } } >
          <ListItemAvatar>
            <Avatar style={ { backgroundColor: '#bbb' } }>
              <SettingsInputComponentIcon /> 
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Ven" />
        </ListItem>
      </List>
    </div>
  </Dialog>
  );
}

export function VenStatusSelectDialog( props ) {
  var items = [{
    name: "online",
    color: "green"
  },{
    name: "offline",
    color: "#bbb"
  }]
  return (
  <Dialog open={ props.open } onClose={ () => {
                                        props.close()
                                      } }>
    <DialogTitle>
      { props.title }
    </DialogTitle>
    <div>
      <List>
        { items.map( g => (
            <ListItem button
                      onClick={ () => {
                                  props.close( g )
                                } }
                      key={ g.name }>
              <ListItemAvatar>
                <Avatar style={ { backgroundColor: g.color } }>
                  <SettingsInputComponentIcon />
                </Avatar>
              </ListItemAvatar>
              <ListItemText primary={ g.name } />
            </ListItem>
          ) ) }
        
      </List>
    </div>
  </Dialog>
  );
}

export function EventCalendarDialog( props ) {
  if(!props.event) return null;
  return (
  <Dialog open={ props.open } onClose={ () => {
                                        props.close()
                                      } }>
    <DialogTitle>
      { props.event.title }
    </DialogTitle>
    <div>
      <List>
        <ListItem >
          <ListItemAvatar>
            <Avatar>
              <SettingsInputComponentIcon />
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary={ props.event.marketContext } />
        </ListItem>
      </List>
    </div>
      <DialogActions>
        <Button onClick={props.close} color="primary">
          close
        </Button>
        <Button onClick={props.handleEventDetailClick} color="primary" autoFocus>
          Event Detail
        </Button>
      </DialogActions>
  </Dialog>
  );
}
