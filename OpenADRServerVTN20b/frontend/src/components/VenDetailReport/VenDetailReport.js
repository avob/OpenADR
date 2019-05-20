import React from 'react';







import { VenAvailableReportHeader } from '../common/VenAvailableReportHeader'


import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';


import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import Toolbar from '@material-ui/core/Toolbar';



var VenAvailableReportDescriptionTable = (props) => {
  const {classes, availableReportDescription} = props

  return (
    <Paper className={classes.root}>
      <Toolbar
      className={classes.root}
    >
      <div className={classes.title}>
         <Typography variant="h6" id="tableTitle">
             Report Descriptions
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
            <TableCell align="right">ReportType</TableCell>
            <TableCell align="right">ReadingType</TableCell>
            <TableCell align="right">OadrMinPeriod</TableCell>
            <TableCell align="right">OadrMaxPeriod</TableCell>
            <TableCell align="right">OadrOnchange</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {availableReportDescription.map(row => (
            <TableRow key={row.id}>
              <TableCell align="right">{row.rid}</TableCell>
              <TableCell align="right">{row.reportType}</TableCell>
              <TableCell align="right">{row.readingType}</TableCell>
              <TableCell align="right">{row.oadrMinPeriod}</TableCell>
              <TableCell align="right">{row.oadrMaxPeriod}</TableCell>
              <TableCell align="right">{(row.oadrOnChange) ? "True" : "False"}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}




export class VenDetailReport extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }

  render() {
    const {classes, availableReportDescription, availableReport} = this.props;

    return (
    <div className={ classes.root } >
      <VenAvailableReportHeader availableReport={availableReport} classes={classes}/>
      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } /> 
      <VenAvailableReportDescriptionTable availableReportDescription={availableReportDescription} classes={classes}/>
  
    </div>
    );
  }
}

export default VenDetailReport;
