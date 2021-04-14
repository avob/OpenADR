import React from 'react';





import Grid from '@material-ui/core/Grid';



import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';





import { history } from '../../store/configureStore';



import FilterPanel from '../common/FilterPanel' 


export class EventHeader extends React.Component {

  handleCreateEventClick = () => {
    history.push( '/event/create' );
  }

  render() {
    const {classes, marketContext, ven} = this.props;
    return (
          <Grid container>            
            <Grid item xs={ 12 }>
              <FilterPanel classes={classes} type="EVENT" hasFilter={{marketContext:true, ven: true, eventStatus:true}} 
                marketContext={marketContext}
                filter={this.props.filters}
                onFilterChange={this.props.onFilterChange}
                ven={ven}
                onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
                onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
                onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}
                />
            </Grid>
          </Grid>        
    );
  }
}

export default EventHeader;

