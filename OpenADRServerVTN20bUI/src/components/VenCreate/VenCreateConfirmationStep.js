import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import classNames from 'classnames';

import Typography from '@material-ui/core/Typography';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';

import TextField from '@material-ui/core/TextField';

import ExtensionIcon from '@material-ui/icons/Extension';

import Grid from '@material-ui/core/Grid';

import InputAdornment from '@material-ui/core/InputAdornment';

import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import Select from '@material-ui/core/Select';

import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import WarningIcon from '@material-ui/icons/Warning';

import Divider from '@material-ui/core/Divider';



export class VenCreateConfirmationStep extends React.Component {

  constructor( props ) {
    super( props );
  }


  render() {
    const {classes, ven} = this.props;



    var WarningSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.warning }
                       style={ { maxWidth: 'none' } }
                       message={ <span id="client-snackbar" className={ classes.message }><WarningIcon style={ { width: 40, height: 40, marginRight: 60 } }className={ classNames( classes.icon, classes.iconVariant ) }/> { props.message }</span> } />
      );
    }
    var venId = ven.authenticationVenId;
    var noGenerateCertificateWarningView = null;
    var generateCertificateWarningView = null;
    var authenticationTypeWarningView = null;
    if ( ven.needLogin ) {
      authenticationTypeWarningView = <Grid container spacing={ 24 }>
                                        <Grid item xs={ 3 } />
                                        <Grid item xs={ 6 }>
                                          <WarningSnackbar message={ <Typography gutterBottom style={ { color: 'white' } }>
                                                                       <strong>Authentication using login / password is required.<br/> VenID is used as VEN login</strong>
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
    }



    if ( ven.needCertificateGeneration == 'no' ) {
      noGenerateCertificateWarningView = <Grid container spacing={ 24 }>
                                           <Grid item xs={ 3 } />
                                           <Grid item xs={ 6 }>
                                             <WarningSnackbar message={ <Typography gutterBottom style={ { color: 'white' } }>
                                                                          <strong>Certificate won't be generated for this VEN.<br/> Provided venId MUST match with VEN certificate fingerprint</strong>
                                                                        </Typography> } />
                                           </Grid>
                                           <Grid item xs={ 3 } />
                                         </Grid>
    } else if ( ven.needCertificateGeneration != 'no' ) {
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
    }



    return (

    <div>
      <Grid container
            spacing={ 8 }
            justify="center">
        <Grid container spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 2 }>
            <Typography gutterBottom
                        variant="title"
                        component="h2">
              VEN Settings
            </Typography>
          </Grid>
        </Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 1 }>
            <TextField label="Profile"
                       value={ ven.venOadrProfile }
                       className={ classes.textField }
                       margin="normal"
                       variant="outlined"
                       InputProps={ { readOnly: true } }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 1 }>
            <TextField label="Authentication"
                       value={ ven.authenticationType }
                       className={ classes.textField }
                       margin="normal"
                       variant="outlined"
                       InputProps={ { readOnly: true } }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 3 }>
            <TextField label="Common Name"
                       value={ ven.venCommonName }
                       className={ classes.textField }
                       margin="normal"
                       variant="outlined"
                       InputProps={ { readOnly: true } }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 3 }>
            <TextField label="VenID"
                       value={ venId }
                       className={ classes.textField }
                       margin="normal"
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
