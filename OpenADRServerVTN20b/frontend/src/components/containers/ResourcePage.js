import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as eventActions from '../../actions/eventActions';
import * as resourceActions from '../../actions/resourceActions';



import Paper from '@material-ui/core/Paper';

import VenResource from '../Ven/VenResource'
import { withStyles } from '@material-ui/core/styles';
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
    margin: theme.spacing(1),
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

    this.props.vtnConfigurationActions.loadMarketContext();
    this.props.vtnConfigurationActions.loadGroup();
    this.props.resourceActions.searchResourceRoot(this.state.filters);
  }

  onFilterChange = (filters) => {
    this.setState({filters});
    this.props.resourceActions.searchResourceRoot(filters);
  }

  onEventSuggestionsFetchRequested = (e) => {

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
    <Paper className={ classes.root } style={{paddingBottom:"10px"}}>
        <VenResource classes={ classes }
                 resource={ resource.resource}
                 data={resource.data}
                 dataId={resource.dataId}
                  marketContext={ resource.marketContext }
                 group={ resource.group }
                 filters={this.state.filters}
                   onFilterChange={this.onFilterChange}
                  resourceActions={this.props.resourceActions}
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
  resourceActions: PropTypes.object.isRequired,
  vtnConfigurationActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    resource: state.resource,
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    eventActions: bindActionCreators( eventActions, dispatch ),
    resourceActions: bindActionCreators( resourceActions, dispatch ),
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch )   
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( ResourcePage ) );
