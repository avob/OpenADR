import React from 'react';







import TextField from '@material-ui/core/TextField';

import Grid from '@material-ui/core/Grid';








import AddIcon from '@material-ui/icons/Add';

import Button from '@material-ui/core/Button';

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';





import { GroupSelectDialog, TargetSelectDialog } from './VtnconfigurationDialog'

import {VenAutocomplete} from './Autocomplete'




var EventTargetTable = (props) => {
  const {classes} = props;
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Target Type</TableCell>
            <TableCell align="right">Target ID</TableCell>
            <TableCell align="right">Targeted Devices</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.eventTarget.map( (row, index) => (
            <TableRow key={index}>
              <TableCell scope="row" align="right">{row.targetType}</TableCell>
              <TableCell scope="row" align="right">{row.targetId}</TableCell>
              <TableCell scope="row" align="right"></TableCell>
              <TableCell scope="row" align="right">
                  <Button size="small" color="secondary" onClick={props.handleRemoveTargetAtIndex(index)}> REMOVE </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}

export class EventTargetPanel extends React.Component {

  constructor( props ) {
    super( props )
    this.state = {
      createTargetType: "",
      createTargetId: "",
      targetSelectDialog: false,
      groupSelectDialog: false
    }
  }

  handleCreateTarget = () => {
    let target = this.props.eventTarget;
    target.push({
      targetType: this.state.createTargetType,
      targetId: this.state.createTargetId
    });
    this.props.onChange(target);
    this.setState({
      createTargetType: "",
      createTargetId: "",
    });
  }

  handleCancelTarget = () => {
    this.setState({
      createTargetType: "",
      createTargetId: "",
    });
  }

  handleRemoveTargetAtIndex = (index) => () => {
    let target = this.props.eventTarget;
    target.splice(index,1);
    this.props.onChange(target);
  }

  handleTargetTypeChange = (e) => {
    this.setState({createTargetType: e.target.value});
  }

  handleTargetIdChange = (e) => {
    this.setState({createTargetId: e.target.value});
  }

  handleTargetSelectDialogClose = (targetType) => {
    var newState = {targetSelectDialog: false}
    if(targetType != null){
      newState.createTargetType = targetType;
    }
    this.setState(newState);
  }

  handleTargetSelectDialogOpen = () => {
    this.setState({targetSelectDialog: true});
  }

  handleGroupSelectClose = (group) => {
    var newState = {groupSelectDialog: false}
    if(group != null){
      newState.createTargetId = group.name;
    }
    this.setState(newState);
  }

  handleGroupSelectOpen = () => {
    this.setState({groupSelectDialog: true});
  }

  onVenSuggestionsSelect = (ven) => {
    console.log(ven)
    if(ven != null){
      this.setState({createTargetId: ven.username});
    }
  } 


  render() {
    const {classes, hasError, eventTarget, group} = this.props;

    return (
    <Grid container
          spacing={ 8 }
          justify="center">
      
      <Grid container spacing={ 24 }>
          <Grid item xs={ 2 }>
           <TextField label="Select Target Type" error={hasError && eventTarget.marketContext == null}
                 value={ this.state.createTargetType }
                 placeholder="Ven, Group ..."
                 className={classes.textField}
                 fullWidth={true}
                 onClick={this.handleTargetSelectDialogOpen}
                 InputProps={ { readOnly: true, } } InputLabelProps={{ shrink: true }}/>

              <TargetSelectDialog open={ this.state.targetSelectDialog }
                                  close={ this.handleTargetSelectDialogClose }
                                  title="Select Target Type" />


            
          </Grid>

          { (this.state.createTargetType === "group") ? <Grid item xs={ 6 }>
            <TextField required label="Group" 
                 value={this.state.createTargetId}
                 className={classes.textField}
                 fullWidth={true}
                 onClick={this.handleGroupSelectOpen}
                 InputProps={{ readOnly: true }} />

              <GroupSelectDialog group={ group}
                                         open={ this.state.groupSelectDialog }
                                         close={ this.handleGroupSelectClose }
                                         title="Select Market Context" />
          </Grid> : null}

          { (this.state.createTargetType === "ven") ? <Grid item xs={ 6 }>
            <VenAutocomplete  suggestions={this.props.ven}
              onSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
              onSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
              onSuggestionsSelect={this.onVenSuggestionsSelect}/>
          </Grid> : null}

          { (this.state.createTargetType !== "" && this.state.createTargetId !== "") ? <Grid item xs={ 2 }>
            <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            className={ classes.button }
                            style={ { marginTop: 13 } }
                            onClick={ this.handleCreateTarget }>
                      <AddIcon />Create
                    </Button>
          </Grid> : null}

          { (this.state.createTargetType !== "") ? <Grid item xs={ 2 }>
            <Button key="btn_cancel"
                            variant="outlined"
                            color="secondary"
                            size="small"
                            className={ classes.button }
                            style={ { marginTop: 13 } }
                            onClick={ this.handleCancelTarget }>
                      <AddIcon />Cancel
                    </Button>
          </Grid> : null}
      </Grid>
      <Grid container spacing={ 24 }
         style={ { marginTop: 20, marginBottom:10 } }>
        <Grid item xs={ 12 }>
          <EventTargetTable classes={classes} eventTarget={eventTarget} 
          handleRemoveTargetAtIndex={this.handleRemoveTargetAtIndex}/>
        </Grid> 
      </Grid>
      
    </Grid>
    );
  }
}

export default EventTargetPanel;
