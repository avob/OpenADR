import React from 'react'
import PropTypes from 'prop-types'
import ReactTable from 'react-table'
import { IndexLink, Link } from 'react-router'
import 'react-table/react-table.css'

class VenReport extends React.Component {
  componentDidMount() {
    this.props.fetchAvailableReport(this.props.params.venId);
  }

  componentWillUnmount(){
    this.props.unmount();
  }

  render() {
      var that = this;
      const columns = [{
        Header: 'ReportSpecifier',
        accessor: 'reportSpecifierId',
        filterMethod: (filter, row) => {
          return row[filter.id].includes(filter.value)
        }
                    
      }
      , {
        Header: 'ReportName',
        accessor: 'reportName' 
      }, {
      Header: 'Descriptions',
      Cell: function(props){
        return <Link to={{
          pathname: '/ven/' + that.props.params.venId + '/report/'+ props.original.reportSpecifierId
        }}>view</Link>
      },
        maxWidth: '100',
        filterable:false
    }, {
      Header: 'Requests',
      Cell: function(props){
        return <Link to={{
          pathname: '/ven/' + that.props.params.venId + '/report/'+ props.original.reportSpecifierId + "/request"
        }}>view</Link>
      },
        maxWidth: '100',
        filterable:false
    }
    ]

      return (
      <div style={{ margin: '0 auto' }} >
        <h2>VenReport</h2>
          <ReactTable
            data={this.props.reports}
            columns={columns}
            filterable
            defaultPageSize={10}
          />
      </div>
    )
  }
}


VenReport.propTypes = {
  reports: PropTypes.array.isRequired,
  fetchAvailableReport: PropTypes.func.isRequired
}

export default VenReport
