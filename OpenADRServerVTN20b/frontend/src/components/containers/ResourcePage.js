import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as venActions from '../../actions/venActions';
import * as eventActions from '../../actions/eventActions';
import * as resourceActions from '../../actions/resourceActions';



import Paper from '@material-ui/core/Paper';

import VenResource from '../Ven/VenResource'
import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
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
    minWidth: 500,
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
  iconButton: {
    marginTop: 10
  },
});

export class ResourcePage extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
    this.state.filters = [];
    if(this.props.location.state && this.props.location.state.filters && this.props.location.state.filters.length > 0) {
      this.state.filters = this.state.filters.concat(props.location.state.filters)
    }
    this.state.sorts = [];
    if(this.props.location.state && this.props.location.state.sorts && this.props.location.state.sorts.length > 0) {
      this.state.sorts = this.state.sorts.concat(props.location.state.sorts)
    }
    this.state.pagination = {
      page: 0
      , size: 10
    }

    this.props.resourceActions.searchResource(this.state.filters);
  }

  onFilterChange = (filters) => {
    this.setState({filters});
    this.resourceActions.searchResource(filters);
  }

  onEventSuggestionsFetchRequested = (e) => {
    var filters = this.state.filters.splice(0);
    // this.props.eventActions.searchEvent(filters, null, null, 0, 5);
  }

  onEventSuggestionsClearRequested = () => {
  }

  onEventSuggestionsSelect = (event) => {
     var filters = this.state.filters;
    filters.push({type:"EVENT", value:event.descriptor.eventId});
    this.setState({filters});
    this.props.eventActions.searchEvent(filters, this.state.sorts, 0, 5);
  }

  render() {
    const {classes, resource} = this.props;
    return (
    <Paper className={ classes.root }>
        <VenResource classes={ classes }
                 resource={ resource.resource}
                 filters={this.state.filters}
                 onFilterChange={this.onFilterChange}
                
                  event={resource.event}
                  onEventSuggestionsFetchRequested={this.onEventSuggestionsFetchRequested}
                  onEventSuggestionsClearRequested={this.onEventSuggestionsClearRequested}
                  onEventSuggestionsSelect={this.onEventSuggestionsSelect}
                 />
    </Paper>

    );
  }
}

ResourcePage.propTypes = {
  eventActions: PropTypes.object.isRequired,
  resourceActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    resource: state.resource
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    eventActions: bindActionCreators( eventActions, dispatch ),
    resourceActions: bindActionCreators( resourceActions, dispatch )  
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( ResourcePage ) );
