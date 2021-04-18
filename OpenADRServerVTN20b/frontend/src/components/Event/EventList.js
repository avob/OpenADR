import React from 'react';

import { history } from '../../store/configureStore';


import moment from 'moment'

import EnhancedTable  from '../common/EnhancedTable'
import TableCell from '@material-ui/core/TableCell';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import DeleteIcon from '@material-ui/icons/Delete';
import { formatTimestamp} from '../../utils/time'

import Grid from '@material-ui/core/Grid';


import FilterPanel from '../common/FilterPanel' 
import AddIcon from '@material-ui/icons/Add';




export class EventList extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {};
    
  }

  handlePaginationChange = (pagination) => {
   this.setState( {
      pagination
    } );
  }

  handleSortChange = (sort) => {
   this.setState( {
      sort
    } );
  }

  handleEditEvent = (id) => () => {
    history.push( '/event/detail/'+ id);
  }

  handleDeleteEvent = (id) => () => {
    this.props.deleteEvent(id)
  }

  handleViewChange = (view) => () => {
    this.props.onViewChange(view);
  } 

  getTitle = () => {
    switch(this.props.view) {
      case "month":
        return moment(this.props.currentDate).format( "YYYY MMMM");

      case "week":
        let start = moment(this.props.currentDate).startOf(this.props.view);
        let end = moment(this.props.currentDate).endOf(this.props.view);
        return start.format( "MMM Do") + " - " + end.format( "MMM Do");

      case "day":
        return moment(this.props.currentDate).format( "dddd MMM DD")

      default:
        return moment(this.props.currentDate).format( "YYYY MMMM");
    }
  }


  getNext = () => {
    var d = moment(this.props.currentDate).add(1, this.props.view);
    var date = d.toDate();
    this.props.onCurrentDateChange(date);
  }

  getBack = () => {
    var d = moment(this.props.currentDate).add(-1, this.props.view);
    var date = d.toDate();
    this.props.onCurrentDateChange(date);
  }

  getToday = () => {
    var date = new Date();
    this.props.onCurrentDateChange(date);
  }

  getFormatedDatetime = (datetime) => {
    var format = formatTimestamp(datetime);
    return (
      <span>{format.date} {format.time} {format.tz}</span>
    );
  }

  handleCreateEventClick = () => {
    history.push( '/event/create' )
  }

  render() {
    const {classes, marketContext, event, ven, filters, sort, pagination, onFilterChange, onSortChange, onPaginationChange,
      onVenSuggestionsFetchRequested, onVenSuggestionsClearRequested, onVenSuggestionsSelect, total, start, end, onStartChange, onEndChange} = this.props;

    return (
      <div className={ classes.root }>
        <EnhancedTable 
        title="Events"
        data={event}
        total={total}
        pagination={pagination}
        sort={sort}
        handlePaginationChange={onPaginationChange}
        handleSortChange={onSortChange}
        handleClick={n => {history.push( '/event/detail/' + n.id )}}
        rows={[
          { id: 'id', numeric: false, disablePadding: true, label: 'Id'},
          { id: 'descriptor.marketContext', numeric: false, disablePadding: true, label: 'MarketContext'},
          { id: 'activePeriod.start', numeric: false, disablePadding: false, label: 'Start' },
          { id: 'activePeriod.duration', numeric: false, disablePadding: false, label: 'Duration' },
          { id: 'lastUpdateTimestamp', numeric: false, disablePadding: false, label: 'Last Update' },
        ]} 
        rowTemplate={n => {
          return <React.Fragment>
            <TableCell align="justify">{n.id}</TableCell>
            <TableCell align="justify">{n.descriptor.marketContext}</TableCell>
            <TableCell>{(n.activePeriod.start) ? this.getFormatedDatetime(n.activePeriod.start) : null}</TableCell>
            <TableCell>{(n.activePeriod.duration) ? n.activePeriod.duration : null}</TableCell>
            <TableCell>{(n.lastUpdateTimestamp) ? this.getFormatedDatetime(n.lastUpdateTimestamp) : null}</TableCell>
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
            

                     <Tooltip title="New" onClick={ this.handleCreateEventClick }>
            <IconButton aria-label="New">
              <AddIcon />
            </IconButton>
          </Tooltip>
          
                    
          </React.Fragment>
        }}
        filter={filters}
        filterPanel={() => {
          return <Grid container>
        <Grid item xs={ 12 }><FilterPanel classes={classes} type="EVENT" hasFilter={{marketContext:true, ven: true, eventStatus:true, date: true}} 
                marketContext={marketContext}
                filter={filters}
                start={start}
                end={end}
                onFilterChange={onFilterChange}
                onStartChange={onStartChange}
                onEndChange={onEndChange}
                ven={ven}
                onVenSuggestionsFetchRequested={onVenSuggestionsFetchRequested}
                onVenSuggestionsClearRequested={onVenSuggestionsClearRequested}
                onVenSuggestionsSelect={  onVenSuggestionsSelect}
                /></Grid>
      </Grid>
        }}
        />
      </div>
    );
  }
}

export default EventList;
