import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'react-router'
import 'react-table/react-table.css'

import ConfirmModal from '../../../../../components/Modal/confirmModal.js'
import EditPullFrequencyModal from './editPullFrequencyModal.js'

  
class VenDetail extends React.Component {
  constructor(props){
    super(props);
    this.state = {};
    this.state.selected = {};
    this.state.modal = {
      show: false
    };
    this.state.editPullFrequencyModal = {
      show: false
    }
    this.state.pullFrequencySecondsEdit = null;
  }

  componentDidMount() {
    this.props.fetchVenDetail(this.props.params.venId);
  }

  componentWillUnmount(){
    this.props.unmount();
  }

  showConfirmationModal(body){
    var that = this;
    return new Promise(function(resolve, reject){
      that.setState({
        modal: {
          show: true
          , title: "Are you sure ?"
          , body: body
          , confirm: () => {
            resolve();
            that.setState({modal:{show:false}})
          }
          , close: () => {that.setState({modal:{show:false}})}
        }
      });
    });
  }

  showEditPullFrequencyModal(ven){
    var that = this;
    var pull =  ven.pullFrequencySeconds || 10;
    return new Promise(function(resolve, reject){
      that.setState({
        editPullFrequencyModal: {
          show: true
          , title: "Are you sure ?"
          , pullFrequencySeconds:  pull
          , confirm: (pullFrequency) => {
            resolve(pullFrequency);
            that.setState({editPullFrequencyModal:{show:false}})
          }
          , close: () => {that.setState({editPullFrequencyModal:{show:false}})}
        }
      });
    });
  }

  render () {
    var that = this;
    var venId = this.props.venId;
    var ven =this.props.ven
    

    var lastUpdateStr = "";
    if(ven && ven.lastUpdateDatetime){
      var date = new Date();
      date.setTime(ven.lastUpdateDatetime);
      lastUpdateStr = date.toLocaleDateString("fr-FR")+ " " + date.toLocaleTimeString("fr-FR");
    }

    var model = null;
    if(ven && ven.pushUrl){
      model = <tr>
              <th>Push url</th>
              <td>{ven.pushUrl}</td>
            </tr>
    }
    else if(ven && ven.pullFrequencySeconds){
       model = <tr>
              <th>Pull frequency</th>
              <td>{ven.pullFrequencySeconds} seconds <button className="pull-right" onClick={function() {
                
                that.showEditPullFrequencyModal(ven).then(function(pullFrequencySecondsEdit){
                  ven.pullFrequencySeconds = pullFrequencySecondsEdit
                     console.log(pullFrequencySecondsEdit)
                  that.props.updateVen(ven);
                  that.props.fetchVenDetail(that.props.params.venId);
                })

              }}>
                <i className="glyphicon glyphicon-edit"/>
                </button>
              </td>
            </tr>
    }
    else {
       model = <tr>
              <th>Pull frequency</th>
              <td>10 seconds <button className="pull-right" onClick={function() {
                
                that.showEditPullFrequencyModal(ven).then(function(pullFrequencySecondsEdit){
                      ven.pullFrequencySeconds = pullFrequencySecondsEdit
                      console.log(pullFrequencySecondsEdit)
                    that.props.updateVen(ven);
                    that.props.fetchVenDetail(that.props.params.venId);
                  })

              }}>
                <i className="glyphicon glyphicon-edit"/>
                </button></td>
            </tr>
    }

    var reportOnly = (ven.reportOnly == null) ? "" : (ven.reportOnly) ? "true":"false";
    var xmlSignature = (ven.xmlSignature == null) ? "" : (ven.xmlSignature) ? "true":"false";

    

    return (
    <div style={{ margin: '0 auto' }} >
      <h2>Ven</h2>

      <div className="col-md-6">
        <table className="table">
          <thead>
            <tr>
              <th>Informations</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th>Username</th>
              <td>{ven.username}</td>
            </tr>
            <tr>
              <th>Name</th>
              <td>{ven.oadrName}</td>
            </tr>
             
             <tr>
              <th>RegistrationId</th>
              <td>{ven.registrationId}</td>
            </tr>
             <tr>
              <th>Last update</th>
              <td>{lastUpdateStr}</td>
            </tr>
             <tr>
              <th>Transport</th>
              <td>{ven.transport}</td>
            </tr>
            <tr>
              <th>Oadr profil</th>
              <td>{ven.oadrProfil}</td>
            </tr>
            {model}
            <tr>
              <th>Report only</th>
              <td>{reportOnly}</td>
            </tr>
            <tr>
              <th>Xml signature</th>
              <td>{xmlSignature}</td>
            </tr>
            

          </tbody>
        </table>
      </div>
      <div className="col-md-6">
        <table className="table">
          <thead>
            <tr>
              <th>Actions</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th>Request Re-Registration</th>
              <td><button className="btn btn-default btn-sm" onClick = {
                () => {
                  that.showConfirmationModal("Do you want to request a re-registration payload from VEN ?").then(function(){
                    if(venId) that.props.requestReregistration(venId); 
                  })
                }
              } >send</button></td>
            </tr>
             <tr>
              <th>Send Cancel Registration</th>
              <td><button className="btn btn-default btn-sm" onClick = {
                () => {
                  that.showConfirmationModal("Do you want to send cancel registration payload to VEN ?").then(function(){
                    if(venId) that.props.cancelRegistration(venId);
                  })
                }
              } >send</button></td>
            </tr>



             <tr>
              <th>Request VEN RegisterReport</th>
              <td><button className="btn btn-default btn-sm" onClick = {
                () => {
                  that.showConfirmationModal("Do you want to request a RegisterReport payload from VEN ?").then(function(){
                    if(venId) that.props.requestVenRegisterService(venId);
                  })
                }
              } >send</button></td>
            </tr>


            <tr>
              <th>Send VTN RegisterReport</th>
              <td><button className="btn btn-default btn-sm" onClick = {
                () => {
                  that.showConfirmationModal("Do you want to send a RegisterReport payload to VEN ?").then(function(){
                    if(venId) that.props.sendVenRegisterService(venId); 
                  })
                }
              } >send</button></td>
            </tr>
          </tbody>
           
        </table>
      </div>
      <div className="col-md-6">
        <table className="table">
          <thead>
            <tr>
              <th>Links</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th>Reports</th>
              <td><Link to={{pathname: '/ven/' + venId + '/report' }}>view</Link></td>
            </tr>
            <tr>
              <th>Requests</th>
              <td><Link to={{pathname: '/ven/' + venId + '/request' }}>view</Link></td>
            </tr>
           
          </tbody>
           
        </table>
      </div>
      <ConfirmModal 
        title={that.state.modal.title} 
        body={that.state.modal.body}
        confirm={that.state.modal.confirm}
        close={that.state.modal.close}
        show={that.state.modal.show}/>
      <EditPullFrequencyModal
        title={that.state.editPullFrequencyModal.title} 
        pullFrequencySeconds={that.state.editPullFrequencyModal.pullFrequencySeconds}
        confirm={that.state.editPullFrequencyModal.confirm}
        close={that.state.editPullFrequencyModal.close}
        show={that.state.editPullFrequencyModal.show}/>
    </div>
  )}
  
  
}

VenDetail.propTypes = {

}

export default VenDetail
