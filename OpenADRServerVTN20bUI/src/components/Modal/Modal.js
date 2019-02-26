import React from 'react';
import PropTypes from 'prop-types'

import Style from './style.js';

class Modal extends React.Component {
    constructor(props) {
        super(props);
        let effect = props.effect || 'fadeInDown';
      
        this.state = {
            visible : props.visible,
            style : Style[effect]
        }
        this.render = this.render.bind(this);
        this.componentWillReceiveProps = this.componentWillReceiveProps.bind(this);
        this.toggle = this.toggle.bind(this);
        this.setSize = this.setSize.bind(this);

        this.setSize(effect);
    }


    componentWillReceiveProps(nextProps) {
        this.setState({
            visible : nextProps.visible
        });
    }

    toggle() {
        this.setState({
            visible : !this.state.visible
        });
    }

    setSize(effect) {
        if(this.props.width) {
            Style[effect].panel.width = this.props.width + 'px';
            Style[effect].panel.marginLeft = '-' + this.props.width / 2 + 'px';
        }
        if(this.props.height) {
            Style[effect].panel.height = this.props.height + 'px';
            Style[effect].panel.marginTop = '-' + this.props.height / 2 + 'px';
        }
    }

    render() {
        var that = this;
        
        return (
            <div >
                <div style={this.state.visible ? this.state.style.mask : this.state.style.maskHidden}
                 onClick={(that.props.discard) ? that.props.discard : that.props.close}/>
                <div style={this.state.visible ? this.state.style.panel : this.state.style.panelHidden} className="modal-content">
                    {this.props.children}
                </div>
            </div>
        );
    }
}

Modal.propTypes = {
}


function chain(promises, currentResults, availableResults){

   

    var l = currentResults.length - 1;
    var defaultValue = currentResults.pop();
    var p;
    var res = currentResults;
    var last = null;
    var lastLast = null;
    var first = true;
    promises.reduce((previous, current, item) => {

        if(item >= currentResults.length){
            p = previous.then(function(result){
                if(first) first = false;
                else {
                    res.push((result) ? result : null);
                    availableResults[item - 1] = result;
                }
                var arr = Array.from(res);
                if(availableResults[item]){
                    arr.push(availableResults[item]);
                }
                else {
                    availableResults = arr;
                }
                
                return current.apply(null, arr);
            })

            return p;
        }
        else {
            return Promise.resolve();
        }

    }, Promise.resolve());

    p.then().catch(function(err){
        if(err) {
            console.error(err);
            return;
        }
        if(res.length > 0){
            chain(promises, res, availableResults);
        }
    });
}

Modal.chain = function(promises){

    chain(promises, [], []);
           
 
}


export default Modal;