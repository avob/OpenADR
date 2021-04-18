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

import { history } from '../../store/configureStore';

var KnownUnitTable = (props) => {
  const {classes, unit} = props
  return (
    <div className={classes.root}>
      <Toolbar
      className={classes.root}
    >
      <div className={classes.title} style={{ flex: 1 }}>
         <Typography variant="h6" id="tableTitle">
             Units
          </Typography>
      </div>
    </Toolbar>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Item description</TableCell>
            <TableCell>Item units</TableCell>
            <TableCell>Type</TableCell>
            <TableCell></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {unit && unit.map(row => (
            <TableRow key={row.itemDescription+ row.itemUnits+ row.xmlType}>
              <TableCell>{row.itemDescription}</TableCell>
              <TableCell>{row.itemUnits}</TableCell>
              <TableCell>{row.xmlType}</TableCell>

            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )

}

var KnownSignalTable = (props) => {
  const {classes, signal} = props
  return (
    <div className={classes.root}>
      <Toolbar
      className={classes.root}
    >
      <div className={classes.title} style={{ flex: 1 }}>
         <Typography variant="h6" id="tableTitle">
             Signals
          </Typography>
      </div>
    </Toolbar>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Signal name</TableCell>
            <TableCell>Signal type</TableCell>
            <TableCell>Signal unit</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {signal && signal.map(row => (
            <TableRow key={row.signalName+row.signalType}>
              <TableCell>{row.signalName}</TableCell>
              <TableCell>{row.signalType}</TableCell>
              <TableCell>{row.units && row.units.map(n => {return <div key={n.itemDescription + "-" + n.itemUnits}>{n.itemDescription + "-" + n.itemUnits}</div>})}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )

}

var KnownReportTable = (props) => {
  const {classes, report} = props
  console.log(report)
  return (
    <div className={classes.root}>
      <Toolbar
      className={classes.root}
    >
      <div className={classes.title} style={{ flex: 1 }}>
         <Typography variant="h6" id="tableTitle">
             Reports
          </Typography>
      </div>
    </Toolbar>
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
              <TableCell>{row.units && row.units.map(n => {return <div key={n.itemDescription + "-" + n.itemUnits}>{n.itemDescription + "-" + n.itemUnits}</div>})}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )

}

export class VtnConfigurationKnown extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/vtn_configuration/known/unit")
        break;
      case 1:
        history.push("/vtn_configuration/known/signal")
        break;
      case 2:
        history.push("/vtn_configuration/known/report")
        break;
      default:
        break;
    }
  };


  componentDidMount() {
    switch(this.props.panel){
      case "unit":
        this.setState({value:0});
        break;
      case "signal":
        this.setState({value:1});
        break;
      case "report":
        this.setState({value:2});
        break;
      default:
        break;
    }
  }


  render() {
    const {classes, known} = this.props;
    const {value} = this.state;
    console.log(known)
    return (
    <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Units" />
        <Tab label="Signals" />
        <Tab label="Reports" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <KnownUnitTable classes={classes} unit={known.unit}/>
                        }
      { value === 1 && <KnownSignalTable classes={classes} signal={known.signal}/>
                       }
      { value === 2 && <KnownReportTable classes={classes} report={known.report}/>
                       }
    </div>

    );
  }
}

export default VtnConfigurationKnown;
