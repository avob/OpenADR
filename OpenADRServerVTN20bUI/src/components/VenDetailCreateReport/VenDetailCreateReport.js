import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles, MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

import { VtnConfigurationVenCard, VtnConfigurationMarketContextCard, VtnConfigurationGroupCard } from '../common/VtnConfigurationCard'
import { MarketContextSelectDialog, GroupSelectDialog } from '../common/VtnconfigurationDialog'

import Input from '@material-ui/core/Input';
import InputBase from '@material-ui/core/InputBase';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';


import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';


import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import GridListTileBar from '@material-ui/core/GridListTileBar';

import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import DeleteIcon from '@material-ui/icons/Delete';
import FilterListIcon from '@material-ui/icons/FilterList';

import StarBorderIcon from '@material-ui/icons/StarBorder';

import ChipInput from 'material-ui-chip-input'

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import SearchIcon from '@material-ui/icons/Search';


import SnackbarContent from '@material-ui/core/SnackbarContent';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';


import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import Checkbox from '@material-ui/core/Checkbox';

import Toolbar from '@material-ui/core/Toolbar';

import { lighten } from '@material-ui/core/styles/colorManipulator';

import Fab from '@material-ui/core/Fab';


const toolbarStyles = theme => ({
  root: {
    paddingRight: theme.spacing.unit,
  },
  highlight:
    theme.palette.type === 'light'
      ? {
          color: theme.palette.secondary.main,
          backgroundColor: lighten(theme.palette.primary.light, 0.85),
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
  },
  title: {
    flex: '0 0 auto',
  },
  margin: {
    marginRight: '10px',
  },
});

let VenAvailableReportDescriptionTableToolbar = props => {
  const { numSelected, availableReport, classes, handeCreateRequestClick } = props;
  return (
    <Toolbar
      className={classNames(classes.root, {
        [classes.highlight]: numSelected > 0,
      })}
    >
      <div className={classes.title}>
        {numSelected > 0 ? (
          <Typography color="primary" variant="subtitle1">
            {numSelected} selected
          </Typography>
        ) : (
          <Typography variant="h6" id="tableTitle">
            Select Report Descriptions
          </Typography>
        )}
      </div>
      <div className={classes.spacer} />
      <div className={classes.actions}>
        {numSelected > 0 ? (
          <Tooltip title="Request selected report description">
           
          <Button variant="outlined" color="primary" aria-label="Add" className={classes.margin} onClick={handeCreateRequestClick}>
            <CloudDownloadIcon className={classes.margin} />
             {(availableReport.reportName == "METADATA_TELEMETRY_USAGE") ? "Subscribe" : "Request"}
          </Button>

          </Tooltip>
        ) : null}
      </div>
    </Toolbar>
  );
};

VenAvailableReportDescriptionTableToolbar = withStyles(toolbarStyles)(VenAvailableReportDescriptionTableToolbar);

class VenAvailableReportDescriptionTable extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {}
    this.state.selected = [];
  }

  handleSelectAllClick = (event) => {
    if (event.target.checked) {
      this.setState(state => ({ selected: this.props.availableReportDescription.map(n => n.rid) }));
      return;
    }
    this.setState({ selected: [] });
  };

  handleClick = (event, rid) => {
    const { selected } = this.state;
    const selectedIndex = selected.indexOf(rid);
    let newSelected = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, rid);
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
  }
 
  handleChangePage = (event, page) => {
    this.setState({ page });
  };

  handleChangeRowsPerPage = event => {
    this.setState({ rowsPerPage: event.target.value });
  };

  isSelected = rid => this.state.selected.indexOf(rid) !== -1;

  handeCreateRequestClick = () => {
    var start = null;
    var end = null;
    var granularity = null;
    var reportBackDuration = null;
    var rid = null;
    if(this.state.selected.length < this.props.availableReportDescription.length) {
      rid = this.state.selected;
    }
    if(this.props.availableReport.reportName == "METADATA_HISTORY_USAGE"){

      this.props.createRequestedReport(this.props.venId, this.props.reportSpecifierId
          , start, end, rid);
    }
    else if(this.props.availableReport.reportName == "METADATA_TELEMETRY_USAGE"){
      this.props.createRequestedReportSubscription(this.props.venId, this.props.reportSpecifierId
          , granularity, reportBackDuration, rid);
    }
    
  }

  render() {
    const {classes, availableReportDescription, availableReport} = this.props
    return (
      <Paper className={classes.root}>
        <VenAvailableReportDescriptionTableToolbar handeCreateRequestClick={this.handeCreateRequestClick} availableReport={availableReport} numSelected={this.state.selected.length} />
        <Table className={classes.table}>
          <TableHead>
            <TableRow>
              <TableCell padding="checkbox">
                <Checkbox
                   color="primary"
                  indeterminate={this.state.selected.length > 0 && this.state.selected.length < availableReportDescription.length}
                  checked={this.state.selected.length === availableReportDescription.length}
                  onChange={this.handleSelectAllClick}
                />
              </TableCell>
              <TableCell align="right">rID</TableCell>
              <TableCell align="right">ReportType</TableCell>
              <TableCell align="right">ReadingType</TableCell>
              <TableCell align="right">OadrMinPeriod</TableCell>
              <TableCell align="right">OadrMaxPeriod</TableCell>
              <TableCell align="right">OadrOnchange</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {availableReportDescription.map(row => {
              const isSelected = this.isSelected(row.rid);
              return (
                <TableRow key={row.id}
                  hover
                      onClick={event => this.handleClick(event, row.rid)}
                      role="checkbox"
                      aria-checked={isSelected}
                      tabIndex={-1}
                      selected={isSelected}>
                  <TableCell padding="checkbox">
                     <Checkbox checked={isSelected}  color="primary"/>
                  </TableCell>
                  <TableCell align="right">{row.rid}</TableCell>
                  <TableCell align="right">{row.reportType}</TableCell>
                  <TableCell align="right">{row.readingType}</TableCell>
                  <TableCell align="right">{row.oadrMinPeriod}</TableCell>
                  <TableCell align="right">{row.oadrMaxPeriod}</TableCell>
                  <TableCell align="right">{(row.oadrOnChange) ? "True" : "False"}</TableCell>
              </TableRow>
            )
            })}
          </TableBody>
        </Table>
      </Paper>
    );
  }
  
}




export class VenDetailCreateReport extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }

  

  render() {
    const {classes, availableReportDescription, availableReport
      , venId, reportSpecifierId
      , createRequestedReport, createRequestedReportSubscription} = this.props;

    console.log(availableReportDescription, availableReport, this.props)


    return (
    <div className={ classes.root } >
      <VenAvailableReportDescriptionTable availableReport={availableReport} 
          availableReportDescription={availableReportDescription} 
          handeCreateRequestClick={this.handeCreateRequestClick}
          createRequestedReport={createRequestedReport}
          createRequestedReportSubscription={createRequestedReportSubscription}
          venId={venId}
          reportSpecifierId={reportSpecifierId}
          classes={classes}/>
    </div>
    );
  }
}

export default VenDetailCreateReport;
