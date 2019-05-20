import React from 'react';






import {  VtnConfigurationMarketContextCard } from '../common/VtnConfigurationCard'
import { MarketContextSelectDialog } from '../common/VtnconfigurationDialog'






import Grid from '@material-ui/core/Grid';




import Divider from '@material-ui/core/Divider';


import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';


import IconButton from '@material-ui/core/IconButton';



import ChipInput from 'material-ui-chip-input'

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';

import SearchIcon from '@material-ui/icons/Search';







import VenDetailHeader from './VenDetailHeader'














var MarketContextGridList = (props) => {
  return (
  <div className={ props.classes.root }>
    <GridList className={ props.classes.gridList }
              cols={ 3 }
              spacing={ 0 }
              cellHeight="auto">
      { props.marketContext.map( context => (
          <GridListTile key={ context.id } className={ props.classes.tile }>
            <VtnConfigurationMarketContextCard classes={ props.classes }
                                               context={ context }
                                               handleRemoveVenMarketContext={ props.handleRemoveVenMarketContext( context ) } />
          </GridListTile>
        ) ) }
    </GridList>
  </div>
  );
}

export class VenDetailEnrollment extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
     this.state.marketContextSelectDialogOpen = false;
  }

  handleMarketContextSelectOpen = () => {
    this.setState( {
      marketContextSelectDialogOpen: true
    } );
  }

  handleMarketContextSelectClose = (context) => {
    if ( context ) {
      this.props.addVenMarketContext( this.props.ven.username, context.id )
    }
    this.setState( {
      marketContextSelectDialogOpen: false
    } );
  }

  handleRemoveVenMarketContext = (context) => {
    return () => {
      console.log( context )
      this.props.removeVenMarketContext( this.props.ven.username, context.id )
    }
  }

  render() {
    const {classes, ven, marketContext, venMarketContext} = this.props;

    var notSubscribedMarketContext = [];
    var venMarketContextId = [];
    for (var i in venMarketContext) {
      venMarketContextId.push( venMarketContext[ i ].id );
    }
    for (var j in marketContext) {
      if ( venMarketContextId.indexOf( marketContext[ j ].id ) === -1 ) {
        notSubscribedMarketContext.push( marketContext[ j ] )
      }
    }


    return (
    <div className={ classes.root } >
      <VenDetailHeader classes={classes} ven={ven} actions={[
 
      ]
      }/>
      { /* MarketContext Row */ }
      <Divider style={ { marginTop: '20px' } } />
      <Grid container >
        <Grid container>
          <Grid item xs={ 8 }>
            <ChipInput label="Filters"
                       placeholder="Filters"
                       value={ this.state.filter }
                       onAdd={ this.handleAddChip }
                       onDelete={ this.handleDeleteChip }
                       fullWidth={ true } />
          </Grid>
          <Grid item xs={ 1 }>
            <IconButton className={ classes.iconButton } aria-label="Search">
              <SearchIcon />
            </IconButton>
          </Grid>
          <Grid item xs={ 3 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    size="small"
                    fullWidth={true}
                    className={ classes.button }
                    onClick={ this.handleMarketContextSelectOpen }>
              <AddIcon />Subscribe to a Market Context
            </Button>
            <MarketContextSelectDialog marketContext={ notSubscribedMarketContext }
                                       open={ this.state.marketContextSelectDialogOpen }
                                       close={ this.handleMarketContextSelectClose }
                                       title="Add VEN to Market Context" />
          </Grid>
        </Grid>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 12 }>
            <MarketContextGridList classes={ classes }
                                   marketContext={ venMarketContext }
                                   handleRemoveVenMarketContext={ this.handleRemoveVenMarketContext } />
          </Grid>
        </Grid>
      </Grid>

     
    </div>
    );
  }
}

export default VenDetailEnrollment;
