import React from 'react';




import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';







import Paper from '@material-ui/core/Paper';

import EventCreateDescriptorStep from './EventCreateDescriptorStep'
import EventCreateActivePeriodStep from './EventCreateActivePeriodStep'
import EventCreateEventSignalStep from './EventCreateEventSignalStep'
import EventCreateEventTarget from './EventCreateEventTarget'
import EventCreateConfirmationStep from './EventCreateConfirmationStep'

import {minutesToICalDuration} from '../../utils/time'





function getSteps() {
  return [ 'Descriptor', 'Active Period', 'Signal', 'Targets', 'Confirmation' ];
}


export class EventCreate extends React.Component {
  constructor( props ) {
    super( props );
    var now = new Date()
    this.state = {
      activeStep: 0,
      descriptor: {
        // eventName: "",
        // timezone: "UTC",
        // priority:0,
        // responseRequired:"always",
        // testEvent: false,
        // vtnComment: "",
       
        // vtnComment: "",
        // marketContext: null
        oadrProfile: "OADR20B",
        priority:0,
        responseRequired:"ALWAYS",
        testEvent: false,
        vtnComment: "",
        marketContext: "http://MarketContext1"
      },
      activePeriod: {
        // start: null,
        // duration: "",
        timezone: "UTC",
        start: now.getTime(),
        duration: 120,
        notificationDuration: 120,
        rampUpDuration: 120,
        toleranceDuration: 120,
        recoveryDuration: 120,
      },
      eventSignal: [{
        // signalName: "",
        // signalType: "",
        // unitType: "",
        // signalInterval: [],
        // currentValue: "",
     
        intervals: [],
        currentValue: "97.5",
        signalName: "ENERGY_PRICE",
        signalType: "price",
        unitType: "euro_per_kwh",
        
      }],
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
      start: this.state.activePeriod.start,
      duration: minutesToICalDuration(this.state.activePeriod.duration)
    }
    addXMLDurationIfNotNull(activePeriod, "rampUpDuration", this.state.activePeriod.rampUpDuration);
    addXMLDurationIfNotNull(activePeriod, "recoveryDuration", this.state.activePeriod.recoveryDuration);
    addXMLDurationIfNotNull(activePeriod, "toleranceDuration", this.state.activePeriod.toleranceDuration);
    addXMLDurationIfNotNull(activePeriod, "notificationDuration", this.state.activePeriod.notificationDuration);

    var dto = {
     
      
      published: needPublish,
      descriptor: {
        marketContext: this.state.descriptor.marketContext,
        priority: this.state.descriptor.priority,
        responseRequired: this.state.descriptor.responseRequired,
        testEvent: this.state.descriptor.testEvent,
        state: "ACTIVE" ,
        oadrProfile: this.state.descriptor.oadrProfile,
      },

      activePeriod: activePeriod,

      signals: this.state.eventSignal,

      targets: this.state.eventTarget
    }
    this.props.createEvent( dto );
  }

  handleDescriptorChange = (descriptor) => {
    this.setState(descriptor);
  }

  handleActivePeriodChange = (activePeriod) => {
    this.setState(activePeriod);
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
    const {classes, marketContext, group, ven} = this.props;
    const steps = getSteps();
    const {activeStep, descriptor, activePeriod, eventSignal, eventTarget, hasError} = this.state;
    var that = this;


    var getStepContent = ( step ) => {
      switch (step) {
        case 0:
          return <EventCreateDescriptorStep classes={classes} marketContext={marketContext} 
            descriptor={descriptor} onChange={this.handleDescriptorChange} hasError={hasError}/>
        case 1:
          return <EventCreateActivePeriodStep classes={classes} marketContext={marketContext} 
            activePeriod={activePeriod} onChange={this.handleActivePeriodChange} hasError={hasError}/>
        case 2:
          return <EventCreateEventSignalStep classes={classes} onChange={this.handleEventSignalChange}
            eventSignal={eventSignal} hasError={hasError}/>
        case 3:
          return <EventCreateEventTarget classes={classes} group={group} eventTarget={eventTarget} 
            onChange={this.handleEventTargetChange}
            ven={ven}
            onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
            onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
            onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}/>
        case 4:
          return <EventCreateConfirmationStep classes={classes} group={group} marketContext={marketContext} 
            descriptor={descriptor} eventTarget={eventTarget} eventSignal={eventSignal} createEvent={this.createEvent}/>
        default:
          return 'Unknown step';
      }
    }

    

    function getSetValidation( step ) {
      switch (step) {
        case 0:
          return descriptor.priority !== null
            && descriptor.marketContext !== null
            && descriptor.eventName !== "";

        case 1:
          return activePeriod.start !== null 
            && activePeriod.duration !== "";

        case 2:

          for(var i in eventSignal){
            if( eventSignal[i].signalName === "" 
              || eventSignal[i].signalType === "" 
              || (eventSignal[i].currentValue === "" 
              && eventSignal[i].signalInterval.length === 0 )){
              return false;
            }
          }
          return true;
          
            
        case 3:
          return eventTarget.length > 0;
        case 4:
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
