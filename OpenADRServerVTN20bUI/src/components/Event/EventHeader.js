import React from 'react';

import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';

import Grid from '@material-ui/core/Grid';

import Select from '@material-ui/core/Select';

import ExtensionIcon from '@material-ui/icons/Extension';
import GroupWorkIcon from '@material-ui/icons/GroupWork';
import ExpandMore from '@material-ui/icons/ExpandMore';


import ChipInput from 'material-ui-chip-input'


import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';

import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'

import { MarketContextSelectDialog } from '../common/VtnconfigurationDialog'

import { history } from '../../store/configureStore';

import { DateAndTimePicker, DurationPicker } from '../common/TimePicker'

import { MarketContextChip } from '../common/FilterChip'

const deltaStartDays = 7
const deltaEndDays = 7

export class EventHeader extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    this.state.marketContextSelectDialogOpen = false;

    var now = new Date();
    var today = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);

    this.state.start = today.getTime() - deltaStartDays * 24 * 60 * 60 * 1000;
    this.state.end = today.getTime() + deltaEndDays * 24 * 60 * 60 * 1000;
    this.state.filter = [];


  }

  handleCreateEventClick = () => {
    history.push( '/event/create' );
  }

  handleMarketContextSelectOpen = () => {
    this.setState( {
      marketContextSelectDialogOpen: true
    } )
  }

  handleMarketContextSelectClose = (context) => {
    var params = {
      marketContextSelectDialogOpen: false
    }
    if ( context != null ) {
      this.handleAddChip( <MarketContextChip name={ context.name } /> );
    }
    this.setState( params )
  }

  handleAddChip = (chip) => {
    var filter = this.state.filter;
    filter.push( chip );
    this.setState( {
      filter
    } )
  }

  handleDeleteChip = (chip, index) => {
    var filter = this.state.filter;
    filter.splice( index, 1 );
    this.setState( {
      filter
    } )
  }



  onStartChange = (start) =>  {
    this.setState({start})
  }


  onEndChange = (end) =>  {
    this.setState({end})
  }


  render() {
    const {classes, marketContext, event} = this.props;

    return (
      <Grid container spacing={ 8 }>
        <Grid container
              item
              xs={ 12 }
              spacing={ 24 }>

          <Grid item xs={ 2 }>
            <DateAndTimePicker classes={ classes } field="Start" 
            value={this.state.start} onChange={this.onStartChange} />
          </Grid>
          <Grid item xs={ 2 }>
            <DateAndTimePicker classes={ classes } field="End" 
            value={this.state.end} onChange={this.onEndChange} />
          </Grid>
          <Grid item xs={ 4 }>
            <ChipInput label="Filters"
                      style={{marginTop:"-3px"}}
                       placeholder="Filters"
                       value={ this.state.filter }
                       onAdd={ this.handleAddChip }
                       onDelete={ this.handleDeleteChip }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 2 }>
            <div style={ { marginTop: 15 } }>
              <IconButton className={ classes.iconButton }
                          aria-label="market_context"
                          onClick={ this.handleMarketContextSelectOpen }>
                <ExtensionIcon />
                <ExpandMore />
              </IconButton>
              <MarketContextSelectDialog marketContext={ marketContext}
                                         open={ this.state.marketContextSelectDialogOpen }
                                         close={ this.handleMarketContextSelectClose }
                                         title="Filter by Market Context" />
       
              <IconButton className={ classes.iconButton } aria-label="Search">
                <SearchIcon />
              </IconButton>
            </div>
          </Grid>
          <Grid item xs={ 2 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    size="small"
                    className={ classes.button }
                    onClick={ this.handleCreateEventClick }>
              <AddIcon />Create a new Event
            </Button>
          </Grid>
        </Grid>
      </Grid>     
    );
  }
}

export default EventHeader;
