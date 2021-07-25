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

import TextField from '@material-ui/core/TextField';


var DetailedTargetTable = (props) => {
  const {classes, targets} = props
  return (
      <Table className={classes.table}>
        <TableHead>
          <TableRow key={"header"}>
            <TableCell>Type</TableCell>
            <TableCell>Target</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {targets && targets.map((row, index) => (
            <TableRow key={index}>
              <TableCell>
                <TextField required
                      label="Target type"
                      value={row.targetType}
                      InputLabelProps={{
                        shrink: true,
                      }}    
                    />
              </TableCell>
              <TableCell>
                <TextField required
                      label="Target id"
                      value={row.targetId}
                      InputLabelProps={{
                        shrink: true,
                      }}    
                    />

              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
  )

}

export default DetailedTargetTable;