import React from 'react'
import PropTypes from 'prop-types'
import ReactTable from 'react-table'
import 'react-table/react-table.css'

class VenReportDescription extends React.Component {
  constructor(props){
    super(props);
    this.state = {};
    this.state.selected = {};
  }

  componentDidMount() {
    this.props.fetchAvailableReportDescription(this.props.params.venId, this.props.params.reportSpecifierId);
  }

  componentWillUnmount(){
    this.props.unmount();
  }

  render() {
      var that = this;
      const columns = [{
        Header: '',
        minWidth:20,
          Filter: function(){ return <input style={{float:"left"}} type="checkbox" onClick={function(e){
            var selected = {};
            if(e.target.checked){
              for(var i in that.props.reportDescriptions){
                var desc = that.props.reportDescriptions[i];
                if(desc.reportRequestBackDuration == null && desc.reportRequestGranularity == null){
                  selected[desc.rid] = true;
                }
                
              }
            }
            that.setState({selected: selected})
          }}/>},
        Cell: function(props){
          var isCurrentlySelected = that.state.selected[props.original.rid] ? true : false;
          var isCurrentlySelectable = (props.original.reportRequestBackDuration != null && props.original.reportRequestGranularity != null) ? true : false;
          var select = function(){
            var selected = that.state.selected
            if(isCurrentlySelected){
              delete selected[props.original.rid];
            }
            else {
              selected[props.original.rid] = true;
            }
            
            that.setState({selected: selected});
          }
          return <input type = "checkbox" onClick={select} checked={isCurrentlySelected} disabled={isCurrentlySelectable}/>
        }
      }, {
        Header: 'RID',
        accessor: 'rid',
        filterMethod: (filter, row) => {
          return row[filter.id].includes(filter.value)
        }
      }
      , {
        Header: 'itemDescription',
        accessor: 'itemDescription',
        filterMethod: (filter, row) => {
          return row[filter.id].includes(filter.value)
        }
      }, {
        Header: 'itemUnits',
        accessor: 'itemUnits' 
      }, {
        Header: 'siScaleCode',
        accessor: 'siScaleCode' ,
        minWidth:50
      }, {
        Header: 'MinPeriod',
        accessor: 'oadrMinPeriod',
        minWidth:50
        
      }, {
        Header: 'MaxPeriod',
        accessor: 'oadrMaxPeriod',
        minWidth:50
        
      }, {
        Header: 'OnChange',
        //accessor: 'oadrOnChange',
        Cell: function(props) {
          return (props.original.oadrOnChange) ? "true" : "false";
        },
        minWidth:50
        
      }, {

      Header: 'Subscription',
       Filter: function(){ 

            var text =  "Sub. Selected";
            var onClick = null;
            if(that.props.reportRequest != null && that.props.reportRequest.reportRequestBackDuration != null && that.props.reportRequest.reportRequestGranularity != null){
              text = "Add Selected";
              onClick = function(){
                that.props.subscribe(that.props.params.venId, that.props.params.reportSpecifierId, Object.keys(that.state.selected).join("|"), null, null
                  , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);})
                that.setState({selected:[]})
              }
            }
            else {
              onClick = function(){
                var granularity = prompt("Granularity ?");
                var reportBackDuration = prompt("ReportBackDuration ?");
                that.props.subscribe(that.props.params.venId, that.props.params.reportSpecifierId, Object.keys(that.state.selected).join("|"), granularity, reportBackDuration
                  , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);})
                that.setState({selected:[]})
              }
            }
            return <button className="btn btn-default btn-xs" style={{float:"left"}} onClick={ onClick }>{text}</button>},
      Cell: function(props){
      

        if(props.original.reportRequestId != "0" 
          && (props.original.reportRequestBackDuration == null && props.original.reportRequestGranularity == null)){
          return <button className="btn btn-default btn-xs" onClick={ () => {

            that.props.subscribe(that.props.params.venId, that.props.params.reportSpecifierId, props.original.rid, null, null
              , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);})
          }}>Add.</button>
        }
        
        else if(props.original.reportRequestId != "0" && (props.original.reportRequestBackDuration == "P0D" && props.original.reportRequestGranularity == "P0D")){
          return <span><button className="btn btn-default btn-xs" onClick={ () => {
             that.props.subscribe(that.props.params.venId, that.props.params.reportSpecifierId, props.original.rid, "P0D", "P0D"
                , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);})
            
          }}>Req.</button></span>


        }
        else if(props.original.reportRequestId != "0" && (props.original.reportRequestBackDuration != null && props.original.reportRequestGranularity != null)){
          return <span><button className="btn btn-default btn-xs" onClick={ () => {
            that.props.unsubscribe(that.props.params.venId,props.original.reportRequestId
              , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);});
            
          }}>UnSub.</button><button className="btn btn-default btn-xs" onClick={ () => {
            that.props.removeFromReport(that.props.params.venId,that.props.params.reportSpecifierId, props.original.rid
              , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);});
            
          }}>Remove</button></span>


        }
        else{
          if(props.original.oadrMinPeriod == "P0D" && props.original.oadrMaxPeriod == "P0D") {
            return <button className="btn btn-default btn-xs" onClick={ () => {
              that.props.subscribe(that.props.params.venId, that.props.params.reportSpecifierId, props.original.rid, "P0D", "P0D"
                , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);})
            }}>Req.</button>
          }
          else if(props.original.oadrMinPeriod == "P0D") {
            return <span><button className="btn btn-default btn-xs" onClick={ () => {
              that.props.subscribe(that.props.params.venId, that.props.params.reportSpecifierId, props.original.rid, "P0D", "P0D"
                , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);})
            }}>Req.</button>
            <button className="btn btn-default btn-xs" onClick={ () => {
              var granularity = prompt("Granularity ?");
              var reportBackDuration = prompt("ReportBackDuration ?");

              that.props.subscribe(that.props.params.venId, that.props.params.reportSpecifierId, props.original.rid, granularity, reportBackDuration
                , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);})
            }}>Sub.</button></span>
          }
          else {
            return <button className="btn btn-default btn-xs" onClick={ () => {
              var granularity = prompt("Granularity ?");
              var reportBackDuration = prompt("ReportBackDuration ?");

              that.props.subscribe(that.props.params.venId, that.props.params.reportSpecifierId, props.original.rid, granularity, reportBackDuration
                , () => {that.props.fetchAvailableReportDescription(that.props.params.venId, that.props.params.reportSpecifierId);})
            }}>Sub.</button>
          }

          
        }
      }
    }]

      return (
      <div style={{ margin: '0 auto' }} >
        <h2>VenReportDescription</h2>
        <ReactTable
        className="-striped"
        data={this.props.reportDescriptions}
        columns={columns}
        hideFilterInput
        filterable={true}
        onFilteredChange={(column, value) => {
          that.setState({selected:{}});
          that.forceUpdate();
        }}
        SubComponent={(row) => {
          return (
            <div>
              <ul>
                <li>ReportRequest: {row.original.reportRequestId}</li>
                <li>ReportRequestBackDuration: {row.original.reportRequestBackDuration}</li>
                <li>ReportRequestGranularity: {row.original.reportRequestGranularity}</li>
              </ul>
            </div>
          )
        }}
        defaultPageSize={10}
      />
      </div>
    )
  }
}


VenReportDescription.propTypes = {
  reportDescriptions: PropTypes.array.isRequired,
  fetchAvailableReportDescription: PropTypes.func.isRequired
}

export default VenReportDescription
