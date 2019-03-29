import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles } from '@material-ui/core/styles';

import { VtnConfigurationVenCard, VtnConfigurationMarketContextCard, VtnConfigurationGroupCard } from '../common/VtnConfigurationCard'
import { MarketContextSelectDialog, GroupSelectDialog } from '../common/VtnconfigurationDialog'

import Input from '@material-ui/core/Input';
import InputBase from '@material-ui/core/InputBase';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';


import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';


import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import GridListTileBar from '@material-ui/core/GridListTileBar';

import IconButton from '@material-ui/core/IconButton';

import StarBorderIcon from '@material-ui/icons/StarBorder';

import ChipInput from 'material-ui-chip-input'

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import SearchIcon from '@material-ui/icons/Search';


import SnackbarContent from '@material-ui/core/SnackbarContent';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

import green from '@material-ui/core/colors/green';

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
