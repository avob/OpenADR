import PropTypes from 'prop-types'
import React from 'react';

import Modal from '../../../../../components/Modal/Modal.js'

const types = [
	<option key="option_x509_RSA" value="x509_RSA">x509 RSA Certificate</option>,
    <option key="option_x509_ECC" value="x509_ECC">x509 ECC Certificate</option>,
    <option key="option_password" value="password">Login / Password</option>
]
const defaultType = "x509_RSA";

class VenCreateSelectAuthenticationModal extends Modal {

	constructor(props) {
	   super(props);
     this.state = {};
     // this.state.type = "x509_RSA"
    this.state.type = defaultType;
    this.state.name = "";

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

    function checkName(){
    	return that.state.name != "";
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
		    	<div className={(!that.state.hasErr) ? "form-group" : checkName() ? "form-group has-success" : "form-group has-error" }>
		            <label>Select VEN name: </label>
		              <input type="text" className="form-control" value={that.state.name}
                    		onChange= {function(e) {that.setState({name: e.target.value})}}/>
	            </div>
	            <div className="form-group">
		            <label>Select VEN authentication method: </label>
		             <select  className="form-control" value={that.state.type}
		                onChange={(e) => {
		                  this.setState({type: e.target.value})}
		                }>
		                {types}
	             	</select>
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
		          	if(checkName()){
		          		that.props.confirm(that.state.type, that.state.name);
		            	that.setState({hasErr:false, type: defaultType, name: ""})
		          	}
		          	else {
		          		that.setState({hasErr:true});
		          	}
		            

		          }}>{rightBtn}</button>
		    </div>
		  </Modal>
		)
	}
}

VenCreateSelectAuthenticationModal.propTypes = {
    show: PropTypes.bool.isRequired
}

export default VenCreateSelectAuthenticationModal;