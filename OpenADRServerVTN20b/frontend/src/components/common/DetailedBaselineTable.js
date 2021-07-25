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

const useRowStyles = makeStyles({
  root: {
    '& > *': {
      borderBottom: 'unset',
    },
  },
});

function Row(props) {
  const { row } = props;
  const [open, setOpen] = React.useState(false);
  const classes = useRowStyles();

  return (
      <React.Fragment>
        <TableRow key={row.baselineId+row.baselineName} className={classes.root}>              
          <TableCell>{row.baselineName}</TableCell>
          <TableCell><UnitPanel unit={row.itemBase}/></TableCell>
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
                    <Table size="small" aria-label="intervals">
                      <TableHead>
                        <TableRow>
                          <TableCell>Duration</TableCell>
                          <TableCell>Value</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {row.intervals && row.intervals.map(interval => (
                          <TableRow className={classes.root}>
                             <TableCell>{interval.duration}</TableCell>
                              <TableCell>{interval.value}</TableCell>
                           </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </Box>
                </Grid>
              </Grid>
            </Collapse>
          </TableCell>
        </TableRow>
      </React.Fragment>
   );
}

var DetailedBaselineTable = (props) => {
  const {classes, baseline} = props
  
  return (
    <div className={classes.root}>

    {baseline ? 
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Unit</TableCell>
            <TableCell />
          </TableRow>
        </TableHead>
        <TableBody>
          <Row row={baseline} />
        </TableBody>
      </Table>
      : null }
    </div>
  )

}

export default DetailedBaselineTable;

