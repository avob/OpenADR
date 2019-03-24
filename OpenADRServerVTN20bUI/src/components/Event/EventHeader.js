import React from 'react';

import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';

import Grid from '@material-ui/core/Grid';



import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';

import GridList from '@material-ui/core/GridList';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'

import { history } from '../../store/configureStore';

import { DatePicker } from '../common/TimePicker'


import FilterPanel from '../common/FilterPanel' 


export class EventHeader extends React.Component {
  constructor( props ) {
    super( props );


  }

  handleCreateEventClick = () => {
    history.push( '/event/create' );
  }

  render() {
    const {classes, marketContext, event} = this.props;
    return (
      <div>
        <Grid container spacing={ 8 }>
          <Grid container>

            <Grid item xs={ 3 }>
              <DatePicker classes={ classes } field="Start" 
              value={this.props.start} onChange={this.props.onStartChange} />
              <DatePicker classes={ classes } field="End" 
              value={this.props.end} onChange={this.props.onEndChange} />
            </Grid>
            <Grid item xs={ 8 }>
              <FilterPanel classes={classes} hasFilter={{marketContext:true}} 
                marketContext={marketContext}
                filter={this.props.filters}
                onFilterChange={this.props.onFilterChange}/>
            </Grid>
            <Grid item xs={ 1 }>
             <Button key="btn_create"
                      variant="outlined"
                      color="primary"
                      size="small"
                      fullWidth={ true } 
                      className={ classes.button }
                      onClick={ this.handleCreateEventClick }>
                <AddIcon />New
              </Button>
            </Grid>
           
          </Grid>
        </Grid>  
        
      </div>   
    );
  }
}

export default EventHeader;

