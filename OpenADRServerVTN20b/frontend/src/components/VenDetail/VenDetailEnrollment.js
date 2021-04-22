import React from 'react';






import { MarketContextSelectDialog } from '../common/VtnconfigurationDialog'




import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';




import AddIcon from '@material-ui/icons/Add';



import EnhancedTable  from '../common/EnhancedTable'
import TableCell from '@material-ui/core/TableCell';


import Tooltip from '@material-ui/core/Tooltip';



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
      this.props.removeVenMarketContext( this.props.ven.username, context )
    
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
    const {classes, marketContext, venMarketContext} = this.props;

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
        handleDeleteSelectedClick={(selected) => {
          for(var i in selected) {
                 this.handleRemoveVenMarketContext(selected[i]);
               }
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
