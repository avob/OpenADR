import React from 'react';

import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import InputAdornment from '@material-ui/core/InputAdornment';

import Clear from '@material-ui/icons/Clear';
import Done from '@material-ui/icons/Done';

import Divider from '@material-ui/core/Divider';




function VtnConfigurationTextField( props ) {
  return (
  <TextField label={ props.field }
             defaultValue={ props.value }
             className={ props.className }
             InputProps={ { readOnly: true, } } />

  );
}

function VtnConfigurationFeatureField( props ) {
  var str = (props.value) ? 'Supported' : 'not supported'
  var ico = (props.value) ? <Done color="action" /> : <Clear color="action" />
  return (
  <TextField label={ props.field }
             defaultValue={ str }
             className={ props.className }
             InputProps={ { readOnly: true, endAdornment: ( <InputAdornment> { ico } </InputAdornment> ), } } />

  );
}

const VtnConfigurationParameter = (props) => {
  const {classes, vtnConfiguration} = props;

  var getTextField = function ( label, field ) {
    var view = null;
    var value = props.vtnConfiguration[ field ];
    if ( props.vtnConfiguration[ field ] != null ) {
      view = (
        <VtnConfigurationTextField className={ classes.textField }
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
        <FormControl className={ classes.formControl } key="textfield_endpoint20b">
          <VtnConfigurationTextField className={ classes.textField }
                                     field="OADR 2.0b Endpoint"
                                     value={ endpoint20b } />
        </FormControl>,
        <FormControl className={ classes.formControl } key="textfield_endpoint20a">
          <VtnConfigurationTextField className={ classes.textField }
                                     field="OADR 2.0a Endpoint"
                                     value={ endpoint20a } />
        </FormControl>
      ]
      return view;

    }
  }

  var getFeatureField = function ( label, field ) {
    var view = null;
    var value = props.vtnConfiguration[ field ];
    if ( props.vtnConfiguration[ field ] != null ) {
      view = (
        <VtnConfigurationFeatureField className={ classes.textField }
                                      field={ label }
                                      value={ value } />
      )
    }
    return view;
  }

  return (
  <div className={ classes.root }>
    <FormControl className={ classes.formControl }>
      { getTextField( 'Identifiant VTN', 'vtnId' ) }
    </FormControl>
    <FormControl className={ classes.formControl }>
      { getTextField( 'Default Pull Frequency (seconds)', 'pullFrequencySeconds' ) }
    </FormControl>
    <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
    { getUrlTextField() }
    <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
    <FormControl className={ classes.formControl }>
      { getFeatureField( 'HTTPS Push feature', 'supportPush' ) }
    </FormControl>
    <FormControl className={ classes.formControl }>
      { getFeatureField( 'Unsecured HTTP Push feature', 'supportUnsecuredHttpPush' ) }
    </FormControl>
  </div>
  );
};

export default VtnConfigurationParameter;
