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



export class MarketContextDetailSettings extends React.Component {
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
    const {classes, marketContext} = this.props;
    if(Object.keys(marketContext).length === 0) return null;
    
    var createdDatetime = formatTimestamp(marketContext.createdTimestamp);
    var lastUpdateDatetime = formatTimestamp(marketContext.lastUpdateTimestamp);
    var activePeriod = marketContext.activePeriod;
    var demandResponseEventScheduleStrategy = marketContext.demandResponseEventScheduleStrategy;
    var reportSubscriptionStrategy = marketContext.reportSubscriptionStrategy;



    return (
    <div className={ classes.root } >


          

          <Panel classes={classes}  title="Behavior">

          <Grid container style={{padding:'0 16px'}}>
            <Grid item xs={ 2 }>
              <VtnTextField className={ classes.textField } field="Oadr Profile" value={ marketContext.descriptor.oadrProfile } />
            </Grid>
            <Grid item xs={ 2 }>
                <VtnTextField className={ classes.textField } field="Response Required" value={ marketContext.descriptor.responseRequired } />
              </Grid>
            <Grid item xs={ 1 }>
                <VtnTextField className={ classes.textField } field="Has Test Event" value={ marketContext.descriptor.testEvent } />
              </Grid>
            <Grid item xs={ 1 }>
              <VtnTextField className={ classes.textField } field="Priority" value={ marketContext.descriptor.priority } />
            </Grid>
       
          </Grid>
       

       </Panel>
       

          

      
          <Panel classes={classes}  title="Report subscription stategy">

      


   

          <Grid container style={{padding:'0 16px'}}>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Preferred Granularity" value={ reportSubscriptionStrategy.preferredGranularity } />
          </Grid>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Preferred ReportBackDuration" value={ reportSubscriptionStrategy.preferredReportBackDuration } />
          </Grid>
      
        </Grid> 

       </Panel>

       
          <Panel classes={classes}  title="Event schedule stategy">




   

          <Grid container style={{padding:'0 16px'}}>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Scheduled CRON" value={ demandResponseEventScheduleStrategy.scheduledCronDate } />
          </Grid>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Scheduled CRON Timezone" value={ demandResponseEventScheduleStrategy.scheduledTimezone } />
          </Grid>
      
        </Grid> 
   

          <Grid container style={{padding:'0 16px'}}>
          <Grid item xs={ 12 }>
            <VtnTextField className={ classes.textField } field="Scheduled dates" value={ demandResponseEventScheduleStrategy.scheduledDate && demandResponseEventScheduleStrategy.scheduledDate.join(",") } />
          </Grid>

        </Grid> 

       </Panel>









     
    </div>
    );
  }
}

export default MarketContextDetailSettings;

