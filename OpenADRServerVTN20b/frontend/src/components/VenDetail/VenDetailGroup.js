import React from 'react';
import {  GroupSelectDialog } from '../common/VtnconfigurationDialog'
import IconButton from '@material-ui/core/IconButton';
import AddIcon from '@material-ui/icons/Add';
import EnhancedTable  from '../common/EnhancedTable'
import TableCell from '@material-ui/core/TableCell';
import Tooltip from '@material-ui/core/Tooltip';


export class VenDetailGroup extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {};
    this.state.groupSelectDialogOpen = false;
    this.state.pagination = {
      page: 0
      , size: 5
    } 
    this.state.sort = {
      sort: "asc"
      , by: "name"
    }
  }

  handleGroupSelectOpen = () => {
    this.setState( {
      groupSelectDialogOpen: true
    } );
  }

  handleGroupSelectClose = (group) => {
    console.log( group )
    if ( group ) {
      this.props.addVenGroup( this.props.ven.username, group.id )
    }
    this.setState( {
      groupSelectDialogOpen: false
    } );
  }



  handleRemoveVenGroup = (group) => {
    return () => {
      console.log( group )
      this.props.removeVenGroup( this.props.ven.username, group.id )
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
    const {classes, group, venGroup} = this.props;

    var notAddedGroup = [];
    var venGroupId = [];
    for (var i in venGroup) {
      venGroupId.push( venGroup[ i ].id );
    }
    for (var j in group) {
      if ( venGroupId.indexOf( group[ j ].id ) === -1 ) {
        notAddedGroup.push( group[ j ] )
      }
    }

    return (
    <div className={ classes.root } >

      <EnhancedTable 
        title="Group"
        data={venGroup}
        total={venGroup.length}
        pagination={this.state.pagination}
        sort={this.state.sort}
        handlePaginationChange={this.handlePaginationChange}
        handleSortChange={this.handleSortChange}
        rows={[
          { id: 'name', numeric: false, disablePadding: true, label: 'Group'},
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
            <Tooltip title="New" onClick={ () => {this.handleGroupSelectOpen() } }>
            <IconButton aria-label="New">
            <AddIcon />
            </IconButton>
            </Tooltip>
          </React.Fragment>
        }}
        />
        <GroupSelectDialog group={ notAddedGroup}
                                         open={ this.state.groupSelectDialogOpen }
                                         close={ this.handleGroupSelectClose }
                                         title="Select Group" />
    </div>
    );
  }
}

export default VenDetailGroup;
