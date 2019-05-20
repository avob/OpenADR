import React from 'react';


import Grid from '@material-ui/core/Grid';

import Divider from '@material-ui/core/Divider';

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';

import {EventSignalPanel} from '../common/EventSignalPanel'


export class EventCreateEventSignalStep extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {};
  }

  handleAddSignalClick = () => {
    var eventSignal = this.props.eventSignal;
    eventSignal.push({
        signalName: "",
        signalType: "",
        unitType: "",
        intervals: [],
        currentValue: "",
      });
    this.props.onChange(eventSignal);
  }

  handleEventSignalChange = (index) => (signal) => {
    var eventSignal = this.props.eventSignal;
    eventSignal[index] = signal;
    this.props.onChange(eventSignal);
  }

  handleRemoveEventSignalChange = (index) => () => {
    var eventSignal = this.props.eventSignal;
    eventSignal.splice(index, 1)
    this.props.onChange(eventSignal);
  }




  render() {
    const {classes, hasError, eventSignal} = this.props;
    var that = this;
    return (
      <div>

        
      
        <Grid container
              style={ { marginTop: 20 } }
              spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 8 }>
            {eventSignal.map((signal, index) => {
              return (
                <div key={"signal_panel_"+index}>
                {(index !== 0) ? <Grid container
                      style={ { marginTop: 20, marginBottom: 20 } }
                      spacing={ 24 }>
                  <Grid item xs={ 2 } />
                  <Grid item xs={ 8 }>
                    <Divider />
                  </Grid>
                  <Grid item xs={ 2 } />
                </Grid> : null}

                <EventSignalPanel 
                  classes={classes} eventSignal={signal} hasError={hasError} 
                    onChange={that.handleEventSignalChange(index)}
                    onRemove={that.handleRemoveEventSignalChange(index)}
                    canBeRemoved={eventSignal.length >0}/>
                    
                </div>
            )
            })}
          </Grid>
          <Grid item xs={ 2 } />
        </Grid>

        <Grid container
              style={ { marginTop: 20 } }
              spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 8 }>
            <Divider />
          </Grid>
          <Grid item xs={ 2 } />
        </Grid>
        <Grid container
              style={ { marginTop: 20 } }
              spacing={ 24 }>
          <Grid item xs={ 2 } />
          <Grid item xs={ 8}>
            <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            className={ classes.button }
                            onClick={ this.handleAddSignalClick }>
                      <AddIcon />Add New Signal
                    </Button>
          </Grid>
          <Grid item xs={ 2 } />
        </Grid>

        
      </div>
    );
  }
}

export default EventCreateEventSignalStep;
