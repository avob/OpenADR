import React from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Toolbar from '@material-ui/core/Toolbar';
import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@material-ui/icons/KeyboardArrowUp';
import Box from '@material-ui/core/Box';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';


import EnhancedAutocomplete from './EnhancedAutocomplete'
import UnitEditPanel from './UnitEditPanel'
import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import {TimezonePicker, DateAndTimePicker, DurationPicker} from '../common/TimePicker'
import TextField from '@material-ui/core/TextField';




var DetailedIntervalEditTable = (props) => {
  const {classes, intervals, onIntervalChange} = props

  var onAddInterval = () => {
      intervals.push({
          duration: "", 
          value: ""
        });
      onIntervalChange(intervals);
    
  }

  var onRemoveInterval = (index) => {
      var newIntervals = [];
      intervals && intervals.map((interval, i) => {
        if(index != i) {
          newIntervals.push(interval);
        }
      });
      onIntervalChange(newIntervals);
  }

  var onIntervalDurationChange = (index) => {
    return (duration) => {
      intervals[index].duration = duration;
      onIntervalChange(intervals);
    }
  }

  var onIntervalValueChange = (index) => {
    return (value) => {
      intervals[index].value = value;
      onIntervalChange(intervals);
    }
  }

  

  return (
    <Table size="small" aria-label="intervals">
        <TableHead>
          <TableRow>
            <TableCell>Interval Duration</TableCell>
            <TableCell>Interval Value</TableCell>
            <TableCell>
              <Button key="btn_delete"
                    color="primary"
                    size="small"
                    onClick={ () => onAddInterval() }>
              Add Interval
            </Button>
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody >
          {intervals && intervals.map((interval, index) => (
            <TableRow className={classes.root} key={index}>
               <TableCell>
                  <DurationPicker classes={ classes } field="Interval Duration" 
                                     value={interval.duration} onChange={(duration) => onIntervalDurationChange(index)(duration)}/>
               </TableCell>
                <TableCell>        
                  <TextField required error={props.error}
                      label="Interval Value"
                      type="number"
                      value={interval.value}
                      onChange={(e) => onIntervalValueChange(index)(e.target.value)}
                      InputLabelProps={{
                        shrink: true,
                      }}    
                    />
                </TableCell>
                <TableCell>
              
            {(intervals.length > 1) && 
                  <Button key="btn_delete"
                          color="secondary"
                          size="small"
                          onClick={ () => onRemoveInterval(index) }>
                    Remove Interval
                  </Button>
              }
            </TableCell>
             </TableRow>
          ))}
        </TableBody>
      </Table>
  )

}

export default DetailedIntervalEditTable;

