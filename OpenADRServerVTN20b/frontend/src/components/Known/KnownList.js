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
import SignalTable from '../common/SignalTable'
import ReportTable from '../common/ReportTable'
import Paper from '@material-ui/core/Paper';
import { Panel, H1 } from '../common/Structure'

import { history } from '../../store/configureStore';

var KnownUnitTable = (props) => {
  const {classes, unit} = props
  return (
    <Panel title="Units">
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Description</TableCell>
            <TableCell>Units</TableCell>
            <TableCell>SiscaleCode</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {unit && unit.map(row => (
            <TableRow key={row.itemDescription+ row.itemUnits+ row.xmlType}>
              <TableCell>{row.itemDescription}</TableCell>
              <TableCell>{row.itemUnits}</TableCell>
              <TableCell>{row.siScaleCode}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      </Panel>
  )

}

export class KnownList extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/marketcontext/known/unit")
        break;
      case 1:
        history.push("/marketcontext/known/signal")
        break;
      case 2:
        history.push("/marketcontext/known/report")
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
    const {classes, unit, signal, report} = this.props;
    const {value} = this.state;
    return (
    <div className={ classes.root }>
    <Paper>
      <Toolbar>
        <div style={{flex: '0 0 auto'}}>
          <H1 value="Known Entities" />
        </div>
        <div style={{flex: '1 1 100%'}} />
        <div style={{ flex: '0 0 auto'}}>

        </div>
      </Toolbar>
      <Divider/>
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
      { value === 0 && <KnownUnitTable classes={classes} unit={unit}/>
                        }
      { value === 1 && <SignalTable classes={classes} signal={signal}/>
                       }
      { value === 2 && <ReportTable classes={classes} report={report}/>
           
                       }

       </Paper>
    </div>

    );
  }
}

export default KnownList;
