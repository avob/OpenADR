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
]

const signalType = [{
    name: "delta",
    label: "Delta",
    description: "Signal indicates the amount to change from what one would have used without the signal" 
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
    label: "oad Control Level Offset",
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

export class EventCreateEventSignalStep extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {};
    this.state.displayedSignalNameDescription = "";
  }

  handleSignalNameChange = (e) => {
    let signal = this.props.eventSignal;
    signal.signalName = e.target.value;
    this.props.onChange(signal);
    var description = "";
    for(var i in signalNameMenuItems){
      if(signalNameMenuItems[i].name == e.target.value){
        description = signalNameMenuItems[i].description;
        break;
      }
    }
    this.setState({displayedSignalNameDescription: description});
  }

  handleSignalTypeChange = (e) => {
    let signal = this.props.eventSignal;
    signal.signalType = e.target.value;
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

    return (
    <Grid container
          spacing={ 8 }
          justify="center">
      <Grid container spacing={ 24 }>
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
        <Grid item xs={ 6 }>
          {this.state.displayedSignalNameDescription}
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>

    </Grid>
    );
  }
}

export default EventCreateEventSignalStep;
