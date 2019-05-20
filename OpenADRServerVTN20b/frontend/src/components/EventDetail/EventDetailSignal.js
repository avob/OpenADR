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
    const {classes, event, copySignals, editMode} = this.props;
    var that = this;
    var hasError = false;

    return (
    <div className={ classes.root } >
      <EventDetailHeader classes={classes} event={event} actions={<Grid container spacing={ 24 }>

        {(!event.published) ? <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handlePublishEventClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> PUBLISH
            </Button>
          </Grid> : null}
          

        {(editMode) ? <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={() => {this.props.updateEvent(false)}}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> UPDATE
            </Button>
          </Grid> : null}

        {(editMode) ? <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="secondary"
                    fullWidth={true}
                    size="small"
                    onClick={() => {this.props.updateEvent(true)}}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> UPDATE AND PUBLISH
            </Button>
          </Grid> : null}
      
      </Grid>}/>



       <Divider style={ { marginTop: '20px', marginBottom:20 } } />
       {copySignals.map((signal, index) => {
          return (
            <div key={"signal_panel_"+index}>
            {(index !==0) ? <Grid container
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
                canBeRemoved={copySignals.length >0}/>
                
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
