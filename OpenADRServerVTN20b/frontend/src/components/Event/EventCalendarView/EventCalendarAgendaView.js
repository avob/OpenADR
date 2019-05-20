import React from 'react';
import BigCalendar from 'react-big-calendar'

import moment from 'moment'

import AgendaView from 'react-big-calendar/lib/Agenda'



class EventCalendarAgendaView extends AgendaView {

}

// var getRange = (date) => {
    
//     return {
//       start:start.toDate(),
//       end:end.toDate()
//     }

// }

EventCalendarAgendaView.range = date => {


  let start = moment(date).startOf('week');;
  let end = moment(start.toDate())
  end.add(7, 'day');

  let current = moment(start.toDate());
  let range = []

  while (current.toDate().getTime() < end.toDate().getTime()) {
    range.push(current.toDate())
    current.add(1, 'day')
  }

  return range;
}

EventCalendarAgendaView.navigate = (date, action) => {
  let d = moment(new Date(date));
  switch (action) {
    case BigCalendar.Navigate.PREVIOUS:
      d.add(-7, 'day')
      return d.toDate();

    case BigCalendar.Navigate.NEXT:
      d.add(7, 'day');
      return d.toDate();

    case BigCalendar.Navigate.TODAY:
      date = new Date();
     
      return date;

    default:
      return date
  }
}



EventCalendarAgendaView.title = date => {
  let start = moment(date).startOf('week');;
  let end = moment(start.toDate())
  end.add(7, 'day');
  return (
    <span>
      <span style={{paddingTop:10}}>{start.format( "M/D/YYYY")} - {end.format( "M/D/YYYY")}</span>
      <span className="rbc-btn-group" style={{float:"right"}} >
        <button type="button" className={(EventCalendarAgendaView.color === "status") ? "rbc-active" : ""} onClick={(e) => {EventCalendarAgendaView.onColorChange("status")}}>Color Status</button>
        <button type="button" className={(EventCalendarAgendaView.color  === "market") ? "rbc-active" : ""} onClick={(e) => {EventCalendarAgendaView.onColorChange("market")}}>Color Market</button>
      </span>  
    </span>
    );
}

export default EventCalendarAgendaView;