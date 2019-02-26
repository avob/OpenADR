import PropTypes from 'prop-types'
import React from 'react';

import Modal from '../../../../../components/Modal/Modal.js'

class VenCreateLoginModal extends Modal {

	constructor(props) {
	   super(props);
     this.state = {};
     this.state.login = "";
     this.state.password = "";
     this.state.password_confirm = "";
	}


	render(){
    var that = this;

    var hasNext = (typeof this.props.hasNext !== 'undefined') ? this.props.hasNext : false;
    var hasPrevious = (typeof this.props.hasPrevious !== 'undefined') ? this.props.hasPrevious : false;

    var rightBtn = <span><i className="glyphicon glyphicon-ok" /> Confirm</span>
    if(hasNext){
      rightBtn = <span><i className="glyphicon glyphicon-arrow-right" /> Next</span>
    }

    var leftBtn = <span><i className="glyphicon glyphicon-remove" /> Cancel</span>
    if(hasPrevious){
      leftBtn = <span><i className="glyphicon glyphicon-arrow-left" /> Previous</span>
    }

    
    function checkNotEmpty(property) {
      if(that.state[property] != ""){
          return true
        }
        else {
          return false
        }
    }

    function checkPasswordConfirmation(){
      return that.state.password == that.state.password_confirm;
    }

    function checErr(){
      return (that.state.hasErr ) ? that.state.hasErr : false;
    }

    var helpBlock = null;
    var passwordConfirmationDontMatch = false;
    if(that.state.hasErr && !checkPasswordConfirmation()){
      passwordConfirmationDontMatch = true;
      helpBlock = <span  className="help-block">Password and password confirmation are not equals</span>
    }
		return (
			<Modal visible={this.props.show} closeFn={this.props.close} effect="fadeInUp"
        close={this.props.close}>
          <div className="modal-header">
            <button type="button" className="close" onClick={this.props.close}>&times;</button>
            <h4 className="modal-title">{this.props.title}</h4>
          </div>
          <div className="modal-body">
            <form className="form">
             <div className={!checErr() ? "form-group" : checkNotEmpty("login") ? "form-group has-success" : "form-group has-error"}>
                <label>Select VEN login </label>
                <input type="text" className="form-control" value={that.state.login}
                    onChange= {function(e) {that.setState({login: e.target.value})}}/>
              </div>
              <div className={!checErr() ? "form-group" : checkNotEmpty("password") ? "form-group has-success" : "form-group has-error"}>
                <label>Select VEN password </label>
                <input type="password" className="form-control" value={that.state.password}
                    onChange= {function(e) {that.setState({password: e.target.value})}}/>
              </div>
               <div className={!checErr() ? "form-group" : checkNotEmpty("password_confirm") && checkPasswordConfirmation()? "form-group has-success" : "form-group has-error"}>
                <label>Confirm VEN password </label>
                <input type="password" className="form-control"  value={that.state.password_confirm}
                    onChange= {function(e) {that.setState({password_confirm: e.target.value})}}/>
                    {helpBlock}
              </div>
            </form>
          </div>
          <div className="modal-footer">
            <button type="button" 
              className="btn btn-default" 
              onClick={(hasPrevious) ? this.props.discard : this.props.close}>{leftBtn}</button>
             <button type="button" 
              className="btn btn-primary" 
              onClick={() => {
                if(checkNotEmpty("login")
                  && checkNotEmpty("password")
                  && checkNotEmpty("password_confirm")
                  && checkPasswordConfirmation()){
                    that.setState({hasErr: false, login:"", password:"", password_confirm:""})
                    that.props.confirm({login:that.state.login, password:that.state.password}); 
                
                }
                that.setState({hasErr: true})

              }}>{rightBtn}</button>
          </div>
      </Modal>
    )
	}
}

VenCreateLoginModal.propTypes = {
    show: PropTypes.bool.isRequired
}

export default VenCreateLoginModal;