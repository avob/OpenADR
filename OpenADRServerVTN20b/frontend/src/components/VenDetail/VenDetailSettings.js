import React from 'react';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Divider from '@material-ui/core/Divider';
import Button from '@material-ui/core/Button';
import RemoveIcon from '@material-ui/icons/Remove';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import VenDetailHeader from './VenDetailHeader'
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';


import {  VtnTextField, VtnFeatureField } from '../common/TextField'



export class VenDetailSettings extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }

  render() {
    const {classes, ven, venActions} = this.props;

    console.log(ven)
    if(!ven || !ven.username) {
      return <div className={ classes.root } />;
    }
    return (
    <div className={ classes.root }>
        
        <div  style={{margin: "0px 5%"}}>
        <FormControl fullWidth >
        <FormLabel>Identitication</FormLabel>
      <FormGroup aria-label="position" row>
          <Grid container spacing={24}>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Common Name" value={ ven.commonName } />
          </Grid>
       
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="VenID" value={ ven.username } />
          </Grid>
 </Grid>
           </FormGroup>
   </FormControl>
       </div>
        <Divider style={{margin: "20px 0"}}/>


       <div  style={{margin: "0px 5%"}}>
        <FormControl fullWidth>
        <FormLabel>Registration</FormLabel>
      <FormGroup aria-label="position" row>
           <Grid container spacing={24}>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Oadr name" value={ ven.oadrName } />
          </Grid>
          
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="RegistrationID" value={ ven.registrationId } />
          </Grid>
            </Grid>
           </FormGroup>
           <FormGroup aria-label="position" row>
          <Grid container spacing={24}>
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Transport" value={ ven.transport } />
          </Grid>
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Mode" value={ ven.httpPullModel ? "PULL" : "PUSH" } />
          </Grid>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Push URL" value={ ven.pushUrl } />
          </Grid>
            </Grid>
           </FormGroup>
           <FormGroup aria-label="position" row>
        <Grid container spacing={24}>
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Report Only" value={ ven.reportOnly ? "Report only" : "Event / Report" } />
          </Grid>
           <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Xml Signature" value={ ven.xmlSignature ? "Xml signature" : "No xml signature" } />
          </Grid>
            </Grid>
           </FormGroup>
   </FormControl>
   </div>


    </div>
    );
  }
}

export default VenDetailSettings;
