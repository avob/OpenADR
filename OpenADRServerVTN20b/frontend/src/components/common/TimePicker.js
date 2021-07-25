import React from 'react';
import TextField from '@material-ui/core/TextField';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

import {isoToTimestamp, formatTimestamp} from '../../utils/time'
import timezone from './timezone'
import HourglassEmptyIcon from '@material-ui/icons/HourglassEmpty';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import InputAdornment from '@material-ui/core/InputAdornment';

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

  var parseDuration = (str) => {
    var duration = "min";
    var number = str.match(/PT([0-9]+)M/);

    if(number != null ) {
      return {number:number[1], duration}
    }
    number = str.match(/PT([0-9]+)H/);
    if(number != null ) {
      duration = "hours";
      return {number:number[1], duration}
    }

    number = str.match(/P([0-9]+)D/);
    if(number != null ) {
      duration = "days";
      return {number:number[1], duration}
    }
    number = "";
    return {number, duration}
  }

  const obj = parseDuration(val);
  
  var d = 0
  const possibleDuration = [ "min", "hours", "days"];
  for(var i in possibleDuration){
    if(obj.duration === possibleDuration[i]) {
      d = i;
    }
 } 
  const [steps, setSteps] = React.useState(parseInt(d));

  var incrementStep = () => {
    var newStep;
    if(steps === possibleDuration.length - 1) {
      newStep = 0;
    } else {
      newStep = steps + 1;
    }
    setSteps(newStep);
    if(obj.number != "") {
      var duration = formatDuration(obj.number, possibleDuration[newStep])
      props.onChange(duration);
    }
    
  }

  var formatDuration = (number, duration) => {
    if(duration == "min") {
      return "PT"+number+"M";
    } else if(duration == "hours") {
      return "PT"+number+"H";
    } else if(duration == "days") {
      return "P"+number+"D";
    }
  }


 
  return (
      <React.Fragment>

      <TextField required error={props.error}
        label={props.field}
        type="number"
        className={classes.textField}
        value={obj.number}
        onChange={(e) => {
          var duration = formatDuration(e.target.value, possibleDuration[steps])

          props.onChange(duration);
        }}
        InputLabelProps={{
          shrink: true,
        }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start" style={{cursor: 'pointer'}} onClick={incrementStep}>
              <HourglassEmptyIcon />
            </InputAdornment>
          ),
          endAdornment: (<InputAdornment position="end" style={{cursor: 'pointer'}} 
                            onClick={incrementStep}>{possibleDuration[steps]}</InputAdornment>)
        }}
   
      />
      </React.Fragment>
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

    console.log(props.classes)
  return (
       <FormControl className={ props.classes.formControl } fullWidth={true}>
          <InputLabel shrink className={ props.classes.textField }>
            {props.label}
          </InputLabel>
          <Select value={ val}
                      className={ props.classes.textField }
                         onChange={(e) => {
                          props.onChange(e.target.value);
                        }}
                        inputProps={ { name: 'timezone', id: 'timezone_select', className:"MuiInputBase-input MuiInput-input"} }
                        >
                  { timezoneView }
          </Select>
        </FormControl>
  );
}