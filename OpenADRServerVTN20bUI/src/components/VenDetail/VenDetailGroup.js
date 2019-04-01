import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles, MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

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

import VenDetailHeader from './VenDetailHeader'

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';


import { history } from '../../store/configureStore';

import {iCalDurationInSeconds, formatTimestamp} from '../../utils/time'
import {isActionReport, isHistoryReport, isTelemetryReport} from '../../utils/venReport'


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


export class VenDetailGroup extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {};
    this.state.groupSelectDialogOpen = false;
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



  handleRemoveVenGroup = (group) => {
    return () => {
      console.log( group )
      this.props.removeVenGroup( this.props.ven.username, group.id )
    }
  }


  render() {
    const {classes, ven,  group, venGroup} = this.props;

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
      <VenDetailHeader classes={classes} ven={ven} actions={[
 
      ]
      }/>
      <Divider style={ { marginTop: '20px'} } />
      { /* Group Row */ }
      <Grid container>
        <Grid container>
          <Grid item xs={ 8 }>
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
                    fullWidth={true}
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
      </Grid>

     
    </div>
    );
  }
}

export default VenDetailGroup;
