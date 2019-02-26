import PropTypes from 'prop-types'
import React from 'react';

import Modal from './Modal.js'

class SelectDurationModal extends Modal {

	constructor(props) {
	   super(props);
     var hour = (this.props.hour != null) ? this.props.hour : 0;
     var minute = (this.props.minute != null) ? this.props.minute : 15;
     var day = (this.props.day != null) ? this.props.day : 0;
     var hourStep = (this.props.hourStep != null) ? this.props.hourStep : 1;
     var minuteStep = (this.props.minuteStep != null) ? this.props.minuteStep : 5;
     var dayStep = (this.props.dayStep != null) ? this.props.dayStep : 1;
     this.state = {};
     this.state.hour = hour;
     this.state.minute = minute;
     this.state.day = day;
     this.state.hourStep = hourStep;
     this.state.minuteStep = minuteStep;
     this.state.dayStep = dayStep;
	   this.render = this.render.bind(this);
	}

  componentWillReceiveProps(nextProps){
    var hour = (nextProps != null) ? nextProps.hour : 0;
     var minute = (nextProps != null) ? nextProps.minute : 15;
     var day = (nextProps != null) ? nextProps.day : 1;
     var hourStep = (nextProps != null) ? nextProps.hourStep : 1;
     var minuteStep = (nextProps.minuteStep != null) ? nextProps.minuteStep : 5;
     var dayStep = (nextProps.dayStep != null) ? nextProps.dayStep : 1;
     this.setState({day, hour, minute, dayStep, hourStep, minuteStep});
     super.componentWillReceiveProps(nextProps);
  }

	render(){
    var that = this;

    var hour = this.state.hour;
    var minute = this.state.minute;
    var day = this.state.day;
    var hourStep = this.state.hourStep;
    var minuteStep = this.state.minuteStep;
    var dayStep = this.state.dayStep;
    var hasNext = (typeof this.props.hasNext !== 'undefined') ? this.props.hasNext : false;
    var hasPrevious = (typeof this.props.hasPrevious !== 'undefined') ? this.props.hasPrevious : false;

    var minuteView = [];
    for(var i = 0 ; i < 60; i += minuteStep){
      minuteView.push( <option key={"minute_"+i} value={i}>{i}</option>);
    }

    var hourView = [];
    for(var i = 0 ; i < 25; i += hourStep){
      hourView.push( <option key={"hour_"+i} value={i}>{i}</option>);
    }

    var dayView = [];
    for(var i = 0 ; i < 31; i += dayStep){
      dayView.push( <option key={"day_"+i} value={i}>{i}</option>);
    }

    var rightBtn = <span><i className="glyphicon glyphicon-ok" /> Confirm</span>
    if(hasNext){
      rightBtn = <span><i className="glyphicon glyphicon-arrow-right" /> Next</span>
    }

    var leftBtn = <span><i className="glyphicon glyphicon-remove" /> Cancel</span>
    if(hasPrevious){
      leftBtn = <span><i className="glyphicon glyphicon-arrow-left" /> Previous</span>
    }

    var width = (this.props.width != null) ? this.props.width : 800; 
		return (
			<Modal visible={this.props.show} closeFn={this.props.close} width={width} effect="fadeInUp"
        close={this.props.close}>
          <div className="modal-header">
            <button type="button" className="close" onClick={this.props.close}>&times;</button>
            <h4 className="modal-title">{this.props.title}</h4>
          </div>
          <div className="modal-body">
            <form className="form-horizontal">
                <div className="form-group">
                  <label className="col-sm-4 control-label">{this.props.body}</label>
                  <div className="col-sm-8" >
                    <label className="col-sm-1 control-label" style={{paddingLeft: "5px", paddingRight:"5px"}}>
                      Days</label>
                    <div className="col-sm-3">
                      <select className="form-control" value={this.state.day} onChange={ (event) => {
                        that.setState({
                          day: parseInt(event.target.value)
                        })
                      }}>
                         {dayView}
                      </select>
                    </div>
                    <label className="col-sm-1 control-label" style={{paddingLeft: "5px", paddingRight:"5px"}}>
                      Hours</label>
                    <div className="col-sm-3">
                        <select className="form-control" value={this.state.hour} onChange={ (event) => {
                          that.setState({
                            hour: parseInt(event.target.value)
                          })
                        }}>
                           {hourView}
                        </select>
                    </div>
                    <label className="col-sm-1 control-label" style={{paddingLeft: "5px", paddingRight:"5px"}}>
                      Minutes</label>
                    <div className="col-sm-3">
                        <select className="form-control" value={this.state.minute} onChange={ (event) => {
                          that.setState({
                            minute: parseInt(event.target.value)
                          })
                        }}>
                           {minuteView}
                        </select>
                    </div>
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
              onClick={() => {that.props.confirm(that.state.hour, that.state.minute, that.state.day); }}>{rightBtn}</button>
          </div>
      </Modal>
    )
	}
}

SelectDurationModal.propTypes = {
    show: PropTypes.bool.isRequired
}

export default SelectDurationModal;