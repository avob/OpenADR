import React from 'react';

import FormControl from '@material-ui/core/FormControl';

import Divider from '@material-ui/core/Divider';

import Grid from '@material-ui/core/Grid';
import FormGroup from '@material-ui/core/FormGroup';
import FormLabel from '@material-ui/core/FormLabel';

import {  VtnTextField, VtnFeatureField } from '../common/TextField'




const VtnConfigurationParameter = (props) => {
  const {classes, vtnConfiguration} = props;
  console.log(vtnConfiguration)
  var getTextField = function ( label, field ) {
    var view = null;
    var value = props.vtnConfiguration[ field ];
    if ( props.vtnConfiguration[ field ] != null ) {
      view = (
        <VtnTextField className={ classes.textField }
                                   field={ label }
                                   value={ value } />
      )
    }
    return view;
  }

  var getUrlTextField = function () {
    var view = null;
    if ( vtnConfiguration.contextPath != null
      && vtnConfiguration.port != null
      && vtnConfiguration.host != null ) {
      var endpoint20b = 'https://' + vtnConfiguration.host + ':' + vtnConfiguration.port + vtnConfiguration.contextPath + '/OpenADR2/Simple/2.0b';
      var endpoint20a = 'https://' + vtnConfiguration.host + ':' + vtnConfiguration.port + vtnConfiguration.contextPath + '/OpenADR2/Simple';
      view = [
          <VtnFeatureField className={ classes.textField }
                                     field="OADR 2.0b Endpoint"
                                     value={ endpoint20b } />
        ,
          <VtnFeatureField className={ classes.textField }
                                     field="OADR 2.0a Endpoint"
                                     value={ endpoint20a } />
      ]
      return view;

    }
  }

  var getFeatureField = function ( label, field ) {
    var view = null;
    var value = props.vtnConfiguration[ field ];
    if ( props.vtnConfiguration[ field ] != null ) {
      view = (
        <VtnFeatureField className={ classes.textField }
                                      field={ label }
                                      value={ value } />
      )
    }
    return view;
  }

  return (
  <div className={ classes.root }>
  <Grid container style={{margin:"0px 10%"}}>
  <FormControl component="fieldset" fullWidth>
      <FormLabel component="legend">Settings</FormLabel>
      <FormGroup aria-label="position" row>
  
            <Grid item xs={ 2 }>

                { getTextField( 'Identifiant VTN', 'vtnId' ) }
            </Grid>

            <Grid item xs={ 2 } >

            </Grid>

             <Grid item xs={ 2 }>

                { getTextField( 'Default Pull Frequency (seconds)', 'pullFrequencySeconds' ) }
            </Grid>

   
   </FormGroup>
   </FormControl>
   </Grid>
   <Divider/>
   <Grid container style={{margin:"0px 10%"}}>
                 <FormControl component="fieldset" fullWidth>
      <FormLabel component="legend">Features</FormLabel>
      <FormGroup aria-label="position" row>
            <Grid item xs={ 2 } >

                { getFeatureField( 'HTTPS Push feature', 'supportPush' ) }
            </Grid>
            <Grid item xs={ 1 } >

            </Grid>

             <Grid item xs={ 2 }>

                { getFeatureField( 'Unsecured HTTP Push feature', 'supportUnsecuredHttpPush' ) }
            </Grid>
 </FormGroup>
   </FormControl>
   </Grid>
    
    
    { getUrlTextField() }
  </div>

  
  );
};

export default VtnConfigurationParameter;
