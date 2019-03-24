import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as eventActions from '../../actions/eventActions';

import { withStyles } from '@material-ui/core/styles';

import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

import EventList from '../Event/EventList'
import EventCalendar from '../Event/EventCalendar'

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

const deltaStartDays = 7
const deltaEndDays = 7


export class EventPage extends React.Component {

  constructor(props) {
    super(props);
    this.state= {};
    this.state.value = 0;
    this.state.filters = [];
    this.state.pagination = {
      page: 0
      , size: 20
    }
    var now = new Date();
    var today = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);

    this.state.start = today.getTime() - deltaStartDays * 24 * 60 * 60 * 1000;
    this.state.end = today.getTime() + deltaEndDays * 24 * 60 * 60 * 1000;
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

  onStartChange = (start) =>  {
    this.setState({start})
    this.props.eventActions.searchEvent(this.state.filters, start, this.state.end
      , this.state.pagination.page, this.state.pagination.size);
  }


  onEndChange = (end) =>  {
    this.setState({end})
    this.props.eventActions.searchEvent(this.state.filters, this.state.start, end
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
                          start={this.state.start}
                          end={this.state.end}
                          onStartChange={this.onStartChange}
                          onEndChange={this.onEndChange}
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
                          start={this.state.start}
                          end={this.state.end}
                          onStartChange={this.onStartChange}
                          onEndChange={this.onEndChange}
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
    eventActions: bindActionCreators( eventActions, dispatch )


  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( EventPage ) );
