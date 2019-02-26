import React from 'react'
import PropTypes from 'prop-types'
import ReactTable from 'react-table'
import { IndexLink, Link } from 'react-router'
import 'react-table/react-table.css'

class EventList extends React.Component {
  render() {
    var that = this;
    var createRequest = function(object){
      var request = {
      
      }
      return that.props.createEvent(request);
    }

    const columns = [{
      Header: 'EventID',
      accessor: 'eventId' 
    },{
      Header: 'Modification',
      accessor: 'modificationNumber' 
    }, {
      Header: 'Start',
      accessor: 'start' 
    }, {
      Header: 'Duration',
      accessor: 'duration'
    }, {
      Header: 'View Detail',
      Cell: props => <Link to={{pathname: '/event/'+props.original.eventId}}>view</Link>
            
         
    }
    ]

    return (
    <div style={{ margin: '0 auto' }} >
      <h2>Event</h2>
      <ReactTable
      data={this.props.events}
      columns={columns}
      defaultPageSize={10}
    />
    </div>
    )
  }
}

EventList.propTypes = {
  events: PropTypes.array.isRequired,
  fetchEvent: PropTypes.func.isRequired,
  createEvent: PropTypes.func.isRequired
}

EventList.prototype.componentDidMount = function(){
  this.props.fetchEvent();
}


export default EventList
