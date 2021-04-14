import React from 'react';


import EventDetailHeader from './EventDetailHeader'
import Divider from '@material-ui/core/Divider';

import {EventTargetPanel} from '../common/EventTargetPanel'

import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

export class EventDetailTarget extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
   const {classes, event, group, editMode, ven} = this.props;
    return (
    <div className={ classes.root } >

       <EventTargetPanel classes={classes} eventTarget={event.targets} group={group} onChange={this.props.updateCopyTargets}
        ven={ven}
        onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
        onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
        onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}/>

    </div>
    );
  }
}

export default EventDetailTarget;
