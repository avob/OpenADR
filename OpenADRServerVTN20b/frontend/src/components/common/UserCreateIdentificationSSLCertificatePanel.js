

import React from 'react';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormControlLabel from '@material-ui/core/FormControlLabel';




import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';



const labelStyle = {

  boxSizing: 'border-box',
  color: 'rgba(0, 0, 0, 0.54)',
  fontSize: '1rem',
  fontWeight: 400,

  lineHeight: 1,
  transition: 'color 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms,transform 200ms cubic-bezier(0.0, 0, 0.2, 1) 0ms',
  transform: 'translate(0, 1.5px) scale(0.75)',
  transformOrigin: 'top left',
  top: 0,
  left: 0,
}

export var UserCreateIdentificationSSLCertificatePanel = (props) => {
  const {classes, identification, vtnConfiguration} = props;

  var handleNeedCertificateGenerationChange = (e) => {
    var identification = props.identification;
    identification.needCertificateGeneration = e.target.value;
    props.onChange(identification);
  };

    var generateOptionView = null;
    if ( vtnConfiguration && vtnConfiguration.supportCertificateGeneration ) {
      generateOptionView = [
        <FormControlLabel key="generate_rsa_radio"
                          value="rsa"
                          control={ <Radio color="primary" /> }
                          label="Generate RSA certificate"
                          labelPlacement="end" />,
        <FormControlLabel key="generate_ecc_radio"
                          value="ecc"
                          control={ <Radio color="primary" /> }
                          label="Generate ECC certificate"
                          labelPlacement="end" />
      ]
    }

    return (

    <FormControl className={ classes.formControl }>
        <FormLabel style={ labelStyle } component="label">
          SSL Certificate
        </FormLabel>
        <RadioGroup aria-label="position"
                    name="position"
                    value={ identification.needCertificateGeneration }
                    onChange={ handleNeedCertificateGenerationChange }
                    row>
          <FormControlLabel value="no"
                            style={ { marginLeft: 0 } }
                            control={ <Radio color="primary" /> }
                            label="Provide Username"
                            labelPlacement="end" />
          { generateOptionView }
        </RadioGroup>
      </FormControl>
    );
} 

export default UserCreateIdentificationSSLCertificatePanel;