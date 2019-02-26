import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'react-router'
import XmlRenderer from 'xml-renderer';
import ReactDOM from 'react-dom';
import 'react-table/react-table.css'

class XmlNode extends React.Component {
  constructor(props){
    super();
    this.state = {};
    if(props.hidden){
      this.state.display =  "none";
      this.state.button =   "+";
      this.state.hidden =  true;
    }
    else{
      this.state.display =  "block";
      this.state.button =   "-";
      this.state.hidden =  false;
    }

    this.state.tagStartColor = "green";
    this.state.tagEndColor = "green";
    this.state.textColor = "blue";
   
  }
  render(){
    var that = this;
    var nodeKey = this.props.renderer.key();
    var nodeName = this.props.renderer.getNode().tagName;
    var indent = 30;
    var view = null;
    if(this.props.renderer.getNode().childNodes.length == 1 && this.props.renderer.getNode().firstChild.nodeType == 3){
      view = <div key={ nodeKey +"_container"} style={{paddingLeft: indent+"px"}}>
                <span style={{color: this.state.tagStartColor}} key={nodeKey+"_start" }><i>&lt;{nodeName}&gt;</i></span>
                    <b style={{color: this.state.textColor}}>{this.props.renderer.getNode().firstChild.nodeValue}</b>
               <span style={{color: this.state.tagEndColor}} key={ nodeKey+"_end" }><i>&lt;/{nodeName}&gt;</i></span>
              </div>
    }
    else if(this.props.renderer.getNode().childNodes.length == 0){
      view = <div key={ nodeKey +"_container"} style={{paddingLeft: indent+"px"}}>
                <span style={{color: this.state.tagEndColor}} key={nodeKey+"_start" }><i>&lt;{nodeName}/&gt;</i></span>
              </div>
    }
    else {
      var childView = (this.state.hidden) ? "..." : <span style={{display:this.state.display}}>{ this.props.renderer.traverse() }</span>;
      var endPadding =  (this.state.hidden) ? "0px" : "10px";
      view = <div key={ nodeKey +"_container"} style={{paddingLeft: indent+"px"}}>
           <b><a onClick={function(){
              if(that.state.hidden){
                that.setState({display: "block", button:"-", hidden:false});
              }
              else {
                 that.setState({display: "none", button:"+", hidden:true});
              }
               
              }
           } style={{color:"red", fontSize:"20px", cursor:"pointer"}}>{this.state.button}</a></b>
             <span style={{color: this.state.tagStartColor}} key={ nodeKey+"_start" }><i>&lt;{nodeName}&gt;</i></span>
                {childView}
             <span style={{paddingLeft: endPadding, color: this.state.tagEndColor}} key={ nodeKey+"_end" }><i>&lt;/{nodeName}&gt;</i></span>
           </div>
    }

    return view;
  }
}

XmlNode.propTypes = {
  renderer: PropTypes.object.isRequired,
  indent: PropTypes.number.isRequired,
  nodeKey: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
}

class EventDetail extends React.Component {
  render() {
    const xr = new XmlRenderer();
    var pixelToDecal = 30;
    // xr.register('self::text()', function(renderer){
    //   var depth = renderer.key().split("/").length
    //   var indent = pixelToDecal * depth;
    //   return <div key={ renderer.key() +"_container"} style={{paddingLeft: indent+"px"}}><b>{renderer.getNode().nodeValue}</b></div>
    // });
    xr.register('self::*', function(renderer){
      var depth = renderer.key().split("/").length
      var indent = pixelToDecal;;
      var textValueView = null;
      
     
      if(depth > 2){
        textValueView =  <XmlNode renderer={renderer} hidden={true}/>
      }
      else {
        textValueView =  <XmlNode renderer={renderer}/>
      }
      
      return textValueView;
        
    });

    var xmlView = null;
    if(this.props.event.event){
      const xmlDom = new window.DOMParser().parseFromString(this.props.event.event, 'application/xml');
      xmlView = <div>{ xr.node(xmlDom).traverse() }</div>
       }

    return (
      <div style={{ margin: '0 auto' }} >
        <h2>Event</h2>
        <div style={{  textAlign:'left' , border:"gray solid 1px"}}>
            {xmlView}
        </div>
      
      </div>
    )
  }
}

EventDetail.propTypes = {
}

EventDetail.prototype.componentDidMount = function(){
  this.props.fetchEventDetail(this.props.params.eventId);
}



export default EventDetail
