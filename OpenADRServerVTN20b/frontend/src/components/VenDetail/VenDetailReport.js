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




var VenAvailableReportTable = (props) => {
  const {classes, availableReport, pagination} = props
  var reports = []
  if(availableReport) {
	  reports = availableReport;
  }
  
  var getActionLabel = (report) => {
    var behavior = "Request";
    if(isTelemetryReport(report)) {
      behavior = "Subscribe"
    }
    return behavior
  }

  var getReportBehavior = (report) => {
    var behavior = "Unknown";
    
    if(isMetadataReport(report)) {
	    behavior = "Metadata"
	  }
    else if(isActionReport(report)) {
        behavior = "Action"
    }
    else if(isHistoryReport(report)) {
      behavior = "History"
    }
    else if(isTelemetryReport(report)) {
      behavior = "Telemetry"
    }
    return behavior
  }

  var getFormatedCreatedDatetime = (report) => {
    var format = formatTimestamp(report.createdDatetime);
    return (
      <span>{format.date}<br/>{format.time} {format.tz}</span>
    );
  }

  var onClick = reportSpecifierId => event => props.handleViewReportDetail(reportSpecifierId)
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Report Behavior</TableCell>
            <TableCell align="right">Specifier ID</TableCell>
            <TableCell align="right">Created<br/>Date/Time</TableCell>
            
            <TableCell align="right">Report Name</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reports.map(row => (
            <TableRow key={row.id}
              hover
          
               >
               <TableCell scope="row" onClick={onClick(row.reportSpecifierId)} align="right">{getReportBehavior(row)}</TableCell>
              <TableCell scope="row" onClick={onClick(row.reportSpecifierId)} align="right">{row.reportSpecifierId}</TableCell>
              <TableCell scope="row" onClick={onClick(row.reportSpecifierId)} align="right">{getFormatedCreatedDatetime(row)}</TableCell>
              
              <TableCell scope="row" onClick={onClick(row.reportSpecifierId)} align="right">{row.reportName}</TableCell>
              <TableCell align="right">
               <Button size="small" color="primary" onClick={function(reportSpecifierId) {
                  return () => {props.handleCreateRequestClick(reportSpecifierId);}
                }(row.reportSpecifierId)  }>{ getActionLabel(row)}</Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <TablePagination
	      rowsPerPageOptions={pagination.options}
	      component="div"
	      count={props.totalReport}
	      rowsPerPage={pagination.size}
	      page={pagination.page}
	      backIconButtonProps={{
	        'aria-label': 'Previous Page',
	      }}
	      nextIconButtonProps={{
	        'aria-label': 'Next Page',
	      }}
	      onChangePage={props.handleChangePage}
	      onChangeRowsPerPage={props.handleChangeRowsPerPage}
	    />
    </Paper>
  );
}

export class VenDetailReport extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {
    	pagination: {
    		page: 0,
    		size: 10,
    		options: [5, 10, 25]
    	}
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
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId);
  }
  
  handleChangePage = (e, newPage) => {
	  var pagination = this.state.pagination;
	  pagination.page = newPage;
	  this.setState({pagination});
	  this.props.pageVenAvailableReport(this.props.venId, newPage, this.state.pagination.size)
  }
  
  handleChangeRowsPerPage = (e) => {
	  var pagination = this.state.pagination;
	  pagination.size = e.target.value;
	  pagination.page= 0;
	  this.setState({pagination});
	  this.props.pageVenAvailableReport(this.props.venId, 0, e.target.value)
  }

  render() {
    const {classes, ven, availableReport, totalReport} = this.props;

    return (
    <div className={ classes.root } >
      <VenDetailHeader classes={classes} ven={ven} actions={[
         <Grid  key="report_action_first_row" container >
          <Grid item xs={ 6 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleRequestRegisterReportClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> REQUIRE REPORTS
            </Button>
          </Grid>
          <Grid item xs={ 6 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleSendRegisterReportClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> SEND REQUESTS
            </Button>
          </Grid>
           <Grid item xs={ 6 }>
          <Button key="btn_create"
                  style={ { marginTop: 15 } }
                  variant="outlined"
                  color="secondary"
                  fullWidth={true}
                  size="small">
            <RemoveIcon style={ { marginRight: 15 } }/> CLEAN REQUESTS
          </Button>
        </Grid>
        <Grid item xs={ 6 }>
          <Button key="btn_create"
                  style={ { marginTop: 15 } }
                  variant="outlined"
                  color="secondary"
                  fullWidth={true}
                  size="small">
            <RemoveIcon style={ { marginRight: 15 } }/> CLEAN REPORTS
          </Button>
        </Grid>
        </Grid>
      ]
      }/>
      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />
      <VenAvailableReportTable ven={ven} 
        availableReport={availableReport} classes={classes} 
        handleViewReportDetail={this.handleViewReportDetail}
        handleCreateRequestClick={this.handleCreateRequestClick}
      	pagination={this.state.pagination}
      handleChangePage={this.handleChangePage}
      handleChangeRowsPerPage={this.handleChangeRowsPerPage}
      totalReport={totalReport}
      	/>


     
    </div>
    );
  }
}

export default VenDetailReport;
