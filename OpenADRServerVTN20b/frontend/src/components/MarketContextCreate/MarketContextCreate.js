import React from 'react';




import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import MarketContextCreateConfigurationStep from './MarketContextCreateConfigurationStep';
import MarketContextCreateConfirmationStep from './MarketContextCreateConfirmationStep';
import MarketContextCreateSignalStep from './MarketContextCreateSignalStep';
import MarketContextCreateReportStep from './MarketContextCreateReportStep';




import Paper from '@material-ui/core/Paper';

import { history } from '../../store/configureStore';




function getSteps() {
  return [ 'Settings', 'Signals', 'Reports', 'Confirmation' ];
}


export class MarketContextCreate extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
        activeStep: 0
     
    };
  }

  handleFinish = () => {
      this.props.createMarketContext(this.props.marketContext)
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

  onChange = (marketContext) => {
    this.setState({marketContext})
  }


  render() {
    const {classes, definition, filterUnit, marketContext} = this.props;
    const steps = getSteps();
    const {activeStep, hasError} = this.state;
    var that = this;

    var getStepContent = ( step ) => {
      switch (step) {
        case 0:
          return <MarketContextCreateConfigurationStep classes={classes} marketContext= {marketContext} onChange={this.onChange} hasError={hasError}/>
        case 1:
          return <MarketContextCreateSignalStep classes={classes} 
                    marketContext= {marketContext}  
                    onChange={this.onChange} 
                    hasError={hasError}
                    definition={definition}
                    filterUnit={filterUnit}
                    group={this.props.group}
                    ven={this.props.ven}
                    onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
                    onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
                    onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}
                  />
        case 2:
          return <MarketContextCreateReportStep classes={classes} 
                    marketContext= {marketContext}  
                    onChange={this.onChange} 
                    hasError={hasError}
                    definition={definition}
                    filterUnit={filterUnit}
                   />
        case 3:
          return <MarketContextCreateConfirmationStep classes={classes} marketContext= {marketContext}  onChange={this.onChange} hasError={hasError}/>
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

        case 2:
          return true;

        case 3:
          return true;

        default:
          return false;
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
