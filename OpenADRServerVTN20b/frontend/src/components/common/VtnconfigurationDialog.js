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
import CalendarTodayIcon from '@material-ui/icons/CalendarToday';
import SendIcon from '@material-ui/icons/Send';

import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';

import {VenAutocomplete} from './Autocomplete'

import amber from '@material-ui/core/colors/amber';

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

export function VenSelectDialog( props ) {
  return (
  <Dialog open={ props.open } onClose={ () => {
                                        props.close()
                                      } }>
    <DialogTitle>
      { props.title }
    </DialogTitle>
    <div>
      <List>
       <ListItem style={{marginBottom:250, width: 400}}>

        <VenAutocomplete  suggestions={props.suggestions}
        onSuggestionsFetchRequested={props.onSuggestionsFetchRequested}
        onSuggestionsClearRequested={props.onSuggestionsClearRequested}
        onSuggestionsSelect={props.onSuggestionsSelect}/>
      </ListItem>
        
      </List>
    </div>
  </Dialog>
  );
}

export function EventStatusSelectDialog( props ) {
  var items = [{
    name: "ACTIVE",
    color: "green",
    label: "Active"
  },{
    name: "CANCELLED",
    color: "red",
    label: "Cancelled"
  },{
    name: "PUBLISHED",
    color: "#bbb",
    label: "Published"
  },{
    name: "NOT_PUBLISHED",
    color: amber[700],
    label: "Not Published"
  },{
    name: "SENDABLE",
    color: "#bbb",
    label: "Sendable"
  },{
    name: "NOT_SENDABLE",
    color: "#bbb",
    label: "Not Sendable"
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
                  <CalendarTodayIcon />
                </Avatar>
              </ListItemAvatar>
              <ListItemText primary={ g.label } />
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

export function VenActionDialog( props ) {
  return (
  <Dialog open={ props.open } onClose={ () => {
                                        props.close()
                                      } }>
    <DialogTitle>
      { props.title }
    </DialogTitle>
    <div>
      <List>
        <ListItem button onClick={() => { props.venActions.requestRegisterReport(props.ven.username); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Request register report"/>
        </ListItem>
        <ListItem button onClick={() => { props.venActions.sendRegisterReport(props.ven.username); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Send register report"/>
        </ListItem>
        <ListItem button onClick={() => { props.venActions.cleanRegistration(props.ven.username); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Clean registration"/>
        </ListItem>
        <ListItem button onClick={() => { props.venActions.registerPartyRequestReregistration(props.ven.username); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Request re-registration"/>
        </ListItem>
        <ListItem button onClick={() => { props.venActions.registerPartyCancelPartyRegistration(props.ven.username); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Request cancel registration"/>
        </ListItem>
        <ListItem button onClick={() => { props.venActions.requestRegisterReport(props.ven.username); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Request register report"/>
        </ListItem>
        <ListItem button onClick={() => { props.venActions.sendRegisterReport(props.ven.username)}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Push create report"/>
        </ListItem>
      </List>
    </div>
      <DialogActions>
        <Button onClick={props.close} color="primary">
          close
        </Button>
      </DialogActions>
  </Dialog>
  );


}

export function EventActionDialog( props ) {
  return (
  <Dialog open={ props.open } onClose={ () => {
                                        props.close()
                                      } }>
    <DialogTitle>
      { props.title }
    </DialogTitle>
    <div>
      <List>
        <ListItem button onClick={() => { props.eventActions.publishEvent(props.event.id); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Publish"/>
        </ListItem>
        <ListItem button onClick={() => { props.eventActions.publishEvent(props.event.id); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Publish"/>
        </ListItem>
        <ListItem button onClick={() => { props.eventActions.publishEvent(props.event.id); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Publish"/>
        </ListItem>
        <ListItem button onClick={() => { props.eventActions.activeEvent(props.event.id); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Active"/>
        </ListItem>
        <ListItem button onClick={() => { props.eventActions.cancelEvent(props.event.id); props.close()}}>
          <ListItemAvatar>
            <Avatar>
              <SendIcon fontSize="small"/>
            </Avatar>
          </ListItemAvatar>
          <ListItemText primary="Cancel"/>
        </ListItem>
      </List>
    </div>
      <DialogActions>
        <Button onClick={props.close} color="primary">
          close
        </Button>
      </DialogActions>
  </Dialog>
  );
}

