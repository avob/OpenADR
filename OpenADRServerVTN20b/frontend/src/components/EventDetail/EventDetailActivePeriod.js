import React from 'react';



import EventDetailHeader from './EventDetailHeader'
import Divider from '@material-ui/core/Divider';
import TextField from '@material-ui/core/TextField';

import Grid from '@material-ui/core/Grid';

import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';

import {formatTimestamp} from '../../utils/time'

var EventTextField = (props) => {
  var value = (props.value != null) ? props.value : "";
  return (
  <TextField label={ props.field }
             value={ value }
             className={ props.className }
             margin="normal"
             fullWidth={true}
             InputProps={ { readOnly: true, } } />

  );
}

export class EventDetailActivePeriod extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
    const {classes, event} = this.props;
        var startDatetime = formatTimestamp(event.activePeriod.start);
    var hasAvancedActivePeriod = false;
    if(event.activePeriod.notificationDuration != null
      || event.activePeriod.rampUpDuration != null
      || event.activePeriod.recoveryDuration != null
      || event.activePeriod.toleranceDuration != null) {
      hasAvancedActivePeriod = true;
    }
    return (
    <div className={ classes.root } >
      <EventDetailHeader classes={classes} event={event}/>
       <Divider style={ { marginTop: '20px' } } />

       <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Start Datetime" 
            value={ startDatetime.date + " " +startDatetime.time + " " + startDatetime.tz } />
          </Grid>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Duration" value={ event.activePeriod.duration } />
          </Grid>
           <Grid item xs={2} >
             <FormControlLabel
            control={
              <Checkbox
                checked={hasAvancedActivePeriod}
                value="advancedActivePeriod"
                color="primary"
              />
            }
            label="Advanced Active Period"
          />
        </Grid>
        </Grid>


   

        {(hasAvancedActivePeriod) ?  <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Notification Duration" value={ event.activePeriod.notificationDuration } />
          </Grid>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Ramp Up Duration" value={ event.activePeriod.rampUpDuration } />
          </Grid>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Recovery Duration" value={ event.activePeriod.recoveryDuration } />
          </Grid>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Tolerance Duration" value={ event.activePeriod.toleranceDuration } />
          </Grid>
        </Grid> : null}

        <Divider style={ { marginTop: '20px' } } />
        

    </div>
    );
  }
}

export default EventDetailActivePeriod;
