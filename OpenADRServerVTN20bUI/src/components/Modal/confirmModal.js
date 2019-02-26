import PropTypes from 'prop-types'
import React from 'react';

import Modal from './Modal.js'

class ConfirmModal extends Modal {

	constructor(props) {
	  super(props);
    this.state = {}
	  this.render = this.render.bind(this);
	}

  // shouldComponentUpdate(nextProps, nextState){
  //     return (this.props.show != nextProps.show);
  //   }

	render(){
    var that = this;

    var hasNext = (typeof this.props.hasNext !== 'undefined') ? this.props.hasNext : false;
    var hasPrevious = (typeof this.props.hasPrevious !== 'undefined') ? this.props.hasPrevious : false;

    var width = (this.props.width != null) ? this.props.width : 800; 

    var rightBtn = <span><i className="glyphicon glyphicon-ok" /> Confirm</span>
    if(hasNext){
      rightBtn = <span><i className="glyphicon glyphicon-arrow-right" /> Next</span>
    }

    var leftBtn = <span><i className="glyphicon glyphicon-remove" /> Cancel</span>
    if(hasPrevious){
      leftBtn = <span><i className="glyphicon glyphicon-arrow-left" /> Previous</span>
    }
    
		return (
			<Modal visible={this.props.show} closeFn={this.props.close} width={width} effect="fadeInUp" 
        close={this.props.close}>
          <div className="modal-header">
            <button type="button" className="close" onClick={this.props.close}>&times;</button>
            <h4 className="modal-title">{this.props.title}</h4>
          </div>
          <div className="modal-body">
            {this.props.body}
          </div>
          <div className="modal-footer">
           
            <button type="button" 
              className="btn btn-default" 
              onClick={(hasPrevious) ? this.props.discard : this.props.close}>{leftBtn}</button>
             <button type="button" 
              className="btn btn-primary" 
              onClick={this.props.confirm}>{rightBtn}</button>
          </div>
      </Modal>
    )
	}
}

ConfirmModal.propTypes = {
    show: PropTypes.bool.isRequired
}



export default ConfirmModal;