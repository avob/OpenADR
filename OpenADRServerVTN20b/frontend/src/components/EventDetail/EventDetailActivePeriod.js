import React from 'react';



import TextField from '@material-ui/core/TextField';

import Grid from '@material-ui/core/Grid';

import {formatTimestamp} from '../../utils/time'
import FormGroup from '@material-ui/core/FormGroup';
import FormControl from '@material-ui/core/FormControl';
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

    return (
    <div className={ classes.root } >


      <div  style={{margin: "0px 5%"}}>
        <FormControl fullWidth >
      <FormGroup aria-label="position" row>
       <Grid container>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Start Datetime" 
            value={ startDatetime.date + " " +startDatetime.time + " " + startDatetime.tz } />
          </Grid>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Duration" value={ event.activePeriod.duration } />
          </Grid>
           
        </Grid>


   

         <Grid container>
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
        </Grid> 
        </FormGroup>
   </FormControl>
       </div>



       

        

    </div>
    );
  }
}

export default EventDetailActivePeriod;
