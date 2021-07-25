import React from 'react';
import Grid from '@material-ui/core/Grid';
import Divider from '@material-ui/core/Divider';
import FormGroup from '@material-ui/core/FormGroup';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';


import {  VtnTextField } from '../common/TextField'
import {Panel} from '../common/Structure'



export class VenDetailSettings extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }

  render() {
    const {classes, ven} = this.props;

    if(!ven || !ven.username) {
      return <div className={ classes.root } />;
    }
    return (
    <div className={ classes.root }>
        
         <Panel classes={classes}  title="Identitication">

          <Grid container spacing={24}>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Common Name" value={ ven.commonName } />
          </Grid>
       
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="VenID" value={ ven.username } />
          </Grid>
 </Grid>

       </Panel>
        <Divider/>


        <Panel classes={classes}  title="Registration">

           <Grid container spacing={24}>
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="Oadr name" value={ ven.oadrName } />
          </Grid>
          
          <Grid item xs={ 6 }>
            <VtnTextField className={ classes.textField } field="RegistrationID" value={ ven.registrationId } />
          </Grid>
            </Grid>
   
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
  

        <Grid container spacing={24}>
          <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Report Only" value={ ven.reportOnly ? "Report only" : "Event / Report" } />
          </Grid>
           <Grid item xs={ 3 }>
            <VtnTextField className={ classes.textField } field="Xml Signature" value={ ven.xmlSignature ? "Xml signature" : "No xml signature" } />
          </Grid>
            </Grid>

   </Panel>


    </div>
    );
  }
}

export default VenDetailSettings;
