import React from 'react';
import ExtensionIcon from '@material-ui/icons/Extension';
import GroupWorkIcon from '@material-ui/icons/GroupWork';
import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';


export var MarketContextChip = (props) => {
  return (
  <span style={ { display: 'flex', alignItems: 'center', marginLeft: '-7px', } }><ExtensionIcon color="disabled" style={ { marginRight: '5px' } }/> { props.name }</span>
  );
}

export var GroupChip = (props) => {
  return (
  <span style={ { display: 'flex', alignItems: 'center', marginLeft: '-7px', } }><GroupWorkIcon color="disabled" style={ { marginRight: '5px' } }/> { props.name }</span>
  );
}

export var VenStatusChip = (props) => {
  return (
  <span style={ { display: 'flex', alignItems: 'center', marginLeft: '-7px', } }><SettingsInputComponentIcon color="disabled" style={ { marginRight: '5px' } }/> { props.name }</span>
  );
}