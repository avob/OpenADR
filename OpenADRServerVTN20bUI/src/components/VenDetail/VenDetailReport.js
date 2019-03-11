import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles, MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

import { VtnConfigurationVenCard, VtnConfigurationMarketContextCard, VtnConfigurationGroupCard } from '../common/VtnConfigurationCard'
import { MarketContextSelectDialog, GroupSelectDialog } from '../common/VtnconfigurationDialog'

import Input from '@material-ui/core/Input';
import InputBase from '@material-ui/core/InputBase';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';


import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';


import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import GridListTileBar from '@material-ui/core/GridListTileBar';

import IconButton from '@material-ui/core/IconButton';

import StarBorderIcon from '@material-ui/icons/StarBorder';

import ChipInput from 'material-ui-chip-input'

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import SearchIcon from '@material-ui/icons/Search';


import SnackbarContent from '@material-ui/core/SnackbarContent';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

import VenDetailHeader from './VenDetailHeader'

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';


import { history } from '../../store/configureStore';


//
// Takes an ical duration in a outputs it as a total # of seconds
//
function iCalDurationInSeconds(durStr) {
  var exp = new RegExp(/^P/);
  let totalSec = 0;
  let valStr;
  if (durStr.match(exp)) {
    // Days
    exp = new RegExp(/(\d+)D/);
    valStr = durStr.match(exp);
    if (valStr) totalSec += parseInt(valStr[1]) * 60 * 60 * 24;
    // Hours
    exp = new RegExp(/(\d+)H/);
    valStr = durStr.match(exp);
    if (valStr) totalSec += parseInt(valStr[1]) * 60 * 60;
    // Minutes
    exp = new RegExp(/(\d+)M/);
    valStr = durStr.match(exp);
    if (valStr) totalSec += parseInt(valStr[1]) * 60;
    // Seconds
    exp = new RegExp(/(\d+)S/);
    valStr = durStr.match(exp);
    if (valStr) totalSec += parseInt(valStr[1]);
  }
  return totalSec;
}


var VenAvailableReportTable = (props) => {
  const {classes, availableReport, ven} = props

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
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Specifier ID</TableCell>
            <TableCell align="right">Created<br/>Date/Time</TableCell>
            <TableCell align="right">Duration</TableCell>
            <TableCell align="right">Report Name</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {availableReport.map(row => (
            <TableRow key={row.id}>
              <TableCell align="right">{row.reportSpecifierId}</TableCell>
              <TableCell align="right">{row.createDatetime}</TableCell>
              <TableCell align="right">{(row.duration) ? row.duration : "Realtime"}</TableCell>
              <TableCell align="right">{row.reportName}</TableCell>
              <TableCell align="right">
                <Button size="small" color="primary" onClick={function(reportSpecifierId) {
                  return () => {props.handleViewReportDetail(reportSpecifierId);}
                }(row.reportSpecifierId)  }> View </Button> 
                | <Button size="small" color="primary" onClick={function(reportSpecifierId) {
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
  const {classes, requestedReport, ven, handleDeleteRequestClick} = props
  console.log(requestedReport)
  var getActionLabel = (request) => {
    if(request.end != null || request.start != null)  {
      return "DELETE"
    } else if(iCalDurationInSeconds(request.granularity) == 0 || iCalDurationInSeconds(request.reportBackDuration) == 0 )  {
      return "DELETE"
    } else if(iCalDurationInSeconds(request.granularity) != 0 && iCalDurationInSeconds(request.reportBackDuration) != 0 )  {
      return "UNSUBSCRIBE"
    }
  }
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">report request ID</TableCell>
            <TableCell align="right">Specifier ID</TableCell>
            <TableCell align="right">rID</TableCell>
            <TableCell align="right">Created<br/>Date/Time</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {requestedReport.map(row => {
            return (
              <TableRow key={row.id}>
                <TableCell align="right">{row.reportRequestId}</TableCell>
                <TableCell align="right">{row.reportSpecifierId}</TableCell>
                <TableCell align="right">{row.rid}</TableCell>
                <TableCell align="right">{row.createDatetime}</TableCell>
                <TableCell align="right">
                  <Button size="small" color="primary" onClick={function(reportRequestId) {
                    return () => {}
                  }(row.reportRequestId)  }> View </Button> 
                  | <Button size="small" color="secondary" onClick={function(reportRequestId) {
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

  render() {
    const {classes, ven, availableReport, requestedReport, cancelRequestReportSubscription} = this.props;

    var SuccessSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.success }
                       style={ { maxWidth: 'none', paddingTop:0, paddingBottom:0 } }
                       message={ <Grid container direction="row" alignItems="center">
                            <Grid item>
                              <DoneIcon style={ { width: 20, height: 20, marginRight: 20, color:"#fff" } }/>
                            </Grid>
                            <Grid item style={{}}>
                               { props.message }
                            </Grid>
                          </Grid>} />
      );
    }

    var DefaultSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.success }
                       style={ { maxWidth: 'none', paddingTop:0, paddingBottom:0, backgroundColor:"#fafafa" } }
                       message={ 
                          <Grid container direction="row" alignItems="center">
                            <Grid item>
                              <CloseIcon style={ { width: 20, height: 20, marginRight: 20, color:"#000" } }/>
                            </Grid>
                            <Grid item>
                               { props.message }
                            </Grid>
                          </Grid>
                     } />
      );
    }



    return (
    <div className={ classes.root } >
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Status
      </Typography>
      <VenDetailHeader classes={classes} ven={ven} actions={[
         <Grid  key="report_action_first_row" container spacing={ 24 }>
          <Grid item xs={ 3 }>
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
          <Grid item xs={ 3 }>
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
           <Grid item xs={ 3 }>
          <Button key="btn_create"
                  style={ { marginTop: 15 } }
                  variant="outlined"
                  color="secondary"
                  fullWidth={true}
                  size="small">
            <RemoveIcon style={ { marginRight: 15 } }/> CLEAN REQUESTS
          </Button>
        </Grid>
        <Grid item xs={ 3 }>
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
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Reports
      </Typography>
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
         handleDeleteRequestClick={this.handleDeleteRequestClick} classes={classes}/>

     
    </div>
    );
  }
}

export default VenDetailReport;
