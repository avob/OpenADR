import React from 'react';



import Grid from '@material-ui/core/Grid';




import Divider from '@material-ui/core/Divider';












import Button from '@material-ui/core/Button';

import RemoveIcon from '@material-ui/icons/Remove';






import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

import VenDetailHeader from './VenDetailHeader'

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TablePagination from '@material-ui/core/TablePagination';
import Paper from '@material-ui/core/Paper';


import { history } from '../../store/configureStore';

import {iCalDurationInSeconds, formatTimestamp} from '../../utils/time'
import EnhancedTable  from '../common/EnhancedTable'

var getActionLabel = (request) => {
    if(request.end != null || request.start != null)  {
      return "DELETE"
    } else if(iCalDurationInSeconds(request.granularity) === 0 || iCalDurationInSeconds(request.reportBackDuration) === 0 )  {
      return "DELETE"
    } else if(iCalDurationInSeconds(request.granularity) !== 0 && iCalDurationInSeconds(request.reportBackDuration) !== 0 )  {
      return "UNSUBSCRIBE"
    }
  }

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
    const {classes, ven, venActions, requestedReport, totalRequest} = this.props;
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
