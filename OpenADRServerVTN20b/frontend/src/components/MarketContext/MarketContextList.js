import React from 'react';
import TableCell from '@material-ui/core/TableCell';

import Paper from '@material-ui/core/Paper';
import AddIcon from '@material-ui/icons/Add';


import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import DeleteIcon from '@material-ui/icons/Delete';

import EnhancedTable  from '../common/EnhancedTable'

import { history } from '../../store/configureStore';
import Avatar from '@material-ui/core/Avatar';


export class MarketContextList extends React.Component {
  constructor( props ) {
    super( props );

    this.state = {}

    this.state.pagination = {
      page: 0
      , size: 5
    } 
    this.state.sort = {
      sort: "asc"
      , by: "name"
    }

  }


  handleCreateMarketContextButtonClick = (e) => {
    history.push( '/marketcontext/create' )
  }

  handleDeleteMarketContext = (id) => {
    var that = this;
    return function ( event ) {
      event.preventDefault();
      that.props.deleteMarketContext( id );
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
    const {classes, marketContext} = this.props;



    return (
    <Paper className={ classes.root }>
      


      <EnhancedTable 
        title="MarketContext"
        data={marketContext}
        total={marketContext.length}
        pagination={this.state.pagination}
        sort={this.state.sort}
        handleClick={n => {history.push( '/marketcontext/detail/' + n.name  +'/settings')}}
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
            <TableCell align="right"><Avatar style={{backgroundColor: n.color, width: "15px", height: "15px"}}/></TableCell>
          </React.Fragment>
        }}
        actionSelected={() => {
          return <React.Fragment>
            <Tooltip title="Delete">
            <IconButton aria-label="Delete">
              <DeleteIcon />
            </IconButton>
          </Tooltip>
          </React.Fragment>
        }}
        action={() => {
          return <React.Fragment>
            

                     <Tooltip title="New" onClick={ this.handleCreateMarketContextButtonClick }>
            <IconButton aria-label="New">
              <AddIcon />
            </IconButton>
          </Tooltip>
          </React.Fragment>
        }}
        />
    </Paper>
    );
  }
}

export default MarketContextList;