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

const deltaStartDays = 7
const deltaEndDays = 7

export class EventHeader extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}

    var now = new Date();
    var today = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);

    this.state.start = today.getTime() - deltaStartDays * 24 * 60 * 60 * 1000;
    this.state.end = today.getTime() + deltaEndDays * 24 * 60 * 60 * 1000;
    this.state.filter = [];


  }

  handleCreateEventClick = () => {
    history.push( '/event/create' );
  }


  onStartChange = (start) =>  {
    this.setState({start})
  }


  onEndChange = (end) =>  {
    this.setState({end})
  }

  onFilterChange = (filter) => {this.setState({filter})}


  render() {
    const {classes, marketContext, event} = this.props;

    return (
      <div>
        <Grid container spacing={ 8 }>
          <Grid container
                xs={ 12 }>

            <Grid item xs={ 3 }>
              <DatePicker classes={ classes } field="Start" 
              value={this.state.start} onChange={this.onStartChange} />
              <DatePicker classes={ classes } field="End" 
              value={this.state.end} onChange={this.onEndChange} />
            </Grid>
            <Grid item xs={ 8 }>
              <FilterPanel classes={classes} hasFilter={{marketContext:true}} 
                marketContext={marketContext}
                filter={this.state.filter}
                onFilterChange={this.onFilterChange}/>
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
