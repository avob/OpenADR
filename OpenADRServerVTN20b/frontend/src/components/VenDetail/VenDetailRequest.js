import React from 'react';
import TableCell from '@material-ui/core/TableCell';
import { history } from '../../store/configureStore';

import {formatTimestamp} from '../../utils/time'
import EnhancedTable  from '../common/EnhancedTable'

export class VenDetailRequest extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
     this.state.pagination = {
      page: 0
      , size: 5
    } 
    this.state.sort = {
      sort: "asc"
      , by: "name"
    }
  }
  
  componentDidMount() {
	  this.props.pageVenRequestedReport(this.props.venId, this.state.pagination.page, this.state.pagination.size)
  }
  
  handleDeleteRequestClick = (reportRequestId) => {
    this.props.cancelRequestReportSubscription(this.props.ven.username, reportRequestId)
  }

  handleViewReportRequestDetail = (reportRequestId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/requests/"+reportRequestId);
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
    const {classes, requestedReport} = this.props;
    console.log(this.props)
    return (
    <div className={ classes.root } >
       <EnhancedTable 
        title="Requested Reports"
        data={requestedReport}
        total={requestedReport.length}
        pagination={this.state.pagination}
        sort={this.state.sort}
        handlePaginationChange={this.handlePaginationChange}
        handleSortChange={this.handleSortChange}
        rows={[
          { id: 'reportSpecifierId', numeric: false, disablePadding: false, label: 'ReportSpecifierID' },
          { id: 'createdDatetime', numeric: false, disablePadding: false, label: 'Created' },
        ]} 
        rowTemplate={n => {
          var created = formatTimestamp(n.createdDatetime);
          return <React.Fragment>
            <TableCell>{n.reportSpecifierId}</TableCell>
            <TableCell>{created.date + " " +created.time + " " + created.tz}</TableCell>
          </React.Fragment>
        }}
        handleClick={(n) => {this.handleViewReportRequestDetail(n.reportRequestId)}}
        action={() => {
          return <React.Fragment>

          </React.Fragment>
        }}
        />
     
    </div>
    );
  }
}

export default VenDetailRequest;
