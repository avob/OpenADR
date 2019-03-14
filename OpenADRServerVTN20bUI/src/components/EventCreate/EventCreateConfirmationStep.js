import React from 'react';

import Typography from '@material-ui/core/Typography';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';

import TextField from '@material-ui/core/TextField';

import ExtensionIcon from '@material-ui/icons/Extension';

import Grid from '@material-ui/core/Grid';


import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import Select from '@material-ui/core/Select';

import Divider from '@material-ui/core/Divider';


export class EventCreateConfirmationStep extends React.Component {

  constructor( props ) {
    super( props );
  }


  render() {
    const {classes, hasError} = this.props;

    return (
    <Grid container
          spacing={ 8 }
          justify="center">
      <Grid container spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 4 }>
        </Grid> 
        <Grid item xs={ 4 }>
         
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>

    </Grid>
    );
  }
}

export default EventCreateConfirmationStep;
