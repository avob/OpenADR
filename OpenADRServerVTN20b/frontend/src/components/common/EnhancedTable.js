import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Checkbox from '@material-ui/core/Checkbox';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import FilterListIcon from '@material-ui/icons/FilterList';
import { lighten } from '@material-ui/core/styles/colorManipulator';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import DeleteIcon from '@material-ui/icons/Delete';
import Divider from '@material-ui/core/Divider';
import {H1} from '../common/Structure'



class EnhancedTableHead extends React.Component {
  createSortHandler = property => event => {
    this.props.onRequestSort(event, property);
  };

  render() {
    const { onSelectAllClick, order, orderBy, numSelected, rowCount, rows, selectable } = this.props;
    return (
      <TableHead>
        <TableRow>
          {selectable ? <TableCell padding="checkbox">
            <Checkbox
              indeterminate={numSelected > 0 && numSelected < rowCount}
              checked={numSelected === rowCount}
              onChange={onSelectAllClick}
            />
          </TableCell> : <TableCell padding="checkbox">
          </TableCell>  }
          
          {rows.map(
            row => (
              <TableCell
                key={row.id}
                align={row.numeric ? 'right' : 'left'}
                padding={row.disablePadding ? 'none' : 'default'}
                sortDirection={orderBy === row.id ? order : false}
              >
                <Tooltip
                  title="Sort"
                  placement={row.numeric ? 'bottom-end' : 'bottom-start'}
                  enterDelay={300}
                >

                  <TableSortLabel
                    active={orderBy === row.id}
                    direction={order}
                    onClick={this.createSortHandler(row.id)}

                  >
                    {row.label}
                  </TableSortLabel>
                </Tooltip>
              </TableCell>
            ),
            this,
          )}
        </TableRow>
      </TableHead>
    );
  }
}

EnhancedTableHead.propTypes = {
  numSelected: PropTypes.number.isRequired,
  onRequestSort: PropTypes.func.isRequired,
  onSelectAllClick: PropTypes.func.isRequired,
  order: PropTypes.string.isRequired,
  orderBy: PropTypes.string.isRequired,
  rowCount: PropTypes.number.isRequired,
};

const toolbarStyles = theme => ({
  root: {
    paddingRight: theme.spacing(1),
  },
  highlight:
    theme.palette.type === 'light'
      ? {
          color: theme.palette.secondary.main,
          backgroundColor: lighten(theme.palette.secondary.light, 0.85),
        }
      : {
          color: theme.palette.text.primary,
          backgroundColor: theme.palette.secondary.dark,
        },
  spacer: {
    flex: '1 1 100%',
  },
  actions: {
    color: theme.palette.text.secondary,
    flex: '0 0 auto',
  },
  title: {
    flex: '0 0 auto',
  },
});

let EnhancedTableToolbar = props => {
  const { numSelected, classes, action, actionSelected, handleSelectableClick, handleDeleteSelectedClick, selectable, filterPanel, handleFilterClick, filter } = props;
  return (
    <Toolbar
      className={classNames(classes.root, {
        [classes.highlight]: numSelected > 0,
      })}
    >
      {props.title ? <div className={classes.title}>
        {numSelected > 0 ? <H1 value={numSelected+" selected" }/>
         : <H1 value={props.title}/>
        }
      </div> : null }

      <div className={classes.spacer} />
      <div className={classes.actions}>

        {numSelected <= 0 && action ? action() : null}
        {numSelected <= 0 ? <Tooltip title="Selectable" onClick={handleSelectableClick}>
            <IconButton aria-label="Selectable" color={selectable ? "primary" : "default"}> 
              <MoreVertIcon />
            </IconButton>
          </Tooltip> : null}

        {numSelected > 0 && handleDeleteSelectedClick ? <Tooltip title="Selectable" onClick={handleDeleteSelectedClick}>
            <IconButton> 
              <DeleteIcon />
            </IconButton>
          </Tooltip> : null}

        { numSelected <= 0 && filterPanel ? <Tooltip title="Filter" onClick={handleFilterClick}>
                    <IconButton aria-label="Filter" color={filter.length !== 0 ? "primary" : "default"}>
                      <FilterListIcon />
                    </IconButton>
                  </Tooltip> : null}
        
      </div>
          


    </Toolbar>
  );
};

EnhancedTableToolbar.propTypes = {
  classes: PropTypes.object.isRequired,
  numSelected: PropTypes.number.isRequired,
  title: PropTypes.string.isRequired,
};

EnhancedTableToolbar = withStyles(toolbarStyles)(EnhancedTableToolbar);

const styles = theme => ({
  root: {
    width: '100%',
  },
  table: {
    minWidth: 1020,
  },
  tableWrapper: {
    overflowX: 'auto',
  },
});

class EnhancedTable extends React.Component {
  state = {
    selected: [],
    selectable: false,
    filterable: this.props.filter && this.props.filter.length !== 0 ? true : false
  };

  handleRequestSort = (event, property) => {
    const orderBy = property;
    let order = 'desc';

    if (this.props.sort.by === property && this.props.sort.sort === 'desc') {
      order = 'asc';
    }

    this.props.handleSortChange({
      sort: order, 
      by: orderBy
    });
  };

  handleSelectAllClick = event => {
    if (event.target.checked) {
      this.setState(state => ({ selected: this.props.data.map(n => n.username) }));
      return;
    }
    this.setState({ selected: [] });
  };

  handleClick = (event, n) => {
    const id = n.id;
    if(this.state.selectable) {
      const { selected } = this.state;
      const selectedIndex = selected.indexOf(id);
      let newSelected = [];

      if (selectedIndex === -1) {
        newSelected = newSelected.concat(selected, id);
      } else if (selectedIndex === 0) {
        newSelected = newSelected.concat(selected.slice(1));
      } else if (selectedIndex === selected.length - 1) {
        newSelected = newSelected.concat(selected.slice(0, -1));
      } else if (selectedIndex > 0) {
        newSelected = newSelected.concat(
          selected.slice(0, selectedIndex),
          selected.slice(selectedIndex + 1),
        );
      }

      this.setState({ selected: newSelected });
    } else if(this.props.handleClick){
      this.props.handleClick(n);
    }
    
  };

  handleChangePage = (event, page) => {
    this.props.handlePaginationChange({
      size: this.props.pagination.size
      , page: page
    });
  };

  handleChangeRowsPerPage = event => {
    this.props.handlePaginationChange({
      size: event.target.value 
      , page: this.props.pagination.page
    });
  };

  isSelected = id => this.state.selected.indexOf(id) !== -1;

  handleSelectableClick = e => {
    this.setState({ selectable: !this.state.selectable});
  }

  handleFilterClick = e => {
    this.setState({ filterable: !this.state.filterable});
  }

  handleDeleteSelectedClick = e => {
    this.props.handleDeleteSelectedClick(this.state.selected);
  }

  render() {
    const { classes, rows, rowTemplate, data, total, pagination, sort, title, subtitle, action, actionSelected, filterPanel, filter } = this.props;
    const { selected, selectable, filterable } = this.state;
    const rowsPerPage = pagination.size;
    const page = pagination.page;
    const emptyRows = rowsPerPage - Math.min(rowsPerPage, total - page * rowsPerPage); 
    const order = sort.sort;
    const orderBy= sort.by;
    return (
      <React.Fragment>
        <EnhancedTableToolbar numSelected={selected.length} title={title} subtitle={subtitle} action={action}
              actionSelected={actionSelected}
              handleSelectableClick={this.handleSelectableClick}
              handleDeleteSelectedClick={this.handleDeleteSelectedClick}
              handleFilterClick={this.handleFilterClick}
              selectable={selectable}
              filterable={filterable}
              filterPanel={filterPanel}
              filter={filter}/>

        {filterPanel != null && filterable ? <React.Fragment><Divider/>{filterPanel()}</React.Fragment> : null}
        {title ? <Divider/> : null}
        
          <Table className={classes.table} aria-labelledby="tableTitle">
            <EnhancedTableHead
              numSelected={selected.length}
              order={order}
              orderBy={orderBy}
              onSelectAllClick={this.handleSelectAllClick}
              onRequestSort={this.handleRequestSort}
              rowCount={total}
              rows={rows}
              selectable={selectable}
              
              
            />
            <TableBody>
              {data
                .map(n => {
                  const isSelected = this.isSelected(n.id);
                  const row = rowTemplate(n);
                  return (
                    <TableRow
                      hover
                      onClick={event => this.handleClick(event, n)}
                      role="checkbox"
                      aria-checked={isSelected}
                      tabIndex={-1}
                      key={n.id}
                      selected={isSelected}
                    >
                      {  selectable ? 
                      <TableCell padding="checkbox">
                        <Checkbox checked={isSelected} />
                      </TableCell>
                      : <TableCell padding="checkbox">
                      </TableCell> }
                      { row }
                    </TableRow>
                  );
                })}
              {emptyRows > 0 && (
                <TableRow style={{ height: 49 * emptyRows }}>
                  <TableCell colSpan={6} />
                </TableRow>
              )}
            </TableBody>
          </Table>
        <TablePagination
          rowsPerPageOptions={[1, 5, 10, 25]}
          component="div"
          count={total}
          rowsPerPage={rowsPerPage}
          page={page}
          backIconButtonProps={{
            'aria-label': 'Previous Page',
          }}
          nextIconButtonProps={{
            'aria-label': 'Next Page',
          }}
          onChangePage={this.handleChangePage}
          onChangeRowsPerPage={this.handleChangeRowsPerPage}
        />
      </React.Fragment>
    );
  }
}

EnhancedTable.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(EnhancedTable);