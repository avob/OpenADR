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

import DetailedIntervalEditTable from './DetailedIntervalEditTable'



function Row(props) {
  const { classes, row, definition, filterUnit, onUnitChange, onIntervalChange } = props;

  return (
      <React.Fragment>
        <TableRow className={classes.root}>              
          <TableCell>
            <TextField required error={props.error}
              label="Baseline Name"
              className={classes.textField}
              value={row.baselineName}
              onChange={(e) => props.onBaselineNameChange(e)}
              InputLabelProps={{
                shrink: true,
              }}    
            />
          </TableCell>
          <TableCell>
            <UnitEditPanel classes={classes} definition={definition} filterUnit={filterUnit} onUnitChange={onUnitChange} unit={row.itemBase}/>

          </TableCell>
       
        </TableRow>
        <TableRow>
          <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
              <Grid container>
                <Grid item xs={ 6 }>
                  <Box mx={2}>
                    <DetailedIntervalEditTable classes={classes} intervals={row.intervals} onIntervalChange={onIntervalChange}/>
                  </Box>
                </Grid>
              </Grid>
          </TableCell>
        </TableRow>
      </React.Fragment>
   );
}

var DetailedBaselineEditTable = (props) => {
  const {classes, baseline, definition, filterUnit} = props
  var onBaselineNameChange = (e) => {
      baseline.baselineName = e.target.value;
      props.onBaselineChange(baseline) 
  }

  var addBaseline = () => {
    props.onBaselineChange({
      baselineName: "",
      itemBase: {
        itemDescription: "No Unit",
        itemUnits: "No Unit",
        siScaleCode: "none"
      },
      intervals: [{
        duration: "",
        value: ""
      }]
    });
  }

  var removeBaseline = () => {
    props.onBaselineChange(null);
  }

  var onUnitChange = (unit) => {
    baseline.itemBase = unit;
    props.onBaselineChange(baseline) 
  }

  var onIntervalChange = (intervals) => {
    baseline.intervals = intervals;
    props.onBaselineChange(baseline) 
  }

  return (
    <div className={classes.root}>



      {baseline && baseline.itemBase ? 
       <React.Fragment>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Unit</TableCell>
            <TableCell />
          </TableRow>
        </TableHead>
        <TableBody>
           <Row classes={classes} row={baseline} definition={definition}
                  filterUnit={filterUnit}
                  onUnitChange={onUnitChange}
                  onBaselineNameChange={onBaselineNameChange}
                  onIntervalChange={onIntervalChange}

                  /> 
          
        </TableBody>
      </Table>

      <Button key="btn_delete"
                                        color="secondary"
                                        size="small"
                                        className={ classes.button }
                                        onClick={ removeBaseline }>
                                  Remove Baseline
                                </Button>
        </React.Fragment>
      : <Button key="btn_delete"
                                        color="primary"
                                        size="small"
                                        className={ classes.button }
                                        onClick={ addBaseline }>
                                  Add Baseline
                                </Button> }
    </div>
  )

}

export default DetailedBaselineEditTable;

