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

import { VtnConfigurationVenCard } from '../common/VtnConfigurationCard'

import { MarketContextSelectDialog } from '../common/VtnconfigurationDialog'

import { history } from '../../store/configureStore';


export class EventList extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
    const {classes, marketContext} = this.props;

    return (
      <div className={ classes.root }>
      <Grid container spacing={ 8 }>
        <Grid container
              item
              xs={ 12 }
              spacing={ 24 }>
          <Grid item xs={ 6 }>
            <ChipInput label="Filters"
                       placeholder="Filters"
                       value={ this.state.filter }
                       onAdd={ this.handleAddChip }
                       onDelete={ this.handleDeleteChip }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 3 }>
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
          <Grid item xs={ 3 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    size="small"
                    className={ classes.button }
                    onClick={ this.handleCreateVENClick }>
              <AddIcon />Create a new VEN
            </Button>
          </Grid>
        </Grid>
      </Grid>
      <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Existing VENs
      </Typography>
     
      </div>
    );
  }
}

export default EventList;
