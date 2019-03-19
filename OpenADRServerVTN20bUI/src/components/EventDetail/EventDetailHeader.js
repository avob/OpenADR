import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles, MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

import { VtnConfigurationEventCard } from '../common/VtnConfigurationCard'

import Input from '@material-ui/core/Input';
import InputBase from '@material-ui/core/InputBase';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';


import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';


import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import GridListTileBar from '@material-ui/core/GridListTileBar';

import IconButton from '@material-ui/core/IconButton';

import StarBorderIcon from '@material-ui/icons/StarBorder';

import ChipInput from 'material-ui-chip-input'

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import SearchIcon from '@material-ui/icons/Search';


import SnackbarContent from '@material-ui/core/SnackbarContent';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';


export class EventDetailHeader extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }


  render() {
    const {classes, event} = this.props;


    var SuccessSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.success }
                       style={ { maxWidth: 'none', paddingTop:0, paddingBottom:0 } }
                       message={ <Grid container direction="row" alignItems="center">
                            <Grid item>
                              <DoneIcon style={ { width: 20, height: 20, marginRight: 20, color:"#fff" } }/>
                            </Grid>
                            <Grid item >
                               { props.message }
                            </Grid>
                          </Grid>} />
      );
    }

    var ErrorSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.error }
                       style={ { maxWidth: 'none', paddingTop:0, paddingBottom:0 } }
                       message={ <Grid container direction="row" alignItems="center">
                            <Grid item>
                              <DoneIcon style={ { width: 20, height: 20, marginRight: 20, color:"#fff" } }/>
                            </Grid>
                            <Grid item >
                               { props.message }
                            </Grid>
                          </Grid>} />
      );
    }

    var DefaultSnackbar = (props) => {
      return (
      <SnackbarContent className={ classes.success }
                       style={ { maxWidth: 'none', paddingTop:0, paddingBottom:0, backgroundColor:"#fafafa" } }
                       message={ 
                          <Grid container direction="row" alignItems="center">
                            <Grid item>
                              <CloseIcon style={ { width: 20, height: 20, marginRight: 20, color:"#000" } }/>
                            </Grid>
                            <Grid item>
                               { props.message }
                            </Grid>
                          </Grid>
                     } />
      );
    }


    var statePanel = null;
    var actionPanel = null;

    if ( event.state == "ACTIVE" ) {
      
      statePanel = <SuccessSnackbar  message={ <Typography component="span" style={ { color:"#fff" } }>
                               <strong>ACTIVE</strong>
                             </Typography> } />

    } else if ( event.state == "UNPUBLISHED" ) {
      statePanel = <DefaultSnackbar  message={ <Typography component="span" >
                               <strong>UNPUBLISHED</strong>
                             </Typography> } />

    } else {
      statePanel = <ErrorSnackbar  message={ <Typography component="span" style={ { color:"#fff" } }>
                               <strong>CANCELED</strong>
                             </Typography> } />
    }

    actionPanel = this.props.actions;

    return (
      <Grid container>
        <Grid container spacing={ 24 }>
          <Grid item xs={ 4 }>
            <VtnConfigurationEventCard key={ 'ven_card_' }
                                     classes={ classes }
                                     event={ event } />
          </Grid>
          <Grid item xs={ 8 }>


            <Grid container>
              <Grid container spacing={ 24 }>
                <Grid item xs={ 4 }>
                  { statePanel }
                </Grid>
                <Grid item xs={ 8 }>
                  <Typography gutterBottom
                              align="center"
                              variant="headline"
                              component="h3">
                   { event.eventId  }
                    
                  </Typography>
                </Grid>
              </Grid>
              {actionPanel}
            

            </Grid>
          </Grid>
        </Grid>
      </Grid>
    );
  }
}

export default EventDetailHeader;
