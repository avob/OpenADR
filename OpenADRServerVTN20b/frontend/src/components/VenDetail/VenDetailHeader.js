import React from 'react';






import { VtnConfigurationVenCard } from '../common/VtnConfigurationCard'







import Grid from '@material-ui/core/Grid';


import Typography from '@material-ui/core/Typography';



















import SnackbarContent from '@material-ui/core/SnackbarContent';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';



export class VenDetailHeader extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }


  render() {
    const {classes, ven} = this.props;


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


    var registrationPanel = null;
    var actionPanel = null;


    if ( ven.registrationId == null ) {
      registrationPanel = <DefaultSnackbar  message={ <Typography component="span" >
                               <strong>Not Registered</strong>
                             </Typography> } />

    } else {
      registrationPanel = <SuccessSnackbar  message={ <Typography component="span" style={ { color:"#fff" } }>
                               <strong>Registered</strong>
                             </Typography> } />
      actionPanel = this.props.actions;

    }

    return (
      <Grid container>
        <Grid container spacing={ 24 }>
          <Grid item lg={ 4 } md={ 6 }>
            <VtnConfigurationVenCard key={ 'ven_card_' }
                                     classes={ classes }
                                     ven={ ven } />
          </Grid>
          <Grid item lg={ 8 } md={ 6 }>


            <Grid container>
              <Grid container>
                <Grid item xs={ 4 }>
                  { registrationPanel }
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

export default VenDetailHeader;
