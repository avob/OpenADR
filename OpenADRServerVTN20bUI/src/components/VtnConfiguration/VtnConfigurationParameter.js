import React from 'react';

import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import InputAdornment from '@material-ui/core/InputAdornment';

import Clear from '@material-ui/icons/Clear';
import Done from '@material-ui/icons/Done';
import red from '@material-ui/core/colors/red';
import green from '@material-ui/core/colors/green';




function VtnConfigurationTextField(props) {
  return (
    <TextField
          label={props.field}
          defaultValue={props.value}
          className={props.className}
          margin="normal"
          variant="outlined"
          InputProps={{
            readOnly: true,
          }}
        />

  );
}

function VtnConfigurationFeatureField(props) {
  var str = (props.value) ? "Supported" : "not supported"
  var ico = (props.value) ? <Done color="action"/> : <Clear color="action"/>
  return (
    <TextField
          label={props.field}
          defaultValue={str}
          className={props.className}
          margin="normal"
          variant="outlined"
          InputProps={{
            readOnly: true,
             endAdornment: (
                <InputAdornment >
                  {ico}
                </InputAdornment>
              ),
          }}
        />

  );
}

const VtnConfigurationParameter = (props) => {
	const { classes, vtnConfiguration } = props;

  var getTextField = function(label, field){
    var view = null;
    var value = props.vtnConfiguration[field];
    if(props.vtnConfiguration[field] != null){
      view = (
        <VtnConfigurationTextField  className={classes.textField} field={label} value={value}/>
      )
    }
    return view;
  }

  var getUrlTextField = function(){
    var view = null;
    if(vtnConfiguration.contextPath != null
       && vtnConfiguration.port != null
       && vtnConfiguration.host != null){
      var endpoint20b = "https://"+vtnConfiguration.host+":"+vtnConfiguration.port+vtnConfiguration.contextPath + "/OpenADR2/Simple/2.0b";
      var endpoint20a = "https://"+vtnConfiguration.host+":"+vtnConfiguration.port+vtnConfiguration.contextPath + "/OpenADR2/Simple";
      view = [
        <VtnConfigurationTextField key="textfield_endpoint20b" className={classes.textField} field="OADR 2.0b Endpoint" value={endpoint20b}/>,
        <VtnConfigurationTextField key="textfield_endpoint20a" className={classes.textField} field="OADR 2.0a Endpoint" value={endpoint20a}/>
      ]
      return view;
      
    }
  }

  var getFeatureField = function(label, field) {
    var view = null;
    var value = props.vtnConfiguration[field];
    if(props.vtnConfiguration[field] != null){
      view = (
        <VtnConfigurationFeatureField  className={classes.textField} field={label} value={value}/>
      )
    }
    return view;
  }

  return (

     <form className={classes.container} noValidate autoComplete="off">
        <FormControl className={classes.formControl}>
            {  getTextField("Identifiant VTN", "vtnId")}
            {  //getTextField("Profile OpenADR", "oadrVersion")
            }
            {  getUrlTextField() }
            {  getFeatureField("HTTPS Push feature", "supportPush")}
            {  getFeatureField("Unsecured HTTP Push feature", "supportUnsecuredHttpPush")}
            {  getTextField("Default Pull Frequency (seconds)", "pullFrequencySeconds")}

        </FormControl>
      </form>
  );
};

export default VtnConfigurationParameter;
