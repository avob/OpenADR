import PropTypes from 'prop-types'
import React from 'react';

import Modal from '../../../../../components/Modal/Modal.js'
import ConfirmModal from '../../../../../components/Modal/confirmModal.js'
import VenCreateLoginModal from './VenCreateLoginModal.js'
import VenCreateSelectAuthenticationModal from './VenCreateSelectAuthenticationModal.js'

const title = "Create a new VEN"
class VenCreateModal extends React.Component {

	constructor(props) {
    super(props);
    this.state = {};
    
    this.state.show =  false;
    this.state.confirmModal = {
      show: false
    };
    this.state.loginModal = {
      show: false
    };
    this.state.authenticationModal = {
      show: false
    };

	}

  shouldComponentUpdate(nextProps, nextState){
    return (this.state.show != nextProps.show || this.state.show );
  }
  componentWillUpdate(nextProps, nextState) {
    if (nextProps.show == true && this.state.show == false) {
      this.chain();
      this.setState({show:true});
    }
    else if (nextProps.show == false) {
      this.setState({confirmModal:{show: false}, show: false});
    }
  }

  showConfirmModal(body, hasNext, confirmCallback){
      var that = this;
      var next = (hasNext) ? hasNext : false;
      return new Promise(function(resolve, reject){
        that.setState({
          confirmModal: {
            show: true
            , title: title
            , body: body
            , hasPrevious: false
            , hasNext: next
            , confirm: () => {
                resolve();
                that.setState({show:true, confirmModal:{show:false}});
                
            }
            , discard: () => {
              reject();
              that.setState({show: false,confirmModal:{show:false}});
            }
            , close: () => {
              reject();
              that.setState({show: false,confirmModal:{show:false}});
            }
          }
        });
      });
  }

  showLoginModal(hasNext){
      var that = this;
      var next = (hasNext) ? hasNext : false;
      return new Promise(function(resolve, reject){
        that.setState({
          loginModal: {
            show: true
            , title: title
            , hasPrevious: false
            , hasNext: next
            , confirm: (venCredentials) => {
                resolve(venCredentials);
                that.setState({show:true, loginModal:{show:false}});
                
            }
            , discard: () => {
              reject();
              that.setState({show: false,loginModal:{show:false}});
            }
            , close: () => {
              reject();
              that.setState({show: false,loginModal:{show:false}});
            }
          }
        });
      });
  }

  showAuthenticationModal(hasNext){
      var that = this;
      var next = (hasNext) ? hasNext : false;
      return new Promise(function(resolve, reject){
        that.setState({
          authenticationModal: {
            show: true
            , title: title
            , hasPrevious: false
            , hasNext: next
            , confirm: (authenticationType, venName) => {
                resolve({type: authenticationType, name:venName});
                that.setState({show:true, authenticationModal:{show:false}});
                
            }
            , discard: () => {
              reject();
              that.setState({show: false,authenticationModal:{show:false}});
            }
            , close: () => {
              reject();
              that.setState({show: false,authenticationModal:{show:false}});
            }
          }
        });
      });
  }

  chain(){
    var that = this;
    var promisesToMake = [
      () => { 
        return that.showAuthenticationModal(true);
      }
      , (authentication) => { 
        if(authentication.type == "x509_RSA"){
          return Modal.chain([
            () =>  {
              return that.showConfirmModal(
                <div>
                    <p>Create a new VEN using x509 RSA Certification based authentication</p>
                    <p>VEN name: <strong>{authentication.name}</strong></p>
                  </div>
              ,  false);
            }
            , () =>  {
              return new Promise(function(resolve, reject){
                 that.props.confirm(authentication);
              });
            }
          ]);
        }
        else if(authentication.type == "x509_ECC"){
          return Modal.chain([
            () =>  {
              return that.showConfirmModal(
                
                <div>
                    <p>Create a new VEN using x509 ECC Certification based authentication</p>
                    <p>VEN name: <strong>{authentication.name}</strong></p>
                  </div>
              , false);
            }
            , () =>  {
              return new Promise(function(resolve, reject){
                 that.props.confirm(authentication);
              });
            }
          ]);
        }
        else {
          return Modal.chain([
            () => {
                return that.showLoginModal();
            }
            , (venCredentials) => {
               return that.showConfirmModal(
                  <div>
                    <p>Create a new VEN using login/password based authentication</p>
                    <p>VEN name: <strong>{authentication.name}</strong></p>
                    <p>VEN login: <strong>{venCredentials.login}</strong></p>
                  </div>
                ,  false);
            }
            , (venCredentials) => {
              that.props.confirm({type: authentication.type, name: authentication.name, login: venCredentials.login, password: venCredentials.password})
            }

          ])
        }
        
      } 
    ];
    Modal.chain(promisesToMake);
  }

  render() {
    var that = this;
    return (<div>

        <ConfirmModal 
          title={that.state.confirmModal.title} 
          body={that.state.confirmModal.body}
          confirm={that.state.confirmModal.confirm}
          discard={that.state.confirmModal.discard}
          close={that.state.confirmModal.close}
          show={that.state.confirmModal.show}
          hasPrevious={that.state.confirmModal.hasPrevious}
          hasNext={that.state.confirmModal.hasNext}/>

        <VenCreateLoginModal 
          title={that.state.loginModal.title} 
          body={that.state.loginModal.body}
          confirm={that.state.loginModal.confirm}
          discard={that.state.loginModal.discard}
          close={that.state.loginModal.close}
          show={that.state.loginModal.show}
          hasPrevious={that.state.loginModal.hasPrevious}
          hasNext={that.state.loginModal.hasNext}/>

        <VenCreateSelectAuthenticationModal 
          title={that.state.authenticationModal.title} 
          confirm={that.state.authenticationModal.confirm}
          discard={that.state.authenticationModal.discard}
          close={that.state.authenticationModal.close}
          show={that.state.authenticationModal.show}
          hasPrevious={that.state.authenticationModal.hasPrevious}
          hasNext={that.state.authenticationModal.hasNext}/>

      </div>)
  
  }
}

VenCreateModal.propTypes = {
    show: PropTypes.bool.isRequired
}

export default VenCreateModal;