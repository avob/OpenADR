import React from 'react';

import Avatar from '@material-ui/core/Avatar';

import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import IconButton from '@material-ui/core/IconButton';

import CancelIcon from '@material-ui/icons/Cancel';


import ExtensionIcon from '@material-ui/icons/Extension';
import GroupWorkIcon from '@material-ui/icons/GroupWork';
import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import CalendarTodayIcon from '@material-ui/icons/CalendarToday';




import Divider from '@material-ui/core/Divider';

import Grid from '@material-ui/core/Grid';

import amber from '@material-ui/core/colors/amber';

import { history } from '../../store/configureStore';

import {formatTimestamp} from '../../utils/time'

import { VenActionDialog } from './VtnconfigurationDialog'


var icoSize = 35;
var innerIcoSize = 20;
var cardContentHeight = 60;

var VtnConfigurationCard = (props) => {
  return (
  <Card className={ props.classes.card }>
    <CardHeader className={ props.classes.media }
                subheader={ props.cardType }
                style={{margin:0, padding: 0, paddingLeft: 16, height: 30}}
                action={ <IconButton size="small"
                                     onClick={ props.close }>
                           <CancelIcon style={ { width: '20px', height: '20px'}}/>
                         </IconButton> } />
    <Divider variant="middle" />
    <CardActionArea onClick={ (e) => {
                                if ( props.edit ) props.edit( e )
                              } }>
      <CardContent style={ { minHeight: cardContentHeight, maxHeight: cardContentHeight,  marginBottom: 5 } }>
        
        <Grid container spacing={ 8 }>
          <Grid container
                >
            <Grid item xs={ 3 }>
              <Avatar style={ { backgroundColor: props.color, margin: '0px 0px', height: icoSize, width: icoSize } }>
                { props.icon }
              </Avatar>
            </Grid>
            <Grid item xs={ 9 } >
              <Typography gutterBottom
                          variant="title"
                          component="h1"
                          align="right"
                          style={{marginRight:0, marginTop: 0, fontSize: '1.5em'}}>
                { props.name }
              </Typography>
           
              <Typography component="p"
                          variant="caption"
                          align="right"
                          style={{marginRight:0}}>
                { props.description }
              </Typography>

         
              
            </Grid>
          </Grid>
        </Grid>
      </CardContent>
    </CardActionArea>
    <Divider variant="middle" />
    <CardActions style={ { minHeight: 40, maxHeight: 40 } }>
      { props.actions }
    </CardActions>
  </Card>

  );

}


export function VtnConfigurationMarketContextCard( props ) {
  var close = null;
  if ( props.handleDeleteMarketContext ) {
    close = props.handleDeleteMarketContext
  } else if ( props.handleRemoveVenMarketContext ) {
    close = props.handleRemoveVenMarketContext
  }
  var handleEventClick = () => {
    history.push( '/event', { filters: [{type:"MARKET_CONTEXT", value: props.context.name}] });
  }
  var handleVenClick = () => {
    history.push( '/ven', { filters: [{type:"MARKET_CONTEXT", value: props.context.name}] });
  }
  return (
  <VtnConfigurationCard classes={ props.classes }
                        color={ props.context.color }
                        name={ props.context.name }
                        description={ props.context.description }
                        close={ close }
                        edit={ props.handleEditMarketContext }
                        cardType={ "Market Context" }
                        icon={ <ExtensionIcon style={ { height: innerIcoSize, width: innerIcoSize } } /> }
                        actions={ [ <Button onClick={handleEventClick} key="action_marketcontext_events" size="small" color="primary"> Events </Button>
                        , <Button onClick={handleVenClick} key="action_marketcontext_vens" size="small" color="primary"> Vens </Button> ] } />
  );
}

export function VtnConfigurationGroupCard( props ) {
   this.state.dialogOpen=false;
  var close = null;
  if ( props.handleDeleteGroup ) {
    close = props.handleDeleteGroup
  } else if ( props.handleRemoveVenGroup ) {
    close = props.handleRemoveVenGroup
  }
  var handleVenClick = () => {
    history.push( '/ven', { filters: [{type:"GROUP", value: props.group.name}] });
  }

  var handleDialogOpen = () => {
    this.setState({dialogOpen: true})
  }
  return (
  <VtnConfigurationCard classes={ props.classes }
                        color="#bbb"
                        name={ props.group.name }
                        description={ props.group.description }
                        close={ close }
                        edit={ props.handleEditGroup }
                        cardType={ "Group" }
                        icon={ <GroupWorkIcon style={ { height: innerIcoSize, width: innerIcoSize } } /> }
                        actions={ [ <Button onClick={handleVenClick}  key="action_group_vens" size="small" color="primary"> Vens </Button> ] } />
  );
}


export class VtnConfigurationVenCard extends React.Component {
  state = {
    dialogOpen: false,
  };


  componentDidMount() {
  }

  handleDialogOpen = () => {
    this.setState({dialogOpen: true})
  }

  handleDialogClose = () => {
    this.setState({dialogOpen: false})
  }

  render() {
    const {classes, account_create} = this.props;
    const {value} = this.state;
    var handleEventClick = () => {
      history.push( '/event', { filters: [{type:"VEN", value: this.props.ven.username}] });
    }
    var color = (this.props.ven.registrationId != null) ? "green" : "#bbb";
    return (<span>
  <VtnConfigurationCard classes={ this.props.classes }
                        color={color}
                        name={ this.props.ven.commonName }
                        description={ this.props.ven.username }
                        close={ this.props.handleDeleteVen }
                        edit={ this.props.handleEditVen }
                        cardType={ "VEN" }
                        icon={ <SettingsInputComponentIcon style={ { height: innerIcoSize, width: innerIcoSize} } /> }
                        actions={ [ 
                           <Button onClick={handleEventClick}  key="action_group_vens" size="small" color="primary"> Events </Button> 
                           , <Button  key="action_vens" size="small" color="primary" onClick={this.handleDialogOpen}> Actions </Button> 
                           ] } />
                            <VenActionDialog open={ this.state.dialogOpen }
                                 close={ this.handleDialogClose }
                                 title="Ven actions" 
                                 ven={this.props.ven}
                                 venActions={this.props.venActions}/>
                                 </span>
  );
  }
 }

// export function VtnConfigurationVenCard( props ) {
//   var color = (props.ven.registrationId != null) ? "green" : "#bbb";
  // var handleEventClick = () => {
  //   history.push( '/event', { filters: [{type:"VEN", value: props.ven.username}] });
  // }

//   return (<span>
//   <VtnConfigurationCard classes={ props.classes }
//                         color={color}
//                         name={ props.ven.commonName }
//                         description={ props.ven.username }
//                         close={ props.handleDeleteVen }
//                         edit={ props.handleEditVen }
//                         cardType={ "VEN" }
//                         icon={ <SettingsInputComponentIcon style={ { height: innerIcoSize, width: innerIcoSize} } /> }
//                         actions={ [ 
//                            <Button onClick={handleEventClick}  key="action_group_vens" size="small" color="primary"> Events </Button> 
//                            , <Button  key="action_group_vens" size="small" color="primary" onClick={() => {
//                              console.log("panel ven dialog: "+props.ven.username)
//                            }}> Actions </Button> 
//                            ] } />
//                             <VenActionDialog open={ this.state.dialogOpen }
//                                  close={ this.handleDialogOpen }
//                                  title="Filter by Market Context" />
//                                  </span>
//   );
// }

export function VtnConfigurationEventCard( props ) {
 
  var color;
  switch(props.event.descriptor.state) {
    case "ACTIVE":
      color = "green";
      break;
    case "CANCELLED":
      color = "red";
      break;
    default:
      color = "#bbb";
      break;
  }

  if(!props.event.published) {
    color = amber[700];
  }

  var handleVenClick = () => {
    history.push( '/ven', { filters: [{type:"EVENT", value: props.event.id}] });
  }

  

  var start = formatTimestamp(props.event.activePeriod.start);

  return (
  <VtnConfigurationCard classes={ props.classes }
                        color={ color }
                        name={ props.event.descriptor.marketContext + ":" + props.event.id}
                        description={

                        <span>
                          { start.date + " " + start.time} | { props.event.activePeriod.duration } | { props.event.activePeriod.notificationDuration }
                           
                          
                        </span>
                        }
                        close={ props.handleDeleteEvent }
                        edit={ props.handleEditEvent }
                        cardType={ "EVENT" }
                        icon={ <CalendarTodayIcon style={ { height: innerIcoSize, width: innerIcoSize} } /> }
                        actions={ [ <Button onClick={handleVenClick} key="action_group_vens" size="small" color="primary"> VENS </Button> ] } />
  );
}
