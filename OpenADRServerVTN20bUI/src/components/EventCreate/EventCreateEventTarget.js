import React from 'react';

import Typography from '@material-ui/core/Typography';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';

import TextField from '@material-ui/core/TextField';

import Grid from '@material-ui/core/Grid';


import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import Select from '@material-ui/core/Select';

import Divider from '@material-ui/core/Divider';
import AddIcon from '@material-ui/icons/Add';

import Button from '@material-ui/core/Button';

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import ExtensionIcon from '@material-ui/icons/Extension';
import GroupWorkIcon from '@material-ui/icons/GroupWork';
import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';

import { GroupSelectDialog, TargetSelectDialog } from '../common/VtnconfigurationDialog'


import {EventTargetPanel} from '../common/EventTargetPanel'



export class EventCreateEventTarget extends React.Component {

  constructor( props ) {
    super( props )
   
  }


  render() {
    const {classes, hasError, eventTarget, group, ven} = this.props;

    return (
    <Grid container
          spacing={ 8 }
          justify="center">
      
      <Grid container spacing={ 24 }>
        <Grid item xs={ 2 } />
          <Grid item xs={ 8 }>
            <EventTargetPanel classes={classes} eventTarget={eventTarget} group={group} onChange={this.props.onChange}
            ven={ven}
            onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
            onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
            onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}/>
          </Grid>

          
        <Grid item xs={ 2 } />
      </Grid>
     
      
    </Grid>
    );
  }
}

export default EventCreateEventTarget;
