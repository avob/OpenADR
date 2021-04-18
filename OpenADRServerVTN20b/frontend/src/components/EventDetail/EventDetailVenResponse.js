import React from 'react';


import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';

import {formatTimestamp} from '../../utils/time'



var EventVenResponseTable = (props) => {
  const {classes} = props;
  return (
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Ven ID</TableCell>
            <TableCell>Last Modification Number</TableCell>
            <TableCell>Last Update</TableCell>
            <TableCell>Opt</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.venResponse.map( (row, index) => {
            var lastUpdateDatetime = formatTimestamp(row.lastUpdateDatetime);
            return (

            <TableRow key={index}>
              <TableCell>{row.venId}</TableCell>
              <TableCell>{row.lastSentModificationNumber}</TableCell>
              <TableCell>{lastUpdateDatetime.date + " " +lastUpdateDatetime.time + " " + lastUpdateDatetime.tz}</TableCell>
              <TableCell>{row.venOpt}</TableCell>
            </TableRow>
          )
          })}
        </TableBody>
      </Table>
  );
}

export class EventDetailVenResponse extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
   const {classes, venResponse} = this.props;

    return (
    <div className={ classes.root } >

       <EventVenResponseTable classes={classes} venResponse={venResponse}/>


    </div>
    );
  }
}

export default EventDetailVenResponse;
