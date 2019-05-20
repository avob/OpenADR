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

import EventList from '../Event/EventList'
import EventCalendar from '../Event/EventCalendar'

import { history } from '../../store/configureStore';

import moment from 'moment'




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


export class EventPage extends React.Component {

  constructor(props) {
    super(props);
    this.state= {};
    this.state.value = 0;
    this.state.filters =  [];
    if(props.location.state && props.location.state.filters.length > 0) {
      this.state.filters = this.state.filters.concat(props.location.state.filters)
    }

    this.state.pagination = {
      page: 0
      , size: 100
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
    this.refreshEvent();
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
  }

  refreshEvent = () => {
    this.props.eventActions.searchEvent(this.state.filters, this.state.start, this.state.end
      , this.state.pagination.page, this.state.pagination.size);
  }

  onFilterChange = (filters) => {
    this.setState({filters});
    this.refreshEvent();
  }

  onPaginationChange = (pagination) => {
    this.setState({pagination});
    this.refreshEvent();
  }

  onVenSuggestionsFetchRequested = (e) => {
    var filters = this.state.filters.splice(0);
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

  onColorChange = (color) => {
    this.setState({color});
  }

  onViewChange = (view) => {
    this.setState({view});
    var range = this.getRequestDateRange(this.state.currentDate, view)
    console.log(view)
    this.props.eventActions.searchEvent(this.state.filters, range.start, range.end
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
    this.setState({currentDate});

    var range = this.getRequestDateRange(currentDate, this.state.view)
    this.props.eventActions.searchEvent(this.state.filters, range.start, range.end
      , this.state.pagination.page, this.state.pagination.size);
  }

  render() {
    const {classes, event} = this.props;
    const {value} = this.state;
    return (
     <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Events" />
        <Tab label="Calendar" />
      </Tabs>
      <Divider variant="middle" />

      { value === 0 && <TabContainer>
                          <EventList classes={classes} 
                          marketContext={ event.marketContext }
                          event={event.event}
                          
                          deleteEvent={ this.props.eventActions.deleteEvent}
                          filters={this.state.filters}
                          pagination={this.state.pagination}
                          onFilterChange={this.onFilterChange}
                          onPaginationChange={this.onPaginationChange}

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
                           />
                       </TabContainer> }
                       
      { value === 1 && <TabContainer>
                          <EventCalendar classes={classes} 
                          marketContext={ event.marketContext }
                          event={event.event}
                          deleteEvent={ this.props.eventActions.deleteEvent}

                          filters={this.state.filters}
                          pagination={this.state.pagination}
                          onFilterChange={this.onFilterChange}
                          onPaginationChange={this.onPaginationChange}

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
                 />
                       </TabContainer> }

    </div>

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
