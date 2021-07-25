import React from 'react';


import Grid from '@material-ui/core/Grid';

import {formatTimestamp} from '../../utils/time'

import {  VtnTextField } from '../common/TextField'
import FormGroup from '@material-ui/core/FormGroup';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import TextField from '@material-ui/core/TextField';
import Divider from '@material-ui/core/Divider';

import {Panel} from '../common/Structure'



export class EventDetailSettings extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  handlePublishEventClick = () => {
    this.props.publishEvent(this.props.event.id);
  }

  handleActiveEventClick = () => {
    this.props.activeEvent(this.props.event.id);
  }
 
  handleCancelEventClick = () => {
    this.props.cancelEvent(this.props.event.id)
  }

  render() {
    const {classes, event} = this.props;
    if(Object.keys(event).length === 0) return null;
    
    var createdDatetime = formatTimestamp(event.createdTimestamp);
    var lastUpdateDatetime = formatTimestamp(event.lastUpdateTimestamp);
    var activePeriod = event.activePeriod;
    var descriptor = event.descriptor;




    return (
    <div className={ classes.root } >


          

          <Panel classes={classes}  title="Identitication">

          <Grid container style={{padding:'0 16px'}}>
            <Grid item xs={ 2 }>
              <VtnTextField className={ classes.textField } field="Oadr Profile" value={ descriptor.oadrProfile } />
            </Grid>
            <Grid item xs={ 2 }>
                <VtnTextField className={ classes.textField } field="Response Required" value={ descriptor.responseRequired } />
              </Grid>
            <Grid item xs={ 1 }>
                <VtnTextField className={ classes.textField } field="Has Test Event" value={ descriptor.testEvent } />
              </Grid>
            <Grid item xs={ 1 }>
              <VtnTextField className={ classes.textField } field="Priority" value={ descriptor.priority } />
            </Grid>
       
          </Grid>
       

       </Panel>
       

          <Panel classes={classes}  title="Active Period">

       <Grid container>
     
          
           
        </Grid>


   

          <Grid container style={{padding:'0 16px'}}>
          <Grid item xs={ 2 }>
            <VtnTextField className={ classes.textField } field="Duration" value={ activePeriod.duration } />
          </Grid>
          <Grid item xs={ 2 }>
            <VtnTextField className={ classes.textField } field="Notification Duration" value={ activePeriod.notificationDuration } />
          </Grid>
          <Grid item xs={ 2 }>
            <VtnTextField className={ classes.textField } field="Ramp Up Duration" value={ activePeriod.rampUpDuration } />
          </Grid>
          <Grid item xs={ 2 }>
            <VtnTextField className={ classes.textField } field="Recovery Duration" value={ activePeriod.recoveryDuration } />
          </Grid>
          <Grid item xs={ 2 }>
            <VtnTextField className={ classes.textField } field="Tolerance Duration" value={ activePeriod.toleranceDuration } />
          </Grid>
        </Grid> 

       </Panel>
     
    </div>
    );
  }
}

export default EventDetailSettings;

