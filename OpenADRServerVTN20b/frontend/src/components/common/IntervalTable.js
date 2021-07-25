import React from 'react';

import { withStyles} from '@material-ui/core/styles';




import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';


import TextField from '@material-ui/core/TextField';



import Grid from '@material-ui/core/Grid';


import MenuItem from '@material-ui/core/MenuItem';

import Select from '@material-ui/core/Select';



import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';

import AddIcon from '@material-ui/icons/Add';

import Button from '@material-ui/core/Button';

import Toolbar from '@material-ui/core/Toolbar';
import { lighten } from '@material-ui/core/styles/colorManipulator';

import EventTargetPanel from './EventTargetPanel';




const toolbarStyles = theme => ({
  root: {
    paddingRight: theme.spacing(1),
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

var IntervalTable = (props) => {
  const {classes, edit} = props;
  return (
    <div >


    {edit ? 
      <Toolbar>
        <Grid container spacing={ 24 }>

          <Grid item xs={ 4 }>
            <TextField label="Interval Duration (min)" required
                        type="number"
                       placeholder="Duration"
                       value={ props.createIntervalDuration }
                       className={ classes.textField }
                       onChange={ props.handleCreateIntervalDurationChange }
                       InputLabelProps={{ shrink: true }}
                       fullWidth={ true }/>
          </Grid> 
          <Grid item xs={ 4 }>
            <TextField label="Interval Value" required
                       placeholder="Value"
                       value={ props.createIntervalValue }
                       className={ classes.textField }
                       onChange={ props.handleCreateIntervalValueChange }
                       InputLabelProps={{ shrink: true }}
                       fullWidth={ true } />
          </Grid> 
         <Grid item xs={ 2 }>
            <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            style={ { marginTop: 13 } }
                            onClick={ props.handleCreateIntervalClick }>
                      <AddIcon />{(props.createIntervalEditMode ) ? "Edit" : "Create"}
                    </Button>
          </Grid>

          {(props.needIntervalCreate && props.createIntervalValue !== "" && props.createIntervalDuration !== "") ? <Grid item xs={ 2 }>
            <Button key="btn_create"
                            variant="outlined"
                            color="secondary"
                            size="small"
                            style={ { marginTop: 13 } }
                            onClick={ props.handleCancelIntervalClick }>
                      <AddIcon />Cancel
                    </Button>
          </Grid>: null}
          
          
      </Grid>
      </Toolbar> : null}
      {(edit && props.intervals.length > 0) ? <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="left">Interval ID</TableCell>
            <TableCell align="right">Interval Duration</TableCell>
            <TableCell align="right">Interval Value</TableCell>
            <TableCell align="right"></TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.intervals.map( (row, index) => (
            <TableRow key={index}>
              <TableCell scope="row" align="left">{index}</TableCell>
              <TableCell scope="row" align="right">{row.duration}</TableCell>
              <TableCell scope="row" align="right">{row.value}</TableCell>

              <TableCell scope="row" align="right">
                  <Button size="small" color="primary" onClick={props.handleEditIntervalClick(row, index)}> EDIT </Button>
              </TableCell>
              <TableCell scope="row" align="right">
                  <Button size="small" color="secondary" onClick={props.handleRemoveSignalIntervalAtIndex(index)}> REMOVE </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table> : null}
      {(!edit && props.intervals.length > 0) ? <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Interval ID</TableCell>
            <TableCell>Interval Duration</TableCell>
            <TableCell>Interval Value</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.intervals.map( (row, index) => (
            <TableRow key={index}>
              <TableCell>{index}</TableCell>
              <TableCell>{row.duration}</TableCell>
              <TableCell>{row.value}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table> : null}
      
    </div>
  );
}

IntervalTable = withStyles(toolbarStyles)(IntervalTable);

export default IntervalTable;