import React from 'react';
import BigCalendar from 'react-big-calendar'

import MonthView from 'react-big-calendar/lib/Month'
import moment from 'moment'


class EventCalendarMonthView extends MonthView {

}

// var getRange = (date) => {
   
//     return {
//       start:start.toDate(),
//       end:end.toDate()
//     }

// }

EventCalendarMonthView.range = date => {


  let start = moment(date).startOf('month');
  let end = moment(date).endOf('month');

  let current = moment(start.toDate());
  let range = []

  while (current.toDate().getTime() < end.toDate().getTime()) {
    range.push(current.toDate())
    current.add(1, 'day')
  }

  return range;
}

EventCalendarMonthView.navigate = (date, action) => {
  let d = moment(new Date(date));
  switch (action) {
    case BigCalendar.Navigate.PREVIOUS:
      d.add(-1, 'month')
      return d.toDate();

    case BigCalendar.Navigate.NEXT:
      d.add(1, 'month');
      return d.toDate();

    case BigCalendar.Navigate.TODAY:
      date = new Date();     
      return date;

    default:
      return date
  }
}



EventCalendarMonthView.title = date => {
  return (
    <span>
      <span style={{paddingTop:10}}>{moment(date).format( "YYYY MMMM")}</span>
      <span className="rbc-btn-group" style={{float:"right"}} >
        <button type="button" className={(EventCalendarMonthView.color === "status") ? "rbc-active" : ""} onClick={(e) => {EventCalendarMonthView.onColorChange("status")}}>Color Status</button>
        <button type="button" className={(EventCalendarMonthView.color  === "market") ? "rbc-active" : ""} onClick={(e) => {EventCalendarMonthView.onColorChange("market")}}>Color Market</button>
      </span>   
    </span>
    );
}

export default EventCalendarMonthView;