import React from 'react';

import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';

import GridList from '@material-ui/core/GridList';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'

import { MarketContextSelectDialog } from '../common/VtnconfigurationDialog'

import { history } from '../../store/configureStore';

import { DateAndTimePicker, DurationPicker } from '../common/TimePicker'

import { MarketContextChip } from '../common/FilterChip'

import EventHeader from './EventHeader'

export class EventList extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  handleEditEvent = (id) => () => {
    history.push( '/event/detail/'+ id);
  }

  handleDeleteEvent = (id) => () => {
    this.props.deleteEvent(id)
  }

  render() {
    const {classes, marketContext, event} = this.props;

    var view = [];


    for (var i in event) {
      var v = event[ i ];
      view.push(
        <VtnConfigurationEventCard key={ 'event_card_' + v.id }
                                 classes={ classes }
                                 event={ v } 

                                  handleEditEvent = {this.handleEditEvent(v.id)}
                                  handleDeleteEvent = {this.handleDeleteEvent(v.id)} />
      );
    }
    return (
      <div className={ classes.root }>
        <EventHeader classes={classes}  marketContext={marketContext} event={event}/>
        <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
        <Typography gutterBottom
                    variant="title"
                    component="h2">
          Existing Events
        </Typography>
        <GridList style={ { justifyContent: 'space-around', } }>
          { view }
        </GridList>
      </div>
    );
  }
}

export default EventList;
