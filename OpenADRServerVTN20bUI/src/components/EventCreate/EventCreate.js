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

import {minutesToICalDuration} from '../../utils/time'





function getSteps() {
  return [ 'Descriptor', 'Event Signal', 'Event Targets', 'Confirmation' ];
}


export class EventCreate extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
      activeStep: 0,
      descriptor: {
        // eventId: "",
        // eventName: "",
        // timezone: "UTC",
        // priority:0,
        // responseRequired:"always",
        // testEvent: false,
        // vtnComment: "",
       
        // vtnComment: "",
        // marketContext: null
        oadrProfile: "OADR20B",
        
        eventId: "mouaiccool",
        eventName: "mouaiccool",
        priority:0,
        responseRequired:"ALWAYS",
        testEvent: false,
        vtnComment: "",
        marketContext: "http://MarketContext1"
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
      eventTarget: [{targetType:"ven", targetId:"4A:D1:2E:95:49:43:80:0B:8D:E9"}]
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

 

  handleCreateEvent = (needPublish) => {
    var addXMLDurationIfNotNull = (src, field, value) => {
      if(value != null){
        src[field] = minutesToICalDuration(value);
      }
    }
    var activePeriod = {
      start: this.state.eventSignal.start,
      duration: minutesToICalDuration(this.state.eventSignal.duration)
    }
    addXMLDurationIfNotNull(activePeriod, "rampUpDuration", this.state.eventSignal.rampUpDuration);
    addXMLDurationIfNotNull(activePeriod, "recoveryDuration", this.state.eventSignal.recoveryDuration);
    addXMLDurationIfNotNull(activePeriod, "toleranceDuration", this.state.eventSignal.toleranceDuration);
    addXMLDurationIfNotNull(activePeriod, "notificationDuration", this.state.eventSignal.notificationDuration);

    var state = (needPublish != null && needPublish) ? "ACTIVE" : "UNPUBLISHED";
    console.log(needPublish, state)
    var dto = {
     
      eventId:this.state.descriptor.eventId,
      eventName: this.state.descriptor.eventName,
      state: state,
      oadrProfile: "OADR20B",
      descriptor: {
        marketContext: this.state.descriptor.marketContext,
        priority: this.state.descriptor.priority,
        responseRequired: this.state.descriptor.responseRequired,
        testEvent: this.state.descriptor.testEvent
      },

      activePeriod: activePeriod,

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
    this.props.createEvent( dto );
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
        console.log(descriptor)
          return descriptor.priority != null
            && descriptor.marketContext != null
            && descriptor.eventId != ""
            && descriptor.eventName != "";

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
                        onClick={ handleValidatedNext( activeStep )}
                        className={ classes.button }>
                  { activeStep === steps.length - 1 ? 'Create Event' : 'Next' }
                </Button>
                { activeStep === steps.length - 1 ? <Button variant="contained"
                        color="secondary"
                        onClick={ () => { that.handleCreateEvent(true) } }
                        className={ classes.button }>
                    Create And Publish Event
                </Button> : null }
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
