import React from 'react';


import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';

import {timestampToISO, isoToTimestamp} from '../../utils/time'
import {isActionReport, isHistoryReport, isTelemetryReport} from '../../utils/venReport'






var VenReportTextField = (props) => {
  var value = (props.value != null) ? props.value : "";
  return (
  <TextField label={ props.field }
             value={ value }
             className={ props.className }
             margin="normal"
             fullWidth={true}
             InputProps={ { shrink: "true", readOnly: true, } } />

  );
}

var DateAndTimePicker = (props) => {
  const { classes } = props;
  var val = "";
  if(props.value != null) {
    val = timestampToISO(props.value);
  }

  return (
    <form className={classes.container} noValidate>
      <TextField required
        label={props.field}
        type="datetime-local"
        defaultValue={props.default}
        className={classes.textField}
        fullWidth={true}
        value={val}
        onChange={(e) => {
          props.onChange(isoToTimestamp(e.target.value));
        }}
        InputLabelProps={{
          shrink: true,
        }}
      />
    </form>
  );
}

var DurationPicker = (props) => {
  const { classes } = props;
   var val = "";
  if(props.value != null) {
    val = props.value;
  }

  return (
    <form className={classes.container} noValidate>
      <TextField required error={props.error}
        label={props.field}
        type="number"
        className={classes.textField}
        fullWidth={true}
        value={val}
        onChange={(e) => {
          props.onChange(e.target.value);
        }}
        InputLabelProps={{
          shrink: true,
        }}
      />
    </form>
  );
}

export function VenAvailableReportHeader (props) {
  const { classes, availableReport} = props;
  var startView = null;
  var durationView = null;
  if(isHistoryReport(availableReport)){
    if(availableReport.start) {
     
      startView = <Grid item xs={ 3 }>
        <VenReportTextField className={ classes.textField } field="Start Date" value={ availableReport.start } />
      </Grid>
    }
    if(availableReport.duration) {
      durationView = <Grid item xs={ 3 }>
        <VenReportTextField className={ classes.textField } field="Duration" value={ availableReport.duration } />
      </Grid>
    }
  }
    
  return (
    <div>
      <Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <VenReportTextField className={ classes.textField } field="Ei Report ID" value={ availableReport.reportId } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenReportTextField className={ classes.textField } field="Report Name" value={ availableReport.reportName } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenReportTextField className={ classes.textField } field="Report Specifier ID" value={ availableReport.reportSpecifierId } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenReportTextField className={ classes.textField } field="Created Datetime" value={ availableReport.createdDatetime } />
          </Grid>
        </Grid>

        {isHistoryReport(availableReport) ? ( <Grid container spacing={ 24 }>
          {startView} 
          {durationView}
        </Grid>) : null}

      </Grid>
    </div>
  );
}

export function VenAvailableReportParamsHeader (props) {
  const { classes, availableReport} = props;
  var startView = null;
  var durationView = null;
  var granularityView = null;
  var reportBackDurationView = null;
  if(isHistoryReport(availableReport)){
    var startError = (props.hasError && props.start == null);
    var durationError = (props.hasError && props.duration == null);
    startView = <Grid item xs={ 3 }>
        <DateAndTimePicker error={startError} classes={ classes } field="Start Date" 
        value={props.start} onChange={props.onStartChange} />
      </Grid>
    durationView = <Grid item xs={ 3 }>
        <DurationPicker error={durationError} classes={ classes } field="Duration (minutes)" 
        value={props.duration} onChange={props.onDurationChange}/>
      </Grid>
  }

  if(isTelemetryReport(availableReport)){

    var granularityError = (props.hasError && props.granularity == null);
    var reportBackDurationError = (props.hasError && props.reportBackDuration == null);

    granularityView = <Grid item xs={ 3 }>
        <DurationPicker error={granularityError} classes={ classes } field="Granularity Duration (minutes)" 
        value={props.granularity} onChange={props.onGranularityChange}/>
      </Grid>
    reportBackDurationView = <Grid item xs={ 3 }>
        <DurationPicker error={reportBackDurationError} classes={ classes } field="Report Back Duration (minutes)" 
        value={props.reportBackDuration} onChange={props.onReportBackDurationChange}/>
      </Grid>
  }
    
  return (
    <div>
      <Grid>
        {isTelemetryReport(availableReport) ? ( <Grid container spacing={ 24 }>
          {granularityView} 
          {reportBackDurationView}
        </Grid>) : null}

        { isHistoryReport(availableReport) ? ( <Grid container spacing={ 24 }>
          {startView} 
          {durationView}
        </Grid>) : null}

      </Grid>
    </div>
  );
}