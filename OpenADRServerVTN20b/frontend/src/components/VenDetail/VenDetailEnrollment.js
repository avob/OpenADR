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



import EnhancedTable  from '../common/EnhancedTable'
import TableCell from '@material-ui/core/TableCell';


import Tooltip from '@material-ui/core/Tooltip';
import DeleteIcon from '@material-ui/icons/Delete';
import FilterListIcon from '@material-ui/icons/FilterList';



import VenDetailHeader from './VenDetailHeader'



export class VenDetailEnrollment extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
     this.state.marketContextSelectDialogOpen = false;
     this.state.pagination = {
      page: 0
      , size: 5
    } 
    this.state.sort = {
      sort: "asc"
      , by: "name"
    }
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

  handlePaginationChange = (pagination) => {
   this.setState( {
      pagination
    } );
  }

  handleSortChange = (sort) => {
   this.setState( {
      sort
    } );
  }


  render() {
    const {classes, ven, venActions, marketContext, venMarketContext} = this.props;

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

       <EnhancedTable 
        title="MarketContext"
        data={venMarketContext}
        total={venMarketContext.length}
        pagination={this.state.pagination}
        sort={this.state.sort}
        handlePaginationChange={this.handlePaginationChange}
        handleSortChange={this.handleSortChange}
        rows={[
          { id: 'name', numeric: false, disablePadding: true, label: 'MarketContext'},
          { id: 'description', numeric: false, disablePadding: false, label: 'Description' },
          { id: 'color', numeric: false, disablePadding: false, label: 'Color' },
        ]} 
        rowTemplate={n => {
          return <React.Fragment>
            <TableCell component="th" scope="row" padding="none">
              {n.name}
            </TableCell>
            <TableCell>{n.description}</TableCell>
            <TableCell align="right">{n.color}</TableCell>
          </React.Fragment>
        }}
        actionSelected={() => {
       
        }}
        action={() => {
          return <React.Fragment>
            

                     <Tooltip title="New" onClick={ () => {this.handleMarketContextSelectOpen() } }>
            <IconButton aria-label="New">
              <AddIcon />
            </IconButton>
          </Tooltip>
          
          </React.Fragment>
        }}
        />
        <MarketContextSelectDialog marketContext={ notSubscribedMarketContext}
                                         open={ this.state.marketContextSelectDialogOpen }
                                         close={ this.handleMarketContextSelectClose }
                                         title="Select Market Context" />
    </div>
    );
  }
}

export default VenDetailEnrollment;
