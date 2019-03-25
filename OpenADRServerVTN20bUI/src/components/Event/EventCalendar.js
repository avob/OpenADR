import React from 'react';

import Typography from '@material-ui/core/Typography';
import BigCalendar from 'react-big-calendar'
import moment from 'moment'
import Divider from '@material-ui/core/Divider';

import style from 'react-big-calendar/lib/css/react-big-calendar.css'

import {iCalDurationInSeconds} from '../../utils/time'

import { EventCalendarDialog } from '../common/VtnconfigurationDialog'

import { history } from '../../store/configureStore';

import EventHeader from './EventHeader'

import amber from '@material-ui/core/colors/amber';
import red from '@material-ui/core/colors/red';
import green from '@material-ui/core/colors/green';




const localizer = BigCalendar.momentLocalizer(moment)

export class EventCalendar extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    this.state.eventCalendarDialog = false;
    this.state.selectedEvent = null;
  }

  handleEventCalendarDialogClose = () =>  {
    this.setState({eventCalendarDialog: false});
  }

  handleEditEvent = (id) => () => {
    history.push( '/event/detail/'+ id);
  }

  getEventPropGetter = (event, start, end, isSelected) => {
    var colorIntensity = 700;
    if(isSelected) {
      colorIntensity = 500;
    }

    var color;
    if(!event.published) {
      color = amber[colorIntensity];
    }
    else if(event.state == "ACTIVE"){
      color = green[colorIntensity];
    }
    else if(event.state == "CANCELED") {
      color = red[colorIntensity];
    }

    
    return {
      style: {backgroundColor: color}
    }
  }

  handleSelectEvent = (event) => {
    this.setState({eventCalendarDialog: true, selectedEvent:event});
  }

  handleEventDetailClick = (id) => () => {
    history.push( '/event/detail/'+ id);
  }

  render() {
    const {classes, marketContext, event, ven, filters, pagination, onFilterChange, onPaginationChange,
      start, end, onStartChange, onEndChange, onVenSuggestionsFetchRequested, onVenSuggestionsClearRequested, onVenSuggestionsSelect} = this.props;

    var calendarEvent = [];
    event.forEach(e => {
      var start = new Date();
      start.setTime(e.activePeriod.start);
      var end = new Date();
      end.setTime(e.activePeriod.start + iCalDurationInSeconds(e.activePeriod.duration) * 1000);
      calendarEvent.push({
        title:e.descriptor.eventId,
        start: start,
        end: end,
        allday:false,
        state: e.descriptor.state,
        published: e.published,
        marketContext:e.descriptor.marketContext,
        id:e.id
      });
    });

    return (
      <div className={ classes.root }>
        <EventHeader classes={classes}  marketContext={marketContext} event={event}
        filters={filters} pagination={pagination} onFilterChange={onFilterChange} onPaginationChange={onPaginationChange}
        start={start} end={end} onStartChange={onStartChange} onEndChange={onEndChange} 
        ven={ven}
                onVenSuggestionsFetchRequested={onVenSuggestionsFetchRequested}
                onVenSuggestionsClearRequested={onVenSuggestionsClearRequested}
                onVenSuggestionsSelect={onVenSuggestionsSelect}/>
        <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
        <BigCalendar style={{height:600}}
          localizer={localizer}
          events={calendarEvent}
          defaultView={BigCalendar.Views.WEEK}
          startAccessor="start"
          endAccessor="end"
          eventPropGetter={this.getEventPropGetter}
          onSelectEvent={this.handleSelectEvent}
        />

        {(this.state.selectedEvent) ? <EventCalendarDialog classes={classes} title={(this.state.selectedEvent) ? this.state.selectedEvent.title : ""}
        event={this.state.selectedEvent}
        open={this.state.eventCalendarDialog}
        close={this.handleEventCalendarDialogClose}
        handleEventDetailClick={this.handleEventDetailClick(this.state.selectedEvent.id)}/> : null}
     
      </div>
    );
  }
}

export default EventCalendar;
