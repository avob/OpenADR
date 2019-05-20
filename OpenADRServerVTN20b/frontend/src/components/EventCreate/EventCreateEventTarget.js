import React from 'react';









import Grid from '@material-ui/core/Grid';


























import {EventTargetPanel} from '../common/EventTargetPanel'



export class EventCreateEventTarget extends React.Component {

  render() {
    const {classes, eventTarget, group, ven} = this.props;

    return (
    <Grid container
          spacing={ 8 }
          justify="center">
      
      <Grid container spacing={ 24 }>
        <Grid item xs={ 2 } />
          <Grid item xs={ 8 }>
            <EventTargetPanel classes={classes} eventTarget={eventTarget} group={group} onChange={this.props.onChange}
            ven={ven}
            onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
            onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
            onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}/>
          </Grid>

          
        <Grid item xs={ 2 } />
      </Grid>
     
      
    </Grid>
    );
  }
}

export default EventCreateEventTarget;
