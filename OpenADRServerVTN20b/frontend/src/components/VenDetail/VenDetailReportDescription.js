import React from 'react';

import Button from '@material-ui/core/Button';
import TableCell from '@material-ui/core/TableCell';
import { history } from '../../store/configureStore';

import EnhancedTable  from '../common/EnhancedTable'
import UnitPanel  from '../common/UnitPanel'




var formatTargetList = (targets) => {
  var view = []

  for(var i in targets) {
    var target = targets[i];
    if(view.length === 0) {
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
    const {classes,  availableReportDescription, selectedReportSpecifierId} = this.props;
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
          { id: 'readingType', numeric: false, disablePadding: false, label: 'ReadingType' },
          { id: 'itemBase', numeric: false, disablePadding: false, label: 'Unit'},
          { id: 'samplingRate', numeric: false, disablePadding: false, label: 'SamplingRate Min/Max/OnChange' },
        ]} 
        rowTemplate={n => {
          return <React.Fragment>

             <TableCell>{formatTargetList(n.eiSubject)}</TableCell>
             <TableCell>{formatTargetList(n.eiDatasource)}</TableCell>
              <TableCell>{n.rid}</TableCell>
              <TableCell>{n.reportType}</TableCell>
              <TableCell>{n.readingType}</TableCell>
              <TableCell><UnitPanel unit={n.itemBase}/></TableCell>              
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
