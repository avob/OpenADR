import React from 'react';

import classNames from 'classnames';

import Typography from '@material-ui/core/Typography';





import TextField from '@material-ui/core/TextField';



import Grid from '@material-ui/core/Grid';









import SnackbarContent from '@material-ui/core/SnackbarContent';
import WarningIcon from '@material-ui/icons/Warning';

import Divider from '@material-ui/core/Divider';



export class VenCreateConfirmationStep extends React.Component {






  render() {
    const {classes, identification, authentication} = this.props;



    var WarningSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.warning }
                       style={ { maxWidth: 'none' } }
                       message={ <span id="client-snackbar" className={ classes.message }><WarningIcon style={ { width: 40, height: 40, marginRight: 60 } }className={ classNames( classes.icon, classes.iconVariant ) }/> { props.message }</span> } />
      );
    }
    var venId = authentication.authenticationVenId;
    var noGenerateCertificateWarningView = null;
    var generateCertificateWarningView = null;
    var authenticationTypeWarningView = null;
    if ( identification.needCertificateGeneration !== "no" ) {
      authenticationTypeWarningView = <Grid container spacing={ 24 }>
                                        <Grid item xs={ 3 } />
                                        <Grid item xs={ 6 }>
                                          <WarningSnackbar message={ <Typography gutterBottom style={ { color: 'white' } }>
                                                                       <strong>Authentication using login / password is required.<br/> VenID is used as VEN login</strong>
                                                                     </Typography> } />
                                        </Grid>
                                        <Grid item xs={ 3 } />
                                      </Grid>
      venId = '<generated>'
       generateCertificateWarningView = <Grid container spacing={ 24 }>
                                         <Grid item xs={ 3 } />
                                         <Grid item xs={ 6 }>
                                           <WarningSnackbar message={ <Typography gutterBottom style={ { color: 'white' } }>
                                                                        <strong>Certificate will be generated for this VEN and can only be downloaded at the end of this form.<br/> Certificate can't be regenerated without re-creating the VEN.</strong>
                                                                      </Typography> } />
                                         </Grid>
                                         <Grid item xs={ 3 } />
                                       </Grid>

    } else {
      authenticationTypeWarningView = <Grid container spacing={ 24 }>
                                        <Grid item xs={ 3 } />
                                        <Grid item xs={ 6 }>
                                          <WarningSnackbar message={ <Typography gutterBottom style={ { color: 'white' } }>
                                                                       <strong>Authentication using x509 client certificate is required.<br/></strong>
                                                                     </Typography> } />
                                        </Grid>
                                        <Grid item xs={ 3 } />
                                      </Grid>

      noGenerateCertificateWarningView = <Grid container spacing={ 24 }>
                                           <Grid item xs={ 3 } />
                                           <Grid item xs={ 6 }>
                                             <WarningSnackbar message={ <Typography gutterBottom style={ { color: 'white' } }>
                                                                          <strong>Certificate won't be generated for this VEN.<br/> Provided venId MUST match with VEN certificate fingerprint</strong>
                                                                        </Typography> } />
                                           </Grid>
                                           <Grid item xs={ 3 } />
                                         </Grid>
    }

    return (

    <div>
      <Grid container
            spacing={ 8 }
            justify="center">
        <Grid container spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 1 }>
            <TextField label="Profile"
                       value={ identification.venOadrProfile }
                       className={ classes.textField }
                       margin="dense"
                       variant="outlined"
                       InputProps={ { readOnly: true } }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 1 }>
            <TextField label="Authentication"
                       value={ authentication.authenticationType }
                       className={ classes.textField }
                       margin="dense"
                       variant="outlined"
                       InputProps={ { readOnly: true } }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 3 }>
            <TextField label="Common Name"
                       value={ identification.venCommonName }
                       className={ classes.textField }
                       margin="dense"
                       variant="outlined"
                       InputProps={ { readOnly: true } }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 3 }>
            <TextField label="VenID"
                       value={ venId }
                       className={ classes.textField }
                       margin="dense"
                       variant="outlined"
                       InputProps={ { readOnly: true } }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 2 } />
        </Grid>
        <Grid container
              style={ { marginTop: 20, marginBottom: 20 } }
              spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 8 }>
            <Divider />
          </Grid>
          <Grid item xs={ 2 } />
        </Grid>
        { authenticationTypeWarningView }
        <Grid container
              style={ { marginTop: 20, marginBottom: 20 } }
              spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 8 }>
            <Divider />
          </Grid>
          <Grid item xs={ 2 } />
        </Grid>
        { noGenerateCertificateWarningView }
        { generateCertificateWarningView }
      </Grid>
    </div>
    );
  }
}

export default VenCreateConfirmationStep;
