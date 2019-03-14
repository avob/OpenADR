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

var MarketContextGridList = (props) => {
  return (
  <div className={ props.classes.root }>
    <GridList className={ props.classes.gridList }
              cols={ 3 }
              spacing={ 0 }
              cellHeight="auto">
      { props.marketContext.map( context => (
          <GridListTile key={ context.id } className={ props.classes.tile }>
            <VtnConfigurationMarketContextCard classes={ props.classes }
                                               context={ context }
                                               handleRemoveVenMarketContext={ props.handleRemoveVenMarketContext( context ) } />
          </GridListTile>
        ) ) }
    </GridList>
  </div>
  );
}

var GroupGridList = (props) => {

  return (
  <div className={ props.classes.root }>
    <GridList className={ props.classes.gridList }
              cols={ 3 }
              spacing={ 0 }
              cellHeight="auto">
      { props.group.map( g => (
          <GridListTile key={ g.id } className={ props.classes.tile }>
            <VtnConfigurationGroupCard classes={ props.classes }
                                       group={ g }
                                       handleRemoveVenGroup={ props.handleRemoveVenGroup( g ) } />
          </GridListTile>
        ) ) }
    </GridList>
  </div>
  );
}


export class VenDetailSettings extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    this.state.marketContextSelectDialogOpen = false;
    this.state.groupSelectDialogOpen = false;


  }

  handleMarketContextSelectOpen = () => {
    this.setState( {
      marketContextSelectDialogOpen: true
    } );
  }

  handleMarketContextSelectClose = (context) => {
    if ( context ) {
      this.props.addVenMarketContext( this.props.ven.username, context.id )
    }
    this.setState( {
      marketContextSelectDialogOpen: false
    } );
  }

  handleGroupSelectOpen = () => {
    this.setState( {
      groupSelectDialogOpen: true
    } );
  }

  handleGroupSelectClose = (group) => {
    console.log( group )
    if ( group ) {
      this.props.addVenGroup( this.props.ven.username, group.id )
    }
    this.setState( {
      groupSelectDialogOpen: false
    } );
  }

  handleRemoveVenMarketContext = (context) => {
    return () => {
      console.log( context )
      this.props.removeVenMarketContext( this.props.ven.username, context.id )
    }
  }

  handleRemoveVenGroup = (group) => {
    return () => {
      console.log( group )
      this.props.removeVenGroup( this.props.ven.username, group.id )
    }
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
    const {classes, ven, marketContext, group, venMarketContext, venGroup} = this.props;

    var notSubscribedMarketContext = [];
    var venMarketContextId = [];
    for (var i in venMarketContext) {
      venMarketContextId.push( venMarketContext[ i ].id );
    }
    for (var i in marketContext) {
      if ( venMarketContextId.indexOf( marketContext[ i ].id ) == -1 ) {
        notSubscribedMarketContext.push( marketContext[ i ] )
      }
    }

    var notAddedGroup = [];
    var venGroupId = [];
    for (var i in venGroup) {
      venGroupId.push( venGroup[ i ].id );
    }
    for (var i in group) {
      if ( venGroupId.indexOf( group[ i ].id ) == -1 ) {
        notAddedGroup.push( group[ i ] )
      }
    }

    return (
    <div className={ classes.root } >
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Status
      </Typography>
      <VenDetailHeader classes={classes} ven={ven} actions={
        <Grid container spacing={ 24 }>
          <Grid item xs={ 4 }>
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
          <Grid item xs={ 4 }>
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
          <Grid item xs={ 4 }>
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

      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Settings
      </Typography>
      <Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Transport" value={ ven.transport } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Authentication Method" value={ ven.authenticationType } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Pull Model" value={ ven.httpPullModel } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Push Url" value={ ven.pushUrl } />
          </Grid>
        </Grid>
      </Grid>
      <Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="RegistrationID" value={ ven.registrationId } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="VenID" value={ ven.username } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Report Only" value={ ven.reportOnly } />
          </Grid>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Xml Signature" value={ ven.xmlSignature } />
          </Grid>
        </Grid>
      </Grid>
      <Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <VenTextField className={ classes.textField } field="Oadr name" value={ ven.oadrName } />
          </Grid>
        </Grid>
      </Grid>
      { /* MarketContext Row */ }
      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Market Context
      </Typography>
      <Grid container spacing={ 24 }>
        <Grid item xs={ 6 }>
          <ChipInput label="Filters"
                     placeholder="Filters"
                     value={ this.state.filter }
                     onAdd={ this.handleAddChip }
                     onDelete={ this.handleDeleteChip }
                     fullWidth={ true } />
        </Grid>
        <Grid item xs={ 1 }>
          <IconButton className={ classes.iconButton } aria-label="Search">
            <SearchIcon />
          </IconButton>
        </Grid>
        <Grid item xs={ 3 }>
          <Button key="btn_create"
                  style={ { marginTop: 15 } }
                  variant="outlined"
                  color="primary"
                  size="small"
                  className={ classes.button }
                  onClick={ this.handleMarketContextSelectOpen }>
            <AddIcon />Subscribe to a Market Context
          </Button>
          <MarketContextSelectDialog marketContext={ notSubscribedMarketContext }
                                     open={ this.state.marketContextSelectDialogOpen }
                                     close={ this.handleMarketContextSelectClose }
                                     title="Add VEN to Market Context" />
        </Grid>
      </Grid>
      <Grid container spacing={ 24 }>
        <Grid item xs={ 12 }>
          <MarketContextGridList classes={ classes }
                                 marketContext={ venMarketContext }
                                 handleRemoveVenMarketContext={ this.handleRemoveVenMarketContext } />
        </Grid>
      </Grid>
      { /* Group Row */ }
      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Group
      </Typography>
      <Grid container spacing={ 24 }>
        <Grid item xs={ 6 }>
          <ChipInput label="Filters"
                     placeholder="Filters"
                     value={ this.state.filter }
                     onAdd={ this.handleAddChip }
                     onDelete={ this.handleDeleteChip }
                     fullWidth={ true } />
        </Grid>
        <Grid item xs={ 1 }>
          <IconButton className={ classes.iconButton } aria-label="Search">
            <SearchIcon />
          </IconButton>
        </Grid>
        <Grid item xs={ 3 }>
          <Button key="btn_create"
                  style={ { marginTop: 15 } }
                  variant="outlined"
                  color="primary"
                  size="small"
                  className={ classes.button }
                  onClick={ this.handleGroupSelectOpen }>
            <AddIcon />Add to a Group
          </Button>
          <GroupSelectDialog group={ notAddedGroup }
                             open={ this.state.groupSelectDialogOpen }
                             close={ this.handleGroupSelectClose }
                             title="Add VEN to group:" />
        </Grid>
      </Grid>
      <Grid container spacing={ 24 }>
        <Grid item xs={ 12 }>
          <GroupGridList classes={ classes }
                         group={ venGroup }
                         handleRemoveVenGroup={ this.handleRemoveVenGroup } />
        </Grid>
      </Grid>
    </div>
    );
  }
}

export default VenDetailSettings;
