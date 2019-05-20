import pxd from 'parse-xsd-duration'

//
// Takes an ical duration in a outputs it as a total # of seconds
//
export function iCalDurationInSeconds(durStr) {
  return pxd(durStr)
}

//
// Take total of minutes and output ical duration
//
export function minutesToICalDuration(minutes) {
  return "PT"+minutes+"M";
}

export function timestampToISO(timestamp) {
  var date = new Date();
  date.setTime(timestamp);
  // remove leading Z: ignoring timezone
  return date.toISOString().slice(0, -1);
}

export function isoToTimestamp(iso) {
  // add leading Z (tz)
  var date = new Date(iso+"Z");
  return date.getTime();
}

var enforceTwoDigit = (date) => {
  date = ""+date;
  if(date.length === 1){
    date = "0"+ date;
  }
  return date;
} 

export function formatTimestamp(timestamp) {
  var date = new Date();
  date.setTime(timestamp);
  return {
    date: date.getFullYear() + "-" + enforceTwoDigit(date.getMonth()+1) + "-" + enforceTwoDigit(date.getDate()),
    time: enforceTwoDigit(date.getHours()) + ":" + enforceTwoDigit(date.getMinutes()),
    tz: "UTC"
  }

}