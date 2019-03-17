import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';


import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';


import Paper from '@material-ui/core/Paper';

import EventCreateDescriptorStep from './EventCreateDescriptorStep'
import EventCreateEventSignalStep from './EventCreateEventSignalStep'
import EventCreateEventTarget from './EventCreateEventTarget'
import EventCreateConfirmationStep from './EventCreateConfirmationStep'




function getSteps() {
  return [ 'Descriptor', 'Event Signal', 'Event Targets', 'Confirmation' ];
}


export class EventCreate extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
      activeStep: 0,
      descriptor: {
        // timezone: "UTC",
        // priority:0,
        // responseRequired:"always",
        // testEvent: false,
        // vtnComment: "",
       
        // vtnComment: "",
        // marketContext: null
        
        priority:0,
        responseRequired:"always",
        testEvent: false,
        vtnComment: "",


        vtnComment: "",
        marketContext: "mouaiccool"
      },
      eventSignal: {
        // signalName: "",
        // signalType: "",
        // unitType: "",
         // start: null,
        // duration: "",
        // signalInterval: [],
        // currentValue: "",
         timezone: "UTC",
        start: 0,
        duration: 120,
        signalInterval: [],
        currentValue: "97.5",
        signalName: "ENERGY_PRICE",
        signalType: "price",
        unitType: "euro_per_kwh",
        notificationDuration: 120,
        rampUpDuration: 120,
        toleranceDuration: 120,
        recoveryDuration: 120,
      },
      // eventTarget: []
      eventTarget: [{targetType:"group", targetId:"Group1"}]
    };
  }

  handleFinish = () => {
    this.handleCreateEvent();
  }

  handleNext = () => {
    const {activeStep} = this.state;
    if ( activeStep === getSteps().length - 1 ) {
      this.handleFinish();
    } else {
      this.setState( {
        activeStep: activeStep + 1,
      } );
    }

  };

  handleBack = () => {
    this.setState( state => ({
      activeStep: state.activeStep - 1,
    }) );
  };

  handleReset = () => {
    this.setState( {
      activeStep: 0,
    } );
  };

 

  handleCreateEvent = () => {
    // var dto = {
    //   "comaSeparatedTargetedVenUsername": "string",
    //   "createdTimestamp": 0,
    //   "duration": "string",
    //   "event": "string",
    //   "eventId": "string",
    //   "id": 0,
    //   "lastUpdateTimestamp": 0,
    //   "marketContext": "string",
    //   "modificationNumber": 0,
    //   "notificationDuration": "string",
    //   "priority": 0,
    //   "rampUpDuration": "string",
    //   "recoveryDuration": "string",
    //   "start": 0,
    //   "state": "ACTIVE",
    //   "testEvent": true,
    //   "toleranceDuration": "string",
    //   "value": "SIMPLE_SIGNAL_PAYLOAD_NORMAL"
    // }
    var dto = {
     
      
      descriptor: {
        marketContext: this.state.descriptor.marketContext,
        priority: this.state.descriptor.priority,
        responseRequired: this.state.descriptor.responseRequired,
        testEvent: this.state.descriptor.testEvent
      },

      activePeriod: {
        start: this.state.eventSignal.start,
        duration: this.state.eventSignal.duration,
        rampUpDuration: this.state.eventSignal.rampUpDuration,
        recoveryDuration: this.state.eventSignal.recoveryDuration,
        toleranceDuration: this.state.eventSignal.toleranceDuration,
        notificationDuration: this.state.eventSignal.notificationDuration
      },

      signals: [{
        signalName: this.state.eventSignal.signalName,
        signalType: this.state.eventSignal.signalType,
        unitType: this.state.eventSignal.unitType,
        currentValue: this.state.eventSignal.currentValue,
        intervals: this.state.eventSignal.signalInterval
      }],

      targets: this.state.eventTarget
    }
    console.log(JSON.stringify(dto))
    // this.props.createEvent( dto );
  }

  handleDescriptorChange = (descriptor) => {
    this.setState(descriptor);
  }

  handleEventSignalChange = (eventSignal) => {
    this.setState(eventSignal);
  }

  handleEventTargetChange = (eventTarget) => {
    this.setState(eventTarget);
  }

  createEvent = () => {

  } 

  

  render() {
    const {classes, marketContext, group} = this.props;
    const steps = getSteps();
    const {activeStep, descriptor, eventSignal, eventTarget, hasError} = this.state;
    var that = this;


    var getStepContent = ( step ) => {
      switch (step) {
        case 0:
          return <EventCreateDescriptorStep classes={classes} marketContext={marketContext} 
            descriptor={descriptor} onChange={this.handleDescriptorChange} hasError={hasError}/>
        case 1:
          return <EventCreateEventSignalStep classes={classes} onChange={this.handleEventSignalChange}
            eventSignal={eventSignal} hasError={hasError}/>
        case 2:
          return <EventCreateEventTarget classes={classes} group={group} eventTarget={eventTarget} 
            onChange={this.handleEventTargetChange}/>
        case 3:
          return <EventCreateConfirmationStep classes={classes} group={group} marketContext={marketContext} 
            descriptor={descriptor} eventTarget={eventTarget} eventSignal={eventSignal} createEvent={this.createEvent}/>
        default:
          return 'Unknown step';
      }
    }

    

    function getSetValidation( step ) {
      switch (step) {
        case 0:
          return descriptor.priority != null
            && descriptor.marketContext != null;

        case 1:
          return eventSignal.start != null 
            && eventSignal.duration != ""
            && (eventSignal.currentValue != "" || eventSignal.signalInterval.length > 0 )
            
        case 2:
          return eventTarget.length > 0;
        case 3:
          return true;
        default:
          return false
      }
    }

    function handleValidatedNext( step ) {
      return (e) => {
        if ( getSetValidation( step ) ) {
          if ( that.state.hasError ) {
            that.setState( {
              hasError: false
            } );
          }
          that.handleNext( e );
        } else {
          that.setState( {
            hasError: true
          } );
        }
      }
    }

    return (
    <div className={ classes.root }>
      <Stepper activeStep={ activeStep } className={ classes.stepper }>
        { steps.map( (label, index) => {
            const props = {};
            const labelProps = {};
            props.completed = false;
            return (
            <Step key={ label } {...props}>
              <StepLabel {...labelProps}>
                { label }
              </StepLabel>
            </Step>
            );
          } ) }
      </Stepper>
      <div>
        { activeStep === steps.length ? (
            <div>
              <Typography className={ classes.instructions }>
                All steps completed - you&apos;re finished
              </Typography>
              <Button onClick={ this.handleReset } className={ classes.button }>
                Reset
              </Button>
            </div>
            ) : (
            <div>
              <div>
                <Button disabled={ activeStep === 0 }
                        onClick={ this.handleBack }
                        className={ classes.button }>
                  Back
                </Button>
                <Button variant="contained"
                        color="primary"
                        onClick={ handleValidatedNext( activeStep ) }
                        className={ classes.button }>
                  { activeStep === steps.length - 1 ? 'Finish' : 'Next' }
                </Button>
              </div>
              <Typography component="div" className={ classes.instructions }>
                <Paper elevation={ 1 } style={ { padding: '20px 0px' } }>
                  { getStepContent( activeStep ) }
                </Paper>
              </Typography>
              
            </div>
            ) }
      </div>
    </div>
    );
  }
}

export default EventCreate;
