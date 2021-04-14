import React from 'react';









import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';




import EventDetailHeader from './EventDetailHeader'
import Divider from '@material-ui/core/Divider';


import Grid from '@material-ui/core/Grid';







import {EventSignalPanel} from '../common/EventSignalPanel'


export class EventDetailSignal extends React.Component {




  handleAddSignalClick = () => {
    this.props.addCopySignals();
  }

  handleEventSignalChange = (index) => (newSignal) => {
    this.props.updateCopySignals(index, newSignal);
  }

  handleRemoveEventSignalChange = (index) => () => {
    this.props.removeCopySignals(index);
  }

  handlePublishEventClick = () => {
    this.props.publishEvent(this.props.event.id);
  }


  render() {
    const {classes, event, editMode, group, ven} = this.props;
    var that = this;
    var hasError = false;

    return (
    <div className={ classes.root } >
       {event.signals && event.signals.map((signal, index) => {
          return (
            <div key={"signal_panel_"+index}>
            {(index !==0) ? <Grid container>
              <Grid item xs={ 12 }>
                <Divider />
              </Grid>
            </Grid> : null}

            <EventSignalPanel 
              classes={classes} eventSignal={signal} hasError={hasError} index={index}
                onChange={that.handleEventSignalChange(index)}
                onRemove={that.handleRemoveEventSignalChange(index)}
                canBeRemoved={event.signals.length >0}
                group={group} onChange={this.props.updateCopyTargets}
        ven={ven}
        onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
        onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
        onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}/>
                
            </div>
        )
        })}

    </div>
    );
  }
}

export default EventDetailSignal;
