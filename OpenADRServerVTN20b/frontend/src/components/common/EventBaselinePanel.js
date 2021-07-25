import React from 'react';

import { withStyles} from '@material-ui/core/styles';



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

import IntervalTable from './IntervalTable'

import {  VtnTextField } from './TextField'
import FormGroup from '@material-ui/core/FormGroup';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';

import UnitPanel from './UnitPanel'


export class EventBaselinePanel extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {};
    this.state.createIntervalDuration = "";
    this.state.createIntervalValue = "";
    this.state.createIntervalEditMode = false;
    this.state.createIntervalEditIndex = null;
    this.state.needIntervalCreate = false;
   

    if(props.baseline.intervals.length > 0) {
      this.state.needIntervalCreate = true;
    }
  }

  componentWillReceiveProps(nextProps) {
    var newState = {};
    var stateChanged = false;

    if(nextProps.baseline.intervals.length > 0) {
      newState.needIntervalCreate = true;
      stateChanged = true;
    }

    if(stateChanged){
      this.setState(newState);
    }
  }

  handleCreateIntervalDurationChange = (e) => {
    this.setState({createIntervalDuration: e.target.value});
  }

  handleCreateIntervalValueChange = (e) => {
    this.setState({createIntervalValue: e.target.value});
  }


  
  handleCreateIntervalClick = () => {
    let baseline = this.props.baseline;
    let interval = {
        duration: this.state.createIntervalDuration,
        value: this.state.createIntervalValue
      }
    if(this.state.createIntervalEditMode){
      baseline.intervals[this.state.createIntervalEditIndex] = interval;
    }
    else {
      baseline.intervals.push(interval);
    }
    
    this.props.onChange(baseline);
    this.setState({
      createIntervalEditMode: false,
      createIntervalEditIndex: null,
      createIntervalDuration: "",
      createIntervalValue: ""
    })
  }

  handleEditIntervalClick =(interval, index) => () => {
    this.setState({
      createIntervalEditMode: true,
      createIntervalDuration: interval.duration,
      createIntervalValue: interval.value,
      createIntervalEditIndex: index
    })
  }

  handleCancelIntervalClick = () => {
    this.setState({
      createIntervalEditMode: false,
      createIntervalEditIndex: null,
      createIntervalDuration: "",
      createIntervalValue: ""
    })
  }

  handleNeedIntervalCreateClick = (e) => {
    if(!e.target.checked) {
      let baseline = this.props.baseline;
      baseline.intervals = [];
      this.props.onChange(baseline);
    }
    this.setState({needIntervalCreate: e.target.checked});
  }


  render() {
    const {classes, hasError, baseline, edit} = this.props;



    return (
      <div style={{margin:"0 5%"}}>
     
      <FormControl fullWidth >
        <FormLabel>Identitication</FormLabel>
      <FormGroup aria-label="position" row>
        <Grid container>
          <Grid item xs={ 4 }>
            <VtnTextField className={ classes.textField } field="ID" value={ baseline.baselineId } />
          </Grid>
          <Grid item xs={ 4 }>
            <VtnTextField className={ classes.textField } field="Name" value={ baseline.baselineName} />
          </Grid>
          <Grid item xs={ 4 }>
            <UnitPanel unit={baseline.itemBase} />
          </Grid>
          
        </Grid>

        </FormGroup>
   </FormControl>
      <FormLabel component="legend">
        Intervals
      </FormLabel>
      <Grid container style={{margin:"20px 0"}}>
        <Grid item xs={ 12 }>
          <IntervalTable edit={edit} intervals={baseline.intervals} 
          handleRemoveSignalIntervalAtIndex={this.handleRemoveSignalIntervalAtIndex}
          needIntervalCreate={this.state.needIntervalCreate}
          createIntervalValue={this.state.createIntervalValue}
          createIntervalDuration={this.state.createIntervalDuration}
          createIntervalEditMode={this.state.createIntervalEditMode}
          handleCreateIntervalDurationChange={this.handleCreateIntervalDurationChange}
          handleCreateIntervalValueChange={this.handleCreateIntervalValueChange}
          handleCreateIntervalClick={this.handleCreateIntervalClick}
          handleEditIntervalClick={this.handleEditIntervalClick}
          handleCancelIntervalClick={this.handleCancelIntervalClick}
          />
        </Grid> 
      </Grid>


      
      
      </div>
    );
  }
}

export default EventBaselinePanel;

