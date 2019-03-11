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


var VenAvailableReportDescriptionTable = (props) => {
  const {classes, availableReportDescription} = props
  console.log(availableReportDescription)

  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">rID</TableCell>
            <TableCell align="right">ReportType</TableCell>
            <TableCell align="right">ReadingType</TableCell>
            <TableCell align="right">OadrMinPeriod</TableCell>
            <TableCell align="right">OadrMaxPeriod</TableCell>
            <TableCell align="right">OadrOnchange</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {availableReportDescription.map(row => (
            <TableRow key={row.id}>
              <TableCell align="right">{row.rid}</TableCell>
              <TableCell align="right">{row.reportType}</TableCell>
              <TableCell align="right">{row.readingType}</TableCell>
              <TableCell align="right">{row.oadrMinPeriod}</TableCell>
              <TableCell align="right">{row.oadrMaxPeriod}</TableCell>
              <TableCell align="right">{(row.oadrOnChange) ? "True" : "False"}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}




export class VenDetailReport extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }

  render() {
    const {classes, availableReportDescription, availableReport} = this.props;

    console.log(availableReportDescription, availableReport)


    return (
    <div className={ classes.root } >
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Report Descriptions
      </Typography>
      <VenAvailableReportDescriptionTable availableReportDescription={availableReportDescription} classes={classes}/>
  
    </div>
    );
  }
}

export default VenDetailReport;
