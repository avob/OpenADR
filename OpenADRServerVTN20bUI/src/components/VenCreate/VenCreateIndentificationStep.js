

import React from 'react';

import Typography from '@material-ui/core/Typography';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';

import TextField from '@material-ui/core/TextField';

import ExtensionIcon from '@material-ui/icons/Extension';

import Grid from '@material-ui/core/Grid';


import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import Select from '@material-ui/core/Select';

import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';

import Divider from '@material-ui/core/Divider';


import HelpIcon from '@material-ui/icons/Help';



const labelStyle = {

  boxSizing: 'border-box',
  color: 'rgba(0, 0, 0, 0.54)',
  fontSize: '1rem',
  fontWeight: 400,
  left: '0px',
  lineHeight: 1,
  transition: 'color 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms,transform 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms',
  transform: 'translate(0, 1.5px) scale(0.75)',
  transformOrigin: 'top left',
  top: 0,
  left: 0,
}
export class VenCreateIndentificationStep extends React.Component {

  constructor( props ) {
    super( props );
  }


  render() {
    const {classes} = this.props;

    var profileView = []

    for (var key in this.props.oadrProfiles) {
      var value = this.props.oadrProfiles[ key ];
      profileView.push(

        <MenuItem key={ key } value={ key }>
        { value.label }
        </MenuItem>

      )
    }

    var generateOptionView = null;
    if(this.props.vtnConfiguration && this.props.vtnConfiguration.supportCertificateGeneration){
        generateOptionView= [
          <FormControlLabel key="generate_rsa_radio" value="rsa"
                      control={ <Radio color="primary" /> }
                      label="Generate RSA certificate"
                      labelPlacement="end" />,
    <FormControlLabel key="generate_ecc_radio"  value="ecc"
                      control={ <Radio color="primary" /> }
                      label="Generate ECC certificate"
                      labelPlacement="end" />
        ]
      }

    return (

    <Grid container
          spacing={ 8 }
          justify="center">
      <Grid container spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 4 }>
          <FormControl className={ classes.formControl }>
            <FormLabel style={ labelStyle } component="label">
              VEN Profile
            </FormLabel>
            <Select autoWidth={ true }
                    style={ { marginTop: "0" } }
                    value={ this.props.venOadrProfile }
                    onChange={ this.props.handleVenOadrProfileChange }
                    inputProps={ { name: 'OpenADR Profile', id: 'oadr_profile_select', } }>
              { profileView }
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={ 4 }>
        <FormControl className={ classes.formControl }>
          <TextField required
                     id="oadr_cn_textfield"
                     fullWidth={ true }
                     label="VEN Common Name"
                     placeholder="myven.oadr.com"
                     value={ this.props.venCommonName }
                     className={ classes.textField }
                     error={ this.props.hasError }
                     onChange={ this.props.handleVenCommonNameChange }
                     InputLabelProps={ { shrink: true, } } />
                 </FormControl>
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>

      <Grid container
            style={ { marginTop: 20 } } 
            spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
        <Divider/>
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>


      <Grid container
            style={ { marginTop: 20 } }
            spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
          <FormControl className={ classes.formControl }>
            <FormLabel style={ labelStyle } component="label">
              SSL Certificate
            </FormLabel>
            <RadioGroup aria-label="position"
                        name="position"
                        value={ this.props.needCertificateGeneration }
                        onChange={ this.props.handleNeedCertificateGenerationChange }
                        row>

              <FormControlLabel value="no" style={{marginLeft:0}}
                                control={ <Radio color="primary" /> }
                                label="Provide VenID fingerprint"
                                labelPlacement="end" />

              {generateOptionView }

              
            </RadioGroup>
          </FormControl>
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>
      

      <Grid container
            style={ { marginTop: 20 } } 
            spacing={ 24 }>
        <Grid item xs={ 2 } />
        <Grid item xs={ 8 }>
        <Divider/>
        </Grid>
        <Grid item xs={ 2 } />
      </Grid>

      <Grid container
            style={ { marginTop: 20 } } 
            spacing={ 24 }>
        <Grid item xs={ 2 } />

        <Grid item xs={ 1 }>
        <HelpIcon color="disabled" style={{width:40, height:40}}/>
        </Grid>

        <Grid item xs={ 6 }>
        <Typography variant="caption" gutterBottom>
          VEN MUST have a valid SSL certificate to securely communicate with VTN. VEN identifier (VenID) is a fingerprint of VEN certificate.
        </Typography>

        <Typography variant="caption" gutterBottom>
          In case VEN certificate is not generated by VTN, user have to provide VenID corresponding to it's certificate fingerprint
        </Typography>

        <Typography variant="caption" gutterBottom>
          In case VEN certificate is generated by VTN, VenID will be computed after certificate generation. 
          Ven Common Name is used as certificate CN subject entry
          Certificate will available for download at the confirmation of this form and only at that time.
          VTN can't re-generate certificate without re-creating a VEN.
        </Typography>

        </Grid>
        <Grid item xs={ 2 } />
      </Grid>
    </Grid>
    );
  }
}

export default VenCreateIndentificationStep;
