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

import VenDetailHeader from './VenDetailHeader'

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';



var VenAvailableReportTable = (props) => {
  const {classes, availableReport, ven} = props
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Specifier ID</TableCell>
            <TableCell align="right">Created<br/>Date/Time</TableCell>
            <TableCell align="right">Duration</TableCell>
            <TableCell align="right">Report Name</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {availableReport.map(row => (
            <TableRow key={row.id}>
              <TableCell align="right">{row.reportSpecifierId}</TableCell>
              <TableCell align="right"></TableCell>
              <TableCell align="right">{row.duration}</TableCell>
              <TableCell align="right">{row.reportName}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}

var VenRequestedReportTable = (props) => {
  const {classes, requestedReport, ven} = props
  console.log(requestedReport)
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Specifier ID</TableCell>
            <TableCell align="right">Created<br/>Date/Time</TableCell>
            <TableCell align="right">Duration</TableCell>
            <TableCell align="right">Report Name</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {requestedReport.map(row => (
            <TableRow key={row.id}>
              <TableCell component="th" scope="row">
                {row.name}
              </TableCell>
              <TableCell align="right">{row.reportSpecifierId}</TableCell>
              <TableCell align="right"></TableCell>
              <TableCell align="right">{row.duration}</TableCell>
              <TableCell align="right">{row.reportName}</TableCell>
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
    const {classes, ven, availableReport, requestedReport} = this.props;
    console.log(this.props)
    console.log(ven, availableReport, requestedReport)



    var SuccessSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.success }
                       style={ { maxWidth: 'none', paddingTop:0, paddingBottom:0 } }
                       message={ <Grid container direction="row" alignItems="center">
                            <Grid item>
                              <DoneIcon style={ { width: 20, height: 20, marginRight: 20, color:"#fff" } }/>
                            </Grid>
                            <Grid item style={{}}>
                               { props.message }
                            </Grid>
                          </Grid>} />
      );
    }

    var DefaultSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.success }
                       style={ { maxWidth: 'none', paddingTop:0, paddingBottom:0, backgroundColor:"#fafafa" } }
                       message={ 
                          <Grid container direction="row" alignItems="center">
                            <Grid item>
                              <CloseIcon style={ { width: 20, height: 20, marginRight: 20, color:"#000" } }/>
                            </Grid>
                            <Grid item>
                               { props.message }
                            </Grid>
                          </Grid>
                     } />
      );
    }



    return (
    <div className={ classes.root } >
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Status
      </Typography>
      <VenDetailHeader classes={classes} ven={ven} actions={
        <div>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small">
              <AddIcon style={ { marginRight: 15 } }/> CREATE REQUEST
            </Button>
          </Grid>
          <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small">
              <CloudDownloadIcon style={ { marginRight: 15 } }/> REQUIRE REPORTS
            </Button>
          </Grid>
          <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small">
              <CloudDownloadIcon style={ { marginRight: 15 } }/> SEND REQUESTS
            </Button>
          </Grid>
        </Grid>
        <Grid container spacing={ 24 }>
        <Grid item xs={ 4 }>
          <Button key="btn_create"
                  style={ { marginTop: 15 } }
                  variant="outlined"
                  color="secondary"
                  fullWidth={true}
                  size="small">
            <RemoveIcon style={ { marginRight: 15 } }/> CLEAN REQUESTS
          </Button>
        </Grid>
        <Grid item xs={ 4 }>
          <Button key="btn_create"
                  style={ { marginTop: 15 } }
                  variant="outlined"
                  color="secondary"
                  fullWidth={true}
                  size="small">
            <RemoveIcon style={ { marginRight: 15 } }/> CLEAN REPORTS
          </Button>
        </Grid>
        </Grid>
      </div>
      }/>
      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Reports
      </Typography>
      <VenAvailableReportTable ven={ven} availableReport={availableReport} classes={classes}/>


      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        Requests
      </Typography>
      <VenRequestedReportTable ven={ven} requestedReport={requestedReport} classes={classes}/>

     
    </div>
    );
  }
}

export default VenDetailReport;
