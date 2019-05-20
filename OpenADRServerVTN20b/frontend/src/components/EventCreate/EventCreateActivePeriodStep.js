import React from 'react';











import Grid from '@material-ui/core/Grid';







import Divider from '@material-ui/core/Divider';












import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';




import {TimezonePicker, DateAndTimePicker, DurationPicker} from '../common/TimePicker'


export class EventCreateEventActivePeriodStep extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {};
    this.state.needAdvancedActivePeriod = false;
    if (props.activePeriod.notificationDuration !== ""
      ||props.activePeriod.rampUpDuration !== ""
      || props.activePeriod.toleranceDuration !== ""
      || props.activePeriod.recoveryDuration !== "") {
      this.state.needAdvancedActivePeriod = true;
    }

   
  }

  componentWillReceiveProps(nextProps) {
  var newState = {};
  var stateChanged = false;
  if (nextProps.activePeriod.notificationDuration !== ""
    || nextProps.activePeriod.rampUpDuration !== ""
    || nextProps.activePeriod.toleranceDuration !== ""
    || nextProps.activePeriod.recoveryDuration !== "") {
    newState.needAdvancedActivePeriod = true;
    stateChanged = true;
  }

  if(stateChanged){
    this.setState(newState);
  }
}

   handleTimezoneChange = (timezone) => {
    let signal = this.props.activePeriod;
    signal.timezone = timezone;
    this.props.onChange(signal);
  }

  handleStartChange = (start) => {
    let signal = this.props.activePeriod;
    signal.start = start;
    this.props.onChange(signal);
  }

   handleDurationChange = (duration) => {
    let signal = this.props.activePeriod;
    signal.duration = duration;
    this.props.onChange(signal);
  }


  handleNeedAdvancedActivePeriod = (e) => {
    if(!e.target.checked) {
      let activePeriod = this.props.activePeriod;
      activePeriod.notificationDuration = "";
      activePeriod.recoveryDuration = "";
      activePeriod.toleranceDuration = "";
      activePeriod.rampUpDuration = "";
      this.props.onChange(activePeriod);
    }
    this.setState({needAdvancedActivePeriod: e.target.checked});
  }

  handleNotificationDurationChange = (duration) => {
    let activePeriod = this.props.activePeriod;
    activePeriod.notificationDuration = duration;
    this.props.onChange(activePeriod);
  }

  handleRecoveryDurationChange = (duration) => {
    let activePeriod = this.props.activePeriod;
    activePeriod.recoveryDuration = duration;
    this.props.onChange(activePeriod);
  }

  handleToleranceDurationChange = (duration) => {
    let activePeriod = this.props.activePeriod;
    activePeriod.toleranceDuration = duration;
    this.props.onChange(activePeriod);
  }

  handleRampUpDurationChange = (duration) => {
    let activePeriod = this.props.activePeriod;
    activePeriod.rampUpDuration = duration;
    this.props.onChange(activePeriod);
  }

  


  render() {
    const {classes, hasError, activePeriod} = this.props;

    



    return (
    <Grid container
          spacing={ 8 }
          justify="center">

      <Grid container spacing={ 24 } >
        <Grid item xs={ 2 } />
        <Grid item xs={ 2 }>
          <TimezonePicker label="Timezone"  classes={classes} value={activePeriod.timezone} onChange={this.handleTimezoneChange}/>
        </Grid> 
        <Grid item xs={ 3 }>
           <DateAndTimePicker classes={ classes } field="Start Date" error={hasError && activePeriod.start == null}
             value={activePeriod.start} onChange={this.handleStartChange} />
        </Grid>
        <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="Duration (minutes)"  error={hasError && activePeriod.duration === ""}
               value={activePeriod.duration} onChange={this.handleDurationChange}/>
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
               value={activePeriod.notificationDuration} onChange={this.handleNotificationDurationChange}/>
        </Grid> 
        <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="RampUp Duration (minutes)" 
               value={activePeriod.rampUpDuration} onChange={this.handleRampUpDurationChange}/>
        </Grid>
         <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="Tolerance Duration (minutes)" 
               value={activePeriod.toleranceDuration} onChange={this.handleToleranceDurationchange}/>
        </Grid>
        <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="Recovery Duration (minutes)" 
               value={activePeriod.recoveryDuration} onChange={this.handleRecoveryDurationChange}/>
        </Grid>
        
        <Grid item xs={ 2 } />
      </Grid> : null}

      </Grid>
    );
  }
}

export default EventCreateEventActivePeriodStep;
