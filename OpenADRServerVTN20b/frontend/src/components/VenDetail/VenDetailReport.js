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

import { formatTimestamp} from '../../utils/time'
import {isActionReport, isHistoryReport, isTelemetryReport, isMetadataReport} from '../../utils/venReport'
import EnhancedTable  from '../common/EnhancedTable'

export class VenDetailReport extends React.Component {
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
	  this.props.pageVenAvailableReport(this.props.venId, this.state.pagination.page, this.state.pagination.size)
  }

  handleRequestRegisterReportClick = () => {
    this.props.requestRegisterReport( this.props.ven.username);
  }

  handleSendRegisterReportClick = () => {
    this.props.sendRegisterReport( this.props.ven.username);
  }

  handleCreateRequestClick = (reportSpecifierId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId+"/create")
  }

  handleViewReportDetail = (reportSpecifierId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId)
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
    const {classes, ven, venActions, availableReport, totalReport} = this.props;
    console.log(this.props)
    return (
    <div className={ classes.root }>
         <EnhancedTable 
        title="Reports"
        data={availableReport}
        total={totalReport}
        pagination={this.state.pagination}
        sort={this.state.sort}
        handlePaginationChange={this.handlePaginationChange}
        handleSortChange={this.handleSortChange}
        rows={[
          { id: 'reportName', numeric: false, disablePadding: false, label: 'ReportName'},
          { id: 'reportSpecifierId', numeric: false, disablePadding: false, label: 'ReportSpecifierID' },
          { id: 'createdDatetime', numeric: false, disablePadding: false, label: 'Created' },
        ]} 
        rowTemplate={n => {
          var created = formatTimestamp(n.createdDatetime);
          return <React.Fragment>
            <TableCell>{n.reportName}</TableCell>
            <TableCell>{n.reportSpecifierId}</TableCell>
            <TableCell>{created.date + " " +created.time + " " + created.tz}</TableCell>
          </React.Fragment>
        }}
        handleClick={(n) => {this.handleViewReportDetail(n.reportSpecifierId)}}
        action={() => {
          return <React.Fragment>

          </React.Fragment>
        }}
        />
     
    </div>
    );
  }
}

export default VenDetailReport;
