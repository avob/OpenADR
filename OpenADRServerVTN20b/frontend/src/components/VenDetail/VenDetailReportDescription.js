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

var formatTargetList = (targets) => {
  var view = []

  for(var i in targets) {
    var target = targets[i];
    if(view.length == 0) {
      view.push(<span> {target.targetType + " " + target.targetId} </span>);
    } else {
       view.push(<br/>);
      view.push(<span> {target.targetType + " " + target.targetId} </span>);
    }
  }
  return view;
}

export class VenDetailReportDescription extends React.Component {
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
      this.props.venActions.loadVenAvailableReportDescription( this.props.ven.username, this.props.selectedReportSpecifierId);
    
	  
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

  handleCreateRequestClick = (reportSpecifierId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId+"/create")
  }

  render() {
    const {classes, ven, venActions, availableReportDescription, selectedReportSpecifierId} = this.props;
    console.log(this.props)
    return (
    <div className={ classes.root }>
         <EnhancedTable 
        title={"Report: " +selectedReportSpecifierId}
        data={availableReportDescription}
        total={availableReportDescription.length}
        pagination={this.state.pagination}
        sort={this.state.sort}
        handlePaginationChange={this.handlePaginationChange}
        handleSortChange={this.handleSortChange}
        rows={[
          { id: 'eiSubject', numeric: false, disablePadding: false, label: 'Subject'},
          { id: 'eiDatasource', numeric: false, disablePadding: false, label: 'Datasource'},
          { id: 'rid', numeric: false, disablePadding: false, label: 'rID'},
          { id: 'reportType', numeric: false, disablePadding: false, label: 'ReportType' },
          { id: 'itemBase', numeric: false, disablePadding: false, label: 'Unit'},
          { id: 'samplingRate', numeric: false, disablePadding: false, label: 'SamplingRate Min/Max/OnChange' },
        ]} 
        rowTemplate={n => {
          var created = formatTimestamp(n.createdDatetime);
          return <React.Fragment>

             <TableCell>{formatTargetList(n.eiSubject)}</TableCell>
             <TableCell>{formatTargetList(n.eiDatasource)}</TableCell>
              <TableCell>{n.rid}</TableCell>
              <TableCell>{n.reportType}</TableCell>
              {n.itemBase ? <TableCell>{n.itemBase.itemDescription}<br/>{n.itemBase.itemUnits}<br/>{n.itemBase.siScaleCode}</TableCell> : <TableCell></TableCell>}
              {n.samplingRate ? <TableCell>{n.samplingRate.oadrMinPeriod}/{n.samplingRate.oadrMaxPeriod}/{(n.samplingRate.oadrOnChange) ? "True" : "False"}</TableCell> : <TableCell></TableCell>}
              
              
        
          </React.Fragment>
        }}
     
        action={() => {
          return <React.Fragment>
            <Button size="small" color="primary" onClick={() => {this.handleCreateRequestClick(selectedReportSpecifierId)}}>Subscribe</Button>
          </React.Fragment>
        }}
        />
     
    </div>
    );
  }
}

export default VenDetailReportDescription;
