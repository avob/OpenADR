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
import { Panel } from '../common/Structure'

import UnitPanel from '../common/UnitPanel'

var SignalTable = (props) => {
  const {classes, signal} = props
  return (
    <Panel title="Signals">
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Type</TableCell>
            <TableCell>Unit</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {signal && signal.map(row => (
            <TableRow key={row.signalName+row.signalType}>
              <TableCell>{row.signalName}</TableCell>
              <TableCell>{row.signalType}</TableCell>
              <TableCell><UnitPanel unit={row.itemBase}/></TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Panel>
  )

}

export default SignalTable;

