import React from 'react';

import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';

import GridList from '@material-ui/core/GridList';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'

import { MarketContextSelectDialog } from '../common/VtnconfigurationDialog'

import { history } from '../../store/configureStore';

import EventHeader from './EventHeader'

export class EventList extends React.Component {
  constructor( props ) {
    super( props );
  }

  handleEditEvent = (id) => () => {
    history.push( '/event/detail/'+ id);
  }

  handleDeleteEvent = (id) => () => {
    this.props.deleteEvent(id)
  }

  render() {
    const {classes, marketContext, event, filters, pagination, onFilterChange, onPaginationChange,
      start, end, onStartChange, onEndChange} = this.props;

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
        <EventHeader classes={classes}  marketContext={marketContext} event={event}
        filters={filters} pagination={pagination} onFilterChange={onFilterChange} onPaginationChange={onPaginationChange}
        start={start} end={end} onStartChange={onStartChange} onEndChange={onEndChange} />
        <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
        <GridList style={ { justifyContent: 'space-around', } }>
          { view }
        </GridList>
      </div>
    );
  }
}

export default EventList;
