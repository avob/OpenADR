import React from 'react';










import Button from '@material-ui/core/Button';








import CloudDownloadIcon from '@material-ui/icons/CloudDownload';


import EventDetailHeader from './EventDetailHeader'
import Divider from '@material-ui/core/Divider';
import TextField from '@material-ui/core/TextField';

import Grid from '@material-ui/core/Grid';




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
      <EventDetailHeader classes={classes} event={event} actions={<Grid container spacing={ 24 }>

        {(!event.published) ? <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handlePublishEventClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> PUBLISH
            </Button>
          </Grid> : null}

        {(event.published && event.descriptor.state === "ACTIVE") ? <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="secondary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleCancelEventClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> CANCEL
            </Button>
          </Grid> : null}

        {(event.published && event.descriptor.state === "CANCELLED") ? <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleActiveEventClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> ACTIVE
            </Button>
          </Grid> : null}
          
      
      </Grid>}/>
       <Divider style={ { marginTop: '20px' } } />
       <Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Event ID" value={ event.id } />
          </Grid>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Modification Number" value={ event.descriptor.modificationNumber } />
          </Grid>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Oadr Profile" value={ event.descriptor.oadrProfile } />
          </Grid>
          <Grid item xs={ 1 }>
            <EventTextField className={ classes.textField } field="Priority" value={ event.descriptor.priority } />
          </Grid>
           <Grid item xs={ 2 }>
            <EventTextField className={ classes.textField } field="State" value={ event.descriptor.state } />
          </Grid>

        </Grid>

        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Created Datetime" 
            value={ createdDatetime.date + " " +createdDatetime.time + " " + createdDatetime.tz } />
          </Grid>
          <Grid item xs={ 3 }>
            <EventTextField className={ classes.textField } field="Last Update Datetime" 
            value={ lastUpdateDatetime.date + " " +lastUpdateDatetime.time + " " + lastUpdateDatetime.tz } />
          </Grid>
           <Grid item xs={ 3 }>
              <EventTextField className={ classes.textField } field="Vtn Comment" value={ event.descriptor.vtnComment } />
            </Grid>
            <Grid item xs={ 1 }>
              <EventTextField className={ classes.textField } field="Is Test Event" value={ event.descriptor.testEvent } />
            </Grid>
            <Grid item xs={ 2 }>
              <EventTextField className={ classes.textField } field="Response Required" value={ event.descriptor.responseRequired } />
            </Grid>
        </Grid>

        
        
     
      </Grid>
    </div>
    );
  }
}

export default EventDetailDescriptor;
