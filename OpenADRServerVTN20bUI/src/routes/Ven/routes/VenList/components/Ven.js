import React from 'react'
import PropTypes from 'prop-types'
import ReactTable from 'react-table'
import { IndexLink, Link } from 'react-router'
import 'react-table/react-table.css'
import VenCreateModal from './VenCreateModal'


const columns = [{
    Header: 'Username',
    accessor: 'username',
    Cell: props => <Link to={{
      pathname: '/ven/' + props.original.username
    }}>{props.original.username}</Link>
  }, {
    Header: 'Name',
    accessor: 'oadrName',
    maxWidth: '150'
  }, {
    Header: 'Profil',
    accessor: 'oadrProfil',
    maxWidth: '100'
  }, {
    Header: 'Last update',
    Cell: props => {
      if(props.original.lastUpdateDatetime){
        var date = new Date();
        date.setTime(props.original.lastUpdateDatetime);

        return date.toLocaleDateString("fr-FR")+ " " + date.toLocaleTimeString("fr-FR");
      }
      return null;
    }
  }
  , {
    Header: 'viewReport',
    Cell: props => <Link to={{
      pathname: '/ven/' + props.original.username + '/report'
    }}>report</Link>,
    maxWidth: '100'
  }
  , {
    Header: 'viewRequest',
    Cell: props => <Link to={{
      pathname: '/ven/' + props.original.username + '/request'
    }}>request</Link>,
    maxWidth: '100'
  }
 ]

class VenList extends React.Component {

  constructor(props) {
      super(props);
      this.state = {};
      this.state.venCreateModal = {
        show: false
      }
  }

  componentDidMount() {
   
    this.props.fetchVen();
  }

  componentWillUnmount(){
    this.props.unmount();
  }

  showVenCreateModal(){
    var that = this;
    return new Promise(function(resolve, reject){
      that.setState({
        venCreateModal: {
          show: true
          , title: "New VEN"
          , confirm: (ven) => {
            resolve(ven);
            that.setState({venCreateModal:{show:false}})
          }
          , close: () => {that.setState({venCreateModal:{show:false}})}
        }
      });
    });
  }

  render() {
    
    var that = this;
      return (
      <div style={{ margin: '0 auto' }} >
        <h2>Ven<a className="btn btn-sm btn-default pull-right" onClick={() => {
          console.log()
          that.showVenCreateModal().then(function(venCredentials){
            that.props.createVen(venCredentials)
          })
        }}><i className="glyphicon glyphicon-plus"/> Create VEN</a></h2>
        <ReactTable
          data={this.props.vens}
          columns={columns}
          defaultPageSize={10}
        />
        <VenCreateModal
          title={that.state.venCreateModal.title} 
          confirm={that.state.venCreateModal.confirm}
          close={that.state.venCreateModal.close}
          show={that.state.venCreateModal.show}/>
      </div>
    )
  }
}


VenList.propTypes = {
  vens: PropTypes.array.isRequired
  , fetchVen: PropTypes.func.isRequired
}


export default VenList
