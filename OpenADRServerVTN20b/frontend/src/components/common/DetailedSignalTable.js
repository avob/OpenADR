import React from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Toolbar from '@material-ui/core/Toolbar';
import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@material-ui/icons/KeyboardArrowUp';
import Box from '@material-ui/core/Box';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';


import UnitPanel from '../common/UnitPanel'
import DetailedIntervalTable from '../common/DetailedIntervalTable'



const useRowStyles = makeStyles({
  root: {
    '& > *': {
      borderBottom: 'unset',
    },
  },
});

function Row(props) {
  const { row, index } = props;
  const [open, setOpen] = React.useState(false);
  const classes = useRowStyles();
  const assets = row.targets && row.targets.filter(t => t.targetType === "ENDDEVICE_ASSET" );
  const endDeviceAsset = (assets && assets.length === 1) ? assets[0] : null;
  
  return (
      <React.Fragment>
              <TableRow key={index} className={classes.root}>
                
                <TableCell>{row.signalName}</TableCell>
                <TableCell>{row.signalType}</TableCell>
                <TableCell><UnitPanel unit={row.itemBase}/></TableCell>
                <TableCell>{endDeviceAsset.targetId}</TableCell>
                <TableCell>
                  <IconButton aria-label="expand row" size="small" onClick={() => setOpen(!open)}>
                    {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
                  </IconButton>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                  <Collapse in={open} timeout="auto" unmountOnExit>
                    <Grid container>
                      <Grid item xs={ 6 }>
                        <Box mx={2}>
                          <DetailedIntervalTable classes={classes} intervals={row.intervals}/>




                        </Box>
                      </Grid>
               
                    </Grid>

                    
                  </Collapse>
                </TableCell>
              </TableRow>
            </React.Fragment>
   );
}

var DetailedSignalTable = (props) => {
  const {classes, signal} = props
  
  return (
    <div className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Signal name</TableCell>
            <TableCell>Signal type</TableCell>
            <TableCell>Signal unit</TableCell>
            <TableCell>EndDeviceAsset</TableCell>
            <TableCell />
          </TableRow>
        </TableHead>
        <TableBody>
          {signal && signal.map((row, index) => (
              <Row key={index} row={row} index={index}/>
          ))}
        </TableBody>
      </Table>
    </div>
  )

}

export default DetailedSignalTable;

