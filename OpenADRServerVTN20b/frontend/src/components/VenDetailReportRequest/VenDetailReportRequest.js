import React from 'react';










import Typography from '@material-ui/core/Typography';




import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import Toolbar from '@material-ui/core/Toolbar';

import {formatTimestamp} from '../../utils/time'




export class VenDetailReportRequest extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }

  render() {
    const {classes, requestedReportSpecifier} = this.props;

    var getFormatedDatetime = (datetime) => {
    var format = formatTimestamp(datetime);
    return (
      <span>{format.date}<br/>{format.time} {format.tz}</span>
    );
  }

    return (
    <div className={ classes.root } >
       <Paper className={classes.root}>
      <Toolbar
      className={classes.root}
    >
      <div className={classes.title}>
         <Typography variant="h6" id="tableTitle">
             Report Requests
          </Typography>
      </div>
      <div className={classes.spacer} />
      <div className={classes.actions}>
      </div>
    </Toolbar>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">rID</TableCell>
            <TableCell align="right">Archived</TableCell>
            <TableCell align="right">Last Update<br/>Date/Time</TableCell>
            <TableCell align="right">Last Update<br/>Value</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {requestedReportSpecifier.map(row => (
            <TableRow key={row.id}>
              <TableCell align="right">{row.rid}</TableCell>
              <TableCell align="right">{(row.archived) ? "Archived": ""}</TableCell>
              <TableCell align="right">{(row.lastUpdateDatetime != null) ? getFormatedDatetime(row.lastUpdateDatetime) : null } </TableCell>
              <TableCell align="right">{(row.lastUpdateValue != null) ? row.lastUpdateValue : null } </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  
    </div>
    );
  }
}

export default VenDetailReportRequest;
