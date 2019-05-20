import React from 'react';




import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';


import Paper from '@material-ui/core/Paper';

import AccountAppCreateIdentificationStep from './AccountAppCreateIdentificationStep'
import UserCreateAuthenticationFormPanel from '../common/UserCreateAuthenticationFormPanel'
import AccountCreateRoleStep from './AccountCreateRoleStep'
import UserCreateConfirmationFormPanel from '../common/UserCreateConfirmationFormPanel'

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


export class AccountAppCreate extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
      activeStep: 0,
      identification: {
        commonName: "",
        needCertificateGeneration: 'rsa'
      },
      authentication: {
          username: '',
          authenticationTypes:authenticationTypes,
          authenticationType: defaultAuthenticationType,
          authenticationPassword: '',
          authenticationPasswordConfirm: '',
          
      },
      roles: []
    };
  }

  handleCreateApp = () => {
    const {identification, authentication,roles} = this.state;
    var dto = {
      'commonName': identification.venCommonName,
      'needCertificateGeneration': identification.needCertificateGeneration,
      'authenticationType': authentication.authenticationType,
      'username': authentication.username,
      'roles': roles
    }

    if ( authentication.authenticationType === 'login' ) {
      dto.password = authentication.authenticationPassword
    }

    this.props.createApp( dto );
  }


  handleFinish = () => {
    this.handleCreateApp()
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
          return <AccountAppCreateIdentificationStep classes={classes}
            identification={that.state.identification}
            onChange={that.handleIndentificationStepChange}
            vtnConfiguration={vtnConfiguration}/>;
        case 1:
          return <UserCreateAuthenticationFormPanel classes={classes}
            authentication={that.state.authentication}
            identification={that.state.identification}
            onChange={that.handleAuthenticationStepChange}/>;
        case 2:
          return <AccountCreateRoleStep classes={classes}
            roles={that.state.roles}
            onChange={that.handleRoleStepChange}/>;
        case 3:
          return <UserCreateConfirmationFormPanel classes={classes}
            identification={that.state.identification}
            authentication={that.state.authentication}
            roles={that.state.roles}/>;
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
                  { activeStep === steps.length - 1 ? 'Create App' : 'Next' }
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

export default AccountAppCreate;
