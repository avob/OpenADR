import PropTypes from 'prop-types'
import React from 'react';

import Modal from './Modal.js'


import './syntaxHighlight.css';


var syntaxHighlight = function (json) {
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}
class JsonPrettyPrintModal extends Modal {

	constructor(props) {
	   super(props);
     this.state = {}
	   this.render = this.render.bind(this);
	}


	render(){
    var that = this;
    var data = (this.props.json != null) ? JSON.parse(this.props.json) : {}; 

		return (
			<Modal visible={this.props.show} closeFn={this.props.close} width={800} effect="fadeInUp"
        close={this.props.close}>
          <div className="modal-header">
            <button type="button" className="close" onClick={this.props.close}>&times;</button>
            <h4 className="modal-title">{this.props.title}</h4>
          </div>
          <div className="modal-body">
             {(() => {
                return <div>
                  <pre style={{"overflowY":"scroll", "height":"500px"}} dangerouslySetInnerHTML={{__html: syntaxHighlight(JSON.stringify(data, null, 2))}}></pre>
                </div>
          })()}

           
          </div>
          <div className="modal-footer">
            <button type="button" 
              className="btn btn-default" 
              onClick={this.props.close}>Close</button>
          </div>
      </Modal>
    )
	}
}

JsonPrettyPrintModal.propTypes = {
    show: PropTypes.bool.isRequired
}




export default JsonPrettyPrintModal;