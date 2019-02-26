import React from 'react'
import PropTypes from 'prop-types'
import ReactTable from 'react-table'
import { IndexLink, Link } from 'react-router'
import 'react-table/react-table.css'
import JsonPrettyPrintModal from '../../../../../components/Modal/JsonPrettyPrintModal.js'

class VenReportRequest extends React.Component {
  constructor(props){
    super(props);
    this.state = {};
    this.state.jsonPrettyPrintModal = {
      show: false
    };

  }

  componentDidMount() {
    this.props.fetchRequestedReport(this.props.params.venId);
  }

  componentWillUnmount(){
    this.props.unmount();
  }

  render() {
      var that = this;

      var subsciptions = [];
      var metadataRequest = [];
      var historyRequests = [];
      for(var i in this.props.requests){
        var request = this.props.requests[i];
        if(request.start == null && request.end == null && !request.rid.includes("-metadata-")){
          subsciptions.push(request);
        }
        else if(request.start == null && request.end == null && request.rid.includes("-metadata-")){
          metadataRequest.push(request);
        }
        else {
          historyRequests.push(request);
        }
      }

      const subsciptionsColumns = [{
          Header: 'ReportRequestId',
          accessor: 'reportRequestId'
        }, {
          Header: 'reportSpecifierId',
          accessor: 'reportSpecifierId'
        }
        , {
          Header: 'RID',
          accessor: 'rid'
        }, {
          Header: 'Granularity',
          accessor: 'granularity',
          maxWidth:80
        }, {
          Header: 'ReportBackDuration',
          accessor: 'reportBackDuration',
          maxWidth:80
        }, {
          Header: 'Last update',
          Cell: props => { 
            if(props.original.lastUpdateDatetime){
              var date = new Date();
              date.setTime(props.original.lastUpdateDatetime);

              return date.toLocaleDateString("fr-FR")+ " " + date.toLocaleTimeString("fr-FR");
            }
             
          }
        }, {
          Header: 'Last value',
          accessor: 'lastUpdateValue',
          maxWidth:80
        }
      ]

      const metadataColumns = [{
          Header: 'ReportRequestId',
          accessor: 'reportRequestId'
        }, {
          Header: 'reportSpecifierId',
          accessor: 'reportSpecifierId'
        }
        , {
          Header: 'RID',
          accessor: 'rid' 
        }, {
          Header: 'Last update',
          Cell: props => { 
            if(props.original.lastUpdateDatetime){
              var date = new Date();
              date.setTime(props.original.lastUpdateDatetime);

              return date.toLocaleDateString("fr-FR")+ " " + date.toLocaleTimeString("fr-FR");
            }
             
          }
        }, {
          Header: 'Last value',
          accessor: 'lastUpdateValue',
          Cell: props => { 
            if(props.original.lastUpdateValue){
              return <button className="btn btn-default btn-xs" onClick={function(uuid, payload){
                  return function(){
                    var split = uuid.split("-");
                    that.setState({
                      jsonPrettyPrintModal: {
                        show: true
                        , title: "MetaData JSON payload for device:"+ split[split.length -1]
                        , json:payload
                        , discard: () => {that.setState({jsonPrettyPrintModal:{show:false}})}
                      }
                    })
                  }
                }(props.original.reportSpecifierId, props.original.lastUpdateValue)}>View payload</button>
            }
             
          }
        }
      ]

      const historyColumns = [{
          Header: 'ReportRequestId',
          accessor: 'reportRequestId' 
        }
        , {
          Header: 'reportSpecifierId',
          accessor: 'reportSpecifierId' 
        }, {
          Header: 'RID',
          accessor: 'rid' 
        }, {
          Header: 'Start',
          accessor: 'start' ,
          Cell: props => { 
            if(props.original.start){
              var date = new Date();
              date.setTime(props.original.start);

              return date.toLocaleDateString("fr-FR")+ " " + date.toLocaleTimeString("fr-FR");
            }
             
          }
        }, {
          Header: 'End',
           accessor: 'end' ,
          Cell: props => { 
            if(props.original.end){
              var date = new Date();
              date.setTime(props.original.end);

              return date.toLocaleDateString("fr-FR")+ " " + date.toLocaleTimeString("fr-FR");
            }
             
          }
        }, {
          Header: 'Last update',
           accessor: 'lastUpdateDatetime' ,
          Cell: props => { 
            if(props.original.lastUpdateDatetime){
              var date = new Date();
              date.setTime(props.original.lastUpdateDatetime);

              return date.toLocaleDateString("fr-FR")+ " " + date.toLocaleTimeString("fr-FR");
            }
             
          }
        }
      ]

      return (
      <div style={{ margin: '0 auto' }} >
        <h2>VenRequest</h2>

        <ul className="nav nav-tabs" role="tablist">
          <li role="presentation" className="active">
            <a href="#subscription_request_panel" aria-controls="subscription_request_panel" role="tab" data-toggle="tab">Subscription Requests <span className="badge">{subsciptions.length}</span></a>
          </li>
          <li role="presentation">
            <a href="#metadata_request_panel" aria-controls="metadata_request_panel" role="tab" data-toggle="tab">MetaData Requests <span className="badge">{metadataRequest.length}</span></a>
          </li>
          <li role="presentation"><a href="#historyrequest_panel" aria-controls="historyrequest_panel" role="tab" data-toggle="tab">History Requests <span className="badge">{historyRequests.length}</span></a></li>
        </ul>


        <div className="tab-content">
          <div role="tabpanel" className="tab-pane active" id="subscription_request_panel">
            <ReactTable
              data={subsciptions}
              columns={subsciptionsColumns}
              filterable
              defaultPageSize={10}
              maxWidth={100}
            />
          </div>
          <div role="tabpanel" className="tab-pane" id="metadata_request_panel">
            <ReactTable
              data={metadataRequest}
              columns={metadataColumns}
              filterable
              defaultPageSize={10}
            />
          </div>
          <div role="tabpanel" className="tab-pane" id="historyrequest_panel">
            <ReactTable
              data={historyRequests}
              columns={historyColumns}
              filterable
              defaultPageSize={10}
            />
          </div>
        </div>
         <JsonPrettyPrintModal 
          title={that.state.jsonPrettyPrintModal.title} 
          body={that.state.jsonPrettyPrintModal.body}
          json={that.state.jsonPrettyPrintModal.json}
          discard={that.state.jsonPrettyPrintModal.discard}
          show={that.state.jsonPrettyPrintModal.show}/>
      </div>
    )
  }
}


VenReportRequest.propTypes = {
  requests: PropTypes.array.isRequired,
  fetchRequestedReport: PropTypes.func.isRequired
}

export default VenReportRequest
