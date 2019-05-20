import React from 'react';



import Grid from '@material-ui/core/Grid';


import Typography from '@material-ui/core/Typography';

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
import Paper from '@material-ui/core/Paper';


import { history } from '../../store/configureStore';

import {iCalDurationInSeconds, formatTimestamp} from '../../utils/time'
import {isActionReport, isHistoryReport, isTelemetryReport} from '../../utils/venReport'




var VenAvailableReportTable = (props) => {
  const {classes, availableReport} = props

  var getActionLabel = (report) => {
    switch(report.reportName) {
      case "METADATA_HISTORY_USAGE":
        return "Request";
      case "METADATA_TELEMETRY_USAGE":
        return "Subscribe";
      default:
        return "Request";

    }
  }

  var getReportBehavior = (report) => {
    var behavior = "Unknown";
    if(isActionReport(report)) {
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
          {availableReport.map(row => (
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
    </Paper>
  );
}

var VenRequestedReportTable = (props) => {
  const {classes, requestedReport, handleDeleteRequestClick} = props
  var getActionLabel = (request) => {
    if(request.end != null || request.start != null)  {
      return "DELETE"
    } else if(iCalDurationInSeconds(request.granularity) === 0 || iCalDurationInSeconds(request.reportBackDuration) === 0 )  {
      return "DELETE"
    } else if(iCalDurationInSeconds(request.granularity) !== 0 && iCalDurationInSeconds(request.reportBackDuration) !== 0 )  {
      return "UNSUBSCRIBE"
    }
  }

  var getFormatedCreatedDatetime = (report) => {
    var format = formatTimestamp(report.createdDatetime);
    return (
      <span>{format.date}<br/>{format.time} {format.tz}</span>
    );
  }

  var groupByReportRequestId = {};

  for(var i in requestedReport) {
    var reportRequestId = requestedReport[i].reportRequestId;
    var group = groupByReportRequestId[reportRequestId];
    if(group == null) {
      group = {}
      group.reportRequestId = requestedReport[i].reportRequestId;
      group.reportSpecifierId = requestedReport[i].reportSpecifierId;
      group.createdDatetime = requestedReport[i].createdDatetime;
      group.start = requestedReport[i].createdDatetime;
      group.duration = requestedReport[i].duration;
      group.granularity = requestedReport[i].granularity;
      group.reportBackDuration = requestedReport[i].reportBackDuration;
      group.request = [];
    }
    group.request.push(requestedReport[i]);

     groupByReportRequestId[reportRequestId] = group;

  }


  var req = Object.values(groupByReportRequestId)

  var onClick = (reportSpecifierId, reportRequestId) => event => props.handleViewReportRequestDetail(reportSpecifierId, reportRequestId)


  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">report request ID</TableCell>
            <TableCell align="right">Specifier ID</TableCell>
            <TableCell align="right"># rIDs</TableCell>
            <TableCell align="right">Created<br/>Date/Time</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {req.map(row => {
            return (
              <TableRow key={row.reportRequestId} hover>
                <TableCell scope="row" onClick={onClick(row.reportSpecifierId, row.reportRequestId)} align="right">{row.reportRequestId}</TableCell>
                <TableCell scope="row" onClick={onClick(row.reportSpecifierId, row.reportRequestId)} align="right">{row.reportSpecifierId}</TableCell>
                <TableCell scope="row" onClick={onClick(row.reportSpecifierId, row.reportRequestId)} align="right">{row.request.length}</TableCell>
                <TableCell scope="row" onClick={onClick(row.reportSpecifierId, row.reportRequestId)} align="right">{getFormatedCreatedDatetime(row)}</TableCell>
                <TableCell align="right">
                  <Button size="small" color="secondary" onClick={function(reportRequestId) {
                    return () => {handleDeleteRequestClick(reportRequestId)}
                  }(row.reportRequestId)  }> { getActionLabel(row)} </Button>
                </TableCell>
              </TableRow>
            )
          })}
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

  handleRequestRegisterReportClick = () => {
    this.props.requestRegisterReport( this.props.ven.username);
  }

  handleSendRegisterReportClick = () => {
    this.props.sendRegisterReport( this.props.ven.username);
  }

  handleCreateRequestClick = (reportSpecifierId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId+"/create")
  }

  handleDeleteRequestClick = (reportRequestId) => {
    this.props.cancelRequestReportSubscription(this.props.ven.username, reportRequestId)
  }

  handleViewReportDetail = (reportSpecifierId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId);
  }

  handleViewReportRequestDetail = (reportSpecifierId, reportRequestId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId+"/requests/"+reportRequestId);
  }

  render() {
    const {classes, ven, availableReport, requestedReport} = this.props;

    return (
    <div className={ classes.root } >
      <VenDetailHeader classes={classes} ven={ven} actions={[
         <Grid  key="report_action_first_row" container spacing={ 12 }>
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
        handleCreateRequestClick={this.handleCreateRequestClick}/>


      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Requests
      </Typography>
      <VenRequestedReportTable ven={ven} requestedReport={requestedReport}
         handleDeleteRequestClick={this.handleDeleteRequestClick} 
         handleViewReportRequestDetail={this.handleViewReportRequestDetail} classes={classes}/>

     
    </div>
    );
  }
}

export default VenDetailReport;
