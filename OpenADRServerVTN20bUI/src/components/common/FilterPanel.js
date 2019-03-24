
import React from 'react';

import Typography from '@material-ui/core/Typography';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';

import TextField from '@material-ui/core/TextField';

import Grid from '@material-ui/core/Grid';


import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import Select from '@material-ui/core/Select';

import Divider from '@material-ui/core/Divider';
import AddIcon from '@material-ui/icons/Add';

import Button from '@material-ui/core/Button';

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import ExtensionIcon from '@material-ui/icons/Extension';
import GroupWorkIcon from '@material-ui/icons/GroupWork';
import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import ChipInput from 'material-ui-chip-input'
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';
import ExpandMore from '@material-ui/icons/ExpandMore';

import { MarketContextSelectDialog, GroupSelectDialog, VenStatusSelectDialog } from './VtnconfigurationDialog'

var MarketContextChip = (props) => {
  return (
  <span style={ { display: 'flex', alignItems: 'center', marginLeft: '-7px', } }><ExtensionIcon color="disabled" style={ { marginRight: '5px' } }/> { props.name }</span>
  );
}

var GroupChip = (props) => {
  return (
  <span style={ { display: 'flex', alignItems: 'center', marginLeft: '-7px', } }><GroupWorkIcon color="disabled" style={ { marginRight: '5px' } }/> { props.name }</span>
  );
}

var VenStatusChip = (props) => {
  return (
  <span style={ { display: 'flex', alignItems: 'center', marginLeft: '-7px', } }><SettingsInputComponentIcon color="disabled" style={ { marginRight: '5px' } }/> { props.name }</span>
  );
}

export class FilterPanel extends React.Component {

  constructor( props ) {
    super( props )
    this.state = {}
    this.state.marketContextSelectDialogOpen = false;
    this.state.groupSelectDialogOpen = false;
    this.state.venStatusSelectDialogOpen = false;
    
  }

  addFilterFromInput = (f) => {
    this.addFilter({
    	type: "input",
    	value: f
    });
  }

  addFilter = (f) => {
  	var filter = this.props.filter;
    filter.push( f);
    this.props.onFilterChange(filter);
  }

  removeFilter = (f, index) => {
    var filter = this.props.filter;
    filter.splice( index, 1 );
    this.props.onFilterChange(filter);
  }

  handleMarketContextSelectOpen = () => {
    this.setState( {
      marketContextSelectDialogOpen: true
    } )
  }

  handleMarketContextSelectClose = (context) => {
    var params = {
      marketContextSelectDialogOpen: false
    }
    if ( context != null ) {
    	this.addFilter({type: "marketContext", value: context.name});
    }
    this.setState( params )
  }

  handleGroupSelectOpen = () => {
    this.setState( {
      groupSelectDialogOpen: true
    } )
  }

  handleGroupSelectClose = (group) => {
    var params = {
      groupSelectDialogOpen: false
    }
    if ( group != null ) {
    	this.addFilter({type: "group", value: group.name});
    }
    this.setState( params )
  }

  handleVenStatusSelectOpen = () => {
    this.setState( {
      venStatusSelectDialogOpen: true
    } )
  }

  handleVenStatusSelectClose = (status) => {
    var params = {
      venStatusSelectDialogOpen: false
    }
    if ( status != null ) {
      this.addFilter({type: "venStatus", value: status.name});
    }
    this.setState( params )
  }


  render() {
    const {classes, action, hasFilter} = this.props;

    var group = (this.props.group) ? this.props.group : [];
    var marketContext = (this.props.marketContext) ? this.props.marketContext : [];
    var filterChip = [];
    for(var i in this.props.filter) {
    	var chip = null;
    	var filter = this.props.filter[i];
    	switch(filter.type){
    		case "marketContext":
    			chip = <MarketContextChip name={ filter.value } />
    			break; 
    		case "group":
    			chip = <GroupChip name={ filter.value } />
    			break; 
    		case "venStatus":
    			chip = <VenStatusChip name={ filter.value } />
    			break; 
    		case "input":
    			chip = filter.value
    			break; 

    		default:
    			break;
    	}
    	filterChip.push(chip)
    }

    return (


    <span>
    	<ChipInput label="Filters"
            			className={classes.textField}
                       value={ filterChip }
                       onDelete={ this.removeFilter }
                       onAdd={this.addFilterFromInput}
                       fullWidth={true}
                       InputLabelProps={{
				          shrink: true,
				        }}
				        style={{width:"75%"}}
				       />
	
    	{(hasFilter && hasFilter.marketContext) ? <span><IconButton className={ classes.iconButton }
                  aria-label="market_context"
                  onClick={ this.handleMarketContextSelectOpen }>
        <ExtensionIcon />
        <ExpandMore />
        
      </IconButton><MarketContextSelectDialog marketContext={ marketContext}
                                 open={ this.state.marketContextSelectDialogOpen }
                                 close={ this.handleMarketContextSelectClose }
                                 title="Filter by Market Context" /></span>: null}

      {(hasFilter && hasFilter.group) ? <span><IconButton className={ classes.iconButton }
                  aria-label="group"
                  onClick={ this.handleGroupSelectOpen }>
        <GroupWorkIcon />
        <ExpandMore />
        
      </IconButton><GroupSelectDialog group={ group }
                         open={ this.state.groupSelectDialogOpen }
                         close={ this.handleGroupSelectClose }
                         title="Filter by Group" /></span> : null}

      {(hasFilter && hasFilter.venStatus) ? <span><IconButton className={ classes.iconButton }
                  aria-label="group"
                  onClick={ this.handleVenStatusSelectOpen }>
        <SettingsInputComponentIcon />
        <ExpandMore />
      </IconButton><VenStatusSelectDialog open={ this.state.venStatusSelectDialogOpen }
                         close={ this.handleVenStatusSelectClose }
                         title="Filter by Ven Status" /></span> : null}
    </span>
    );
  }
}

export default FilterPanel;
