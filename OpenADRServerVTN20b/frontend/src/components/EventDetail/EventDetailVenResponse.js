import React from 'react';


import EventDetailHeader from './EventDetailHeader'
import Divider from '@material-ui/core/Divider';

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';


import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

import {formatTimestamp} from '../../utils/time'



var EventVenResponseTable = (props) => {
  const {classes} = props;
  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell align="right">Ven ID</TableCell>
            <TableCell align="right">Last Modification Number</TableCell>
            <TableCell align="right">Last Update</TableCell>
            <TableCell align="right">Opt</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.venResponse.map( (row, index) => {
            var lastUpdateDatetime = formatTimestamp(row.lastUpdateDatetime);
            return (

            <TableRow key={index}>
              <TableCell scope="row" align="right">{row.venId}</TableCell>
              <TableCell scope="row" align="right">{row.lastSentModificationNumber}</TableCell>
              <TableCell scope="row" align="right">{lastUpdateDatetime.date + " " +lastUpdateDatetime.time + " " + lastUpdateDatetime.tz}</TableCell>
              <TableCell scope="row" align="right">{row.venOpt}</TableCell>
            </TableRow>
          )
          })}
        </TableBody>
      </Table>
    </Paper>
  );
}

export class EventDetailVenResponse extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
   const {classes, event, venResponse} = this.props;

    return (
    <div className={ classes.root } >
      <EventDetailHeader classes={classes} event={event} actions={<Grid container spacing={ 24 }>

         <Grid item xs={ 4 }>
            <Button key="btn_create"
                    style={ { marginTop: 15 } }
                    variant="outlined"
                    color="primary"
                    fullWidth={true}
                    size="small"
                    onClick={this.props.refreshVenResponse}>
              <CloudDownloadIcon style={ { marginRight: 15 } }/> REFRESH
            </Button>
          </Grid>
          

      
      
      </Grid>}/>
       <Divider style={ { marginTop: '20px', marginBottom:20 } } />

       <EventVenResponseTable classes={classes} venResponse={venResponse}/>


    </div>
    );
  }
}

export default EventDetailVenResponse;
