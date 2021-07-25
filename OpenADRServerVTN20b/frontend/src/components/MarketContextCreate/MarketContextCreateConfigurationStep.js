import React from 'react';







import TextField from '@material-ui/core/TextField';



import Grid from '@material-ui/core/Grid';



import ColorPicker from 'material-ui-color-picker'



import Avatar from '@material-ui/core/Avatar';



import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';

import FormControlLabel from '@material-ui/core/FormControlLabel';



import MenuItem from '@material-ui/core/MenuItem';

import Select from '@material-ui/core/Select';

import Divider from '@material-ui/core/Divider';

import {TimezonePicker, DateAndTimePicker, DurationPicker} from '../common/TimePicker'


import InputLabel from '@material-ui/core/InputLabel';

import Checkbox from '@material-ui/core/Checkbox';
import {Panel} from '../common/Structure'


export class MarketContextCreateConfigurationStep extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {};


   
  }

   

  onNameChange = (event) => {
    this.props.marketContext.name = event.target.value;
    this.props.onChange(this.props.marketContext)
  }

  onDescriptionChange = (event) => {
    this.props.marketContext.description = event.target.value;
    this.props.onChange(this.props.marketContext)
  }

  onColorChange = (color) => {
    this.props.marketContext.color = color;
    this.props.onChange(this.props.marketContext)
  }

  onOadrProfileChange = (event) => {
    this.props.marketContext.descriptor.oadrProfile = event.target.value;;
    this.props.onChange(this.props.marketContext)
  }

  onPriorityChange = (event) => {
    this.props.marketContext.descriptor.priority = event.target.value;;
    this.props.onChange(this.props.marketContext)
  }

  onResponseRequiredChange = (event) => {
    this.props.marketContext.descriptor.responseRequired = event.target.value;;
    this.props.onChange(this.props.marketContext)
  }

  onTestEventChange = (event) => {
    this.props.marketContext.descriptor.testEvent = event.target.checked;;
    this.props.onChange(this.props.marketContext)
  }

  onPreferredGranularityChange = (event) => {
    this.props.marketContext.reportSubscriptionStrategy.preferredGranularity = event.target.value;;
    this.props.onChange(this.props.marketContext)
  }

  onPreferredReportBackDurationChange = (event) => {
    this.props.marketContext.reportSubscriptionStrategy.preferredReportBackDuration = event.target.value;
    this.props.onChange(this.props.marketContext)
  }

  onScheduledCronChange = (event) => {
    this.props.marketContext.demandResponseEventScheduleStrategy.scheduledCron = event.target.value;;
    this.props.onChange(this.props.marketContext)
  }

  onScheduledCronTimezoneChange = (event) => {
    this.props.marketContext.demandResponseEventScheduleStrategy.scheduledCronTimezone = event.target.value;;
    this.props.onChange(this.props.marketContext)
  }

  onScheduledDateChange = (event) => {
    this.props.marketContext.demandResponseEventScheduleStrategy.scheduledDate = event.target.value;;
    this.props.onChange(this.props.marketContext)
  }


  render() {
    const {classes, marketContext, hasError} = this.props;
    var descriptor = marketContext.descriptor || {};
    var activePeriod = marketContext.activePeriod || {};
    var demandResponseEventScheduleStrategy = marketContext.demandResponseEventScheduleStrategy || {};
    var reportSubscriptionStrategy = marketContext.reportSubscriptionStrategy || {};

    

    return (
      <React.Fragment>
      <Panel title="Identitification">
        <Grid container >
            <Grid item xs={ 2 }>
              <FormControl className={ classes.formControl } fullWidth={true}>
              <TextField required
                         label="Name"
                         value={ marketContext.name }
                         className={ classes.textField }
                         error={ hasError }
                         onChange={this.onNameChange}
                         InputLabelProps={ { shrink: true, } } />
              </FormControl>
            </Grid>
             
             <Grid item xs={ 8 }>
               <FormControl className={ classes.formControl } fullWidth={true}>
              <TextField label="Description"
                         value={ marketContext.description }
                         className={ classes.textField }
                         multiline
                         onChange={this.onDescriptionChange} 
                         InputLabelProps={ { shrink: true, } } />
              </FormControl>


        
                         
      
            </Grid>
            <Grid item xs={ 1 }>
             <ColorPicker label="Color"
                             defaultValue='#000'

                             value={marketContext.color}
                             TextFieldProps={ { 
                              className: classes.textField
                              , InputLabelProps: {
                                shrink: true,
                              },
                              InputProps:{style:{color: marketContext.color}}
                              } } 
                             onChange={ this.onColorChange } />

             </Grid>
              <Grid item xs={ 1 }>

              <Avatar style={{backgroundColor: marketContext.color, width: "15px", height: "15px", top: "25px"}}/>
             </Grid>
          
   
          
          </Grid>






        </Panel>

        <Panel title="Behavior">
        <Grid container >
         
            <Grid  item xs={3} >
             <FormControl className={ classes.formControl } fullWidth={true}>
               
                  <InputLabel shrink className={ classes.textField }>
                    OadrProfile
                  </InputLabel>
                  <Select value={ descriptor.oadrProfile}
                                 onChange={this.onOadrProfileChange}
                                className={ classes.textField }
                                inputProps={ { name: 'oadr_profile_select', id: 'oadr_profile_select', className:"MuiInputBase-input MuiInput-input"} }
                                >
                        <MenuItem key="menu_item_oadr_profile_20a" value="OADR20A">
                         2.0a
                         </MenuItem> 

                         <MenuItem key="menu_item_oadr_profile_20a" value="OADR20B">
                         2.0b
                         </MenuItem> 
                  </Select>
                </FormControl>
          </Grid>
          <Grid item xs={ 3 }>
               <FormControl className={ classes.formControl } fullWidth={true}>
                <TextField required label="Priority"
                  type="number"
                  value={ descriptor.priority }
                  onChange={this.onPriorityChange}
                  className={classes.textField}
                  
                  InputLabelProps={ { shrink: true, } }  />
    
              </FormControl>                                 
          </Grid> 
    
          
          <Grid item xs={3} >
               <FormControl className={ classes.formControl } fullWidth={true}>
               
                  <InputLabel shrink className={ classes.textField }>
                    Response Required
                  </InputLabel>
                  <Select value={ descriptor.responseRequired}

                                 onChange={this.onResponseRequiredChange}
                           className={ classes.textField }
                                inputProps={ { name: 'response_required_select', id: 'response_required_select', className:"MuiInputBase-input MuiInput-input"  } }
                                >
                        <MenuItem key="response_required_item_always" value="ALWAYS">
                         Always
                         </MenuItem> 

                         <MenuItem key="response_required_item_never" value="NEVER">
                         Never
                         </MenuItem> 
                  </Select>
                </FormControl>
          </Grid>

          <Grid item xs={3} >
     
          <InputLabel shrink className={ classes.textField }>
            Has test Event
          </InputLabel>
          <Checkbox
                checked={descriptor.testEvent}
                onChange={this.onTestEventChange}
                value="testEvent"
                color="primary"
              />
          </Grid>
          </Grid>

     


        </Panel>



      

      <Panel title="Report subscription stategy">
          <Grid container >
             <Grid item xs={ 6}>
               <FormControl className={ classes.formControl } fullWidth={true}>
              <TextField label="Preferred granularity"
                         value={ reportSubscriptionStrategy.preferredGranularity }
                         className={ classes.textField }
                         multiline
                         onChange={this.onPreferredGranularityChange} 
                         InputLabelProps={ { shrink: true, } } />
              </FormControl>

            </Grid>
            <Grid item xs={ 6 }>
              <FormControl className={ classes.formControl } fullWidth={true}>
              <TextField label="Preferred report back duration"
                         value={ reportSubscriptionStrategy.preferredReportBackDuration }
                         className={ classes.textField }
                         multiline
                         onChange={this.onPreferredReportBackDurationChange} 
                         InputLabelProps={ { shrink: true, } } />
              </FormControl>

            </Grid> 
         
            
          </Grid> 
      </Panel>
      <Panel title="Event schedule stategy">
          <Grid container >
             <Grid item xs={ 6}>
               <FormControl className={ classes.formControl } fullWidth={true}>
              <TextField label="Sceduled CRON"
                         value={ demandResponseEventScheduleStrategy.scheduledCron }
                         className={ classes.textField }
                         multiline
                         onChange={this.onScheduledCronChange} 
                         InputLabelProps={ { shrink: true, } } />
              </FormControl>

            </Grid>
            <Grid item xs={ 6 }>
              <FormControl className={ classes.formControl } fullWidth={true}>
              <TextField label="Scheduled CRON time zone"
                         value={ demandResponseEventScheduleStrategy.scheduledCronTimezone }
                         className={ classes.textField }
                         multiline
                         onChange={this.onScheduledCronTimezoneChange} 
                         InputLabelProps={ { shrink: true, } } />
              </FormControl>

            </Grid> 
         
            
          </Grid> 
          <Grid container >
             <Grid item xs={ 6}>
               <FormControl className={ classes.formControl } fullWidth={true}>
              <TextField label="Scheduled dates"
                         value={ demandResponseEventScheduleStrategy.scheduledDate }
                         className={ classes.textField }
                         multiline
                         onChange={this.onScheduledDateChange} 
                         InputLabelProps={ { shrink: true, } } />
              </FormControl>

            </Grid>
      
         
            
          </Grid> 
      </Panel>
        </React.Fragment>
 
    );
  }
}

export default MarketContextCreateConfigurationStep;
