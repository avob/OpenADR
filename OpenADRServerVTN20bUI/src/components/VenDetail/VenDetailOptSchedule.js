import React from 'react';


import Typography from '@material-ui/core/Typography';

import VenDetailHeader from './VenDetailHeader'
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

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'

import { MarketContextSelectDialog } from '../common/VtnconfigurationDialog'

import { history } from '../../store/configureStore';

import {DatePicker, DurationPicker } from '../common/TimePicker'

import {formatTimestamp} from '../../utils/time'

import FilterPanel from '../common/FilterPanel' 

const deltaStartDays = 7
const deltaEndDays = 7


var VenOptTable = (props) => {
  const {classes} = props;
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Opt ID</TableCell>
            <TableCell align="right">MarketContext</TableCell>
            <TableCell align="right">Start</TableCell>
            <TableCell align="right">End</TableCell>
            <TableCell align="right">Opt</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.venOpt.map( (row, index) => {
            var startDatetime = formatTimestamp(row.start);
            var endDatetime = formatTimestamp(row.end);
            return (

            <TableRow key={index}>
              <TableCell scope="row" align="right">{row.optId}</TableCell>
              <TableCell scope="row" align="right">{row.marketContext}</TableCell>
              <TableCell scope="row" align="right">{startDatetime.date + " " +startDatetime.time + " " + startDatetime.tz}</TableCell>
              <TableCell scope="row" align="right">{endDatetime.date + " " +endDatetime.time + " " + endDatetime.tz}</TableCell>
              <TableCell scope="row" align="right">{row.opt}</TableCell>
            </TableRow>
          )
          })}
        </TableBody>
      </Table>
    </Paper>
  );
}

export class VenDetailOptSchedule extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    var now = new Date();
    var today = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);

    this.state.start = today.getTime() - deltaStartDays * 24 * 60 * 60 * 1000;
    this.state.end = today.getTime() + deltaEndDays * 24 * 60 * 60 * 1000;

    this.state.filter = [];

  }


  handleMarketContextSelectOpen = () => {
    this.setState( {
      marketContextSelectDialogOpen: true
    } )
  }


  onStartChange = (start) =>  {
    this.setState({start})
  }


  onEndChange = (end) =>  {
    this.setState({end})
  }

  onFilterChange = (filter) => {this.setState({filter})}

  render() {
    const {classes, ven, venOpt, marketContext} = this.props;

    return (
    <div className={ classes.root } >
      <VenDetailHeader classes={classes} ven={ven} actions={[
 
      ]
      }/>
      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />

      <Grid container>
        <Grid container >

          <Grid item xs={ 3 }>
            <DatePicker classes={ classes } field="Start" 
            value={this.state.start} onChange={this.onStartChange} />
             <DatePicker classes={ classes } field="End" 
            value={this.state.end} onChange={this.onEndChange} />
          </Grid>
          <Grid item xs={ 9 }>
            <FilterPanel classes={classes} hasFilter={{marketContext:true}} 
                marketContext={marketContext}
                filter={this.state.filter}
                onFilterChange={this.onFilterChange}
            />
          </Grid>
        </Grid>
      </Grid>
      
      <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
      <VenOptTable classes={classes} venOpt={venOpt}/>


    </div>
    );
  }
}



export default VenDetailOptSchedule;
