import React from 'react';


import Grid from '@material-ui/core/Grid';

import {formatTimestamp} from '../../utils/time'

import {  VtnTextField } from '../common/TextField'
import FormGroup from '@material-ui/core/FormGroup';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';


export class EventDetailDescriptor extends React.Component {
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


    return (
    <div className={ classes.root } >

         <div  style={{margin: "0px 5%"}}>

        <FormControl fullWidth >
        <FormLabel>Identitication</FormLabel>
      <FormGroup aria-label="position" row>
        <Grid container>
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Event ID" value={ event.id } />
          </Grid>
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Modification Number" value={ event.descriptor.modificationNumber } />
          </Grid>
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Oadr Profile" value={ event.descriptor.oadrProfile } />
          </Grid>
          <Grid item xs={ 1 }>
            <VtnTextField className={ classes.textField } field="Priority" value={ event.descriptor.priority } />
          </Grid>
           <Grid item xs={ 2 }>
            <VtnTextField className={ classes.textField } field="State" value={ event.descriptor.state } />
          </Grid>

        </Grid>
        <Grid container >
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Created Datetime" 
            value={ createdDatetime.date + " " +createdDatetime.time + " " + createdDatetime.tz } />
          </Grid>
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Last Update Datetime" 
            value={ lastUpdateDatetime.date + " " +lastUpdateDatetime.time + " " + lastUpdateDatetime.tz } />
          </Grid>
           <Grid item xs={ 3 }>
              <VtnTextField className={ classes.textField } field="Vtn Comment" value={ event.descriptor.vtnComment } />
            </Grid>
            <Grid item xs={ 1 }>
              <VtnTextField className={ classes.textField } field="Is Test Event" value={ event.descriptor.testEvent } />
            </Grid>
            <Grid item xs={ 2 }>
              <VtnTextField className={ classes.textField } field="Response Required" value={ event.descriptor.responseRequired } />
            </Grid>
        </Grid>
        </FormGroup>
   </FormControl>
       </div>








     
    </div>
    );
  }
}

export default EventDetailDescriptor;
