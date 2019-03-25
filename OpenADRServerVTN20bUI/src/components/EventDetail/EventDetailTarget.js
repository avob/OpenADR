import React from 'react';


import EventDetailHeader from './EventDetailHeader'
import Divider from '@material-ui/core/Divider';

import {EventTargetPanel} from '../common/EventTargetPanel'

import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

export class EventDetailTarget extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
   const {classes, event, copyTargets, group, editMode, ven} = this.props;
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

       <EventTargetPanel classes={classes} eventTarget={copyTargets} group={group} onChange={this.props.updateCopyTargets}
        ven={ven}
        onVenSuggestionsFetchRequested={this.props.onVenSuggestionsFetchRequested}
        onVenSuggestionsClearRequested={this.props.onVenSuggestionsClearRequested}
        onVenSuggestionsSelect={this.props.onVenSuggestionsSelect}/>

    </div>
    );
  }
}

export default EventDetailTarget;
