import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as eventActions from '../../actions/eventActions';
import * as venActions from '../../actions/venActions';


import { withStyles } from '@material-ui/core/styles';

import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

import EventDetailDescriptor from '../EventDetail/EventDetailDescriptor'
import EventDetailActivePeriod from '../EventDetail/EventDetailActivePeriod'
import EventDetailSignal from '../EventDetail/EventDetailSignal'
import EventDetailTarget from '../EventDetail/EventDetailTarget'
import EventDetailVenResponse from '../EventDetail/EventDetailVenResponse'

import green from '@material-ui/core/colors/green';
import red from '@material-ui/core/colors/red';

import { history } from '../../store/configureStore';


function TabContainer( props ) {
  return (
  <Typography component="div" style={ { padding: 8 * 3 } }>
    { props.children }
  </Typography>
  );
}

const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: theme.spacing.unit,
    marginRight: theme.spacing.unit,
  },
  dense: {
    marginTop: 19,
  },
  menu: {
    width: 200,
  },
  formControl: {
    margin: theme.spacing.unit,
    display:'flex'
  },
  card: {
    maxWidth: 350,
    minWidth: 350,
  },
  media: {
    height: 40,
    paddingTop: 10,
    paddingRight: 10
  },
  button: {
    margin: theme.spacing.unit,
  },
  success: {
    backgroundColor: green[600],
  },
  error: {
    backgroundColor: red[600],
  },
  iconButton: {
    marginTop: 10
  },
});

export class EventDetailPage extends React.Component {

  constructor() {
    super()
    this.state = {
      value: 0,
      copySignals: [],
      copyTargets: [],
      editMode: false
    };
  }

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/event/detail/"+this.props.match.params.id+"/descriptor")
        break;
      case 1:
        history.push("/event/detail/"+this.props.match.params.id+"/activeperiod")
        break;
      case 2:
        history.push("/event/detail/"+this.props.match.params.id+"/signal")
        break;
      case 3:
        history.push("/event/detail/"+this.props.match.params.id+"/target")
        break;
      case 4:
        history.push("/event/detail/"+this.props.match.params.id+"/venresponse")
        break;
      default:
        break;
    }

  };

  componentDidMount() {
    this.props.vtnConfigurationActions.loadMarketContext();
    this.props.vtnConfigurationActions.loadGroup();
    this.props.eventActions.loadEventDetail(this.props.match.params.id);
    this.props.eventActions.loadEventVenResponse(this.props.match.params.id);

    if(this.props.event_detail.event.signals) {
      this.setState({copySignals: this.props.event_detail.event.signals});
    }
    if(this.props.event_detail.event.targets) {
      this.setState({copyTargets: this.props.event_detail.event.targets});
    }

    switch(this.props.match.params.panel){
      case "descriptor":
        this.setState({value:0});
        break;
      case "activeperiod":
        this.setState({value:1});
        break;
      case "signal":
        this.setState({value:2});
        break;
      case "target":
        this.setState({value:3});
        break;
      case "venresponse":
        this.setState({value:4});
        break;
      default:
        break;
    }
    

  }

  componentDidUpdate(prevProps, prevState) {
    if(this.props.event_detail.event.signals !== prevProps.event_detail.event.signals) {
      this.setState({copySignals: this.props.event_detail.event.signals});
    }
    if(this.props.event_detail.event.targets !== prevProps.event_detail.event.targets) {
      this.setState({copyTargets: this.props.event_detail.event.targets});
    }

    
  }

  updateCopySignals = (index, newSignal) => {
    var copySignals = this.state.copySignals;
    copySignals[index] = newSignal;
    this.setState({copySignals: copySignals, editMode: true});
  }

  removeCopySignals = (index) => {
    var copySignals = this.state.copySignals;
    copySignals.splice(index, 1)
    this.setState({copySignals: copySignals, editMode: true});
  }

  updateCopyTargets = (newTargets) => {
    this.setState({copyTargets: newTargets, editMode: true});
  }

   addCopySignals = (index) => {
    var copySignals = this.state.copySignals;
    copySignals.push({
        signalName: "",
        signalType: "",
        unitType: "",
        intervals: [],
        currentValue: "",
      });
    this.setState({copySignals: copySignals, editMode: true});
  }

  updateEvent = (published) => {
    var dto = {
      published:published,
      signals: this.state.copySignals,
      targets: this.state.copyTargets
    }
    this.props.eventActions.updateEvent(this.props.match.params.id, dto)
    this.setState({editMode: false});
  }

  onVenSuggestionsFetchRequested = (e) => {
    var filters = [];
    filters.push({type:"VEN", value:e.value});
    this.props.venActions.searchVen(filters, 0, 5);
  }

  onVenSuggestionsClearRequested = () => {
  }

  onVenSuggestionsSelect = (ven) => {
     var filters = this.state.filters;
    filters.push({type:"VEN", value:ven.username});
    this.setState({filters});
    this.refreshEvent();
  }

  render() {
    const {classes, event_detail} = this.props;
    const {value} = this.state;
    if(!event_detail.event.activePeriod ) return null;
    return (
     <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Descriptor" />
        <Tab label="Active Period" />
        <Tab label="Signals" />
        <Tab label="Targets" />
        <Tab label="Ven Responses" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <TabContainer>
                <EventDetailDescriptor classes={classes} event={event_detail.event} 
                  activeEvent={this.props.eventActions.activeEvent}
                  cancelEvent={this.props.eventActions.cancelEvent}
                  publishEvent={this.props.eventActions.publishEvent}/>
                     
                       </TabContainer> }     
      { value === 1 && <TabContainer>
                <EventDetailActivePeriod classes={classes} event={event_detail.event}/>
                     
                       </TabContainer> }
      { value === 2 && <TabContainer>
                <EventDetailSignal classes={classes} event={event_detail.event} 
                  updateEvent={this.updateEvent}
                   
                  
                  editMode={this.state.editMode}
                  copySignals={this.state.copySignals}
                  updateCopySignals={this.updateCopySignals}
                 
                  removeCopySignals={this.removeCopySignals}
                  addCopySignals={this.addCopySignals}
                  publishEvent={this.props.eventActions.publishEvent}
                  />
                  
                       </TabContainer> }
      { value === 3 && <TabContainer>
                <EventDetailTarget classes={classes} event={event_detail.event}

                  updateEvent={this.updateEvent}
                  group={event_detail.group}
                  editMode={this.state.editMode}
                  copyTargets={this.state.copyTargets}
                  updateCopyTargets={this.updateCopyTargets}
                   ven={event_detail.ven}
                onVenSuggestionsFetchRequested={this.onVenSuggestionsFetchRequested}
                onVenSuggestionsClearRequested={this.onVenSuggestionsClearRequested}
                onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}
                  />
                     
                       </TabContainer> }

      { value === 4 && <TabContainer>
                <EventDetailVenResponse classes={classes} event={event_detail.event} venResponse={event_detail.venResponse}
                refreshVenResponse={() => {this.props.eventActions.loadEventVenResponse(this.props.match.params.id)}}
               />
                     
                       </TabContainer> }
                


    </div>

    );
  }
}

EventDetailPage.propTypes = {
  eventActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    event_detail: state.event_detail
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    eventActions: bindActionCreators( eventActions, dispatch ),
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch ),
    venActions: bindActionCreators( venActions, dispatch ),

    
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( EventDetailPage ) );
