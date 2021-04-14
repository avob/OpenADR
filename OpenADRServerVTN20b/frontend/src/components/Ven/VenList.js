import React from 'react';

import Divider from '@material-ui/core/Divider';

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';

import GridList from '@material-ui/core/GridList';

import { VtnConfigurationVenCard } from '../common/VtnConfigurationCard'

import { history } from '../../store/configureStore';
import Grid from '@material-ui/core/Grid';


import FilterPanel from '../common/FilterPanel' 

import EnhancedTable  from '../common/EnhancedTable'
import TableCell from '@material-ui/core/TableCell';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import DeleteIcon from '@material-ui/icons/Delete';
import FilterListIcon from '@material-ui/icons/FilterList';
import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import Avatar from '@material-ui/core/Avatar';
import Typography from '@material-ui/core/Typography';
import { formatTimestamp} from '../../utils/time'

export class VenList extends React.Component {
  constructor( props ) {
    super( props );
    
  }

  

  handleDeleteVen = (username) => {
    var that = this;
    return function ( event ) {
      event.preventDefault();
      that.props.deleteVen( username )

    }

  }

  handleEditVen = (username) => {
    return function ( event ) {
      event.preventDefault();
      history.push( '/ven/detail/' + username )
    }
  }

  handleCreateVENClick = () => {
    history.push( '/ven/create' )
  }

  getFormatedDatetime = (datetime) => {
    var format = formatTimestamp(datetime);
    return (
      <span>{format.date} {format.time} {format.tz}</span>
    );
  }

   handleEventClick = (username) => {
    return (e) => { e.preventDefault(); history.push( '/event', { filters: [{type:"VEN", value: username}] }); }
  }

  render() {
    const {classes, ven, marketContext, group, event, total} = this.props;
    return (
    <div className={ classes.root }>
       <EnhancedTable 
        title="VENs"
        data={ven}
        total={total}
        pagination={this.props.pagination}
        sort={this.props.sort}
        handlePaginationChange={this.props.onPaginationChange}
        handleSortChange={this.props.onSortChange}
        handleClick={n => {history.push( '/ven/detail/' + n.username )}}
        rows={[
          { id: 'commonName', numeric: false, disablePadding: true, label: 'Name'},
          { id: 'lastUpdateDatetime', numeric: false, disablePadding: false, label: 'Last Update' },
          { id: 'registrationId', numeric: false, disablePadding: false, label: 'Status' },
        ]} 
        rowTemplate={n => {
          return <React.Fragment>
            <TableCell align="justify">{n.commonName}</TableCell>
            <TableCell>{(n.lastUpdateDatetime) ? this.getFormatedDatetime(n.lastUpdateDatetime) : null}</TableCell>
            <TableCell align="right"><Avatar style={ { backgroundColor: (n.registrationId != null) ? "green" : "#bbb" , width: "15px", height: "15px"}  }/></TableCell>
          </React.Fragment>
        }}
        actionSelected={() => {
          return <React.Fragment>
            <Tooltip title="Delete">
            <IconButton aria-label="Delete">
              <DeleteIcon />
            </IconButton>
          </Tooltip>
          </React.Fragment>
        }}
        action={() => {
          return <React.Fragment>
            

                     <Tooltip title="New" onClick={ this.handleCreateVENClick }>
            <IconButton aria-label="New">
              <AddIcon />
            </IconButton>
          </Tooltip>
          
                    
          </React.Fragment>
        }}
        filter={this.props.filters}
        filterPanel={() => {
          return <Grid container>
        <Grid item xs={ 12 }><FilterPanel classes={classes}  type="VEN" hasFilter={{group:true, marketContext:true, venStatus:true, event:true}} 
            group={group}
            marketContext={marketContext}
            filter={this.props.filters}
            onFilterChange={this.props.onFilterChange}

            event={event}
            onEventSuggestionsFetchRequested={this.props.onEventSuggestionsFetchRequested}
            onEventSuggestionsClearRequested={this.props.onEventSuggestionsClearRequested}
            onEventSuggestionsSelect={this.props.onEventSuggestionsSelect}/> </Grid>
      </Grid>
        }}
        />
        
    </div>
    );
  }
}

export default VenList;
