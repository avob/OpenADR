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

import EventDetailDescriptor from '../EventDetail/EventDetailDescriptor'
import EventDetailSignal from '../EventDetail/EventDetailSignal'
import EventDetailTarget from '../EventDetail/EventDetailTarget'

import green from '@material-ui/core/colors/green';
import red from '@material-ui/core/colors/red';



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
  success: {
    backgroundColor: green[600],
  },
  error: {
    backgroundColor: red[600],
  }
});

export class EventDetailPage extends React.Component {


  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
  };

  componentDidMount() {
    this.props.eventActions.loadEventDetail(this.props.match.params.id);

  }

  render() {
    const {classes, event_detail} = this.props;
    const {value} = this.state;
    console.log(event_detail);
    return (
     <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Descriptor" />
        <Tab label="Signals" />
        <Tab label="Targets" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <TabContainer>
                <EventDetailDescriptor classes={classes} event={event_detail.event}
                  activeEvent={this.props.eventActions.activeEvent}
                  cancelEvent={this.props.eventActions.cancelEvent}/>
                     
                       </TabContainer> }
      { value === 1 && <TabContainer>
                <EventDetailSignal classes={classes} event={event_detail.event}/>
                     
                       </TabContainer> }
      { value === 2 && <TabContainer>
                <EventDetailTarget classes={classes} event={event_detail.event}/>
                     
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
    eventActions: bindActionCreators( eventActions, dispatch )
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( EventDetailPage ) );
