import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles} from '@material-ui/core/styles';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'


import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';


import Typography from '@material-ui/core/Typography';

import EventDetailHeader from './EventDetailHeader'
import Divider from '@material-ui/core/Divider';
import TextField from '@material-ui/core/TextField';

import Grid from '@material-ui/core/Grid';

import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';

import {formatTimestamp} from '../../utils/time'


import {EventSignalPanel} from '../common/EventSignalPanel'

var EventTextField = (props) => {
  var value = (props.value != null) ? props.value : "";
  return (
  <TextField label={ props.field }
             value={ value }
             className={ props.className }
             margin="normal"
             fullWidth={true}
             InputProps={ { readOnly: true, } } />

  );
}

export class EventDetailSignal extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    this.state.editMode = false;
    this.state.signals = this.props.event.signals.splice(0);


  }

componentWillReceiveProps(nextProps) {
  this.setState({signals: this.props.event.signals, editMode: false});
}

  handleAddSignalClick = () => {
    var signals = this.state.signals;
    if(!this.state.editMode) {
      signals = this.props.event.signals;
    }
    signals.push({
        signalName: "",
        signalType: "",
        unitType: "",
        intervals: [],
        currentValue: "",
      });
    this.setState({signals: signals, editMode: true});
  }

  handleEventSignalChange = (index) => (signal) => {
    var signals = this.state.signals;
    signals[index] = signal;
    this.setState({signals: signals, editMode: true});
  }

  handleRemoveEventSignalChange = (index) => () => {
    var signals = this.state.signals.splice(0);
    signals.splice(index, 1)
    this.setState({signals: signals, editMode: true});
  }

  handleUpdateEvent = () => {
    var updateEvent = {};
    updateEvent.signals = this.state.signals;
    updateEvent.targets = this.props.event.targets;
    this.setState({editMode:false})
    // this.props.updateEvent(this.props.paramId, updateEvent);
    
  }

  render() {
    const {classes, event} = this.props;
    var that = this;
    var hasError = false;


    var signals = this.state.signals;

    return (
    <div className={ classes.root } >
      <EventDetailHeader classes={classes} event={event} actions={<Grid container spacing={ 24 }>

        {(this.state.editMode) ? <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleUpdateEvent}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> UPDATE
            </Button>
          </Grid> : null}
      
      </Grid>}/>



       <Divider style={ { marginTop: '20px', marginBottom:20 } } />
       {signals.map((signal, index) => {
          return (
            <div key={"signal_panel_"+index}>
            {(index !=0) ? <Grid container
                  style={ { marginTop: 20, marginBottom: 20 } }
                  spacing={ 24 }>
              <Grid item xs={ 12 }>
                <Divider />
              </Grid>
            </Grid> : null}

            <EventSignalPanel 
              classes={classes} eventSignal={signal} hasError={hasError} 
                onChange={that.handleEventSignalChange(index)}
                onRemove={that.handleRemoveEventSignalChange(index)}
                canBeRemoved={signals.length >0}/>
                
            </div>
        )
        })}

        <Divider style={ { marginTop: 30} } />
        <Grid container
              style={ { marginTop: 20 } }
              spacing={ 24 }>
          <Grid item xs={ 12 }>
            <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            className={ classes.button }
                            onClick={ this.handleAddSignalClick }>
                      <AddIcon />Add New Signal
                    </Button>
          </Grid>
        </Grid>
    </div>
    );
  }
}

export default EventDetailSignal;
