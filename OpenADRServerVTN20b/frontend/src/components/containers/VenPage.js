import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as venActions from '../../actions/venActions';
import * as eventActions from '../../actions/eventActions';

import Paper from '@material-ui/core/Paper';

import VenList from '../Ven/VenList'
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

export class VenPage extends React.Component {

  constructor(props) {
    super(props);
    console.log(props)
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
    this.state.sort = {
      sort: "asc"
      , by: "commonName"
    }

    this.props.vtnConfigurationActions.loadMarketContext();
    this.props.vtnConfigurationActions.loadGroup();
    this.props.venActions.searchVen(this.state.filters, this.state.sorts, this.state.pagination.page, this.state.pagination.size);
  }

  onFilterChange = (filters) => {
    this.setState({filters});
    this.props.venActions.searchVen(filters, this.state.sorts, this.state.pagination.page, this.state.pagination.size);
  }

  onSortChange = (sort) => {
    const sorts = [{property: sort.by, type: sort.sort.toUpperCase()}];
    this.setState({sorts: sorts, sort: sort});
     this.props.venActions.searchVen(this.state.filters, sorts, this.state.pagination.page, this.state.pagination.size);
  }

  onPaginationChange = (pagination) => {
    this.setState({pagination});
    this.props.venActions.searchVen(this.state.filters, this.state.sorts, pagination.page, pagination.size);
  }

  onEventSuggestionsFetchRequested = (e) => {

  }

  onEventSuggestionsClearRequested = () => {
  }

  onEventSuggestionsSelect = (event) => {
     var filters = this.state.filters;
    filters.push({type:"EVENT", value:event.descriptor.eventId});
    this.setState({filters});
    this.props.eventActions.searchEvent(filters, this.state.sorts, this.state.pagination.page, this.state.pagination.size);
  }

  render() {
    const {classes, ven} = this.props;
    return (
    <Paper className={ classes.root }>
        <VenList classes={ classes }
                 ven={ ven.ven}
                 total={ven.total}
                 marketContext={ ven.marketContext }
                 group={ ven.group }
                 deleteVen={ this.props.venActions.deleteVen } 
                 filters={this.state.filters}
                 sort={this.state.sort}
                 pagination={this.state.pagination}
                 onFilterChange={this.onFilterChange}
                 onSortChange={this.onSortChange}
                 onPaginationChange={this.onPaginationChange}

                 event={ven.event}
                  onEventSuggestionsFetchRequested={this.onEventSuggestionsFetchRequested}
                  onEventSuggestionsClearRequested={this.onEventSuggestionsClearRequested}
                  onEventSuggestionsSelect={this.onEventSuggestionsSelect}
                 />
    </Paper>

    );
  }
}

VenPage.propTypes = {
  venActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    ven: state.ven
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    venActions: bindActionCreators( venActions, dispatch ),
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch ),
    eventActions: bindActionCreators( eventActions, dispatch )  
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( VenPage ) );
