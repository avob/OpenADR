import PropTypes from 'prop-types'
import React from 'react';

import Modal from '../../../../../components/Modal/Modal.js'

class EditPullFrequencyModal extends Modal {

	constructor(props) {
	  super(props);
    this.state = {}
    this.state.pullFrequencySeconds = 10;
	  this.render = this.render.bind(this);
	}

  componentWillReceiveProps(nextProps){
    var pullFrequencySeconds = nextProps.pullFrequencySeconds
    this.setState({pullFrequencySeconds});
    super.componentWillReceiveProps(nextProps);
  }


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
            <form>
              <div className="form-group">
                <label >Pull frequency in seconds</label>
                <input type="number" className="form-control" id="exampleInputEmail1" value={that.state.pullFrequencySeconds}
                  onChange= {function(e) {that.setState({pullFrequencySeconds: e.target.value})}}/>
              </div>
            </form>
          </div>
          <div className="modal-footer">
           
            <button type="button" 
              className="btn btn-default" 
              onClick={(hasPrevious) ? this.props.discard : this.props.close}>{leftBtn}</button>
             <button type="button" 
              className="btn btn-primary" 
              onClick={function(){
                that.props.confirm(that.state.pullFrequencySeconds);
              }}>{rightBtn}</button>
          </div>
      </Modal>
    )
	}
}

EditPullFrequencyModal.propTypes = {
    show: PropTypes.bool.isRequired
}



export default EditPullFrequencyModal;