import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as eventActions from '../../actions/eventActions';
import * as venActions from '../../actions/venActions';

import { withStyles } from '@material-ui/core/styles';

import EventList from '../Event/EventList'
import EventCalendar from '../Event/EventCalendar'

import { history } from '../../store/configureStore';
import Paper from '@material-ui/core/Paper';

import moment from 'moment'

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

export class EventPage extends React.Component {

  constructor(props) {
    super(props);
    this.state= {};
    this.state.value = 0;
    this.state.filters =  [];
    if(props.location.state && props.location.state.filters && props.location.state.filters.length > 0) {
      this.state.filters = this.state.filters.concat(props.location.state.filters)
    }
    this.state.sorts = [];
    if(props.location.state && props.location.state.sorts && props.location.state.sorts.length > 0) {
      this.state.sorts = this.state.sorts.concat(props.location.state.sorts)
    }

    this.state.pagination = {
      page: 0
      , size: 5
    } 
    this.state.sort = {
      sort: "asc"
      , by: "id"
    }

    this.state.color = "status";
    this.state.view = "week";
    this.state.currentDate = new Date();


    
  }

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/event/list")
        break;
      case 1:
        history.push("/event/calendar")
        break;
      default:
        break;
    }
  };

  componentDidMount() {
    this.props.vtnConfigurationActions.loadMarketContext();
    this.props.eventActions.searchEvent(this.state.filters, this.state.sorts, this.state.start, this.state.end
      , this.state.pagination.page, this.state.pagination.size);
    if(this.props.match.params.panel != null) {
      switch(this.props.match.params.panel){
        case "list":
          this.setState({value:0});
          break;
        case "calendar":
          this.setState({value:1});
          break;
        default:
          break;
      }
    } else if(history.location.pathname === "/event/calendar") {
      this.setState({value:1});
    }
    
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if(history.location.pathname === "/event" && this.state.value !== 0) {
      this.setState({value:0});
    } else if(history.location.pathname === "/event/calendar" && this.state.value !== 1) {
      this.setState({value:1});
    }
  }

  onFilterChange = (filters) => {
    this.setState({filters});
    this.props.eventActions.searchEvent(filters, this.state.sorts, this.state.start, this.state.end
      , this.state.pagination.page, this.state.pagination.size);
  }

  onSortChange = (sort) => {
    this.setState({sort});
    const sorts = [{property: sort.by, type: sort.sort.toUpperCase()}];
    this.setState({sorts: sorts});
    this.props.eventActions.searchEvent(this.state.filters, sorts, this.state.start, this.state.end
      , this.state.pagination.page, this.state.pagination.size);
  }

  onPaginationChange = (pagination) => {
    this.setState({pagination});
    this.props.eventActions.searchEvent(this.state.filters, this.state.sorts, this.state.start, this.state.end
      , pagination.page, pagination.size);
  }

  onStartChange = (start) => {
    this.setState({start});
    this.props.eventActions.searchEvent(this.state.filters, this.state.sorts, start, this.state.end
      , this.state.pagination.page, this.state.pagination.size);
  }

  onEndChange = (end) => {
    this.setState({end});
    this.props.eventActions.searchEvent(this.state.filters, this.state.sorts, this.state.start, end
      , this.state.pagination.page, this.state.pagination.size);
  }

  onVenSuggestionsFetchRequested = (e) => {
    var filters = this.state.filters.splice(0);
    filters.push({type:"VEN", value:e.value});
    this.props.venActions.searchVen(filters, [], 0, 5);
  }

  onVenSuggestionsClearRequested = () => {
  }

  onVenSuggestionsSelect = (ven) => {
     var filters = this.state.filters;
    filters.push({type:"VEN", value:ven.username});
    this.setState({filters});
    this.props.eventActions.searchEvent(filters, this.state.sorts, this.state.start, this.state.end
      , this.state.pagination.page, this.state.pagination.size);
  }

  onColorChange = (color) => {
    this.setState({color});
  }

  onViewChange = (view) => {
    this.setState({view});
    var range = this.getRequestDateRange(this.state.currentDate, view)
    this.setState({start: range.start, end: range.end})
    this.props.eventActions.searchEvent(this.state.filters, this.state.sorts, range.start, range.end
      , this.state.pagination.page, this.state.pagination.size);
  }

  getRequestDateRange = (date, view) => {
    var d;
    switch(view) {
      case "month":
      case "week":
      case "day":
         d = moment(date);
        return {
          start: d.startOf(view).toDate().getTime()
          , end: d.endOf(view).toDate().getTime()
        };
      case "agenda":
         d = moment(date).startOf("day");
        return {
          start: d.toDate().getTime()
          , end: d.add(2, "week").toDate().getTime()
        };
      default:
        break;
    }
  }

  onCurrentDateChange = (currentDate) => {
    

    var range = this.getRequestDateRange(currentDate, this.state.view)
    this.setState({currentDate: currentDate, start: range.start, end: range.end});
    this.props.eventActions.searchEvent(this.state.filters, range.start, range.end
      , this.state.pagination.page, this.state.pagination.size);
  }

  render() {
    const {classes, event} = this.props;
    const {value} = this.state;

    return (
     <Paper className={ classes.root }>
      { value === 0 && 
                          <EventList classes={classes} 
                          marketContext={ event.marketContext }
                          event={event.event}
                          total={event.total}
                          
                          deleteEvent={ this.props.eventActions.deleteEvent}
                          filters={this.state.filters}
                          pagination={this.state.pagination}
                          sort={this.state.sort}
                          onFilterChange={this.onFilterChange}
                          onSortChange={this.onSortChange}
                          onPaginationChange={this.onPaginationChange}
                          start={this.state.start}
                          end={this.state.end}
                          onStartChange={this.onStartChange}
                          onEndChange={this.onEndChange}

                          ven={event.ven}
                          onVenSuggestionsFetchRequested={this.onVenSuggestionsFetchRequested}
                          onVenSuggestionsClearRequested={this.onVenSuggestionsClearRequested}
                          onVenSuggestionsSelect={this.onVenSuggestionsSelect}

                          color={this.state.color}
                          view={this.state.view}
                          currentDate={this.state.currentDate}
                          onColorChange={this.onColorChange}
                          onViewChange={this.onViewChange}
                          onCurrentDateChange={this.onCurrentDateChange}
                           />}
                       
      { value === 1 && 
                          <EventCalendar classes={classes} 
                          marketContext={ event.marketContext }
                          event={event.event}
                          deleteEvent={ this.props.eventActions.deleteEvent}

                          filters={this.state.filters}
                          pagination={this.state.pagination}
                          onFilterChange={this.onFilterChange}
                          onPaginationChange={this.onPaginationChange}
                          start={this.state.start}
                          end={this.state.end}
                          onStartChange={this.onStartChange}
                          onEndChange={this.onEndChange}
                          ven={event.ven}
                          onVenSuggestionsFetchRequested={this.onVenSuggestionsFetchRequested}
                          onVenSuggestionsClearRequested={this.onVenSuggestionsClearRequested}
                          onVenSuggestionsSelect={this.onVenSuggestionsSelect}

                          color={this.state.color}
                          view={this.state.view}
                          currentDate={this.state.currentDate}
                          onColorChange={this.onColorChange}
                          onViewChange={this.onViewChange}
                          onCurrentDateChange={this.onCurrentDateChange}
                 />}

    </Paper>

    );
  }
}

EventPage.propTypes = {
  vtnConfigurationActions: PropTypes.object.isRequired,
  eventActions: PropTypes.object.isRequired,
};

function mapStateToProps( state ) {
  return {
    event: state.event
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch ),
    eventActions: bindActionCreators( eventActions, dispatch ),
    venActions: bindActionCreators( venActions, dispatch )

    


  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( EventPage ) );
