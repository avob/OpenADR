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

import UnitPanel from '../common/UnitPanel'

var TargetTable = (props) => {
  const {classes, targets} = props
  return (
    <div className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Type</TableCell>
            <TableCell>Target</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {targets && targets.map(row => (
            <TableRow>
              <TableCell>{row.targetType}</TableCell>
              <TableCell>{row.targetId}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )

}

export default TargetTable;