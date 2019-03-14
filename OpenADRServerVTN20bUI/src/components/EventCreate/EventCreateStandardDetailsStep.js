import React from 'react';

import Typography from '@material-ui/core/Typography';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';

import TextField from '@material-ui/core/TextField';

import ExtensionIcon from '@material-ui/icons/Extension';

import Grid from '@material-ui/core/Grid';


import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import Select from '@material-ui/core/Select';

import Divider from '@material-ui/core/Divider';

import IconButton from '@material-ui/core/IconButton';
import ExpandMore from '@material-ui/icons/ExpandMore';

import Checkbox from '@material-ui/core/Checkbox';



import {TimezonePicker, DateAndTimePicker, DurationPicker} from '../common/TimePicker'

import { MarketContextSelectDialog } from '../common/VtnconfigurationDialog'

const labelStyle = {

  boxSizing: 'border-box',
  color: 'rgba(0, 0, 0, 0.54)',
  fontSize: '1rem',
  fontWeight: 400,
  left: '0px',
  lineHeight: 1,
  transition: 'color 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms,transform 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms',
  transform: 'translate(0, 1.5px) scale(0.75)',
  transformOrigin: 'top left',
  top: 0,
  left: 0,
  marginTop: "-8px"
}

export class EventCreateStandardDetailsStep extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {
      marketContextSelectDialogOpen: false
    }
  
  }

  handleTimezoneChange = (timezone) => {
    let details = this.props.standardDetails;
    details.timezone = timezone;
    this.props.onChange(details);
  }

  handleStartChange = (start) => {
    console.log(start)
    let details = this.props.standardDetails;
    details.start = start;
    this.props.onChange(details);
  }


  handleDurationChange = (duration) => {
    let details = this.props.standardDetails;
    details.duration = duration;
    this.props.onChange(details);
  }

  handleMarketContextSelectOpen = () => {
    this.setState({marketContextSelectDialogOpen: true})
  }

  handleMarketContextSelectClose = (context) => {
    if(context != null) {
      let details = this.props.standardDetails;
      details.marketContext = context.name;
      this.props.onChange(details);
    }
    this.setState({marketContextSelectDialogOpen: false})
  }

  handlePriorityChange = (e) => {
    let details = this.props.standardDetails;
    details.priority = e.target.value;
    this.props.onChange(details);
  }

  handleResponseRequiredChange = (e) => {
    let details = this.props.standardDetails;
    details.responseRequired = e.target.value;
    this.props.onChange(details);
  }

  handleTestEventChange = (e) => {
    let details = this.props.standardDetails;
    details.testEvent = e.target.checked;
    this.props.onChange(details);
  }

  handleVtnCommentChange = (e) => {
    let details = this.props.standardDetails;
    details.vtnComment = e.target.value;
    this.props.onChange(details);
  }

  render() {
    const {classes, hasError, standardDetails, marketContext} = this.props;


    return (
    <Grid container
          spacing={ 8 }
          justify="center">
      <Grid container spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 3 }>
          <TimezonePicker label="Timezone"  classes={classes} value={standardDetails.timezone} onChange={this.handleTimezoneChange}/>
        </Grid> 
        <Grid item xs={ 3 }>
           <DateAndTimePicker classes={ classes } field="Start Date" error={hasError && standardDetails.start == null}
             value={standardDetails.start} onChange={this.handleStartChange} />
        </Grid>
        <Grid item xs={ 2 }>
           <DurationPicker classes={ classes } field="Duration (minutes)"  error={hasError && standardDetails.duration == ""}
               value={standardDetails.duration} onChange={this.handleDurationChange}/>
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>


      <Grid container
            style={ { marginTop: 20 } }
            spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
          <Divider />
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>

       <Grid container spacing={ 24 } style={ { marginTop: 20 } } >
        <Grid item xs={ 2 } />
       
       
       <Grid item xs={ 3 }>
     
              <TextField required label="Market Context" error={hasError && standardDetails.marketContext == null}
                 value={ (standardDetails.marketContext) ? standardDetails.marketContext : "" }
                 className={classes.textField}
                 fullWidth={true}
                 onClick={this.handleMarketContextSelectOpen}
                 InputProps={ { readOnly: true, } } />

              <MarketContextSelectDialog marketContext={ marketContext}
                                         open={ this.state.marketContextSelectDialogOpen }
                                         close={ this.handleMarketContextSelectClose }
                                         title="Select Market Context" />
  
                                         
        </Grid> 
        <Grid item xs={ 1 }>
     
              <TextField required label="Priority"
                type="number"
                value={ standardDetails.priority }
                onChange={this.handlePriorityChange}
                className={classes.textField}
                fullWidth={true}/>
  
                                         
        </Grid> 

        <Grid item xs={2} >
             <FormControl className={ classes.formControl }>
                <FormLabel style={ labelStyle } component="label">
                  Response Required
                </FormLabel>
                <Select value={ standardDetails.responseRequired}
                              style={ { marginTop: 0 } }

                               onChange={this.handleResponseRequiredChange}
                   
                              inputProps={ { name: 'response_required_select', id: 'response_required_select', } }
                              >
                      <MenuItem key="response_required_item_always" value="always">
                       Always
                       </MenuItem> 

                       <MenuItem key="response_required_item_never" value="never">
                       Never
                       </MenuItem> 
                </Select>
              </FormControl>
        </Grid>
         <Grid item xs={2} >
           <FormControlLabel
          control={
            <Checkbox
              checked={standardDetails.testEvent}
              onChange={this.handleTestEventChange}
              value="testEvent"
              color="primary"
            />
          }
          label="Test Event"
        />
        </Grid>
       
        <Grid item xs={ 2 } />
      </Grid>

      <Grid container
            style={ { marginTop: 20 } }
            spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
          <Divider />
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>

      <Grid container spacing={ 24 } style={ { marginTop: 20 } }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
            <TextField  label="VTN Comment"
                multiline
          rowsMax="4"
                value={ standardDetails.vtnComment }
                onChange={this.handleVtnCommentChange}
                className={classes.textField}
                fullWidth={true}/>
        </Grid>
        <Grid item xs={ 2 } />

      </Grid>

    </Grid>
    );
  }
}

export default EventCreateStandardDetailsStep;
