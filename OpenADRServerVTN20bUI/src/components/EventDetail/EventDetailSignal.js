import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles} from '@material-ui/core/styles';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'



import Typography from '@material-ui/core/Typography';

import EventDetailHeader from './EventDetailHeader'

export class EventDetailSignal extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
    const {classes, event} = this.props;

    return (
    <div className={ classes.root } >
      <EventDetailHeader classes={classes} event={event}/>
   

    </div>
    );
  }
}

export default EventDetailSignal;
