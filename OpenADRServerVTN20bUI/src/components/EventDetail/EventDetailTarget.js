import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles} from '@material-ui/core/styles';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'



import Typography from '@material-ui/core/Typography';

import EventDetailHeader from './EventDetailHeader'
import Divider from '@material-ui/core/Divider';


export class EventDetailTarget extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
   const {classes, event} = this.props;

    return (
    <div className={ classes.root } >
      <EventDetailHeader classes={classes} event={event}/>
       <Divider style={ { marginTop: '20px' } } />

    </div>
    );
  }
}

export default EventDetailTarget;
