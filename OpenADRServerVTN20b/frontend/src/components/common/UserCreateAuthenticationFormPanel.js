import React from 'react';



import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';


import TextField from '@material-ui/core/TextField';



import Grid from '@material-ui/core/Grid';



import MenuItem from '@material-ui/core/MenuItem';

import Select from '@material-ui/core/Select';




const labelStyle = {

  boxSizing: 'border-box',
  color: 'rgba(0, 0, 0, 0.54)',
  fontSize: '1rem',
  fontWeight: 400,

  lineHeight: 1,
  transition: 'color 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms,transform 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms',
  transform: 'translate(0, 1.5px) scale(0.75)',
  transformOrigin: 'top left',
  top: 0,
  left: 0,
}

export class UserCreateAuthenticationFormPanel extends React.Component {





  handleAuthenticationTypeChange = (e) => {
    var authentication = this.props.authentication;
    authentication.authenticationType = e.target.value;
    this.props.onChange(authentication);
  };


  handleAuthenticationPasswordChange = (e) => {
    var authentication = this.props.authentication;
    authentication.authenticationPassword = e.target.value;
    this.props.onChange(authentication);
  };

  handleAuthenticationPasswordConfirmChange = (e) => {
    var authentication = this.props.authentication;
    authentication.authenticationPasswordConfirm = e.target.value;
    this.props.onChange(authentication);
  };

  handleAuthenticationUsernameChange = (e) => {
    console.log(e)
    var authentication = this.props.authentication;
    authentication.username = e.target.value;
    this.props.onChange(authentication);
  }


  render() {
    const {classes, hasError, identification, authentication} = this.props;

    var authenticationTypeView = []

    for (var key in authentication.authenticationTypes) {
      var value = authentication.authenticationTypes[ key ];
      authenticationTypeView.push( <MenuItem key={ key } value={ key }>
                                   { value.label }
                                   </MenuItem> )
    }

    var loginPasswordView = null;

    var missingPassword = null;
    if ( hasError && authentication.authenticationType === "login" && authentication.authenticationPassword === '' ) {
      missingPassword = true;
    }

    if ( authentication.authenticationType === "login" ) {

      loginPasswordView = <Grid container spacing={ 24 }>
                            <Grid item xs={ 2 } />
                            <Grid item xs={ 4 }>
                              <FormControl className={ classes.formControl }>
                                <TextField required={ true }
                                           type="password"
                                           id="password_textfield"
                                           label="Password"
                                           value={ authentication.authenticationPassword }
                                           className={ classes.textField }
                                           error={ missingPassword }
                                           onChange={ this.handleAuthenticationPasswordChange }
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
                                           value={ authentication.authenticationPasswordConfirm }
                                           className={ classes.textField }
                                           error={ this.props.hasError }
                                           onChange={ this.handleAuthenticationPasswordConfirmChange }
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
              Authentication Method
            </FormLabel>
            <Select value={ authentication.authenticationType }
                    style={ { marginTop: 0 } }
                    onChange={ this.handleAuthenticationTypeChange }
                    inputProps={ { name: 'VEN Authentication Type', id: 'oadr_authentication_select', } }>
              { authenticationTypeView }
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={ 4 }>
          <FormControl className={ classes.formControl }>
            <TextField required
                       id="login_textfield"
                       label="Username"
                       value={ (identification.needCertificateGeneration !== "no" 
                        && authentication.authenticationType !== "login") ? '<generated>' : authentication.username }
                       disabled={ (identification.needCertificateGeneration !== "no")}
                       className={ classes.textField }
                       onChange={ this.handleAuthenticationUsernameChange }
                       InputLabelProps={ { shrink: true, } } />
          </FormControl>
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>
      { loginPasswordView }
    </Grid>
    );
  }
}

export default UserCreateAuthenticationFormPanel;
