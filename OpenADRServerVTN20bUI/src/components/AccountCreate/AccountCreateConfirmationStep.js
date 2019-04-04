import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import classNames from 'classnames';

import Typography from '@material-ui/core/Typography';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';

import TextField from '@material-ui/core/TextField';

import ExtensionIcon from '@material-ui/icons/Extension';

import Grid from '@material-ui/core/Grid';

import InputAdornment from '@material-ui/core/InputAdornment';

import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import Select from '@material-ui/core/Select';

import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import WarningIcon from '@material-ui/icons/Warning';

import Divider from '@material-ui/core/Divider';



export class AccountCreateConfirmationStep extends React.Component {

  constructor( props ) {
    super( props );
  }


  render() {
    const {classes} = this.props;

    return (

    <div>
      <Grid container
            spacing={ 8 }
            justify="center">

        <Grid container
              style={ { marginTop: 20, marginBottom: 20 } }
              spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 8 }>
            <Divider />
          </Grid>
          <Grid item xs={ 2 } />
        </Grid>
      </Grid>
    </div>
    );
  }
}

export default AccountCreateConfirmationStep;
