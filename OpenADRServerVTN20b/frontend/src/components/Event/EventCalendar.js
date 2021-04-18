import React from 'react';


import BigCalendar from 'react-big-calendar'
import moment from 'moment'
// required for react-big-calendar
// eslint-disable-next-line
import style from 'react-big-calendar/lib/css/react-big-calendar.css'

import {iCalDurationInSeconds} from '../../utils/time'

import { EventCalendarDialog } from '../common/VtnconfigurationDialog'

import { history } from '../../store/configureStore';

import amber from '@material-ui/core/colors/amber';
import red from '@material-ui/core/colors/red';
import green from '@material-ui/core/colors/green';

import EventCalendarWeekView from './EventCalendarView/EventCalendarWeekView'
import EventCalendarDayView from './EventCalendarView/EventCalendarDayView'
import EventCalendarMonthView from './EventCalendarView/EventCalendarMonthView'
import EventCalendarAgendaView from './EventCalendarView/EventCalendarAgendaView'

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import FilterListIcon from '@material-ui/icons/FilterList';
import FilterPanel from '../common/FilterPanel' 

const localizer = BigCalendar.momentLocalizer(moment)


var marketContextColorCache = {};
export class EventCalendar extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    this.state.eventCalendarDialog = false;
    this.state.selectedEvent = null;
    this.state.filterable = false;
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    marketContextColorCache = {};
    for(var i in this.props.marketContext) {
      var context = this.props.marketContext[i];
      marketContextColorCache[context.name] = context.color;
    }
  }

  handleEventCalendarDialogClose = () =>  {
    this.setState({eventCalendarDialog: false});
  }

  handleEditEvent = (id) => () => {
    history.push( '/event/detail/'+ id);
  }

  getEventPropGetter = (event, start, end, isSelected) => {
    if(this.props.color === "status") {
      var colorIntensity = 700;
      if(isSelected) {
        colorIntensity = 500;
      }

      var color;
      if(!event.published) {
        color = amber[colorIntensity];
      }
      else if(event.state === "ACTIVE"){
        color = green[colorIntensity];
      }
      else if(event.state === "CANCELLED") {
        color = red[colorIntensity];
      }

      
      return {
        style: {backgroundColor: color}
      }
    }
    else {
      return {
        style: {backgroundColor: marketContextColorCache[event.marketContext]}
      }
    }
      
  }

  handleSelectEvent = (event) => {
    this.setState({eventCalendarDialog: true, selectedEvent:event});
  }

  handleEventDetailClick = (id) => () => {
    history.push( '/event/detail/'+ id);
  }

  handleFilterClick = e => {
    console.log(this.state.filterable)
    this.setState({ filterable: !this.state.filterable});
  }



  render() {
    var that = this;
    const {classes, marketContext, event, ven, filters, onFilterChange
      , onVenSuggestionsFetchRequested, onVenSuggestionsClearRequested, onVenSuggestionsSelect, start, end, onStartChange, onEndChange, view, currentDate} = this.props;

    var calendarEvent = [];
    event.forEach(e => {
      var start = new Date();
      start.setTime(e.activePeriod.start);
      var end = new Date();
      end.setTime(e.activePeriod.start + iCalDurationInSeconds(e.activePeriod.duration) * 1000);
      calendarEvent.push({
        title:e.descriptor.marketContext+":"+e.id,
        start: start,
        end: end,
        allday:false,
        state: e.descriptor.state,
        published: e.published,
        marketContext:e.descriptor.marketContext,
        id:e.id
      });
    });


    EventCalendarWeekView.onColorChange = this.props.onColorChange;
    EventCalendarWeekView.color = this.props.color;
    EventCalendarDayView.onColorChange = this.props.onColorChange;
    EventCalendarDayView.color = this.props.color;
    EventCalendarMonthView.onColorChange = this.props.onColorChange;
    EventCalendarMonthView.color = this.props.color;
    EventCalendarAgendaView.onColorChange = this.props.onColorChange;
    EventCalendarAgendaView.color = this.props.color;

    
  
    return (
      <div className={ classes.root }>
         <Paper >
         
         <Toolbar>
            <div style={{flex: '0 0 auto'}}>
              <Typography variant="h6" id="tableTitle">

                  Events
                </Typography>
            </div>
            <div style={{flex: '1 1 100%'}} />
            <div style={{float:"right"}}>

               <Tooltip title="Filter" onClick={this.handleFilterClick}>
                          <IconButton aria-label="Filter" color={this.props.filters.length !== 0 ? "primary" : "default"}>
                            <FilterListIcon />
                          </IconButton>
                        </Tooltip> 

                
              
            </div>
                


        </Toolbar>
        <div style={{margin: "0px 20px"}}>
        {this.state.filterable ? <FilterPanel classes={classes} type="EVENT" hasFilter={{marketContext:true, ven: true, eventStatus:true, date: true}} 
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
                      onVenSuggestionsSelect={onVenSuggestionsSelect}
                      /> : null}
         </div>
        <Table className={classes.table} aria-labelledby="tableTitle">
        <TableBody>
        <BigCalendar style={{height:600, margin: "20px 20px"}}
          localizer={localizer}
          events={calendarEvent}
          defaultView={view}
          defaultDate={currentDate}
          startAccessor="start"
          endAccessor="end"
          eventPropGetter={this.getEventPropGetter}
          onSelectEvent={this.handleSelectEvent}
          views={{ month: EventCalendarMonthView, week: EventCalendarWeekView , day: EventCalendarDayView, agenda:EventCalendarAgendaView}}
          onNavigate={(date) => {
            that.props.onCurrentDateChange(date);
          }}
          onView={(view) => {
            that.props.onViewChange(view)
            }
          }
        />
        </TableBody>

        {(this.state.selectedEvent) ? <EventCalendarDialog classes={classes} title={(this.state.selectedEvent) ? this.state.selectedEvent.title : ""}
        event={this.state.selectedEvent}
        open={this.state.eventCalendarDialog}
        close={this.handleEventCalendarDialogClose}
        handleEventDetailClick={this.handleEventDetailClick(this.state.selectedEvent.id)}/> : null}
       </Table>
       </Paper>
      </div>
    );
  }
}

export default EventCalendar;
