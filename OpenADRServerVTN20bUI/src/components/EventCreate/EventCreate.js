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

import EventCreateStandardDetailsStep from './EventCreateStandardDetailsStep'
import EventCreateEventSignalStep from './EventCreateEventSignalStep'
import EventCreateEventSignalInterval from './EventCreateEventSignalInterval'
import EventCreateConfirmationStep from './EventCreateConfirmationStep'




function getSteps() {
  return [ 'Event Standard Details', 'Event Signal', 'Event Signal Interval', 'Confirmation' ];
}


export class EventCreate extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
      activeStep: 0,
      standardDetails: {
        // timezone: "UTC",
        // priority:0,
        // responseRequired:"always",
        // testEvent: false,
        // vtnComment: "",
        // start: null,
        // duration: "",
        // vtnComment: "",
        // marketContext: null
         timezone: "UTC",
        priority:0,
        responseRequired:"always",
        testEvent: false,
        vtnComment: "",
        start: 0,
        duration: 120,
        vtnComment: "",
        marketContext: "mouaiccool"
      },
      eventSignal: {
        signalName: "",
        signalType: ""
      },
      eventSignalInterval: {}
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
    var dto = {

    }

    this.props.createEvent( dto );
  }

  handleStandardDetailsChange = (standardDetails) => {
    this.setState(standardDetails);
  }

  handleEventSignalChange = (eventSignal) => {
    this.setState(eventSignal);
  }

  

  render() {
    const {classes, marketContext} = this.props;
    const steps = getSteps();
    const {activeStep, standardDetails, eventSignal, hasError} = this.state;
    var that = this;


    var getStepContent = ( step ) => {
      switch (step) {
        case 0:
          return <EventCreateStandardDetailsStep classes={classes} marketContext={marketContext} 
              standardDetails={standardDetails} onChange={this.handleStandardDetailsChange} hasError={hasError}/>
        case 1:
          return <EventCreateEventSignalStep classes={classes} onChange={this.handleEventSignalChange}
          eventSignal={eventSignal} hasError={hasError}/>
        case 2:
          return <EventCreateEventSignalInterval classes={classes} eventSignalInterval={this.state.eventSignalInterval}/>
        case 3:
          return <EventCreateConfirmationStep classes={classes} />
        default:
          return 'Unknown step';
      }
    }

    

    function getSetValidation( step ) {
      switch (step) {
        case 0:
          return standardDetails.start != null 
            && standardDetails.duration != "" 
            && standardDetails.priority != null
            && standardDetails.marketContext != null;

        case 1:
          return true;
        case 2:
          return true;
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
