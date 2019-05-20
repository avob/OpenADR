import React from 'react';
import BigCalendar from 'react-big-calendar'

import TimeGrid from 'react-big-calendar/lib/TimeGrid'
import moment from 'moment'


class EventCalendarWeekView extends React.Component {
  render() {
    let { date } = this.props
    let range = EventCalendarWeekView.range(date)

    return <TimeGrid {...this.props} range={range} eventOffset={15} />
  }
}

// var getRange = (date) => {
   
//     return {
//       start:start.toDate(),
//       end:end.toDate()
//     }

// }

EventCalendarWeekView.range = date => {


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

EventCalendarWeekView.navigate = (date, action) => {
  let d = moment(new Date(date));
  switch (action) {
    case BigCalendar.Navigate.PREVIOUS:
      d.add(-7, 'day')
      return d.toDate();

    case BigCalendar.Navigate.NEXT:
      d.add(7, 'day');
      return d.toDate();

    case BigCalendar.Navigate.TODAY:
    console.log("today")
      date = new Date();
     
      return date;

    default:
      return date
  }
}


EventCalendarWeekView.title = date => {
  let start = moment(date).startOf('week');
  let end = moment(start.toDate());
  end.add(7, 'day');
  return (
    <span>
      <span style={{paddingTop:10}}>{start.format( "MMM Do")} - {end.format( "MMM Do")}</span>
      <span className="rbc-btn-group" style={{float:"right"}} >
          <button type="button" className={(EventCalendarWeekView.color === "status") ? "rbc-active" : ""} onClick={(e) => {EventCalendarWeekView.onColorChange("status")}}>Color Status</button>
          <button type="button" className={(EventCalendarWeekView.color  === "market") ? "rbc-active" : ""} onClick={(e) => {EventCalendarWeekView.onColorChange("market")}}>Color Market</button>
        </span>  
    </span>
    );
}

export default EventCalendarWeekView;