import React from 'react';












import TextField from '@material-ui/core/TextField';

import Grid from '@material-ui/core/Grid';




import Divider from '@material-ui/core/Divider';












import Button from '@material-ui/core/Button';

import RemoveIcon from '@material-ui/icons/Remove';






import CloudDownloadIcon from '@material-ui/icons/CloudDownload';



import VenDetailHeader from './VenDetailHeader'





var VenTextField = (props) => {
  var value = (props.value != null) ? props.value : "";
  return (
  <TextField label={ props.field }
             value={ value }
             className={ props.className }
             margin="normal"
             fullWidth={true}
             InputProps={ { readOnly: true, } } />

  );
}




export class VenDetailSettings extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
   
    


  }



  

  handleReRegistrationClick = () => {
    this.props.registerPartyRequestReregistration( this.props.ven.username);
  }

  handleCancelRegistrationClick = () => {
    this.props.registerPartyCancelPartyRegistration( this.props.ven.username);
  }

  handleCleanRegistrationClick = () => {
    this.props.cleanRegistration( this.props.ven.username);
  }


  render() {
    const {classes, ven} = this.props;

    

    return (
    <div className={ classes.root } >
      <VenDetailHeader classes={classes} ven={ven} actions={
        <Grid container spacing={ 12 }>
          <Grid item xs={ 12 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleReRegistrationClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> RE-REGISTRATION
            </Button>
          </Grid>
          <Grid item xs={ 6 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="secondary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleCancelRegistrationClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> CANCEL REGISTRATION
            </Button>
          </Grid>
          <Grid item xs={ 6 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="secondary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleCleanRegistrationClick}>
              <RemoveIcon style={ { marginRight: 15 } }/> CLEAN REGISTRATION
            </Button>
          </Grid>
      </Grid>
      }/>

      <Divider style={ { marginTop: '20px' } } />
       <Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="VenID" value={ ven.username } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Authentication Method" value={ ven.authenticationType } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Common Name" value={ ven.commonName } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Xml Signature" value={ ven.oadrProfil } />
          </Grid>
        </Grid>
      </Grid>

      <Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Oadr name" value={ ven.oadrName } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Transport" value={ ven.transport } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="RegistrationID" value={ ven.registrationId } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Push Url" value={ ven.pushUrl } />
          </Grid>
        </Grid>
      </Grid>
      <Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Pull Model" value={ ven.httpPullModel } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Report Only" value={ ven.reportOnly } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Xml Signature" value={ ven.xmlSignature } />
          </Grid>
          <Grid item xs={ 3 }>

          </Grid>
        </Grid>
      </Grid>
    </div>
    );
  }
}

export default VenDetailSettings;
