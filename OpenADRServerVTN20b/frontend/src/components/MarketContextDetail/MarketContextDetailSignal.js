import React from 'react';
import Divider from '@material-ui/core/Divider';
import Grid from '@material-ui/core/Grid';
import {EventSignalPanel} from '../common/EventSignalPanel'
import DetailedSignalTable from '../common/DetailedSignalTable'
import DetailedBaselineTable from '../common/DetailedBaselineTable'
import DetailedTargetTable from '../common/DetailedTargetTable'
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';

import {Panel} from '../common/Structure'

import {  VtnTextField } from '../common/TextField'
export class MarketContextDetailSignal extends React.Component {




  handleAddSignalClick = () => {
    this.props.addCopySignals();
  }

  render() {
    const {classes, marketContext} = this.props;
    var that = this;
    var hasError = false;
    var signals = marketContext.signals;
    var baseline = marketContext.baseline;
    var targets = marketContext.targets;
    var activePeriod = marketContext.activePeriod;

    return (
    <div className={ classes.root } >
         
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
          <Panel classes={classes}  title="Signals">
            <DetailedSignalTable classes={classes} signal={signals}/>
          </Panel>
          <Panel classes={classes}  title="Baseline">
            <DetailedBaselineTable classes={classes} baseline={baseline}/>
          </Panel>
          <Panel classes={classes}  title="Targets">
            <DetailedTargetTable classes={classes} targets={targets}/>
          </Panel>
         


    </div>
    );
  }
}

export default MarketContextDetailSignal;
