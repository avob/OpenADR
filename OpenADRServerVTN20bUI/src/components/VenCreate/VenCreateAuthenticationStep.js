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


const labelStyle = {

  boxSizing: 'border-box',
  color: 'rgba(0, 0, 0, 0.54)',
  fontSize: '1rem',
  fontWeight: 400,
  left: '0px',
  lineHeight: 1,
  transition: 'color 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms,transform 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms',
  transform: 'translate(0, 1.5px) scale(0.75)',
  transformOrigin: 'top left',
  top: 0,
  left: 0,
}

export class VenCreateAuthenticationStep extends React.Component {

  constructor( props ) {
    super( props );
  }


  render() {
    const {classes, hasError} = this.props;

    var authenticationTypeView = []

    for (var key in this.props.authenticationTypes) {
      var value = this.props.authenticationTypes[ key ];
      authenticationTypeView.push( <MenuItem key={ key } value={ key }>
                                   { value.label }
                                   </MenuItem> )
    }

    var loginPasswordView = null;

    var missingVenId = false;
    if ( hasError && !this.props.needCertificateGeneration && this.props.authenticationVenId == '' ) {
      missingVenId = true;
    }

    console.log( this.props )
    var missingPassword = null;
    if ( hasError && this.props.needLogin && this.props.authenticationPassword == '' ) {
      missingPassword = true;
    }

    if ( this.props.needLogin ) {

      loginPasswordView = <Grid container spacing={ 24 }>
                            <Grid item xs={ 2 } />
                            <Grid item xs={ 4 }>
                              <FormControl className={ classes.formControl }>
                                <TextField required={ true }
                                           type="password"
                                           id="password_textfield"
                                           label="Password"
                                           value={ this.props.authenticationPassword }
                                           className={ classes.textField }
                                           error={ missingPassword }
                                           onChange={ this.props.handleAuthenticationPasswordChange }
                                           InputLabelProps={ { shrink: true, } } />
                              </FormControl>
                            </Grid>
                            <Grid item xs={ 4 }>
                              <FormControl className={ classes.formControl }>
                                <TextField required
                                           type="password"
                                           fullWidth={ true }
                                           id="password_confirm_textfield"
                                           label="Password Confirm"
                                           value={ this.props.authenticationPasswordConfirm }
                                           className={ classes.textField }
                                           error={ this.props.hasError }
                                           onChange={ this.props.handleAuthenticationPasswordConfirmChange }
                                           InputLabelProps={ { shrink: true, } } />
                              </FormControl>
                            </Grid>
                            <Grid item xs={ 2 } />
                          </Grid>
    }

    return (
    <Grid container
          spacing={ 8 }
          justify="center">
      <Grid container spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 4 }>
          <FormControl className={ classes.formControl }>
            <FormLabel style={ labelStyle } component="label">
              VEN Authentication
            </FormLabel>
            <Select value={ this.props.authenticationType }
                    style={ { marginTop: 0 } }
                    onChange={ this.props.handleAuthenticationTypeChange }
                    inputProps={ { name: 'VEN Authentication Type', id: 'oadr_authentication_select', } }>
              { authenticationTypeView }
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={ 4 }>
          <FormControl className={ classes.formControl }>
            <TextField required
                       error={ missingVenId }
                       id="login_textfield"
                       label="VEN Id"
                       value={ (this.props.needCertificateGeneration) ? '<generated>' : this.props.authenticationVenId }
                       className={ classes.textField }
                       error={ missingVenId }
                       onChange={ this.props.handleAuthenticationVenIdChange }
                       InputLabelProps={ { shrink: true, } }
                       disabled={ this.props.needCertificateGeneration } />
          </FormControl>
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>
      { loginPasswordView }
    </Grid>
    );
  }
}

export default VenCreateAuthenticationStep;
