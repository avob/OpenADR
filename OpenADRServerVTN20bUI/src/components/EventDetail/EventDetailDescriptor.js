import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles} from '@material-ui/core/styles';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'



import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import SearchIcon from '@material-ui/icons/Search';


import SnackbarContent from '@material-ui/core/SnackbarContent';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import Typography from '@material-ui/core/Typography';

import EventDetailHeader from './EventDetailHeader'

import Grid from '@material-ui/core/Grid';


export class EventDetailDescriptor extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  handleActiveEventClick = () => {
    this.props.activeEvent(this.props.event.id)
  }
 
  handleCancelEventClick = () => {
    this.props.cancelEvent(this.props.event.id)
  }

  render() {
    const {classes, event} = this.props;
    console.log(event)
    return (
    <div className={ classes.root } >
      <EventDetailHeader classes={classes} event={event} actions={[
         <Grid  key="report_action_first_row" container spacing={ 24 }>
          <Grid item xs={ 3 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleActiveEventClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> ACTIVE AND PUBLISH
            </Button>
          </Grid>
          <Grid item xs={ 3 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="secondary"
                    fullWidth={true}
                    size="small"
                    onClick={this.handleCancelEventClick}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> CANCEL
            </Button>
          </Grid>
         
        </Grid>
      ]
      }/>
   

    </div>
    );
  }
}

export default EventDetailDescriptor;
