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
import Paper from '@material-ui/core/Paper';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';

import EventDetailDescriptor from '../EventDetail/EventDetailDescriptor'
import EventDetailActivePeriod from '../EventDetail/EventDetailActivePeriod'
import EventDetailSignal from '../EventDetail/EventDetailSignal'
import EventDetailTarget from '../EventDetail/EventDetailTarget'
import EventDetailVenResponse from '../EventDetail/EventDetailVenResponse'

import green from '@material-ui/core/colors/green';
import red from '@material-ui/core/colors/red';

import { history } from '../../store/configureStore';
import { EventActionDialog } from '../common/VtnconfigurationDialog'


const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: 0, 
    marginRight: 0,
  },
  dense: {
    marginTop: 19,
  },
  menu: {
    width: 200,
  },
  formControl: {
    margin: theme.spacing(1),
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
    margin: theme.spacing(1),
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
      dialogOpen: false
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


  onVenSuggestionsFetchRequested = (e) => {
    var filters = [];
    filters.push({type:"VEN", value:e.value});
    this.props.venActions.searchVen(filters, [], 0, 5);
  }

  onVenSuggestionsClearRequested = () => {
  }

  onVenSuggestionsSelect = (ven) => {
     var filters = this.state.filters;
    filters.push({type:"VEN", value:ven.username});
    this.setState({filters});
    this.refreshEvent();
  }

  handleVenClick (event) {
      history.push( '/ven', { filters: [{type:"EVENT", value: event.id}] });
    }

  handleActionClick (event) {
      this.setState({dialogOpen: true})
    }

  handleDialogClose = () => {
    this.setState({dialogOpen: false})
  }


  render() {
    const {classes, event_detail} = this.props;
    const {value} = this.state;
    if(!event_detail.event.activePeriod ) return null;
    return (
     <Paper className={ classes.root }>
     <Toolbar>
            <div style={{flex: '0 0 auto'}}>
              <Typography variant="h6" id="tableTitle">
                  {event_detail.event.descriptor.marketContext + " - " + event_detail.event.id}
                </Typography>
            </div>
            <div style={{flex: '1 1 100%'}} />
            <div style={{ flex: '0 0 auto'}}>


                
                   <Button size="small" color="primary" onClick={() => {this.handleVenClick(event_detail.event) }}>VEN</Button>
                   <Button size="small" color="primary" onClick={() => {this.handleActionClick(event_detail.event) }}>Action</Button>
            </div>
                


        </Toolbar>
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

      <Divider style={{margin: "20px 0"}}/>
      { value === 0 && <EventDetailDescriptor classes={classes} event={event_detail.event} 
                  /> }     
      { value === 1 && <EventDetailActivePeriod classes={classes} event={event_detail.event}/> }
      { value === 2 && <EventDetailSignal classes={classes} event={event_detail.event} 
          group={event_detail.group}
                   ven={event_detail.ven}
                onVenSuggestionsFetchRequested={this.onVenSuggestionsFetchRequested}
                onVenSuggestionsClearRequested={this.onVenSuggestionsClearRequested}
                onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}
                /> }
      { value === 3 && <EventDetailTarget classes={classes} event={event_detail.event}

                 
                  group={event_detail.group}
                   ven={event_detail.ven}
                onVenSuggestionsFetchRequested={this.onVenSuggestionsFetchRequested}
                onVenSuggestionsClearRequested={this.onVenSuggestionsClearRequested}
                onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}
                  /> }

      { value === 4 &&  <EventDetailVenResponse classes={classes} event={event_detail.event} venResponse={event_detail.venResponse}
                refreshVenResponse={() => {this.props.eventActions.loadEventVenResponse(this.props.match.params.id)}}
               />  }
                
               <EventActionDialog open={ this.state.dialogOpen }
             close={ this.handleDialogClose }
             title="Ven actions" 
             event={event_detail.event}
             eventActions={this.props.eventActions}/>

    </Paper>

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
