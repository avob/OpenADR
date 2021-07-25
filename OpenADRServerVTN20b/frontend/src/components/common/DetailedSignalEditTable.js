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



const useRowStyles = makeStyles({
  root: {
    '& > *': {
      borderBottom: 'unset',
    },
  },
});





function Row(props) {
  const { row, index, definition, filterUnit, countSignals } = props;
  const [open, setOpen] = React.useState(false);
  const classes = useRowStyles();
   const assets = row.targets && row.targets.filter(t => t.targetType === "ENDDEVICE_ASSET" );
  const endDeviceAsset = (assets && assets.length === 1) ? assets[0].targetValue : "";
  return (
      <React.Fragment>
              <TableRow className={classes.root}> 
                <TableCell>
                  <EnhancedAutocomplete
                    creatable
                    options={definition.signalName}
                    value={row.signalName}
                    label = "Signal Name"
                    onSelected={props.onSignalNameChange(index)}
                  />

                </TableCell>
                <TableCell>
                  <EnhancedAutocomplete
                    options={definition.signalType}
                    value={row.signalType}
                    label = "Signal Type"
                    onSelected={props.onSignalTypeChange(index)}
                  />
                </TableCell>
                <TableCell>
                  <UnitEditPanel definition={definition} filterUnit={filterUnit} onUnitChange={props.onUnitChange(index)} unit={row.itemBase}/>
                </TableCell>
                <TableCell>
                  <EnhancedAutocomplete
                      creatable
                      options={definition.endDeviceAsset}
                      value={endDeviceAsset}
                      label = "Target Type"
                      onSelected={props.onTargetTypeChange(index, 0)}
                    />
                </TableCell>
                <TableCell>
                    
                    {(countSignals > 1) && 
                        <Button key="btn_delete"
                              color="secondary"
                              size="small"
                              className={ classes.button }
                              onClick={ props.onRemoveSignalClick(index) }>
                        Remove Signal
                      </Button>
                    }
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                    <Grid container>
                      <Grid item xs={ 6 }>
                        <Box mx={2}>
                          <DetailedIntervalEditTable classes={classes} intervals={row.intervals} onIntervalChange={props.onIntervalChange(index)}/>
                        </Box>
                      </Grid>
                     
                    </Grid>

                    
                </TableCell>
              </TableRow>
            </React.Fragment>
   );
}

var DetailedSignalEditTable = (props) => {
  const {classes, signals, definition, filterUnit} = props
  
  var onAddSignalClick = () => {
    signals.push({
      signalName: "", 
      signalType: "",
      itemBase: {
        itemDescription: "No Unit",
        itemUnits: "No Unit",
        siScaleCode: "none"
      },
      intervals: []
    });
    props.onSignalChange(signals);
  }

  var onRemoveSignalClick = (index) => {
    return () => {
      signals.splice(index, index);
      props.onSignalChange(signals);
    }
  }


  var onAddSignalIntervalClick = (index) => {
    return () => {
      if(signals[index].intervals) {
        signals[index].intervals.push({
          duration: "", 
          value: ""
        });
      } else {
        signals[index].intervals = [{}];
      }
      props.onSignalChange(signals);
    }
  }

  var onRemoveSignalIntervalClick = (index, indexInterval) => {
    return () => {
      signals[index].intervals = signals[index].intervals.splice(indexInterval, indexInterval);
      props.onSignalChange(signals);
    }
  }

  var onAddSignalTargetClick = (index) => {
    return () => {
      if(signals[index].targets) {
        signals[index].targets.push({});
      } else {
        signals[index].targets = [{}];
      }
      props.onSignalChange(signals);
    }
  }

  var onRemoveSignalTargetClick = (index, indexTarget) => {
    return () => {
      signals[index].targets = signals[index].targets.splice(indexTarget, indexTarget);
      props.onSignalChange(signals);
    }
  }

  var onSignalNameChange = (index) => {
    return (signalName) => {
      signals[index].signalName = signalName;
      props.onSignalChange(signals);
    }
  }

  var onSignalTypeChange = (index) => {
    return (signalType) => {
      signals[index].signalType = signalType;
      props.onSignalChange(signals);
    }
  }

  var onUnitChange = (index) => {
    return (unit) => {
      signals[index].itemBase = unit;
      props.onSignalChange(signals);
    }
  }

  var onIntervalChange = (index) => {
     return (intervals) => {
        signals[index].intervals = intervals;
        props.onSignalChange(signals);
     }
  }

  var onTargetTypeChange = (index, indexTarget) => {
    return (value) => {
      if(signals[index].targets && signals[index].targets[indexTarget]) {
        signals[index].targets[indexTarget].targetValue = value;
      } else {
        signals[index].targets = [{
          targetType: "ENDDEVICE_ASSET",
          targetValue: value
        }]
      }
      
      props.onSignalChange(signals);
    }
  }

  var onTargetIdChange = (index, indexTarget) => {
    return (value) => {
      signals[index].targets[indexTarget].targetId = value;
      props.onSignalChange(signals);
    }
  }




  return (
    <div className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Signal name</TableCell>
            <TableCell>Signal type</TableCell>
            <TableCell>Signal unit</TableCell>
            <TableCell>EndDeviceAsset</TableCell>
            <TableCell>
              <Button key="btn_create"
                            color="primary"
                            size="small"
                            className={ classes.button }
                            onClick={ onAddSignalClick }>
                      Add New Signal
                    </Button>
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {signals && signals.map((row, index) => (
              <Row row={row} key={index} index={index} 
                  onRemoveSignalClick={onRemoveSignalClick}
                  onAddSignalIntervalClick={onAddSignalIntervalClick} onRemoveSignalIntervalClick={onRemoveSignalIntervalClick}
                  onAddSignalTargetClick={onAddSignalTargetClick} onRemoveSignalTargetClick={onRemoveSignalTargetClick}
                  onSignalNameChange={onSignalNameChange} onSignalTypeChange={onSignalTypeChange}
                  onIntervalChange={onIntervalChange}
                  onTargetTypeChange={onTargetTypeChange} onTargetIdChange={onTargetIdChange}
                  onUnitChange={onUnitChange}
                  definition={definition}
                  filterUnit={filterUnit}
                  countSignals={signals.length}

              />
          ))}
        </TableBody>
      </Table>
    </div>
  )

}

export default DetailedSignalEditTable;

