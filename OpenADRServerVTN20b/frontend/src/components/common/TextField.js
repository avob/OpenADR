import React from 'react';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import InputAdornment from '@material-ui/core/InputAdornment';
import Clear from '@material-ui/icons/Clear';
import Done from '@material-ui/icons/Done';

export var VtnTextField = ( props ) => {
  return (
  <TextField defaultValue={ props.value ? props.value : "" }
             className={ props.className }
             fullWidth
             helperText={ props.field ? props.field : "" }
             margin="normal"
             InputProps={ { readOnly: true } } />

  );
}

export var  VtnFeatureField = ( props ) => {
  var str = (props.value) ? 'Supported' : 'not supported'
  var ico = (props.value) ? <Done color="action" /> : <Clear color="action" />
  return (
  <TextField defaultValue={ str }
             className={ props.className }
             fullWidth
             helperText={ props.field }
             margin="normal"
             InputProps={ { readOnly: true, endAdornment: ( <InputAdornment> { ico } </InputAdornment> ), } } />

  );
}