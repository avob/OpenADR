import React from 'react';
import TextField from '@material-ui/core/TextField';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

import {isoToTimestamp, formatTimestamp} from '../../utils/time'
import timezone from './timezone'

export var DatePicker = (props) => {
  const { classes } = props;
  var valDate = null;
  var now = new Date();
  var f;
  if(props.value != null) {
     f  = formatTimestamp(props.value); 
    valDate = f.date ;
  }
  else {
     f  = formatTimestamp(now.getTime()); 
    valDate = f.date ;
  }

  var onDateChange = (e) => {
    props.onChange(isoToTimestamp(e.target.value + "T00:00"));
  }


  return (
      <TextField required error={props.error}
          label={props.field}
          type="date"
          className={classes.textField}
          value={valDate}
          onChange={onDateChange}
          fullWidth={props.fullWidth != null && props.fullWidth }
          InputLabelProps={{
            shrink: true,
          }}
          InputProps={{style:{marginTop:24}}}
        />
  );
}

export var DateAndTimePicker = (props) => {
  const { classes } = props;
  var valDate = null;
  var valTime = null;
  var now = new Date();
  var f;
  if(props.value != null) {
     f  = formatTimestamp(props.value); 
    valDate = f.date ;
    valTime = f.time    
  }
  else {
     f  = formatTimestamp(now.getTime()); 
    valDate = f.date ;
    valTime = "00:00"  
  }

  var onDateChange = (e) => {
    var d  = new Date();
    d.setTime(isoToTimestamp(e.target.value + "T"+valTime));
    props.onChange(isoToTimestamp(e.target.value + "T"+valTime));
  }

  var onTimeChange = (e) => {
    console.log(e.target.value)
    props.onChange(isoToTimestamp(valDate + "T"+e.target.value));
  }

  return (
      <span>
        <TextField required error={props.error}
          label={props.field}
          type="date"
          className={classes.textField}
          value={valDate}
          onChange={onDateChange}
          InputLabelProps={{
            shrink: true,
          }}
        />
        <TextField required error={props.error}
          label={props.field}
          type="time"
          className={classes.textField}
          value={valTime}
          onChange={ onTimeChange}
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            step: 60, // 5 min
          }}
        />
      </span>
  );
}

export var DurationPicker = (props) => {
  const { classes } = props;
   var val = "";
  if(props.value != null) {
    val = props.value;
  }

  return (
      <TextField required error={props.error}
        label={props.field}
        type="number"
        className={classes.textField}
        fullWidth={true}
        value={val}
        onChange={(e) => {
          props.onChange(e.target.value);
        }}
        InputLabelProps={{
          shrink: true,
        }}
      />
  );
}

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


export var TimezonePicker = (props) => {
   var val = "";
  if(props.value != null) {
    val = props.value;
  }

  var timezoneView = []

    for (var i in timezone) {
      var tz = timezone[i];
      var offset = Math.abs(tz.offset);
      var isNegative = tz.offset < 0;
      var offsetMinute = offset * 60;
      var remainder = offsetMinute % 60;
      var hours = Math.floor(offsetMinute / 60);
      var offsetStr = "GMT";
      if(isNegative){
        offsetStr += "-";
      }
      else {
        offsetStr += "+";
      }
      if(hours < 10) {
        offsetStr += "0"
      }
      offsetStr += hours + ""
      offsetStr += ":"
      if(remainder < 10) {
        offsetStr += "0"
      }
      offsetStr += remainder;
      timezoneView.push( <MenuItem key={ tz.name } value={ tz.name }>
                                   { tz.name } ({offsetStr})
                                   </MenuItem> )
    }


  return (
       <FormControl style={{marginLeft:8}}>
          <FormLabel style={ labelStyle } component="label">
            {props.label}
          </FormLabel>
          <Select value={ val}
                        style={ { marginTop: 0 } }
                         onChange={(e) => {
                          props.onChange(e.target.value);
                        }}
                        
                        inputProps={ { name: 'timezone', id: 'timezone_select',  } }
                        >
                  { timezoneView }
          </Select>
        </FormControl>
  );
}