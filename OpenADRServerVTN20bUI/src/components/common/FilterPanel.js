
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
import CalendarTodayIcon from '@material-ui/icons/CalendarToday';

import ChipInput from 'material-ui-chip-input'
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';
import ExpandMore from '@material-ui/icons/ExpandMore';

import { MarketContextSelectDialog, GroupSelectDialog, VenStatusSelectDialog, VenSelectDialog, EventStatusSelectDialog, EventSelectDialog } from './VtnconfigurationDialog'

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

var VenChip = (props) => {
  return (
  <span style={ { display: 'flex', alignItems: 'center', marginLeft: '-7px', } }><SettingsInputComponentIcon color="disabled" style={ { marginRight: '5px' } }/> { props.name }</span>
  );
}

var EventChip = (props) => {
  return (
  <span style={ { display: 'flex', alignItems: 'center', marginLeft: '-7px', } }><CalendarTodayIcon color="disabled" style={ { marginRight: '5px' } }/> { props.name }</span>
  );
}

export class FilterPanel extends React.Component {

  constructor( props ) {
    super( props )
    this.state = {}
    this.state.marketContextSelectDialogOpen = false;
    this.state.groupSelectDialogOpen = false;
    this.state.venStatusSelectDialogOpen = false;
    this.state.venSelectDialogOpen = false;
    this.state.eventStatusSelectDialogOpen = false;
    this.state.eventSelectDialogOpen = false;
    
  }

  addFilterFromInput = (f) => {
    this.addFilter({
    	type: this.props.type,
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
    	this.addFilter({type: "MARKET_CONTEXT", value: context.name});
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
    	this.addFilter({type: "GROUP", value: group.name});
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
      this.addFilter({type: "IS_REGISTERED", value: status.name == "online"});
    }
    this.setState( params )
  }

  handleVenSelectOpen = () => {
    this.setState( {
      venSelectDialogOpen: true
    } )
  }

  handleVenSelectClose = (ven) => {
    var params = {
      venSelectDialogOpen: false
    }
    if ( ven != null ) {
      this.addFilter({type: "VEN", value: ven.username});
    }
    this.setState( params );
  }

  handleEventStatusSelectOpen = () => {
    this.setState( {
      eventStatusSelectDialogOpen: true
    } )
  }

  handleEventStatusSelectClose = (status) => {
    var params = {
      eventStatusSelectDialogOpen: false
    }
    if ( status != null && (status.name == "ACTIVE" || status.name == "CANCELLED")) {
      this.addFilter({type: "EVENT_STATE", value: status.name});
    } else  if ( status != null && (status.name == "NOT_PUBLISHED" || status.name == "PUBLISHED")) {
      this.addFilter({type: "EVENT_PUBLISHED", value: status.name});
    }
    this.setState( params );
  }

  handleEventSelectOpen = () => {
    this.setState( {
      eventSelectDialogOpen: true
    } )
  }

  handleEventSelectClose = (event) => {
    var params = {
      eventSelectDialogOpen: false
    }
    if ( event != null ) {
      this.addFilter({type: "EVENT", value: event.descriptor.eventId});
    }
    this.setState( params );
  }


  render() {
    const {classes, action, hasFilter} = this.props;

    var group = (this.props.group) ? this.props.group : [];
    var marketContext = (this.props.marketContext) ? this.props.marketContext : [];
    var ven = (this.props.ven) ? this.props.ven : [];
    var event = (this.props.event) ? this.props.event : [];
    var filterChip = [];
    for(var i in this.props.filter) {
    	var chip = null;
    	var filter = this.props.filter[i];
    	switch(filter.type){
    		case "MARKET_CONTEXT":
    			chip = <MarketContextChip name={ filter.value } />
    			break; 
    		case "GROUP":
    			chip = <GroupChip name={ filter.value } />
    			break; 
    		case "IS_REGISTERED":
    			chip = <VenChip name={ filter.value } />
    			break; 
    		case "VEN":
    			chip = <VenChip name={ filter.value } />
    			break; 
        case "EVENT":
          chip = <EventChip name={ filter.value } />
          break; 
        case "EVENT_STATE":
          chip = <EventChip name={ filter.value } />
          break; 
        case "EVENT_PUBLISHED":
          chip = <EventChip name={ filter.value } />
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


      {(hasFilter && hasFilter.ven) ? <span><IconButton className={ classes.iconButton }
                  aria-label="ven"
                  onClick={ this.handleVenSelectOpen }>
        <SettingsInputComponentIcon />
        <ExpandMore />
      </IconButton><VenSelectDialog open={ this.state.venSelectDialogOpen }
                        suggestions={ven}
                        onSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
                        onSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
                        onSuggestionsSelect={this.handleVenSelectClose}
                        close={ this.handleVenSelectClose }
                        title="Filter by Ven" /></span> : null}

      {(hasFilter && hasFilter.eventStatus) ? <span><IconButton className={ classes.iconButton }
                  aria-label="eventStatus"
                  onClick={ this.handleEventStatusSelectOpen }>
        <CalendarTodayIcon />
        <ExpandMore />
      </IconButton><EventStatusSelectDialog open={ this.state.eventStatusSelectDialogOpen }
                         close={ this.handleEventStatusSelectClose }
                         title="Filter by Event Status" /></span> : null}

      {(hasFilter && hasFilter.event) ? <span><IconButton className={ classes.iconButton }
                  aria-label="eventStatus"
                  onClick={ this.handleEventSelectOpen }>
        <CalendarTodayIcon />
        <ExpandMore />
      </IconButton><EventSelectDialog open={ this.state.eventSelectDialogOpen }
                            suggestions={event}
                        onSuggestionsFetchRequested={this.props.onEventSuggestionsFetchRequested}
                        onSuggestionsClearRequested={this.props.onEventSuggestionsClearRequested}
                        onSuggestionsSelect={this.handleEventSelectClose}
                         close={ this.handleEventSelectClose }
                         title="Filter by Event Status" /></span> : null}
    </span>
    );
  }
}

export default FilterPanel;
