import React from 'react';




import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import MarketContextCreateConfigurationStep from './MarketContextCreateConfigurationStep';
import MarketContextCreateConfirmationStep from './MarketContextCreateConfirmationStep';




import Paper from '@material-ui/core/Paper';

import { history } from '../../store/configureStore';




function getSteps() {
  return [ 'MarketContext settings', 'Confirmation' ];
}


export class MarketContextCreate extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
        activeStep: 0
        , marketContext: {
           name: ""
           , description: ""
           , color: ""
        }
    };
  }

  handleFinish = () => {
      this.props.createMarketContext(this.state.marketContext)
      history.push( '/vtn_configuration/marketcontext' )
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

  handleConfigurationChange = (marketContext) => {
    this.setState({marketContext})
  }


  render() {
    const {classes} = this.props;
    const steps = getSteps();
    const {activeStep, marketContext, hasError} = this.state;
    var that = this;


    var getStepContent = ( step ) => {
      switch (step) {
        case 0:
          return <MarketContextCreateConfigurationStep classes={classes} marketContext= {marketContext} handleConfigurationChange={this.handleConfigurationChange} hasError={hasError}/>
        case 1:
          return <MarketContextCreateConfirmationStep classes={classes} marketContext= {marketContext}  hasError={hasError}/>
        default:
          return 'Unknown step';
      }
    }

    

    function getSetValidation( step ) {
      switch (step) {
        case 0:
          return marketContext.name !== ""

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
                  { activeStep === steps.length - 1 ? 'Create Market Context' : 'Next' }
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

export default MarketContextCreate;
