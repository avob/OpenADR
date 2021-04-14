import React from 'react';




import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import GroupCreateConfigurationStep from './GroupCreateConfigurationStep';
import GroupCreateConfirmationStep from './GroupCreateConfirmationStep';




import Paper from '@material-ui/core/Paper';

import {minutesToICalDuration} from '../../utils/time'

import { history } from '../../store/configureStore';




function getSteps() {
  return [ 'Group settings', 'Confirmation' ];
}


export class GroupCreate extends React.Component {
  constructor( props ) {
    super( props );
    var now = new Date()
    this.state = {
        activeStep: 0
        , group: {
           name: ""
           , description: ""
           , color: ""
        }
    };
  }

  handleFinish = () => {
      this.props.createGroup(this.state.group)
      history.push( '/vtn_configuration/group' )
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

  handleConfigurationChange = (group) => {
    this.setState({group})
  }


  render() {
    const {classes} = this.props;
    const steps = getSteps();
    const {activeStep, group, hasError} = this.state;
    var that = this;


    var getStepContent = ( step ) => {
      switch (step) {
        case 0:
          return <GroupCreateConfigurationStep classes={classes} group= {group} handleConfigurationChange={this.handleConfigurationChange} hasError={hasError}/>
        case 1:
          return <GroupCreateConfirmationStep classes={classes} group= {group}  hasError={hasError}/>
        default:
          return 'Unknown step';
      }
    }

    

    function getSetValidation( step ) {
      switch (step) {
        case 0:
          return group.name !== ""

        case 1:
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
    <Paper className={ classes.root }>
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
                  { activeStep === steps.length - 1 ? 'Create Group' : 'Next' }
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
    </Paper>
    );
  }
}

export default GroupCreate;
