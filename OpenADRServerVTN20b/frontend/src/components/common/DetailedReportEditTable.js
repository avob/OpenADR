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
import Button from '@material-ui/core/Button';
import EnhancedAutocomplete from './EnhancedAutocomplete'

import UnitEditPanel from './UnitEditPanel'

var DetailedReportEditTable = (props) => {
  const {classes, reports, definition, filterUnit, onReportChange} = props
  var onAddReport = () => {
    reports.push({
             "reportName": "",
             "reportType": "",
             "readingType": "",
             "payloadBase": "",
             "itemBase": {
              itemDescription: "No Unit",
              itemUnits: "No Unit",
              siScaleCode: "none"
            }
        });
      onReportChange(reports);
  }

  var onUnitChange = (index) => {
    return (unit) => {
        reports[index].itemBase = unit;
        onReportChange(reports);
    }
  }

  var onReportNameChange = (index) => {
    return (reportName) => {
        reports[index].reportName = reportName;
        onReportChange(reports);
    }
  }

  var onReportTypeChange = (index) => {
    return (reportType) => {
        reports[index].reportType = reportType;
        onReportChange(reports);
    }
  }

  var onReadingTypeChange = (index) => {
    return (readingType) => {
        reports[index].readingType = readingType;
        onReportChange(reports);
    }
  }

  var onPayloadTypeChange = (index) => {
    return (payloadType) => {
        reports[index].payloadBase = payloadType;
        onReportChange(reports);
    }
  }

  var onRemoveReport = (index) => {
    var newReports = [];
    reports && reports.map((report, i) => {
      if(index != i) {
        newReports.push(report);
      }
    });
    onReportChange(newReports);
  }

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
            <TableCell>
              <Button key="btn_delete"
                    color="primary"
                    size="small"
                    onClick={ () => onAddReport() }>
              Add Report
            </Button>
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reports && reports.map((row, index) => (
            <TableRow key={index}>
              <TableCell>
                <EnhancedAutocomplete
                    creatable
                    options={definition.reportName}
                    value={row.reportName}
                    label = "Report Name"
                    onSelected={onReportNameChange(index)}
                  />
              </TableCell>
              <TableCell>
                <EnhancedAutocomplete
                    creatable
                    options={definition.reportType}
                    value={row.reportType}
                    label = "Report Type"
                    onSelected={onReportTypeChange(index)}
                  />
              </TableCell>
              <TableCell>
                <EnhancedAutocomplete
                    creatable
                    options={definition.readingType}
                    value={row.readingType}
                    label = "Reading Type"
                    onSelected={onReadingTypeChange(index)}
                  />
              </TableCell>
              <TableCell>
                <EnhancedAutocomplete
                    creatable
                    options={definition.payloadType}
                    value={row.payloadBase}
                    label = "Payload Type"
                    onSelected={onPayloadTypeChange(index)}
                  />
              </TableCell>
              <TableCell>

                <UnitEditPanel classes={classes} definition={definition} filterUnit={filterUnit} onUnitChange={onUnitChange(index)} unit={row.itemBase}/>
              </TableCell>
              <TableCell>
              
            {(reports.length > 1) && 
                  <Button key="btn_delete"
                          color="secondary"
                          size="small"
                          onClick={ () => onRemoveReport(index) }>
                    Remove Report
                  </Button>
              }
            </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Panel>
  )

}

export default DetailedReportEditTable;