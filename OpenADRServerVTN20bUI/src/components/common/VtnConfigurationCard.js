import React from 'react';

import Avatar from '@material-ui/core/Avatar';

import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import CardHeader from '@material-ui/core/CardHeader';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import GridListTile from '@material-ui/core/GridListTile';

import IconButton from '@material-ui/core/IconButton';


import TextField from '@material-ui/core/TextField';
import CancelIcon from '@material-ui/icons/Cancel';


import ExtensionIcon from '@material-ui/icons/Extension';
import GroupWorkIcon from '@material-ui/icons/GroupWork';
import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';




import Divider from '@material-ui/core/Divider';

import Grid from '@material-ui/core/Grid';



import Paper from '@material-ui/core/Paper';

var VtnConfigurationCard = (props) => {
  return (
  <Card className={ props.classes.card } style={ { margin: 5 } }>
    <CardHeader className={ props.classes.media }
                subheader={ props.cardType }
                action={ <IconButton size="small"
                                     onClick={ props.close }
                                     style={ { padding: 0, margin: '5px 10px' } }>
                           <CancelIcon />
                         </IconButton> } />
    <Divider variant="middle" />
    <CardActionArea onClick={ (e) => {
                                if ( props.edit ) props.edit( e )
                              } }>
      <CardContent style={ { minHeight: 120, maxHeight: 120 } }>
        <Typography gutterBottom
                    variant="title"
                    component="h2"
                    align="center">
          { props.name }
        </Typography>
        <Grid container spacing={ 8 }>
          <Grid container
                item
                xs={ 12 }
                spacing={ 24 }>
            <Grid item xs={ 4 }>
              <Avatar style={ { backgroundColor: props.color, margin: '0px 10px', height: 50, width: 50 } }>
                { props.icon }
              </Avatar>
            </Grid>
            <Grid item xs={ 8 }>
              <Typography component="p"
                          variant="caption"
                          align="right">
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
  return (
  <VtnConfigurationCard classes={ props.classes }
                        color={ props.context.color }
                        name={ props.context.name }
                        description={ props.context.description }
                        close={ close }
                        edit={ props.handleEditMarketContext }
                        cardType={ "Market Context" }
                        icon={ <ExtensionIcon style={ { height: 30, width: 30 } } /> }
                        actions={ [ <Button key="action_marketcontext_events" size="small" color="primary"> Events </Button>, <Button key="action_marketcontext_vens" size="small" color="primary"> Vens </Button> ] } />
  );
}

export function VtnConfigurationGroupCard( props ) {
  var close = null;
  if ( props.handleDeleteGroup ) {
    close = props.handleDeleteGroup
  } else if ( props.handleRemoveVenGroup ) {
    close = props.handleRemoveVenGroup
  }
  return (
  <VtnConfigurationCard classes={ props.classes }
                        color="#bbb"
                        name={ props.group.name }
                        description={ props.group.description }
                        close={ close }
                        edit={ props.handleEditGroup }
                        cardType={ "Group" }
                        icon={ <GroupWorkIcon style={ { height: 30, width: 30 } } /> }
                        actions={ [ <Button key="action_group_vens" size="small" color="primary"> Vens </Button> ] } />
  );
}

export function VtnConfigurationVenCard( props ) {
  var color = (props.ven.registrationId != null) ? "green" : "#bbb";
  return (
  <VtnConfigurationCard classes={ props.classes }
                        color={color}
                        name={ props.ven.commonName }
                        description={ props.ven.username }
                        close={ props.handleDeleteVen }
                        edit={ props.handleEditVen }
                        cardType={ "VEN" }
                        icon={ <SettingsInputComponentIcon style={ { height: 30, width: 30} } /> }
                        actions={ [ <Button key="action_group_vens" size="small" color="primary"> Detail </Button> ] } />
  );
}
