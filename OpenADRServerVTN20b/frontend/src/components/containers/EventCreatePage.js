import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as eventActions from '../../actions/eventActions';
import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as venActions from '../../actions/venActions';



import { withStyles } from '@material-ui/core/styles';

import Divider from '@material-ui/core/Divider';

import EventCreate from '../EventCreate/EventCreate'


const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
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
  iconButton: {
    marginTop: 10
  },
});

export class EventCreatePage extends React.Component {


  state = {
  };

  componentDidMount() {
    this.props.vtnConfigurationActions.loadMarketContext();
    this.props.vtnConfigurationActions.loadGroup();
  }

  onVenSuggestionsFetchRequested = (e) => {
     var filters = [];
    filters.push({type:"VEN", value:e.value});
    this.props.venActions.searchVen([], [], 0, 5);
  }

  onVenSuggestionsClearRequested = () => {
  }

  onVenSuggestionsSelect = (ven) => {

  }

  render() {
    const {classes, event_create} = this.props;

    return (
     <div className={ classes.root }>
      <Divider variant="middle" />
       <EventCreate classes={classes} marketContext={event_create.marketContext} group={event_create.group} 
                          createEvent={this.props.eventActions.createEvent}
                          ven={event_create.ven}
                          onVenSuggestionsFetchRequested={this.onVenSuggestionsFetchRequested}
                          onVenSuggestionsClearRequested={this.onVenSuggestionsClearRequested}
                          onVenSuggestionsSelect={this.onVenSuggestionsSelect}
                          />

    </div>

    );
  }
}

EventCreatePage.propTypes = {
  eventActions: PropTypes.object.isRequired,
  vtnConfigurationActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    event_create: state.event_create
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    eventActions: bindActionCreators( eventActions, dispatch ),
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch ),
    venActions: bindActionCreators( venActions, dispatch )

    
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( EventCreatePage ) );
