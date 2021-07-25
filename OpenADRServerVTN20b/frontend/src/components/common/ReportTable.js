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

var ReportTable = (props) => {
  const {classes, report} = props
  return (
    <Panel title="Reports">
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Report name</TableCell>
            <TableCell>Report type</TableCell>
            <TableCell>Reading type</TableCell>
            <TableCell>Payload</TableCell>
            <TableCell>Report unit</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {report && report.map(row => (
            <TableRow key={row.reportName+row.reportType}>
              <TableCell>{row.reportName}</TableCell>
              <TableCell>{row.reportType}</TableCell>
              <TableCell>{row.readingType}</TableCell>
              <TableCell>{row.payloadBase}</TableCell>
              <TableCell><UnitPanel unit={row.itemBase}/></TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Panel>
  )

}

export default ReportTable;