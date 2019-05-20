import React from 'react';




import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';


import VenCreateIndentificationStep from './VenCreateIndentificationStep';
import UserCreateAuthenticationFormPanel from '../common/UserCreateAuthenticationFormPanel'
import VenCreateConfirmationStep from './VenCreateConfirmationStep';


import Paper from '@material-ui/core/Paper';


const defaultOadrProfile = '20b'
const oadrProfiles = {
  '20b': {
    label: 'Oadr 2.0b'
  },
  '20a': {
    label: 'Oadr 2.0a'
  },
}
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
  return [ 'Identification settings', 'Authentication settings', 'Confirmation' ];
}


export class VenCreate extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
      activeStep: 0,
     

      




      identification: {
        venCommonName: 'oadr.avob.com',
        venOadrProfile: defaultOadrProfile,
        needCertificateGeneration: 'rsa'
      },
      authentication: {
        authenticationTypes:authenticationTypes,
        authenticationType: defaultAuthenticationType,
        authenticationPassword: '',
        authenticationPasswordConfirm: '',
        authenticationVenId: '',
        
      },

    };
  }

  handleFinish = () => {
    this.handleCreateVen();
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


  
  handleAuthenticationTypeChange = (e) => {
    this.setState( {
      authenticationType: e.target.value,
    } );
  };

  handleAuthenticationVenIdChange = (e) => {
    this.setState( {
      authenticationVenId: e.target.value,
    } );
  };
  handleAuthenticationPasswordChange = (e) => {
    this.setState( {
      authenticationPassword: e.target.value,
    } );
  };
  handleAuthenticationPasswordConfirmChange = (e) => {
    this.setState( {
      authenticationPasswordConfirm: e.target.value,
    } );
  };

  handleCreateVen = () => {
    const {identification, authentication} = this.state;
    var dto = {
      'commonName': identification.venCommonName,
      'needCertificateGeneration': identification.needCertificateGeneration,
      'oadrProfil': identification.venOadrProfile,

      'authenticationType': authentication.authenticationType,
      'username': authentication.authenticationVenId
    }

    if ( authentication.authenticationType === 'login' ) {
      dto.password = authentication.authenticationPassword
    }

    this.props.createVen( dto );
  }


  render() {
    const {classes, vtnConfiguration} = this.props;
    const steps = getSteps();
    const {activeStep} = this.state;
    var that = this;


    function getStepContent( step ) {
      switch (step) {
        case 0:
          return <VenCreateIndentificationStep classes={ classes }
                                              hasError={ that.state.hasError }
                                              oadrProfiles={oadrProfiles}
                                              identification={that.state.identification}
                                              onChange={that.handleIndentificationStepChange}
                                              vtnConfiguration={vtnConfiguration} />;
        case 1:
          return <UserCreateAuthenticationFormPanel classes={classes}
            authentication={that.state.authentication}
            identification={that.state.identification}
            onChange={that.handleAuthenticationStepChange}/>;
        case 2:
          return <VenCreateConfirmationStep classes={ classes } 
            authentication={that.state.authentication}
            identification={that.state.identification} />;
        default:
          return 'Unknown step';
      }
    }

    function getSetValidation( step ) {
      switch (step) {
        case 0:
          return that.state.venCommonName !== ''
        case 1:

          var hasVenIdWhenNoCertificateGeneration = that.state.needCertificateGeneration !== 'no'
            || (that.state.needCertificateGeneration === 'no' && that.state.authenticationVenId !== '');

          var hasValidLoginPasswordWhenLoginAuthentication = that.state.authenticationType !== 'login' || (that.state.authenticationPassword !== '' &&
            that.state.authenticationPasswordConfirm !== '' && that.state.authenticationPassword === that.state.authenticationPasswordConfirm);

          return hasVenIdWhenNoCertificateGeneration && hasValidLoginPasswordWhenLoginAuthentication
        case 2:
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

export default VenCreate;
