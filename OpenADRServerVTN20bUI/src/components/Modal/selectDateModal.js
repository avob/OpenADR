import PropTypes from 'prop-types'
import React from 'react';

import Modal from './Modal.js'

import DatePicker from 'react-date-picker'
import TimePicker from 'react-time-picker'

class SelectDurationModal extends Modal {

	constructor(props) {
	   super(props);
     var now = new Date();
     var date = (this.props.date != null) ? this.props.date : now;
     this.state = {
        date: now
        , time: '00:00'
      }

	   this.render = this.render.bind(this);
	}

  componentWillReceiveProps(nextProps){
    var now = new Date();
    var date = (nextProps.date != null) ? nextProps.date : now;
    var time = "00:00"
    this.setState({date, time});
    super.componentWillReceiveProps(nextProps);
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
		return (
			<Modal visible={this.props.show} closeFn={this.props.close} effect="fadeInUp"
        close={this.props.close}>
          <div className="modal-header">
            <button type="button" className="close" onClick={this.props.close}>&times;</button>
            <h4 className="modal-title">{this.props.title}</h4>
          </div>
          <div className="modal-body">
            <form className="form-horizontal">
                <div className="form-group">
                  <label className="col-sm-4 control-label">{this.props.body}</label>
                  <div className="col-sm-3">
                       <DatePicker
                        onChange={(date) => {that.setState({ date: date });} }
                        value={this.state.date}
                        maxDate={this.props.maxDate}
                        minDate={this.props.minDate}/>
                      
                  </div>
                  <div className="col-sm-3">
          
                        <TimePicker
                         onChange={(time) => {that.setState({ time: time });} }
                        value={this.state.time}/>
                  </div>
                  
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
                var t = that.state.time.split(":")
                that.state.date.setHours(t[0])
                that.state.date.setMinutes(t[1])
                that.state.date.setSeconds(0)
                that.props.confirm(that.state.date); 

              }}>{rightBtn}</button>
          </div>
      </Modal>
    )
	}
}

SelectDurationModal.propTypes = {
    show: PropTypes.bool.isRequired
}

export default SelectDurationModal;