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

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import AddIcon from '@material-ui/icons/Add';

import Button from '@material-ui/core/Button';

import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';




import {TimezonePicker, DateAndTimePicker, DurationPicker} from '../common/TimePicker'



const signalTypeMenuItems = [{
    name: "delta",
    label: "Delta",
    description: "Signal indicates the amount to change from what one would have used without the signal",
  }, {
    name: "level",
    label: "Level",
    description: "Signal indicates a program level" 
  }, {
    name: "multiplier",
    label: "Multiplier",
    description: "Signal indicates a multiplier applied to the current rate of delivery or usage from what one would have used without the signal" 
  } , {
    name: "price",
    label: "Price",
    description: "Signal indicates the price" 
  } , {
    name: "priceMultiplier",
    label: "Price Multiplier",
    description: "Signal indicates the price multiplier. Extended price is the computed price value multiplied by the number of units" 
  } , {
    name: "priceRelative",
    label: "Price Relative",
    description: "Signal indicates the relative price" 
  } , {
    name: "setpoint",
    label: "Set Point",
    description: "Signal indicates a target amount of units" 
  } , {
    name: "x-loadControlCapacity",
    label: "Load Control Capacity",
    description: "This is an instruction for the load controller to operate at a level that is some percentage of its maximum load consumption capacity. This can be mapped to specific load controllers to do things like duty cycling. Note that 1.0 refers to 100% consumption. In the case of simple ON/OFF type devices then 0 = OFF and 1 = ON" 
  } , {
    name: "x-loadControlLevelOffset",
    label: "Load Control Level Offset",
    description: "Discrete integer levels that are relative to normal operations where 0 is normal operations" 
  }  , {
    name: "x-loadControlPercentOffset",
    label: "Load Control Percent Offset",
    description: "Percentage change from normal load control operations" 
  } , {
    name: "x-loadControlSetPoint",
    label: "Load Control Set Point",
    description: "Load controller set points" 
  } 
]

const signalNameMenuItems = [{
    name: "BID_ENERGY",
    label: "Bid Energy",
    description: "This is the amount of energy from a resource that was bid into a program" 
  },{
    name: "BID_LOAD",
    label: "Bid Load",
    description: "This is the amount of load that was bid by a resource into a program" 
  }, {
    name: "BID_PRICE",
    label: "Bid Price",
    description: "This is the price that was bid by the resource"

  }, {
    name: "CHARGE_STATE",
    label: "Charge State",
    description: "State of energy storage resource"

  }, {
    name: "DEMAND_CHARGE",
    label: "Demand Charge",
    description: "This is the demand charge"

  }, {
    name: "ELECTRICITY_PRICE",
    label: "Electricity Price",
    description: "This is the cost of electricity"

  }, {
    name: "ENERGY_PRICE",
    label: "Energy Price",
    description: "This is the cost of energy"

  }, {
    name: "LOAD_CONTROL",
    label: "Load Control",
    description: "Set load output to relative values"

  }, {
    name: "LOAD_DISPATCH",
    label: "Load Dispatch",
    description: "This is used to dispatch load"

  }, {
    name: "SIMPLE",
    label: "Simple",
    description: "Simple levels (OpenADR 2.0a compliant) "

  }, {
    name: "SIMPLE_DEPRECATED",
    label: "Simple (deprecated)",
    description: "depreciated - for backwards compatibility with A profile"

  }
];



const unitMenuItems = {

  currencyPerKwh: [{name: "euro_per_kwh",
    label: "€ / kWh",
    description: "Euro per kWh"
  }, {
    name: "dollar_per_kwh",
    label: "$ / kWh",
    description: "Euro per kWh"
  }],
  energy: [{
    name: "kwh",
    label: "kWh",
    description: "kWh"
  }],
  power: [{
    name: "kw",
    label: "kW",
    description: "kW"
  }],
  none:[{
    name: "none",
    label: "None",
    description: "None"
  }]
}

const validSignalCombination = [{
  signalName: "SIMPLE",
  signalType: "level",
  unit: unitMenuItems.none
}, {
  signalName: "ELECTRICITY_PRICE",
  signalType: "price",
  unit: unitMenuItems.currencyPerKwh
} , {
  signalName: "ELECTRICITY_PRICE",
  signalType: "priceRelative",
  unit: unitMenuItems.currencyPerKwh
} , {
  signalName: "ELECTRICITY_PRICE",
  signalType: "priceMultiplier",
  unit: unitMenuItems.none
}, {
  signalName: "ENERGY_PRICE",
  signalType: "priceRelative",
  unit: unitMenuItems.currencyPerKwh
}, {
  signalName: "ENERGY_PRICE",
  signalType: "priceMultiplier",
  unit: unitMenuItems.none
}, {
  signalName: "ENERGY_PRICE",
  signalType: "price",
  unit: unitMenuItems.currencyPerKwh
}, {
  signalName: "DEMAND_CHARGE",
  signalType: "priceRelative",
  unit: unitMenuItems.currencyPerKwh
}, {
  signalName: "DEMAND_CHARGE",
  signalType: "priceMultiplier",
  unit: unitMenuItems.none
}, {
  signalName: "DEMAND_CHARGE",
  signalType: "price",
  unit: unitMenuItems.currencyPerKwh
}, {
  signalName: "BID_PRICE",
  signalType: "price",
  unit: unitMenuItems.currencyPerKwh
}, {
  signalName: "BID_LOAD",
  signalType: "setpoint",
  unit: unitMenuItems.power
}, {
  signalName: "BID_ENERGY",
  signalType: "setpoint",
  unit: unitMenuItems.energy
}, {
  signalName: "CHARGE_STATE",
  signalType: "setpoint",
  unit: unitMenuItems.energy
}, {
  signalName: "CHARGE_STATE",
  signalType: "delta",
  unit: unitMenuItems.energy
}, {
  signalName: "CHARGE_STATE",
  signalType: "multiplier",
  unit: unitMenuItems.none
}, {
  signalName: "LOAD_DISPATCH",
  signalType: "setpoint",
  unit: unitMenuItems.power
}, {
  signalName: "LOAD_DISPATCH",
  signalType: "delta",
  unit: unitMenuItems.power
}, {
  signalName: "LOAD_DISPATCH",
  signalType: "multiplier",
  unit: unitMenuItems.none
}, {
  signalName: "LOAD_DISPATCH",
  signalType: "level",
  unit: unitMenuItems.power
}, {
  signalName: "LOAD_CONTROL",
  signalType: "x-loadControlCapacity",
  unit: unitMenuItems.none
}, {
  signalName: "LOAD_CONTROL",
  signalType: "x-loadControlPercentOffset",
  unit: unitMenuItems.none
}, {
  signalName: "LOAD_CONTROL",
  signalType: "x-loadControlSetPoint",
  unit: unitMenuItems.none
}, {
  signalName: "LOAD_CONTROL",
  signalType: "x-loadControlLevelOffset",
  unit: unitMenuItems.none
}];

var getAvailableSignalType = (signalName) => {
  var validSignalType = [];
  for(var i in validSignalCombination) { 
    var combination = validSignalCombination[i];
    if(combination.signalName == signalName){
      validSignalType.push(combination.signalType)
    }
  }
  return validSignalType;
}

var getAvailableUnitType = (signalName, signalType) => {
  var validUnitType = [];
  for(var i in validSignalCombination) { 
    var combination = validSignalCombination[i];
    if(combination.signalName == signalName && combination.signalType == signalType){
      validUnitType = validUnitType.concat(combination.unit);
      
    }
  }
  return validUnitType;
}




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
  marginTop: "-8px"
}

var SignalIntervalTable = (props) => {
  const {classes} = props;
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Duration</TableCell>
            <TableCell align="right">Value</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.signalInterval.map( (row, index) => (
            <TableRow key={index}>
              <TableCell scope="row" align="right">{row.duration}</TableCell>
              <TableCell scope="row" align="right">{row.value}</TableCell>
              <TableCell scope="row" align="right">
                  <Button size="small" color="secondary" onClick={props.handleRemoveSignalIntervalAtIndex(index)}> REMOVE </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}

export class EventCreateEventSignalStep extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {};
    this.state.createIntervalDuration = "";
    this.state.createIntervalValue = "";
    this.state.needIntervalCreate = false;
    this.state.needAdvancedActivePeriod = false;
    if (props.eventSignal.notificationDuration !== ""
      ||props.eventSignal.rampUpDuration !== ""
      || props.eventSignal.toleranceDuration !== ""
      || props.eventSignal.recoveryDuration !== "") {
      this.state.needAdvancedActivePeriod = true;
    }

    if(props.eventSignal.signalInterval.length > 0) {
      this.state.needIntervalCreate = true;
    }
  }

  componentWillReceiveProps(nextProps) {
  var newState = {};
  var stateChanged = false;
  if (nextProps.eventSignal.notificationDuration !== ""
    || nextProps.eventSignal.rampUpDuration !== ""
    || nextProps.eventSignal.toleranceDuration !== ""
    || nextProps.eventSignal.recoveryDuration !== "") {
    newState.needAdvancedActivePeriod = true;
    stateChanged = true;
  }

  if(nextProps.eventSignal.signalinterval.length > 0) {
    newState.needIntervalCreate = true;
    stateChanged = true;
  }

  if(stateChanged){
    this.setState(newState);
  }
}

   handleTimezoneChange = (timezone) => {
    let signal = this.props.eventSignal;
    signal.timezone = timezone;
    this.props.onChange(signal);
  }

  handleStartChange = (start) => {
    let signal = this.props.eventSignal;
    signal.start = start;
    this.props.onChange(signal);
  }

   handleDurationChange = (duration) => {
    let signal = this.props.eventSignal;
    signal.duration = duration;
    this.props.onChange(signal);
  }

  handleSignalNameChange = (e) => {
    let signal = this.props.eventSignal;
    signal.signalName = e.target.value;
    

    var availableSignalType = getAvailableSignalType(signal.signalName);
    if(availableSignalType.length == 1) {
      signal.signalType = availableSignalType[0];
      var availableUnitType = getAvailableUnitType(signal.signalName, signal.signalType);
      if(availableUnitType.length == 1) {
        signal.unitType = availableUnitType[0].name;
      }
    }

    this.props.onChange(signal);
  }

  handleSignalTypeChange = (e) => {
    let signal = this.props.eventSignal;
    signal.signalType = e.target.value;
    this.props.onChange(signal);
  }

  handleUnitTypeChange = (e) => {
    let signal = this.props.eventSignal;
    signal.unitType = e.target.value;
    this.props.onChange(signal);
  }

  handleRemoveSignalIntervalAtIndex = (index) => () => {
    let signal = this.props.eventSignal;
    signal.signalInterval.splice(index,1);
    this.props.onChange(signal);
  }

  handleCreateIntervalDurationChange = (e) => {
    this.setState({createIntervalDuration: e.target.value});
  }

  handleCreateIntervalValueChange = (e) => {
    this.setState({createIntervalValue: e.target.value});
  }

  handleCurrentValueChange = (e) => {
    let signal = this.props.eventSignal;
    signal.currentValue = e.target.value;
    this.props.onChange(signal);
  }
  
  handleCreateIntervalClick = () => {
    let signal = this.props.eventSignal;
    signal.signalInterval.push({
      duration: this.state.createIntervalDuration,
      value: this.state.createIntervalValue
    });
    this.props.onChange(signal);
    this.setState({
      createIntervalDuration: "",
      createIntervalValue: ""
    })
  }

  handleNeedIntervalCreateClick = (e) => {
    this.setState({needIntervalCreate: e.target.checked});
  }

  handleNeedAdvancedActivePeriod = (e) => {
    this.setState({needAdvancedActivePeriod: e.target.checked});
  }

  handleNotificationDurationChange = () => {
    let signal = this.props.eventSignal;
    signal.notificationDuration = duration;
    this.props.onChange(signal);
  }

  handleRecoveryDurationChange = () => {
    let signal = this.props.eventSignal;
    signal.recoveryDuration = duration;
    this.props.onChange(signal);
  }

  handleToleranceDurationChange = () => {
    let signal = this.props.eventSignal;
    signal.toleranceDuration = duration;
    this.props.onChange(signal);
  }

  handleRampUpDurationChange = () => {
    let signal = this.props.eventSignal;
    signal.rampUpDuration = duration;
    this.props.onChange(signal);
  }

  


  render() {
    const {classes, hasError, eventSignal} = this.props;

    var signalNameView = [];
    for(var i in signalNameMenuItems){
      var s  = signalNameMenuItems[i];
      signalNameView.push(<MenuItem key={"menu_item_signal_name_"+s.name} value={s.name}>
                       {s.label}
                       </MenuItem> )
    }

    var signalTypeView = [];
    if(eventSignal.signalName != "") {
      var validSignalType = getAvailableSignalType(eventSignal.signalName);
      for(var i in signalTypeMenuItems){
        var s  = signalTypeMenuItems[i];
        if(validSignalType.includes(s.name) ){
          signalTypeView.push(<MenuItem key={"menu_item_signal_name_"+s.name} value={s.name}>
                         {s.label}
                         </MenuItem> );
        }
        
      }
    }

    var unitTypeView = [];
     if(eventSignal.signalType != "") {
      var validUnitType = getAvailableUnitType(eventSignal.signalName, eventSignal.signalType);
      for(var i in validUnitType){
        var s = validUnitType[i];
        unitTypeView.push(<MenuItem key={"menu_item_unit_type_"+s.name} value={s.name}>
             {s.label}
             </MenuItem> );
        
      }
    }

    
    



    return (
    <Grid container
          spacing={ 8 }
          justify="center">

      <Grid container spacing={ 24 } >
        <Grid item xs={ 2 } />
        <Grid item xs={ 2 }>
          <TimezonePicker label="Timezone"  classes={classes} value={eventSignal.timezone} onChange={this.handleTimezoneChange}/>
        </Grid> 
        <Grid item xs={ 3 }>
           <DateAndTimePicker classes={ classes } field="Start Date" error={hasError && eventSignal.start == null}
             value={eventSignal.start} onChange={this.handleStartChange} />
        </Grid>
        <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="Duration (minutes)"  error={hasError && eventSignal.duration == ""}
               value={eventSignal.duration} onChange={this.handleDurationChange}/>
        </Grid>
        <Grid item xs={1} >
           <FormControlLabel
          control={
            <Checkbox
              checked={this.state.needAdvancedActivePeriod}
              onChange={this.handleNeedAdvancedActivePeriod}
              value="Intervals"
              color="primary"
            />
          }
          label="Advanced Period"
        />
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>

      {(this.state.needAdvancedActivePeriod) ? <Grid container
              style={ { marginTop: 20 } }
              spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 8 }>
            <Divider />
          </Grid>
          <Grid item xs={ 2 } />
        </Grid>: null}

      {(this.state.needAdvancedActivePeriod) ? <Grid container 
          style={ { marginTop: 20 } }
          spacing={ 24 } >
        <Grid item xs={ 2 } />
        <Grid item xs={ 2 }>
          <DurationPicker classes={ classes } field="Notification Duration (minutes)" 
               value={eventSignal.notificationDuration} onChange={this.handleNotificationDurationChange}/>
        </Grid> 
        <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="RampUp Duration (minutes)" 
               value={eventSignal.rampUpDuration} onChange={this.handleRampUpDurationChange}/>
        </Grid>
         <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="Tolerance Duration (minutes)" 
               value={eventSignal.toleranceDuration} onChange={this.handleToleranceDurationchange}/>
        </Grid>
        <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="Recovery Duration (minutes)" 
               value={eventSignal.recoveryDuration} onChange={this.handleRecoveryDurationChange}/>
        </Grid>
        
        <Grid item xs={ 2 } />
      </Grid> : null}

       <Grid container
            style={ { marginTop: 20 } }
            spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
          <Divider />
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>


      <Grid container spacing={ 24 }
         style={ { marginTop: 20, marginBottom: (!this.state.needIntervalCreate) ? 10 : 0 } }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 2 }>
          <FormControl className={ classes.formControl }>
                <FormLabel style={ labelStyle } component="label">
                  Signal Name
                </FormLabel>
                <Select required value={ eventSignal.signalName}
                              style={ { marginTop: 0 } }

                               onChange={this.handleSignalNameChange}
                   
                              inputProps={ { name: 'signal_name_select', id: 'signal_name_select', } }
                              >
                      {signalNameView}
                </Select>
              </FormControl>

        </Grid> 
        <Grid item xs={ 2 }>
          <FormControl className={ classes.formControl }>
                <FormLabel style={ labelStyle } component="label">
                  Signal Name
                </FormLabel>
                <Select required disabled={eventSignal.signalName == ""} value={ eventSignal.signalType}
                              style={ { marginTop: 0 } }

                               onChange={this.handleSignalTypeChange}
                   
                              inputProps={ { name: 'signal_type_select', id: 'signal_type_select', } }
                              >
                      {signalTypeView}
                </Select>
              </FormControl>

        </Grid> 
        <Grid item xs={ 2 }>
          <FormControl className={ classes.formControl }>
                <FormLabel style={ labelStyle } component="label">
                  Signal Unit
                </FormLabel>
                <Select required disabled={eventSignal.signalName == "" || eventSignal.signalType == ""} value={ eventSignal.unitType}
                              style={ { marginTop: 0 } }

                               onChange={this.handleUnitTypeChange}
                   
                              inputProps={ { name: 'unit_type_select', id: 'unit_type_select', } }
                              >
                      {unitTypeView}
                </Select>
              </FormControl>

        </Grid>

        <Grid item xs={ 1 }>
            <TextField label="Current Value"
                       placeholder="Current Value"
                       value={ eventSignal.currentValue }
                       className={ classes.textField }
                       onChange={ this.handleCurrentValueChange }
                       fullWidth={ true } />
          </Grid> 
        <Grid item xs={1} >
           <FormControlLabel
          control={
            <Checkbox
              checked={this.state.needIntervalCreate}
              onChange={this.handleNeedIntervalCreateClick}
              value="Intervals"
              color="primary"
            />
          }
          label="Intervals"
        />
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>





      {(this.state.needIntervalCreate) ? <Grid container
            style={ { marginTop: 20 } }
            spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
          <Divider />
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>: null}

       

      <Grid container spacing={ 24 }
         style={ { marginTop: (this.state.needIntervalCreate) ? 20 : 0} }>
        <Grid item xs={ 2 } />
        
        
          {(this.state.needIntervalCreate) ? <Grid item xs={ 2 }>
            <TextField label="Interval Duration (min)" required
                        type="number"
                       placeholder="Duration"
                       value={ this.state.createIntervalDuration }
                       className={ classes.textField }
                       onChange={ this.handleCreateIntervalDurationChange }
                       InputLabelProps={{ shrink: true }}
                       fullWidth={ true }/>
          </Grid> : null}

          {(this.state.needIntervalCreate) ? <Grid item xs={ 5 }>
            <TextField label="Interval Value" required
                       placeholder="Value"
                       value={ this.state.createIntervalValue }
                       className={ classes.textField }
                       onChange={ this.handleCreateIntervalValueChange }
                       InputLabelProps={{ shrink: true }}
                       fullWidth={ true } />
          </Grid> : null}
          {(this.state.needIntervalCreate && this.state.createIntervalValue != "" && this.state.createIntervalDuration != "") ? <Grid item xs={ 1 }>
            <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            style={ { marginTop: 13 } }
                            onClick={ this.handleCreateIntervalClick }>
                      <AddIcon />Create
                    </Button>
          </Grid>: null}
          
          
        <Grid item xs={ 2 } />
      </Grid>
      {(this.state.needIntervalCreate) ? <Grid container spacing={ 24 }
         style={ { marginTop: 20 , marginBottom:10 } }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
          <SignalIntervalTable classes={classes} signalInterval={eventSignal.signalInterval} 
          handleRemoveSignalIntervalAtIndex={this.handleRemoveSignalIntervalAtIndex}/>
        </Grid> 
        <Grid item xs={ 2 } />
      </Grid>: null}
      

    </Grid>
    );
  }
}

export default EventCreateEventSignalStep;
