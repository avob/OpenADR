import React from 'react';

import Button from '@material-ui/core/Button';
import TableCell from '@material-ui/core/TableCell';



import { history } from '../../store/configureStore';

import { formatTimestamp} from '../../utils/time'
import EnhancedTable  from '../common/EnhancedTable'

export class VenDetailRequestSpecifier extends React.Component {
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
      this.props.venActions.loadVenRequestedReportSpecifier(this.props.ven.username, this.props.selectedReportRequestId);
    
	  
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

  handleViewRequestDetail = (reportRequestId) => {
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

  handleCreateRequestClick = (reportSpecifierId) => {
    history.push("/ven/detail/"+this.props.ven.username+"/reports/"+reportSpecifierId+"/create")
  }

  handleDeleteRequestClick = (reportRequestId) => {
    this.props.cancelRequestReportSubscription(this.props.ven.username, reportRequestId)
  }

  render() {
    const {classes, ven, requestedReportSpecifier, selectedReportRequestId} = this.props;
    return (
    <div className={ classes.root }>
         <EnhancedTable 
        title={"Request: " +selectedReportRequestId}
        data={requestedReportSpecifier}
        total={requestedReportSpecifier.length}
        pagination={this.state.pagination}
        sort={this.state.sort}
        handlePaginationChange={this.handlePaginationChange}
        handleSortChange={this.handleSortChange}
        rows={[
          { id: 'rid', numeric: false, disablePadding: false, label: 'rID'},
          { id: 'lastUpdateDatetime', numeric: false, disablePadding: false, label: 'Last Update' },
          { id: 'lastUpdateValue', numeric: false, disablePadding: false, label: 'Last Update value' },
        ]} 
        rowTemplate={n => {
          var lastUpdateDatetime = formatTimestamp(n.lastUpdateDatetime);
          return <React.Fragment>
            <TableCell>{n.rid}</TableCell>
             <TableCell >{lastUpdateDatetime.date + " " +lastUpdateDatetime.time + " " + lastUpdateDatetime.tz}</TableCell>
              <TableCell>{(n.lastUpdateValue != null) ? n.lastUpdateValue : null } </TableCell>
          </React.Fragment>
        }}
     
        action={() => {
          return <React.Fragment>
            <Button size="small" color="secondary" onClick={() => {
              this.props.cancelRequestReportSubscription(ven.username, selectedReportRequestId)
              history.push("/ven/detail/"+this.props.ven.username+"/requests")
            }

            }>Unsubscribe</Button>
          </React.Fragment>
        }}
        />
     
    </div>
    );
  }
}

export default VenDetailRequestSpecifier;
