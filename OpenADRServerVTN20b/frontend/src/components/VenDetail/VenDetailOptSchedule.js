import React from 'react';
import TableCell from '@material-ui/core/TableCell';

import {formatTimestamp} from '../../utils/time'

import EnhancedTable  from '../common/EnhancedTable'

export class VenDetailOptSchedule extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    this.state.pagination = {
      page: 0
      , size: 5
    } 
    this.state.sort = {
      sort: "asc"
      , by: "optId"
    }
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


  render() {
    const {classes, venOpt} = this.props;

    return (
    <div className={ classes.root } >
        <EnhancedTable 
        title="Opt"
        data={venOpt}
        total={venOpt.length}
        pagination={this.state.pagination}
        sort={this.state.sort}
        handlePaginationChange={this.handlePaginationChange}
        handleSortChange={this.handleSortChange}
        rows={[
          { id: 'optId', numeric: false, disablePadding: true, label: 'OptID'},
          { id: 'marketContext', numeric: false, disablePadding: false, label: 'MarketContext' },
          { id: 'startDatetime', numeric: true, disablePadding: false, label: 'Start' },
          { id: 'endDatetime', numeric: true, disablePadding: false, label: 'End' },
          { id: 'opt', numeric: true, disablePadding: false, label: 'Opt' },
        ]} 
        rowTemplate={n => {
          var startDatetime = formatTimestamp(n.start);
          var endDatetime = formatTimestamp(n.end);
          return <React.Fragment>
            <TableCell component="th" scope="row" padding="none">
              {n.optId}
            </TableCell>
            <TableCell scope="row" align="right">{n.marketContext}</TableCell>
            <TableCell scope="row" align="right">{startDatetime.date + " " +startDatetime.time + " " + startDatetime.tz}</TableCell>
            <TableCell scope="row" align="right">{endDatetime.date + " " +endDatetime.time + " " + endDatetime.tz}</TableCell>
            <TableCell scope="row" align="right">{n.opt}</TableCell>
          </React.Fragment>
        }}
        actionSelected={() => {
       
        }}
        action={() => {
          return <React.Fragment>

          </React.Fragment>
        }}
        />


    </div>
    );
  }
}



export default VenDetailOptSchedule;
