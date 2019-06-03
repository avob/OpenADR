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



var VenRequestedReportTable = (props) => {
  const {classes, requestedReport, handleDeleteRequestClick, pagination} = props
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
  


  var onClick = (reportSpecifierId, reportRequestId) => event => props.handleViewReportRequestDetail(reportSpecifierId, reportRequestId)


  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            {/* <TableCell align="right">report request ID</TableCell> */}
            <TableCell align="right">Specifier ID</TableCell>
            <TableCell align="right">Created<br/>Date/Time</TableCell>
     
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {requestedReport.map(row => {
        	  console.log(row)
            return (
              <TableRow key={row.reportRequestId+row.reportSpecifierId+row.rid} hover>
              {/* <TableCell scope="row" onClick={onClick(row.reportSpecifierId, row.reportRequestId)} align="right">{row.reportRequestId}</TableCell>*/}
                <TableCell scope="row" onClick={onClick(row.reportSpecifierId, row.reportRequestId)} align="right">{row.reportSpecifierId}</TableCell>
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
      <TablePagination
      rowsPerPageOptions={pagination.options}
      component="div"
      count={props.totalRequest}
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


export class VenDetailRequest extends React.Component {
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
	  this.props.pageVenRequestedReport(this.props.venId, this.state.pagination.page, this.state.pagination.size)
  }

  handleRequestRegisterReportClick = () => {
    this.props.requestRegisterReport( this.props.ven.username);
  }

  handleSendRegisterReportClick = () => {
    this.props.sendRegisterReport( this.props.ven.username);
  }
  
  handleDeleteRequestClick = (reportRequestId) => {
    this.props.cancelRequestReportSubscription(this.props.ven.username, reportRequestId)
  }

  handleViewReportRequestDetail = (reportSpecifierId, reportRequestId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId+"/requests/"+reportRequestId);
  }
  
  handleChangePage = (e, newPage) => {
	  var pagination = this.state.pagination;
	  pagination.page = newPage;
	  this.setState({pagination});
	  this.props.pageVenRequestedReport(this.props.venId, newPage, this.state.pagination.size)
  }
  
  handleChangeRowsPerPage = (e) => {
	  var pagination = this.state.pagination;
	  pagination.size = e.target.value;
	  pagination.page= 0;
	  this.setState({pagination});
	  this.props.pageVenRequestedReport(this.props.venId, 0, e.target.value)
  }

  render() {
    const {classes, ven, requestedReport, totalRequest} = this.props;

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

      <VenRequestedReportTable ven={ven} requestedReport={requestedReport}
         handleDeleteRequestClick={this.handleDeleteRequestClick} 
         handleViewReportRequestDetail={this.handleViewReportRequestDetail} classes={classes}
   	pagination={this.state.pagination}
      handleChangePage={this.handleChangePage}
      handleChangeRowsPerPage={this.handleChangeRowsPerPage}
      totalRequest={totalRequest}
      />

     
    </div>
    );
  }
}

export default VenDetailRequest;
