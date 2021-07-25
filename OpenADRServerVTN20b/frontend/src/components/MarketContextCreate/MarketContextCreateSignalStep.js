import React from 'react';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Avatar from '@material-ui/core/Avatar';
import {Panel} from '../common/Structure'
import {TimezonePicker, DateAndTimePicker, DurationPicker} from '../common/TimePicker'
import DetailedSignalEditTable from '../common/DetailedSignalEditTable'
import DetailedBaselineEditTable from '../common/DetailedBaselineEditTable'
import DetailedTargetEditTable from '../common/DetailedTargetEditTable'


export class MarketContextCreateSignalStep extends React.Component {


  onDurationChange = (duration) => {
    this.props.marketContext.activePeriod.duration = duration;
    this.props.onChange(this.props.marketContext);
  }

  onNotificationDurationChange = (duration) => {
    this.props.marketContext.activePeriod.notificationDuration = duration;
    this.props.onChange(this.props.marketContext);
  }

  onRecoveryDurationChange = (duration) => {
    this.props.marketContext.activePeriod.recoveryDuration = duration;
    this.props.onChange(this.props.marketContext);
  }

  onToleranceDurationChange = (duration) => {
    this.props.marketContext.activePeriod.toleranceDuration = duration;
    this.props.onChange(this.props.marketContext);
  }

  onRampUpDurationChange = (duration) => {
    this.props.marketContext.activePeriod.rampUpDuration = duration;
    this.props.onChange(this.props.marketContext);
  }

  onSignalChange = (signals) => {
    this.props.marketContext.signals = signals;
    this.props.onChange(this.props.marketContext);
  }

  onBaselineChange = (baseline) => {
    this.props.marketContext.baseline = baseline;
    this.props.onChange(this.props.marketContext);
  }

  onTargetChange = (targets) => {
    this.props.marketContext.targets = targets;
    this.props.onChange(this.props.marketContext);
  }



  render() {
    const {classes, marketContext, hasError, definition, filterUnit} = this.props;
    var activePeriod = marketContext.activePeriod || {};
    var signals = marketContext.signals;
    var baseline = marketContext.baseline;
    var targets = marketContext.targets;
    return (
      <React.Fragment>

         <Panel title="Active Period">
          <Grid container >
             <Grid item xs={ 2}>
               <DurationPicker classes={ classes } field="Duration" error={hasError && activePeriod.duration === ""}
                   value={activePeriod.duration} onChange={this.onDurationChange}/>
            </Grid>
            <Grid item xs={ 2 }>
              <DurationPicker classes={ classes } field="Notification" 
                   value={activePeriod.notificationDuration} onChange={this.onNotificationDurationChange}/>
            </Grid> 
            <Grid item xs={ 2 }>
               <DurationPicker classes={ classes } field="RampUp" 
                   value={activePeriod.rampUpDuration} onChange={this.onRampUpDurationChange}/>
            </Grid>
           <Grid item xs={ 2 }>
               <DurationPicker classes={ classes } field="Tolerance" 
                   value={activePeriod.toleranceDuration} onChange={this.onToleranceDurationChange}/>
            </Grid>
            <Grid item xs={ 2 }>
               <DurationPicker classes={ classes } field="Recovery" 
                   value={activePeriod.recoveryDuration} onChange={this.onRecoveryDurationChange}/>
            </Grid>
            
          </Grid> 
      </Panel>
      <Panel classes={classes}  title="Signals">
        <DetailedSignalEditTable classes={classes} signals={signals} 
          definition={definition}
          filterUnit={filterUnit}
          onSignalChange={this.onSignalChange}
          />
      </Panel>
      <Panel classes={classes}  title="Baseline">
        <DetailedBaselineEditTable classes={classes} baseline={baseline}
        definition={definition}
        filterUnit={filterUnit}
        onBaselineChange={this.onBaselineChange}/>
      </Panel>
      <Panel classes={classes}  title="Targets">
        <DetailedTargetEditTable classes={classes} targets={targets}
        onTargetChange={this.onTargetChange}
        group={this.props.group}
        ven={this.props.ven}
        onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
        onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
        onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}
        />
      </Panel>
      </React.Fragment>
    );
  }
}

export default MarketContextCreateSignalStep;
