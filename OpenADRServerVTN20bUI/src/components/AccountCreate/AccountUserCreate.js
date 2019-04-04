import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';


import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';


import Paper from '@material-ui/core/Paper';

import AccountUserCreateIdentificationStep from './AccountUserCreateIdentificationStep'
import AccountCreateAuthenticationStep from './AccountCreateAuthenticationStep'
import AccountCreateRoleStep from './AccountCreateRoleStep'
import AccountCreateConfirmationStep from './AccountCreateConfirmationStep'

const defaultAuthenticationType = 'x509'
const authenticationTypes = {
  x509: {
    label: 'Client certificate authentication'
  },
  login: {
    label: 'Login / Password authentication'
  }
}

function getSteps() {
  return [ 'Identification settings', 'Authentication settings', 'Roles settings', 'Confirmation' ];
}


export class AccountUserCreate extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
      activeStep: 0,
      identification: {
        commonName: "",
        needCertificateGeneration: 'rsa'
      },
      authentication: {
          authenticationTypes:authenticationTypes,
          authenticationType: defaultAuthenticationType,
          authenticationPassword: '',
          authenticationPasswordConfirm: '',
          
      },
      roles: []
    };
  }

  handleFinish = () => {

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

  
  handleIndentificationStepChange = (identification) => {
    this.setState({identification});
  }

  handleAuthenticationStepChange = (authentication) => {
    this.setState({authentication});
  } 

  handleRoleStepChange = (roles) => {
    this.setState({roles});
  }

 
  render() {
    const {classes, vtnConfiguration} = this.props;
    const steps = getSteps();
    const {activeStep} = this.state;
    var that = this;


    function getStepContent( step ) {
      switch (step) {
        case 0:
          return <AccountUserCreateIdentificationStep classes={classes}
            identification={that.state.identification}
            onChange={that.handleIndentificationStepChange}
            vtnConfiguration={vtnConfiguration}/>;
        case 1:
          return <AccountCreateAuthenticationStep classes={classes}
            authentication={that.state.authentication}
            onChange={that.handleAuthenticationStepChange}/>;
        case 2:
          return <AccountCreateRoleStep classes={classes}
            roles={that.state.roles}
            onChange={that.handleRoleStepChange}/>;
        case 3:
          return <AccountCreateConfirmationStep classes={classes}/>;
        default:
          return 'Unknown step';
      }
    }

    function getSetValidation( step ) {
      switch (step) {
        case 0:
          return true;
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
                  { activeStep === steps.length - 1 ? 'Create Ven' : 'Next' }
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

export default AccountUserCreate;
