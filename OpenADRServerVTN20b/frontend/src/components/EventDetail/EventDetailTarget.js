import React from 'react';


import {EventTargetPanel} from '../common/EventTargetPanel'

export class EventDetailTarget extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
   const {classes, event, group, ven} = this.props;
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
